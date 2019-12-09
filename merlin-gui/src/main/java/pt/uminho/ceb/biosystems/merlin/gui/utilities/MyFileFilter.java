package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import java.io.File;
import java.io.FileFilter;

public class MyFileFilter implements FileFilter {

	
	private String extension;

	public MyFileFilter() {
		
		this.extension = null;
	}
	
	public MyFileFilter(String extension) {
	
		this.extension = extension;
	}

	@Override
	public boolean accept(File pathname) {

		if(this.extension== null)
			return true;
		else if(pathname.getName().endsWith(extension.toString()))
			return true;
		
		return false;
	}

}
