/**
 * Separate Chaining Table.
 * @param <K> Key
 * @param <V> Value
 */
class SeparateChain<K,V> {
    /**
     * Array of entries.
     */
    private Node<K,V>[] storage;

    /**
     * Number of elements.
     */
    private int elements = 0;

    /**
     * Hashtable constructor.
     * @param size Size of table
     */
    
    @SuppressWarnings("unchecked")
    public SeparateChain(int size) {
        //Create a hash table where the size of the storage is
        //the provided size (number of "slots" in the table)
        //Assume size is >= 2
        storage = new Node[size];
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
        //Use separate chaining if that location is in use.
        
        if (k == null || v == null)
        {
            throw new IllegalArgumentException();
        }

        int bucket = findHashPosition(k);

        Node<K, V> node = new Node<>(new TableEntry<>(k,v));
        Node<K, V> head = storage[bucket];

        if (storage[bucket] == null)
        {
            storage[bucket] = node;
            elements++;
            while (size() >= (getCapacity() * .8))
                rehash(getCapacity() * 2);
        }
        else {
            while (head.next != null) {
                if (head.entry.getKey().equals(k)) {
                    head.entry = node.entry;
                }
                head = head.next;
            }
            if (head.entry.getKey().equals(k)) {
                head.entry = node.entry;
            } else {
                head.next = node;
                elements++;
                while (size() >= (getCapacity() * .8))
                    rehash(getCapacity() * 2);
            }
        }
    }

    /**
     * Removes key from storage.
     * @param k Key
     * @return Value
     */
    public V remove(K k) {
        //Remove the given key (and associated value)
        //from the table. Return the value removed.
        //If the value is not in the table, return null.
        int bucket = findHashPosition(k);
        Node<K, V> head = storage[bucket];

        if (storage[bucket] == null)
        {
            return null;
        }
        else if (storage[bucket].entry.getKey().equals(k))
        {
            V oldNode = storage[bucket].entry.getValue();
            storage[bucket] = storage[bucket].next;
            head = null;
            elements--;
            return oldNode;
        }
        else
        {
            while (head.next != null)
            {
                if (head.next.entry.getKey().equals(k))
                {
                    V oldNext = head.next.entry.getValue();
                    if (head.next.next != null)
                    {
                        head.next = head.next.next;
                    }
                    else
                    {
                        head.next.entry = null;
                        elements--;
                        return oldNext;
                    }
                }
                head = head.next;
            }
        }

        return null;
    }

    /**
     * Given a key, return the value from the table.
     * @param k Key
     * @return Value
     */
    public V get(K k) {
        int bucket = findHashPosition(k);
        Node<K, V> head = storage[bucket];

        while (head != null)
        {
            if (head.entry.getKey().equals(k))
            {
                return head.entry.getValue();
            }
            head = head.next;

        }
        return null;
    }

