#!/bin/bash
# This is the main script the start and stop all services.
#     author: Lex Xie
#     http://www.apache.org/licenses/LICENSE-2.0

LOG_FILE="/mnt/logs/$1_console.log"
PID_FILE="/mnt/pids/$1_pid.file"

function error() {
	echo "$1"
	exit -1
}

if [[ $# != 2 ]]; then
	error "Usage: $0 <service-name> <start | stop | restart | log | status>"
fi

BASEDIR=$(dirname "$BASH_SOURCE")
echo "Script location     - $BASEDIR"
cd $BASEDIR

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
        CD_PATH="${array[0]}/$1"
        JAVAS="-Xms500m -Xmx1500m"
        APP_ID="${array[0]}-server"
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
