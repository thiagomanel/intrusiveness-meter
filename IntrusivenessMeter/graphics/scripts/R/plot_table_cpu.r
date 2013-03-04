#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# This program generates a PNG image file which represents the 
# cpu usage data stored in the given files whose names are passed
# as argument.
#
# Usage:
# R --slave --args mrbench_filename testdfsio_read1_filename testdfsio_write1_filename testdfsio_read2_filename testdfsio_write2_filename image_filename < plot_table_cpu.r
# 
# Arguments:
# TODO explain the arguments
# image_filename: name of the generated image file
#
# Note: this script is very coupled to the benchmarks and configurations I used in the experiment.
#

#
# Required libraries
#
library("ggplot2")

#
# Generated image properties
#
width = 1000
height = 1000
res = 120

# reading args
args <- commandArgs()
mrbench_source_file <- args[4]
test_dfsio_read1_source_file <- args[5]
test_dfsio_write1_source_file <- args[6]
test_dfsio_read2_source_file <- args[7]
test_dfsio_write2_source_file <- args[8]
terasort_source_file <- args[9]
generated_image_name <- args[10]

# loading data
mrbench_data <- read.csv(mrbench_source_file)
test_dfsio_read1_data <- read.csv(test_dfsio_read1_source_file)
test_dfsio_write1_data <- read.csv(test_dfsio_write1_source_file)
test_dfsio_read2_data <- read.csv(test_dfsio_read2_source_file)
test_dfsio_write2_data <- read.csv(test_dfsio_write2_source_file)
terasort_data <- read.csv(terasort_source_file)

# this line is necessary because the columns can have any name 
colnames(mrbench_data)<-c('X0')
colnames(test_dfsio_read1_data)<-c('X0')
colnames(test_dfsio_write1_data)<-c('X0')
colnames(test_dfsio_read2_data)<-c('X0')
colnames(test_dfsio_write2_data)<-c('X0')
colnames(terasort_data)<-c('X0')

# calculating time
time_mrbench <- seq(1, nrow(mrbench_data), by=1)
time_dfsio_read1 <- seq(1, nrow(test_dfsio_read1_data), by=1)
time_dfsio_write1 <- seq(1, nrow(test_dfsio_write1_data), by=1)
time_dfsio_read2 <- seq(1, nrow(test_dfsio_read2_data), by=1)
time_dfsio_write2 <- seq(1, nrow(test_dfsio_write2_data), by=1)
time_terasort <- seq(1, nrow(terasort_data), by=1)

# integrating data from all benchmarks
cpu_usage <- c(mrbench_data$X0, test_dfsio_read1_data$X0, test_dfsio_write1_data$X0, test_dfsio_read2_data$X0, test_dfsio_write2_data$X0, terasort_data$X0);
time <- c(time_mrbench, time_dfsio_read1, time_dfsio_write1, time_dfsio_read2, time_dfsio_write2, time_terasort)
benchmark <- c(rep.int(c("MRBench"), nrow(mrbench_data)), rep.int(c("DFSIO Read (I)"), nrow(test_dfsio_read1_data)), 
          rep.int(c("DFSIO Write (I)"), nrow(test_dfsio_write1_data)), rep.int(c("DFSIO Read (II)"), nrow(test_dfsio_read2_data)),
            rep.int(c("DFSIO Write (II)"), nrow(test_dfsio_write2_data)), rep.int(c("Terasort"), nrow(terasort_data)))

data <- data.frame(time, cpu_usage, benchmark)

plot <- ggplot(data, aes(time, cpu_usage)) 

png(generated_image_name, height = height, width = width, res = res)

plot +
geom_line() +
# horizontal axis
scale_x_continuous("tempo (segundos)") + 
# vertical axis
scale_y_continuous("Consumo de CPU (%)") + 
facet_grid(. ~ benchmark, scales = "free_x") + opts(title = expression("Consumo de CPU em %"))
