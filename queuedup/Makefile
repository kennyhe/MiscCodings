all: addqueueup

addqueueup: main.c queue.h queue.c
	gcc -O2 -o addqueueup main.c queue.c

debug: main.c queue.h queue.c
	gcc -O0 -DDEBUG -o addqueueup_dbg main.c queue.c

.PHONY: clean clean_all
clean:
	-rm -rf *.o addqueueup addqueueup_dbg

clean_all:
	-rm -rf *.o addqueueup addqueueup_dbg result

