#!/bin/bash -e

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# User Activity Monitor
#
# This script calls the xprintidle to get the user's idle time
# and prints the information in a file.
#
# usage:
#
# bash activity_monitor.sh SLEEP_TIME
#
# Parameters:
# SLEEP_TIME The time between calls to xprintidle
#

INTRUSIVENESS_METER_HOME="$INTRUSIVENESS_METER_HOME"

RESULTS_DIRECTORY="$INTRUSIVENESS_METER_HOME/results"
LOGS_DIRECTORY="$INTRUSIVENESS_METER_HOME/logs"

ACTIVITY_LOG_FILE="$RESULTS_DIRECTORY/user_activity.log"
DEBUG_FILE="$LOGS_DIRECTORY/activity_monitor.log"

# debug configuration
DEBUG=true
DEBUG_FILE_NAME="$LOGS_DIRECTORY/activity_monitor.log"

SLEEP_TIME=$1

function debug_startup
{
        if [ $DEBUG ]; then
                touch $DEBUG_FILE_NAME
        fi
}

function debug
{
        if [ $DEBUG ]; then
                echo -n "`date "+%d-%m-%Y-%H-%M-%S"`    " >> $DEBUG_FILE_NAME
                # TODO if the log file is too big, it must truncate to 0
                # or do something so the file does not grow without limit.
                echo $1 >> $DEBUG_FILE_NAME
        fi
}

while [ true ]; do
	debug "Collecting user activity data"
	echo -n "`date "+%d-%m-%Y-%H-%M-%S"`    " >> $ACTIVITY_LOG_FILE
	xprintidle >> $ACTIVITY_LOG_FILE
	debug "Collected user activity data"
	
	debug "Going sleep"
	sleep $SLEEP_TIME
	debug "Woke up"
done
