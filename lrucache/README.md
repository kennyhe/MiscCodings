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
