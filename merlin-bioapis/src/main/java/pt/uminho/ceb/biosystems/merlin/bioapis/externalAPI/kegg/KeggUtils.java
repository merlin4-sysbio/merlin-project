package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg;

import java.util.ArrayList;
import java.util.List;

public class KeggUtils {

	
	public static List<String> parserMultiLineInfo(List<String> info){
		ArrayList<String> values = new ArrayList<String>();
		
		if(info!=null){
			for(String line : info){
				String ecnumbersArray[] = line.split("\\s+");
				for(int i =0; i < ecnumbersArray.length; i++)
					values.add(ecnumbersArray[i]);
			}
		}
		return values;
	}
	
	public static String parserKeggCode(String data){
		return data.substring(0, 6);
	}
}