/*
 * queue.c
 *
 *  Created on: Oct 8, 2013
 *      Author: she@scu.edu
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "queue.h"

// Define the size of the hash list
#define HASH_SIZE 20

// The data structure for storing a single element.
typedef struct element {
    char* id; // The pointer to the id of the element
    struct element* prev; // The previous item in the queue
    struct element* next; // The next item in the queue
    struct element* succ; // The successive item in the hash list
} ELEMENT;


/*
 * The hashed array list, for quick search of the elements:
 * Each item in this array stores the header of a linked list (linked by ELEMENT.succ), and each linked list
 * stores the elements whose ID's hash value equals to the index of the header in the array.
 * The new element will always be inserted in the end of the list, because in real life, the subjects who have
 * waited for longer time are more likely to leave the queue.
 */
ELEMENT hash_lists[HASH_SIZE];
ELEMENT* hash_lists_ins_point[HASH_SIZE]; // The pointer to the last item in each list, for quick insert to the end of the list.

/* The Queue, stored in a bi-directional linked list(linked by ELEMENT.prev and ELEMENT.next) starts with
 * head and end with rear.
 */
ELEMENT head, rear;


void init_queue() {
    // Initialize the bi-directional queue
    head.next = & rear;
    head.prev = NULL;
    rear.next = NULL;
    rear.prev = & head;

    // Initialize the hash list
    int i = 0;
    while (i < HASH_SIZE) {
        hash_lists[i].succ = NULL;
        hash_lists_ins_point[i] = &(hash_lists[i]);
        ++i;
    }
}


void delete_queue() {
    /* Visits the elements and free the memory space used by them.
     * Starts visiting from the second element, and free the memory used by previous element.
     */
    ELEMENT* p = head.next->next;
    while (p) { // p != NULL
        free(p->prev->id); // Frees the space for storing the ID string
        free(p->prev);     // Frees the space for the element structure
        p = p->next;
    }
}


/**
 * Get the hash value of a string key.
 * @param key The key to be hashed
 * @return An unsigned integer ranges in [0, HASH_SIZE-1]
 */
unsigned hash(char* key) {
    unsigned sum = 0;
    // Sum the ASCII value of all characters
    while (*key) {
        sum += *key;
        key++;
    }
    // Mod HASH_SIZE, then get the hash value of this string.
    return sum % HASH_SIZE;
}


void add_element(char* key) {
    // Allocates memory space for the element, stores the ID string and the pointers
    ELEMENT* e = (ELEMENT*)malloc(sizeof(ELEMENT));
    if (NULL == e) {
        printf("System out of memory!");
        return;
    }

    e->id = (char*)malloc(strlen(key) + 1);
    if (NULL == e->id) {
        printf("System out of memory!");
        return;
    }
    strcpy(e->id, key);

    // Inserts the element to the rear of the bi-directional linked list.
    e->prev = rear.prev;
    rear.prev->next = e;
    e->next = &rear;
    rear.prev = e;
    e->succ = NULL;

    // Insert the element to end of the right hashed linked list
    unsigned index = hash(key);
    hash_lists_ins_point[index]->succ = e;
    hash_lists_ins_point[index] = e;
}


char* poll_element() {
    // Retrieves the element from the head
    ELEMENT* e = head.next;
    if (&rear == e) // The Queue is empty
        return NULL;

    // Removes the element from the bi-directional linked list
    head.next = e->next;
    e->next->prev = &head;

    // Remove the element from the hashed list
    unsigned index = hash(e->id);
    ELEMENT* left = &(hash_lists[index]);
    ELEMENT* right = left->succ;
    while (right != e) {
        left = right;
        right = right->succ;
    }
    left->succ = e->succ;
    if (! e->succ) { // e->succ == NULL, it's the last element of in the hash list
    	hash_lists_ins_point[index] = left;
    }

    // Free the memory space used by the structure
    char* tmp = e->id;
    free(e);
    return tmp;
}


void remove_element(char* key) {
    // Locates the element in the hashed list
    ELEMENT* e = NULL;
    unsigned index = hash(key);
    ELEMENT* left = &(hash_lists[index]);
    e = left->succ;
    while (e && strcmp(e->id, key)) { // When e != NULL, compare two strings: e->id != key
        left = e;
        e = e->succ;
    }

    if (! e) { // e == NULL
        printf("Element %s is not in the queue!\n", key);
        return;
    }

    // Remove the element from the hashed list
    left->succ = e->succ;
    if (! e->succ) { // e->succ == NULL, it's the last element of in the hash list
    	hash_lists_ins_point[index] = left;
    }

    // Removes the element from the bi-directional linked list
    e->next->prev = e->prev;
    e->prev->next = e->next;

    // Free the memory space used by the structure
    free(e->id);
    free(e);
}
