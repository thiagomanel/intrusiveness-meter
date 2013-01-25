#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# This program generates a PNG image file which represents the 
# write data stored in the given file whose name is passed
# as argument.
#
# Usage:
# R --slave --args source_filename image_filename < write.r
# 
# Arguments:
# source_filename: the file that stores the write data
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
writes <- read.csv(source_file, sep=" ")

# this line is necessary because the columns can have any name
colnames(writes)<-c('X0', 'X1')
# the values must be shown in KB
writes$X1 <- writes$X1/1024
# treat the time of the first write as the start
writes$X0 <- writes$X0 - writes$X0[1]
# time is shown in minutes
writes$X0 <- writes$X0/(60*1000*1000)
frame <- data.frame(time = writes$X0, writes = writes$X1)


plot <- ggplot(height = height, width = width, res = res, frame, aes(frame$time, frame$writes))

# Setting image properties
png(generated_image_name, height = height, width = width, res = res)

plot + 
# horizontal axis
scale_x_continuous("tempo (min)") + 
# vertical axis
scale_y_continuous("tamanho write (KB)") + 
# use color
geom_point(aes(colour = frame$writes)) + 
# title
opts(title = expression("tamanho write (KB) X tempo (min)"))
