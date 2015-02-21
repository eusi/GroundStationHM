package edu.hm.cs.sam.mc.routing;

/**
 * Class to implement Key-Value-Pairs
 *
 * @author Stefan Hoelzl
 * @version 0.1
 */

public class Pair<K, V> {
    private K key;
    private V value;
    
    /**
     * Constructor to initialize Pair
     * @param k Key
     * @param v Value
     */
    public Pair(final K k, final V v) {
        setKey(k);
        setValue(v);
    }
    
    /**
     * Returns the Value saved in this Pair
     * @return value
     */
    public V getValue() {
        return value;
    }
    
    /**
     * Sets the value for this pair
     * @param value Value to Key
     */
    public void setValue(final V value) {
        this.value = value;
    }
    
    /**
     * Gets the Key of the Pair
     * @return Key to this Pair
     */
    public K getKey() {
        return key;
    }
    
    /**
     * Sets the Key for this Pair
     * @param key
     */
    public void setKey(final K key) {
        this.key = key;
    }
}
