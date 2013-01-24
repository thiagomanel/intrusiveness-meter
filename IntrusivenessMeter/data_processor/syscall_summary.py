#!/usr/bin/python

#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# This script summarizes the syscall files, extracting the most 
# important information (read, write and seek calls and the time of the
# call). It writes the summary to three different files, one for each 
# syscall.
#

import sys

FIELD_SEPARATOR = ">"

READ_CALL = "sys_read"
WRITE_CALL = "sys_write"
SEEK_CALL = "sys_llseek" 

READ_FILE_NAME = "read.calls"
WRITE_FILE_NAME = "write.calls"
SEEK_FILE_NAME = "seek.calls"

SYSCALL_NAME_INDEX = 1
SYSCALL_TIME_INDEX = 2
OPERATION_TIME_INDEX = 3

READ_SIZE_INDEX = 11
WRITE_SIZE_INDEX = 11
SEEK_OFFSET_HIGH = 5
SEEK_OFFSET_LOW = 6

class Syscalls:
    
    def __init__(self, read_file_name, write_file_name, seek_file_name, field_separator):
        self.read_file = open(read_file_name, "w")
        self.write_file = open(write_file_name, "w")   
        self.seek_file = open(seek_file_name, "w")
        self.field_separator = field_separator

    def finish(self):
        self.read_file.flush()
        self.read_file.close()
    
        self.write_file.flush()
        self.write_file.close()
        
        self.seek_file.flush()
        self.seek_file.close()

    def process_syscall(self, syscall_data):
        fields = syscall_data.split(self.field_separator)
        syscall_name = fields[SYSCALL_NAME_INDEX]
        syscall_time = fields[SYSCALL_TIME_INDEX]

        if syscall_name == READ_CALL:
            self.read_file.write("%s %s\n" % (syscall_time, fields[READ_SIZE_INDEX]))
            self.read_file.flush()
        elif syscall_name == WRITE_CALL:
            self.write_file.write("%s %s\n" % (syscall_time, fields[WRITE_SIZE_INDEX]))
            self.write_file.flush()
        elif syscall_name == SEEK_CALL:
            offset_high = int(fields[SEEK_OFFSET_HIGH])
            offset_low = int(fields[SEEK_OFFSET_LOW])
            seek_size = offset_high << 32 | offset_low
            self.seek_file.write("%s %d\n" % (syscall_time, seek_size))
            self.seek_file.flush()

syscalls = Syscalls(READ_FILE_NAME, WRITE_FILE_NAME, SEEK_FILE_NAME, FIELD_SEPARATOR)

for line in sys.stdin:
    syscalls.process_syscall(line)

syscalls.finish()
