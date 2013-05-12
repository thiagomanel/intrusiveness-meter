#!/bin/bash -e

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Whole System Processes Monitor
#
# This script logs all the running processes.
#
# Usage:
# bash whole_system_processes_monitor.sh RESULTS_DIRECTORY WAIT_TIME
# 
# Parameters:
# RESULTS_DIRECTORY: the directory where the log will be put.
# WAIT_TIME: time between the logging actions.
#

RESULTS_DIRECTORY=$1
RESULTS_FILE="$RESULTS_DIRECTORY/system_processes"
WAIT_TIME=$2

touch $RESULTS_FILE

while [ "1" = "1" ]; do
	TIME="`source/time.sh`"
	PROCESSES="`ps xau`"
	echo "---------------------" >> $RESULTS_FILE
	echo "---------------------" >> $RESULTS_FILE
	echo "$TIME" >> $RESULTS_FILE
	echo "---------------------" >> $RESULTS_FILE
	echo "$PROCESSES" >> $RESULTS_FILE
	echo >> $RESULTS_FILE

	sleep $WAIT_TIME
done
