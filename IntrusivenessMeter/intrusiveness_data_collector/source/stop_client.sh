#!/bin/bash

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Intrusiveness Meter Client Stopper
#
# This script stops all the programs of the Intrusiveness Meter Client
#

function stop_system_resource_collector
{
	PID="`ps xau | grep system_resource_usage_collector.sh | awk '{ print $2}'`"
	kill $PID
}

function stop_hadoop_aware_collector
{
	PID="`ps xau | grep hadoop_aware_collector.py | awk '{ print $2}'`"
	kill $PID
}

function stop_task_tracker_daemon
{
	PID="`ps xau | grep task_tracker_start_daemon.sh | awk '{ print $2}'`"
	kill $PID
}

stop_system_resource_collector
stop_hadoop_aware_collector
stop_task_tracker_daemon
