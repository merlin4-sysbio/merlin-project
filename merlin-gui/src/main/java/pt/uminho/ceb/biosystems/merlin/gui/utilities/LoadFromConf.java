package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.EBIOMASSTEMPLATE;
import pt.uminho.ceb.biosystems.merlin.utilities.io.ConfFileReader;

/**
 * @author adias
 *
 */
public class LoadFromConf {

	public static Map<String,String> loadDatabaseCredentials(String path){
		HashMap<String, String> credentials = new HashMap<String,String>();
		Map<String, String> settings = null;
		
		try {
			settings = ConfFileReader.loadConf(path+"/database_settings.conf");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		String dbType = settings.get("dbtype");
		
		
		try {
			if (dbType.equals("mysql")) {
				credentials.put("dbtype",dbType);
				credentials.put("username",settings.get("username"));
				credentials.put("password",settings.get("password"));
				credentials.put("host",settings.get("host"));
				credentials.put("port",settings.get("port"));
			}
			if (dbType.equals("h2")) {
				credentials.put("dbtype",dbType);
				credentials.put("username",settings.get("h2_username"));
				credentials.put("password",settings.get("h2_password"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			//Workbench.getInstance().error("error! check your database configuration file in merlin directory at /conf/database_settings.conf");
			Workbench.getInstance().error(e);

		}
		return credentials;
	}

	public static Map<String,String> loadReactionsThresholds(String path){
	Map<String, String> credentials = null;

		try {
			credentials = ConfFileReader.loadConf(path+"/reaction_thresholds.conf");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
			return credentials;
	}
	
	public static Map<String,String> loadGPRsettings(String path){
	Map<String, String> settings = null;

		try {
			settings = ConfFileReader.loadConf(path+"/gpr_settings.conf");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
			return settings;
	}
	
	public static Map<String,String> loadEbiomassContents(String path, EBIOMASSTEMPLATE template){
	HashMap<String, String> contents = new HashMap<String,String>();
	Map<String, String> settings = null;

		try {
			settings = ConfFileReader.loadConf(path+"/ebiomass_contents.conf");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		try {

			if (template.equals(EBIOMASSTEMPLATE.Archea)) {
				contents.put("proteinContents",settings.get("A_proteinContents"));
				contents.put("dnaContents",settings.get("A_dnaContents"));
				contents.put("rnaContents",settings.get("A_rnaContents"));
				contents.put("mRNA_Contents",settings.get("A_mRNA_Contents"));
				contents.put("tRNA_Contents",settings.get("A_tRNA_Contents"));
				contents.put("rRNA_Contents",settings.get("A_rRNA_Contents"));
			}
			if (template.equals(EBIOMASSTEMPLATE.Cyano)) {
				contents.put("proteinContents",settings.get("C_proteinContents"));
				contents.put("dnaContents",settings.get("C_dnaContents"));
				contents.put("rnaContents",settings.get("C_rnaContents"));
				contents.put("mRNA_Contents",settings.get("C_mRNA_Contents"));
				contents.put("tRNA_Contents",settings.get("C_tRNA_Contents"));
				contents.put("rRNA_Contents",settings.get("C_rRNA_Contents"));
			}
			if (template.equals(EBIOMASSTEMPLATE.GramPositive)) {
				contents.put("proteinContents",settings.get("P_proteinContents"));
				contents.put("dnaContents",settings.get("P_dnaContents"));
				contents.put("rnaContents",settings.get("P_rnaContents"));
				contents.put("mRNA_Contents",settings.get("P_mRNA_Contents"));
				contents.put("tRNA_Contents",settings.get("P_tRNA_Contents"));
				contents.put("rRNA_Contents",settings.get("P_rRNA_Contents"));
			}
			if (template.equals(EBIOMASSTEMPLATE.GramNegative)) {
				contents.put("proteinContents",settings.get("N_proteinContents"));
				contents.put("dnaContents",settings.get("N_dnaContents"));
				contents.put("rnaContents",settings.get("N_rnaContents"));
				contents.put("mRNA_Contents",settings.get("N_mRNA_Contents"));
				contents.put("tRNA_Contents",settings.get("N_tRNA_Contents"));
				contents.put("rRNA_Contents",settings.get("N_rRNA_Contents"));
			}
			if (template.equals(EBIOMASSTEMPLATE.Mold)) {
				contents.put("proteinContents",settings.get("M_proteinContents"));
				contents.put("dnaContents",settings.get("M_dnaContents"));
				contents.put("rnaContents",settings.get("M_rnaContents"));
				contents.put("mRNA_Contents",settings.get("M_mRNA_Contents"));
				contents.put("tRNA_Contents",settings.get("M_tRNA_Contents"));
				contents.put("rRNA_Contents",settings.get("M_rRNA_Contents"));
			}
			if (template.equals(EBIOMASSTEMPLATE.Yeast)) {
				contents.put("proteinContents",settings.get("Y_proteinContents"));
				contents.put("dnaContents",settings.get("Y_dnaContents"));
				contents.put("rnaContents",settings.get("Y_rnaContents"));
				contents.put("mRNA_Contents",settings.get("Y_mRNA_Contents"));
				contents.put("tRNA_Contents",settings.get("Y_tRNA_Contents"));
				contents.put("rRNA_Contents",settings.get("Y_rRNA_Contents"));
			}
			if (template.equals(EBIOMASSTEMPLATE.Custom)) {
				contents.put("proteinContents",settings.get("proteinContents"));
				contents.put("dnaContents",settings.get("dnaContents"));
				contents.put("rnaContents",settings.get("rnaContents"));
				contents.put("mRNA_Contents",settings.get("mRNA_Contents"));
				contents.put("tRNA_Contents",settings.get("tRNA_Contents"));
				contents.put("rRNA_Contents",settings.get("rRNA_Contents"));
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			Workbench.getInstance().error("error! check contents configuration file in merlin directory at /conf/ebiomass_contents.conf");
		}
			
		return contents;
	}
	
	public static Map<String,String> loadTransportReactionsSettings(String path){
		Map<String, String> settings = null;

			try {
				settings = ConfFileReader.loadConf(path+"/transp_reactions_settings.conf");
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
				return settings;
		}
	
	
}





