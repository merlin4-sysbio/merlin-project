package pt.uminho.ceb.biosystems.merlin.utilities.datastructures.map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author odias
 *
 */
public class MapUtils extends pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.MapUtils {

	/**
	 * Revert Map from Set
	 * 
	 * @param map
	 * @return
	 */
	public static <S,T> Map<S, Set<T>> revertMapFromSet(Map<T, Set<S>> map) {
		
		Map<S, Set<T>> ret = new HashMap<S, Set<T>>();
	
		for(T key : map.keySet()){
			
			Set<S> valueList = map.get(key);
	
			for(S value : valueList) {
	
				Set<T> setKeys = ret.get(value);
				if(setKeys == null){
					
					setKeys = new HashSet<T>();
					ret.put(value, setKeys);
				}
				setKeys.add(key);
			}
		}
	
		return ret;
	}
	
	/**
	 * Read File to Map
	 * 
	 * @param f
	 * @param idxId
	 * @param idxData
	 * @param sep
	 * @return
	 * @throws IOException
	 */
	public static Map<Integer, String> readFile(String f, int idxId, int idxData, String sep) throws IOException{
		Map<Integer, String> data = new TreeMap<Integer, String>();
		
		FileReader filer = new FileReader(f);
		BufferedReader reader = new BufferedReader(filer);
		
		String line = reader.readLine();
		
		while(line!=null){
			
			String[] ldata = line.split(sep); 
			String dataS = "";
			if(ldata.length>idxData)
				dataS = ldata[idxData].trim();
			data.put(Integer.parseInt(ldata[idxId].trim()), dataS);
			line = reader.readLine();
		}
		reader.close();
		filer.close();
		
		return data;
	}



}
