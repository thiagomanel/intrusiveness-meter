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

#
# TODO add configuration loading
#

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

def stop_all_benchmarks():
    # TODO add calls to hadoop here
    # TODO maybe add the benchmarks as parameter 
    # I think calling hadoop is enough
    Popen(["killall", "cpu"])
    Popen(["killall", "memory"])
    Popen(["killall", "io"])


machines = []

HADOOP_SCRIPT = "hadoop.sh"
HADOOP_SCRIPT_CONF_FILE = "hadoop_configuration"
PROBABILITY_TO_RUN = 1
SLEEP_TIME = 60

benchmarks = ["mr"]

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

        running = True
    time.sleep(SLEEP_TIME) 
