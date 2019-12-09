/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.gui.operations.modelTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.reversibilitySource;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.reversibilityTemplate;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelStoichiometryServices;
import pt.uminho.ceb.biosystems.merlin.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

/**
 * @author ODias
 *
 */
@Operation(name="correct reactions reversibility", description="correct reactions reversibility")
public class CorrectReversibility {

	protected static final String GITHUB_MODELSEED_URL = "https://raw.githubusercontent.com/ModelSEED/ModelSEEDDatabase/dev/";
	protected static final String GITHUB_MODELSEED_PREFIX = "Unique_ModelSEED_";
	private WorkspaceAIB project;
	private boolean forceCorrect;
	private reversibilitySource source;
	private reversibilityTemplate template;

	@Port(name="source",description="reversibility source", direction = Direction.INPUT, order=1)
	public void setSource(reversibilitySource source){

		this.source = source;
	}

	@Port(name="template",description="organism template", direction = Direction.INPUT, order=2)
	public void setSource(reversibilityTemplate template){

		this.template = template;
	}

	@Port(name="force corrections",description="force reversibility corrections", advanced = true, validateMethod="checkForceCorrect", direction = Direction.INPUT, order=3)
	public void setForceCorrect(boolean forceCorrect){

		this.forceCorrect = forceCorrect;
	}

	@Port(name="select workspace",description="select workspace", validateMethod="checkProject", direction = Direction.INPUT, order=4)
	public void setProject(WorkspaceAIB project) throws Exception{

		double infLowerBound = -999999;
		double zeroLowerBound = 0;

		try {

			if(source.equals(reversibilitySource.Zeng)) {

				File file = new File(FileUtils.getUtilitiesFolderPath() + "irr_reactions.txt");

				BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

				String text;
				while ((text = bufferedReader.readLine()) != null) {

					String[] data = text.split("\t");


					Map<Integer, String> reactions = ModelReactionsServices.getLabelsByExternalIdentifiers(this.project.getName(), data[0]);

					for (Integer id : reactions.keySet()) {

						String equation = reactions.get(id).replace("<=>", "=>");

						boolean rever = !Utilities.get_boolean_int_to_boolean(data[1]);

						Long lowerBound = (long) zeroLowerBound;
						equation = reactions.get(id).replace(" <=> ", " => ");

						if(rever) {

							equation = reactions.get(id).replace(" => ", " <=> ");
							lowerBound = (long) infLowerBound;

						}
						
						ModelReactionsServices.updateModelReactionReversibleAndLowerBoundAndEquationByReactionId(rever, lowerBound, Integer.valueOf(id), equation, this.project.getName());

//						query = "UPDATE reaction SET reversible = "+rever+", lowerBound = "+lowerBound+", equation= '"+equation+"' WHERE idreaction ="+id;
//
//						ProjectAPI.executeQuery(query, stmt);
					}
				}
				bufferedReader.close();
			}
			else {

				TsvParserSettings settings = new TsvParserSettings();
				TsvParser parser = new TsvParser(settings);

				String path = GITHUB_MODELSEED_URL+"Biochemistry/Aliases/"+GITHUB_MODELSEED_PREFIX+"Reaction_Aliases.txt";

				URL fileAliases = new URL(path);
				InputStream inAliases = fileAliases.openStream();
				List<String[]> rowsAliases = parser.parseAll(inAliases);

				Map<String,String> idConversion = new HashMap<String,String>();

				for(String[] line : rowsAliases) {

					if(line[2].contains("KEGG")) {
						if(!idConversion.containsKey(line[0]))
							idConversion.put(line[0],line[1].substring(0, 6));
						//						if(!idConversion.containsKey(line[1]))
						//							idConversion.put(line[1],line[2].substring(0, 6));
					}
				}

				inAliases.close();

				TsvParserSettings settings2 = new TsvParserSettings();
				settings2.setMaxCharsPerColumn(10000);
				TsvParser parser2 = new TsvParser(settings2);

				URL fileReactions = new URL(GITHUB_MODELSEED_URL+"Biochemistry/reactions.tsv");
				InputStream inReactions = fileReactions.openStream();
				List<String[]> rowsReactions = parser2.parseAll(inReactions);

				URL fileTemplate = null;
				if(template.equals(reversibilityTemplate.GramNegative))
					fileTemplate = new URL(GITHUB_MODELSEED_URL+"Templates/GramNegative/Reactions.tsv");
				if(template.equals(reversibilityTemplate.GramPositive))
					fileTemplate = new URL(GITHUB_MODELSEED_URL+"Templates/GramPositive/Reactions.tsv");
				if(template.equals(reversibilityTemplate.Microbial))
					fileTemplate = new URL(GITHUB_MODELSEED_URL+"Templates/Microbial/Reactions.tsv");
				if(template.equals(reversibilityTemplate.Mycobacteria))
					fileTemplate = new URL(GITHUB_MODELSEED_URL+"Templates/Mycobacteria/Reactions.tsv");
				if(template.equals(reversibilityTemplate.Plant))
					fileTemplate = new URL(GITHUB_MODELSEED_URL+"Templates/Plant/Reactions.tsv");
				if(template.equals(reversibilityTemplate.Fungi))
					fileTemplate = new URL(GITHUB_MODELSEED_URL+"Templates/Fungi/Reactions.tsv");
				if(template.equals(reversibilityTemplate.Human))
					fileTemplate = new URL(GITHUB_MODELSEED_URL+"Templates/Human/Reactions.tsv");

				InputStream inTemplate = fileTemplate.openStream();
				List<String[]> rowsTemplate = parser.parseAll(inTemplate);


				for(String[] line : rowsTemplate) {

					String keggID = idConversion.get(line[0]);
					if(keggID != null) {

	
						Map<Integer, String> reactions = ModelReactionsServices.getLabelsByExternalIdentifiers(this.project.getName(), keggID);					

						for (Integer id : reactions.keySet()) {
							String equation = reactions.get(id);

							boolean rever = false;
							if(line[2].equals("="))
								rever = true;

							if(line[2].equals("<")) {

								equation = CorrectReversibility.reverseEquation(keggID, equation, rowsReactions);
								ModelStoichiometryServices.swapReactantsAndProducts(this.project.getName(), id);
							}

							Long lowerBound = (long) zeroLowerBound;

							equation = equation.replace(" <=> ", " => ");

							if(rever) {

								equation = equation.replace(" => ", " <=> ");
								lowerBound = (long) infLowerBound;
							}
							
							ModelReactionsServices.updateModelReactionReversibleAndLowerBoundAndEquationByReactionId(rever, lowerBound, Integer.valueOf(id), equation, this.project.getName());

//							query = "UPDATE reaction SET reversible = "+rever+", lowerBound = "+lowerBound+", equation= '"+equation+"' " +//, notes='RRC' " +
//									" WHERE idreaction ="+id;

							//ProjectAPI.executeQuery(query, stmt);
						}
					}
				}
			}


			MerlinUtils.updateReactionsView(this.project.getName());
			Workbench.getInstance().info("Reactions reversibility corrected!");
			

		} catch (Exception e) {

			e.printStackTrace();
			throw new IllegalArgumentException(e);
		} 
		
	}

