#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# This program generates a PNG image file which represents the 
# write amount data stored in the given file whose name is passed
# as argument.
#
# Usage:
# R --slave --args source_filename image_filename < write_amount.r
# 
# Arguments:
# source_filename: the file that stores the write amount data
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
write_amount <- read.csv(source_file)

# this line is necessary because the columns can have any name
colnames(write_amount) <- c('X0')
time <- seq(1, nrow(write_amount), by=1)
frame <- data.frame(time = time, write_amount = write_amount$X0)


plot <- ggplot(height = height, width = width, res = res, frame, aes(frame$time, frame$write_amount))

# Setting image properties
png(generated_image_name, height = height, width = width, res = res)

plot + 
# horizontal axis
scale_x_continuous("tempo") + 
# vertical axis
scale_y_continuous("tamanho write") + 
# use color
geom_line(aes(colour = frame$write_amount)) + 
# title
opts(title = expression("tamanho write X tempo"))
