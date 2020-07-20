package pt.uminho.ceb.biosystems.merlin.dao.implementation.experimental;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.experimental.IExperimentalInhibitorDAO;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalInhibitor;


public class ExperimentalInhibitorDAOImpl extends GenericDaoImpl<ExperimentalInhibitor> implements IExperimentalInhibitorDAO {

	public ExperimentalInhibitorDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ExperimentalInhibitor.class);
		
	}

	@Override
	public void addExperimentalInhibitor(ExperimentalInhibitor ExperimentalInhibitor) {
		super.save(ExperimentalInhibitor);
		
	}

	@Override
	public void addExperimentalInhibitorList(List<ExperimentalInhibitor> ExperimentalInhibitorList) {
		for (ExperimentalInhibitor ExperimentalInhibitor: ExperimentalInhibitorList) {
			this.addExperimentalInhibitor(ExperimentalInhibitor);
		}
	}

	@Override
	public List<ExperimentalInhibitor> getAllExperimentalInhibitor() {
		return super.findAll();
	}

	@Override
	public ExperimentalInhibitor getExperimentalInhibitor(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeExperimentalInhibitor(ExperimentalInhibitor ExperimentalInhibitor) {
		super.delete(ExperimentalInhibitor);
		
	}

	@Override
	public void removeExperimentalInhibitorList(List<ExperimentalInhibitor> ExperimentalInhibitorList) {
		for (ExperimentalInhibitor ExperimentalInhibitor: ExperimentalInhibitorList) {
			this.removeExperimentalInhibitor(ExperimentalInhibitor);
		}
	}

	@Override
	public void updateExperimentalInhibitorList(List<ExperimentalInhibitor> ExperimentalInhibitorList) {
		for (ExperimentalInhibitor ExperimentalInhibitor: ExperimentalInhibitorList) {
			this.update(ExperimentalInhibitor);
		}	
	}

	public void updateExperimentalInhibitor(ExperimentalInhibitor ExperimentalInhibitor) {
		super.update(ExperimentalInhibitor);
	}

}