	public static String reverseEquation(String keggID, String equation, List<String[]> rowsReactions){

		String newEquation = equation;

		for(String[] line : rowsReactions) {

			if(line.length>1 && line[1]!=null && line[1].equals(keggID)) {

				String[] equationSplit = equation.split(" <=> | => | <= ");
				String[] reactants = equationSplit[0].split(" \\+ ");
				String[] msEqSplit = line[7].split(" <=> | => | <= ");

				for(int i = 0 ; i<reactants.length;i++) {

					if(msEqSplit[0].contains(reactants[i])) {

						newEquation = equationSplit[1] + " => " + equationSplit[0];
						i = reactants.length;
					}
				}
			}
		}

		return newEquation;
	}

	/**
	 * @param project
	 */
	public void checkForceCorrect(boolean forceCorrect) {

		this.forceCorrect = forceCorrect;
	}

	/**
	 * @param project
	 */
	public void checkProject(WorkspaceAIB project) {

		try {
			if(project == null) {

				throw new IllegalArgumentException("No ProjectGUISelected!");
			}
			else {

				this.project = project;

//				boolean exists = ModelReactionsServices.checkReactionNotLikeSourceAndNotReversible(this.project.getName(), "TRANSPORTERS");

//				if(exists && !this.forceCorrect)
//					Workbench.getInstance().info("Model already has irriversible reactions!\nPlease check force reversibility corrections to continue.");

				if(!ProjectServices.isMetabolicDataAvailable(project.getName()))			
					throw new IllegalArgumentException("Please load metabolic data!");


			}
		} 
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
	}
}
