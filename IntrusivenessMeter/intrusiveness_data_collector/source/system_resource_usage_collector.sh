#!/bin/bash -e

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Whole System Resouces Usage Data Collector
# 
# This program collects data about CPU, memory and disk usages of the system
# usage: 
# bash system_resource_usage_collector.sh OUTPUT_BASE_FILENAME OUTPUT_DIRECTORY DEVICE_NAME
#
# Parameters:
# OUTPUT_BASE_FILENAME : this radical is used to construct the output file names. 
# The program creates three files, one for idle CPU information, other for 
# user CPU information and another for memory information. 
# If OUTPUT_BASE_FILENAME is "aaaa", the created files are aaaa_system.idlecpu 
# aaaa_system.usercpu and aaaa.mem
# OUTPUT_DIRECTORY : the directory where the result files will be placed.
# If it does not exist, it will be created.
# DEVICE_NAME : device to monitor
#


#
# FIXME Error Handling
#

BASE_OUTPUT_FILENAME=$1
OUTPUT_DIRECTORY=$2
DEVICE_TO_MONITOR=$3

INTRUSIVENESS_METER_HOME=$INTRUSIVENESS_METER_HOME

CPU_IDLE_FILENAME=$OUTPUT_DIRECTORY/$BASE_OUTPUT_FILENAME"_system.idlecpu"
CPU_USER_FILENAME=$OUTPUT_DIRECTORY/$BASE_OUTPUT_FILENAME"_system.usercpu"
MEMORY_USAGE_FILENAME=$OUTPUT_DIRECTORY/$BASE_OUTPUT_FILENAME"_system.mem"
READ_RATE_FILENAME=$OUTPUT_DIRECTORY/$BASE_OUTPUT_FILENAME"_system.read"
WRITE_RATE_FILENAME=$OUTPUT_DIRECTORY/$BASE_OUTPUT_FILENAME"_system.write"

CPU_IDLE=0
CPU_USER=0
MEMORY_USED=0
#  FIXME add doc on these values
# Read vmstat command documentation to get information about these values meaning 
#
READ_NUMBER=0
WRITE_NUMBER=0
READ_SECTORS=0
WRITTEN_SECTORS=0

#
# TODO code that may be used to get specific cpu data
#
#NUMBER_OF_PROCESSORS=$1

#LIMIT=`expr $NUMBER_OF_PROCESSORS - 1`

#for (( c=0; c<$NUMBER_OF_PROCESSORS; c++ )); do
#	MPSTAT_INFO="`mpstat -P $c | sed 1,3d`"
#	echo $MPSTAT_INFO
#done

DEBUG=true
DEBUG_FILE_NAME="$INTRUSIVENESS_METER_HOME/logs/collector_system.log"

function debug_startup
{
        if [ $DEBUG ]; then
                touch $DEBUG_FILE_NAME
        fi
}

function debug
{
        if [ $DEBUG ]; then
                echo -n "`date "+%d-%m-%Y-%H-%M-%S"`" "`date "+%s%N"` " >> $DEBUG_FILE_NAME
                # TODO if the log file is too big, it must truncate to 0
                # or do something so the file does not grow without limit.
                echo $1 >> $DEBUG_FILE_NAME
        fi
}

function check_base_file_name
{
        if [ -z $BASE_OUTPUT_FILENAME ]; then
                echo "Invalid base filename. It must not be empty."
                exit
        fi
}

function start_up
{
	mkdir -p $OUTPUT_DIRECTORY
	touch $CPU_IDLE_FILENAME
	touch $CPU_USER_FILENAME
	touch $MEMORY_USAGE_FILENAME
	touch $READ_RATE_FILENAME
	touch $WRITE_RATE_FILENAME
}

