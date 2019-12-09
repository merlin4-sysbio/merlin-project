package pt.uminho.ceb.biosystems.merlin.auxiliary;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

public class UseExistingIdOtherwiseGenerateUsingIdentity  extends IdentityGenerator {
//	private static final Logger log = Logger.getLogger(UseIdOrGenerate.class.getName());

	  @Override
	    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
	        Serializable id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);
	        
	        if (id == null || ((Integer)id == 0)) {
	            id = super.generate(session, object) ;
	            return id;
	        } else {
	            return id;

	        }
	        
//	        return id != null ? id : super.generate(session, object);
	    }
}