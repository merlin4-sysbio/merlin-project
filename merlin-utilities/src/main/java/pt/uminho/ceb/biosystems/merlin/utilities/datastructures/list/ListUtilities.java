package pt.uminho.ceb.biosystems.merlin.utilities.datastructures.list;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ListUtilities extends pt.uminho.ceb.biosystems.mew.utilities.datastructures.list.ListUtilities {

	public static <T> List<List<T>> split(List<T> list, final int length) {
	    List<List<T>> parts = new ArrayList<List<T>>();
	    final int size = list.size();
	    for (int i = 0; i < size; i += length) {
	        parts.add(new ArrayList<T>(
	            list.subList(i, Math.min(size, i + length)))
	        );
	    }
	    return parts;
	}
	
	/**
	 * Get smaller concurrent list.
	 * 
	 * @param list
	 * @param size
	 * @return
	 */
	public static ConcurrentLinkedQueue<?> getConcurrentList(ConcurrentLinkedQueue<?> list,  int size) {

		ConcurrentLinkedQueue<Object> objects = new ConcurrentLinkedQueue<>();

		boolean flag = true;
		int i = 0;

		while(flag) {

			objects.add(list.poll());
			i++;

			if(i>size || i>list.size())
				flag = false;
		}
		return objects;
	}
	
}
