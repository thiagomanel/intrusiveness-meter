#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# Hadoop Running Benchmarks Info Daemon
#
# This daemon gets information if the Hadoop is running something
# and logs it. 
#

import os
from Logger import *
from configuration_loader import *
from IDGenerator import *
import time

INTRUSIVENESS_METER_HOME_PROPERTY = "INTRUSIVENESS_METER_HOME"
INTRUSIVENESS_METER_HOME = os.environ["INTRUSIVENESS_METER_HOME"]

SOURCE_DIRECTORY = INTRUSIVENESS_METER_HOME + "/source"
HADOOP_INFO_SCRIPT = SOURCE_DIRECTORY + "/hadoop_info.sh"

ERROR_LOG_FILE = "hadoop_running.error"
LOG_FILE = "hadoop_running.log"

def there_are_running_benchmarks():
    running = False
    # FIXME hard coded
    input = os.popen("bash " + HADOOP_INFO_SCRIPT + " -r").read()
    if input == "true":
        running = True
    return running

error_log = Logger(ERROR_LOG_FILE, "a")
logger = Logger(LOG_FILE, "a")
idGenerator = IDGenerator() 
incarnationid = idGenerator.generate()

logger.log("Incarnation ID: " + incarnationid)
error_log.log("Incarnation ID: " + incarnationid)

SLEEP_TIME = 1

while True:
    logger.log(str(there_are_running_benchmarks()))
    time.sleep(SLEEP_TIME)
