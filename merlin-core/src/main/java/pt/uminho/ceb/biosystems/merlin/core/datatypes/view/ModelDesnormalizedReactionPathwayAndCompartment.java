package pt.uminho.ceb.biosystems.merlin.core.datatypes.view;

public class ModelDesnormalizedReactionPathwayAndCompartment {

	private Integer idreaction;
	private String reactionName;
	private String reactionEquation;
	private Integer idpathway;
	private String pathwayName;
	private Boolean reactionInModel;
	private Boolean reactionIsGeneric;
	private String reactionSource;
	private Integer compartmentId;
	private String reactionNotes;
	private Float lowerBound;
	private Float upperBound;
	private String compartmentName;
	
	public ModelDesnormalizedReactionPathwayAndCompartment() {
		
	}
	
	public ModelDesnormalizedReactionPathwayAndCompartment(Integer idreaction, String reactionName,
			String reactionEquation, Integer idpathway, String pathwayName,
			Boolean reactionInModel, String reactionSource, Boolean reactionIsGeneric, Integer compartmentId,
			String reactionNotes, Float lowerBound, Float upperBound) {
		super();
		this.idreaction = idreaction;
		this.reactionName = reactionName;
		this.reactionEquation = reactionEquation;
		this.idpathway = idpathway;
		this.pathwayName = pathwayName;
		this.reactionInModel = reactionInModel;
		this.reactionIsGeneric = reactionIsGeneric;
		this.reactionSource = reactionSource;
		this.compartmentId = compartmentId;
		this.reactionNotes = reactionNotes;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	public Integer getIdreaction() {
		return idreaction;
	}
	public void setIdreaction(Integer idreaction) {
		this.idreaction = idreaction;
	}
	public String getReactionName() {
		return reactionName;
	}
	public void setReactionName(String reactionName) {
		this.reactionName = reactionName;
	}
	public String getReactionEquation() {
		return reactionEquation;
	}
	public void setReactionEquation(String reactionEquation) {
		this.reactionEquation = reactionEquation;
	}
	public Integer getIdpathway() {
		return idpathway;
	}
	public void setIdpathway(Integer idpathway) {
		this.idpathway = idpathway;
	}
	public String getPathwayName() {
		return pathwayName;
	}
	public void setPathwayName(String pathwayName) {
		this.pathwayName = pathwayName;
	}
	public Boolean getReactionInModel() {
		return reactionInModel;
	}
	public void setReactionInModel(Boolean reactionInModel) {
		this.reactionInModel = reactionInModel;
	}
	public Boolean getReactionIsGeneric() {
		return reactionIsGeneric;
	}
	public void setReactionIsGeneric(Boolean reactionIsGeneric) {
		this.reactionIsGeneric = reactionIsGeneric;
	}
	public Integer getCompartmentId() {
		return compartmentId;
	}
	public void setCompartmentId(Integer compartmentId) {
		this.compartmentId = compartmentId;
	}
	public String getReactionNotes() {
		return reactionNotes;
	}
	public void setReactionNotes(String reactionNotes) {
		this.reactionNotes = reactionNotes;
	}
	public String getReactionSource() {
		return reactionSource;
	}
	public void setReactionSource(String reactionSource) {
		this.reactionSource = reactionSource;
	}

	public Float getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(Float lowerBound) {
		this.lowerBound = lowerBound;
	}

	public Float getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(Float upperBound) {
		this.upperBound = upperBound;
	}
	
	/**
	 * @return the reversible
	 */
	public boolean isReversible() {

		return (lowerBound < 0 && upperBound > 0);
	}

	public String getCompartmentName() {
		return compartmentName;
	}

	public void setCompartmentName(String compartmentName) {
		this.compartmentName = compartmentName;
	}

	
}