    /**
     * Rehashes storage.
     * @param size New size
     * @return True if successful
     */
    @SuppressWarnings("unchecked")
    public boolean rehash(int size) {
        //Increase or decrease the size of the storage,
        //rehashing all values.
        if (size < 1)
        {
            return false;
        }
        Node<K,V>[] oldStorage = storage;
        storage = new Node[size];
        elements = 0;

        Node<K, V> head;

        for (Node<K,V> node : oldStorage)
        {
            if (node != null)
            {
                rehashPut(node.entry.getKey(), node.entry.getValue());
                head = node;
                while (head.next != null)
                {
                    rehashPut(head.next.entry.getKey(), head.next.entry.getValue());
                    head = head.next;
                }
            }
        }

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
    private void rehashPut(K k, V v) {
        //Place value v at the location of key k.
        //Use separate chaining if that location is in use.
        //You may assume both k and v will not be null.
        if (k == null || v == null)
        {
            throw new IllegalArgumentException();
        }

        int bucket = findHashPosition(k);

        Node<K, V> node = new Node<>(new TableEntry<>(k,v));
        Node<K, V> head = storage[bucket];

        if (storage[bucket] == null)
        {
            storage[bucket] = node;
            elements++;
        }
        else {
            while (head.next != null) {
                if (head.entry.getKey().equals(k)) {
                    head.entry = node.entry;
                }
                head = head.next;
            }
            if (head.entry.getKey().equals(k)) {
                head.entry = node.entry;
            } else {
                head.next = node;
                elements++;
            }
        }
    }

    /**
     * Testing code.
     * @param args args
     */
    public static void main(String[] args) {
        //main method for testing, edit as much as you want
        SeparateChain<String,String> st1 = new SeparateChain<>(10);
        SeparateChain<String,String> st6 = new SeparateChain<>(10);
        SeparateChain<String,Integer> st2 = new SeparateChain<>(5);

        st6.put("peach", "3");
        st6.put("pear", "3");
        st6.put("orange", "2");
        st6.put("banana", "1");

        st6.rehash(4);
        st6.rehash(1);

        if(st1.getCapacity() == 10 && st2.getCapacity() == 5 && st1.size() == 0 && st2.size() == 0) {
            System.out.println("Yay 1");
        }

        st1.put("a","apple");
        st1.put("b","banana");
        st1.put("banana","b");
        st1.put("b","butter");

        if(st1.toString().equals("a:apple\nbanana:b\nb:butter") && st1.toStringDebug().equals("[0]: null\n[1]: null\n[2]: null\n[3]: null\n[4]: null\n[5]: null\n[6]: null\n[7]: [a:apple]->[banana:b]->null\n[8]: [b:butter]->null\n[9]: null")) {
            System.out.println("Yay 2");
        }

        if(st1.getCapacity() == 10 && st1.size() == 3 && st1.get("a").equals("apple") && st1.get("b").equals("butter") && st1.get("banana").equals("b")) {
            System.out.println("Yay 3");
        }

        st2.put("a",1);
        st2.put("b",2);
        st2.put("e",3);
        st2.put("y",4);

        if(st2.toString().equals("e:3\ny:4\na:1\nb:2") && st2.toStringDebug().equals("[0]: null\n[1]: [e:3]->[y:4]->null\n[2]: null\n[3]: null\n[4]: null\n[5]: null\n[6]: null\n[7]: [a:1]->null\n[8]: [b:2]->null\n[9]: null")) {
            System.out.println("Yay 4");
        }

        if(st2.getCapacity() == 10 && st2.size() == 4 && st2.get("a").equals(1) && st2.get("b").equals(2) && st2.get("e").equals(3) && st2.get("y").equals(4)) {
            System.out.println("Yay 5");
        }

        if(st2.remove("e").equals(3) && st2.getCapacity() == 10 && st2.size() == 3 && st2.get("e") == null && st2.get("y").equals(4)) {
            System.out.println("Yay 6");
        }

        if(st2.toString().equals("y:4\na:1\nb:2") && st2.toStringDebug().equals("[0]: null\n[1]: [y:4]->null\n[2]: null\n[3]: null\n[4]: null\n[5]: null\n[6]: null\n[7]: [a:1]->null\n[8]: [b:2]->null\n[9]: null")) {
            System.out.println("Yay 7");
        }

        if(st2.rehash(0) == false && st2.size() == 3 && st2.getCapacity() == 10) {
            System.out.println("Yay 8");
        }

        if(st2.rehash(4) == true && st2.size() == 3 && st2.getCapacity() == 4) {
            System.out.println("Yay 9");
        }

        if(st2.toString().equals("y:4\na:1\nb:2") && st2.toStringDebug().equals("[0]: null\n[1]: [y:4]->[a:1]->null\n[2]: [b:2]->null\n[3]: null")) {
            System.out.println("Yay 10");
        }

        SeparateChain<String,String> st3 = new SeparateChain<>(2);
        st3.put("a","a");
        st3.remove("a");

        if(st3.toString().equals("") && st3.toStringDebug().equals("[0]: null\n[1]: null")) {
            st3.put("a","a");
            if(st3.toString().equals("a:a") && st3.toStringDebug().equals("[0]: null\n[1]: [a:a]->null")) {
                System.out.println("Yay 11");
            }
        }
    }

    /**
     * Node type.
     * @param <K> Key
     * @param <V> Value
     */
    public static class Node<K,V> {
        /**
         * Curr entry.
         */
        public TableEntry<K,V> entry;
        /**
         * Next Node.
         */
        public Node<K,V> next;

        /**
         * Node constructor.
         * @param entry Entry
         */
        public Node(TableEntry<K,V> entry) {
            this.entry = entry;
        }

        /**
         * Node constructor.
         * @param entry Entry
         * @param next Next Node
         */
        public Node(TableEntry<K,V> entry, Node<K,V> next) {
            this(entry);
            this.next = next;
        }

        /**
         * Node string rep.
         * @return String
         */
        public String toString() {
            return "[" + entry.toString() + "]->";
        }
    }

    /**
     * Returns string representation.
     * @return String
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < storage.length; i++) {
            Node<K,V> curr = storage[i];
            if(curr == null) continue;

            while(curr != null) {
                s.append(curr.entry.toString());
                s.append("\n");
                curr = curr.next;
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
            Node<K,V> curr = storage[i];

            s.append("[" + i + "]: ");
            while(curr != null) {
                s.append(curr.toString());
                curr = curr.next;
            }
            s.append("null\n");
        }
        return s.toString().trim();
    }
}