function print_machine_info
{
	GENERAL_INFO="`uname -a`"
	C_INF0="`cat /proc/cpuinfo`"
	MEM_INFO="`cat /proc/meminfo`"

	echo "Machine general information" >> $CPU_IDLE_FILENAME
	echo "Machine general information" >> $CPU_USER_FILENAME
	echo "Machine general information" >> $MEMORY_USAGE_FILENAME
	echo "Machine general information" >> $READ_RATE_FILENAME
	echo "Machine general information" >> $WRITE_RATE_FILENAME

	echo "$GENERAL_INFO" >> $CPU_IDLE_FILENAME
	echo "$GENERAL_INFO" >> $CPU_USER_FILENAME
	echo "$GENERAL_INFO" >> $MEMORY_USAGE_FILENAME
	echo "$GENERAL_INFO" >> $READ_RATE_FILENAME
	echo "$GENERAL_INFO" >> $WRITE_RATE_FILENAME
	
	echo >> $CPU_IDLE_FILENAME
	echo >> $CPU_USER_FILENAME
	echo >> $MEMORY_USAGE_FILENAME
	echo >> $READ_RATE_FILENAME
	echo >> $WRITE_RATE_FILENAME
	
	echo "Machine CPU information" >> $CPU_IDLE_FILENAME
	echo "Machine CPU information" >> $CPU_USER_FILENAME
	echo "Machine CPU information" >> $MEMORY_USAGE_FILENAME
	echo "Machine CPU information" >> $READ_RATE_FILENAME
	echo "Machine CPU information" >> $WRITE_RATE_FILENAME
	
	echo >> $CPU_IDLE_FILENAME
	echo >> $CPU_USER_FILENAME
	echo >> $MEMORY_USAGE_FILENAME
	echo >> $READ_RATE_FILENAME
	echo >> $WRITE_RATE_FILENAME
	
	echo "`cat /proc/cpuinfo`" >> $CPU_IDLE_FILENAME
	echo "`cat /proc/cpuinfo`" >> $CPU_USER_FILENAME
	echo "`cat /proc/cpuinfo`" >> $MEMORY_USAGE_FILENAME
	echo "`cat /proc/cpuinfo`" >> $READ_RATE_FILENAME
	echo "`cat /proc/cpuinfo`" >> $WRITE_RATE_FILENAME

	echo >> $CPU_IDLE_FILENAME
	echo >> $CPU_USER_FILENAME
	echo >> $MEMORY_USAGE_FILENAME
	echo >> $READ_RATE_FILENAME
	echo >> $WRITE_RATE_FILENAME
	
	echo "Machine memory information" >> $CPU_IDLE_FILENAME
	echo "Machine memory information" >> $CPU_USER_FILENAME
	echo "Machine memory information" >> $MEMORY_USAGE_FILENAME
	echo "Machine memory information" >> $READ_RATE_FILENAME
	echo "Machine memory information" >> $WRITE_RATE_FILENAME

	echo >> $CPU_IDLE_FILENAME
	echo >> $CPU_USER_FILENAME
	echo >> $MEMORY_USAGE_FILENAME
	echo >> $READ_RATE_FILENAME
	echo >> $WRITE_RATE_FILENAME

	echo "$MEM_INFO" >> $CPU_IDLE_FILENAME
	echo "$MEM_INFO" >> $CPU_USER_FILENAME
	echo "$MEM_INFO" >> $MEMORY_USAGE_FILENAME
	echo "$MEM_INFO" >> $READ_RATE_FILENAME
	echo "$MEM_INFO" >> $WRITE_RATE_FILENAME

	echo >> $CPU_IDLE_FILENAME
	echo >> $CPU_USER_FILENAME
	echo >> $MEMORY_USAGE_FILENAME
	echo >> $READ_RATE_FILENAME
	echo >> $WRITE_RATE_FILENAME
}

function get_system_data
{
	sar_data="`sar -u -r -d 1 1 | grep "Average" | sed 1d | sed 2d | sed 3d`"
	vmstat_data="`vmstat -p $DEVICE_TO_MONITOR | sed 1d`"
	CPU_IDLE="`echo $sar_data | awk '{ print $8 }'`"
	CPU_USER="`echo $sar_data | awk '{ print $3 }'`"
	MEMORY_USED="`echo $sar_data | awk '{ print $12 }'`"
	READ_NUMBER="`echo $vmstat_data | awk '{ print $1 }'`"
	READ_SECTORS="`echo $vmstat_data | awk '{ print $2 }'`"
	WRITE_NUMBER="`echo $vmstat_data | awk '{ print $3 }'`"
	WRITTEN_SECTORS="`echo $vmstat_data | awk '{ print $4 }'`"
}

function print_system_cpu_usage
{
	echo -n "`date "+%d-%m-%Y-%H-%M-%S"`" "`date "+%s%N"` " >> $CPU_IDLE_FILENAME
	echo -n "`date "+%d-%m-%Y-%H-%M-%S"`" "`date "+%s%N"` " >> $CPU_USER_FILENAME

	echo $CPU_IDLE >> $CPU_IDLE_FILENAME
	echo $CPU_USER >> $CPU_USER_FILENAME
}

function print_system_memory_usage
{
	echo -n "`date "+%d-%m-%Y-%H-%M-%S"`" "`date "+%s%N"` " >> $MEMORY_USAGE_FILENAME
	echo $MEMORY_USED >> $MEMORY_USAGE_FILENAME
}

function print_system_device_usage
{
	echo -n "`date "+%d-%m-%Y-%H-%M-%S"`" "`date "+%s%N"` " >> $READ_RATE_FILENAME
	echo -n "`date "+%d-%m-%Y-%H-%M-%S"`" "`date "+%s%N"` " >> $WRITE_RATE_FILENAME
	echo -n $READ_NUMBER >> $READ_RATE_FILENAME
	echo " $READ_SECTORS" >> $READ_RATE_FILENAME
	echo -n $WRITE_NUMBER >> $WRITE_RATE_FILENAME
	echo " $WRITTEN_SECTORS" >> $WRITE_RATE_FILENAME
}

#
# Main
#

if [ $INTRUSIVENESS_METER_HOME ]; then
	check_base_file_name

	debug_startup

	debug "Creating results files"
	start_up
	debug "Created results files"
	
	debug "getting system information"
	print_machine_info
	debug "got system information"

	while [ "1" = "1" ]; do
		debug "Getting data from system"
		get_system_data
		debug "Got data from system"
	
		print_system_cpu_usage
		print_system_memory_usage
		print_system_device_usage
	done
else
	echo "INTRUSIVENESS_METER_HOME is not defined."
fi
