#!/bin/bash

INTRUSIVENESS_METER_HOME="$INTRUSIVENESS_METER_HOME"
SOURCE_DIRECTORY="$INTRUSIVENESS_METER_HOME/source"
ACTIVITY_MONITOR_SCRIPT="$SOURCE_DIRECTORY/activity_monitor.sh"

WAIT_TIME_BETWEEN_KILLS=30

while [ true ]; do
	PID="`ps xau | grep activity_monitor.sh | grep -v grep | awk '{ print $2 }'`"
	echo $PID

	if [ $PID ]; then
		kill $PID
	fi
	sleep 10
	$ACTIVITY_MONITOR_SCRIPT 10 &
	sleep $WAIT_TIME_BETWEEN_KILLS	
done
