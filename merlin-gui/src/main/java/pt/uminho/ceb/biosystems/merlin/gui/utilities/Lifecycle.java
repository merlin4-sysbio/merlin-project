/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.plaf.ColorUIResource;

import org.platonos.pluginengine.PluginLifecycle;

import es.uvigo.ei.aibench.workbench.Workbench;

/**
 * @author paulo maia, 09/05/2007
 *
 */
public class Lifecycle extends PluginLifecycle {
	
	@Override
	public void start(){

		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			String theme = "Large-Font";
			com.jtattoo.plaf.mint.MintLookAndFeel.setTheme(theme);

			String laf = "com.jtattoo.plaf.mint.MintLookAndFeel";
			//String laf = "com.jtattoo.plaf.fast.FastLookAndFeel";

			String os_name = System.getProperty("os.name");

			if(os_name.contains("Windows"))
				UIManager.setLookAndFeel(laf);

		} 
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {

			e1.printStackTrace();
		} 

		UIManager.getLookAndFeelDefaults().put("Table:\"Table.cellRenderer\".background",new ColorUIResource(Color.WHITE));
		UIManager.getLookAndFeelDefaults().put("Table:\"Table.showGrid", false);

		UIManager.put("Table.alternateRowColor", new Color (242, 242, 242));

		UIManager.put("ComboBox.background", new Color (242, 242, 242));
		UIManager.put("ComboBox.selectionBackground", new ColorUIResource(Color.WHITE));

		UIManager.getDefaults().put("TableHeader.cellBorder" , BorderFactory.createEmptyBorder(0,0,0,0));
		UIManager.getDefaults().put("Table.showGrid", false);

		WindowListener[] windowListeners = Workbench.getInstance().getMainFrame().getWindowListeners();
		for (WindowListener windowListener : windowListeners)
			Workbench.getInstance().getMainFrame().removeWindowListener(windowListener);
		
		Workbench.getInstance().getMainFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				
				Workbench.getInstance().getMainFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				Workbench.getInstance().executeOperation("operations.QuitOperation.ID");
			}
		});
		
		Workbench.getInstance().getMainFrame().setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
	}
}
