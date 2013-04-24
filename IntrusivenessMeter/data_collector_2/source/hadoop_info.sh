#!/bin/bash

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Hadoop Information Facade
# 
# This program provides information on the Hadoop installation
#

OPTION=$1

HADOOP="$HADOOP_HOME/bin/hadoop"

function hadoop_jobs
{
	echo "`$HADOOP job -list | tail -n 1 | awk '{ print $1 }'`"
}


if [ $OPTION = "-r" ]; then
	JOBS="$(hadoop_jobs)"

	# dependant on the hadoop output
	if [ $JOBS = "JobId" ]; then
		echo -n "false"
	else
		echo -n "true"
	fi
fi
