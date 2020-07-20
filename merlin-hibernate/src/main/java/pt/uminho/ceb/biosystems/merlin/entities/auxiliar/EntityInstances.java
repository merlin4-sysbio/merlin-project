package pt.uminho.ceb.biosystems.merlin.entities.auxiliar;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "ENTITY" )
public class EntityInstances<T> {

	List<T> instances;
	private Class<T> klass;
	
	
	@XmlElement( name = "INSTANCE")
	public void setInstances( List<T> instances )
	{
		this.instances = instances;
	}
	
	public List<T> getInstances()
	{
		return this.instances;
	}

	public void add( T instance )
	{
		if( this.instances == null )
		{
			this.instances = new ArrayList<>();
		}
		this.instances.add( instance );

	}
	
	public void setIntancesClass( Class<T> klass) {
		this.klass = klass;
		
	}
	
	public Class<T> getIntancesClass() {
		return this.klass;
		
	}
}