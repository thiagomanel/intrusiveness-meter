#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Intrusiveness Meter Controller
#
# This program is the kernel of the Intrusiveness Meter. It coordinates
# the activity of the benchmarks and data collectores.
#

import random
import os
import time
from subprocess import *
from configuration_loader import *

CONF_DIRECTORY = "../conf"

HADOOP_SCRIPT = "hadoop.sh"
HADOOP_SCRIPT_CONF_FILE = CONF_DIRECTORY + "/hadoop_configuration"

CONTROLLER_CONF_FILE = CONF_DIRECTORY + "/controller_configuration"
PROBABILITY_TO_RUN_PROPERTY = "PROBABILITY_TO_RUN"
SLEEP_TIME_PROPERTY = "SLEEP_TIME"

CONTROLLER_LOG_FILE_NAME = "controller.log"


class Logger:
    def __init__(this, log_file_name):
        this.log_file_name = log_file_name
        this.file = open(log_file_name, "w")

    def log(this, log):
        this.file.write(str(time.time()) + " " + log + "\n")
        this.file.flush()

class Controller:

    def __init__(self, configuration_file, log_file):
        configuration_loader = Loader(configuration_file)
        self.l = Logger(log_file)

        if not configuration_loader.has_property(PROBABILITY_TO_RUN_PROPERTY) or not configuration_loader.has_property(SLEEP_TIME_PROPERTY):
            print "Invalid Configuration File"
            self.l.log("Invalid Configuration File")
            exit()

        self.probability_to_run = float(configuration_loader.get_property(PROBABILITY_TO_RUN_PROPERTY))
        self.sleep_time = float(configuration_loader.get_property(SLEEP_TIME_PROPERTY))

    def choose_benchmark(self, benchmarks):
        return random.sample(benchmarks, 1)[0]

    def run_benchmark(self, benchmark, hadoop_script_conf_file):
        return os.system("bash hadoop.sh " + benchmark + " " + hadoop_script_conf_file)

    def stop_all_benchmarks(self):
        # TODO add calls to hadoop here
        # TODO maybe add the benchmarks as parameter 
        # I think calling hadoop is enough
        Popen(["killall", "cpu"])
        Popen(["killall", "memory"])
        Popen(["killall", "io"])
    
    def thereAreRunningBenchmarks(self):
        running = False
        input = os.popen("bash hadoop_info.sh -r").read()
        if input == "true":
            running = True
        return running

    def run(self, benchmarks, HADOOP_SCRIPT_CONF_FILE):
        self.l.log("starting execution")
        chosen_benchmark = ""
        while True:
            random_number = random.random()
            self.l.log("random number:" + str(random_number))
            if random_number < self.probability_to_run:
                if self.thereAreRunningBenchmarks():
                    self.l.log("gave up of starting. There are running benchmarks")
                else:
                    self.l.log("will start benchmark")
                    # this logic should be in choose_benchmark
                    if chosen_benchmark == "dfwrite":
                        chosen_benchmark = "dfread"
                    elif chosen_benchmark == "dfread":
                        chosen_benchmark = "dfclean"
                    elif chosen_benchmark == "teragen":
                        chosen_benchmark = "terasort"
                    elif chosen_benchmark == "terasort":
                        chosen_benchmark = "teravalidate"
                    elif chosen_benchmark == "teravalidate":
                        chosen_benchmark = "teraclean"
                    else:
                        # i think i need to finish the running benchmarks
                        chosen_benchmark = self.choose_benchmark(benchmarks)
	
                    self.l.log("chosen benchmark: " + chosen_benchmark)
                    self.l.log("will start benchmark:" + chosen_benchmark)
                    self.run_benchmark(chosen_benchmark, HADOOP_SCRIPT_CONF_FILE)
                    self.l.log("started benchmark: " + chosen_benchmark)
            else:
                self.l.log("Decided not to run")
            time.sleep(self.sleep_time) 


#
#  Main
#

def main():
    benchmarks = ["teragen", "dfwrite"]

    controller = Controller(CONTROLLER_CONF_FILE, CONTROLLER_LOG_FILE_NAME)
    controller.run(benchmarks, HADOOP_SCRIPT_CONF_FILE)

main()

