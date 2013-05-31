#!/bin/bash

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Client Easy-Starter
#
# This script starts the Hadoop daemons and the 
# Intrusiveness Meter daemons and should be used by
# the machine users to start the environment after
# restarting the machine. 
#

INTRUSIVENESS_METER_HOME="$INTRUSIVENESS_METER_HOME"
HADOOP_HOME="$HADOOP_HOME"

$HADOOP_HOME/bin/start-dfs.sh
$HADOOP_HOME/bin/start-mapred.sh

$INTRUSIVENESS_METER_HOME/source/start_client.sh
