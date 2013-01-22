#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# This program generates a PNG image file which represents the 
# seek size data stored in the given file whose name is passed
# as argument.
#
# Usage:
# R --slave --args source_filename image_filename < seek_size.r
# 
# Arguments:
# source_filename: the file that stores the seek size data
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
seek_size <- read.csv(source_file)

# this line is necessary because the columns can have any name
colnames(seek_size) <- c('X0')
time <- seq(1, nrow(seek_size), by=1)
frame <- data.frame(time = time, seek_size = seek_size$X0)


plot <- ggplot(height = height, width = width, res = res, frame, aes(frame$time, frame$seek_size))

# Setting image properties
png(generated_image_name, height = height, width = width, res = res)

plot + 
# horizontal axis
scale_x_continuous("tempo") + 
# vertical axis
scale_y_continuous("tamanho seek") + 
# use color
geom_line(aes(colour = frame$seek_size)) + 
# title
opts(title = expression("tamanho seek X tempo"))
