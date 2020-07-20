package pt.uminho.ceb.biosystems.merlin.dao.implementation.experimental;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.experimental.IExperimentalFactorDAO;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalFactor;


public class ExperimentalFactorDAOImpl extends GenericDaoImpl<ExperimentalFactor> implements IExperimentalFactorDAO {

	public ExperimentalFactorDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ExperimentalFactor.class);
		
	}

	@Override
	public void addExperimentalFactor(ExperimentalFactor ExperimentalFactor) {
		super.save(ExperimentalFactor);
		
	}

	@Override
	public void addExperimentalFactorList(List<ExperimentalFactor> ExperimentalFactorList) {
		for (ExperimentalFactor ExperimentalFactor: ExperimentalFactorList) {
			this.addExperimentalFactor(ExperimentalFactor);
		}
	}

	@Override
	public List<ExperimentalFactor> getAllExperimentalFactor() {
		return super.findAll();
	}

	@Override
	public ExperimentalFactor getExperimentalFactor(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeExperimentalFactor(ExperimentalFactor ExperimentalFactor) {
		super.delete(ExperimentalFactor);
		
	}

	@Override
	public void removeExperimentalFactorList(List<ExperimentalFactor> ExperimentalFactorList) {
		for (ExperimentalFactor ExperimentalFactor: ExperimentalFactorList) {
			this.removeExperimentalFactor(ExperimentalFactor);
		}
	}

	@Override
	public void updateExperimentalFactorList(List<ExperimentalFactor> ExperimentalFactorList) {
		for (ExperimentalFactor ExperimentalFactor: ExperimentalFactorList) {
			this.update(ExperimentalFactor);
		}
	}

	@Override
	public void updateExperimentalFactor(ExperimentalFactor ExperimentalFactor) {
		super.update(ExperimentalFactor);
	}

}
