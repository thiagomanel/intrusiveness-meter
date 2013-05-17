#!/bin/bash -e

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Intrusiveness Meter Client Starter
#
# This script starts all the programs of the Intrusiveness Meter Client
#
# It loads the client properties from the conf file "client_configuration"
# The configuration file must be structured as follow:
# 
# DEVICE_TO_MONITOR=[property value]
# SYSTEM_RESOURCE_BASE_OUTPUT_FILENAME=[property value]
# WHOLE_SYSTEM_PROCESS_DAEMON_WAIT_TIME=[property value]
#

INTRUSIVENESS_METER_HOME=$INTRUSIVENESS_METER_HOME

RESULTS_DIRECTORY="$INTRUSIVENESS_METER_HOME/results"
CONF_DIRECTORY="$INTRUSIVENESS_METER_HOME/conf"
CONFIGURATION_FILE="$CONF_DIRECTORY/client_configuration"

DEVICE_TO_MONITOR=""
SYSTEM_RESOURCE_BASE_OUTPUT_FILENAME=""
WHOLE_SYSTEM_PROCESS_DAEMON_WAIT_TIME=""

SYSTEM_RESOURCE_COLLECTOR="$INTRUSIVENESS_METER_HOME/source/system_resource_usage_collector.sh"
HADOOP_AWARE_COLLECTOR="$INTRUSIVENESS_METER_HOME/source/hadoop_aware_collector.py"
TASK_TRACKER_DAEMON="$INTRUSIVENESS_METER_HOME/source/task_tracker_start_daemon.sh"
WHOLE_SYSTEM_PROCESS_DAEMON="$INTRUSIVENESS_METER_HOME/source/whole_system_processes_monitor.sh"

SYSTEM_RESOURCE_COLLECTOR_ERROR_FILE="$INTRUSIVENESS_METER_HOME/logs/system_resource_usage_collector.error"
HADOOP_AWARE_COLLECTOR_ERROR_FILE="$INTRUSIVENESS_METER_HOME/logs/hadoop_aware_collector.error"
TASK_TRACKER_DAEMON_ERROR_FILE="$INTRUSIVENESS_METER_HOME/logs/task_tracker_start_daemon.error"
WHOLE_SYSTEM_PROCESS_DAEMON_ERROR_FILE="$INTRUSIVENESS_METER_HOME/logs/whole_system_processes_monitor.error"

# debug configuration
DEBUG=true
DEBUG_FILE_NAME="$INTRUSIVENESS_METER_HOME/logs/client_starter.log"

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

function read_configuration
{
	CONFIGURATION_CONTENT=`cat $CONFIGURATION_FILE | grep -v "#"`
        debug "loaded configuration = $CONFIGURATION_CONTENT"
        CONTENT=($CONFIGURATION_CONTENT)

        # client configuration
	DEVICE_TO_MONITOR="`echo ${CONTENT[0]} | cut -d = -f2-`"
        SYSTEM_RESOURCE_BASE_OUTPUT_FILENAME="`echo ${CONTENT[1]} | cut -d = -f2-`"
        WHOLE_SYSTEM_PROCESS_DAEMON_WAIT_TIME="`echo ${CONTENT[2]} | cut -d = -f2-`"

	debug "device to monitor = $DEVICE_TO_MONITOR"
	debug "system resource base output filename = $SYSTEM_RESOURCE_BASE_OUTPUT_FILENAME"
	debug "whole system process daemon wait time = $WHOLE_SYSTEM_PROCESS_DAEMON_WAIT_TIME"
}

function start_client_daemons
{
	debug "Starting system resource usage monitor"
	$SYSTEM_RESOURCE_COLLECTOR $SYSTEM_RESOURCE_BASE_OUTPUT_FILENAME $RESULTS_DIRECTORY $DEVICE_TO_MONITOR 2> $SYSTEM_RESOURCE_COLLECTOR_ERROR_FILE &
	debug "Started system resource usage monitor"

	debug "Starting hadoop aware daemon"
	python $HADOOP_AWARE_COLLECTOR $RESULTS_DIRECTORY 2> $HADOOP_AWARE_COLLECTOR_ERROR_FILE &
	debug "Started hadoop aware daemon"	

	debug "Starting Task Tracker starter daemon"
	$TASK_TRACKER_DAEMON 2> $TASK_TRACKER_DAEMON_ERROR_FILE &
	debug "Started Task Tracker starter daemon"	

	debug "Starting whole system process monitor"
	$WHOLE_SYSTEM_PROCESS_DAEMON $RESULTS_DIRECTORY $WHOLE_SYSTEM_PROCESS_DAEMON_WAIT_TIME 2> $WHOLE_SYSTEM_PROCESS_DAEMON_ERROR_FILE &
	debug "Started whole system process monitor"
}

#
# Main
#

debug_startup

debug "Reading configuration"
read_configuration

debug "Starting daemons"
echo "Starting daemons"
start_client_daemons
echo "Started daemons" 
debug "Started daemons"
