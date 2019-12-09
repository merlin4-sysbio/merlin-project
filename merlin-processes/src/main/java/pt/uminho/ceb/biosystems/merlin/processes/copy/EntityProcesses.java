package pt.uminho.ceb.biosystems.merlin.processes.copy;

import java.util.List;

public class EntityProcesses {

	public EntityProcesses() {
		// TODO Auto-generated constructor stub
	}

	public static String[][] getStats(List<Integer> values) {

		String[][] res = new String[][] {{"Number of entries", ""+values.get(0)}};
		
		return res;
	}

}
