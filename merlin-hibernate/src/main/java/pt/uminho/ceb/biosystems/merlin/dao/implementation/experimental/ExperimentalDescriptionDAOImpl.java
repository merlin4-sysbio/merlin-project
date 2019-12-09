package pt.uminho.ceb.biosystems.merlin.dao.implementation.experimental;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.experimental.IExperimentalDescriptionDAO;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalDescription;


public class ExperimentalDescriptionDAOImpl extends GenericDaoImpl<ExperimentalDescription> implements IExperimentalDescriptionDAO {

	public ExperimentalDescriptionDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ExperimentalDescription.class);
		
	}

	@Override
	public void addExperimentalDescription(ExperimentalDescription ExperimentalDescription) {
		super.save(ExperimentalDescription);
		
	}

	@Override
	public void addExperimentalDescriptionList(List<ExperimentalDescription> ExperimentalDescriptionList) {
		for (ExperimentalDescription ExperimentalDescription: ExperimentalDescriptionList) {
			this.addExperimentalDescription(ExperimentalDescription);
		}
	}

	@Override
	public List<ExperimentalDescription> getAllExperimentalDescription() {
		return super.findAll();
	}

	@Override
	public ExperimentalDescription getExperimentalDescription(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeExperimentalDescription(ExperimentalDescription ExperimentalDescription) {
		super.delete(ExperimentalDescription);
		
	}

	@Override
	public void removeExperimentalDescriptionList(List<ExperimentalDescription> ExperimentalDescriptionList) {
		for (ExperimentalDescription ExperimentalDescription: ExperimentalDescriptionList) {
			this.removeExperimentalDescription(ExperimentalDescription);
		}
	}

	@Override
	public void updateExperimentalDescriptionList(List<ExperimentalDescription> ExperimentalDescriptionList) {
		for (ExperimentalDescription ExperimentalDescription: ExperimentalDescriptionList) {
			this.update(ExperimentalDescription);
		}
	}

	@Override
	public void updateExperimentalDescription(ExperimentalDescription ExperimentalDescription) {
		super.update(ExperimentalDescription);
		
	}

}
