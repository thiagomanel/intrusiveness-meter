#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Hadoop Activity Monitor
#
# This program checks which Hadoop processes are running
# and, if there are new processes, calls the process monitor
# to monitor them.
#
# usage:
# python hadoop_aware_collector.py RESULTS_DIRECTORY
#
# Parameters:
# RESULTS_DIRECTORY : the directory where the results will be placed.
# 

import Logger
import time
from subprocess import *
import os
import sys

# FIXME hard coded
COLLECTOR_SCRIPT = "source/process_resource_usage_collector.sh"
RESULTS_DIRECTORY = sys.argv[1]

monitored_processes = []

logger = Logger.Logger("hadoop_aware.log")
hadoop_cpu_usage = Logger.Logger("hadoop_resources_usage.cpu")
hadoop_memory_usage = Logger.Logger("hadoop_resources_usage.mem")

def get_benchmarks_processes():
    hadoop_processes_str = os.popen("ps xau | grep hadoop | grep attempt | grep -v grep | awk '{ print $2 }'").read()
    logger.log("hadoop processes string:" + hadoop_processes_str)
    hadoop_processes = hadoop_processes_str.split()
    logger.log("hadoop processes:" + str(hadoop_processes))

    return hadoop_processes

def get_process_cpu_usage(benchmark_process):
    cpu =  float(os.popen("ps -p " + benchmark_process + " -o %cpu | sed 1d").read())
    print cpu
    return cpu

def get_process_memory_usage(benchmark_process):
    memory = float(os.popen("awk '/Rss:/{ sum += $2 } END { print sum }' /proc/" + benchmark_process + "/smaps").read())
    print memory
    return memory

def start_process_monitoring(PID):
    logger.log("PID to monitor:" + PID)
    Popen(["bash", COLLECTOR_SCRIPT, str(PID), "1", str(PID) + "-" + str(time.time()), RESULTS_DIRECTORY])

while True:
    total_cpu_usage = 0
    total_memory_usage = 0

    benchmarks_processes = get_benchmarks_processes()
    logger.log("benchmark processes:" + str(benchmarks_processes)) 
    logger.log("monitored:" + str(monitored_processes))

    for benchmark_process in benchmarks_processes:
        process_cpu_usage = get_process_cpu_usage(benchmark_process)
        process_memory_usage = get_process_memory_usage(benchmark_process)

        total_cpu_usage += process_cpu_usage
        total_memory_usage += process_memory_usage

        logger.log("benchmark process:" + benchmark_process)
        if not benchmark_process in monitored_processes:
            logger.log("starting monitoring PID:" + benchmark_process)
            start_process_monitoring(benchmark_process)
    monitored_processes = benchmarks_processes

    hadoop_cpu_usage.log(str(total_cpu_usage))
    hadoop_memory_usage.log(str(total_memory_usage))

    time.sleep(1)
