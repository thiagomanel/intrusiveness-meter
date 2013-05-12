#!/bin/bash

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Intrusiveness Meter Client's monitor
#
# This script provides information on the client's health, or which 
# scripts are running and which are not running.
#

CONF_DIRECTORY="conf"
CLIENTS_FILE="$CONF_DIRECTORY/clients"

CLIENTS="`cat $CLIENTS_FILE`"

for HOST in $CLIENTS; do
	echo "---------------------"
	echo $HOST
	echo "Info on hadoop aware script"
	ssh $HOST "ps xau | grep python | grep hadoop_aware_collector.py | grep -v grep"
	echo "Info on system resource usage collector"
	ssh $HOST "ps xau | grep bash | grep system_resource_usage_collector.sh | grep -v grep"
	echo "Info on task tracker start daemon"
	ssh $HOST "ps xau | grep bash | grep task_tracker_start_daemon.sh | grep -v grep"
	echo "---------------------"
done
