package pt.uminho.ceb.biosystems.merlin.processes.annotation;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.AnnotationEnzymes;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesHomologyScorer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesRowInfo;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.PairValueComparator;

/**
 * @author ODias
 *
 */
public class AnnotationEnzymesProcesses {

	final static Logger logger = LoggerFactory.getLogger(AnnotationEnzymesProcesses.class);

	/**
	 * @param result
	 * @return
	 */
	public static String[][] getStats(List<Integer> result) {

		String[][] res = new String[result.size()][];

		int r = 0;

		res[r] = new String[] {"Number of Genes", ""+result.get(r++)};
		res[r] = new String[] {"Number of Genes without similarities", ""+result.get(r++)};
		res[r] = new String[] {"Number of Genes with unavailable locus tag", ""+result.get(r++)};
		res[r] = new String[] {"Number of Genes with unavailable query", ""+result.get(r++)};
		res[r] = new String[] {"Number of Genes with unavailable gene name", ""+result.get(r++)};
		res[r] = new String[] {"Number of homologue genes", ""+result.get(r++)};
		res[r] = new String[] {"Average homologues per gene", ""+result.get(r++)};
		res[r] = new String[] {"Number of organisms with at least one homologue gene", ""+result.get(r++)};
		res[r] = new String[] {"\t Eukaryota:\t", ""+result.get(r++)};
		res[r] = new String[] {"\t Bacteria:\t", ""+result.get(r++)};
		res[r] = new String[] {"\t Archaea:\t", ""+result.get(r++)};
		res[r] = new String[] {"\t Viruses:\t", ""+result.get(r++)};
		res[r] = new String[] {"\t other sequences:\t", ""+result.get(r++)};
		return res;
	}

	/**
	 * @param enzymesAnnotation
	 * @param database
	 * @return
	 */
	public static WorkspaceGenericDataTable getMainTableData(AnnotationEnzymes enzymesAnnotation, String database) {

		boolean thresholdBool = false;

		enzymesAnnotation.setTableRowIndex(new HashMap<>());
		enzymesAnnotation.setKeys(new HashMap<>());

		enzymesAnnotation.setInitialLocus(new HashMap<Integer, String>());
		enzymesAnnotation.setInitialNames(new HashMap<Integer, String>());

		enzymesAnnotation.setInitialProdItem(new HashMap<Integer,String>());
		enzymesAnnotation.setInitialEcItem(new HashMap<Integer,String>());

		enzymesAnnotation.getWorkspace().setInitialiseHomologyData(false);

		ArrayList<String> columnsNames = new ArrayList<String>();
		columnsNames.add("info");
		columnsNames.add("gene");
		columnsNames.add("status");
		columnsNames.add("name");
		columnsNames.add("product");
		columnsNames.add("score");
		columnsNames.add("EC number(s)");
		columnsNames.add("score");
		columnsNames.add("notes");

		WorkspaceGenericDataTable qrt = new WorkspaceGenericDataTable(columnsNames, "EC numbers", "") {

			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) {

				if (col<4 //|| (hasChromosome && col == 4) 
						|| this.getColumnClass(col).equals(Boolean.class) 
						|| this.getColumnClass(col).equals(String[].class) || this.getColumnName(col).equals("notes"))  {
					return true;
				}
				else return false;
			}
		};

		//EC |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
		enzymesAnnotation = AnnotationEnzymesProcesses.getECRanksPair(enzymesAnnotation);

		//PROD |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
		enzymesAnnotation = AnnotationEnzymesProcesses.getProductRanksPair(enzymesAnnotation);
		//   |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		Set<Integer> specificDatabase = null;

		Map<Integer, List<Object>> data;

		data = new HashMap<>();

