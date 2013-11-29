#LRU Cache simulation (C code)


##WARNING: This code piece is a response to a job interview challenge homework.
##If you are going to response that challenge, please STOP reading anything in this project and DO IT YOURSELF!!! Reading my code and ideas may help you pass this round of code challenge but you may not really benefit from it!


This short program simulate an LRU cache. It's response to an interview challenge homework.

Need to consider the performance issue.

The code read input from stdin.

Line 1: The count of the commands.

Line 2: The first command, must be BOUND <cache size>

Other lines: Should be one of the following command:

BOUND <new cache size>

SET <key> <value>  # both <key> and <value> should be string no longer than 10 bytes.

GET <key>  # get the <value> if it's in the cache, else return NULL. It will use the cache element.

PEEK <key> # get the <value> but do not use the cache element.

DUMP # print the contents in the cache

##Test case generator
The TestCase.java is a test case generator for this program.

<pre>
To compile:
javac TestCase.java

To run:
java TestCase &gt; test_data_file_name

Then open the test_data_file, check how many commands in the file, 
and put the count of rows in the first line.
