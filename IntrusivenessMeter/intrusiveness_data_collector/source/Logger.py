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

import os
from Configuration import *

INTRUSIVENESS_METER_HOME_PROPERTY = "INTRUSIVENESS_METER_HOME"
INTRUSIVENESS_METER_HOME = os.environ[INTRUSIVENESS_METER_HOME_PROPERTY]

LOGS_DIRECTORY = INTRUSIVENESS_METER_HOME + "/logs"
TIME_SCRIPT = INTRUSIVENESS_METER_HOME + "/source/time.sh"

class Logger:
    def __init__(this, log_file_name, mode = "w"):
        this.log_file_name = LOGS_DIRECTORY + "/" + log_file_name
        this.file = open(this.log_file_name, mode)

    def get_time(self):
        return os.popen("bash " + TIME_SCRIPT).read()

    def log(this, log):
        if LOG_ENABLED: 
            this.file.write(this.get_time() + " " + log + "\n")
            this.file.flush()
