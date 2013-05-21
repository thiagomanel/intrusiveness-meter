#!/bin/bash -e

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Hadoop Task Tracker Starter Daemon
#
# This program is used to start periodically the TaskTracker in the
# user's machine (as it's killed each time the user reports discomfort).
# 
# Usage:
# 
# bash task_tracker_start_daemon.sh WAIT_TIME
#
# Parameters:
# WAIT_TIME: the between the attempts of starting the Hadoop Task Tracker
#

WAIT_TIME=$1

# debug configuration
DEBUG=true
DEBUG_FILE_NAME="$INTRUSIVENESS_METER_HOME/logs/task_tracker_starter.log"

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

function start_task_tracker
{
	$HADOOP_HOME/bin/start-mapred.sh
}

while [ true ]; do
	debug "Starting TaskTracker"        
	start_task_tracker
        debug "Started TaskTracker. Going sleep."
	sleep $WAIT_TIME
done
