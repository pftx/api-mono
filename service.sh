#!/bin/bash
# This is the main script the start and stop all services.
#     author: Lex Xie
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Usage:
#     1. Install artifacts to lib by: 
#          ./service.sh install
#     2. Manage all the services:
#          ./service.sh <service-name> <start | stop | restart | log | status>
#
###############################################################################

LOG_FILE="/mnt/logs/$1_console.log"
PID_FILE="/mnt/pids/$1_pid.file"
INSTALL_DIR="/mnt/lib/api-mono"

function error() {
	echo "$1"
	exit -1
}

function install() {
	DIR="$1/target"
	if [ -d $DIR ]; then
		file=`ls $DIR/*.jar.original 2>/dev/null`
		if [[ -e "$file" ]]; then
			file="${file%.original}"
			echo "Executable location - $file"
			filename=$(basename "$file")
			cp ./$file $INSTALL_DIR/$filename
		fi
		return 0
	else
		return 1
	fi
}

if [ $1 == "install" ]; then
	echo "   XXX              - Clean the current installed artifacts..."
	rm $INSTALL_DIR/*
	
	BASEDIR=$(dirname "$BASH_SOURCE")
	echo "Script location     - $BASEDIR"
	cd $BASEDIR
	
	for i in $(ls -d */); do
		part=${i%%/}
		install $part
		if [ $? == 1 ]; then
			for i in $(ls -d $part/*/); do
				part2=${i%%/}
				install $part2
			done
		fi
	done
	echo "   XXX              - Installation done. Result:"
	ls -al $INSTALL_DIR
	cd -
	exit 0
fi

if [[ $# != 2 ]]; then
	error "Usage: $0 <service-name> <start | stop | restart | log | status>"
fi

case $1 in
	"eureka" )
        CD_PATH="eureka-service"
        JAVAS="-Xms500m -Xmx500m"
        PORT=8022
		;;
	"config" )
        CD_PATH="config-service"
        JAVAS="-Xms500m -Xmx500m"
        PORT=8011
		;;
	"gateway" )
        CD_PATH="gateway"
        JAVAS="-Xms600m -Xmx600m"
        PORT=8088
		;;
	* )
        IFS='-' read -r -a array <<< "$1"
        CD_PATH="$1"
        JAVAS="-Xms500m -Xmx1500m"
        APP_ID="${array[0]}-server"
		;;
esac

file=`ls $INSTALL_DIR/$CD_PATH*.jar 2>/dev/null`
if [[ -e "$file" ]]; then
	echo "Executable jar - $file"
else
	error "Executable jar not found."
fi

function start() {
	echo "Start with jar file: $file"
	nohup java $JAVAS -jar $file > $LOG_FILE 2>&1 &
	echo -n $! > "$PID_FILE"
	echo "service started with PID `cat $PID_FILE`."
}

function seelog() {
	clear
	tail -n 200 -f $LOG_FILE
}

function checkstatus() {
	if test -z "$PORT"; then
		curl "http://localhost:8022/eureka/apps/$APP_ID"
	else
		curl "http://localhost:$PORT/monitor/info"
	fi
}

function stop() {
	if [[ -e "$PID_FILE" ]]; then
		if kill `cat $PID_FILE` > /dev/null; then
			rm $PID_FILE
			echo "service stoped."
		fi
	else
		error "$PID_FILE not found, can not stop the service."
	fi
}

case $2 in
	start )
		start
		;;
	stop )
		stop
		;;
	log )
		seelog
		;;
	status )
		checkstatus
		;;
	restart )
		stop
		sleep 10
		start
		;;
	* )
		error "Invalid parameter - $2"
		;;
esac
