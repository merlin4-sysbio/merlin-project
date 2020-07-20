package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * This class creates and resizes an image icon
 * 
 * @author oDias
 *
 */
public class CreateImageIcon extends ImageIcon  {

	private static final long serialVersionUID = 1L;
	private ImageIcon imageIcon;
	private double factor;
	
	
	
	public CreateImageIcon(ImageIcon imageIcon, double factor) {
		
		super();
		this.imageIcon = imageIcon;
		this.factor = factor;
	}



	/**
	 * @return
	 */
	public ImageIcon resizeImageIcon() {
		
		Image image = imageIcon.getImage();

		int width = (int) (factor * image.getWidth(null));
		int height = (int) (factor * image.getHeight(null));

		Image newImage  = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon newImageIcon = new ImageIcon(newImage);

		return newImageIcon;
	}
	
}
