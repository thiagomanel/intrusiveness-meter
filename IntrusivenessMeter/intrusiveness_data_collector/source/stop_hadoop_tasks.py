#!/usr/bin/python

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Hadoop Stop Script
# 
# This program stops the TaskTracker component and kills 
# the running tasks
#

import Logger
import os

logger = Logger.Logger("stopped.log", "a")
discomfort_report = Logger.Logger("discomfort.log", "a")

RESULTS_DIRECTORY = os.environ["INTRUSIVENESS_METER_HOME"] + "/logs"
SYSTEM_PROCESSES_LOGGER = os.environ['INTRUSIVENESS_METER_HOME'] + "/source/whole_system_processes_monitor.sh"

# FIXME duplicated in hadoop_aware_collector.py
def get_benchmarks_processes():
    hadoop_processes_str = os.popen("ps xau | grep hadoop | grep attempt | grep -v grep | awk '{ print $2 }'").read()
    logger.log("hadoop processes string:" + hadoop_processes_str)
    hadoop_processes = hadoop_processes_str.split()
    logger.log("hadoop processes:" + str(hadoop_processes))

    return hadoop_processes

def get_tasktracker_process():
    return os.popen("ps xau | grep TaskTracker | awk '{ print $2 }'").read()

def kill_task_tracker():
    hadoop_home = os.environ['HADOOP_HOME']
    # FIXME should kill only the TaskTracker
    os.popen("bash " + hadoop_home + "/bin/stop-mapred.sh")

def log_system_processes():
    logger.log(SYSTEM_PROCESSES_LOGGER)
    logger.log(RESULTS_DIRECTORY)
    os.popen("bash " + SYSTEM_PROCESSES_LOGGER + " " + RESULTS_DIRECTORY)  

logger.log("logging the system processes") 
log_system_processes()
logger.log("logged system processes")
discomfort_report.log("")
logger.log("killing tasktracker")
kill_task_tracker()
logger.log("killed tasktracker")

for benchmark_process in get_benchmarks_processes():
    logger.log("stopping PID: " + benchmark_process)
    os.popen("kill " + benchmark_process)
    logger.log("stopped PID: " + benchmark_process)
