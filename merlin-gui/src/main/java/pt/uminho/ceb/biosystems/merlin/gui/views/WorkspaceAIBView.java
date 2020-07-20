package pt.uminho.ceb.biosystems.merlin.gui.views;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

/**
 * @author ODias
 *
 */
public class WorkspaceAIBView extends WorkspaceUpdatablePanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanel1;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private WorkspaceAIB project;
	private int counter;
	private JLabel jLabel5;
	private String email;


	/**
	 * @param p
	 */
	public WorkspaceAIBView(WorkspaceAIB project) {

		this.project = project;
		initGUI();
		this.addListenersToGraphicalObjects();
	}

	/**
	 * 
	 */
	private void initGUI() {

		try  {

			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);
			this.jPanel1 = new JPanel();

			jLabel1 = new JLabel();
			jPanel1.add(jLabel1);
			jLabel1.setBounds(24, this.increaseCounter(35), 489, 20);

			jLabel2 = new JLabel();
			jPanel1.add(jLabel2);
			jLabel2.setBounds(48, this.increaseCounter(35), 489, 20);

			jLabel3 = new JLabel();
			jPanel1.add(jLabel3);
			jLabel3.setBounds(48, this.increaseCounter(35), 489, 20);

			jLabel4 = new JLabel();
			jPanel1.add(jLabel4);
			jLabel4.setBounds(48, this.increaseCounter(35), 489, 20);
			
			jLabel5 = new JLabel();
			jPanel1.add(jLabel5);
			jLabel5.setBounds(48, this.increaseCounter(35), 489, 20);
			
			this.fillList();
			
			jPanel1.setBorder(BorderFactory.createTitledBorder(project.getName()+" data"));
			jPanel1.setLayout(new BorderLayout());
			//this.jPanel1.setBounds(0, 0, 100, 100);
			//this.jPanel1.setPreferredSize(new Dimension(1000, 100));
			JScrollPane scrollp = new JScrollPane(this.jPanel1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			this.add(scrollp, BorderLayout.CENTER);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void fillList() {

		this.setToolTipText("click to refresh");
		
		jLabel1.setText("organism");
		
		if(project.getTaxonomyID()>0) {

			jLabel2.setText("name: "+project.getOrganismName());

			jLabel3.setText("lineage: "+project.getOrganismLineage());
			jLabel3.setBounds((int) jLabel3.getBounds().getX(), (int) jLabel3.getBounds().getY(), project.getOrganismLineage().length()*8, (int) jLabel3.getBounds().getHeight()); 
			this.jPanel1.setPreferredSize(new Dimension(project.getOrganismLineage().length()*8, 100));
			jLabel4.setText("taxonomy ID: "+project.getTaxonomyID());
		}
		this.getEmail();
		jLabel5.setText("e-mail:"+this.email);
	}


	/* (non-Javadoc)
	 * @see merlin_utilities.UpdateUI#updateGraphicalObject()
	 */
	@Override
	public void updateTableUI() {
		
		this.fillList();
		this.updateUI();
		this.revalidate();
		this.repaint();
	}

	/* (non-Javadoc)
	 * @see merlin_utilities.UpdateUI#addListenersToGraphicalObjects(javax.swing.JPanel, javax.swing.MyJTable)
	 */
	@Override
	public void addListenersToGraphicalObjects() {

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {

				updateTableUI();
			}
		});

		this.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {}

			@Override
			public void focusGained(FocusEvent arg0) {

				updateTableUI();
			}
		});
	}

	@Override
	public String getWorkspaceName() {
		return this.project.getName();
	}

	/**
	 * @return the counter
	 */
	private int increaseCounter(int step) {
		counter = counter+step;
		return counter;
	}
	
	
	public void getEmail() {

		String confEmail = "";
		ArrayList<String> listLines = new ArrayList<>();
		String confPath = FileUtils.getConfFolderPath().concat("email.conf");
		File configFile = new File(confPath);
		try {
			Scanner file = new Scanner(configFile);
			while(file.hasNextLine()==true) {
				listLines.add(file.nextLine());
			}
			file.close();	
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}

		for (String item : listLines) {
			
			if(item.startsWith("email")) {

				String[]parts=item.split(":");
				confEmail = parts[1].trim();
			}
		}
		this.email = confEmail;
	}
}
