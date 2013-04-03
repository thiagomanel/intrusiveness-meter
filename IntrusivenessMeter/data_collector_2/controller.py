import random
import os
import time
from subprocess import *

machines = []

HADOOP_SCRIPT = "hadoop.sh"
HADOOP_SCRIPT_CONF_FILE = "hadoop_configuration"
PROBABILITY_TO_RUN = 0.1
SLEEP_TIME = 1

benchmarks = ["cpu", "memory"]

class Logger:
    def __init__(this, log_file_name):
        this.log_file_name = log_file_name
        this.file = open(log_file_name, "w")

    def log(this, log):
        this.file.write(str(time.time()) + " " + log + "\n")
        this.file.flush()

def choose_benchmark(benchmarks):
    return random.sample(benchmarks, 1)[0]

def run_benchmark(benchmark):
    return os.system("bash hadoop.sh " + benchmark + " " + HADOOP_SCRIPT_CONF_FILE)

def get_benchmark_process_PID(benchmark):
    if benchmark == "cpu":
        return os.popen("ps xau | grep test/cpu | grep -v grep").read().split()[1]
    elif benchmark == "memory":
        return os.popen("ps xau | grep test/memory | grep -v grep").read().split()[1]

def start_process_monitor(PID):
    os.system("bash process_resource_usage_collector.sh " + PID + " 1 " + PID)

def stop_all_benchmarks():
    os.system("killall cpu")
    os.system("killall memory")

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
        run_benchmark(chosen_benchmark)
        l.log("started benchmark: " + chosen_benchmark)
        PID = get_benchmark_process_PID(chosen_benchmark)
        l.log("starting monitor: monitored PID " + PID )
        start_process_monitor(PID)
        l.log("started monitor: monitored PID " + PID)
        running = True
    time.sleep(SLEEP_TIME) 
