#
# Federal University of Campina Grande
# Distributed Systems Laboratory
#
# Author: Armstrong Mardilson da Silva Goes
# Contact: armstrongmsg@lsd.ufcg.edu.br
#

#
# This program generates a PNG image file which represents the 
# seek data stored in the given file whose name is passed
# as argument.
#
# Usage:
# R --slave --args source_filename image_filename < seek.r
# 
# Arguments:
# source_filename: the file that stores the seek data
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
seeks <- read.csv(source_file, sep=" ")

# this line is necessary because the columns can have any name
colnames(seeks)<-c('X0', 'X1')
# the values must be shown in KB 
seeks$X1 <- seeks$X1/1024
frame <- data.frame(time = seeks$X0, seeks = seeks$X1)


plot <- ggplot(height = height, width = width, res = res, frame, aes(frame$time, frame$seeks))

# Setting image properties
png(generated_image_name, height = height, width = width, res = res)

plot + 
# horizontal axis
scale_x_continuous("tempo") + 
# vertical axis
scale_y_continuous("tamanho seek") + 
# use color
geom_point(aes(colour = frame$seeks)) + 
# title
opts(title = expression("tamanho seek X tempo"))
