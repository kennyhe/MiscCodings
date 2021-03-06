import java.util.TreeMap;
import java.util.Iterator;

class LRUCache {
    public static final int DEFAULT_BOUND = 63;
    private int bound = 0;
    private int countEmpty = 0;
    private CacheUnit[] units = null;
    
    private HashList hashlist = new HashList();

    LRUCache(int bound) {
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
        CacheUnit cu = hashlist.searchKey(key);
        if (cu != null) { // key exist
            cu.value = value;
            // updateRankers(cu, true); // reset the ranker of this unit and right shift the others
            return;
        }

        int pos = getNextInsPos();
        String oldKey = units[pos].key;
        units[pos].key = key;
        units[pos].value = value;
        if (units[pos].isEmpty()) {
            hashlist.addCacheUnit(units[pos]);
            countEmpty--;
        } else {
            hashlist.updateCacheUnitPos(units[pos], oldKey);
        }
        // updateRankers(units[pos], true);
    }

    public String get(String key) {
        return get(key, true);
    }

    public String get(String key, boolean used) {
        CacheUnit cu = hashlist.searchKey(key);
        if (cu == null)
            return null; // key not exist in the cache

        if (used)
            updateRankers(cu);
        return cu.value;
    }

    public String toString() {
        TreeMap<String, String> map = new TreeMap<>();
        StringBuffer sb = new StringBuffer();

        int i = 0;
        while (i < bound - countEmpty) {
            map.put(units[i].key.toUpperCase(), units[i].key + " " + units[i].value + units[i].ranker.toString());
            i++;
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

    /** Right shift all the rankers, and set 1 for the ranker of unit cu if it's not null */
    private void updateRankers(CacheUnit cu, boolean reset) {
        for (CacheUnit u : units) {
            if (u == cu) {
                if (reset)
                    u.ranker.reset();
                else
                    u.ranker.shiftUsed();
            } else {
                u.ranker.shift();
            }
        }
    }
    private void updateRankers(CacheUnit cu) {
        updateRankers(cu, false);
    }

    private void incBound(int n) {
        int prevBound = bound;
        bound = n;

        CacheUnit[] tmp = new CacheUnit[bound];
        boolean expandRanker = false;
        if (! Ranker.isSizeEnough(bound)) {
            Ranker.updateSize(bound);
            expandRanker = true;
        }

        for (int i = 0; i < prevBound; i++) {
            tmp[i] = units[i];
            if (expandRanker)
                units[i].ranker.expand();
        }
        for (int i = prevBound; i < bound; i++) {
            tmp[i] = new CacheUnit();
        }
        units = tmp;
        countEmpty += bound - prevBound;
    }

    private void decBound(int n) {
        int prevBound = bound;
        bound = n;

        CacheUnit[] tmp = new CacheUnit[bound];

        for (int i = 0; i < bound; i++) {
            tmp[i] = units[i];
        }
        for (int i = bound; i < prevBound; i++) {
            hashlist.removeCacheUnit(units[i]);
            units[i] = null; // Let the VM to collect the garbage
        }
        units = tmp;
        countEmpty -= (prevBound - bound);
        if (countEmpty < 0)
            countEmpty = 0;
    }


    private int getNextInsPos() {
        // step1, check whether there is an empty position
        if (countEmpty > 0)
            return bound - countEmpty; // Don't decrease countEmpty here, decrease it later

        // step2, get the position according to LRU algorithm.
        if (units[0].ranker.zeroRU())
            return 0;
        int pos = 0;

        for (int i = 1; i < bound; i++) {
            // If recent used count is zero
            if (units[i].ranker.zeroRU()) {
                return i;
            }
            if (units[i].ranker.lessRUThan(units[pos].ranker)) {
                pos = i;
            }
        }
        return pos; // pos is the least recently used
    }
        
}


/*
 * There are 4 classes to support the LRU Cache:
 * CacheUnit: Cache unit. It contains key, value, ranker information.
 * Ranker: To keep recording the ranking of each Cache Unit.
 * HashList: The hash list to speed up the search of the keys in the cache.
 * HashNode: Node information in the Hash List.
 */

class CacheUnit {
    String key = null;
    String value = null;
    Ranker ranker = new Ranker();
    HashNode hn = null;


    CacheUnit() {
    }

    /** Check whether this cache unit has ever been used. */
    boolean isEmpty() {
        return (hn == null);
    }

    
}



class Ranker {
    private final static long HIGH_BIT_MASK = 0x4000000000000000L;
    private final static int WORDS_BOUND_RATIO = 252;  // 252 = 63 * 4. It can also be 1008 = 63 * 16
    // The bits width of ranker is no less than 1/4 of the cache size.
    // In real implementation such width should be tuned for best LRU performance
    private static int size = 1; /** The bits in the ranker is size * 63 */
    private long[] rankBits;
    private int usedCounter = 0;

    Ranker() {
        rankBits = new long[size]; // Default bits are all 0.
    }

    static boolean isSizeEnough(int bound) {
        return (bound <= size * WORDS_BOUND_RATIO); 
    }
        
    static void updateSize(int bound) {
        if (! isSizeEnough(bound))
            size = (bound -1) / WORDS_BOUND_RATIO + 1;
    }

    void expand() {
        long[] tmp  = new long[size];
        for (int i = 0; i < rankBits.length; i++)
            tmp[i] = rankBits[i];
        rankBits = tmp;
    }

    /** Right shift the Ranker without setting the used bit */
    void shift() {
        if (usedCounter == 0) {// all bits in the ranker are zero, need not shift
            return;
        }

        if ((rankBits[0] & 1L) > 0) { // The LSB is 1, and will be shifted out
            usedCounter--;
        }

        rankBits[0] >>= 1;
        for (int i = 1; i < size; i++) {
            if (rankBits[i] == 0) continue; // In many cases all the bits may be zero

            if ((rankBits[i] & 1) > 0) // LSB is 1, then set the MSB of lower number to 1
                rankBits[i-1] |= HIGH_BIT_MASK;
            rankBits[i] >>= 1;
        }
    }

    /** Right shift the Ranker and set the MSB of the highest number as 1, means recently used. */
    void shiftUsed() {
        shift();
        rankBits[size-1] |= HIGH_BIT_MASK; // Set the used bit
        usedCounter++;
    }

    /** Reset other bits to 0 but the MSB to 1, when a unit is used or replaced. */
    void reset() {
        if (usedCounter > 0) {
            for (int i = 0; i < size; i++) {
                rankBits[i] = 0;
            }
        }
        rankBits[size-1] = HIGH_BIT_MASK; // Set the MSB to 1, and all other bits are 0.
        usedCounter = 1;
    }

    /** Whether the object of this Ranker is less recently used than the object with Ranker r.
        Should call the zeroRU() first to reduce the amount of comparison. */
    boolean lessRUThan(Ranker r) {
        // first count whether 
        if (usedCounter < r.usedCounter) {
            return true;
        } else if (usedCounter > r.usedCounter) {
            return false;
        }

        // then compare the bits spreading
        for (int i = size - 1; i >= 0; i--) {
            if (rankBits[i] < (r.rankBits[i]))
                return true;
            else if (rankBits[i] > (r.rankBits[i]))
                return false;
        }
        return false; // equal
    }

    /** Whether the object of this Ranker is all zero. If yes, absolutely its the least recent used item */
    boolean zeroRU() {
        return (usedCounter == 0);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("[Ranker:");
        for (int i = size - 1; i >= 0; i--) {
            sb.append(rankBits[i]).append("\t");
        }
        sb.append("]");
        return sb.toString();
    }
}



/** The node in the hash list, for quick lookup of the cache units. */
class HashNode {
    // Allow the two members accessible to other classes
    CacheUnit cu = null;
    HashNode next = null;
}

/** The HashList for quick look up of the cache units. */
class HashList {
    private final static int MIN_HASH_SIZE = 16;
    private int size = MIN_HASH_SIZE;
    private HashNode[] heads = null;

    HashList() {
        this(MIN_HASH_SIZE);
    }

    HashList(int s) {
        if (s > MIN_HASH_SIZE) size = s;

        heads = new HashNode[size];
        for (int i = 0; i < size; i++)
            heads[i] = new HashNode(); // an empty head for each row in the list, to simplify the add and delete the nodes.
    }

    /** Hash function */
    int hash(String key) {
        int x = 0;
        for (byte b: key.getBytes())
            x += b;
        return (x % size);
    }

    CacheUnit searchKey(String key) {
        int h = hash(key);
        HashNode p = heads[h].next;
        while (p != null) {
            CacheUnit cu = p.cu;
            if (cu.key.equals(key))
                return cu;
        }
        return null;
    }
        
    void addCacheUnit(CacheUnit cu) {
        int h = hash(cu.key);
        HashNode p = heads[h];
        while (p.next != null)
            p = p.next;
        HashNode node = new HashNode();
        node.cu = cu;
        cu.hn = node;
        p.next = node;  // Append to the end of the list
    }

    // delete the cache unit from the lists
    void removeCacheUnit(CacheUnit cu) {
        int h = hash(cu.key);

        HashNode node = cu.hn;
        // remove the node from the list heads[h];
        HashNode p = heads[h];
        while (p.next != node) {
            p = p.next;
        }
        p.next = node.next;
    }

    // Move the nodes across the lists, without delete and recreate nodes.
    void updateCacheUnitPos(CacheUnit cu, String oldKey) {
        int h1 = hash(oldKey);
        int h2 = hash(cu.key);
        if (h1 == h2) return; // Under the same hash list, need not move.

        HashNode node = cu.hn;
        // remove the node from the list heads[h1];
        HashNode p = heads[h1];
        while ((p.next != node) /* && (p.next != null): Exceptional case, if everything goes well it never happens */ ) {
            p = p.next;
        }
        p.next = node.next;

        // append it to list heads[h2]
        node.next = null;
        p = heads[h2];
        while (p.next != null) {
            p = p.next;
        }
        p.next = node;
    }

}    

