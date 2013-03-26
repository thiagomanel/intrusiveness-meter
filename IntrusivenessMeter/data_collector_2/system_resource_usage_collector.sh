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
# This program collects data about CPU and memory usages of the system
# FIXME correct the name of the script
# usage: 
# resource_usage_collector OUTPUT_BASE_FILENAME
#
# Parameters:
# OUTPUT_BASE_FILENAME : this radical is used to construct the output file names. 
# The program creates three files, one for idle CPU information, other for 
# user CPU information and another for memory information. 
# If OUTPUT_BASE_FILENAME is "aaaa", the created files are aaaa_system.idlecpu 
# aaaa_system.usercpu and aaaa.mem
# 

#
# FIXME Error Handling
#

BASE_OUTPUT_FILENAME=$1

CPU_IDLE_FILENAME=$BASE_OUTPUT_FILENAME"_system.idlecpu"
CPU_USER_FILENAME=$BASE_OUTPUT_FILENAME"_system.usercpu"
MEMORY_USAGE_FILENAME=$BASE_OUTPUT_FILENAME"_system.mem"

CPU_IDLE=0
CPU_USER=0
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

DEBUG=true
DEBUG_FILE_NAME="collector_system.log"

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
	touch $CPU_IDLE_FILENAME
	touch $CPU_USER_FILENAME
	touch $MEMORY_USAGE_FILENAME
}

function get_system_data
{
	sar_data=`sar -u -r 1 1 | grep "Average" | sed 1d | sed 2d`
	CPU_IDLE=`echo $sar_data | awk '{ print $8 }'`
	CPU_USER=`echo $sar_data | awk '{ print $3 }'`
	MEMORY_USED=`echo $sar_data | awk '{ print $12 }'`
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

#
# Main
#

check_base_file_name

debug_startup

debug "Creating results files"
start_up
debug "Created results files"

while [ "1" = "1" ]; do
	debug "Getting data from system"
	get_system_data
	debug "Got data from system"

	print_system_cpu_usage
	print_system_memory_usage
done
