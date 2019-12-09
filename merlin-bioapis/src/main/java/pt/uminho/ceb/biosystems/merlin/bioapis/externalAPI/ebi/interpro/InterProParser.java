package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.interpro;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.Entry;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.Xref;

/**
 * @author João Sequeira
 *
 */
public class InterProParser {

	final static Logger logger = LoggerFactory.getLogger(InterProParser.class);


	/**
	 * @return
	 * @throws IOException
	 */
	public static Map<String,String> getEc2Go() throws IOException {

		URL url = new URL("http://www.geneontology.org/external2go/ec2go");
		Scanner s = new Scanner(url.openStream());
		HashMap<String,String>ec2go = new HashMap<String,String>();
		while (s.next().substring(0,1).equals("!")){s.next();}
		while (s.hasNextLine()) {
			String line = s.nextLine();
			List<String> entry = Arrays.asList(line.split(" "));
			if (!(entry.get(0).equals("!"))){
				ec2go.put(entry.get(entry.size()-1),entry.get(0));
			}
		}
		s.close();

		return ec2go;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public static Map<String,String> getSl2Go() throws IOException {

		URL url = new URL("ftp://ftp.ebi.ac.uk/pub/databases/GO/goa/external2go/uniprotkb_sl2go");
		Scanner s = new Scanner(url.openStream());
		HashMap<String,String>sl2go = new HashMap<String,String>();
		Pattern pattern = Pattern.compile("UniProtKB-SubCell:SL-[0-9]* (.+) >");
		while (s.next().substring(0,1).equals("!")){s.next();}
		while (s.hasNextLine()) {
			String line = s.nextLine();
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()){
				List<String> entry = Arrays.asList(line.split(" "));
				sl2go.put(entry.get(entry.size()-1),matcher.group(1));
			}
		}
		s.close();		
		return sl2go;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public static Map<String,String> getInterPro2Go() throws IOException {

		URL url = new URL("http://www.geneontology.org/external2go/interpro2go");
		Scanner s = new Scanner(url.openStream());
		HashMap<String,String>interpro2go = new HashMap<String,String>();
		Pattern pattern = Pattern.compile("InterPro:IPR[0-9]* (.+) >");
		while (s.next().substring(0,1).equals("!")){s.next();}
		while (s.hasNextLine()) {
			String line = s.nextLine();
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()){
				List<String> entry = Arrays.asList(line.split(" "));
				interpro2go.put(entry.get(entry.size()-1),matcher.group(1));
			}
		}
		s.close();

		return interpro2go;
	}

