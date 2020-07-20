package pt.uminho.ceb.biosystems.merlin.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.AbstractSyntaxTreeNode;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.DataTypeEnum;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.IValue;

/**
 * @author odias
 *
 */
public class RulesParser {

	
	/**
	 * @param genes
	 * @param concatenate
	 * @return
	 */
	public static String processReactionGenes(Set<Pair<String,String>> genes, boolean concatenate) {

		String geneData = "";
		if(genes!=null) {

			for(Pair<String,String> gene:genes){
				if(gene!=null && gene.getA() != null){
					
					geneData=geneData.concat(gene.getA());
					
					if(concatenate && gene.getB()!=null && !gene.getB().isEmpty())
						geneData=geneData.concat("_").concat(gene.getB());
					
					geneData=geneData.concat(" or ");
				}
			}	
//			geneData=geneData.substring(0, geneData.lastIndexOf(" or "));
			geneData = geneData.replaceAll("\\s+or\\s+$", "");
		}
		
		return geneData;
	}
	
	/**
	 * Get gene rule from raw data.
	 * 
	 * @param rawData
	 * @param genes
	 * @return
	 */
	public static String getGeneRule(String rawData, Map<Integer, Pair<String, String>> genes) {
		
		return RulesParser.getRules2String(RulesParser.parseGeneRules(rawData, genes));
	}

	/**
	 * Parse gene rules from database;
	 * 
	 * @param rawData
	 * @param genes
	 * @return
	 */
	public static List<List<Pair<String, String>>> parseGeneRules(String rawData, Map<Integer, Pair<String, String>> genes) {

		List<List<Pair<String, String>>> res = new ArrayList<>();
		
		rawData = rawData.toUpperCase();

		String [] rules = rawData.split(" OR ");
		
		for(String rule : rules) {
		
			String [] ids = rule.split(" AND ");
			
			List<Pair<String, String>> pairList= new ArrayList<>();
			
			for(String idString : ids) {
				
				pairList.add(genes.get(Integer.parseInt(idString.replaceAll("[\\(\\)]", "").trim())));
			}
			res.add(pairList);
		}
		
		return res;
	}
	
	/**
	 * reverse parse gene rules from database;
	 * 
	 * @param rawData
	 * @param genes
	 * @return
	 */
	public static String reverseParseGeneRules(String processedData, Map<String, Integer> genes) {
		
		List<List<Pair<String, String>>> res = new ArrayList<>();

		String [] rules = processedData.split(" OR ");
		
		for(String rule : rules) {
		
			String [] ids = rule.split(" AND ");
			
			List<Pair<String, String>> pairList= new ArrayList<>();
			
			for(String idString : ids) {
				
				int data = genes.get(idString.trim());
				
				pairList.add(new Pair<String, String>(String.valueOf(data),""));
			}
			res.add(pairList);
		}
		
		return RulesParser.getRules2String(res);
	}

	/**
	 * Get list of OR rules to list
	 * 
	 * @param geneRules
	 * @return
	 */
	public static List<String> getAND_geneRulesList2List(List<List<Pair<String, String>>> geneRules) {

		List<String> resultsList = new ArrayList<>();
		
		for(List<Pair<String, String>> geneRule : geneRules) {

			String rule = null;
			for(Pair<String, String> gene : geneRule) {

				if(rule == null)
					rule = "";
				else
					rule = rule.concat(" AND ");

				rule = rule.concat(gene.getA());

				if(gene.getB() != null && !gene.getB().trim().isEmpty())
					rule = rule.concat(" (").concat(gene.getB()).concat(")");

			}
			resultsList.add(rule);					
		}
		return resultsList;
	}

	/**
	 * * Get list of AND gene rules 2 String
	 * 
	 * @param genesRule
	 * @return
	 */
	public static String getOR_geneRulesList2String(List<String> genesRule) {

		String rule = null;

		for(String genes : genesRule) {

			if(rule == null)
				rule = "";
			else
				rule = rule.concat(" OR ");

			rule = rule.concat(genes);
		}
		return rule;
	}
	
	/**
	 * Get geneRules 2 String
	 * 
	 * @param geneRules
	 * @return
	 */
	public static String getRules2String(List<List<Pair<String, String>>> geneRules) {
		
		return RulesParser.getOR_geneRulesList2String(RulesParser.getAND_geneRulesList2List(geneRules));
	}
	
	/**
	 * retrieves gene rules combinations
	 * 
	 * @param node
	 * @param list
	 * @return
	 */
	public static List<String> getGeneRuleCombinations(AbstractSyntaxTreeNode<DataTypeEnum, IValue> node){
		
		List<String> res = new ArrayList<>();
		
		if(node.isLeaf()) {
			
			res.add(node.toString());
			
			return res;
		}
		else{
			
			List<String> left = getGeneRuleCombinations(node.getChildAt(0));
			List<String> right = getGeneRuleCombinations(node.getChildAt(1));
			
			if(isAndNode(node)){
				res = combineGenes(left, right);
			}
			else {
				res.addAll(left);
				res.addAll(right);
			}
			
			return res;
		}
	}
	
	
	/**
	 * @param geneRulesCombination
	 * @return
	 */
	public static String getGeneRuleString(List<String>geneRulesCombination,  Map<String, Integer> geneIds){
		
		if(geneIds!=null && !geneIds.isEmpty()){
			for(int i=0; i<geneRulesCombination.size(); i++){
				
				String[] genes = geneRulesCombination.get(i).split(" AND ");
				
				for(String gene : genes)
					if(geneIds.containsKey(gene.trim()))
						geneRulesCombination.get(i).replaceAll(gene.trim(), Integer.toString(geneIds.get(gene.trim())));
			}
		}
		
		String geneRuleString = getOR_geneRulesList2String(geneRulesCombination);
		
		return geneRuleString;
	}
	
	/**
	 * Verify is a rule node is AND
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isAndNode (AbstractSyntaxTreeNode<DataTypeEnum, IValue> node){
		
		String nodeRule = node.toString();
		
		String left = node.getChildAt(0).toString();
		String right = node.getChildAt(1).toString();
		nodeRule = node.toString();
		nodeRule = nodeRule.replace(left, "");
		nodeRule = nodeRule.replace(right, "");
		
		if(nodeRule.matches("\\(\\s{0,3}and\\s{0,3}\\)"))
			return true;
		
//		if(node.matches("\\(.{0,20}\\s(and)\\s.+\\)") || node.matches("\\(.+\\s(and)\\s.{0,20}\\)"))
//			return true;
//			
//		else if(node.matches(".+\\)\\s{1,2}and\\s{1,2}\\(.+")){
//			
//			String[] branches = node.split("\\)\\s{1,2}and\\s{1,2}\\(");
//			
//			if(Math.abs(StringUtils.countMatches(branches[0],"(") - StringUtils.countMatches(branches[1],"("))==2)
//				return true;
//		}
		
		return false;
	}
	

	/**
	 * retrive the combinations between one element of each list
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static List<String> combineGenes(List<String> left, List<String> right){
		
		List<String> combinations = new ArrayList<>();
		
		for(String leftGene : left){
			for(String rightGene : right){
				
				if(!leftGene.equalsIgnoreCase(rightGene))
					combinations.add(leftGene.concat(" AND ").concat(rightGene));
			}
		}
		
		return combinations;
	}

}
