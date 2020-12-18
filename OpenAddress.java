/**
 * Open Addressing with Linear Probing Table.
 * @param <K> Key
 * @param <V> Value
 */
class OpenAddress<K,V> {
    /**
     * Array of entries.
     */
    private TableEntry<K,V>[] storage;

    /**
     * Array of tombstones.
     */
    private boolean[] tombstones;

    /**
     * Number of elements.
     */
    private int elements = 0;

    /**
     * Hashtable constructor.
     * @param size Size of table
     */
    @SuppressWarnings("unchecked")
    public OpenAddress(int size) {
        //Create a hash table where the size of the storage is
        //the provided size (number of "slots" in the table)
        //Assume size is >= 2
        storage = new TableEntry[size];
        tombstones = new boolean[size];
    }

    /**
     * Return how many "slots" are in the table.
     * @return Capacity
     */
    public int getCapacity() {
        return storage.length;
    }

    /**
     * Return the number of elements in the table.
     * @return Number of elements.
     */
    public int size() {
        return elements;
    }

    /**
     * Puts entry in storage.
     * @param k Key
     * @param v Value
     */
    public void put(K k, V v) {
        //Place value v at the location of key k.
        //Use linear probing if that location is in use.
        if (k == null || v == null)
        {
            throw new IllegalArgumentException();
        }

        int bucketsProbed = 0;
        int bucket = findHashPosition(k);

        while (bucketsProbed < getCapacity())
        {
            if (storage[bucket] != null && storage[bucket].getKey().equals(k))
            {
                storage[bucket] = new TableEntry<>(k, v);
                return;
            }
            else if (storage[bucket] == null)
            {
                storage[bucket] = new TableEntry<>(k, v);
                elements++;
                if (isTombstone(bucket))
                    tombstones[bucket] = false;
                while (size() >= (getCapacity() * .8))
                    rehash(getCapacity() * 2);
                return;
            }


            bucket = (bucket + 1) % getCapacity();
            bucketsProbed++;
        }
    }

    /**
     * Remove the given key (and associated value) from the table.
     * @param k Key
     * @return Value in the table if not null
     */
    public V remove(K k) {
        int bucketsProbed = 0;
        int bucket = findHashPosition(k);

        while (bucketsProbed < getCapacity())
        {
            if (storage[bucket].getKey().equals(k))
            {
                V val = storage[bucket].getValue();
                storage[bucket] = null;
                elements--;
                tombstones[bucket] = true;
                return val;
            }
            bucket = (bucket + 1) % getCapacity();
            bucketsProbed++;
        }
        return null;
    }

    /**
     * Given a key, return the value from the table.
     * @param k Key
     * @return Value
     */
    public V get(K k) {
        int bucketsProbed = 0;
        int bucket = findHashPosition(k);

        while (bucketsProbed < getCapacity())
        {
            if (storage[bucket] != null && storage[bucket].getKey().equals(k))
            {
                return storage[bucket].getValue();
            }
            bucket = (bucket + 1) % getCapacity();
            bucketsProbed++;
        }
        //If the value is not in the table, return null.

        return null;
    }

    /**
     * Checks location if there is a tombstone.
     * @param loc Location
     * @return True if there is a tombstone at given index
     */
    public boolean isTombstone(int loc) {
        //this is a helper method needed for printing
        return tombstones[loc];
    }

    /**
     * Rehashes storage.
     * @param size New size
     * @return if successful
     */
    @SuppressWarnings("unchecked")
    public boolean rehash(int size) {
        //Increase or decrease the size of the storage,
        //rehashing all values.

        if (size < (size() + 1))
        {
            return false;
        }
        TableEntry<K,V>[] oldStorage = storage;
        storage = new TableEntry[size];
        tombstones = new boolean[size];
        elements = 0;
        for (TableEntry<K,V> element : oldStorage)
        {
            if (element != null)
                rehashPut(element.getKey(), element.getValue());
        }

        //If the new size won't fit all the elements,
        //with at least _one_ empty space, return false
        //and do not rehash. Return true if able to rehash.

        return true;
    }

    /**
     * Calculates hash position of key.
     * @param k Key
     * @return Hash position
     */
    private int findHashPosition(K k)
    {
        int bucket = k.hashCode() % getCapacity();

        if (bucket < 0)
            bucket *= -1;

        return bucket;
    }
    /**
     * Puts entry in storage.
     * @param k Key
     * @param v Value
     */
    public void rehashPut(K k, V v) {
        //Place value v at the location of key k.
        //Use linear probing if that location is in use.
        if (k == null || v == null)
        {
            throw new IllegalArgumentException();
        }

        int bucketsProbed = 0;
        int bucket = findHashPosition(k);

        while (bucketsProbed < getCapacity())
        {
            if (storage[bucket] != null && storage[bucket].getKey().equals(k))
            {
                storage[bucket] = new TableEntry<>(k, v);
                return;
            }
            else if (storage[bucket] == null)
            {
                storage[bucket] = new TableEntry<>(k, v);
                elements++;
                if (isTombstone(bucket))
                    tombstones[bucket] = false;
                return;
            }


            bucket = (bucket + 1) % getCapacity();
            bucketsProbed++;
        }
    }