		if(!database.isEmpty()) {

			specificDatabase = enzymesAnnotation.getBlastGeneDatabase().get(database);

			int position  = 0;

			for(Integer index : enzymesAnnotation.getGeneData().keySet()) {

				if(specificDatabase.contains(enzymesAnnotation.getIdentifiers().get(index))) {
					data.put(position, enzymesAnnotation.getGeneData().get(index));

					enzymesAnnotation.getKeys().put(position, enzymesAnnotation.getIdentifiers().get(index));//genes list
					enzymesAnnotation.getTableRowIndex().put(enzymesAnnotation.getIdentifiers().get(index), position);

					enzymesAnnotation.getInitialLocus().put(position, enzymesAnnotation.getInitialOriginalLocus().get(index));
					enzymesAnnotation.getInitialNames().put(position, enzymesAnnotation.getInitialOriginalNames().get(index));

					//					enzymesAnnotation.getReverseKeys().put(Integer.parseInt(enzymesAnnotation.getOriginalKeys().get(index)), position);//reverse genes list

					position++;
				}
			}
		}
		else {

			for(Integer index : enzymesAnnotation.getGeneData().keySet()) {

				data.put(index, enzymesAnnotation.getGeneData().get(index));

				enzymesAnnotation.getKeys().put(index, enzymesAnnotation.getIdentifiers().get(index));//genes list
				enzymesAnnotation.getTableRowIndex().put(enzymesAnnotation.getIdentifiers().get(index), index);
				enzymesAnnotation.getInitialLocus().put(index, enzymesAnnotation.getInitialOriginalLocus().get(index));
				enzymesAnnotation.getInitialNames().put(index, enzymesAnnotation.getInitialOriginalNames().get(index));
				//enzymesAnnotation.getReverseKeys().put(enzymesAnnotation.getIdentifiers().get(index), index);//reverse genes list
			}
		}

		int size = AnnotationEnzymesProcesses.getArraySize(enzymesAnnotation.getKeys().values());

		enzymesAnnotation.setEnzyme(new String[(size)+1][]);
		enzymesAnnotation.setEcnPercent(new String[(size)+1][]);
		enzymesAnnotation.setProdPercent(new String[(size)+1][]);
		enzymesAnnotation.setProduct(new String[(size)+1][]);

		for(Integer index  : data.keySet()) {

			List<Object> dataList = new ArrayList<>(data.get(index));

			dataList = AnnotationEnzymesProcesses.processProductNamesData(index, dataList, enzymesAnnotation);
			dataList = AnnotationEnzymesProcesses.processECNumberData(index, dataList,  thresholdBool, enzymesAnnotation);
			data.put(index, dataList);
		}

		for(Integer key : enzymesAnnotation.getKeys().keySet()) {

			if(data.containsKey(key))
				qrt.addLine(data.get(key), enzymesAnnotation.getKeys().get(key));
		}

