#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# The Logger class is the logger used by all the python programs of 
# the intrusiveness meter. It put the logs files in the directory 
# LOGS_DIRECTORY
#

import time
import os

LOGS_DIRECTORY = "logs"

class Logger:
    def __init__(this, log_file_name, mode = "w"):
        this.log_file_name = LOGS_DIRECTORY + "/" + log_file_name
        this.file = open(this.log_file_name, mode)

    def get_time(self):
        return os.popen("bash source/time.sh").read()

    def log(this, log):
        this.file.write(this.get_time() + " " + log + "\n")
        this.file.flush()
