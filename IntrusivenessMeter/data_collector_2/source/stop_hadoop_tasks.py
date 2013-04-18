import Logger
import os

logger = Logger.Logger("stopped.log")
# FIXME hard coded
HADOOP_HOME = "/local/armstrongmsg/hadoop-1.1.1"

# FIXME duplicated in hadoop_aware_collector.py
def get_benchmarks_processes():
    hadoop_processes_str = os.popen("ps xau | grep hadoop | grep attempt | grep -v grep | awk '{ print $2 }'").read()
    logger.log("hadoop processes string:" + hadoop_processes_str)
    hadoop_processes = hadoop_processes_str.split()
    logger.log("hadoop processes:" + str(hadoop_processes))

    return hadoop_processes

def get_tasktracker_process():
    return os.popen("ps xau | grep TaskTracker | awk '{ print $2 }'").read()

logger.log("killing tasktracker")
# FIXME it should call hadoop
os.popen("kill " + get_tasktracker_process())
logger.log("killed tasktracker")

for benchmark_process in get_benchmarks_processes():
    logger.log("stopping PID: " + benchmark_process)
    os.popen("kill " + benchmark_process)
    logger.log("stopped PID: " + benchmark_process)
