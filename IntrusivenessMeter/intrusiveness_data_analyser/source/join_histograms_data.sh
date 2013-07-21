#!/bin/bash -e

CPU_DATA_FILE_1SEC=$1
CPU_DATA_FILE_10SEC=$2
CPU_DATA_FILE_100SEC=$3
MEMORY_DATA_FILE_1SEC=$4
MEMORY_DATA_FILE_10SEC=$5
MEMORY_DATA_FILE_100SEC=$6

RESULT_CPU_DATA_FILE="result.data.cpu"
RESULT_MEMORY_DATA_FILE="result.data.memory"

touch $RESULT_CPU_DATA_FILE
touch $RESULT_MEMORY_DATA_FILE

HEADER_CPU_DATA_FILE="timestamp cpu discomfort"
HEADER_MEMORY_DATA_FILE="timestamp memory discomfort"

echo "$HEADER_CPU_DATA_FILE interval" >> $RESULT_CPU_DATA_FILE
echo "$HEADER_MEMORY_DATA_FILE interval" >> $RESULT_MEMORY_DATA_FILE

# process cpu data
while read line; do
	if [ "$line" != "$HEADER_CPU_DATA_FILE" ] ; then
		echo "$line 1" >> $RESULT_CPU_DATA_FILE
	fi
done < $CPU_DATA_FILE_1SEC

while read line; do
	if [ "$line" != "$HEADER_CPU_DATA_FILE" ]; then
		echo "$line 10" >> $RESULT_CPU_DATA_FILE
	fi
done < $CPU_DATA_FILE_10SEC

while read line; do
	if [ "$line" != "$HEADER_CPU_DATA_FILE" ]; then
		echo "$line 100" >> $RESULT_CPU_DATA_FILE
	fi
done < $CPU_DATA_FILE_100SEC

# process memory data
while read line; do
	if [ "$line" != "$HEADER_MEMORY_DATA_FILE" ]; then
		echo "$line 1" >> $RESULT_MEMORY_DATA_FILE
	fi
done < $MEMORY_DATA_FILE_1SEC

while read line; do
	if [ "$line" != "$HEADER_MEMORY_DATA_FILE" ]; then
		echo "$line 10" >> $RESULT_MEMORY_DATA_FILE
	fi
done < $MEMORY_DATA_FILE_10SEC

while read line; do
	if [ "$line" != "$HEADER_MEMORY_DATA_FILE" ]; then
		echo "$line 100" >> $RESULT_MEMORY_DATA_FILE
	fi
done < $MEMORY_DATA_FILE_100SEC
