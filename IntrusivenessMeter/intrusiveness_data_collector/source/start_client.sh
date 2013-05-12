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

# FIXME it should be read from a conf file
RESULTS_DIRECTORY="results"

# FIXME hard coded
SYSTEM_RESOURCE_COLLECTOR="source/system_resource_usage_collector.sh"
# FIXME it should be read from a conf file
DEVICE_TO_MONITOR="/dev/sda7"
# FIXME it should be read from a conf file
SYSTEM_RESOURCE_BASE_OUTPUT_FILENAME="monitoring"

HADOOP_AWARE_COLLECTOR="source/hadoop_aware_collector.py"

# FIXME hard coded
TASK_TRACKER_DAEMON="source/task_tracker_start_daemon.sh"

WHOLE_SYSTEM_PROCESS_DAEMON="source/whole_system_processes_monitor.sh"

./$SYSTEM_RESOURCE_COLLECTOR $SYSTEM_RESOURCE_BASE_OUTPUT_FILENAME $RESULTS_DIRECTORY $DEVICE_TO_MONITOR &

python $HADOOP_AWARE_COLLECTOR $RESULTS_DIRECTORY &

./$TASK_TRACKER_DAEMON &

# FIXME hard coded
./$WHOLE_SYSTEM_PROCESS_DAEMON $RESULTS_DIRECTORY 1 &