	/**
	 * Parse gff list to InterPro result list.
	 * 
	 * @param gffList
	 * @return
	 * @throws IOException
	 */
	public static List<HashMap<String, String>> getGffInformation(List<String> gffList) throws IOException {

		List<HashMap<String, String>> domains = new ArrayList<HashMap<String, String>>();

		Pattern GOPattern = Pattern.compile("Ontology_term=(.+?);");
		Pattern IDPattern = Pattern.compile("ID=match(.+)_[0-9]+_[0-9]+;");
		Pattern SDPattern = Pattern.compile("signature_desc=(.+?);");
		Pattern NamePattern = Pattern.compile("Name=(.+?);");
		Pattern StatusPattern = Pattern.compile("status=(.+?);");
		Pattern xrefPattern = Pattern.compile("Dbxref=(.+)");

		for (int i = 0; i < gffList.size(); i++) {

			if (gffList.get(i).startsWith("EMBOSS_001\t")) {

				if (!(gffList.get(i).startsWith("EMBOSS_001\t."))) {

					String[] parts = gffList.get(i).split("\t");
					HashMap<String,String> domain = new HashMap<String,String>();

					domain.put("Source", parts[1]);      			//database from where the result is
					domain.put("Type", parts[2]);					//type of match
					domain.put("Start_position", parts[3]);
					domain.put("End_position", parts[4]);
					domain.put("E-value", parts[5]);
					domain.put("Strand", parts[6]);
					domain.put("Phase", parts[7]);					//phase indicates where the feature begins with reference to the reading frame

					Matcher GOMatcher = GOPattern.matcher(parts[8]);
					if (GOMatcher.find()){domain.put("Ontology_term", GOMatcher.group(1));}

					Matcher IDMatcher = IDPattern.matcher(parts[8]);
					if(IDMatcher.find()){domain.put("Match_ID", IDMatcher.group(1));}

					Matcher SDMatcher = SDPattern.matcher(parts[8]);
					if(SDMatcher.find()){domain.put("Description", SDMatcher.group(1));}

					Matcher NameMatcher = NamePattern.matcher(parts[8]);
					if(NameMatcher.find()){domain.put("Name", NameMatcher.group(1));}

					Matcher StatusMatcher = StatusPattern.matcher(parts[8]);
					if(StatusMatcher.find()){domain.put("Status", StatusMatcher.group(1));}

					Matcher xrefMatcher = xrefPattern.matcher(parts[8]);
					if(xrefMatcher.find()){domain.put("Dbxref", xrefMatcher.group(1));}

					domains.add(domain);

				}

			}

			if (gffList.get(i).startsWith(">match")) {

				Pattern SeqPattern = Pattern.compile(">match(.+?)_[0-9]+_[0-9]+");
				Matcher SeqMatcher = SeqPattern.matcher(gffList.get(i));
				SeqMatcher.matches();

				String sequence	= "";

				i++;

				while (!(gffList.get(i).startsWith(">match")) && i < gffList.size()-1) {

					sequence += gffList.get(i);		

					i ++;

				}

				for (HashMap<String,String>domain:domains){

					if(domain.get("Match_ID").equals(SeqMatcher.group(1))){

						domain.put("Sequence",sequence);
					}
				}

				i--;
			}
		}

		return domains;	
	}

	/**
	 * Parse xml list to InterPro result list.
	 * Additional maps are sought.
	 * 
	 * @param xmlList
	 * @return
	 * @throws IOException
	 */
	public static InterProResultsList getXmlInformation(List<String> xmlList) throws IOException {

		for (String line:xmlList) 
			logger.trace(line);

		Map<String,String> ec2go = getEc2Go();
		Map<String,String> sl2go = getSl2Go();
		Map<String,String> interpro2go = getInterPro2Go();

		return InterProParser.getXmlInformation(xmlList, ec2go, sl2go, interpro2go);
	}

