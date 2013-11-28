all: addqueueup

addqueueup: Atypon.c queue.h queue.c
	gcc -O2 -o addqueueup Atypon.c queue.c

debug: Atypon.c queue.h queue.c
	gcc -O0 -DDEBUG -o addqueueup_dbg Atypon.c queue.c

.PHONY: clean clean_all
clean:
	-rm -rf *.o addqueueup addqueueup_dbg

clean_all:
	-rm -rf *.o addqueueup addqueueup_dbg result

