#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# This program is the kernel of the Intrusiveness Meter. It coordinates
# the activity of the benchmarks and data collectores.
#

import random
import os
import time
from subprocess import *

class Logger:
    def __init__(this, log_file_name):
        this.log_file_name = log_file_name
        this.file = open(log_file_name, "w")

    def log(this, log):
        this.file.write(str(time.time()) + " " + log + "\n")
        this.file.flush()

def choose_benchmark(benchmarks):
    return random.sample(benchmarks, 1)[0]

def run_benchmark(benchmark, hadoop_script_conf_file):
    return os.system("bash hadoop.sh " + benchmark + " " + hadoop_script_conf_file)

def get_benchmark_process_PID(benchmark):
    if benchmark == "cpu":
        return os.popen("ps xau | grep test/cpu | grep -v grep").read().split()[1]
    elif benchmark == "memory":
        return os.popen("ps xau | grep test/memory | grep -v grep").read().split()[1]
    elif benchmark == "io":
        return os.popen("ps xau | grep test/io | grep -v grep").read().split()[1]

# FIXME this function should not 
# receive the pid
# it should get the pid of the process 
def start_process_monitor(PID):
    # ssh to all machines
    # get the pid
    # start the collector 
    os.system("bash process_resource_usage_collector.sh " + PID + " 1 " + PID)

def stop_all_benchmarks():
    # I think call hadoop is enough
    os.system("killall cpu")
    os.system("killall memory")
    os.system("killall io")



machines = []

HADOOP_SCRIPT = "hadoop.sh"
HADOOP_SCRIPT_CONF_FILE = "hadoop_configuration"
PROBABILITY_TO_RUN = 0.1
SLEEP_TIME = 1

benchmarks = ["cpu", "memory", "io"]
#
# choose a benchmark to run
# run the benchmark
# get the benchmark process PID
# start the process monitor with the PID
#

running = False

l = Logger("controller.log")

l.log("starting execution")
while True:
    if random.random() < PROBABILITY_TO_RUN:
        l.log("will start benchmark")
        if running:
            l.log("stopping all benchmarks")
            stop_all_benchmarks()
        chosen_benchmark = choose_benchmark(benchmarks)
        l.log("chosen benchmark: " + chosen_benchmark)
        l.log("will start benchmark:" + chosen_benchmark)
        run_benchmark(chosen_benchmark, HADOOP_SCRIPT_CONF_FILE)
        l.log("started benchmark: " + chosen_benchmark)
        time.sleep(1)
        PID = get_benchmark_process_PID(chosen_benchmark)
        l.log("starting monitor: monitored PID " + PID )
        start_process_monitor(PID)
        l.log("started monitor: monitored PID " + PID)
        running = True
    time.sleep(SLEEP_TIME) 
