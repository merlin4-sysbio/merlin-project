package pt.uminho.ceb.biosystems.merlin.dao.implementation.regulatory;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory.IRegulatoryPromoterDAO;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryPromoter;

public class RegulatoryPromoterDAOImpl extends GenericDaoImpl<RegulatoryPromoter> implements IRegulatoryPromoterDAO {

	public RegulatoryPromoterDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, RegulatoryPromoter.class);
		
	}

	@Override
	public void addRegulatoryPromoter(RegulatoryPromoter RegulatoryPromoter) {
		super.save(RegulatoryPromoter);
	}

	@Override
	public void addRegulatoryPromoterList(List<RegulatoryPromoter> RegulatoryPromoterList) {
		for (RegulatoryPromoter RegulatoryPromoter: RegulatoryPromoterList) {
			this.addRegulatoryPromoter(RegulatoryPromoter);
		}
		
	}

	@Override
	public List<RegulatoryPromoter> getAllRegulatoryPromoter() {
		return super.findAll();
	}

	@Override
	public RegulatoryPromoter getRegulatoryPromoter(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeRegulatoryPromoter(RegulatoryPromoter RegulatoryPromoter) {
		super.delete(RegulatoryPromoter);
		
	}

	@Override
	public void removeRegulatoryPromoterList(List<RegulatoryPromoter> RegulatoryPromoterList) {
		for (RegulatoryPromoter RegulatoryPromoter: RegulatoryPromoterList) {
			this.removeRegulatoryPromoter(RegulatoryPromoter);
		}
		
	}

	@Override
	public void updateRegulatoryPromoterList(List<RegulatoryPromoter> RegulatoryPromoterList) {
		for (RegulatoryPromoter RegulatoryPromoter: RegulatoryPromoterList) {
			this.update(RegulatoryPromoter);
		}
		
	}

	@Override
	public void updateRegulatoryPromoter(RegulatoryPromoter RegulatoryPromoter) {
		super.update(RegulatoryPromoter);
		
	}

}
