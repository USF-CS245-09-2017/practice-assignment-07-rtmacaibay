/**
 * Hashtable - a container with good efficiency for storing values, finding, and removing
 * @author Robert
 * @assignment p7
 */

import java.util.ArrayList;

public class Hashtable<K, V> {
	//node class that holds information
	class Node<K, V> {
		protected K key;
		protected V value;
		
		protected Node<K, V> next;
		
		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
	
	//private global variables
	private ArrayList<Node<K, V>> bucket;
	private int numBuckets;
	private int size;
	
	/**
	 * default constructor ~ creates a bucket size of 2500 items
	 */
	public Hashtable() {
		bucket = new ArrayList<Node<K, V>>();
		numBuckets = 2500;
		size = 0;
		for (int i = 0; i < numBuckets; i++)
			bucket.add(null);
	}
	
	/**
	 * gets the index to find/place of a particular key
	 * @param key - the information used to find a value in the bucket
	 * @return the index of the location of a value
	 */
	private int getBucketIndex(K key) {
		//find the hash of the key
		int hashCode = Math.abs(key.hashCode());
		//calculate the index ~ hash(key) % number of buckets
		int index = hashCode % numBuckets;
		
		return index;
	}
	
	/**
	 * checks if the bucket contains a certain key
	 * @param key - the information used to find a value in the bucket
	 * @return whether the key exists
	 */
	public boolean containsKey(K key) {
		//get index in the bucket where the key could be found
		int bucketIndex = getBucketIndex(key);
		//start at the head at that index
		Node<K, V> head = bucket.get(bucketIndex);
		
		//iterate through the keys at that index
		while (head != null) {
			//return true if we've found the key
			if (head.key.equals(key))
				return true;
			//iterate!!
			head = head.next;
		}
		
		//return false if we don't find it
		return false;
	}
	
	/**
	 * gets the value from a certain key
	 * @param key - the information used to find a value in the bucket
	 * @return the value
	 */
	public V get(K key) {
		//get index of the key in the bucket
		int bucketIndex = getBucketIndex(key);
		//start at the head at the index
		Node<K, V> head = bucket.get(bucketIndex);
		
		//iterate through the keys at that index
		while (head != null) {
			//return the value if we've found it
			if (head.key.equals(key))
				return head.value;
			//iterate!!
			head = head.next;
		}
		
		//return nothing bc we didn't find the goods
		return null;
	}
	
	/**
	 * puts a certain key associated to a value into the bucket
	 * @param key - the information used to find a value in the bucket
	 * @param value - the value that'll be in the bucket
	 */
	public void put(K key, V value) {
		//get index of the key in the bucket
		int bucketIndex = getBucketIndex(key);
		//start at the head at the index
		Node<K, V> head = bucket.get(bucketIndex);
		
		//iterate through the keys at the index
		while (head != null) {
			//if we found the key that means it already exists in the bucket
			if (head.key.equals(key)) {
				//change value at that key bc we want to put it that way
				head.value = value;
				return;
			}
			//iterate!!!!
			head = head.next;
		}
		//update the size bc we adding it!
		size++;
		//go back to the head
		head = bucket.get(bucketIndex);
		//create a new node to which it carries our key and value
		Node<K, V> newNode = new Node<K, V>(key, value);
		//set the node's next to the head
		newNode.next = head;
		//set the head at that index to the node
		bucket.set(bucketIndex, newNode);
		
		//check if the number of elements in our bucket exceed a "threshold"
		if ((double)size / numBuckets >= 0.7) {
			//create a temporary copy of our bucket
			ArrayList<Node<K, V>> temp = bucket;
			//reset the bucket to a new arraylist
			bucket = new ArrayList<Node<K, V>>();
			//double the number of buckets
			numBuckets = 2 * numBuckets;
			//change size to zero
			size = 0;
			
			//set everything in the bucket to null
			for (int i = 0; i < numBuckets; i++)
				bucket.add(null);
			
			//iterate through all the items in the temp copy
			for (Node<K, V> headNode : temp) {
				//iterate through and get the existing headnodes
				while (headNode != null) {
					//add that node to the bucket
					put(headNode.key, headNode.value);
					//iterate through that list
					headNode = headNode.next;
				}
			}
		}
	}
	
	/**
	 * removes a certain key/value pair from the bucket
	 * @param key - the information used to find a value in the bucket
	 * @return the value removed
	 */
	public V remove(K key) {
		//get index of key in the bucket
		int bucketIndex = getBucketIndex(key);
		//start at the head at that index
		Node<K, V> head = bucket.get(bucketIndex);
		//have a previous to keep track of nodes
		Node<K, V> previous = null;
		
		//iterate through the nodes 
		while (head != null) {
			//if we found the key break the loop
			if (head.key.equals(key))
				break;
			//set previous to... the previous
			previous = head;
			//iterate
			head = head.next;
		}
		
		//if head is null, that means key didnt exist in first place
		if (head == null)
			return null;
		
		//decrement size
		size--;
		
		//if previous is null, then all we have to do is just set head
		if (previous != null)
			previous.next = head.next;
		else
			bucket.set(bucketIndex, head.next);
		
		//return the removed value
		return head.value;
	}
	
	/**
	 * gets size of the bucket
	 * @return the size
	 */
	public int size() {
		return size;
	}
	
	/**
	 * checks if bucket is empty
	 * @return whether bucket is empty
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
}