		return qrt;
	}


	/**
	 * @param enzymesAnnotationRowInfo
	 * @return
	 */
	public static WorkspaceDataTable[] getRowInfo(AnnotationEnzymesRowInfo enzymesAnnotationRowInfo) {

		int t = 0;
		String[] datatableNames = new String[4];
		datatableNames[t++] = "homology data";
		datatableNames[t++] = "taxonomy";
		if(enzymesAnnotationRowInfo.isInterproAvailable())
			datatableNames[t++] = "InterPro";
		datatableNames[t++] = "FASTA sequence";
		datatableNames[t++] = "setup parameters";

		WorkspaceDataTable[] res = new WorkspaceDataTable[datatableNames.length];

		List<String> columnsNames = new ArrayList<String>();

		int datatableCounter = 0;

		/////////////////////////////////////////////////////////////////////////////////////////////////
		columnsNames.add("reference ID");
		columnsNames.add("locus ID");
		columnsNames.add("status");
		columnsNames.add("organism");
		columnsNames.add("e-Value");
		columnsNames.add("score (bits)");
		columnsNames.add("identity");
		columnsNames.add("positives");
		columnsNames.add("query coverage");
		columnsNames.add("target coverage"); 
		columnsNames.add("product");
		columnsNames.add("EC number");
		res[datatableCounter] = new WorkspaceDataTable(columnsNames, datatableNames[datatableCounter]);

		for(List<String> lists : enzymesAnnotationRowInfo.getHomologyResults())
			res[datatableCounter].addLine(lists);

		datatableCounter++;

		///////////////////////////////////////////////////////////////////////////////////////////////////
		columnsNames = new ArrayList<String>();
		columnsNames.add("organism");
		columnsNames.add("phylogenetic tree");
		res[datatableCounter] = new WorkspaceDataTable(columnsNames, datatableNames[datatableCounter]);

		for(List<String> lists : enzymesAnnotationRowInfo.getTaxonomyResults())
			res[datatableCounter].addLine(lists);

		datatableCounter++;

		/////////////////////////////////////////////////////////////////////////////////////////////////

		if(enzymesAnnotationRowInfo.isInterproAvailable()) {

			columnsNames = new ArrayList<String>();
			columnsNames.add("database");
			columnsNames.add("domain accession");
			columnsNames.add("name");
			columnsNames.add("e-Value");
			columnsNames.add("EC Number");
			columnsNames.add("product");
			columnsNames.add("GO name");
			columnsNames.add("location");
			columnsNames.add("entry accession");
			columnsNames.add("entry name");
			columnsNames.add("entry description");
			columnsNames.add("start");
			columnsNames.add("end");
			res[datatableCounter] = new WorkspaceDataTable(columnsNames, datatableNames[datatableCounter]);

			for(List<String> lists : enzymesAnnotationRowInfo.getInterProResults())					
				res[datatableCounter].addLine(lists);

			datatableCounter++;
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////

		columnsNames = new ArrayList<String>();
		columnsNames.add("sequence");
		res[datatableCounter] = new WorkspaceDataTable(columnsNames, datatableNames[datatableCounter]);

		ArrayList<String> ql = new ArrayList<String>();
		ql.add(setSequenceView(enzymesAnnotationRowInfo.getSequence()));
		res[datatableCounter].addLine(ql);

		datatableCounter++;

		/////////////////////////////////////////////////////////////////////////////////////////////////

		columnsNames = new ArrayList<String>();
		columnsNames.add("program");
		columnsNames.add("version");
		columnsNames.add("databaseID");
		columnsNames.add("e-value");
		columnsNames.add("lower identity");
		columnsNames.add("upper identity");
		columnsNames.add("positives");
		columnsNames.add("query coverage");
		columnsNames.add("target coverage");
		columnsNames.add("matrix");
		columnsNames.add("wordSize");
		columnsNames.add("gapCosts");
		columnsNames.add("max number Of alignments");
		res[datatableCounter] = new WorkspaceDataTable(columnsNames, datatableNames[datatableCounter]);

		for(List<String> lists : enzymesAnnotationRowInfo.getHomologySearchSetup())
			res[datatableCounter].addLine(lists);

		return res;
	}

	/**
	 * @param title
	 * @param frequencyScore
	 * @param taxonomyScore
	 * @param finalScore
	 * @param data
	 * @return
	 */
	public static WorkspaceDataTable getSelectionPane(String title, Map<Integer, String> frequencyScore, Map<Integer, String> taxonomyScore, Map<Integer, String> finalScore, List<String[]> data) {

		ArrayList<String> columnsNames = new ArrayList<String>();
		DecimalFormatSymbols separator = new DecimalFormatSymbols();
		separator.setDecimalSeparator('.');
		DecimalFormat format = new DecimalFormat("##.##",separator);

		columnsNames.add("products");
		columnsNames.add("frequency (%)");
		columnsNames.add("occurrences");
		columnsNames.add("frequency score");
		columnsNames.add("taxonomy score");
		columnsNames.add("final score");
		columnsNames.add("program");

		WorkspaceDataTable dataTable = new WorkspaceDataTable(columnsNames, title);

		if(data != null) {
			double total=0;

			for(int i=0; i<data.size(); i++){
				String[] list = data.get(i);
				total+=Integer.parseInt(list[2]);
			}

			for(int i=0; i<data.size(); i++){
				String[] list = data.get(i);

				ArrayList<String> ql = new ArrayList<String>();
				ql.add(list[1]);
				ql.add(format.format(Integer.parseInt(list[2])/total*100)+" %");

				ql.add(list[2]);
				ql.add(frequencyScore.get(Integer.parseInt(list[0])));
				ql.add(taxonomyScore.get(Integer.parseInt(list[0])));
				ql.add(finalScore.get(Integer.parseInt(list[0])));
				ql.add(list[3]);
				dataTable.addLine(ql);
			}
		}
		return dataTable;
	}

	/**
	 * @param selectedItem
	 * @param row
	 * @return
	 */
	public static String getECPercentage(String selectedItem, int row, AnnotationEnzymes enzymesAnnotation) {

		for(int i = 0; i < enzymesAnnotation.getEnzyme()[enzymesAnnotation.getKeys().get(row)].length; i++) {

			if(enzymesAnnotation.getEnzyme()[enzymesAnnotation.getKeys().get(row)][i].trim().equals(selectedItem.trim()))
				return enzymesAnnotation.getEcnPercent()[enzymesAnnotation.getKeys().get(row)][i];
		}
		return "manual";
	}


	/**
	 * @param selectedItem
	 * @param row
	 * @return
	 */
	public static String getProductPercentage(String selectedItem, int row, AnnotationEnzymes enzymesAnnotation) {

		for(int i = 0; i < enzymesAnnotation.getProduct()[(enzymesAnnotation.getKeys().get(row))].length; i++) {

			if(enzymesAnnotation.getProduct()[(enzymesAnnotation.getKeys().get(row))][i].trim().equals(selectedItem.trim())) {

				if(enzymesAnnotation.getProdPercent()[(enzymesAnnotation.getKeys().get(row))][i]=="0")
					return "manual";
				else
					return enzymesAnnotation.getProdPercent()[(enzymesAnnotation.getKeys().get(row))][i];
			}
		}
		return "manual";
	}


	/**
	 * @param enzymesAnnotation
	 * @return
	 */
	public static AnnotationEnzymes getECRanksPair(AnnotationEnzymes enzymesAnnotation) {

		Map<Integer, String> ecName = new HashMap<>();
		Map<Integer, List<Integer>> ecHomologueSKeysToRankSKeys = new HashMap<>();

		try {

			Map<Integer, Double> ecNumberRank = new HashMap<>();

			List<String[]> dataFromECNumber = enzymesAnnotation.getDataFromEcNumber();

			if(dataFromECNumber!=null) {

				for (int i = 0; i<dataFromECNumber.size(); i++){	

					String[] list = dataFromECNumber.get(i);

					int homologueSKey = Integer.parseInt(list[1]);
					int homologueECRankSKey = Integer.parseInt(list[0]);
					String name = list[2];

					//number of eckeys for each gene
					Set<Integer> rankSKeys = new HashSet<>();

					if(ecHomologueSKeysToRankSKeys.containsKey(homologueSKey))					
						rankSKeys = new HashSet<>(ecHomologueSKeysToRankSKeys.get(homologueSKey));

					rankSKeys.add(homologueECRankSKey);

					ecHomologueSKeysToRankSKeys.put(homologueSKey,new ArrayList<>(rankSKeys));

					double productEcScore = Double.parseDouble(list[3]);
					String formatedProductEcScore = enzymesAnnotation.getFormat().format(productEcScore);

					ecNumberRank.put(homologueECRankSKey, Double.parseDouble(formatedProductEcScore));
					ecName.put(homologueECRankSKey, name);
				}
			}

			Map<Integer,List<Integer>> orgRank = new HashMap<>();

			if(enzymesAnnotation.getEcRank()!=null) {

				for (int i = 0; i<enzymesAnnotation.getEcRank().size(); i++){

					String[] list = enzymesAnnotation.getEcRank().get(i);

					int homologueECRankSKey = Integer.parseInt(list[0]);

					// organism rank for each ecnumber
					List<Integer> orgTax = new ArrayList<Integer>();
					if(orgRank.containsKey(homologueECRankSKey))
						orgTax = orgRank.get(homologueECRankSKey);

					orgTax.add(Integer.parseInt(list[1]));
					orgRank.put(homologueECRankSKey, orgTax);
				}
			}

			int maxRank=0;

			if(enzymesAnnotation.getMaxTaxRank()!=null && !enzymesAnnotation.getMaxTaxRank().equals(""))
				maxRank=Integer.parseInt(enzymesAnnotation.getMaxTaxRank());

			if(orgRank!=null) {

				for(int homologueECRankSKey : orgRank.keySet()) {

					double frequency = ecNumberRank.get(homologueECRankSKey)/enzymesAnnotation.getHomologuesCountEcNumber().get(homologueECRankSKey);

					if(frequency>1)
						frequency=1;

					AnnotationEnzymesHomologyScorer scorer = new AnnotationEnzymesHomologyScorer(frequency, orgRank.get(homologueECRankSKey), maxRank, enzymesAnnotation.getAlpha(), enzymesAnnotation.getBeta(), enzymesAnnotation.getMinimumNumberofHits());

					enzymesAnnotation.getEnzymeFrequencyScore().put(homologueECRankSKey,enzymesAnnotation.getFormat().format(scorer.getScore1()));
					enzymesAnnotation.getEnzymeTaxonomyScore().put(homologueECRankSKey, enzymesAnnotation.getFormat().format(scorer.getScore2()));
					enzymesAnnotation.getEnzymeFinalScore().put(homologueECRankSKey, enzymesAnnotation.getFormat().format(scorer.getScore()));
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}

		enzymesAnnotation.setEcName(ecName);
		enzymesAnnotation.setEcKeys(ecHomologueSKeysToRankSKeys);

		return enzymesAnnotation;
	}



	/**
	 * @param stmt
	 * @param prodRank
	 * @param prodName
	 * @param format
	 * @return
	 */
	public static AnnotationEnzymes getProductRanksPair(AnnotationEnzymes enzymesAnnotation) {

		Map<Integer,String> prodName = new HashMap<>();
		Map<Integer,List<Integer>> prodKeys = new HashMap<>();

		try {

			Map<Integer, Double> productRank = new HashMap<>();

			if(enzymesAnnotation.getDataFromProduct()!=null) {

				for (int i = 0; i<enzymesAnnotation.getDataFromProduct().size(); i++) {

					String[] list = enzymesAnnotation.getDataFromProduct().get(i);

					//number of productKeys for each gene

					Set<Integer> s_key = new HashSet<>();
					if(prodKeys.containsKey(Integer.parseInt(list[1])))
						s_key = new HashSet<>(prodKeys.get(Integer.parseInt(list[1])));

					s_key.add(Integer.parseInt(list[0]));
					prodKeys.put(Integer.parseInt(list[1]),new ArrayList<>(s_key));

					double productRankScore = Double.parseDouble(list[3]);
					String formatedProductRankScore = enzymesAnnotation.getFormat().format(productRankScore);

					productRank.put(Integer.parseInt(list[0]), Double.parseDouble(formatedProductRankScore));
					prodName.put(Integer.parseInt(list[0]), list[2]);
				}
			}

			Map<Integer,List<Integer>> orgRank = new HashMap<>();

			if(enzymesAnnotation.getTaxRank()!=null) {

				for (int i = 0; i<enzymesAnnotation.getTaxRank().size(); i++){	
					String[] list = enzymesAnnotation.getTaxRank().get(i);

					// organism rank for each product
					List<Integer> orgTax = new ArrayList<Integer>();

					if(orgRank.containsKey(Integer.parseInt(list[0])))
						orgTax = orgRank.get(Integer.parseInt(list[0]));

					orgTax.add(Integer.parseInt(list[1]));
					orgRank.put(Integer.parseInt(list[0]), orgTax);
				}
			}

			int maxRank=0;

			String enzymesAnnotationRank =null;

			if(enzymesAnnotation.getMaxTaxRank() !=null && !enzymesAnnotation.getMaxTaxRank().equals("")){
				if((enzymesAnnotationRank = enzymesAnnotation.getMaxTaxRank())!=null){
					maxRank=Integer.parseInt(enzymesAnnotationRank);
				}
			}

			if(orgRank!=null) {

				for(int key : orgRank.keySet()) {

					if(productRank.containsKey(key) && enzymesAnnotation.getHomologuesCountProduct().containsKey(key)) {

						double frequency = productRank.get(key)/enzymesAnnotation.getHomologuesCountProduct().get(key);
						if(frequency>1)
							frequency=1;

						AnnotationEnzymesHomologyScorer scorer = new AnnotationEnzymesHomologyScorer(frequency, orgRank.get(key), maxRank, enzymesAnnotation.getAlpha(), enzymesAnnotation.getBeta(), enzymesAnnotation.getMinimumNumberofHits());

						Map<Integer, String> s1 = enzymesAnnotation.getProductFrequencyScore();
						if(s1==null)
							s1 = new HashMap<>();
						s1.put(key,enzymesAnnotation.getFormat().format(scorer.getScore1()));
						enzymesAnnotation.setProductFrequencyScore(s1);

						Map<Integer, String> s2 = enzymesAnnotation.getProductTaxonomyScore();
						if(s2==null)
							s2 = new HashMap<>();
						s2.put(key, enzymesAnnotation.getFormat().format(scorer.getScore2()));
						enzymesAnnotation.setProductTaxonomyScore(s2);

						Map<Integer, String> s = enzymesAnnotation.getProductFinalScore();
						if(s==null)
							s = new HashMap<>();
						s.put(key, enzymesAnnotation.getFormat().format(scorer.getScore()));

						enzymesAnnotation.setProductFinalScore(s);
					}
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}

		enzymesAnnotation.setProdName(prodName);
		enzymesAnnotation.setProdKeys(prodKeys);

		return enzymesAnnotation;
	}


	/**
	 * @param dataSet
	 * @return
	 */
	private static int getArraySize(Collection <Integer> dataSet){

		int max = 0;

		for(int key: dataSet)
			if(key>max)
				max=key;

		return max+1;
	}


	/**
	 * @param sequence
	 * @return
	 */
	private static String setSequenceView(String sequence){
		String seq = new String();
		for(int i=0;i<sequence.toCharArray().length;i++)
		{
			if(i!=0 && (i%70)==0){seq+="\n";}
			seq+=sequence.charAt(i);			
		}
		return seq;
	}



	/**
	 * @param index
	 * @param dataList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> processProductNamesData(int index, List<Object> dataList, AnnotationEnzymes enzymesAnnotation){

		Map<Integer,String> prodName = enzymesAnnotation.getProdName();
		Map<Integer,List<Integer>> prodKeys = enzymesAnnotation.getProdKeys();

		String[][] prod = new String[2][0];
		Pair<String,Double> products[] = null;

		int key = enzymesAnnotation.getKeys().get(index);

		enzymesAnnotation.getProduct()[key]= new String[0];
		enzymesAnnotation.getProdPercent()[key] = new String[0];

		if(prodKeys.containsKey(key)) {

			List<Integer> keyList = prodKeys.get(key);

			products = new Pair[keyList.size()];
			int j=0;

			while(j<keyList.size()) {

				int listKey = keyList.get(j);
				String prodRankScore = enzymesAnnotation.getProductFinalScore().get(listKey);

				Double score = 0.0;
				if(prodRankScore==null)
					logger.error("no score for prodRank s_key {}",listKey);
				else				
					score = Double.parseDouble(prodRankScore);

				products[j]=new Pair<String, Double>(prodName.get(listKey), score);
				j++;
			}

			Arrays.sort(products, new PairValueComparator<Double>());

			prod = new String[2][keyList.size()+1];

			enzymesAnnotation.getProduct()[key] = new String[keyList.size()+1];

			enzymesAnnotation.getProdPercent()[key] = new String[keyList.size()+1];

			enzymesAnnotation.getProduct()[key][0]="";

			enzymesAnnotation.getProdPercent()[key][0]="";

			prod[0][0]="";
			prod[1][0]="";
			j = 0;

			while (j < products.length) {

				Double score = products[j].getB();
				prod[0][j+1] = products[j].getA();
				enzymesAnnotation.getProduct()[key][j+1] = products[j].getA();

				if(score>=0) {

					prod[1][j+1]= enzymesAnnotation.getFormat().format(score);
				}
				else {

					prod[1][j+1]="manual";
					enzymesAnnotation.getProduct()[key][j+1]=prod[0][j+1];
				}
				enzymesAnnotation.getProdPercent()[key][j+1] = prod[1][j+1];
				j++;
			}

			if(prod[0].length>0) {

				dataList.add(prod[0]);
			}
			else {

				dataList.add(new String[0]);
			}

			if(prod[1].length>0) {

				dataList.add(prod[1][prod[1].length-1]);
			}
			else {

				dataList.add(prod[1][0]);
			}

			enzymesAnnotation.getInitialProdItem().put(index, prod[0][prod[0].length-1]);
		}
		else
		{
			dataList.add(new String[0]);
			dataList.add("");
		}

		return dataList;
	}

	/**
	 * @param index
	 * @param dataList
	 * @param thresholdBool
	 * @param enzymesAnnotation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> processECNumberData(int index, List<Object> dataList, boolean thresholdBool, AnnotationEnzymes enzymesAnnotation){

		Map<Integer,String> ecName = enzymesAnnotation.getEcName();
		Map<Integer,List<Integer>> ecKeys = enzymesAnnotation.getEcKeys();

		String[][] ecn = new String[2][0];
		Pair<String,Double> ecnumber[] = null;

		int key = enzymesAnnotation.getKeys().get(index);

		enzymesAnnotation.getEnzyme()[key] = new String[0];

		enzymesAnnotation.getEcnPercent()[key] = new String[0];

		if (ecKeys.containsKey(key)) {

			List<Integer> keyList = ecKeys.get(key);

			ecnumber = new Pair[keyList.size()];
			int j=0;
			while(j<keyList.size()) {

				int listKey = keyList.get(j);
				String ecRankScore = enzymesAnnotation.getEnzymeFinalScore().get(listKey);

				Double score = 0.0;
				if(ecRankScore ==null)
					logger.error("no score for ecRank s_key {}",listKey);
				else	
					score = Double.parseDouble(ecRankScore);					

				ecnumber[j]=new Pair<String, Double>(ecName.get(listKey), score);
				j++;
			}

			Arrays.sort(ecnumber, new PairValueComparator<Double>()); 

			ecn = new String[2][keyList.size()+1];

			enzymesAnnotation.getEnzyme()[key]= new String[keyList.size()+1];

			enzymesAnnotation.getEcnPercent()[key]=	new String[keyList.size()+1];

			enzymesAnnotation.getEnzyme()[key][0]="";

			enzymesAnnotation.getEcnPercent()[key][0]="";

			ecn[0][0]="";
			ecn[1][0]="";
			j = 0;
			while (j < ecnumber.length) {

				Double score = ecnumber[j].getB();
				ecn[0][j+1] = ecnumber[j].getA();
				enzymesAnnotation.getEnzyme()[key][j+1]=ecnumber[j].getA();

				if(score>=0)
					ecn[1][j+1]= enzymesAnnotation.getFormat().format(score);
				else
					ecn[1][j+1]=" ";

				enzymesAnnotation.getEcnPercent()[key][j+1] = ecn[1][j+1];
				j++;
			}

			String out = "";
			if(ecn[1].length>0 && ecnumber[ecnumber.length-1].getB()>=enzymesAnnotation.getThreshold()) {

				out =  ecn[0][ecnumber.length];
			}

			enzymesAnnotation.getInitialEcItem().put(index, out);
		}



		dataList.add(ecn[0]);
		String ec_score = "";
		String note = "";

		if(ecn[1].length>0 && ecnumber[ecnumber.length-1].getB()>=enzymesAnnotation.getThreshold()) {

			ec_score = ecn[1][ecnumber.length];
		}
		else {

			if(ecn[1].length>0) {

				ec_score = "<"+enzymesAnnotation.getThreshold();
			}
		}

		dataList.add(ec_score);
		dataList.add(note);

		return dataList;
	}


	/**
	 * Retrieve all genes between lower and upper threshold.
	 * 
	 * @param lowerThreshold
	 * @param upperThreshold
	 * @param usedManual
	 * @param enzymesAnnotation
	 * @return
	 */
	public static Set<String> getGenesInThreshold(double lowerThreshold, double upperThreshold, boolean usedManual, AnnotationEnzymes enzymesAnnotation)  {

		Set<String> genes = new HashSet<>();

		Map<Integer, String> mappedEcItem = enzymesAnnotation.getInitialEcItem();

		if(enzymesAnnotation.getCommittedEcItem()!= null)
			for(int row : enzymesAnnotation.getCommittedEcItem().keySet())
				if(enzymesAnnotation.getCommittedEcItem().get(row)!=null && !enzymesAnnotation.getCommittedEcItem().get(row).equalsIgnoreCase("null"))
					mappedEcItem.put(row, enzymesAnnotation.getCommittedEcItem().get(row));


		for(int row : enzymesAnnotation.getInitialProdItem().keySet()) {

			int key = enzymesAnnotation.getKeys().get(row);

			//mappedEcItem.put(row,enzymesAnnotation.getEnzymesList().get(key));
			String selectedItem = mappedEcItem.get(row);
			//String selectedItem = enzymesAnnotation.getIntegrationEcItem().get(key);

			String ecWeigth = "";

			if(enzymesAnnotation.getIntegrationEcItem().containsKey(row) || enzymesAnnotation.getEnzymesList().containsKey(row))
				ecWeigth = AnnotationEnzymesProcesses.getECPercentage(selectedItem, row, enzymesAnnotation);

			if(ecWeigth.equalsIgnoreCase("manual")) {

				if(usedManual)
					genes.add(enzymesAnnotation.getQueries().get(key));
			}
			else if(ecWeigth.isEmpty()) {

				if(lowerThreshold == 0)					
					genes.add(enzymesAnnotation.getQueries().get(key));
			}
			else {

				double score = Double.parseDouble(ecWeigth);

				if( score < upperThreshold && score > lowerThreshold)
					genes.add(enzymesAnnotation.getQueries().get(key));
			}
		}

		return genes;
	}


	/**
	 * Get rows for genes with InterPro entries.
	 * 
	 * @param interProGenes
	 * @param locusKeys
	 * @param enzymesAnnotation
	 * @return
	 */
	public static List<Integer> getInterProRows(List<String> interProGenes, Map<String, Integer> locusKeys, AnnotationEnzymes enzymesAnnotation) {

		List<Integer> ret = new ArrayList<>();

		for(String locus : locusKeys.keySet())
			if(interProGenes.contains(locus))
				ret.add(enzymesAnnotation.getTableRowIndex().get(locusKeys.get(locus)));

		return ret;
	}


	/**
	 * Method to upate the user commited settings for a specific database
	 */
	public static void updateSettings(boolean restore, AnnotationEnzymes enzymesAnnotation) {

		if (restore) {

			enzymesAnnotation.setThreshold(AnnotationEnzymes.THRESHOLD);
			enzymesAnnotation.setUpperThreshold(AnnotationEnzymes.UPPER_THRESHOLD);
			enzymesAnnotation.setBeta(AnnotationEnzymes.BETA);
			enzymesAnnotation.setAlpha(AnnotationEnzymes.ALPHA);
			enzymesAnnotation.setMinimumNumberofHits(AnnotationEnzymes.MINIMUM_NUMBER_OF_HITS);
		}
		else {

			enzymesAnnotation.setThreshold(enzymesAnnotation.getCommittedThreshold());
			enzymesAnnotation.setUpperThreshold(enzymesAnnotation.getCommittedUpperThreshold());
			enzymesAnnotation.setBeta(enzymesAnnotation.getCommittedBeta());
			enzymesAnnotation.setAlpha(enzymesAnnotation.getCommittedAlpha());
			enzymesAnnotation.setMinimumNumberofHits(enzymesAnnotation.getCommittedMinHomologies());

		}

	}
}
