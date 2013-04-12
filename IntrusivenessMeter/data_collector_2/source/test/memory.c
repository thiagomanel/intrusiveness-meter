/*
	 Federal University of Campina Grande
	 Distributed Systems Laboratory
	
	 Author: Armstrong Mardilson da Silva Goes
	 Contact: armstrongmsg@lsd.ufcg.edu.br
*/

/*
         This program produces a memory bound workload.
         It allocates TO_ALLOCATE_1 bytes, fills the memory
         using memset and then sleeps 
         SLEEP_TIME_1 seconds. After that, it allocates 
         TO_ALLOCATE_2 bytes, fills the memory using memset and
         then sleeps SLEEP_TIME_2 seconds.
*/

# include <stdio.h>
# include <stdlib.h>
# include <string.h>
# include <unistd.h>

/*
    In seconds
*/
# define SLEEP_TIME_1 100
# define SLEEP_TIME_2 100

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
