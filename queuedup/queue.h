/*
 * queue.h
 *
 *  Created on: Oct 8, 2013
 *      Author: she@scu.edu
 */

#ifndef QUEUE_H_
#define QUEUE_H_


/**
 * Initialize the queue.
 */
void init_queue();


/**
 * Free the memory used by the queue.
 */
void delete_queue();


/**
 * Inserts an element into the queue, and puts it into the hash list.
 * @param key The ID of the element to be added into the queue
 */
void add_element(char* key);


/**
 * Retrieves and removes the item from the head of the queue, and returns its ID.
 * @return The ID of the polled element, or zero if the queue is empty
 * @Note The caller should be responsible for free the memory space pointed by the returned pointer
 */
char* poll_element();


/**
 * Removes the element whose ID equals to the key.
 * @param key The ID of the element to be removed
 */
void remove_element(char* key);


#endif /* QUEUE_H_ */
