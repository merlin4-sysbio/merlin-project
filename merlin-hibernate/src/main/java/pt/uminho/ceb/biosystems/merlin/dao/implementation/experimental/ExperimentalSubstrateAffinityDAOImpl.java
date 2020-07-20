package pt.uminho.ceb.biosystems.merlin.dao.implementation.experimental;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.experimental.IExperimentalSubstrateAffinityDAO;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalSubstrateAffinity;


public class ExperimentalSubstrateAffinityDAOImpl extends GenericDaoImpl<ExperimentalSubstrateAffinity> implements IExperimentalSubstrateAffinityDAO {

	public ExperimentalSubstrateAffinityDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ExperimentalSubstrateAffinity.class);
		
	}
	
	@Override
	public void addExperimentalSubstrateAffinity(ExperimentalSubstrateAffinity ExperimentalSubstrateAffinity) {
		super.save(ExperimentalSubstrateAffinity);
		
	}

	@Override
	public void addEnzymesAnnotationEcNumberList(List<ExperimentalSubstrateAffinity> ExperimentalSubstrateAffinityList) {
		for (ExperimentalSubstrateAffinity ExperimentalSubstrateAffinity: ExperimentalSubstrateAffinityList) {
			this.addExperimentalSubstrateAffinity(ExperimentalSubstrateAffinity);
		}
	}

	@Override
	public List<ExperimentalSubstrateAffinity> getAllExperimentalSubstrateAffinity() {
		return super.findAll();
	}

	@Override
	public ExperimentalSubstrateAffinity getExperimentalSubstrateAffinity(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeExperimentalSubstrateAffinity(ExperimentalSubstrateAffinity ExperimentalSubstrateAffinity) {
		super.delete(ExperimentalSubstrateAffinity);
		
	}

	@Override
	public void removeExperimentalSubstrateAffinityList(List<ExperimentalSubstrateAffinity> ExperimentalSubstrateAffinityList) {
		for (ExperimentalSubstrateAffinity ExperimentalSubstrateAffinity: ExperimentalSubstrateAffinityList) {
			this.removeExperimentalSubstrateAffinity(ExperimentalSubstrateAffinity);
		}
	}

	@Override
	public void updateExperimentalSubstrateAffinityList(List<ExperimentalSubstrateAffinity> ExperimentalSubstrateAffinityList) {
		for (ExperimentalSubstrateAffinity ExperimentalSubstrateAffinity: ExperimentalSubstrateAffinityList) {
			this.update(ExperimentalSubstrateAffinity);
		}
	}

	@Override
	public void updateExperimentalSubstrateAffinity(ExperimentalSubstrateAffinity ExperimentalSubstrateAffinity) {
		super.update(ExperimentalSubstrateAffinity);
	}

}
