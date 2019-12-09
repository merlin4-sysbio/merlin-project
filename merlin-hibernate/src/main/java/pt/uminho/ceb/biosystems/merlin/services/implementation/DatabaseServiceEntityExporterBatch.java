package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments.CompartmentsAnnotationCompartments;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments.CompartmentsAnnotationReportsHasCompartments;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumber;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberList;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberRank;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberRankHasOrganism;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomologyHasHomologues;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologues;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologuesHasEcNumber;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologyData;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologySetup;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationOrganism;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductList;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductRank;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductRankHasOrganism;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationScorerConfig;
import pt.uminho.ceb.biosystems.merlin.entities.auxiliar.EntityInstances;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalDescription;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalFactor;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalInhibitor;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalSubstrateAffinity;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalTurnoverNumber;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproLocation;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproModel;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResult;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasModel;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResults;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproXRef;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelAliases;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompound;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelDblinks;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelDictionary;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEffector;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEntityisfrom;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEnzymaticAlternativeCofactor;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEnzymaticCofactor;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelFeature;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelFunctionalParameter;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelMetabolicRegulation;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModule;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModulesHasCompound;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathway;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasCompound;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModule;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProteinComposition;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSameAs;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSequence;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSequenceFeature;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelStoichiometry;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelStrain;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubstrateAffinity;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSuperpathway;
import pt.uminho.ceb.biosystems.merlin.entities.project.Projects;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryEvent;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryPromoter;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryRiFunction;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatorySigmaPromoter;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryTranscriptionUnit;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryTranscriptionUnitGene;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryTranscriptionUnitPromoter;

@SuppressWarnings("rawtypes")
public class DatabaseServiceEntityExporterBatch extends DatabaseServiceImpl implements PropertyChangeListener{

	private Integer counter = 1;
	private IGenericDao genericDao;
	private PropertyChangeSupport changes;
	private AtomicBoolean cancel;

	public DatabaseServiceEntityExporterBatch(StandardServiceRegistry registry) throws JAXBException {
		super(registry);
		this.changes = new PropertyChangeSupport(this);
		this.setCancel(false);

	}

	@SuppressWarnings({"unchecked" })
	private Object mapEntities(String outputDirectory) throws Exception{

		Class [] entities = {ModelGene.class, ModelSequence.class, ModelProtein.class, ModelCompound.class,
				EnzymesAnnotationEcNumber.class, EnzymesAnnotationOrganism.class,
				EnzymesAnnotationHomologySetup.class, 
				EnzymesAnnotationScorerConfig.class, EnzymesAnnotationGeneHomology.class,
				EnzymesAnnotationHomologyData.class,
				EnzymesAnnotationProductRank.class, EnzymesAnnotationHomologues.class,
				EnzymesAnnotationEcNumberRank.class, EnzymesAnnotationEcNumberList.class, 
				EnzymesAnnotationProductList.class, EnzymesAnnotationEcNumberRankHasOrganism.class,
				EnzymesAnnotationGeneHomologyHasHomologues.class,  EnzymesAnnotationHomologuesHasEcNumber.class,
				EnzymesAnnotationProductRankHasOrganism.class,
				ExperimentalFactor.class, ExperimentalDescription.class, 
				ExperimentalInhibitor.class, ExperimentalSubstrateAffinity.class,
				ExperimentalTurnoverNumber.class, 	
				InterproEntry.class, InterproModel.class, InterproResults.class,
				InterproXRef.class,	InterproLocation.class, InterproResult.class,
				InterproResultHasEntry.class, InterproResultHasModel.class,  	
				ModelAliases.class, ModelDblinks.class, ModelDictionary.class,
				ModelCompartment.class, ModelFeature.class,
				ModelModule.class, ModelOrthology.class, ModelPathway.class,
				ModelProteinComposition.class,
				ModelReactionLabels.class, ModelStrain.class, ModelSubstrateAffinity.class,
				ModelSuperpathway.class, ModelReaction.class,  
				ModelEffector.class, ModelEntityisfrom.class, ModelEnzymaticAlternativeCofactor.class,
				ModelEnzymaticCofactor.class, ModelFunctionalParameter.class,
				ModelGeneHasCompartment.class, ModelGeneHasOrthology.class,
				ModelMetabolicRegulation.class, ModelModuleHasOrthology.class,
				ModelModulesHasCompound.class, ModelPathwayHasCompound.class,
				ModelPathwayHasModelProtein.class, ModelPathwayHasModule.class,
				ModelPathwayHasReaction.class, ModelReactionHasModelProtein.class,
				ModelSameAs.class, ModelSequenceFeature.class, ModelStoichiometry.class,
				ModelSubunit.class, ModelModuleHasModelProtein.class,
				Projects.class,
				RegulatoryPromoter.class, RegulatoryTranscriptionUnit.class,
				RegulatoryRiFunction.class, RegulatoryTranscriptionUnitGene.class,
				RegulatoryTranscriptionUnitPromoter.class, RegulatorySigmaPromoter.class, 
				RegulatoryEvent.class, CompartmentsAnnotationCompartments.class,
				CompartmentsAnnotationReportsHasCompartments.class,};
		
		this.changes.firePropertyChange("size", null, entities.length);
		
		int tableCounter = 0;

		for(Class entity:entities) {
			
			if(!this.cancel.get()) {
				this.changes.firePropertyChange("tablesCounter", null, tableCounter);
				tableCounter ++;
				
				genericDao = new GenericDaoImpl(this.sessionFactory, entity);
				writexml(createEntityIntances(this.genericDao.findAll(), entity), outputDirectory);
			}
			else {
				break;
			}
			
		}

		this.changes.firePropertyChange("tablesCounter", null, tableCounter);
		
		return null;	
	}