    /**
     * Testing code.
     * @param args args
     */
    public static void main(String[] args) {
        //main method for testing
        OpenAddress<String,String> st6 = new OpenAddress<>(2);
        st6.put("orange","3");
        st6.put("peach","3");
        st6.put("pear","3");
        st6.put("banana","1");
        st6.rehash(10);
        st6.rehash(5);

        OpenAddress<String,String> st1 = new OpenAddress<>(10);
        OpenAddress<String,Integer> st2 = new OpenAddress<>(5);

        if(st1.getCapacity() == 10 && st2.getCapacity() == 5 && st1.size() == 0 && st2.size() == 0) {
            System.out.println("Yay 1");
        }

        st1.put("a","apple");
        st1.put("b","banana");
        st1.put("banana","b");
        st1.put("b","butter");

        if(st1.toString().equals("a:apple\nb:butter\nbanana:b") && st1.toStringDebug().equals("[0]: null\n[1]: null\n[2]: null\n[3]: null\n[4]: null\n[5]: null\n[6]: null\n[7]: a:apple\n[8]: b:butter\n[9]: banana:b")) {
            System.out.println("Yay 2");
        }

        if(st1.getCapacity() == 10 && st1.size() == 3 && st1.get("a").equals("apple") && st1.get("b").equals("butter") && st1.get("banana").equals("b")) {
            System.out.println("Yay 3");
        }

        st2.put("a",1);
        st2.put("b",2);
        st2.put("e",3);
        st2.put("y",4);

        if(st2.toString().equals("e:3\ny:4\na:1\nb:2") && st2.toStringDebug().equals("[0]: null\n[1]: e:3\n[2]: y:4\n[3]: null\n[4]: null\n[5]: null\n[6]: null\n[7]: a:1\n[8]: b:2\n[9]: null")) {
            System.out.println("Yay 4");
        }

        if(st2.getCapacity() == 10 && st2.size() == 4 && st2.get("a").equals(1) && st2.get("b").equals(2) && st2.get("e").equals(3) && st2.get("y").equals(4)) {
            System.out.println("Yay 5");
        }

        if(st2.remove("e").equals(3) && st2.getCapacity() == 10 && st2.size() == 3 && st2.get("e") == null && st2.get("y").equals(4)) {
            System.out.println("Yay 6");
        }

        if(st2.toString().equals("y:4\na:1\nb:2") && st2.toStringDebug().equals("[0]: null\n[1]: tombstone\n[2]: y:4\n[3]: null\n[4]: null\n[5]: null\n[6]: null\n[7]: a:1\n[8]: b:2\n[9]: null")) {
            System.out.println("Yay 7");
        }

        if(st2.rehash(2) == false && st2.size() == 3 && st2.getCapacity() == 10) {
            System.out.println("Yay 8");
        }

        if(st2.rehash(4) == true && st2.size() == 3 && st2.getCapacity() == 4) {
            System.out.println("Yay 9");
        }

        if(st2.toString().equals("y:4\na:1\nb:2") && st2.toStringDebug().equals("[0]: null\n[1]: y:4\n[2]: a:1\n[3]: b:2")) {
            System.out.println("Yay 10");
        }

        OpenAddress<String,String> st3 = new OpenAddress<>(2);
        st3.put("a","a");
        st3.remove("a");

        if(st3.toString().equals("") && st3.toStringDebug().equals("[0]: null\n[1]: tombstone")) {
            st3.put("a","a");
            if(st3.toString().equals("a:a") && st3.toStringDebug().equals("[0]: null\n[1]: a:a")) {
                System.out.println("Yay 11");
            }
        }
    }

    /**
     * Returns string representation.
     * @return String
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < storage.length; i++) {
            if(storage[i] != null && !isTombstone(i)) {
                s.append(storage[i]);
                s.append("\n");
            }
        }
        return s.toString().trim();
    }

    /**
     * Returns string representation.
     * @return String
     */
    public String toStringDebug() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < storage.length; i++) {
            if(!isTombstone(i)) {
                s.append("[" + i + "]: " + storage[i] + "\n");
            }
            else {
                s.append("[" + i + "]: tombstone\n");
            }

        }
        return s.toString().trim();
    }
}