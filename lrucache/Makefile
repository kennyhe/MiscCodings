all: lru Solution.class
lru: lrucache.c
	gcc -O2 -o lru lrucache.c
Solution.class: Solution.java
	javac Solution.java
clean:
	rm -f lru
