package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.interpro;

import java.util.List;

public class InterProParams {
	
	private String email;
	private String title;
	private String goterms;
	private String pathways;
	private String sequence;
	private List<String> appl;
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the goterms
	 */
	public String getGoterms() {
		return goterms;
	}
	/**
	 * @param goterms the goterms to set
	 */
	public void setGoterms(String goterms) {
		this.goterms = goterms;
	}
	/**
	 * @return the pathways
	 */
	public String getPathways() {
		return pathways;
	}
	/**
	 * @param pathways the pathways to set
	 */
	public void setPathways(String pathways) {
		this.pathways = pathways;
	}
	/**
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	/**
	 * @return the appl
	 */
	public List<String> getAppl() {
		return appl;
	}
	/**
	 * @param appl the appl to set
	 */
	public void setAppl(List<String> appl) {
		this.appl = appl;
	}
}