	@SuppressWarnings("unchecked")
	public EntityInstances createEntityIntances(List entities, Class class_) {

		EntityInstances res = new EntityInstances<>();
		res.setIntancesClass(class_);

		for(int i = 0; i < entities.size(); i++) {
			res.add(entities.get(i));
		}
		return res;
	}

	public void dbtoXML (String outputDirectory) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			mapEntities(outputDirectory);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	private void writexml (EntityInstances entities, String outputPath) throws JAXBException {


		JAXBContext jaxbcontext = JAXBContext.newInstance(entities.getClass(), entities.getIntancesClass());
		Marshaller jaxbMarshaller = jaxbcontext.createMarshaller();
		jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
		// print xml in console
		//jaxbMarshaller.marshal( entities, System.out );
		jaxbMarshaller.marshal( entities, new File(outputPath +this.counter.toString()+"-"+entities.getIntancesClass().getSimpleName() + ".xml") );
		this.counter++;
	}


	private Map<Integer, File>  getFiles(String xmlPath) {

		Map<Integer, File> maptofill = new TreeMap<>();

		File file = new File(xmlPath);

		File[] directoryListing = file.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				Integer filenumber = Integer.valueOf(child.getName().split("\\.")[0].split("-")[0]);
				maptofill.put(filenumber, child);
			}
		}
		return maptofill;
	}


	public void readxmldb (String xmlDirectory, AtomicBoolean cancel) throws Exception {

		Map<Integer, File> mapa = this.getFiles(xmlDirectory);

		this.changes.firePropertyChange("size", null, mapa.size());

		int tableCounter = 0;

		for(Integer key: mapa.keySet()) {

			if(!cancel.get()) {
				this.changes.firePropertyChange("tablesCounter", null, tableCounter);
				tableCounter ++;

				EntityInstances entity = this.readxml(mapa.get(key), xmlDirectory);
				
				if(entity != null)
					this.saveToDatabase(entity);
			}
			else {
				break;
			}
		}

		this.changes.firePropertyChange("tablesCounter", null, tableCounter);

	}
	
	private EntityInstances readxml (File xmlfile, String xmlDirectory) throws JAXBException {

		try {
			String xmlfilename = xmlfile.getName();
			//TODO THIS IS NOT SUPPOSED TO BE HARDCODED
			File file = new File(xmlDirectory.concat(xmlfilename));
			String classname = xmlfilename.split("\\.")[0].split("-")[1];

			Class class_ = null;

			if (classname.toLowerCase().startsWith("compartments")) {
				class_ = Class.forName("pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments.".concat(classname));
			}

			else if (classname.toLowerCase().startsWith("enzymes")) {
				class_ = Class.forName("pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.".concat(classname));
			}

			else if (classname.toLowerCase().startsWith("experimental")) {
				class_ = Class.forName("pt.uminho.ceb.biosystems.merlin.entities.experimental.".concat(classname));
			}

			else if (classname.toLowerCase().startsWith("interpro")) {
				class_ = Class.forName("pt.uminho.ceb.biosystems.merlin.entities.interpro.".concat(classname));
			}

			else if (classname.toLowerCase().startsWith("projects")) {
				class_ = Class.forName("pt.uminho.ceb.biosystems.merlin.entities.project.".concat(classname));
			}

			else if (classname.toLowerCase().startsWith("regulatory")) {
				class_ = Class.forName("pt.uminho.ceb.biosystems.merlin.entities.regulatory.".concat(classname));
			}
			else if (classname.toLowerCase().startsWith("model")) {
				class_ = Class.forName("pt.uminho.ceb.biosystems.merlin.entities.model.".concat(classname));
			}

			JAXBContext jaxbContext = JAXBContext.newInstance(EntityInstances.class, class_);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (EntityInstances) jaxbUnmarshaller.unmarshal(file);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private void saveToDatabase (EntityInstances entity) throws Exception {
		if (entity!=null && entity.getInstances()!=null && !entity.getInstances().isEmpty()) {
			
			Transaction tx = null;
			try {
				
				tx = sessionFactory.getCurrentSession().beginTransaction();
				genericDao = new GenericDaoImpl(this.sessionFactory, entity.getInstances().get(0).getClass());
				for(int i = 0; i < entity.getInstances().size(); i++) {
					genericDao.save(entity.getInstances().get(i));
					if( i % 50 == 0 ) {
						genericDao.flushSession();        
						genericDao.clearSession();
					}
				}
				tx.commit();
			} catch (RuntimeException e) {
				tx.rollback();
				throw new Exception(e);
			}
		}
	}

	/**
	 * @param l
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	/**
	 * @param l
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}

	public void propertyChange(PropertyChangeEvent evt) {

		this.changes.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());				
	}

	/**
	 * @param cancel the cancel to set
	 */
	public void setCancel(boolean cancel) {
		this.cancel = new AtomicBoolean(cancel);
	}

}
