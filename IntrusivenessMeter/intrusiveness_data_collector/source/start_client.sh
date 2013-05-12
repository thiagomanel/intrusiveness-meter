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

INTRUSIVENESS_METER_HOME=$INTRUSIVENESS_METER_HOME

RESULTS_DIRECTORY="$INTRUSIVENESS_METER_HOME/results"

SYSTEM_RESOURCE_COLLECTOR="$INTRUSIVENESS_METER_HOME/source/system_resource_usage_collector.sh"
# FIXME it should be read from a conf file
DEVICE_TO_MONITOR="/dev/sda7"
# FIXME it should be read from a conf file
SYSTEM_RESOURCE_BASE_OUTPUT_FILENAME="monitoring"
# FIXME it should be read from a conf file
WHOLE_SYSTEM_PROCESS_DAEMON_WAIT_TIME=1

HADOOP_AWARE_COLLECTOR="$INTRUSIVENESS_METER_HOME/source/hadoop_aware_collector.py"

TASK_TRACKER_DAEMON="$INTRUSIVENESS_METER_HOME/source/task_tracker_start_daemon.sh"

WHOLE_SYSTEM_PROCESS_DAEMON="$INTRUSIVENESS_METER_HOME/source/whole_system_processes_monitor.sh"

$SYSTEM_RESOURCE_COLLECTOR $SYSTEM_RESOURCE_BASE_OUTPUT_FILENAME $RESULTS_DIRECTORY $DEVICE_TO_MONITOR &

python $HADOOP_AWARE_COLLECTOR $RESULTS_DIRECTORY &

$TASK_TRACKER_DAEMON &

$WHOLE_SYSTEM_PROCESS_DAEMON $RESULTS_DIRECTORY $WHOLE_SYSTEM_PROCESS_DAEMON_WAIT_TIME &
