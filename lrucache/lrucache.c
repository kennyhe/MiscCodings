#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define STR_MAX_SIZE 11

/**
 * The two macro below are for two unclear requirements:
 * 1. Whether the SET operation be treated as a use, default true;
 * 2. Whether the key sorting is case sensitive or not, default false.
 */
#define SET_IS_USE
//#define SORT_IGNORE_CASE


/** The node type declaration */
typedef struct node {
    char key[STR_MAX_SIZE];
    char value[STR_MAX_SIZE];

    // The pointers in the cache linked list
    struct node *prev, * next;
 
    // The pointers in the key sorting map
    struct node *lchild, *rchild;
} NODE;

typedef int bool;
#define true -1
#define false 0


/** The global variables for the cache */
NODE head, rear;
size_t bound;
int empty_count;
NODE* root; // The root of sorted map


/* Below are the key functions related to the operations */

/** init the cache, the parameter is for the first bound. */
int init(size_t b);

/** resize the cache, BOUND */
void resize(size_t b);

/** SET an item */
void put(char* key, char* value);

/** get an item by key. If use == 0, it's PEEK, else it's GET. */
char* get(char* key, bool use);

/** DUMP, print all the items */
void dump();


/* Below are supporting functions */

/** creates a new node */
NODE* create_node(char* key, char* value);

/** insert or promote node when it's SET or used */
void promote(NODE * node);

/** increase bound */
void inc_bound(size_t b);
/** decrease bound */
void dec_bound(size_t b);

/** delete the nodes until next is NULL or &rear */
void remove_nodes(NODE* p);


/** rebuild the keyword sorting tree */
#ifdef SORT_IGNORE_CASE
void rebuild_tree(bool ignore_case);
#else
void rebuild_tree();
#endif


/** add a leaf under a tree node, call recursively */
#ifdef SORT_IGNORE_CASE
void add_leaf(NODE* leaf, NODE* node, bool ignore_case);
#else
void add_leaf(NODE* leaf, NODE* node);
#endif


/** visit the tree with in-order and print the nodes */
void print_node(NODE* tree);
/** visit the tree with pre-order and fetch the value by key */
NODE* fetch(char* key, NODE* node);


/** compare keys, reimplement of strcmp. Allow case insensitive compare */
#ifdef SORT_IGNORE_CASE
int scmp(char* s1, char* s2, bool ignore_case);
#endif


/* The main function */
int main() {
    int rows, i, bound;
    if (scanf("%d", &rows) != 1) return -1;
    if (rows <= 0) return -1;
    
    char cmd[STR_MAX_SIZE];
    char key[STR_MAX_SIZE];
    char value[STR_MAX_SIZE];
    char* v = NULL;

    if (scanf("%s", cmd) != 1) return -1;

    if (strcmp(cmd, "BOUND")) return -1;

    if (scanf("%d", &bound) != 1) return -1;

    if (bound <= 0) return -1;

    init(bound);
    
    for (i = 1; i < rows; i++) {
        if (scanf("%s", cmd) != 1) break;
        if (strcmp(cmd, "BOUND") == 0) {
            if (scanf("%d", &bound) != 1) break;
            if (bound <= 0) break;
            resize(bound);
        } else if (strcmp(cmd, "SET") == 0) {
            if (scanf("%s", key) != 1) break;
            if (scanf("%s", value) != 1) break;
            put(key, value);
        } else if (strcmp(cmd, "GET") == 0) {
            if (scanf("%s", key) != 1) break;
            v = get(key, true);
            if (v)
                printf("%s\n", v);
            else
                printf("NULL\n");
        } else if (strcmp(cmd, "PEEK") == 0) {
            if (scanf("%s", key) != 1) break;
            v = get(key, false);
            if (v)
                printf("%s\n", v);
            else
                printf("NULL\n");
        } else if (strcmp(cmd, "DUMP") == 0) {
            dump();
        } else
            break;
    }

    // free the memory used by the cache
    rear.prev->next = NULL;
    remove_nodes(head.next);
    return 0;
}


NODE* create_node(char* key, char* value) {
    NODE* n = (NODE*) malloc(sizeof (NODE));
    strncpy(n->key, key, STR_MAX_SIZE);
    strncpy(n->value, value, STR_MAX_SIZE);
    n->prev = n->next = n->lchild = n->rchild = NULL;
    return n;
}


void promote(NODE* node) {
    if (node->prev)
       node->prev->next = node->next;
    if (node->next)
       node->next->prev = node->prev;

    node->next = head.next;
    head.next->prev = node;
    node->prev = &head;
    head.next = node;
}

void inc_bound(size_t b) {
    empty_count += (b - bound);
    bound = b;
}


void dec_bound(size_t b) {
    empty_count -= (bound - b);
    
    if (empty_count < 0) {
       // Need to delete empty_count nodes from the end
       NODE *p = &head;
       size_t i = 0;
       while (i < b) {
           i++;
           p = p->next;
       }
       NODE* q = p->next;
       p->next = &rear;
       rear.prev->next = NULL;
       rear.prev = p;

       // delete the nodes from q and rebuild the sorting tree
       remove_nodes(q);
       rebuild_tree(false);

       empty_count = 0;
    }
    bound = b;
}


