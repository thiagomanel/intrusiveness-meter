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

# 2 hours
WAIT_TIME=7200

function start_task_tracker
{
	$HADOOP_HOME/bin/start-mapred.sh
}

while [ true ]; do
	start_task_tracker
	sleep $WAIT_TIME
done
