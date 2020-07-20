package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.sbml_semantics;


import java.io.Serializable;
import java.util.List;

public class SematicSbmlItemRef implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected List<String> miriamCodes;
	protected List<String> names;
	protected String mainName;
	protected List<String> urls;

	public SematicSbmlItemRef(List<String> miriamCodes, List<String> names,
			String mainName, List<String> urls) {
		super();
		this.miriamCodes = miriamCodes;
		this.names = names;
		this.mainName = mainName;
		this.urls = urls;
//		this.precision = precision;
	}
	
	
	public List<String> getMiriamCodes() {
		return miriamCodes;
	}
	public void setMiriamCodes(List<String> miriamCodes) {
		this.miriamCodes = miriamCodes;
	}
	public List<String> getNames() {
		return names;
	}
	public void setNames(List<String> names) {
		this.names = names;
	}
	public String getMainName() {
		return mainName;
	}
	public void setMainName(String mainName) {
		this.mainName = mainName;
	}
	public List<String> getUrls() {
		return urls;
	}
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
	
	public boolean hasTheSameName(SematicSbmlItemRef ref){
		
		boolean isTheSame = true;
		List<String> compNames = ref.getNames();
		
		for(int i = 0; i<compNames.size() && isTheSame; i++)
			isTheSame = this.names.contains(compNames.get(i));
		
		return isTheSame; 
	}
	
	public void concatInformation(SematicSbmlItemRef ref){
		this.miriamCodes.addAll(ref.getMiriamCodes());
		this.names.addAll(ref.getNames());
		this.urls.addAll(ref.getUrls());
	}
}
