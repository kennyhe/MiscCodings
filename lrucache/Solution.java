/*
 Enter your code here. Read input from STDIN. Print output to STDOUT
 Your class should be named Solution
*/
import java.util.TreeMap;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;

class Solution {

    public static void main(String[] args) {
        try{
            java.io.BufferedReader stdin = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            String line = stdin.readLine();
            if ((line == null) || (! line.matches("\\d+"))) {
                println("Invalid input! Should input a number which indicates the count of commands lines.");
                return;
            } else {
                int rows = Integer.parseInt(line);
                line = stdin.readLine();
                if ((line == null) || (! line.matches("BOUND\\s\\d+"))) {
                    println("The first command must be BOUND <cache size>!");
                    return;
                } else {
                    LRUCache cache = new LRUCache(Integer.parseInt(line.substring(6)));
                    for (int i = 1; i < rows; i++) {
                        line = stdin.readLine();
                        if (line == null) {
                            println("Expected " + rows + " lines input but reached the end of file in line " + i);
                            return;
                        }
                        // println("--->" + line);
                        if (line.matches("BOUND\\s\\d+")) {
                            cache.setBound(Integer.parseInt(line.substring(6)));
                        } else if (line.matches("GET\\s\\S+")) {
                            println(cache.get(line.substring(4)));
                        } else if (line.matches("PEEK\\s\\S+")) {
                            println(cache.get(line.substring(5), false));
                        } else if (line.matches("DUMP")) {
                            println(cache.toString());
                        } else if (line.matches("SET\\s\\S+\\s\\S+")) {
                            String[] parts = line.split("\\s");
                            cache.put(parts[1], parts[2]);
                        } else {
                            println("Syntax error in line " + i + ":" + line);
                            return;
                        }
                    }
                }
            }
        }catch (java.io.IOException e) {
            System.out.println(e); 
        }
    }

    private static void println(String s) {
        if (s == null) {
            System.out.println("NULL");
        } else {
            System.out.println(s);
        }
    }
}



class LRUCache {
    public static final int DEFAULT_BOUND = 64;
    private int bound = 0;
    private int countEmpty = 0;
    private CacheUnit head, rear;
    private boolean useWhenSet = true;         // Whether the set is counted as used
    private boolean keyCaseSensitive = true;   // Whether key is sensitive
    private HashMap<String, CacheUnit> hashmap = new HashMap<>();

    LRUCache(int bound, boolean useWhenSet, boolean kcs) {
        this(bound);
        this.useWhenSet = useWhenSet;
        this.keyCaseSensitive = kcs;
    }
    
    LRUCache(int bound) {
        head = new CacheUnit(null, null);
        rear = new CacheUnit(null, null);
        head.next = rear;
        rear.prev = head;
        
        if (bound <= 0)
            incBound(DEFAULT_BOUND);
        else
            incBound(bound);
    }


    public void setBound(int n) {
        if (n <= 0)
            return;

        if (n > bound)
            incBound(n);
        else
            decBound(n);
    }

    // set operaion: If the key already exists, do not set, but update the cache.
    // The set operation (1st time, or update in later operations) will be counted as one use.
    public void put(String key, String value) {
        CacheUnit cu = hashmap.get(key);
        if (cu != null) { // key exist
            cu.value = value;
            if (useWhenSet) promote(cu);
            return;
        }

        if (countEmpty > 0) {
            cu = new CacheUnit(key, value);
            if (useWhenSet) {
                promote(cu);
            } else { // append to end
                cu.prev = rear.prev;
                rear.prev.next = cu;
                cu.next = rear;
                rear.prev = cu;
            }
            countEmpty --;
        } else {
            // Replace the last element in the linked list
            cu = rear.prev;
            hashmap.remove(cu.key); // remove the old key/node pair from the hashmap
            cu.key = key;
            cu.value = value;
            if (useWhenSet) promote(cu);
        }
        hashmap.put(key, cu);
    }
    
    /* Move the CacheUnit to the head of the list */
    private void promote(CacheUnit cu) {
        if (cu.prev != null)
            cu.prev.next = cu.next;
        
        if (cu.next != null)
            cu.next.prev = cu.prev;
            
        cu.prev = head;
        cu.next = head.next;
        head.next.prev = cu;
        head.next = cu;
    }
    
    public String get(String key) {
        return get(key, true);
    }

    public String get(String key, boolean used) {
        CacheUnit cu = hashmap.get(key);
        if (cu == null)
            return null; // key not exist in the cache

        if (used)
            promote(cu);
        return cu.value;
    }

    public String toString() {
        TreeMap<String, String> map = new TreeMap<>();
        StringBuffer sb = new StringBuffer();

        CacheUnit unit = head.next;
        while (unit != rear) {
            map.put( ((keyCaseSensitive)?unit.key.toUpperCase(): unit.key), unit.key + " " + unit.value);
            unit = unit.next;
        };

        Iterator<String> keys = map.navigableKeySet().iterator();
        String key;
        if (keys.hasNext()) {
            key = keys.next();
            sb.append(map.get(key));
        }

        while (keys.hasNext()) {
            key = keys.next();
            sb.append("\n").append(map.get(key));
        }
        
        return sb.toString();
    }

    private void incBound(int n) {
        countEmpty += n - bound;
        bound = n;
    }

    private void decBound(int n) {
        countEmpty -= bound - n;
        if (countEmpty < 0) {
            // need to delete last (- countEmpty) elements
            CacheUnit p, q;
            int i = 0;
            if (n + countEmpty > 0) {
                // move from rear;
                p = rear.prev;
                while (i > countEmpty) { // && p != head) {
                    i--; p = p.prev;
                }
            } else {
                // Move from head
                p = head;
                while (i < n) { // && p != rear) {
                    i++; p = p.next;
                }
            }
            q = p.next;
            q.prev = null;
            rear.prev.next = null;
            p.next = rear;
            rear.prev = p;
            
            // Clear the records from the hashmap
            while (q != null) {
                p = q;
                hashmap.remove(q.key);
                q = q.next;
                // Remove the links to make the objects zero reference and can be recalled in the garbage collection
                if (p.prev != null) {
                    p.prev.next = null;
                    p.prev = null;
                    p = null;
                }
            }
            
            countEmpty = 0;
        }
        bound = n;
    }

}


/* CacheUnit: Cache unit. It contains key, value, ranker information. */

class CacheUnit {
    String key;
    String value;
    CacheUnit prev = null, next = null;

    CacheUnit(String key, String value) {
        this.key = key;
        this.value = value;
    }
}

