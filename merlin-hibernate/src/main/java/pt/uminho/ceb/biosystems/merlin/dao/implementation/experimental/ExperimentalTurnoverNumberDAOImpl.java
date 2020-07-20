package pt.uminho.ceb.biosystems.merlin.dao.implementation.experimental;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.experimental.IExperimentalTurnoverNumberDAO;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalTurnoverNumber;

public class ExperimentalTurnoverNumberDAOImpl extends GenericDaoImpl<ExperimentalTurnoverNumber> implements IExperimentalTurnoverNumberDAO {

	public ExperimentalTurnoverNumberDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ExperimentalTurnoverNumber.class);
		
	}

	@Override
	public void addExperimentalTurnoverNumber(ExperimentalTurnoverNumber ExperimentalTurnoverNumber) {
		super.save(ExperimentalTurnoverNumber);
		
	}

	@Override
	public void addExperimentalTurnoverNumberList(List<ExperimentalTurnoverNumber> ExperimentalTurnoverNumberList) {
		for (ExperimentalTurnoverNumber ExperimentalTurnoverNumber: ExperimentalTurnoverNumberList) {
			this.addExperimentalTurnoverNumber(ExperimentalTurnoverNumber);
		}
	}

	@Override
	public List<ExperimentalTurnoverNumber> getAllExperimentalTurnoverNumber() {
		return super.findAll();
	}

	@Override
	public ExperimentalTurnoverNumber getExperimentalTurnoverNumber(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeExperimentalTurnoverNumber(ExperimentalTurnoverNumber ExperimentalTurnoverNumber) {
		super.delete(ExperimentalTurnoverNumber);
		
	}

	@Override
	public void removeExperimentalTurnoverNumberList(List<ExperimentalTurnoverNumber> ExperimentalTurnoverNumberList) {
		for (ExperimentalTurnoverNumber ExperimentalTurnoverNumber: ExperimentalTurnoverNumberList) {
			this.removeExperimentalTurnoverNumber(ExperimentalTurnoverNumber);
		}
	}

	@Override
	public void updateExperimentalTurnoverNumberList(List<ExperimentalTurnoverNumber> ExperimentalTurnoverNumberList) {
		for (ExperimentalTurnoverNumber ExperimentalTurnoverNumber: ExperimentalTurnoverNumberList) {
			this.update(ExperimentalTurnoverNumber);
		}
	}

	@Override
	public void updateExperimentalTurnoverNumber(ExperimentalTurnoverNumber ExperimentalTurnoverNumber) {
		super.update(ExperimentalTurnoverNumber);
	}

}
