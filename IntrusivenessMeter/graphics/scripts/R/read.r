#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# This program generates a PNG image file which represents the 
# read data stored in the given file whose name is passed
# as argument.
#
# Usage:
# R --slave --args source_filename image_filename < read.r
# 
# Arguments:
# source_filename: the file that stores the read data
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
reads <- read.csv(source_file, sep=" ")

# this line is necessary because the columns can have any name
colnames(reads) <- c('X0', 'X1')
# the values must be shown in KB
reads$X1 <- reads$X1/1024
frame <- data.frame(time = reads$X0, reads = reads$X1)

plot <- ggplot(height = height, width = width, res = res, frame, aes(frame$time, frame$reads))

# Setting image properties
png(generated_image_name, height = height, width = width, res = res)

plot + 
# horizontal axis
scale_x_continuous("tempo") + 
# vertical axis
scale_y_continuous("tamanho read (KB)") + 
# use color
geom_point(aes(colour = frame$reads)) + 
# title
opts(title = expression("tamanho read X tempo"))
