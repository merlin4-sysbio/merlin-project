package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import pt.uminho.ceb.biosystems.merlin.utilities.OpenBrowser;

public class LinkOut extends JPopupMenu{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8183676298335377174L;
	private Map<Integer,String> database;
	private Map<Integer,String> url;
	private Map<Integer,ImageIcon> ico;

	public LinkOut(List<Integer> menuItems, String query){
		initGUI("External Links", menuItems, query);
	}
	
	private void initGUI(String windowName, List<Integer> menuItems, String query) {
		
		this.database = new TreeMap<Integer,String>();
		this.database.put(0, "iHop");
		this.database.put(1, "UniProt");
		this.database.put(2, "BRENDA");
		this.database.put(3, "BRENDA");
		this.database.put(4, "Blast");
		this.database.put(5, "Taxonomy");
		this.database.put(6, "InterPro");
		
		this.url = new TreeMap<Integer,String>();
		this.url.put(0, "http://www.ihop-net.org/UniPub/iHOP/?search="+query);
		this.url.put(1, "http://www.uniprot.org/uniprot/?query="+query+"&sort=score");
		//this.url.put(2, "http://www.brenda-enzymes.org/php/search_result.php4?a=9&W%5B2%5D="+query+"&T%5B2%5D=2&Search=Search&l=10");
		this.url.put(2, "http://www.brenda-enzymes.org/search_result.php?quicksearch=1&noOfResults=10&a=9&W[2]="+query+"&T[2]=2");
		//this.url.put(3, "http://www.brenda-enzymes.org/php/result_flat.php4?ecno="+query);
		this.url.put(3, "http://www.brenda-enzymes.org/enzyme.php?ecno="+query);
		this.url.put(4, "http://blast.ncbi.nlm.nih.gov/Blast.cgi?PAGE=Proteins&PROGRAM=blastp&BLAST_PROGRAMS=blastp&QUERY="+query+"&PAGE_TYPE=BlastSearch&LINK_LOC=protein&log$=seqview_list_blast");
		this.url.put(5, "http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Undef&name="+query+"&lvl=0&srchmode=1");
		this.url.put(6, "https://www.ebi.ac.uk/interpro/entry/"+query);
		
		this.ico = new TreeMap<Integer,ImageIcon>();
		this.ico.put(0, new ImageIcon(getClass().getClassLoader().getResource("icons/ihop.png")));
		this.ico.put(1, new ImageIcon(getClass().getClassLoader().getResource("icons/uniprot.png")));
		this.ico.put(2, new ImageIcon(getClass().getClassLoader().getResource("icons/brenda.png")));
		this.ico.put(3, new ImageIcon(getClass().getClassLoader().getResource("icons/brenda.png")));
		this.ico.put(4, new ImageIcon(getClass().getClassLoader().getResource("icons/helix.png")));
		this.ico.put(5, new ImageIcon(getClass().getClassLoader().getResource("icons/helix.png")));
		this.ico.put(6, new ImageIcon(getClass().getClassLoader().getResource("icons/interpro.png")));
		this.addMenuItem(menuItems, query);
	}	
	
	private void addMenuItem(List<Integer> menuItems, String query){

		for(final Integer item: menuItems)
		{
			JMenuItem menuItem = new JMenuItem(this.database.get(item),this.ico.get(item));
			menuItem.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent arg0) 
				{
					OpenBrowser  openUrl = new OpenBrowser();
					openUrl.setUrl(url.get(item));
					openUrl.openURL();
				}});
			this.add(menuItem);
		}
	}


}
