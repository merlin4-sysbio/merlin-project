package pt.uminho.ceb.biosystems.merlin.utilities.external;

import java.io.Serializable;

public interface IMetaboliteExternalRef extends IExternalRef, Serializable{
	
	String getSmiles();
	Integer getCharge();
	String getFormula();
	String getMass();
	String getInchikey();

}
