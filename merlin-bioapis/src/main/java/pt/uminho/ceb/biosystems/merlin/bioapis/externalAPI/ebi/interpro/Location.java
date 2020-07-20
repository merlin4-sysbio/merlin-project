package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.interpro;

/**
 * @author João Sequeira
 *
 */
public class Location {
	
	private int start;
	private int end;
	private float score;
	private int hmmstart;
	private int hmmend;
	private int hmmlength;
	private float evalue;
	private int envstart;
	private int envend;
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}
	/**
	 * @return the score
	 */
	public float getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(float score) {
		this.score = score;
	}
	/**
	 * @return the hmmstart
	 */
	public int getHmmstart() {
		return hmmstart;
	}
	/**
	 * @param hmmstart the hmmstart to set
	 */
	public void setHmmstart(int hmmstart) {
		this.hmmstart = hmmstart;
	}
	/**
	 * @return the hmmend
	 */
	public int getHmmend() {
		return hmmend;
	}
	/**
	 * @param hmmend the hmmend to set
	 */
	public void setHmmend(int hmmend) {
		this.hmmend = hmmend;
	}
	/**
	 * @return the hmmlength
	 */
	public int getHmmlength() {
		return hmmlength;
	}
	/**
	 * @param hmmlength the hmmlength to set
	 */
	public void setHmmlength(int hmmlength) {
		this.hmmlength = hmmlength;
	}
	/**
	 * @return the evalue
	 */
	public float getEvalue() {
		return evalue;
	}
	/**
	 * @param evalue the evalue to set
	 */
	public void setEvalue(float evalue) {
		this.evalue = evalue;
	}
	/**
	 * @return the envstart
	 */
	public int getEnvstart() {
		return envstart;
	}
	/**
	 * @param envstart the envstart to set
	 */
	public void setEnvstart(int envstart) {
		this.envstart = envstart;
	}
	/**
	 * @return the envend
	 */
	public int getEnvend() {
		return envend;
	}
	/**
	 * @param envend the envend to set
	 */
	public void setEnvend(int envend) {
		this.envend = envend;
	}
}