void remove_nodes(NODE *p) {
    NODE *q;
    while (p) {
        q = p->next;
        free(p);
        p = q;
    }
}


#ifdef SORT_IGNORE_CASE
void add_leaf(NODE* leaf, NODE* node, bool ic) {
    if (scmp(leaf->key, node->key, ic) < 0) {
#else
void add_leaf(NODE* leaf, NODE* node) {
    if (strcmp(leaf->key, node->key) < 0) {
#endif
        if (node->lchild) {
#ifdef SORT_IGNORE_CASE
            add_leaf(leaf, node->lchild, ic);
#else
            add_leaf(leaf, node->lchild);
#endif
        } else {
            node->lchild = leaf;
            leaf->lchild = leaf->rchild = NULL;
        }
    } else {
        if (node->rchild) {
#ifdef SORT_IGNORE_CASE
            add_leaf(leaf, node->rchild, ic);
#else
            add_leaf(leaf, node->rchild);
#endif
        } else {
            node->rchild = leaf;
            leaf->lchild = leaf->rchild = NULL;
        }
    }
}


void print_node(NODE* node) {
    if (node->lchild)
        print_node(node->lchild);

    printf("%s %s\n", node->key, node->value);

    if (node->rchild)
        print_node(node->rchild);
}


NODE* fetch(char* key, NODE* node) {
    if (! node) return NULL;

    int cmp = strcmp(key, node->key);
    if (cmp == 0) {
        return node;
    } else if (cmp < 0) {
        return fetch(key, node->lchild);
    } else {
        return fetch(key, node->rchild);
    }
}


#ifdef SORT_IGNORE_CASE
void rebuild_tree(bool ic) {
#else
void rebuild_tree() {
#endif
    NODE* p = head.next;
    if (p == &rear) {
        root = NULL;
        return;
    }
    root = head.next;
    root->lchild = root->rchild = NULL;
    p = root->next;

    while (p != &rear) {
#ifdef SORT_IGNORE_CASE
        add_leaf(p, root, ic);
#else
        add_leaf(p, root);
#endif
        p = p->next;
    }
}

#ifdef SORT_IGNORE_CASE
int scmp(char* s1, char* s2, bool ignore_case) {

    if (ignore_case) {
        if (((*s1 >= 'a' && *s1 <='z') || (*s1 >= 'A' && *s1 <='Z')) && 
            ((*s2 >= 'a' && *s2 <='z') || (*s2 >= 'A' && *s2 <='Z'))) {
            char c1 = *s1, c2 = *s2;
            // convert them into uppercase
            if (c1 >= 'a') c1 -= 32;
            if (c2 >= 'a') c2 -= 32;
            if (c1 > c2)
                return 1;
            else if (c1 < c2)
                return -1;
            else
                return (s1 + 1, s2 + 1, true);
        } else if (*s1 > *s2) {
            return 1;
        } else if (*s1 < *s2) {
            return -1;
        } else {
            if (*s1)
                return (s1 + 1, s2 + 1, true);
            else
                return true;
        }
    } else {
        return strcmp(s1, s2);
    }
}
#endif


// Key operations

int init(size_t b) {
    head.prev = rear.next = NULL;
    head.next = &rear;
    rear.prev = &head;
    bound = b;
    empty_count = b;
    root = NULL;
}


void resize(size_t b) {
    if (b > bound)
        inc_bound(b);
    else
        dec_bound(b);
}


void put(char* key, char* value) {
    NODE *node = NULL;
    if (root)
        node = fetch(key, root);
    if (node) {
        // update the value
        strncpy(node->value, value, STR_MAX_SIZE);
#ifdef SET_IS_USE
        promote(node);
#endif
    } else {
        if (empty_count) { // root == NULL case is covered here also
           node = create_node(key, value);
           empty_count--;
#ifdef SET_IS_USE
            promote(node);
#endif

#ifdef SORT_IGNORE_CASE
            if (root)
                add_leaf(node, root, false);
            else
                rebuild_tree(false); // only for the first element
#else
            if (root)
                add_leaf(node, root);
            else
                rebuild_tree(); // only for the first element
#endif
        } else {
           node = rear.prev;
           strncpy(node->key, key, STR_MAX_SIZE);
           strncpy(node->value, value, STR_MAX_SIZE);
#ifdef SET_IS_USE
            promote(node);
#endif
#ifdef SORT_IGNORE_CASE
            rebuild_tree(false);
#else
            rebuild_tree();
#endif
        }
    }
}


char* get(char* key, bool use) {
    if (! root) return NULL;
    
    NODE *node = fetch(key, root);
    if (node) {
        if (use) promote(node);
        return node->value;
    } else
        return NULL;
}


void dump() {
    // dump2();
    if (! root) return;

#ifdef SORT_IGNORE_CASE
    // if print in alphabet order ignore case, then need to re-sort the tree
    rebuild_tree(true);
#endif

    print_node(root);

#ifdef SORT_IGNORE_CASE
    rebuild_tree(false);
#endif
}
