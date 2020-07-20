package pt.uminho.ceb.biosystems.merlin.dao.implementation.regulatory;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory.IRegulatoryRiFunctionDAO;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryRiFunction;

 public class RegulatoryRiFunctionDAOImpl extends GenericDaoImpl<RegulatoryRiFunction> implements IRegulatoryRiFunctionDAO {

 public RegulatoryRiFunctionDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, RegulatoryRiFunction.class);
	
	}

	@Override 
 public void addRegulatoryRiFunction(RegulatoryRiFunction RegulatoryRiFunction) {
		super.save(RegulatoryRiFunction);
		
	}

	@Override 
 public void addRegulatoryRiFunctionList(List<RegulatoryRiFunction> RegulatoryRiFunctionList) {
		for (RegulatoryRiFunction RegulatoryRiFunction: RegulatoryRiFunctionList) {
			this.addRegulatoryRiFunction(RegulatoryRiFunction);
		}
		
	}

	@Override 
 public List<RegulatoryRiFunction> getAllRegulatoryRiFunction() {
		return super.findAll();
	}

	@Override 
 public RegulatoryRiFunction getRegulatoryRiFunction(Integer id) {
		return super.findById(id);
	}

	@Override 
 public void removeRegulatoryRiFunction(RegulatoryRiFunction RegulatoryRiFunction) {
		super.delete(RegulatoryRiFunction);
		
	}

	@Override 
 public void removeRegulatoryRiFunctionList(List<RegulatoryRiFunction> RegulatoryRiFunctionList) {
		for (RegulatoryRiFunction RegulatoryRiFunction: RegulatoryRiFunctionList) {
			this.removeRegulatoryRiFunction(RegulatoryRiFunction);
		}
		
	}

	@Override 
 public void updateRegulatoryRiFunctionList(List<RegulatoryRiFunction> RegulatoryRiFunctionList) {
		for (RegulatoryRiFunction RegulatoryRiFunction: RegulatoryRiFunctionList) {
			this.update(RegulatoryRiFunction);
		}
		
	}

	@Override 
 public void updateRegulatoryRiFunction(RegulatoryRiFunction RegulatoryRiFunction) {
		super.update(RegulatoryRiFunction);
		
	}

}
