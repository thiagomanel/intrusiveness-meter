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
mrbench_data <- read.csv(mrbench_source_file, sep=" ")
test_dfsio_read1_data <- read.csv(test_dfsio_read1_source_file, sep=" ")
test_dfsio_write1_data <- read.csv(test_dfsio_write1_source_file, sep=" ")
test_dfsio_read2_data <- read.csv(test_dfsio_read2_source_file, sep=" ")
test_dfsio_write2_data <- read.csv(test_dfsio_write2_source_file, sep=" ")
terasort_data <- read.csv(terasort_source_file, sep=" ")

# this line is necessary because the columns can have any name 
colnames(mrbench_data)<-c('X0', 'X1')
colnames(test_dfsio_read1_data)<-c('X0', 'X1')
colnames(test_dfsio_write1_data)<-c('X0', 'X1')
colnames(test_dfsio_read2_data)<-c('X0', 'X1')
colnames(test_dfsio_write2_data)<-c('X0', 'X1')
colnames(terasort_data)<-c('X0', 'X1')

# the values must be shown in KB
mrbench_data$X1 <- mrbench_data$X1/1024
test_dfsio_read1_data$X1 <- test_dfsio_read1_data$X1/1024
test_dfsio_write1_data$X1 <- test_dfsio_write1_data$X1/1024
test_dfsio_read2_data$X1 <- test_dfsio_read2_data$X1/1024
test_dfsio_write2_data$X1 <- test_dfsio_write2_data$X1/1024
terasort_data$X1 <- terasort_data$X1/1024

# treat the time of the first write as the start
start_time_mrbench <- mrbench_data$X0[1]
start_time_dfsio_read1 <- test_dfsio_read1_data$X0[1]
start_time_dfsio_write1 <- test_dfsio_write1_data$X0[1]
start_time_dfsio_read2 <- test_dfsio_read2_data$X0[1]
start_time_dfsio_write2 <- test_dfsio_write2_data$X0[1]
start_time_teragen <- terasort_data$X0[1]

mrbench_data$X0 <- mrbench_data$X0 - start_time_mrbench
test_dfsio_read1_data$X0 <- test_dfsio_read1_data$X0 - start_time_dfsio_read1
test_dfsio_write1_data$X0 <- test_dfsio_write1_data$X0 - start_time_dfsio_write1
test_dfsio_read2_data$X0 <- test_dfsio_read2_data$X0 - start_time_dfsio_read2
test_dfsio_write2_data$X0 <- test_dfsio_write2_data$X0 - start_time_dfsio_write2
terasort_data$X0 <- terasort_data$X0 - start_time_teragen

# time is shown in minutes
mrbench_data$X0 <- mrbench_data$X0/(60*1000*1000)
test_dfsio_read1_data$X0 <- test_dfsio_read1_data$X0/(60*1000*1000)
test_dfsio_write1_data$X0 <- test_dfsio_write1_data$X0/(60*1000*1000)
test_dfsio_read2_data$X0 <- test_dfsio_read2_data$X0/(60*1000*1000)
test_dfsio_write2_data$X0 <- test_dfsio_write2_data$X0/(60*1000*1000)
terasort_data$X0 <- terasort_data$X0/(60*1000*1000)

# integrating data from all benchmarks
write_size <- c(mrbench_data$X1, test_dfsio_read1_data$X1, test_dfsio_write1_data$X1, test_dfsio_read2_data$X1, test_dfsio_write2_data$X1, terasort_data$X1);
time <- c(mrbench_data$X0, test_dfsio_read1_data$X0, test_dfsio_write1_data$X0, test_dfsio_read2_data$X0, test_dfsio_write2_data$X0, terasort_data$X0);
benchmark <- c(rep.int(c("MRBench"), nrow(mrbench_data)), rep.int(c("DFSIO Read (I)"), nrow(test_dfsio_read1_data)), 
          rep.int(c("DFSIO Write (I)"), nrow(test_dfsio_write1_data)), rep.int(c("DFSIO Read (II)"), nrow(test_dfsio_read2_data)),
            rep.int(c("DFSIO Write (II)"), nrow(test_dfsio_write2_data)), rep.int(c("Terasort"), nrow(terasort_data)))

data <- data.frame(time, write_size, benchmark)
plot <- ggplot(data, aes(time, write_size)) 

png(generated_image_name, height = height, width = width, res = res)

plot +
geom_point() +
# horizontal axis
scale_x_continuous("tempo (minutos)") + 
# vertical axis
scale_y_continuous("Tamanho do write (em KB)") + 
facet_grid(. ~ benchmark, scales = "free_x") + opts(title = expression("Tamanho do write (em KB)"))
