package pt.uminho.ceb.biosystems.merlin.utilities;


public class SortableData implements Comparable<Object>{

	protected int value;
	protected Object[] n;
	
	public SortableData(int value, Object[] n)
	{
		this.value = value;
		this.n = n;
	}
	
	public int compareTo(Object arg0) {
		int so = ((SortableData)arg0).getValue();
//		return value-so;
		return so-value;
	}

	public Object[] getData() {
		return n;
	}

	public int getValue() {
		return value;
	}

}
