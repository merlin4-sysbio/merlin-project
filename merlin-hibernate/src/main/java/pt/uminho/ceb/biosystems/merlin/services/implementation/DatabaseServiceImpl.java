package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;

import pt.uminho.ceb.biosystems.merlin.auxiliary.ModelCompoundType;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ModuleContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.SequenceContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.view.ModelDesnormalizedReactionPathwayAndCompartment;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologyStatus;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.compartments.CompartmentsAnnotationCompartmentsDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.compartments.CompartmentsAnnotationReportsHasCompartmentsDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationEcNumberDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationEcNumberListDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationEcNumberRankDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationEcNumberRankHasOrganismDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationGeneHomologyDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationGeneHomologyHasHomologuesDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationHomologuesDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationHomologuesHasEcnumberDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationHomologyDataDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationHomologySetupDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationOrganismDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationProductListDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationProductrankDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationProductrankHasOrganismDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes.EnzymesAnnotationScorerconfigDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro.InterproEntryDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro.InterproLocationDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro.InterproModelDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro.InterproResultDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro.InterproResultHasEntryDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro.InterproResultHasModelDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro.InterproResultsDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro.InterproXRefDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelAliasesDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelCompartmentDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelCompoundDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelDblinksDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelEnzymaticCofactorDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelGeneDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelModuleDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelOrthologyDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelPathwayDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelProteinCompositionDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelProteinDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelReactionDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelReactionLabelsDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelSequenceDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelStoichiometryDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelSubunitDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.ModelSuperpathwayDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar.ModelGeneHasCompartmentDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar.ModelGeneHasOrthologyDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar.ModelModuleHasModelProteinDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar.ModelModuleHasOrthologyDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar.ModelPathwayHasModelCompoundDAO;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar.ModelPathwayHasModelProteinDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar.ModelPathwayHasModuleDAO;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar.ModelPathwayHasReactionDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar.ModelReactionHasModelProteinDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.project.ProjectsDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.regulatory.RegulatoryEventDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.regulatory.RegulatoryTranscriptionUnitDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.regulatory.RegulatoryTranscriptionUnitPromoterDAOImpl;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.views.DatabaseViewsDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.compartments.ICompartmentsAnnotationCompartmentsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.compartments.ICompartmentsAnnotationReportsHasCompartmentsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberListDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberRankDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberrankHasOrganismDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationGeneHomologyHasHomologuesDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationGenehomologyDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologuesDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologuesHasEcNumberDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologydataDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologysetupDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationOrganismDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationProductListDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationProductrankDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationProductrankHasOrganismDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationScorerconfigDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproEntryDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproLocationDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproModelDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultHasEntryDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultHasModelDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproXRefDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelAliasesDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelCompartmentDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelCompoundDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelDblinksDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelEnzymaticCofactorDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneHasCompartmentDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneHasOrthologyDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelModuleDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelModuleHasModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelModuleHasOrthologyDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelOrthologyDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasModelCompoundDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasModuleDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasReactionDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelProteinCompositionDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionHasModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionLabelsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSequenceDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelStoichiometryDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSubunitDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSuperpathwayDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.project.IProjectsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory.IRegulatoryEventDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory.IRegulatoryTranscriptionUnitDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory.IRegulatoryTranscriptionUnitPromoterDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.views.IDatabaseViewsDao;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResult;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResults;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproXRef;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompound;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSequence;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IAliasesService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IAnnotationCompartmentsService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IAnnotationEnzymesService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.ICompartmentService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.ICompoundService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IDatabaseService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IDblinksService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IDesnormalizerService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IGenesService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IHomologyService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IModuleService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IOrthologyService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IPathwayService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IProjectService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IProteinService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IReactionService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IRegulatoryEventService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.ISequenceService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IStoichiometryService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.ISubunitService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.ITranscriptionUnitService;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.I_InterproService;
import pt.uminho.ceb.biosystems.merlin.utilities.DatabaseProgressStatus;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class DatabaseServiceImpl implements IDatabaseService{

	protected SessionFactory sessionFactory;
	private IAnnotationEnzymesService annotationService;
	private IGenesService geneservice;
	private IPathwayService pathwayservice;
	private ICompartmentService compartmentservice;
	private IReactionService reactionservice;
	private ISubunitService subunitservice;
	private IHomologyService homologyservice;
	private IOrthologyService orthologyservice;
	private ICompoundService compoundservice;
	private IStoichiometryService stoichservice;
	private IProteinService proteinservice;
	private IAliasesService aliasesService;
	private ITranscriptionUnitService transcriptionservice;
	private IRegulatoryEventService regulatoryservice;
	private I_InterproService interproservice;
	private IDblinksService dblinksService;
	private ISequenceService sequenceservice;
	private IDesnormalizerService desnormalizerService;
	private IModuleService moduleService;
	private IProjectService projectService;
	private IAnnotationCompartmentsService annotationCompartmentsService;

	public DatabaseServiceImpl(StandardServiceRegistry registry) {

		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (CommandAcceptanceException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			//usually this error is thrown by hibernate when it is searching for a table that does not exists yet! Try to fix this issue
		}

		IEnzymesAnnotationEcNumberDAO ecnumberDAO = new EnzymesAnnotationEcNumberDAOImpl(sessionFactory);
		IEnzymesAnnotationEcNumberRankDAO ecnumberRankDAO = new EnzymesAnnotationEcNumberRankDAOImpl(sessionFactory);
		IEnzymesAnnotationProductrankDAO productRankDAO = new EnzymesAnnotationProductrankDAOImpl(sessionFactory);
		IEnzymesAnnotationScorerconfigDAO scorerconfigDAO = new EnzymesAnnotationScorerconfigDAOImpl(sessionFactory);
		IEnzymesAnnotationProductListDAO productlistDAO = new EnzymesAnnotationProductListDAOImpl(sessionFactory);
		IEnzymesAnnotationEcNumberListDAO ecnumberlistDAO = new EnzymesAnnotationEcNumberListDAOImpl(sessionFactory);
		IEnzymesAnnotationOrganismDAO organismDAO = new EnzymesAnnotationOrganismDAOImpl(sessionFactory);
		IEnzymesAnnotationHomologuesDAO homologuesDAO = new EnzymesAnnotationHomologuesDAOImpl(sessionFactory);
		IEnzymesAnnotationProductrankHasOrganismDAO productRankHasOrganism = new EnzymesAnnotationProductrankHasOrganismDAOImpl(sessionFactory);
		IModelGeneDAO modelgeneDAO = new ModelGeneDAOImpl(sessionFactory);
		IModelPathwayHasReactionDAO pathHasReactDAO = new ModelPathwayHasReactionDAOImpl(sessionFactory);
		IModelPathwayHasModelProteinDAO pathHasEnzymeDAO = new ModelPathwayHasModelProteinDAOImpl(sessionFactory);
		IModelGeneHasCompartmentDAO genehascompartDAO = new ModelGeneHasCompartmentDAOImpl(sessionFactory);
		IModelReactionHasModelProteinDAO modelreactionhasenzymeDAO = new ModelReactionHasModelProteinDAOImpl(sessionFactory);
		IModelStoichiometryDAO stoichiometryDAO = new ModelStoichiometryDAOImpl(sessionFactory);
		IModelPathwayDAO modelpathwayDAO = new ModelPathwayDAOImpl(sessionFactory);
		IModelReactionLabelsDAO modelReactionLabelsDAO = new ModelReactionLabelsDAOImpl(sessionFactory);
		IModelReactionDAO reactionDAO = new ModelReactionDAOImpl(sessionFactory);
		IModelSubunitDAO modelsubunitDAO = new ModelSubunitDAOImpl(sessionFactory);
		IEnzymesAnnotationHomologysetupDAO homologysetupDAO = new EnzymesAnnotationHomologySetupDAOImpl(sessionFactory);
		IEnzymesAnnotationGeneHomologyHasHomologuesDAO hashomologuesDAO = new EnzymesAnnotationGeneHomologyHasHomologuesDAOImpl(sessionFactory);
		IEnzymesAnnotationHomologydataDAO homologydataDAO = new EnzymesAnnotationHomologyDataDAOImpl(sessionFactory);
		I_InterproEntryDAO entryDAO = new InterproEntryDAOImpl(sessionFactory);
		I_InterproXRefDAO xrefDAO = new InterproXRefDAOImpl(sessionFactory);
		I_InterproModelDAO modelDAO = new InterproModelDAOImpl(sessionFactory);
		IEnzymesAnnotationGenehomologyDAO homologyDAO = new EnzymesAnnotationGeneHomologyDAOImpl(sessionFactory);
		IModelCompartmentDAO modelcompartmentDAO = new ModelCompartmentDAOImpl(sessionFactory);
		ICompartmentsAnnotationReportsHasCompartmentsDAO reportsHasCompartment = new CompartmentsAnnotationReportsHasCompartmentsDAOImpl(sessionFactory);
		IModelOrthologyDAO modelorthologyDAO = new ModelOrthologyDAOImpl(sessionFactory);
		IModelGeneHasOrthologyDAO geneHasOrthologyDAO = new ModelGeneHasOrthologyDAOImpl(sessionFactory);
		IModelCompoundDAO modelcompoundDAO = new ModelCompoundDAOImpl(sessionFactory);
		IModelEnzymaticCofactorDAO cofactorDAO = new ModelEnzymaticCofactorDAOImpl(sessionFactory);
		IModelAliasesDAO modelaliasesDAO = new ModelAliasesDAOImpl(sessionFactory);
		IModelProteinDAO modelproteinDAO = new ModelProteinDAOImpl(sessionFactory);
		IModelProteinCompositionDAO modelcompositionDAO = new ModelProteinCompositionDAOImpl(sessionFactory);
		IRegulatoryTranscriptionUnitDAO RegulatoryTranscriptionunitDAO = new RegulatoryTranscriptionUnitDAOImpl(sessionFactory,null);
		IRegulatoryEventDAO modelregulatoryDAO = new RegulatoryEventDAOImpl(sessionFactory);
		I_InterproResultDAO interproResultDAO = new InterproResultDAOImpl(sessionFactory);
		I_InterproResultsDAO interproResultsDAO = new InterproResultsDAOImpl(sessionFactory);
		I_InterproLocationDAO interprolocationDAO = new InterproLocationDAOImpl(sessionFactory);
		I_InterproResultHasModelDAO interproResultHasModelDAO = new InterproResultHasModelDAOImpl(sessionFactory);
		I_InterproResultHasEntryDAO interproResultHasEntryDAO = new InterproResultHasEntryDAOImpl(sessionFactory);
		IModelDblinksDAO modeldblinksDAO = new ModelDblinksDAOImpl(sessionFactory);
		IModelSequenceDAO sequenceDAO = new ModelSequenceDAOImpl(sessionFactory);	
		IDatabaseViewsDao dbViewsDAO = new DatabaseViewsDaoImpl(sessionFactory);
		IProjectsDAO projectDao = new ProjectsDAOImpl(sessionFactory);
		IRegulatoryTranscriptionUnitPromoterDAO RegulatoryTranscriptionpromoterDAO = new RegulatoryTranscriptionUnitPromoterDAOImpl(sessionFactory);
		IEnzymesAnnotationHomologuesHasEcNumberDAO homologuesHasEcNumberDAO = new EnzymesAnnotationHomologuesHasEcnumberDAOImpl(sessionFactory);
		IModelModuleDAO moduleDAO = new ModelModuleDAOImpl(sessionFactory, null);
		IEnzymesAnnotationEcNumberrankHasOrganismDAO ecNumberRankHasOrganismDao = new EnzymesAnnotationEcNumberRankHasOrganismDAOImpl(sessionFactory);
		IModelModuleHasOrthologyDAO modelModuleHasOrthologyDAO = new ModelModuleHasOrthologyDAOImpl(sessionFactory);
		IModelPathwayHasModelCompoundDAO modelPathwayHasModelCompoundDAO = new ModelPathwayHasModelCompoundDAO(sessionFactory);
		IModelPathwayHasModuleDAO modelPathwayHasModuleDAO = new ModelPathwayHasModuleDAO(sessionFactory);
		ICompartmentsAnnotationCompartmentsDAO annotationCompartmentsDAO = new CompartmentsAnnotationCompartmentsDAOImpl(sessionFactory);
		IModelSuperpathwayDAO superpathwayDAO = new ModelSuperpathwayDAOImpl(sessionFactory);
		IModelModuleHasModelProteinDAO moduleHasProteinDAO = new ModelModuleHasModelProteinDAOImpl(sessionFactory);

		annotationService = new AnnotationEnzymesServiceImpl(ecnumberDAO, ecnumberRankDAO, productRankDAO, 
				scorerconfigDAO, homologydataDAO, 
				productlistDAO, ecnumberlistDAO, organismDAO, productRankHasOrganism, homologuesDAO, 
				homologysetupDAO, homologyDAO, homologuesHasEcNumberDAO, ecNumberRankHasOrganismDao, modelgeneDAO, interproResultsDAO, hashomologuesDAO);

		annotationCompartmentsService = new AnnotationCompartmentsServiceImpl(annotationCompartmentsDAO, reportsHasCompartment);

		pathwayservice = new PathwayServiceImpl(modelpathwayDAO, pathHasReactDAO, pathHasEnzymeDAO, modelPathwayHasModelCompoundDAO, modelPathwayHasModuleDAO, 
				superpathwayDAO);
		stoichservice = new StoichiometryServiceImpl(stoichiometryDAO, modelcompartmentDAO, reactionDAO, modelcompoundDAO);
		compartmentservice = new CompartmentServiceImpl(modelcompartmentDAO, reportsHasCompartment, genehascompartDAO, reactionDAO, stoichiometryDAO);
		reactionservice = new ReactionServiceImpl(reactionDAO, modelreactionhasenzymeDAO, pathHasReactDAO,
				stoichiometryDAO, modelcompartmentDAO, modelReactionLabelsDAO, modelgeneDAO);


		subunitservice = new SubunitServiceImpl (modelsubunitDAO, modelproteinDAO, reactionDAO,
				modelaliasesDAO, modelgeneDAO, modelReactionLabelsDAO, moduleDAO, moduleHasProteinDAO);

		homologyservice = new HomologyServiceImpl (homologyDAO, homologysetupDAO, hashomologuesDAO, homologydataDAO,
				interproResultsDAO, interproResultDAO, entryDAO, xrefDAO, modelDAO, homologyDAO,sequenceDAO, organismDAO,homologuesDAO);

		orthologyservice = new OrthologyServiceImpl(modelorthologyDAO, geneHasOrthologyDAO);

		compoundservice = new CompoundServiceImpl(modelcompoundDAO, stoichiometryDAO);
		aliasesService = new AliasesServiceImpl(modelaliasesDAO);
		proteinservice = new ProteinServiceImpl(modelproteinDAO, modelcompositionDAO, modelreactionhasenzymeDAO, reactionDAO, cofactorDAO,
				modelaliasesDAO, modelReactionLabelsDAO, scorerconfigDAO, homologydataDAO, productlistDAO, ecnumberlistDAO, homologyDAO);

		geneservice = new GenesServiceImpl(modelgeneDAO, genehascompartDAO, modelsubunitDAO,
				modelaliasesDAO, modelcompartmentDAO, reactionDAO, modelReactionLabelsDAO, modelreactionhasenzymeDAO, modelproteinDAO, 
				geneHasOrthologyDAO);


		transcriptionservice = new TranscriptionUnitServiceImpl(RegulatoryTranscriptionunitDAO, RegulatoryTranscriptionpromoterDAO);
		regulatoryservice = new RegulatoryEventServiceImpl(modelregulatoryDAO);
		interproservice = new InterproServiceImpl(interproResultsDAO, interproResultDAO, interprolocationDAO, interproResultHasModelDAO, entryDAO, xrefDAO, modelDAO, interproResultHasEntryDAO);
		dblinksService = new DblinksServiceImpl(modeldblinksDAO);
		sequenceservice = new SequenceServiceImpl(sequenceDAO, modelgeneDAO);
		desnormalizerService = new DesnormalizerServiceImpl(dbViewsDAO);

		moduleService = new ModuleServiceImpl(moduleDAO, modelModuleHasOrthologyDAO, modelproteinDAO, moduleHasProteinDAO);

		projectService = new ProjectServiceImpl(projectDao);
	}


	public String[][] getCompartmentsAnnotationDataContainerStats() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String[][] result = annotationCompartmentsService.getCompartmentsAnnotationDataContainerStats();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	//***************************************************************************************************

	public Integer loadGene(String sequenceId, String name, String locusTag, String origin,
			String transcriptionDirection, String leftEndPosition, String rightEndPosition, String booleanRule) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = geneservice.loadGene(sequenceId, name, locusTag, origin, transcriptionDirection, leftEndPosition, rightEndPosition, booleanRule);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, String> getQueriesBySKey() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = annotationService.getQueriesBySKey();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Set<String> getAllDatabaseGenes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Set<String> result = geneservice.getAllDatabaseGenes();
			tx.commit();
			return (Set<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String, Set<String>> getGeneNamesAliases(String className) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Set<String>> result = geneservice.getGeneNamesAliases(className);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String, List<String>> getECNumbers() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, List<String>> result = geneservice.getECNumbers();
			tx.commit();
			return (Map<String, List<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<String, String> getSeqIdAndProteinName() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, String> result = geneservice.getSeqIdAndProteinName();
			tx.commit();
			return (Map<String, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String, Set<String>> getSeqIdAndAlias(String className) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Set<String>> result = geneservice.getSeqIdAndAlias(className);
			tx.commit();
			return (Map<String, Set<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String, Set<String>> getAllPathways() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Set<String>> result = pathwayservice.getAllPathways();
			tx.commit();
			return (Map<String, Set<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Pair<Integer, String>> getAllModelReactionHasModelProteinByReactionId(Integer idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Pair<Integer, String>> result = reactionservice.getAllModelReactionHasModelProteinByReactionId(idReaction);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String, Set<String>> getEnzymesPathways() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Set<String>> result = pathwayservice.getEnzymesPathways();
			tx.commit();
			return (Map<String, Set<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean isGeneCompartmentLoaded(Integer idGene) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = geneservice.isGeneCompartmentLoaded(idGene);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public boolean addPathway_has_Reaction(Integer idPathway, Integer idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = pathwayservice.addPathway_has_Reaction(idPathway, idReaction);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}



	public boolean addReaction_has_Enzyme(Integer idprotein, Integer idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = reactionservice.addReaction_has_Enzyme(idprotein, idReaction);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String, List<Integer>> loadEnzymeGetReactions(Integer idgene, Set<String> ecNumber, 
			String proteinName, boolean integratePartial, boolean integrateFull, 
			boolean insertProductNames, String classe, String inchi, Float molecularWeight, 
			Float molecularWeightExp, Float molecularWeightKd, Float molecularWeightSeq, Float pi,
			boolean inModel, String source, String aliasName, Integer aliasId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, List<Integer>> result = subunitservice.loadEnzymeGetReactions(idgene, ecNumber, proteinName, integratePartial, integrateFull, insertProductNames, classe, inchi, molecularWeight, molecularWeightExp, molecularWeightKd, molecularWeightSeq, pi, inModel, source, aliasName, aliasId);

			tx.commit();
			return (Map<String, List<Integer>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, String> getIdCompartmentAbbMap() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = compartmentservice.getIdCompartmentAbbMap();
			tx.commit();
			return (Map<Integer, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Set<String> getCompartments() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Set<String> result = compartmentservice.getCompartments();
			tx.commit();
			return (Set<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Integer> getEnzymeCompartments(String ecNumber) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = subunitservice.getEnzymeCompartments(ecNumber);
			tx.commit();
			return (List<Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public void updateLocusTag(String oldLocusTag, String newLocusTag) throws Exception {  
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			geneservice.updateLocusTag(oldLocusTag, newLocusTag);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public Map<String, String> loadGeneLocusFromHomologyData(String sequence_id)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, String> result = annotationService.loadGeneLocusFromHomologyData(sequence_id);
			tx.commit();
			return (Map<String, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public String getLocusTagFromHomologyData(String sequence_id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String result = annotationService.getLocusTagFromHomologyData(sequence_id);
			tx.commit();
			return (String) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public String getNcbiBlastDatabase() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String result = homologyservice.getNcbiBlastDatabase();
			tx.commit();
			return (String) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public String getHmmerDatabase() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String result = homologyservice.getHmmerDatabase();
			tx.commit();
			return (String) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String, Set<String>> getOrthologs(String entry) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Set<String>> result = orthologyservice.getOrthologs(entry);
			tx.commit();
			return (Map<String, Set<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Integer addBiomassPathway(String name, String code) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = pathwayservice.addBiomassPathway(name, code);
			tx.commit();
			return (Integer) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Integer insertCompoundToDatabase(String name, double molecularWeight) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = compoundservice.insertCompoundToDatabase(name, molecularWeight);
			tx.commit();
			return (Integer) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, ReactionContainer> getReactionsData(String ecnumber, Integer id, boolean isCompartimentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, ReactionContainer> result = reactionservice.getReactionsData(ecnumber, id, isCompartimentalized);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getGeneData(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = subunitservice.getGeneData(id);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<ModelReactionHasModelProtein> getProteinHasReaction() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ModelReactionHasModelProtein> result = reactionservice.getProteinHasReaction();
			tx.commit();
			return (List<ModelReactionHasModelProtein>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getReactionPathway() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = pathwayservice.getReactionPathway();
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getModelReactionNameAndECNumber(boolean originalreactions) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getModelReactionNameAndECNumber(originalreactions);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getGeneData2(String ecnumber, Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = geneservice.getGeneData2(ecnumber, id);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Set<String>> getCompoundsReactions(boolean isCompartimentalizedModel) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Set<String>> result = stoichservice.getCompoundsReactions(isCompartimentalizedModel);
			tx.commit();
			return (List<Set<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Integer> getReversibilities(Boolean inModel, Boolean isOriginalReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = stoichservice.getReversibilities(inModel, isOriginalReaction);
			tx.commit();
			return (List<Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getMetabolitesProperties(Boolean isCompartimentalized, Boolean inModel, List<ModelCompoundType> types) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = compoundservice.getMetabolitesProperties(isCompartimentalized, inModel, types);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<ModelCompound> getMetabolitesNotInModel(List<ModelCompoundType> typeList) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ModelCompound> result = compoundservice.getMetabolitesNotInModel(typeList);
			tx.commit();
			return (List<ModelCompound>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<ArrayList<String>> getReactions(Integer id, Integer compartment, boolean isCompartimentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ArrayList<String>> result = reactionservice.getReactions(id, compartment, isCompartimentalized);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public long countReactionsInModel(boolean isCompartimentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Long  result = reactionservice.countReactionsInModel(isCompartimentalized);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public long countReactionsByInModelAndSource(boolean isCompartmentalisedModel, boolean inModel, String source) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Long  result = reactionservice.countReactionsByInModelAndSource(isCompartmentalisedModel, inModel, source);
			tx.commit();
			return (Long) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public long countPathwayHasReactionByReactionId(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Long result = reactionservice.countPathwayHasReactionByReactionId(id);
			tx.commit();
			return (Long) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public ArrayList<String> getGenesModel() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			ArrayList<String> result = geneservice.getGenesModel();
			tx.commit();
			return (ArrayList<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<ProteinContainer> getEnzymesForReaction(Integer idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ProteinContainer> result = reactionservice.getEnzymesForReaction(idReaction);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<ProteinContainer> getEnzymesModel() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ProteinContainer> result = proteinservice.getEnzymesModel();
			tx.commit();
			return (List<ProteinContainer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public String getPathwaysByRowID(int id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String result = pathwayservice.getPathwaysByRowID(id);
			tx.commit();
			return (String) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getPathwaysList() throws Exception { 
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = pathwayservice.getPathwaysList();
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<ModelReaction> getReactionData(int id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ModelReaction> result = reactionservice.getReactionData(id);
			tx.commit();
			return (List<ModelReaction>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getStoichiometryData(int reactionID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = stoichservice.getStoichiometryData(reactionID);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Integer> getReactionsID(int pathway, int idProtein) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = pathwayservice.getReactionsID(pathway, idProtein);
			tx.commit();
			return (List<Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Integer> getPathwayID(int idReaction, int idProtein) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = pathwayservice.getPathwayID(idReaction, idProtein);
			tx.commit();
			return (List<Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Integer getStoichiometryID(int idReaction, int m, int idCompartment, double coefficient) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = stoichservice.getStoichiometryID(idReaction, m, idCompartment, coefficient);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Pair<Boolean, Boolean> checkReactionIsReversibleAndInModel(Integer reactionID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Pair<Boolean, Boolean> result = reactionservice.checkReactionIsReversibleAndInModel(reactionID);
			tx.commit();
			return (Pair<Boolean, Boolean>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Pair<String, String> getEquationAndSourceFromReaction(Integer reactionID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Pair<String, String> result = reactionservice.getEquationAndSourceFromReaction(reactionID);
			tx.commit();
			return (Pair<String, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getProteinsInModel() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = proteinservice.getProteinsInModel();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public List<String[]> getEnzymes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = proteinservice.getEnzymes();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getDistinctModelProteinAttributes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = proteinservice.getDistinctModelProteinAttributes();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getGeneInfo(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = geneservice.getGeneInfo(id);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<ModelProtein> getAllFromProtein() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ModelProtein> result = proteinservice.getAllFromProtein();
			tx.commit();
			return (List<ModelProtein>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getSynonyms(String class_, int entity) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = aliasesService.getSynonyms(class_, entity);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Integer getProteinID(String class_, String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = proteinservice.getProteinID(class_, name);
			tx.commit();
			return (Integer) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkAliasExistence(String class_, Integer entity, String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = aliasesService.checkAliasExistence(class_, entity, name);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public List<Integer> getReactionsIDs(Integer proteinId, String ecNumber) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = reactionservice.getReactionsIDs(proteinId, ecNumber);
			tx.commit();
			return (List<Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkEnzymeInModelExistence(Integer protId, String source) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = proteinservice.checkEnzymeInModelExistence(protId, source);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getReactionHasEnzymeData2(Integer reactionId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getReactionHasEnzymeData2(reactionId);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	//	public boolean checkEnzyme(Integer protId, String ecNumber) throws Exception {
	//		Transaction tx = null;
	//		try {
	//			tx = sessionFactory.getCurrentSession().beginTransaction();
	//			boolean result = proteinservice.checkEnzyme(protId, ecNumber);
	//			tx.commit();
	//			return (boolean) result;
	//		} catch (RuntimeException e) {
	//			tx.rollback();
	//			throw new Exception(e);
	//		}
	//	}

	public List<Integer> getDataFromReaction(String ecnumber) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = reactionservice.getDataFromReaction(ecnumber);
			tx.commit();
			return (List<Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String, String[]> getDataFromTU2(Map<String, String[]> qls) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, String[]> result = transcriptionservice.getDataFromTU2(qls);
			tx.commit();
			return (Map<String, String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getPromoterNameFromTU(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = transcriptionservice.getPromoterNameFromTU(id);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getProteinComposition() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = proteinservice.getProteinComposition();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Integer> getMetabolitesInModel() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = stoichservice.getMetabolitesInModel();
			tx.commit();
			return (List<Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getPathwayHasReactionData() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = pathwayservice.getPathwayHasReactionData();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Pair<String, String>> getReactionHasEnzyme(boolean isCompartimentalised) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Pair<String, String>> result = reactionservice.getReactionHasEnzyme(isCompartimentalised);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, String> getGeneIdLocusTag() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = geneservice.getGeneIdLocusTag();
			tx.commit();
			return (Map<Integer, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public void deleteDuplicatedQuerys(String query) throws Exception {  
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.deleteDuplicatedQuerys(query);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public Map<Integer, String> getGeneIds() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = geneservice.getGeneIds();
			tx.commit();
			return (Map<Integer, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Boolean checkIfModelPathwayNameExists(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Boolean result = pathwayservice.checkIfModelPathwayNameExists(name);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getStoichiometry(boolean isCompartmentalisedModel) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getStoichiometry(isCompartmentalisedModel);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getDataFromReaction2() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getDataFromReaction2();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public String getGeneId(String sequenceID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String result = geneservice.getGeneId(sequenceID);
			tx.commit();
			return (String) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean removeSelectedReaction(int reaction_id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = reactionservice.removeSelectedReaction(reaction_id);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getAllEnzymes(boolean isEncodedInGenome, boolean isCompartmentalizedModel) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = proteinservice.getAllEnzymes(isEncodedInGenome, isCompartmentalizedModel);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}
	
	public Map<Integer, Long> getProteinsData2() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, Long> result = proteinservice.getProteinsData2();
			tx.commit();
			return (Map<Integer, Long>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<ReactionContainer> getAllModelReactionByInModel() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ReactionContainer> result = reactionservice.getAllModelReactionByInModel();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, PathwayContainer> getPathways(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, PathwayContainer> result = pathwayservice.getPathways(id);
			tx.commit();
			return (Map<Integer, PathwayContainer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getMetabolitesWithBothProperties(Boolean isCompartimentalized, Boolean inModel, List<ModelCompoundType> types) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = compoundservice.getMetabolitesWithBothProperties(isCompartimentalized, inModel, types);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public String[] getBooleanRuleFromReaction(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String[] result = reactionservice.getBooleanRuleFromReaction(id);
			tx.commit();
			return (String[]) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getCompoundData(int id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = stoichservice.getCompoundData(id);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getRowInfoTFs(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = regulatoryservice.getRowInfoTFs(id);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String,String[]> getDataFromRegulatoryEvent(Map<String,String[]> qls) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String,String[]> result = regulatoryservice.getDataFromRegulatoryEvent(qls);
			tx.commit();
			return (Map<String,String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String,String[]> getDataFromTranscriptUnitPromoter(Map<String,String[]> qls) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String,String[]> result = transcriptionservice.getDataFromTranscriptUnitPromoter(qls);
			tx.commit();
			return (Map<String,String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Long countPromoterWithRegulationsByTFs() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Long result = transcriptionservice.countPromoterWithRegulationsByTFs();
			tx.commit();
			return (Long) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Integer[] countPromoters() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer[] result = regulatoryservice.countPromoters();
			tx.commit();
			return (Integer[]) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, String> getLocusKeys() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = annotationService.getLocusKeys();
			tx.commit();
			return (Map<Integer, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String, Boolean> getHomologyAvailabilities(Integer skey) throws Exception {  //FAZERRRR!!
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Boolean> result = annotationService.getHomologyAvailabilities(skey);
			tx.commit();
			return (Map<String, Boolean>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<InterproResults> getInterproAvailability(Integer key) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<InterproResults> result = annotationService.getInterproAvailability(key);
			tx.commit();
			return (List<InterproResults>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public String getHomologySequence(Integer skey) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String result = annotationService.getHomologySequence(skey);
			tx.commit();
			return (String) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<List<String>> getHomologySetup(Integer skey) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<List<String>> result = annotationService.getHomologySetup(skey);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getLoadedIntroProAnnotations(String status) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = interproservice.getLoadedIntroProAnnotations(status);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Integer loadInterProAnnotation(String query, String querySequence, String mostLikelyEC,
			String mostLikelyLocalization, String name, String mostLikelyName, String status) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = interproservice.loadInterProAnnotation(query, querySequence, mostLikelyEC, mostLikelyLocalization, name, mostLikelyName, status);
			tx.commit();
			return (Integer) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public void loadInterProLocation(int start, int end, float score, int hmmstart, int hmmend, float eValue,
			int envstart, int envend, int hmmlength, int resultID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			interproservice.loadInterProLocation(start, end, score, hmmstart, hmmend, eValue, envstart, envend, hmmlength, resultID);;
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public void loadInterProResultHasModel(int resultID, String modelAccession) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			interproservice.loadInterProResultHasModel(resultID, modelAccession);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public void setInterProStatus(Integer id, String status) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			interproservice.setInterProStatus(id, status);;
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public void deleteInterProEntries(String status) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			interproservice.deleteInterProEntries(status);;
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public List<String> getInterProGenes(String status) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = interproservice.getInterProGenes(status);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public void deleteSetOfGenes(Set<Integer> deleteGeneslist) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.deleteSetOfGenes(deleteGeneslist);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public List<String> getProgramFromHomologySetup(String status) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = homologyservice.getProgramFromHomologySetup(status);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getSpecificStats(String program, String status) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = homologyservice.getSpecificStats(program);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Integer> getAllFromGeneHomology(String program) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = homologyservice.getAllFromGeneHomology(program);
			tx.commit();
			return (List<Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Long getNumberOfHomologueGenes(String program) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Long result = homologyservice.getNumberOfHomologueGenes(program);
			tx.commit();
			return (Long) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getTaxonomy(String program) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = homologyservice.getTaxonomy(program);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public String geneHomologyHasHomologues(String locus) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String result = homologyservice.geneHomologyHasHomologues(locus);
			tx.commit();
			return (String) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getProgram(String query) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = annotationService.getProgram(query);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getProductRankData(String locus) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = annotationService.getProductRankData(locus);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public String getLastestUsedBlastDatabase() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String result = annotationService.getLastestUsedBlastDatabase();
			tx.commit();
			return (String) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getCommittedHomologyData() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = annotationService.getCommittedHomologyData();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean productListHasKey(Integer s_key) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = annotationService.productListHasKey(s_key);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean ecNumberListHasKey(Integer s_key) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = annotationService.ecNumberListHasKey(s_key);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, String> getDatabaseLocus() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = annotationService.getDatabaseLocus();
			tx.commit();
			return (Map<Integer, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean homologyDataHasKey(int key) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = homologyservice.homologyDataHasKey(key);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Integer getHomologyDataKey(int key) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = homologyservice.getHomologyDataKey(key);
			tx.commit();
			return (Integer) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getDataFromecNumberRank() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = annotationService.getDataFromecNumberRank();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getEcRank() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = annotationService.getEcRank();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Integer getMaxTaxRank() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = annotationService.getMaxTaxRank();
			tx.commit();
			return (Integer) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getProductRank() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = annotationService.getProductRank();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getTaxRank() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = annotationService.getTaxRank();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	//	public List<String[]> getProductRank2() throws Exception {
	//		Transaction tx = null;
	//		try {
	//			tx = sessionFactory.getCurrentSession().beginTransaction();
	//			List<String[]> result = annotationService.getProductRank2();
	//			tx.commit();
	//			return (List<String[]>) result;
	//		} catch (RuntimeException e) {
	//			tx.rollback();
	//			throw new Exception(e);
	//		}
	//	}

	public List<ModelGene> getGenesID() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ModelGene> result = geneservice.getGenesID();
			tx.commit();
			return (List<ModelGene>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkHomologuesHasEcNumber(int homologues_s_key, int ecnumber_s_key) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = annotationService.checkHomologuesHasEcNumber(homologues_s_key, ecnumber_s_key);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String, Integer> getQueriesByGeneId() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Integer> result = geneservice.getQueriesByGeneId();
			tx.commit();
			return (Map<String, Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Integer getGeneHomologySkey(Integer skey, String query) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = annotationService.getGeneHomologySkey(skey, query);
			tx.commit();
			return (Integer) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<String[]> getEnzymeProteinID(Integer idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getEnzymeProteinID(idReaction);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getEcNumbersList(Integer reactionID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = reactionservice.getEcNumbersList(reactionID);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getEnzymesByReaction(Integer idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getEnzymesByReaction(idReaction);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getEnzymesByReactionAndPathway(int idReaction, int pathway) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getEnzymesByReactionAndPathway(idReaction, pathway);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, String> getProteinIdAndEcNumber(Integer geneID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = subunitservice.getProteinIdAndEcNumber(geneID);
			tx.commit();
			return (Map<Integer, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public String[][] getProteins() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String[][] result = proteinservice.getProteins();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean loadGeneHomologyData(String homologyDataClient, String program) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = annotationService.loadGeneHomologyData(homologyDataClient, program);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean hasDrains() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = pathwayservice.hasDrains();
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean isTransporterLoaded() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = pathwayservice.isTransporterLoaded();
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean areCompartmentsPredicted() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = compartmentservice.areCompartmentsPredicted();
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getCompartmentsAnnotationReportsHasCompartmentsNameAndScore(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = compartmentservice.getCompartmentsAnnotationReportsHasCompartmentsNameAndScore(id);
			tx.commit();

			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	//	public boolean isCompartmentalisedModel() throws Exception {
	//		Transaction tx = null;
	//		try {
	//			tx = sessionFactory.getCurrentSession().beginTransaction();
	//			boolean result = compartmentservice.areCompartmentsPredicted();
	//			tx.commit();
	//			return (boolean) result;
	//		} catch (RuntimeException e) {
	//			tx.rollback();
	//			throw new Exception(e);
	//		}
	//	}

	public Map<Integer, String> getReactionsNames(List<Integer> identifiers) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = reactionservice.getReactionsNames(identifiers);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkCommitedData() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = homologyservice.checkCommitedData();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public String getSetupProgram() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String result = homologyservice.getSetupProgram();
			tx.commit();
			return (String) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Boolean isMetabolicDataLoaded() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Boolean result = compoundservice.isMetabolicDataLoaded();
			tx.commit();
			return (Boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getCompoundIDsFromStoichiometry() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = stoichservice.getCompoundIDsFromStoichiometry();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getAliasClassTU(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = aliasesService.getAliasClassTU(id);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getAliasClassG(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = aliasesService.getAliasClassG(id);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getAliasClassR(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = aliasesService.getAliasClassR(id);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getAliasClassP(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = aliasesService.getAliasClassP(id);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getEntryType(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = compoundservice.getEntryType(id);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getCompoundWithBiologicalRoles() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = compoundservice.getCompoundWithBiologicalRoles();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getCompoundReactions(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = compoundservice.getCompoundReactions(id);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<PathwayContainer> getAllFromPathwaySortedByName() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<PathwayContainer> result = pathwayservice.getAllFromPathwaySortedByName();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Long countCompoundsByName(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Long result = compoundservice.countCompoundsByName(name);
			tx.commit();
			return (Long) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getMetaboliteData(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = compoundservice.getMetaboliteData(name);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<ProteinContainer> getDataFromEnzyme(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ProteinContainer> result = proteinservice.getDataFromEnzyme(id);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getRelatedReactions(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = reactionservice.getRelatedReactions(name);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, String> getPathwaysNames() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = pathwayservice.getPathwaysNames();
			tx.commit();
			return (Map<Integer, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean isReactionInModel(int id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = reactionservice.isReactionInModel(id);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, String> getExistingPathwaysID(int idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = pathwayservice.getExistingPathwaysID(idReaction);
			tx.commit();
			return (Map<Integer, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getExistingEnzymesID(int idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = reactionservice.getExistingEnzymesID(idReaction);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkReactionHasEnzymeData(Integer idProtein, Integer idReaction)  throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = reactionservice.checkReactionHasEnzymeData(idProtein, idReaction);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, String> getExistingPathwaysID2(Map<Integer, String> existingPathwaysID, int idReaction)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = pathwayservice.getExistingPathwaysID2(idReaction);
			tx.commit();
			return (Map<Integer, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getExistingEnzymesID2(int idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = reactionservice.getExistingEnzymesID2(idReaction);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, String> getExistingPathwaysID2(int idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = pathwayservice.getExistingPathwaysID2(idReaction);
			tx.commit();
			return (Map<Integer, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Integer getPathwayID(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = pathwayservice.getPathwayID(name);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String, List<String>> getStoichiometryData2(int reactionId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, List<String>> result = stoichservice.getStoichiometryData2(reactionId);
			tx.commit();
			return (Map<String, List<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkGenes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = geneservice.checkGenes();
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getPathwayHasEnzymeData(int pathwayID, boolean isCompartimentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result =pathwayservice.getPathwayHasEnzymeData(pathwayID, isCompartimentalized);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkIfReactioLabelNameAlreadyExists(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = reactionservice.checkIfReactioLabelNameAlreadyExists(name);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public long countGenesSynonyms(String aliasClass) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			long result = aliasesService.countGenesSynonyms(aliasClass);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public long countGenesEncodingProteins() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Long result = subunitservice.countGenesEncodingProteins();
			tx.commit();
			return (Long) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public long countGenesInModel() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Long result = subunitservice.countGenesInModel();
			tx.commit();
			return (Long) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<ProteinContainer> getDataFromSubunit(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ProteinContainer> result = subunitservice.getDataFromSubunit(id);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getGPRstatusAndReactionAndDefinition(Integer proteinId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = subunitservice.getGPRstatusAndReactionAndDefinition(proteinId);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getDataFromGeneHasOrthology(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = orthologyservice.getDataFromGeneHasOrthology(id);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getDataFromGene(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = geneservice.getDataFromGene(id);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Integer> getReactionID(Integer proteinID, String ecNumber) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = reactionservice.getReactionID(proteinID, ecNumber);
			tx.commit();
			return (List<Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public long countProteinsEnzymesNotLikeSource(String source) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			long result = proteinservice.countProteinsEnzymesNotLikeSource(source);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public long countProteinsTransporters() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			long result = proteinservice.countProteinsTransporters();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public long countProteinsSynonyms(String class_) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			long result = aliasesService.countProteinsSynonyms(class_);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public long countProteinsComplexes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Long result = proteinservice.countProteinsComplexes();
			tx.commit();
			return (Long) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getAllProteins() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = proteinservice.getAllProteins();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> loadData() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = regulatoryservice.loadData();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getProteinName(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = regulatoryservice.getProteinName(id);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<PathwayContainer> getPathwaysIDsByReactionID(Integer idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<PathwayContainer> result = pathwayservice.getPathwaysIDsByReactionID(idReaction);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String[]> getAllFromProteinComposition() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = proteinservice.getAllFromProteinComposition();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getNameFromTranscriptionUnitPromoterTable(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = transcriptionservice.getNameFromTranscriptionUnitPromoterTable(id);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, ReactionContainer> getReactionIdAndPathwayID(String source, boolean original) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, ReactionContainer> result = pathwayservice.getReactionIdAndPathwayID(source, original);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Integer> getProteinIdByPathwayID(int id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = pathwayservice.getProteinIdByPathwayID(id);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Integer> getReactionIdByPathwayID(int id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = pathwayservice.getReactionIdByPathwayID(id);
			tx.commit();
			return (List<Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public int getSKeyFromOrganism(String organism) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = annotationService.getSKeyFromOrganism(organism);
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public boolean checkPathwayHasEnzymeEntryByProteinId(Integer idprotein) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = pathwayservice.checkPathwayHasEnzymeEntryByProteinId(idprotein);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkPathwayHasEnzymeEntryByReactionID(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = pathwayservice.checkPathwayHasEnzymeEntryByReactionID(id);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public int getecNumberSkey(String ecNumber) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = annotationService.getecNumberSkey(ecNumber);
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public int getProductRankSkey(int geneHomology_s_key, String name, Integer rank) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = annotationService.getProductRankSkey(geneHomology_s_key, name, rank);
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public int getProductRankHasOrganismSkey(int prodKey, int orgKey) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = annotationService.getProductRankHasOrganismSkey(prodKey, orgKey);
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkInternalIdFromDblinks(String cl, int internal, String database) throws Exception { 
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = dblinksService.checkInternalIdFromDblinks(cl, internal, database);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkEntityFromAliases(String cl, int entity, String alias) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = aliasesService.checkEntityFromAliases(cl, entity, alias);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkGeneIDFromSequence(int geneID, SequenceType type) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = sequenceservice.checkGeneIDFromSequence(geneID, type);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public int getGeneIDByLocusTag(String locusTag) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = geneservice.getGeneIDByLocusTag(locusTag);
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkModules(int gene, int protein_id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = subunitservice.checkModules(gene, protein_id);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean checkGeneHasOrthologyEntries(int geneID, int orthid) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = orthologyservice.checkGeneHasOrthologyEntries(geneID, orthid);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public int getPathwayIdByNameAndCode(String name, String code) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = pathwayservice.getPathwayIdByNameAndCode(name, code);
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}
	public boolean checkPathwayHasEnzymeData(Integer proteinID, Integer pathwayID) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = pathwayservice.checkPathwayHasEnzymeData(proteinID, pathwayID);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public int getCompoundIDbyExternalIdentifier(String externalID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = compoundservice.getCompoundIDbyExternalIdentifier(externalID);
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public boolean checkModelEnzymaticCofactorEntry(int compoundId, int proteinId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = proteinservice.checkModelEnzymaticCofactorEntry(compoundId, proteinId);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Integer getProteinIDFromName(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = proteinservice.getProteinIDFromName(name);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public int getProteinIDFromNameAndClass(String name, String class_) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = proteinservice.getProteinIDFromNameAndClass(name, class_);
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public long countGenesInGeneHasCompartment() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Long result = geneservice.countGenesInGeneHasCompartment();
			tx.commit();
			return (Long) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public int countGenes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = geneservice.countGenes();
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public String[] getAllOrganisms() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String[] result = annotationService.getAllOrganisms();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public String[] getAllGenus() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String[] result = annotationService.getAllGenus();
			tx.commit();
			return (String[]) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Double getBlastEValue(String database) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Double result = annotationService.getBlastEValue(database);
			tx.commit();
			return (Double) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public void initCompartments(Map<String, String> compartments) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			compartmentservice.initCompartments(compartments);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public int getCompartmentID(String localisation) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = compartmentservice.getCompartmentID(localisation);
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public ModelCompartment getCompartment(String localisation) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			ModelCompartment result = compartmentservice.getCompartment(localisation);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public String getPathwayCodeByName(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String result = pathwayservice.getPathwayCodeByName(name);
			tx.commit();
			return (String) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}







	public Map<Integer, String> getPathwayID3(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = pathwayservice.getPathwayID3(name);
			tx.commit();
			return (Map<Integer, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public long getNumberOfCompartments() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			long result = annotationCompartmentsService.getNumberOfCompartments();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<String, String> getAnnotationCompartments() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, String> result = annotationCompartmentsService.getAnnotationCompartments();
			tx.commit();
			return (Map<String, String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public void resetDatabaseScorer(String blastDatabase) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.resetDatabaseScorer(blastDatabase);;
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public List<String> getCommitedScorerData(String blastDatabase) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = annotationService.getCommitedScorerData(blastDatabase);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public void setBestAlphaFound(String blastDatabase) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.setBestAlphaFound(blastDatabase);;
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public List<String> bestAlphasFound(boolean bestAlpha) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = annotationService.bestAlphasFound(bestAlpha);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public void setLastestUsedBlastDatabase(String latestDB) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.setLastestUsedBlastDatabase(latestDB);;
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public int getEcNumberRankSkey(int geneHomology_s_key, String concatEC, int ecnumber) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = annotationService.getEcNumberRankSkey(geneHomology_s_key, concatEC, ecnumber);
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<String, List<String>> getECNumbers2() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, List<String>>  result = geneservice.getECNumbers2();
			tx.commit();
			return (Map<String, List<String>> ) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public void loadGenesCompartments(Integer idGene, Map<String, Integer> compartmentsDatabaseIDs,
			Integer primaryCompartment, String scorePrimaryCompartment, Map<String, String> secondaryCompartmens,
			boolean primLocation) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			geneservice.loadGenesCompartments(idGene, compartmentsDatabaseIDs, primaryCompartment, scorePrimaryCompartment, secondaryCompartmens, primLocation);;
			tx.commit();

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public Map<String, Integer> getCompartmentsDatabaseIDs(String primaryCompartment, String primaryCompartmentAbb,
			Map<String, Double> secondaryCompartmens, Map<String, String> secondaryCompartmensAbb,
			Map<String, Integer> compartmentsDatabaseIDs, String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Integer>  result = geneservice.getCompartmentsDatabaseIDs(primaryCompartment, primaryCompartmentAbb, secondaryCompartmens, secondaryCompartmensAbb, compartmentsDatabaseIDs, name);
			tx.commit();
			return (Map<String, Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Integer loadGene(String locusTag, String sequence_id, String geneName, String direction,
			String left_end, String right_end, String informationType) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer  result = geneservice.loadGene(locusTag, sequence_id, geneName, direction, left_end, right_end, informationType);
			tx.commit();
			return (Integer) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<String, Set<String>> getSequenceIds() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Set<String>>  result = geneservice.getSequenceIds();
			tx.commit();
			return (Map<String, Set<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<String[]> getAllGenes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]>  result = geneservice.getAllGenes();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<String[]> getRegulatoryGenes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]>  result = geneservice.getRegulatoryGenes();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<String[]> getEncodingGenes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]>  result = geneservice.getEncodingGenes();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<Integer, Integer> getProteins(Integer protId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, Integer>  result = geneservice.getProteins(protId);
			tx.commit();
			return (Map<Integer, Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<Integer, String[]> countReactionsByPathwayID(Map<Integer, String[]> qls, boolean isCompartimentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String[]> result = pathwayservice.countReactionsByPathwayID(qls, isCompartimentalized);
			tx.commit();
			return  result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public int selectCompartmentID(String compartment, String abbreviation) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = compartmentservice.selectCompartmentID(compartment, abbreviation);
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<CompartmentContainer> getCompartmentsInfo() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<CompartmentContainer> result = compartmentservice.getCompartmentsInfo();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Long countNumberOfGenes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			long result = annotationCompartmentsService.countNumberOfGenes();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<String, Integer> getCompartmentAbbIdMap() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Integer> result = compartmentservice.getCompartmentAbbIdMap();
			tx.commit();
			return (Map<String, Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public boolean removeSelectedReaction(Integer reaction_id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = reactionservice.removeSelectedReaction(reaction_id);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}



	public List<ModelReaction> getReactionData(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ModelReaction> result = reactionservice.getReactionData(id);
			tx.commit();
			return (List<ModelReaction>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public String[] getReactionData2(Integer reactionID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String[] result = reactionservice.getReactionData2(reactionID);
			tx.commit();
			return (String[]) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	//	public List<String[]> getEnzymesByReaction(Integer idReaction) throws Exception {
	//		Transaction tx = null;
	//		try {
	//			tx = sessionFactory.getCurrentSession().beginTransaction();
	//			List<String[]> result = reactionservice.getEnzymesByReaction(idReaction);
	//			tx.commit();
	//			return (List<String[]>) result;
	//		} catch (RuntimeException e) {
	//			tx.rollback();
	//			throw new Exception(e);
	//		}
	//	}


	public List<String[]> getEnzymesByReactionAndPathway(Integer idReaction, Integer pathway) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getEnzymesByReactionAndPathway(idReaction, pathway);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	//	public boolean containsReactionByOriginalReaction(boolean originalReaction) throws Exception {
	//		Transaction tx = null;
	//		try {
	//			tx = sessionFactory.getCurrentSession().beginTransaction();
	//			boolean result = reactionservice.containsReactionByOriginalReaction(originalReaction);
	//			tx.commit();
	//			return (boolean) result;
	//		} catch (RuntimeException e) {
	//			tx.rollback();
	//			throw new Exception(e);
	//		}
	//	}


	public boolean isReactionInModel(Integer reactionId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = reactionservice.isReactionInModel(reactionId);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<String> getExistingEnzymesID(Integer idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = reactionservice.getExistingEnzymesID(idReaction);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<String> getExistingEnzymesID2(Integer idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = reactionservice.getExistingEnzymesID2(idReaction);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<Integer, List<Integer>> getEnzymesReactions2() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, List<Integer>>  result = reactionservice.getEnzymesReactions2();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public boolean checkBiochemicalOrTransportReactions(boolean isTransporter) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean  result = reactionservice.checkBiochemicalOrTransportReactions(isTransporter);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public String[] getEnzymesByReaction2(Integer idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String[]  result = reactionservice.getEnzymesByReaction2(idReaction);
			tx.commit();
			return (String[]) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public String[] getEnzymesByReactionAndPathway2(Integer idReaction, Integer pathway) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String[]  result = reactionservice.getEnzymesByReactionAndPathway2(idReaction, pathway);
			tx.commit();
			return (String[]) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<Integer, ReactionContainer> getEnzymesReactionsMap(boolean isTransporters) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, ReactionContainer>  result = reactionservice.getEnzymesReactionsMap(isTransporters);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public ReactionContainer getDatabaseReactionContainer(Integer idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			ReactionContainer result = reactionservice.getDatabaseReactionContainer(idReaction);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Set<String> checkUndefinedStoichiometry(boolean isCompartmentalisedModel)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Set<String> result =stoichservice.checkUndefinedStoichiometry(isCompartmentalisedModel);
			tx.commit();
			return (Set<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<String[]> getStoichiometryInfo(boolean isCompartmentalisedModel) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getStoichiometryInfo(isCompartmentalisedModel);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

//	public List<String[]> getReactionHasEnzymeData(Integer id) throws Exception {
//		Transaction tx = null;
//		try {
//			tx = sessionFactory.getCurrentSession().beginTransaction();
//			List<String[]> result = reactionservice.getReactionHasEnzymeData(id);
//			tx.commit();
//			return (List<String[]>) result;
//		} catch (RuntimeException e) {
//			tx.rollback();
//			throw new Exception(e);
//		}
//	}


	public List<String[]> countReactions(Integer id, boolean isCompartmentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.countReactions(id, isCompartmentalized);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public ReactionContainer getDataForReactionContainerByReactionId(Integer reactionID, boolean isCompartmentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			ReactionContainer result = reactionservice.getDataForReactionContainerByReactionId(reactionID, isCompartmentalized);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<ReactionContainer> getDataForReactionContainer(boolean isCompartmentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ReactionContainer> result = reactionservice.getDataForReactionContainer(isCompartmentalized);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<String, List<String>> getECNumbers_() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, List<String>> result = subunitservice.getECNumbers_();
			tx.commit();
			return (Map<String, List<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<Integer> countGenesEncodingEnzymesAndTransporters() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = subunitservice.countGenesEncodingEnzymesAndTransporters();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public int countProteinsAssociatedToGenes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = subunitservice.countProteinsAssociatedToGenes();
			tx.commit();
			return (int) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<String[]> getSpecificStats(String program) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = homologyservice.getSpecificStats(program);
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<List<String>> getHomologyResults(int key) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<List<String>> result = homologyservice.getHomologyResults(key);
			tx.commit();
			return (List<List<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<List<String>> getHomologyTaxonomy(int key) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<List<String>> result = homologyservice.getHomologyTaxonomy(key);
			tx.commit();
			return (List<List<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<List<String>> getInterProResult(int key) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<List<String>> result = homologyservice.getInterProResult(key);
			tx.commit();
			return (List<List<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Integer loadInterProResult(String tool, float eValue, float score, String family, String accession,
			String name, String ec, String goName, String localization, String database, int resultsID)
					throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = homologyservice.loadInterProResult(tool, eValue, score, family, accession, name, ec, goName, localization, database, resultsID);
			tx.commit();
			return (Integer) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Integer loadInterProEntry(String accession, String description, String name, String type) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = homologyservice.loadInterProEntry(accession, description, name, type);
			tx.commit();
			return (Integer) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public void loadXrefs(String category, String database, String name, String id, int entryID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			homologyservice.loadXrefs(category, database, name, id, entryID);;
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Integer loadInterProModel(String accession, String name, String description) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = homologyservice.loadInterProModel(accession, name, description);
			tx.commit();
			return (Integer) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Set<String> getGenesFromDatabase(String eVal, String matrix, Integer numberOfAlignments, String wordSize,
			String program, String databaseID, boolean deleteProcessing) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Set<String>  result = homologyservice.getGenesFromDatabase(eVal, matrix, numberOfAlignments, wordSize, program, databaseID, deleteProcessing);
			tx.commit();
			return (Set<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Set<String> getGenesFromDatabase(String program, boolean deleteProcessing) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Set<String>  result = homologyservice.getGenesFromDatabase(program, deleteProcessing);
			tx.commit();
			return (Set<String> ) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<String, List<String>> getUniprotEcNumbers() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, List<String>> result = homologyservice.getUniprotEcNumbers();
			tx.commit();
			return (Map<String, List<String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<String[]>  getCommittedHomologyData2() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = homologyservice.getCommittedHomologyData2();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<String[]> getCommittedHomologyData3() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]>result = homologyservice.getCommittedHomologyData3();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<Integer, Long> getHomologuesCountByEcNumber() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, Long> result = homologyservice.getHomologuesCountByEcNumber();
			tx.commit();
			return (Map<Integer, Long>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<Integer, Long> getHomologuesCountByProductRank() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, Long> result = homologyservice.getHomologuesCountByProductRank();
			tx.commit();
			return (Map<Integer, Long>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<String[]> getGenesInformation() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = homologyservice.getGenesInformation();
			tx.commit();
			return (List<String[]>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<String, Set<Integer>> getGenesPerDatabase() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Set<Integer>> result = homologyservice.getGenesPerDatabase();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public String[] getBlastDatabases(String program) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String[] result = homologyservice.getBlastDatabases(program);
			tx.commit();
			return (String[]) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public void deleteHomologyData(String database) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			homologyservice.deleteHomologyData(database);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public void insertAutomaticEnzymeAnnotation(Integer homologySkey, String locusTag, String geneName, String product,
			String ecnumber, boolean selected, String chromossome, String notes, Map<Integer, String> locusTag2,
			Map<Integer, String> geneName2, Map<Integer, String> ecMap, Map<Integer, String> confLevelMap)
					throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			homologyservice.insertAutomaticEnzymeAnnotation(homologySkey, locusTag, geneName, product, ecnumber, selected, chromossome, notes, locusTag2, geneName2, ecMap, confLevelMap);;
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}


	public String getEbiBlastDatabase() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String result = homologyservice.getEbiBlastDatabase();
			tx.commit();
			return (String) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<String, Pair<Integer, String>> getModelInformationForBiomass(List<String> metaboliteIDs)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Pair<Integer, String>> result = compoundservice.getModelInformationForBiomass(metaboliteIDs);
			tx.commit();
			return (Map<String, Pair<Integer, String>>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public MetaboliteContainer getModelCompoundByName(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			MetaboliteContainer result = compoundservice.getModelCompoundByName(name);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<Integer, MetaboliteContainer> getAllMetabolites() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, MetaboliteContainer> result = compoundservice.getAllMetabolites();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<Integer> getEnzymesStats(String originalReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = proteinservice.getEnzymesStats(originalReaction);
			tx.commit();
			return (List<Integer>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


//	public Pair<List<String>, Boolean[]> getECnumber(int proteinID) throws Exception {
//		Transaction tx = null;
//		try {
//			tx = sessionFactory.getCurrentSession().beginTransaction();
//			Pair<List<String>, Boolean[]> result = proteinservice.getECnumber(proteinID);
//			tx.commit();
//			return (Pair<List<String>, Boolean[]>) result;
//		} catch (RuntimeException e) {
//			tx.rollback();
//			throw new Exception(e);
//		}
//	}


//	public String[] getECnumber2(int proteinID) throws Exception {
//		Transaction tx = null;
//		try {
//			tx = sessionFactory.getCurrentSession().beginTransaction();
//			String[] result = proteinservice.getECnumber2(proteinID);
//			tx.commit();
//			return (String[]) result;
//		} catch (RuntimeException e) {
//			tx.rollback();
//			throw new Exception(e);
//		}
//	}


	public Map<String, Pair<String, Pair<Double, String>>> getExistingMetabolitesID(int idReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Pair<String, Pair<Double, String>>>  result = stoichservice.getExistingMetabolitesID(idReaction);
			tx.commit();
			return ( Map<String, Pair<String, Pair<Double, String>>> ) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Pair<Double, Double> getReactantsAndProducts(boolean isCompartmentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Pair<Double, Double> result = stoichservice.getReactantsAndProducts(isCompartmentalized);
			tx.commit();
			return (Pair<Double, Double>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public ProteinContainer getProteinData(int id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			ProteinContainer result = proteinservice.getProteinData(id);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public List<String> getAliasClassC(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = aliasesService.getAliasClassC(id);
			tx.commit();
			return (List<String>) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public double getAvarageNumberOfPromotersByTU(int num) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			double result = transcriptionservice.getAvarageNumberOfPromotersByTU(num);
			tx.commit();
			return (double) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public Map<String, List<String[]>> getDataFromRegulatoryEvent2() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, List<String[]>> result =regulatoryservice.getDataFromRegulatoryEvent2();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public void updateReactionProperties(int reactionID, int columnNumber, Object object, Integer notesColumn,
			Integer isReversibleColumn, Integer inModelColun) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			reactionservice.updateReactionProperties(reactionID, columnNumber, object, notesColumn, isReversibleColumn, inModelColun);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public void insertNewGene(String name, String transcription_direction, String left_end_position,
			String right_end_position, String[] subunits, String locusTag) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			geneservice.insertNewGene(name, transcription_direction, left_end_position, right_end_position, subunits, locusTag);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public void updateGene(int geneIdentifier, String name, String transcription_direction, String left_end_position,
			String right_end_position, String[] subunits, String[] oldSubunits, String locusTag) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			geneservice.updateGene(geneIdentifier, name, transcription_direction, left_end_position, right_end_position, subunits, oldSubunits, locusTag);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public void removeGeneAssignemensts(Integer geneIdentifier, String proteindIdentifier) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			geneservice.removeGeneAssignemensts(geneIdentifier, proteindIdentifier);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public boolean checkSubunitData(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = subunitservice.checkSubunitData(id);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}



	public void insertEnzymes(int idProtein, String ecnumber, boolean editedReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			proteinservice.insertEnzymes(idProtein, ecnumber, editedReaction);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public boolean insertProtein(ProteinContainer protein,
			String[] synonyms, String[] enzymes) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = proteinservice.insertProtein(protein, synonyms, enzymes);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	//	public void setExternalCompartment(String compartment) throws Exception {
	//		try {
	//			sessionFactory.getCurrentSession().beginTransaction();
	//			reactionservice.setExternalCompartment(compartment);
	//			tx.commit();
	//		} catch (RuntimeException e) {
	//			tx.rollback();
	//			throw new Exception(e);
	//		}
	//		
	//	}


	public boolean updateProtein(ProteinContainer protein, String[] synonyms, String[] oldSynonyms, String[] enzymes, String[] oldEnzymes) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = proteinservice.updateProtein(protein, synonyms, oldSynonyms, enzymes, oldEnzymes);
			tx.commit();
			return (boolean) result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	public void load_geneHomology_has_homologues(String referenceID, String gene, Float eValue, Float bits,
			Integer geneHomology_s_key, Integer homologues_s_key) throws Exception{
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.load_geneHomology_has_homologues(referenceID, gene, eValue, bits, geneHomology_s_key, homologues_s_key);;
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public void saveDatabaseStatus(String blastDatabase, Float threshold, Float upperThreshold, Float alpha, Float beta,
			Integer minHits) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			proteinservice.saveDatabaseStatus(blastDatabase, threshold, upperThreshold, alpha, beta, minHits);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public void loadProductRank(Map<String, Integer> pd, Integer geneHomology_s_key, Map<String, List<Integer>> prodOrg)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.loadProductRank(pd, geneHomology_s_key, prodOrg);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public void commitToDatabase(String blastDatabase, Float threshold, Map<Integer, String[]>  annotationEditedEnzymeData, Map<Integer, String> annotationEnzymesList, Map<Integer, String[]> annotationEditedProductData, Map<Integer, String> annotationProductList, Map<Integer, String> annotationNamesList,
			Map<Integer, String> annotationNotesMap, Map<Integer, String> annotationLocusList, Float upperThreshold, Float alpha, Float beta, Integer minHits ) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			proteinservice.commitToDatabase(blastDatabase, threshold, annotationEditedEnzymeData, annotationEnzymesList, annotationEditedProductData, annotationProductList, annotationNamesList, annotationNotesMap, annotationLocusList, upperThreshold, alpha, beta, minHits);;
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	public void removeEnzymesAssignments(String ecnumber, Boolean[] inModel, List<String> enzymes_ids,
			Integer proteinID, boolean removeReaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			proteinservice.removeEnzymesAssignments(ecnumber, inModel, enzymes_ids, proteinID, removeReaction);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public boolean checkPathwayHasReactionData(Integer idreaction, Integer pathwayID) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = pathwayservice.checkPathwayHasReactionData(idreaction, pathwayID);
			tx.commit();

			return result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Integer getMetaboliteIdByName(String name) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = compoundservice.getMetaboliteIdByName(name);
			tx.commit();

			return result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public boolean isCompartimentalizedModel() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = reactionservice.isCompartimentalizedModel();
			tx.commit();

			return (boolean) result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public int getGeneLastInsertedID() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int result = geneservice.getGeneLastInsertedID();
			tx.commit();

			return result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<String, Integer> getQueries() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Integer> result = annotationService.getQueries();
			tx.commit();

			return result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	/**
	 *
	 */
	public Integer insertNameAndAbbreviation(String name, String abb) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = compartmentservice.insertNameAndAbbreviation(name, abb);
			tx.commit();

			return result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	/**
	 *
	 */
	@Override
	public Integer insertModelPathwayCodeAndName(String code, String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = pathwayservice.insertModelPathwayCodeAndName(code, name);
			tx.commit();

			return result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	/**
	 *
	 */
	@Override
	public void insertModelPathwayIdAndSuperPathway(Integer id, Integer superID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			pathwayservice.insertModelPathwayIdAndSuperPathway(id, superID);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	/**
	 *
	 */
	@Override
	public boolean deleteModelPathwayHasModelProteinByPathwayId(Integer pathId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = pathwayservice.deleteModelPathwayHasModelProteinByPathwayId(pathId);
			tx.commit();
			return result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	/**
	 *
	 */
	@Override
	public boolean deleteModelPathwayHasReactionByPathwayId(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = pathwayservice.deleteModelPathwayHasReactionByPathwayId(id);
			tx.commit();
			return result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	/**
	 *
	 */
	@Override
	public boolean deleteModelPathwayHasModelProteinByIdProtein(Integer idprotein) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = pathwayservice.deleteModelPathwayHasModelProteinByIdProtein(idprotein);
			tx.commit();
			return result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	/**
	 *
	 */
	@Override
	public boolean deleteModelPathwayHasReactionByReactionId(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = pathwayservice.deleteModelPathwayHasReactionByReactionId(id);
			tx.commit();
			return result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	/**
	 *
	 */
	@Override
	public void updateModelReactionReversibleAndLowerBoundAndEquationByReactionId(Boolean reversible, Long lower, Integer id, String equation)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			reactionservice.updateModelReactionReversibleAndLowerBoundAndEquationByReactionId(reversible, lower, id, equation);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	/**
	 *
	 */
	@Override
	public void updateModelReactionInModelByReactionId(Integer reactionId, Boolean inModel) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			reactionservice.updateModelReactionInModelByReactionId(reactionId, inModel);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public void removeModelGeneByGeneId(Integer geneId) throws Exception {

		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			geneservice.removeModelGeneByGeneId(geneId);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	@Override
	public void updateReaction(String name, String equation, Boolean reversible, Integer compartmentId,
			Boolean isSpontaneous, Boolean isNonEnzymatic, Boolean isGeneric, String booleanRule, Long lower,
			Long upper, Integer reactionId, Boolean inModel) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			reactionservice.updateReaction(name, equation, reversible, compartmentId, isSpontaneous, isNonEnzymatic, isGeneric, booleanRule, lower, upper, reactionId, inModel);
			tx.commit();

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public boolean deleteModelReactionHasModelProteinByReactionId(Integer reactionId, Integer protId)
			throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			boolean res = reactionservice.deleteModelReactionHasModelProteinByReactionId(reactionId, protId);
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public boolean deleteModelPathwayHasReactionByReactionIdAndPathwayId(Integer reactionId, Integer pathId)
			throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			boolean res = pathwayservice.deleteModelPathwayHasReactionByReactionIdAndPathwayId(reactionId, pathId);
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void insertModelPathwayHasReaction(Integer idReaction, Integer idPathway) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {

			boolean pathwayHasReactionInDatabase = pathwayservice.checkPathwayHasReactionData(idReaction, idPathway);
			if (!pathwayHasReactionInDatabase) {
				pathwayservice.insertModelPathwayHasReaction(idReaction, idPathway);
			}
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void insertModelPathwayHasModelProtein(Integer pathId, Integer proteinId) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			pathwayservice.insertModelPathwayHasModelProtein(pathId, proteinId);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void insertModelReactionHasModelProtein(Integer idReaction, Integer idprotein) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			reactionservice.insertModelReactionHasModelProtein(idReaction, idprotein);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public List<Integer> getModelPathwayHasEnzymeReactionIdByEcNumberAndPathwayIdAndProteinId(Integer pathwayId, Integer proteinId) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			List<Integer> res = pathwayservice.getModelPathwayHasEnzymeReactionIdByEcNumberAndPathwayIdAndProteinId(pathwayId, proteinId);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<Integer> getModelPathwayHasEnzymeReactionIdByEcNumberAndReactionIdAndProteinId(Integer reactId, Integer proteinId) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			List<Integer> res = pathwayservice.getModelPathwayHasEnzymeReactionIdByEcNumberAndReactionIdAndProteinId(reactId, proteinId);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public boolean deleteModelStoichiometryByStoichiometryId(Integer stoichId) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			boolean res = stoichservice.deleteModelStoichiometryByStoichiometryId(stoichId);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void insertModelStoichiometry(Integer reactionId, Integer compoundId, Integer compartmentId,
			double stoichiometric_coef) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			stoichservice.insertModelStoichiometry(reactionId, compoundId, compartmentId, stoichiometric_coef);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public void updateModelStoichiometryByStoichiometryId(Integer id, double coeff, Integer compartmentId,
			Integer compoundId) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			stoichservice.updateModelStoichiometryByStoichiometryId(id, coeff, compartmentId, compoundId);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public void updateModelReactionNotesByReactionId(Integer reactionId, String notes) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			reactionservice.updateModelReactionNotesByReactionId(reactionId, notes);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}


	}


	@Override
	public void updateInModelAndSourceByReactionId(Integer idreaction, boolean inModel, String source)
			throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			reactionservice.updateInModelAndSourceByReactionId(idreaction, inModel, source);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public List<String> getAllPathwaysNamesOrdered() throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			List<String> res = pathwayservice.getAllPathwaysNamesOrdered();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<String> getAllPathwaysOrderedByNameInModelWithReaction(Boolean inModel) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			List<String> res = pathwayservice.getAllPathwaysOrderedByNameInModelWithReaction(inModel);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<ModelDesnormalizedReactionPathwayAndCompartment> getAllReactionsView(Boolean compartimentalized, Boolean encodedOnly) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			List<ModelDesnormalizedReactionPathwayAndCompartment> res = desnormalizerService.getAllReactionsView(compartimentalized, encodedOnly);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<String[]> getEnzymesAnnotationEcNumberrankAttributesByLocusTag(String query) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			List<String[]> res = annotationService.getEnzymesAnnotationEcNumberrankAttributesByLocusTag(query);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Integer getProjectID(Long genomeID) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			Integer res = projectService.getProjectID(genomeID);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void updateOrganismID(Long genomeID) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			projectService.updateOrganismID(genomeID);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<ModelSequence> getAllModelSequence() throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			List<ModelSequence> res = sequenceservice.getAllModelSequence();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<String, SequenceContainer> getSequencesWithQueriesBySequenceType(SequenceType seqtype) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			Map<String, SequenceContainer> res = sequenceservice.getSequencesWithQueriesBySequenceType(seqtype);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void closeJDBCConnection() {
		sessionFactory.getCurrentSession().close();
		// AFTER THIS NO MORE DB CONNECTION IN THIS SERVICE!!
	}


	@Override
	public List<Integer[]> getAllModelCompoundIdWithCompartmentIdCountedReactions(Boolean isCompartimentalized,
			Boolean inModel, List<ModelCompoundType> type, Boolean withTransporters) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			List<Integer[]> res = compoundservice.getAllModelCompoundIdWithCompartmentIdCountedReactions(isCompartimentalized, inModel, type, withTransporters);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void updateGenesLocusByQuery(String query, String locus) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			geneservice.updateGenesLocusByQuery(query, locus);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public Map<String, Integer> getGeneIdByQuery() throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			Map<String, Integer> res = geneservice.getGeneIdByQuery();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public void insertCompoundGeneratingExternalID(String name, String entryType, String formula, String molecularW,
			String charge) throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			compoundservice.insertCompoundGeneratingExternalID(name, entryType, formula, molecularW, charge);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public long countEntriesInGeneHomology() throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			Long num = homologyservice.countEntriesInGeneHomology();
			tx.commit();

			return num;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public long countEntriesInGene() throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			Long num = geneservice.countEntriesInGene();
			tx.commit();

			return num;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public long countGenesWithName() throws Exception {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		try {
			Long num = geneservice.countGenesWithName();
			tx.commit();

			return num;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	/**
	 * @throws Exception 
	 *
	 */
	@Override
	public void updateModelCompoundAttributes(String name, String entryType, String formula, String molecularW,
			Short charge, String externalIdentifier) throws Exception{
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			compoundservice.updateModelCompoundAttributes(name, entryType, formula, molecularW, charge, externalIdentifier);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	/**
	 * @throws Exception 
	 *
	 */
	@Override
	public Map<Integer, MetaboliteContainer> getModelStoichiometryByReactionId(Integer reactionID) throws Exception{
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, MetaboliteContainer> result = stoichservice.getModelStoichiometryByReactionId(reactionID);
			tx.commit();
			return result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<Integer, ReactionContainer> getAllModelReactionAttributesByReactionId(boolean isCompartimentalised)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, ReactionContainer> result = reactionservice.getAllModelReactionAttributesByReactionId(isCompartimentalised);
			tx.commit();
			return  result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer getReactionLabelIdByName(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer id = reactionservice.getReactionLabelIdByName(name);
			tx.commit();
			return id;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public ModelReactionLabels getReactionLabelByName(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			ModelReactionLabels res = reactionservice.getReactionLabelByName(name);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<Integer> getReactionsRelatedToLabelName(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> res = reactionservice.getReactionsRelatedToLabelName(name);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public ModelReactionLabels insertNewModelReactionLabel(String equation, boolean isGeneric, boolean isNonEnzymatic, boolean isSpontaneous, String name, String source) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			ModelReactionLabels res = reactionservice.insertNewModelReactionLabel(equation, isGeneric, isNonEnzymatic, isSpontaneous, name, source);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Integer insertNewReaction(boolean inModel, double lowerBound, double upperBound, String boolean_rule,
			String equation, boolean isGeneric, boolean isNonEnzymatic, boolean isSpontaneous, String name,
			String source, Integer compartmentId, String notes) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer res = reactionservice.insertNewReaction(inModel, lowerBound, upperBound, boolean_rule, equation,
					isGeneric, isNonEnzymatic, isSpontaneous, name, source, compartmentId, notes);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Integer insertProjectEntry(Long genomeID, Integer version) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer res = projectService.insertProjectEntry(genomeID, version);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public String[] getOrganismData(Long genomeID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String[] res = projectService.getOrganismData(genomeID);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void updateOrganismData(long genomeID, String[] data) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			projectService.updateOrganismData(genomeID, data);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer getOrganismID() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer res = projectService.getOrganismID();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void loadFastaSequences(Map<Integer, String[]> sequences, SequenceType seqType) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			sequenceservice.loadFastaSequences(sequences, seqType);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public Map<String, Integer> countGenesReactionsBySubunit() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Integer> res = subunitservice.countGenesReactionsBySubunit();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public String[][] getSubunitsByGeneId(int geneIdentifier) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String[][] res = subunitservice.getSubunitsByGeneId(geneIdentifier);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<GeneContainer> getSequenceByGeneId(int idGene) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<GeneContainer> res = sequenceservice.getSequenceByGeneId(idGene);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public int[] countProteins() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int[] res = proteinservice.countProteins();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public boolean checkGenomeSequencesByType(SequenceType type) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean res = sequenceservice.checkGenomeSequencesByType(type);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<String, Integer> getProteinsCountFromSubunit() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Integer> res = subunitservice.getProteinsCountFromSubunit();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<ReactionContainer> getReactionDataForStats(boolean isCompartimentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ReactionContainer>  res = reactionservice.getReactionDataForStats(isCompartimentalized);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<String[]> getUpdatedPathways(boolean isCompartimentalized, boolean encodedOnly) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]>  res = pathwayservice.getUpdatedPathways(isCompartimentalized, encodedOnly);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void insertNewSequence(Integer idGene, SequenceType type, String sequence, Integer sequenceLength) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			sequenceservice.insertNewSequence(idGene, type, sequence, sequenceLength);
			tx.commit();

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, String[]> countProteinIdByPathwayID(Map<Integer, String[]> qls) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String[]>  res = pathwayservice.countProteinIdByPathwayID(qls);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer insertNewReaction(ReactionContainer container) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer id = reactionservice.insertNewReaction(container);
			tx.commit();
			return id;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<String, List<Integer>> getModelReactionIdsRelatedToNames() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, List<Integer>> id = reactionservice.getModelReactionIdsRelatedToNames();
			tx.commit();
			return id;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void insertNewModelAliasEntry(String cl, int entity, String alias) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			aliasesService.insertNewModelAliasEntry(cl, entity, alias);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer insertNewProteinEntry(String name, String classString) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer res = proteinservice.insertNewProteinEntry(name, classString);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<ModelSequence> getAllEntityModelSequences() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ModelSequence> res = sequenceservice.getAllEntityModelSequences();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<ModelCompartment> getAllEntityModelCompartments() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ModelCompartment> res = compartmentservice.getAllEntityModelCompartments();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public Map<Integer, String> getModelCompartmentIdAndName() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> res = compartmentservice.getModelCompartmentIdAndName();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public CompartmentContainer findCompartmentByName(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			CompartmentContainer res = compartmentservice.findCompartmentByName(name);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<Integer> getAllModelReactionsIDsByAttributes(String equation, boolean inModel, boolean isGeneric,
			boolean isSpontaneous, boolean isNonEnzymatic, String source, boolean isCompartimentalized)
					throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> res = reactionservice.getAllModelReactionsIDsByAttributes(equation, inModel, isGeneric, isSpontaneous, isNonEnzymatic, source, isCompartimentalized);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}




	@Override
	public void insertModelGeneHasCompartment(boolean PrimaryLocation, Double score,
			Integer model_compartment_idcompartment, Integer model_gene_idgene) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			this.geneservice.insertModelGeneHasCompartment(PrimaryLocation, score, model_compartment_idcompartment, model_gene_idgene);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Integer insertGeneHomologyEntry(String locusTag, String uniprotEcnumber, Boolean uniprotStar, int setupId,
			String query, DatabaseProgressStatus status,int seqId,String chromosome, String organelle) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int res = homologyservice.insertGeneHomologyEntry(locusTag, uniprotEcnumber, uniprotStar, setupId, query, status,seqId,organelle,chromosome);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public void updateGeneHomologyEntry(String locusTag, String uniprotEcnumber, Boolean uniprotStar, int setupId,
			String query, DatabaseProgressStatus status, int seqId, String chromosome, String organelle, int sKey) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			homologyservice.updateGeneHomologyEntry(locusTag, uniprotEcnumber, uniprotStar, setupId, query, status, seqId, chromosome, organelle, sKey);

			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void updateGeneHomologyStatus(String databaseName, String locusTag, DatabaseProgressStatus status) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			homologyservice.updateGeneHomologyStatus(databaseName, locusTag, status);

			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Integer insertModelCompound(String name, String inchi, String entry_type,
			String external_identifier, String formula, String molecular_weight, String neutral_formula, Short charge,
			String smiles, Boolean hasBiologicalRoles) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = compoundservice.insertModelCompound(name, inchi, entry_type, external_identifier, formula, molecular_weight, neutral_formula, charge, smiles, hasBiologicalRoles);;	
			tx.commit();
			return result;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	@Override
	public Integer insertGeneHomologues(int organismSKey, String locusID, String definition,
			Float calculatedMw, String product,String organelle,Boolean uniprotStar) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int value = homologyservice.insertGeneHomologues(organismSKey, locusID, definition, calculatedMw, product, organelle, uniprotStar);

			tx.commit();
			return value;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public Boolean checkReactionNotLikeSourceAndNotReversible(String source) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Boolean res = reactionservice.checkReactionNotLikeSourceAndNotReversible(source);

			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<Integer, String> getLabelsByExternalIdentifiers(String name) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> res = reactionservice.getLabelsByExternalIdentifiers(name);

			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<Object[]> getReactionsList(Integer pathwayID, boolean noEnzymes, boolean isCompartimentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Object[]> res = reactionservice.getReactionsList(pathwayID, noEnzymes, isCompartimentalized);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<Integer> getModelReactionLabelIdByName(String name, boolean isCompartimentalized)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> res = reactionservice.getModelReactionLabelIdByName(name, isCompartimentalized);

			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<String[]> getGPRsECNumbers(boolean isCompartimentalized)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> res = subunitservice.getGPRsECNumbers(isCompartimentalized);

			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override

	public List<String[]> getDataFromReactionForBlockedReactionsTool(boolean isCompartimentalized) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> res = reactionservice.getDataFromReactionForBlockedReactionsTool(isCompartimentalized);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}



	@Override
	public void updateBooleanRuleAndNotes(List<String> names, Map<String, String> rules, String message)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			reactionservice.updateBooleanRuleAndNotes(names, rules, message);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}



	@Override
	public void removeReactionsFromModelByBooleanRule(List<String> names, boolean keepReactionsWithNotes,
			boolean keepManualReactions) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			reactionservice.removeReactionsFromModelByBooleanRule(names, keepReactionsWithNotes, keepManualReactions);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}


	@Override
	public List<Object[]> getAllModelReactionsByTransportersAndIsCompartimentalized(boolean isTransporters) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Object[]> res = reactionservice.getAllModelReactionsByTransportersAndIsCompartimentalized(isTransporters);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<String> getAllCompoundsInModel() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> res = compoundservice.getAllCompoundsInModel();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<Integer, ReactionContainer> getAllModelReactionAttributesbySource(boolean isCompartimentalized,
			String source) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, ReactionContainer> res = reactionservice.getAllModelReactionAttributesbySource(isCompartimentalized, source);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Boolean checkModelSequenceType(String sequenceType) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Boolean res = geneservice.checkModelSequenceType(sequenceType);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public String getCompartmentsTool(Integer taxID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String res = projectService.getCompartmentsTool(taxID);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public GeneContainer getGeneDataById(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			GeneContainer res = geneservice.getGeneDataById(id);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer insertEcNumberEntry(String ecNumber) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer res = annotationService.insertEcNumberEntry(ecNumber);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<Object[]> getReactantsOrProductsInCompartment(Boolean isCompartimentalized, Boolean Reactants, Boolean Products) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Object[]> res = compartmentservice.getReactantsOrProductsInCompartment(isCompartimentalized, Reactants, Products);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<String> getECNumbersWithModules() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> res = proteinservice.getECNumbersWithModules();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void updateECNumberModuleStatus(String ecNumber, String status) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			moduleService.updateECNumberModuleStatus(ecNumber, status);
			tx.commit();

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public ArrayList<String[]> getCompartmentDataByName(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			ArrayList<String[]> res = compartmentservice.getCompartmentDataByName(name);
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Map<String, Integer> getEnzymeEcNumberAndProteinID() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Integer> res = proteinservice.getEnzymeEcNumberAndProteinID();
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Map<Integer, List<Integer>> getEnzymesCompartments() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, List<Integer>> res = proteinservice.getEnzymesCompartments();
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<String, Integer> getGeneLocusTagAndIdgene() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Integer> res = geneservice.getGeneLocusTagAndIdgene();
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<String, Integer> getPathwayCodeAndIdpathway() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Integer> res = pathwayservice.getPathwayCodeAndIdpathway();
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<String, Integer> getModelModuleEntryIdAndId() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Integer> res = moduleService.getModelModuleEntryIdAndId();
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public boolean checkModulesByModuleId(int geneId, int proteinId, int moduleId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean res = subunitservice.checkModulesByModuleId(geneId, proteinId, moduleId);
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Map<String, Integer> getExternalIdentifierAndIdCompound() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Integer> res = compoundservice.getExternalIdentifierAndIdCompound();
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<String, Integer> getEntryIdAndOrthologyId() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Integer> res = orthologyservice.getEntryIdAndOrthologyId();
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}



	@Override
	public String getEnzymeEcNumberByProteinID(Integer proteinId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String res = proteinservice.getEnzymeEcNumberByProteinID(proteinId);
			tx.commit();
			return res;
		}

		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public ArrayList<String[]> getAllGenes2() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			ArrayList<String[]> res = geneservice.getAllGenes2();

			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<String, List<Integer>> getEcNumbersByReactionId() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, List<Integer>> res = reactionservice.getEcNumbersByReactionId();
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	@Override
	public void updateProteinSetEcNumberSourceAndInModel(Integer model_protein_idprotein, String ecnumber,
			boolean inModel, String source) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			proteinservice.updateProteinSetEcNumberSourceAndInModel(model_protein_idprotein, ecnumber, inModel, source);
			tx.commit();

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void insertDbLinksEntry(String class_, Integer internalId, String database, String externalId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			dblinksService.insertDbLinksEntry(class_, internalId, database, externalId);
			tx.commit();

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Integer insertNewGene(String locusTag, String name, String query, String origin) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer id = geneservice.insertNewGene(locusTag, name, query, origin);
			tx.commit();
			return id;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<ProteinContainer> getProducts() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ProteinContainer> res = proteinservice.getProducts();
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Map<String, Set<String>> getQueryAndAliasFromProducts() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Set<String>> res = geneservice.getQueryAndAliasFromProducts();
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public ProteinContainer getProteinEcNumberAndInModelByProteinID(Integer proteinID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			ProteinContainer res = proteinservice.getProteinEcNumberAndInModelByProteinID(proteinID);
			tx.commit();
			return res;
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public ProteinContainer getProteinIdByEcNumber(String ecNumber) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			ProteinContainer res = proteinservice.getProteinIdByEcNumber(ecNumber);
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<ReactionContainer> getDistinctReactionByProteinIdAndCompartimentalized(Integer proteinID, boolean isCompartimentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ReactionContainer> res = reactionservice.getDistinctReactionByProteinIdAndCompartimentalized(proteinID, isCompartimentalized);
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<ReactionContainer> getReactionIdFromProteinIdWithPathwayIdNull(Integer proteinId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ReactionContainer> res = reactionservice.getReactionIdFromProteinIdWithPathwayIdNull(proteinId);
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer getIdReactionLabelFromReactionId(Integer reactionId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer res = reactionservice.getIdReactionLabelFromReactionId(reactionId);
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void removeReactionsBySource(String source) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			reactionservice.removeReactionsBySource(source);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public boolean checkReactionsBySource(String source) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean exists = reactionservice.checkReactionsBySource(source);
			tx.commit();
			return exists;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public MetaboliteContainer getCompoundByExternalIdentifier(String identifier) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			MetaboliteContainer compound = compoundservice.getCompoundByExternalIdentifier(identifier);
			tx.commit();
			return compound;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<Object[]> getCompoundIDAndStoichiometricCoefficientAndCompartmentIDByReactionID(Integer reactionID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Object[]> res = reactionservice.getCompoundIDAndStoichiometricCoefficientAndCompartmentIDByReactionID(reactionID);
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<ProteinContainer> getProteinIdByIdgene(Integer idGene) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ProteinContainer> res = proteinservice.getProteinIdByIdgene(idGene);
			tx.commit();
			return res;

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void updateSourceByReactionLabelId(Integer reactionLabelId, String source) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			reactionservice.updateSourceByReactionLabelId(reactionLabelId, source);
			tx.commit();

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void removeSubunitByGeneIdAndProteinId(Integer geneId, Integer protId) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			subunitservice.removeSubunitByGeneIdAndProteinId(geneId, protId);;
			tx.commit();

		}
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}

	}

	public List<String[]> getReactionIdAndEcNumberAndProteinId() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getReactionIdAndEcNumberAndProteinId();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public List<Integer[]> getReactionIdAndPathwayId() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer[]> result = reactionservice.getReactionIdAndPathwayId();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<Integer[]> getStoichiometryDataFromTransportersSource() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer[]> result = stoichservice.getStoichiometryDataFromTransportersSource();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override

	public List<String[]> getReacIdEcNumProtIdWhereSourceEqualTransporters() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getReacIdEcNumProtIdWhereSourceEqualTransporters();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Integer insertEcNumberRank(Integer geneHomology_s_key, String concatEC, Integer ecnumber) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = annotationService.insertEcNumberRank(geneHomology_s_key, concatEC, ecnumber);

			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override

	public Map<String, Set<Integer>> getCompartIdAndEcNumbAndProtId() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, Set<Integer>> result = compartmentservice.getCompartIdAndEcNumbAndProtId();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Boolean FindEnzymesAnnotationEcNumberRankHasOrganismByIds(Integer sKey, Integer orgKey) throws Exception{
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Boolean result = annotationService.FindEnzymesAnnotationEcNumberRankHasOrganismByIds(sKey, orgKey);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<Integer> getDistinctReactionIdWhereSourceTransporters(Boolean isTransporter) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = reactionservice.getDistinctReactionIdWhereSourceTransporters(isTransporter);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void InsertEnzymesAnnotationEcNumberRankHasOrganism(Integer sKey, Integer orgKey) throws Exception{
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.InsertEnzymesAnnotationEcNumberRankHasOrganism(sKey, orgKey);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer getGeneHomologySkey(String query, Integer homologySetupID) throws Exception{
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer res = annotationService.getGeneHomologySkey(query, homologySetupID);
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer insertEnzymesAnnotationOrganism(String organism, String taxonomy, Integer taxrank) throws Exception{
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer res = annotationService.insertEnzymesAnnotationOrganism(organism, taxonomy, taxrank);
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer getHomologuesSkey(String referenceID) throws Exception{
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer res = annotationService.getHomologuesSkey(referenceID);
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void insertEnzymesAnnotationHomologuesHasEcNumber(int homologues_s_key, int ecnumber_s_key) throws Exception{
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.insertEnzymesAnnotationHomologuesHasEcNumber(homologues_s_key, ecnumber_s_key);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Set<String> getAllReactionsNames() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Set<String> res = reactionservice.getAllReactionsNames();
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public String getGeneNameById(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			String res = geneservice.getGeneNameById(id);
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<GeneContainer> getAllGeneData() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<GeneContainer> res = geneservice.getAllGeneData();
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public int getHomologySetupSkeyByAttributes(String databaseID, String program, double eVal, String matrix, short wordSize,
			String gapCosts, int maxNumberOfAlignments, String version) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int res = annotationService.getHomologySetupSkeyByAttributes(databaseID, program, eVal, matrix, wordSize, gapCosts, maxNumberOfAlignments, version);
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<CompartmentContainer> getCompartmentsRelatedToGene(Integer idGene) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<CompartmentContainer> res = geneservice.getCompartmentsRelatedToGene(idGene);
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Integer insertHomologySetup(String databaseID, String program, double eVal, String matrix, short wordSize,
			String gapCosts, int maxNumberOfAlignments, String version) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int res = annotationService.insertHomologySetup(databaseID, program, eVal, matrix, wordSize, gapCosts, maxNumberOfAlignments, version);
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Map<Integer, ReactionContainer> getAllOriginalTransportersFromStoichiometry() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, ReactionContainer> res = stoichservice.getAllOriginalTransportersFromStoichiometry();
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public int getHomologySetupSkeyByAttributes2(String databaseID, String program, double eVal, int maxNumberOfAlignments, String version) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int res = annotationService.getHomologySetupSkeyByAttributes2(databaseID, program, eVal, maxNumberOfAlignments, version);
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer insertNewModelModule(ModuleContainer container) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer res = moduleService.insertNewModelModule(container);
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer insertHomologySetup2(String databaseID, String program, double eVal, int maxNumberOfAlignments, String version) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			int res = annotationService.insertHomologySetup2(databaseID, program, eVal, maxNumberOfAlignments, version);
			tx.commit();
			return res;

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void insertNewModelModuleHasModelOrthology(Integer moduleId, Integer orthologyId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			moduleService.insertNewModelModuleHasModelOrthology(moduleId, orthologyId);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer insertNewOrthologue(String entryId, String locus) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer res = orthologyservice.insertNewOrthologue(entryId, locus);
			tx.commit();
			return res;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}



	@Override
	public void removeGeneHomologyBySKey(Integer skey) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.removeGeneHomologyBySKey(skey);
			tx.commit();

		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public boolean checkModelModuleHasModelOrthology(Integer moduleId, Integer orthologyId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean exists = moduleService.checkModelModuleHasModelOrthology(moduleId, orthologyId);
			tx.commit();
			return exists;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void insertModelGeneHasOrthology(Integer geneId, Integer orthologyId, Double score) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			geneservice.insertModelGeneHasOrthology(geneId, orthologyId, score);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void updateModelGeneEndPositions(Integer geneId, String leftEndPosition, String rightEndPosition) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			geneservice.updateModelGeneEndPositions(geneId, leftEndPosition, rightEndPosition);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void updateProteinSetEcNumber(Integer idProtein, String ecNumber) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			proteinservice.updateProteinSetEcNumber(idProtein, ecNumber);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void updateCompartmentsTool(Long organismId, String tool) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			projectService.updateCompartmentsTool(organismId, tool);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void removeProtein(Integer proteinId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			proteinservice.removeProtein(proteinId);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void removeCompoundByExternalIdentifier(String identifier) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			compoundservice.removeCompoundByExternalIdentifier(identifier);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void insertModelPathwayHasCompound(Integer compoundId, Integer idPathway) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			pathwayservice.insertModelPathwayHasCompound(compoundId, idPathway);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void insertModelEnzymaticCofactor(int compoundID, int protID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			proteinservice.insertModelEnzymaticCofactor(compoundID, protID);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void insertModelPathwayHasModuleEntry(Integer pathId, Integer moduleId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			pathwayservice.insertModelPathwayHasModuleEntry(pathId, moduleId);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public boolean checkModelPathwayHasModuleEntry(Integer pathId, Integer moduleId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean exists = pathwayservice.checkModelPathwayHasModuleEntry(pathId, moduleId);
			tx.commit();
			return exists;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer getCompartmentIdByNameAndAbbreviation(String name, String abb) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer id = compartmentservice.getCompartmentIdByNameAndAbbreviation(name, abb);
			tx.commit();
			return id;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void removeNotOriginalReactions(Boolean isTransporter) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			reactionservice.removeNotOriginalReactions(isTransporter);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<String[]> getCompoundDataFromStoichiometry(Integer idreaction) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = stoichservice.getCompoundDataFromStoichiometry(idreaction);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<ProteinContainer> getEnzymeHasReaction() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ProteinContainer> result = proteinservice.getEnzymeHasReaction();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Map<String, String> getNameAndAbbreviation() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<String, String> res = annotationCompartmentsService.getNameAndAbbreviation();
			tx.commit();
			return res;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<String[]> getReactionGenes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getReactionGenes();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<CompartmentContainer> getBestCompartmenForGene() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<CompartmentContainer> res = annotationCompartmentsService.getBestCompartmenForGene();
			tx.commit();
			return res;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public CompartmentContainer getAnnotationCompartmentByAbbreviation(String abb) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			CompartmentContainer res = annotationCompartmentsService.getAnnotationCompartmentByAbbreviation(abb);
			tx.commit();
			return res;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer insertAnnotationCompartmentNameAndAbbreviation(String name, String abb) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer res = annotationCompartmentsService.insertAnnotationCompartmentNameAndAbbreviation(name, abb);
			tx.commit();
			return res;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public boolean checkCompartmentsReportsHasCompartmentEntry(Integer reportId, Integer compartmentId)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean res = annotationCompartmentsService.checkCompartmentsReportsHasCompartmentEntry(reportId, compartmentId);
			tx.commit();
			return res;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}

	@Override
	public void insertCompartmentsReportsHasCompartmentEntry(Integer reportId, Integer compartmentId, Double score)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationCompartmentsService.insertCompartmentsReportsHasCompartmentEntry(reportId, compartmentId, score);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}

	@Override
	public boolean checkSuperPathwayData(Integer id, Integer superID) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean res = pathwayservice.checkSuperPathwayData(id, superID);
			tx.commit();
			return res;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}

	@Override
	public boolean checkPathwayHasCompoundData(Integer pathId, Integer compId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean res = pathwayservice.checkPathwayHasCompoundData(pathId, compId);
			tx.commit();
			return res;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}

	@Override
	public Set<Integer> getModelDrains()
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Set<Integer> result = reactionservice.getModelDrains();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}
	
	@Override
	public Long countPathwayHasReaction(boolean isCompartimentalized)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Long result = pathwayservice.countPathwayHasReaction(isCompartimentalized);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}


	@Override
	public boolean checkIfReactionsHasData() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = reactionservice.checkIfReactionsHasData();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}

	@Override
	public GeneContainer getModelGeneByQuery(String query) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			GeneContainer result = geneservice.getModelGeneByQuery(query);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}

	@Override
	public void removeGeneHomologyByQuery(String query) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.removeGeneHomologyByQuery(query);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}

	@Override
	public void updateModelStoichiometryByReactionIdAndCompoundId(double coeff, Integer reactionId,
			Integer compoundId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			stoichservice.updateModelStoichiometryByReactionIdAndCompoundId(coeff, reactionId, compoundId);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}


	@Override
	public CompartmentContainer getAnnotationCompartmentByName(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			CompartmentContainer result = annotationCompartmentsService.getAnnotationCompartmentByName(name);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}


	@Override
	public void setAllReactionsInModel(boolean setInModel, boolean keepSpontaneousReactions) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			reactionservice.setAllReactionsInModel(setInModel, keepSpontaneousReactions);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}

	@Override
	public Map<Integer, List<PathwayContainer>> getPathwaysByReaction() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, List<PathwayContainer>> result = pathwayservice.getPathwaysByReaction();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}

	@Override
	public Integer insertNewGene(GeneContainer container) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = geneservice.insertNewGene(container);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}

	@Override
	public void updateGene(GeneContainer gene) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			geneservice.updateGene(gene);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}


	@Override
	public GeneContainer getGeneHomologyEntryByQuery(String query) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			GeneContainer result = annotationService.getGeneHomologyEntryByQuery(query);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}
	
	
	@Override
	public List<InterproResults> getAllInterproResultsByQuery(String query) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<InterproResults> result = interproservice.getAllInterproResultsByQuery(query);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}

	
	@Override
	public Integer insertInterproResults(String query, String querySequence, 
			String mostLikelyEc, String mostLikelyLocalization, 
			String mostLikelyName, String status) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = interproservice.insertInterproResults(query, querySequence, mostLikelyEc,
					mostLikelyLocalization, mostLikelyName, status);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}
	
	@Override
	public List<InterproResult> getAllInterproResultByDatabaseAndAccessionAndResultsId(String database, String accession,
			Integer results) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<InterproResult> result = interproservice.getAllInterproResultByDatabaseAndAccessionAndResultsId(database, accession, results);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}
	
	
	@Override
	public Integer insertInterproResultData(String tool, Float eValue, Float score, String familyName, String accession,
			String name, String ec, String goName, String localization, String database, Integer resultId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = interproservice.insertInterproResultData(tool, eValue, score, familyName, accession, name, ec, goName, localization, database, resultId);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}
	
	@Override
	public List<InterproEntry> getAllInterproEntryByAccession(String accession) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<InterproEntry> result = interproservice.getAllInterproEntryByAccession(accession);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}
	
	
	@Override
	public Integer insertInterproEntryData(String accession, String name, String description, String type) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = interproservice.insertInterproEntryData(accession, name, description, type);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}
	
	
	@Override
	public List<InterproXRef> getAllInterproXRefByExternalIdAndEtryId(String external, Integer entryId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<InterproXRef> result = interproservice.getAllInterproXRefByExternalIdAndEtryId(external, entryId);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}
	
	
	@Override
	public Integer insertInterproXRefData(String category, String database, String name, String external_id, Integer entry_id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = interproservice.insertInterproXRefData(category, database, name, external_id, entry_id);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}



	@Override
	public Integer getModuleIdByReactionAndDefinitionAndEntryId(String entryId, String reaction,
			String definition) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = moduleService.getModuleIdByReactionAndDefinitionAndEntryId(entryId, reaction, definition);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}


	@Override
	public void updateModelReactionSetInModelAndNotesAndBooleanRuleByReactionName(boolean inModel, String notes,
			String booleanRule, String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			reactionservice.updateModelReactionSetInModelAndNotesAndBooleanRuleByReactionName(inModel, notes, booleanRule, name);
			tx.commit();
		} 
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}


	@Override
	public List<String[]> getDataForGPRAssignment() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = reactionservice.getDataForGPRAssignment();
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}

	@Override
	public Integer countInitialMetabolicGenes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = geneservice.countInitialMetabolicGenes();
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<String> getDuplicatedQuerys() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = annotationService.getDuplicatedQuerys();
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void resetAllScorers() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.resetAllScorers();
			tx.commit();
		} 
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}


	@Override
	public void updateScorerConfigSetLatest(boolean latest) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.updateScorerConfigSetLatest(latest);
			tx.commit();
		} 
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void updateScorerConfigSetLatestByBlastDatabase(boolean latest, String blastDatabase) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.updateScorerConfigSetLatestByBlastDatabase(latest, blastDatabase);
			tx.commit();
		} 
		catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Set<Integer> getHomologyGenesSKeyByStatus(HomologyStatus status, String program) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Set<Integer> result = annotationService.getHomologyGenesSKeyByStatus(status, program);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}
	
	@Override
	public List<String> getInterproModelAccessionByAccession(String accession) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String> result = interproservice.getInterproModelAccessionByAccession(accession);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}
	
	
	@Override
	public Integer insertInterproModelData(String accession, String description, String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = interproservice.insertInterproModelData(accession, description, name);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}
	
	@Override
	public List<InterproResultHasEntry> getAllInterproResultHasEntryByResultIdAndEntryID(int resultId, int entryId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<InterproResultHasEntry> result = interproservice.getAllInterproResultHasEntryByResultIdAndEntryID(resultId, entryId);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}
	
	
	@Override
	public Integer insertInterproResultHasEntry(int resultId, int entryId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = interproservice.insertInterproResultHasEntry(resultId, entryId);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}	
	}
	
	

	@Override
	public Set<String> getHomologyGenesQueryByStatus(HomologyStatus status, String program) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Set<String> result = annotationService.getHomologyGenesQueryByStatus(status, program);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Pair<Set<Integer>, Set<String>> getHomologyGenesSKeyAndQueryByAttributes(HomologyStatus status,
			String program, double evalue, String matrix, String wordSize, Integer maxNumberOfAlignments) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Pair<Set<Integer>, Set<String>> result = annotationService.getHomologyGenesSKeyAndQueryByAttributes(status,
					program, evalue, matrix, wordSize, maxNumberOfAlignments);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Pair<Set<Integer>, Set<String>> getHomologyGenesSKeyAndQueryByStatusAndProgramAndDatabaseId(
			HomologyStatus status, String program, String databaseId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Pair<Set<Integer>, Set<String>> result = annotationService.getHomologyGenesSKeyAndQueryByStatusAndProgramAndDatabaseId(status,
					program, databaseId);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<Integer> getModelGenesIDs(boolean encoded) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = geneservice.getModelGenesIDs(encoded);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}
			
	@Override
	public Integer insertHomologyData(Integer geneHomologySKey, String locusTag, String geneName,
			String product, String ecnumber, Boolean selected, String chromossome, String notes) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			 Integer result = annotationService.insertHomologyData(geneHomologySKey, locusTag, geneName, product, ecnumber, selected, chromossome, notes);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Integer getOrthologyIdByEntryIdAndLocus(String entryId, String locusId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = orthologyservice.getOrthologyIdByEntryIdAndLocus(entryId, locusId);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}
	
	@Override
	public Integer getOrthologyIdByEntryIdWherwLocusIdIsNullOrEmpty(String entryId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = orthologyservice.getOrthologyIdByEntryIdWherwLocusIdIsNullOrEmpty(entryId);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void updateOrthologyLocusIdByEntryId(String entryId, String locusId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			orthologyservice.updateOrthologyLocusIdByEntryId(entryId, locusId);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<Integer, String> getModuleIdAndNoteByGeneIdAndProteinId(Integer geneId, Integer protId)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, String> result = moduleService.getModuleIdAndNoteByGeneIdAndProteinId(geneId, protId);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}
	
	@Override
	public Set<Integer> getSKeyForAutomaticAnnotation(String blastDatabase) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			  Set<Integer> result = annotationService.getSKeyForAutomaticAnnotation(blastDatabase);
			  tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void insertModelSubunit(Integer geneId, Integer protId, String note, String gprStatus)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			subunitservice.insertModelSubunit(geneId, protId, note, gprStatus);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void deleteFromHomologyDataByDatabaseID(String database) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			annotationService.deleteFromHomologyDataByDatabaseID(database);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Set<String> getAllBlastDatabases() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Set<String> result = annotationService.getAllBlastDatabases();
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public CompartmentContainer findCompartmentByAbbreviation(String abbreviation) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			CompartmentContainer result = compartmentservice.findCompartmentByAbbreviation(abbreviation);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public CompartmentContainer findCompartmentById(Integer id) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			CompartmentContainer result = compartmentservice.findCompartmentById(id);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<Integer> getProteinsByReactionId(Integer reactionId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<Integer> result = reactionservice.getProteinsByReactionId(reactionId);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<CompartmentContainer> getProteinCompartmentsByProteinId(Integer proteinId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<CompartmentContainer> result = proteinservice.getProteinCompartmentsByProteinId(proteinId);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public CompartmentContainer getReactionCompartment(Integer reactionId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			CompartmentContainer result = reactionservice.getReactionCompartment(reactionId);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public boolean checkModelSubunitEntry(Integer geneId, Integer protId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = subunitservice.checkModelSubunitEntry(geneId, protId);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<ReactionContainer> getGenesReactionsBySubunit() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ReactionContainer> result = reactionservice.getGenesReactionsBySubunit();
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public List<ReactionContainer> getModelReactionIdsRelatedToName(String name) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<ReactionContainer> result = reactionservice.getModelReactionIdsRelatedToName(name);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer countSequencesByType(SequenceType type) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = sequenceservice.countSequencesByType(type);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Integer countTotalOfReactions(boolean isCompartmentalized) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = reactionservice.countTotalOfReactions(isCompartmentalized);
			tx.commit();
			return result;			
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Map<Integer, GeneContainer> getAllGeneDatabyIds() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Map<Integer, GeneContainer> result = geneservice.getAllGeneDatabyIds();
			tx.commit();
			return result;	
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Integer insertNewProteinEntry(ProteinContainer protein) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = proteinservice.insertNewProteinEntry(protein);
			tx.commit();		
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}
	
	@Override
	public void deleteCompartment(Integer compartmentId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			compartmentservice.deleteCompartment(compartmentId);
			tx.commit();	
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}
	
	public List<Pair<Integer, String>> getReactionHasEnzyme2(boolean isCompartimentalised) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			 List<Pair<Integer, String>> result = reactionservice.getReactionHasEnzyme2(isCompartimentalised);
			tx.commit();		
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void replaceCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			compartmentservice.replaceCompartment(compartmentIdToKeep, compartmentIdToReplace);
			tx.commit();		
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public Map<Integer, List<CompartmentContainer>> getReportsByGene() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			 Map<Integer, List<CompartmentContainer>> result = annotationCompartmentsService.getReportsByGene();
			tx.commit();		
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Map<Integer, List<CompartmentContainer>> getCompartmentsRelatedToGenes() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			 Map<Integer, List<CompartmentContainer>> result = this.geneservice.getCompartmentsRelatedToGenes();
			tx.commit();		
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void removeAllFromModelGeneHasCompartment() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			this.geneservice.removeAllFromModelGeneHasCompartment();
			tx.commit();		
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void replaceReactionCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			reactionservice.replaceReactionCompartment(compartmentIdToKeep, compartmentIdToReplace);
			tx.commit();	
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	public void removeCompartmentsNotInUse() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			this.compartmentservice.removeCompartmentsNotInUse();
			tx.commit();		
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void replaceStoichiometryCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace)
			throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			stoichservice.replaceStoichiometryCompartment(compartmentIdToKeep, compartmentIdToReplace);
			tx.commit();		
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}
			
	public void updateProjectsByGenomeID(Long genomeID, Map<String, String> documentSummaryMap) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			this.projectService.updateProjectsByGenomeID(genomeID, documentSummaryMap);
			tx.commit();		
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
		
	}


	@Override
	public void insertModelModuleHasModelProtein(Integer proteinId, Integer moduleId) throws Exception {
		
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			this.moduleService.insertModelModuleHasModelProtein(proteinId, moduleId);
			tx.commit();		
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
		
	}


	@Override
	public boolean checkModelModuletHasProteinData(Integer proteinId, Integer moduleId) throws Exception {
		
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = moduleService.checkModelModuletHasProteinData(proteinId, moduleId);
			tx.commit();		
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
		
	}
	
	
	@Override
	public Integer countCompartmentsAnnotationReports() throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = this.annotationCompartmentsService.countCompartmentsAnnotationReports();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
		
	}
	
	@Override
	public List<String[]> getHomologyProductsByGeneHomologySkey(Integer skey) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			List<String[]> result = this.homologyservice.getHomologyProductsByGeneHomologySkey(skey);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
		
	}
	
	@Override
	public Integer getAnnotationHomologySkeyBySequenceId(int sequenceId) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = this.homologyservice.getAnnotationHomologySkeyBySequenceId(sequenceId);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
		
	}
	
	@Override
	public Integer insertTranscriptionUnitName(String gene) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Integer result = this.transcriptionservice.insertTranscriptionUnitName(gene);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
		
	}
	
	@Override
	public boolean checkTranscriptionUnitNameExists(String name) throws Exception {

		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = this.transcriptionservice.checkTranscriptionUnitNameExists(name);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}

	@Override
	public boolean deleteGenesWithoutECRankAndProdRank() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = this.annotationService.deleteGenesWithoutECRankAndProdRank();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public Long countSubunitEntries() throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Long result = this.subunitservice.countSubunitEntries();
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public boolean isProteinEncodedByGenes(Integer proteinId) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = this.subunitservice.isProteinEncodedByGenes(proteinId);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public boolean removeSelectedReactionLabel(Integer reactionLabelId) throws Exception {
		
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			boolean result = this.reactionservice.removeSelectedReactionLabel(reactionLabelId);
			tx.commit();
			return result;
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
	}


	@Override
	public void deleteEmptyPathways(boolean checkReactions, boolean checkProteins) throws Exception {
		Transaction tx = null;
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			pathwayservice.deleteEmptyPathways(checkReactions, checkProteins);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw new Exception(e);
		}
		
	}

}