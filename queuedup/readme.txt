Candiate Name: Shouchun He    Email: she@scu.edu, he.scu2013@gmail.com   Phone: (408)310-0723   LinkedIn profile: http://www.linkedin.com/in/kennyhe2002/


Solution for the "Add Queue Up" problem

1. Compile
    Prefer to compile and run in Linux environment (haven't tried on Windows and OS X yet but hopefully there will be no big issues).
    Run "make" in the folder and then get the executable "addqueueup".

2. Run
    The program read the input from stdin, and write the output to stdout. So you can run it and type the input line by line, or simply redirect the input from a file (e.g. the sample.txt) and redirect the output to another file (e.g. "result").
         ./addqueueup < test/sample.txt > result

3. How I solve the problem
    Since this is a queue problem, so a bi-directional linked list need to be used as the major data structure for storing the elements to get maximum time and space efficiency.
    However, unlike a normal queue, this queue needs to support "removing elements from the middle by name/id" feature. A very basic implementation is to scan the queue and locate the element with the name/id, and remove it. When the length of the queue is N, the average comparing time will be N/2. That's inefficient.
    My solution is introducing a hash list (http://en.wikipedia.org/wiki/Hash_list) to speed up the locating of the element. This solution brings some extra cost of time and space to calculate the hash and maintain the hash list, but it reduces the average comparing time to as lower as N/2/k. Here k is the range of output of the hash function.

4. How I guarantee the efficiency and maintainability of the code
  1) Time efficiency: By using the hash list, the average number of string comparison operations -- the most significant time consuming operation in this problem -- can be reduced from N/2 to N/2/k. Here I used a very simple hash function which adds the ASCII code of all charaters in a string and then mod by 20. The hash function will not cause too much CPU time but can evently scatter the hash value evenly between 0 and 19.
  2) Space efficiency: Except for the empty head and rear elements of the bi-directional linked list and the hash list, the memory space for all other elements are dynamically allocated and no spaces wasted. When the elements are deleted, the corresponding memory space will be free.
  3) Readability: I added necessary comments in the code to describe the usage of APIs, data structures, purposes of the code pieces. I always follow the most widely used coding style guide of the software industry (e.g. Google coding stylei for C/C++, python, Java/Android, etc.) to keep the smooth communication with the peers.

It takes me three hours to solve this problem (design, coding, debug).
