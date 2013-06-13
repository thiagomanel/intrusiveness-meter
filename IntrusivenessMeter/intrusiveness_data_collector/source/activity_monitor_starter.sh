#!/bin/bash

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# User Activity Monitor Starter
#
# This script periodically kills the user activity monitor 
# and starts it. It's used because the monitor stops mysteriously.
#

INTRUSIVENESS_METER_HOME="$INTRUSIVENESS_METER_HOME"
SOURCE_DIRECTORY="$INTRUSIVENESS_METER_HOME/source"
LOGS_DIRECTORY="$INTRUSIVENESS_METER_HOME/logs"
ACTIVITY_MONITOR_SCRIPT="$SOURCE_DIRECTORY/activity_monitor.sh"
ACTIVITY_MONITOR_ERROR_LOG="$LOGS_DIRECTORY/activity_monitor.error"

WAIT_TIME_BETWEEN_KILLS=30

while [ true ]; do
	PID="`ps xau | grep activity_monitor.sh | grep -v grep | awk '{ print $2 }'`"
	echo $PID

	if [ $PID ]; then
		kill $PID
	fi
	
	sleep 10
	$ACTIVITY_MONITOR_SCRIPT 10 2> $ACTIVITY_MONITOR_ERROR_LOG &
	sleep $WAIT_TIME_BETWEEN_KILLS	
done
