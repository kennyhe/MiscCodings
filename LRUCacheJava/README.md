#LRU Cache

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


## Ambiguity of the original requirements
1. The requirements did not say whether the keys should be case sensitive or not, when comparing and dumping. In my implementation, I control it with MACRO so the user can complile for different options.
2. It also not say how to handle the SET when the <key> exists. Replacing, or simply ignore? Here I assume it's replacing.
3. It did not say whether the SET operation is treated as a USE. I also use MACRO to control the options.


#Original Requirements

Caches are a key element in scaling a system. One popular form of cache is called a Least Recently Used Cache (http://en.wikipedia.org/wiki/Cache_algorithms#Least_Recently_Used). Your task is to implement a cache that can be tested against a series of inputs. These actions should define an API you use for the cache object.

Your cache should store simple key/value strings of length up to 10 characters. It should also have a customizable upper bound to the number of keys that can be stored in the cache at any time. You do not have to be thread safe.

##Possible Inputs:

BOUND    :  Set the upper bound. If the cache size is currently greater than this number, then extra entries must be removed following the LRU policy

SET   :  Set the value of this key

GET   :  Get the value of this key and prints to stdout.

PEEK   :  Gets the value of the key but does not mark it as being used. Prints the value to standard out.

DUMP  :  Prints the current state of the cache as a list of key/value pairs in alphabetical order by key.

 

###Input Format:

First line of input contains an integer N,the number of commands.

The following N lines each describe a command.

Note: The first command will always be BOUND.

###Output Format:

Print the appropriate outputs for GET , PEEK and DUMP commands. In case for GET/PEEK command if the key does not exist in the cache just output the string "NULL"(quotes are for clarity).

 

###Sample Input
<pre>8
BOUND 2
SET a 2
SET b 4
GET b
PEEK a
SET c 5
GET a
DUMP</pre>

###Sample Output
<pre>4
2
NULL
b 4
c 5</pre>

##Constraints:

Total number of lines in input will be no more than 1,000,000(10^6)

###Note: There may be DUMP commands scattered throughout the input file.
