/*
 * Copyright 2010
 * IBB-CEB - Institute for Biotechnology and Bioengineering - Centre of Biological Engineering
 * CCTC - Computer Science and Technology Center
 *
 * University of Minho 
 * 
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 * 
 * Created inside the SysBioPseg Research Group (http://sysbio.di.uminho.pt)
 */
package pt.uminho.ceb.biosystems.merlin.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author paulo maia, Mar 6, 2009 at 12:26:20 PM
 *
 * @param <K> the key type for this hashmap
 * @param <V> the value type for this hashmap mappings
 */
public class IndexedHashMap<K,V> extends HashMap<K,V>{
	
	private static final long serialVersionUID = 1L;
	private List<K> index;
	
	public IndexedHashMap(){
		super();
		index = new ArrayList<K>(); 
	}
	
	public IndexedHashMap(int initialCapacity){
		super(initialCapacity);
		index = new ArrayList<K>(initialCapacity);
	}
	
	public IndexedHashMap(int initialCapacity, float loadFactor){
		super(initialCapacity,loadFactor);
		index = new ArrayList<K>(initialCapacity);
	}
	
	
	@Override
	public void clear(){
		super.clear();
		index.clear();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		IndexedHashMap<K,V> result = null;
		
		result = (IndexedHashMap<K,V>)super.clone();
		result.index = new ArrayList<K>();
		
		return result;
	}
	
	@Override
	public V put(K key, V value){
		V old = super.put(key, value);
		index.add(key);
		return old;
	}
	
	public V putAt(int idx, K key, V value){
		V old = super.put(key, value);
		index.add(idx,key);
		return old;
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		super.putAll(m);
//		for(K key: m.keySet())
//			index.add(key); 
	//	adicionar indice redundante com o public V putAt, duplica os indices
	}
	
	@Override
	public V remove(Object key){
		V result = super.remove(key);
		index.remove(key);
		return result;
	}
	
	
	public int getIndexOf(K key){
		return index.indexOf(key);
	}
	
	public K getKeyAt(int ind){
		return index.get(ind);
	}
	
	public V getValueAt(int ind){
		K key = getKeyAt(ind);
		return super.get(key);
	}

	public List<K> getIndexArray() {
		return index;
	}
	
	@Override
	public int size(){
		return index.size();
	}
	

}
