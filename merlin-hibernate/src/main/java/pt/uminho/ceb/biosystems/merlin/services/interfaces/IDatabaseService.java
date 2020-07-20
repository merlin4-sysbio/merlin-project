package pt.uminho.ceb.biosystems.merlin.services.interfaces;

public interface IDatabaseService extends IAnnotationCompartmentsService, IAnnotationEnzymesService, IGenesService, IPathwayService, ICompartmentService,
IReactionService, ISubunitService, IHomologyService, IOrthologyService, ICompoundService, IStoichiometryService, IProteinService,
IAliasesService, ITranscriptionUnitService, IRegulatoryEventService, I_InterproService, IDblinksService, ISequenceService, IDesnormalizerService, IModuleService, IProjectService
{

	public void closeJDBCConnection();

}
