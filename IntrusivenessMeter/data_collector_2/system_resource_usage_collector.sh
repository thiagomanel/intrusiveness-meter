#!/bin/bash

NUMBER_OF_PROCESSORS=$1

LIMIT=`expr $NUMBER_OF_PROCESSORS - 1`

for (( c=0; c<$NUMBER_OF_PROCESSORS; c++ )); do
	MPSTAT_INFO="`mpstat -P $c | sed 1,3d`"
	echo $MPSTAT_INFO
done


