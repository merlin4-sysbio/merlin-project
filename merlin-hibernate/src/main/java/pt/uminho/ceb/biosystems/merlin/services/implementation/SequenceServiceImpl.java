package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.SequenceContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSequenceDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSequence;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.ISequenceService;

public class SequenceServiceImpl implements ISequenceService{


	private IModelSequenceDAO sequenceDAO;
	private IModelGeneDAO modelGeneDAO;

	public SequenceServiceImpl(IModelSequenceDAO sequenceDAO, IModelGeneDAO modelGeneDAO) { 
		this.sequenceDAO = sequenceDAO;
		this.modelGeneDAO = modelGeneDAO;
	}

	@Override
	public void insertNewSequence(Integer idGene, SequenceType type, String sequence, Integer sequenceLength) {

		ModelSequence modelSequence = new ModelSequence();

		ModelGene modelGene = this.modelGeneDAO.getModelGene(idGene);
		
		modelSequence.setModelGene(modelGene);
		modelSequence.setSequenceType(type.toString());
		modelSequence.setSequenceLength(sequenceLength);
		modelSequence.setSequence(sequence);
		
		this.sequenceDAO.addModelSequence(modelSequence);
		
	}

	@Override
	public boolean checkGeneIDFromSequence(int geneID, SequenceType seqtype) throws Exception {
		boolean exists = false;

		List<Integer> list = this.sequenceDAO.getModelSequenceGeneIdByGeneIdAndSequenceType(geneID, seqtype);

		if (!list.isEmpty()) {
			exists = true;
		}
		return exists;
	}


	@Override
	public List<ModelSequence> getAllModelSequence() throws Exception {

		return this.sequenceDAO.getAllModelSequence();
	}

	@Override
	public Map<String, SequenceContainer> getSequencesWithQueriesBySequenceType(SequenceType seqtype) throws Exception {
		Map<String, SequenceContainer> sequenceWithQuery = new HashMap<>();
		List<ModelSequence> sequences = this.sequenceDAO.getModelSequencesBySequenceType(seqtype);
		
		
		if(sequences != null) {
			
			for(ModelSequence sequence :sequences) {
				
				
				SequenceContainer container = new SequenceContainer(sequence.getIdsequence());
				
				container.setSeqType(seqtype);
				
				if(sequence.getModelGene() != null)
					container.setQuery(sequence.getModelGene().getQuery());
				
				container.setSequence(sequence.getSequence());
				container.setSequenceLength(sequence.getSequenceLength());
				
				
				ModelGene a = sequence.getModelGene();
				if (a != null && a.getQuery() != null)
					sequenceWithQuery.put(a.getQuery(), container);
			}
		}
		
		return sequenceWithQuery;
	}

	@Override
	public void loadFastaSequences(Map<Integer, String[]> sequences,
			SequenceType seqType) throws Exception {

		for (Integer geneID : sequences.keySet()) {

			String[] seqInfo = sequences.get(geneID);

			ModelSequence sequence = new ModelSequence();

			if(!seqType.equals(SequenceType.RNA) || !seqType.equals(SequenceType.RRNA) || !seqType.equals(SequenceType.TRNA)) {

				ModelGene modelGene = this.modelGeneDAO.getModelGene(geneID);

				if(modelGene != null)
					sequence.setModelGene(modelGene);
			}

			sequence.setSequenceType(seqType.toString());
			sequence.setSequence(seqInfo[0]);
			sequence.setSequenceLength(Integer.parseInt(seqInfo[1]));
			
			this.sequenceDAO.addModelSequence(sequence);

		}
	}
	
	@Override 
	public List<GeneContainer> getSequenceByGeneId(int idGene) {
		
		return this.sequenceDAO.getSequenceByGeneId(idGene);
	}
	
	@Override 
	public boolean checkGenomeSequencesByType(SequenceType type) {
		
		return this.sequenceDAO.checkGenomeSequencesByType(type);
	}
	
	@Override 
	public Integer countSequencesByType(SequenceType type) {
		
		return this.sequenceDAO.countSequencesByType(type);
	}
	
	@Override
	public List<ModelSequence> getAllEntityModelSequences() throws JAXBException{
		ModelSequence res = this.sequenceDAO.getAllModelSequence().get(0);
		JAXBContext jaxbcontext = JAXBContext.newInstance(ModelSequence.class);
		Marshaller jaxbMarshaller = jaxbcontext.createMarshaller();
		jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
		jaxbMarshaller.marshal( res, System.out );
		return null;
	}
}


