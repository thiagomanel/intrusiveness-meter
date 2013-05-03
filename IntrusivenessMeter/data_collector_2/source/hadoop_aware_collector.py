import Logger
import time
from subprocess import *
import os
import sys


COLLECTOR_SCRIPT = "process_resource_usage_collector.sh"
RESULTS_DIRECTORY = sys.argv[1]

monitored_processes = []

logger = Logger.Logger("hadoop_aware.log")

def get_benchmarks_processes():
    hadoop_processes_str = os.popen("ps xau | grep hadoop | grep attempt | grep -v grep | awk '{ print $2 }'").read()
    logger.log("hadoop processes string:" + hadoop_processes_str)
    hadoop_processes = hadoop_processes_str.split()
    logger.log("hadoop processes:" + str(hadoop_processes))

    return hadoop_processes

def start_process_monitoring(PID):
    logger.log("PID to monitor:" + PID)
    Popen(["bash", COLLECTOR_SCRIPT, str(PID), "1", str(PID) + "-" + str(time.time()), RESULTS_DIRECTORY])


while True:
    benchmarks_processes = get_benchmarks_processes()
    logger.log("benchmark processes:" + str(benchmarks_processes)) 
    logger.log("monitored:" + str(monitored_processes))

    for benchmark_process in benchmarks_processes:
        logger.log("benchmark process:" + benchmark_process)
        if not benchmark_process in monitored_processes:
            logger.log("starting monitoring PID:" + benchmark_process)
            start_process_monitoring(benchmark_process)
    monitored_processes = benchmarks_processes

    time.sleep(1)
