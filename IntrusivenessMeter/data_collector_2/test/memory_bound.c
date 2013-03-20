# include <stdio.h>
# include <stdlib.h>
# include <string.h>
# include <unistd.h>

/*
    In seconds
*/
# define SLEEP_TIME_1 20
# define SLEEP_TIME_2 20

/*
   100 MB
*/
# define TO_ALLOCATE_1 1024 * 1024 * 100

/*
   100 MB
*/
# define TO_ALLOCATE_2 1024 * 1024 * 100

int main(void)
{
	char *data = (char *) malloc(TO_ALLOCATE_1);
	memset(data, 1, TO_ALLOCATE_1);

	sleep(SLEEP_TIME_1);
	
	char *data2 = (char *) malloc(TO_ALLOCATE_2);
	memset(data2, 2, TO_ALLOCATE_2);

	sleep(SLEEP_TIME_2);

	return 0;
}
