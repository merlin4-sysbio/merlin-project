package pt.uminho.ceb.biosystems.merlin.core.containers.model;

import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;

public class SequenceContainer {
	
	private int id;
	private String sequence;
	private SequenceType seqType;
	private Integer sequenceLength;
	private Integer geneID;
	private String query;
	
	public SequenceContainer(int id) {
		this.id = id;
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public SequenceType getSeqType() {
		return seqType;
	}
	public void setSeqType(SequenceType seqType) {
		this.seqType = seqType;
	}
	public Integer getSequenceLength() {
		return sequenceLength;
	}
	public void setSequenceLength(Integer sequenceLength) {
		this.sequenceLength = sequenceLength;
	}
	public Integer getGeneID() {
		return geneID;
	}
	public void setGeneID(Integer geneID) {
		this.geneID = geneID;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	
}
