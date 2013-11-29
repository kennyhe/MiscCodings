#LRU Cache simulation (C code)


##WARNING: This code piece is a response to a job interview challenge homework.
##If you are going to response that challenge, please STOP reading anything in this project and DO IT YOURSELF!!! Reading my code and ideas may help you pass this round of code challenge but you may not really benefit from it!


This short program simulate an LRU cache. It's response to an interview challenge homework.

There are several ways to implement LRU. 

###Solution 1
In the CACHE system, all the cache elements are physically adjacent in a consecutive memory. So people always use lots of bits for each Cache element to track the recent usage status. Whenever there is an access, right shift the bits. And always replace the element with smallest bits.

###Solution 2
For simulation purpose only: To store the Cache elements in a linked list. When a cache element is accessed, move it to the head of the list; when replace happens, always replace the element at the rear of the list and move the element to the head.

This solution is much better in performance.

## Element fetching
By the way, for quick fetching the relative value in the cache by the key, all the cache elements should be logically arranged in a quick indexing system, such as: A binary tree, a hashmap, etc. When the elements are added or replaced, such binary tree or hashmap also need to be updated.


##Test case generator
The TestCase.java is a test case generator for this program.
<pre>To compile:
javac TestCase.java
To run:
java TestCase &gt; test_data_file_name
Then open the test_data_file, check how many commands in the file, 
and put the count of rows in the first line.
</pre>

##Requirements

The code read input from stdin.

Line 1: The count of the commands.

Line 2: The first command, must be BOUND <cache size>

Other lines: Should be one of the following command:

BOUND <new cache size>

SET <key> <value>  # both <key> and <value> should be string no longer than 10 bytes.

GET <key>  # get the <value> if it's in the cache, else return NULL. It will use the cache element.

PEEK <key> # get the <value> but do not use the cache element.

DUMP # print the contents in the cache in the alphabet order of the keys

### Ambiguity of the original requirements
1. The requirements did not say whether the keys should be case sensitive or not, when comparing and dumping. In my implementation, I control it with MACRO so the user can complile for different options.
2. It also not say how to handle the SET when the <key> exists. Replacing, or simply ignore? Here I assume it's replacing.
3. It did not say whether the SET operation is treated as a USE. I also use MACRO to control the options.
