#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# This program generates a PNG image file which represents the 
# memory usage data stored in the given file whose name is passed
# as argument.
#
# Usage:
# R --slave --args source_filename image_filename < memory_percentual.r
# 
# Arguments:
# source_filename: the file that stores the memory usage data
# image_filename: name of the generated image file
#

#
# Required libraries
#
library(ggplot2)

#
# Generated image properties
#
width = 1000
height = 1000
res = 120

args <- commandArgs()
source_file <- args[4]
generated_image_name <- args[5]

#
# Loading data
#
memory_usage <- read.csv(source_file)
colnames(memory_usage) <- c('X0')
time <- seq(1, nrow(memory_usage), by=1)
frame <- data.frame(time = time, memory_usage = memory_usage$X0)


plot <- ggplot(height = height, width = width, res = res, frame, aes(frame$time, frame$memory_usage))

# Setting image properties
png(generated_image_name, height = height, width = width, res = res)

plot + 
# horizontal axis
scale_x_continuous("tempo") + 
# vertical axis
scale_y_continuous("consumo de memoria (%)") + 
# use color
geom_line(aes(colour = frame$memory_usage)) + 
# title
opts(title = expression("consumo de memoria (%) X tempo"))
