/**
 * TableEntry datatype.
 * @param <K> Key
 * @param <V> Value
 */
class TableEntry<K,V> {
	/**
	 * Key storage.
	 */
	private K key;
	
	/**
	 * Value storage.
	 */
	private V value;

	/**
	 * Constructor.
	 * @param key Key
	 * @param value Val
	 */
	public TableEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Key getter.
	 * @return Key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * Value getter.
	 * @return Value
	 */
	public V getValue() {
		return value;
	}

	/**
	 * String converter.
	 * @return String
	 */
	public String toString() {
		return key.toString()+":"+value.toString();
	}
}