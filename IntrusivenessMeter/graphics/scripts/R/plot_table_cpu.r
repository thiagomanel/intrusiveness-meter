library("ggplot2")

# reading args
args <- commandArgs()
mrbench_source_file <- args[4]
test_dfsio_read1_source_file <- args[5]
test_dfsio_write1_source_file <- args[6]
test_dfsio_read2_source_file <- args[7]
test_dfsio_write2_source_file <- args[8]
teragenerate_source_file <- args[9]
terasort_source_file <- args[10]
teravalidate_source_file <- args[11]

# loading data
mrbench_data <- read.csv(mrbench_source_file)
test_dfsio_read1_data <- read.csv(test_dfsio_read1_source_file)
test_dfsio_write1_data <- read.csv(test_dfsio_write1_source_file)
test_dfsio_read2_data <- read.csv(test_dfsio_read2_source_file)
test_dfsio_write2_data <- read.csv(test_dfsio_write2_source_file)
teragenerate_data <- read.csv(teragenerate_source_file)
terasort_data <- read.csv(terasort_source_file)
teravalidate_data <- read.csv(teravalidate_source_file)

# this line is necessary because the columns can have any name 
colnames(mrbench_data)<-c('X0')
colnames(test_dfsio_read1_data)<-c('X0')
colnames(test_dfsio_write1_data)<-c('X0')
colnames(test_dfsio_read2_data)<-c('X0')
colnames(test_dfsio_write2_data)<-c('X0')
colnames(teragenerate_data)<-c('X0')
colnames(terasort_data)<-c('X0')
colnames(teravalidate_data)<-c('X0')

# calculating time
time_mrbench <- seq(1, nrow(mrbench_data), by=1)
time_dfsio_read1 <- seq(1, nrow(test_dfsio_read1_data), by=1)
time_dfsio_write1 <- seq(1, nrow(test_dfsio_write1_data), by=1)
time_dfsio_read2 <- seq(1, nrow(test_dfsio_read2_data), by=1)
time_dfsio_write2 <- seq(1, nrow(test_dfsio_write2_data), by=1)
time_teragenerate <- seq(1, nrow(teragenerate_data), by=1)
time_terasort <- seq(1, nrow(terasort_data), by=1)
time_teravalidate <- seq(1, nrow(teravalidate_data), by=1)

# integrating data from all benchmarks
cpu_usage <- c(mrbench_data$X0, test_dfsio_read1_data$X0, test_dfsio_write1_data$X0, test_dfsio_read2_data$X0, test_dfsio_write2_data$X0, 
            teragenerate_data$X0, terasort_data$X0, teravalidate_data$X0);
time <- c(time_mrbench, time_dfsio_read1, time_dfsio_write1, time_dfsio_read2, time_dfsio_write2, 
          time_teragenerate, time_terasort, time_teravalidate)
benchmark <- c(rep.int(c("MRBench"), nrow(mrbench_data)), rep.int(c("TestDFSIO Read (I)"), nrow(test_dfsio_read1_data)), 
          rep.int(c("TestDFSIO Write (I)"), nrow(test_dfsio_write1_data)), rep.int(c("TestDFSIO Read (II)"), nrow(test_dfsio_read2_data)),
            rep.int(c("TestDFSIO Write (II)"), nrow(test_dfsio_write2_data)), rep.int(c("Teragenerate"), nrow(teragenerate_data)),
               rep.int(c("Terasort"), nrow(terasort_data)), rep.int(c("Teravalidate"), nrow(teravalidate_data)))

data <- data.frame(time, cpu_usage, benchmark)

plot <- ggplot(data, aes(time, cpu_usage)) + geom_point()
png("plot.png")

plot + facet_grid(. ~ benchmark) + opts(title = expression("Consumo de CPU em %"))


