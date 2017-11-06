#!/bin/bash
# This is the main script the start and stop all services.
#     author: Lex Xie
#     http://www.apache.org/licenses/LICENSE-2.0

function error() {
	echo "$1"
	exit -1
}

if [[ $# != 2 ]]; then
	error "Usage: $0 <service-name> <start | stop | restart>"
fi

BASEDIR=$(dirname "$BASH_SOURCE")
echo "Script location     - $BASEDIR"
cd $BASEDIR

case $1 in
	"eureka" )
        CD_PATH="eureka-service"
        JAVAS="-Xms500m -Xmx500m"
		;;
	"config" )
        CD_PATH="config-service"
        JAVAS="-Xms500m -Xmx500m"
		;;
	"gateway" )
        CD_PATH="gateway"
        JAVAS="-Xms600m -Xmx600m"
		;;
	* )
        IFS='-' read -r -a array <<< "$1"
        CD_PATH="${array[0]}/$1"
        JAVAS="-Xms500m -Xmx1500m"
		;;
esac

echo "Executable location - $CD_PATH/target"
cd "$CD_PATH/target" || error "Invalid service name."

file=`ls $1*.jar.original 2>/dev/null`
if [[ -e "$file" ]]; then
	file="${file%.original}"
else
	error "Executable jar not found."
fi

PID_FILE="pid.file"

function start() {
	echo "Start with jar file: $file"
	nohup java $JAVAS -jar $file > console.log 2>&1 &
	echo -n $! > "$PID_FILE"
	echo "service started with PID `cat $PID_FILE`."
}

function seelog() {
	clear
	tail -n 200 -f console.log
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
	restart )
		stop
		sleep 10
		start
		;;
	* )
		error "Invalid parameter - $2"
		;;
esac