	/**
	 * Parse xml list to InterPro result list.
	 * 
	 * 
	 * @param xmlList
	 * @param ec2go
	 * @param sl2go
	 * @param interpro2go
	 * @return
	 * @throws IOException
	 */
	public static InterProResultsList getXmlInformation(List<String> xmlList, Map<String,String> ec2go, Map<String,String> sl2go, Map<String,String> interpro2go) throws IOException {

		for (String line:xmlList) 
			logger.trace(line);

		InterProResultsList interProResultsList = new InterProResultsList();

		Pattern matchPattern = Pattern.compile("<([^/]+)-match");
		Pattern descPattern = Pattern.compile("desc=\"(.+?)\"");
		Pattern typePattern = Pattern.compile("type=\"(.+?)\"");
		Pattern evaluePattern = Pattern.compile("evalue=\"(.+?)\"");
		Pattern scorePattern = Pattern.compile("score=\"(.+?)\"");
		Pattern familyNamePattern = Pattern.compile("familyName=\"(.+?)\"");
		Pattern acPattern = Pattern.compile("ac=\"(.+?)\"");
		Pattern namePattern = Pattern.compile("name=\"(.+?)\"");
		Pattern universalPattern = Pattern.compile("\"(.+?)\"");
		Pattern libraryPattern = Pattern.compile("library=\"(.+?)\"");
		Pattern versionPattern = Pattern.compile("version=\"(.+?)\"");
		Pattern xrefPattern = Pattern.compile("<(.+)-xref");
		//		Pattern mdSequencePattern = Pattern.compile("<sequence md5=\"(.+?)\">(.+?)<");
		//		
		//		Matcher mdSequenceMatcher = mdSequencePattern.matcher(file.get(3));
		//		System.out.println(file.get(3));
		//		System.out.println(mdSequenceMatcher.group(1));
		//		interProResultsList.setMd5(mdSequenceMatcher.group(1));
		//		interProResultsList.setQuerySequence(mdSequenceMatcher.group(2));
		//		System.out.println(interProResultsList.getMd5());
		//		System.out.println(interProResultsList.getQuerySequence());

		for (int i = 6; i < xmlList.size(); i++) {

			Matcher matchMatcher = matchPattern.matcher(xmlList.get(i));

			if (matchMatcher.find()) {

				InterProResult result = new InterProResult();

				result.setTool(matchMatcher.group(1));

				Matcher evalueMatcher = evaluePattern.matcher(xmlList.get(i));
				if (evalueMatcher.find()){result.seteValue(Double.parseDouble(evalueMatcher.group(1)));}

				Matcher scoreMatcher = scorePattern.matcher(xmlList.get(i));
				if (scoreMatcher.find()){result.setScore(Double.parseDouble(scoreMatcher.group(1)));}

				Matcher familyNameMatcher = familyNamePattern.matcher(xmlList.get(i));
				if (familyNameMatcher.find()){result.setFamily(familyNameMatcher.group(1));}

				i++;

				Matcher acMatcher = acPattern.matcher(xmlList.get(i));
				if (acMatcher.find()){result.setAccession(acMatcher.group(1));}

				Matcher nameMatcher = namePattern.matcher(xmlList.get(i));
				if (nameMatcher.find()){result.setName(nameMatcher.group(1));}

				i++;

				if (xmlList.get(i).trim().startsWith("<entry")) {

					Entry entry = new Entry();

					Matcher entryAcMatcher = acPattern.matcher(xmlList.get(i));
					if (entryAcMatcher.find()) {entry.setAccession(entryAcMatcher.group(1));}

					Matcher descMatcher = descPattern.matcher(xmlList.get(i));
					if (descMatcher.find()) {entry.setDescription(descMatcher.group(1));}

					Matcher entryNameMatcher = namePattern.matcher(xmlList.get(i));
					if (entryNameMatcher.find()) {entry.setName(entryNameMatcher.group(1));}

					Matcher typeMatcher = typePattern.matcher(xmlList.get(i));
					if (typeMatcher.find()) {entry.setType(typeMatcher.group(1));}

					result.setEntry(entry);

					i++;

					//entries with Xrefs end in </entry>, but those that lack xRefs end at the start of the models
					while(!(xmlList.get(i).trim().startsWith("</entry>") || xmlList.get(i).trim().startsWith("<models>")) && i < xmlList.size()) {
						Matcher specificXrefMatcher = xrefPattern.matcher(xmlList.get(i)); 
						Matcher xrefMatcher = universalPattern.matcher(xmlList.get(i));

						Xref xref = new Xref();
						List<String> t = new ArrayList<String>();

						while (xrefMatcher.find()){

							t.add(xrefMatcher.group().replace("\"", ""));
						}

						if (specificXrefMatcher.find()){
							if (specificXrefMatcher.group(1).equals("go")){
								xref.setCategory(t.get(0));
								xref.setDatabase(t.get(1));
								xref.setId(t.get(2));
								xref.setName(t.get(3));
							} else if (specificXrefMatcher.group(1).equals("pathway")){
								xref.setDatabase(t.get(0));
								xref.setId(t.get(1));
								xref.setName(t.get(2));
							} else {

								System.out.println("new kind of xref");
							}
						}

						entry.addXref(xref);
						i++;
					}

					if (entry.getXrefs().size() > 0)						
						i++;
				}

				if (xmlList.get(i).trim().startsWith("<models>")) {

					i++;

					while(!(xmlList.get(i).trim().startsWith("</models>")) && i < xmlList.size()) {
						Matcher modelMatcher = universalPattern.matcher(xmlList.get(i));

						List<String> t = new ArrayList<String>();
						Model model = new Model();

						while (modelMatcher.find()) {

							t.add(modelMatcher.group().replace("\"", ""));

						}

						if (t.size() < 2) {

							model.setAccession(t.get(0));
						}
						else if (t.size() < 3) {

							model.setAccession(t.get(0));
							model.setName(t.get(1));
						} else {

							model.setAccession(t.get(0));
							model.setDescription(t.get(1));
							model.setName(t.get(2));
						}

						result.addModel(model);
						i++;
					}

					i++;
				}

				Matcher libraryMatcher = libraryPattern.matcher(xmlList.get(i));
				if (libraryMatcher.find()){result.setDatabase(libraryMatcher.group(1));}

				Matcher versionMatcher = versionPattern.matcher(xmlList.get(i));
				if (versionMatcher.find()){result.setDatabaseVersion(versionMatcher.group(1));}

				i += 3;

				while (!(xmlList.get(i).trim().startsWith("</locations>"))){

					Matcher locationMatcher = universalPattern.matcher(xmlList.get(i));

					Location location = new Location();
					List<Object> t = new ArrayList<Object>();

					while (locationMatcher.find()) {

						String data = locationMatcher.group().replace("\"", "");

						if (!(data.equals("NONE"))&&!(data.equals("STRONG")))
							t.add(data);
					}

					if (t.size() == 2) {

						try {

							location.setStart(Integer.parseInt((String) t.get(0)));
							location.setEnd(Integer.parseInt((String) t.get(1)));
						} 
						catch (NumberFormatException e) {
							
							System.out.println("Wrong number: two arguments");
						}
					} 
					else if (t.size() == 3) {

						
						try {

							location.setScore(Float.parseFloat((String) t.get(0)));
							location.setStart(Integer.parseInt((String) t.get(1)));
							location.setEnd(Integer.parseInt((String) t.get(2)));
						} 
						catch (NumberFormatException e) {
							
							System.out.println("Wrong number: three arguments ");
							//System.out.println(xmlList);
						}

					} 
					else if(t.size() == 7) {

						location.setScore(Float.parseFloat((String) t.get(0)));
						location.setEvalue(Float.parseFloat((String) t.get(1)));
						location.setHmmstart(Integer.parseInt((String) t.get(2)));
						location.setHmmend(Integer.parseInt((String) t.get(3)));
						location.setHmmlength(Integer.parseInt((String) t.get(4)));
						location.setStart(Integer.parseInt((String) t.get(5)));
						location.setEnd(Integer.parseInt((String) t.get(6)));

					}
					else if(t.size() == 9) {

						location.setEnvstart(Integer.parseInt((String) t.get(0)));
						location.setEnvend(Integer.parseInt((String) t.get(1)));
						location.setScore(Float.parseFloat((String) t.get(2)));
						location.setEvalue(Float.parseFloat((String) t.get(3)));
						location.setHmmstart(Integer.parseInt((String) t.get(4)));
						location.setHmmend(Integer.parseInt((String) t.get(5)));
						location.setHmmlength(Integer.parseInt((String) t.get(6)));
						location.setStart(Integer.parseInt((String) t.get(7)));
						location.setEnd(Integer.parseInt((String) t.get(8)));
					}

					result.addLocation(location);
					i++;
				}

				if (result.getEntry() != null && result.getEntry().getXrefs() != null) {

					for (Xref xref:result.getEntry().getXrefs()) {

						if (ec2go.containsKey(xref.getId())) {

							String ec = ec2go.get(xref.getId());
							result.setEC(ec);
						}
						if (interpro2go.containsKey(xref.getId())) {

							String name = interpro2go.get(xref.getId());
							result.setGOName(name);
						}
						if (sl2go.containsKey(xref.getId())) {

							String localization = sl2go.get(xref.getId());
							result.setLocalization(localization);
						}
					}
				}

				interProResultsList.addResult(result);
				i++;
			}
		}

		return interProResultsList;
	}
}