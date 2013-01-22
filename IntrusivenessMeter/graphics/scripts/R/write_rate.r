#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# This program generates a PNG image file which represents the 
# write rate data stored in the given file whose name is passed
# as argument.
#
# Usage:
# R --slave --args source_filename image_filename < write_rate.r
# 
# Arguments:
# source_filename: the file that stores the write rate data
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
write_rate <- read.csv(source_file)

# this line is necessary because the columns can have any name
colnames(write_rate) <- c('X0')
time <- seq(1, nrow(write_rate), by=1)
frame <- data.frame(time = time, write_rate = write_rate$X0)


plot <- ggplot(height = height, width = width, res = res, frame, aes(frame$time, frame$write_rate))

# Setting image properties
png(generated_image_name, height = height, width = width, res = res)

plot + 
# horizontal axis
scale_x_continuous("tempo") + 
# vertical axis
scale_y_continuous("write calls") + 
# use color
geom_line(aes(colour = frame$write_rate)) + 
# title
opts(title = expression("write calls X tempo"))
