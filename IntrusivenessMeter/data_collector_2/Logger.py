import time

class Logger:
    def __init__(this, log_file_name):
        this.log_file_name = log_file_name
        this.file = open(log_file_name, "w")

    def log(this, log):
        this.file.write(str(time.time()) + " " + log + "\n")
        this.file.flush()
