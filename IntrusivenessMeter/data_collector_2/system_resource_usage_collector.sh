#!/bin/bash -e

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

CPU_IDLE=0
MEMORY_USED=0

#
# TODO code that may be used to get specific cpu data
#
#NUMBER_OF_PROCESSORS=$1

#LIMIT=`expr $NUMBER_OF_PROCESSORS - 1`

#for (( c=0; c<$NUMBER_OF_PROCESSORS; c++ )); do
#	MPSTAT_INFO="`mpstat -P $c | sed 1,3d`"
#	echo $MPSTAT_INFO
#done

function get_system_data
{
	sar_data=`sar -u -r 1 1 | grep "Average" | sed 1d | sed 2d`
	CPU_IDLE=`echo $sar_data | awk '{ print $8 }'`
	MEMORY_USED=`echo $sar_data | awk '{ print $12 }'`
}

get_system_data

echo $CPU_IDLE
echo $MEMORY_USED
