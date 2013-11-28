/*
 * Atypon.c
 *
 *  Created on: Oct 8, 2013
 *      Author: she@scu.edu
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "queue.h"
#define BUF_WIDTH 1024

int main() {
    init_queue();

    char buf[BUF_WIDTH];

    while (fgets(buf, BUF_WIDTH, stdin)) {
        /* fixes the string read from the terminal by removing the last '\n' */
        int pos = strlen(buf) - 1;
        if ((pos > 0) && ('\n' == buf[pos]))
            buf[pos] = '\0';

        /* parses the command */
        if ((buf[0] == 'a') &&
    			(buf[1] == 'd') &&
    			(buf[2] == 'd') &&
    			(buf[3] == ' ')) {
    		add_element(buf + 4);
    	} else if ((buf[0] == 'n') &&
    			(buf[1] == 'e') &&
    			(buf[2] == 'x') &&
    			(buf[3] == 't') &&
    			(buf[4] == '\0')) {
    		char* key = poll_element();
    		if (key) {
    			printf("%s\n", key);
    			free(key); // Need to free
    		}
    	} else if ((buf[0] == 'r') &&
    			(buf[1] == 'e') &&
    			(buf[2] == 'm') &&
    			(buf[3] == 'o') &&
    			(buf[4] == 'v') &&
    			(buf[5] == 'e') &&
    			(buf[6] == ' ')) {
    		remove_element(buf + 7);
    	} else {
    		printf("Could not recognize this command: %s\n", buf);
    	}
    }

    delete_queue();
}
