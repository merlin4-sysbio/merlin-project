/*
 * Created on May 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package pt.uminho.ceb.biosystems.merlin.utilities.io;

import java.io.File;


/**
 * @author  Analia  File Handling class
 */
public abstract class FileHandling {
	
	public FileHandling(){}
	
	protected abstract void process(File f);
	
	public void listAllFiles(File dir){
		//lists the files and subdirectories in a directory
		
		String[] children = dir.list();
		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i=0; i<children.length; i++) {
				// Get filename of file or directory
				String filename = children[i];
				System.out.println("File or directory: "+filename);
			}
		}		
	}
	
//	/**
//	 * It is also possible to filter the list of returned files.
//	 * This example does not return any files that start with `.'.
//	 */
//	private void filterFiles(File dir) {		
//		FilenameFilter filter = new FilenameFilter() {
//			public boolean accept(File dir, String name) {
//				return !name.startsWith(".");
//			}
//		};
//		String[] children = dir.list(filter);
//	}

//	/**
//	 * This filter only returns directories
//	 */
//	private void listDirectories(File dir) {
//		// The list of files can also be retrieved as File objects
//		File[] files = dir.listFiles();
//				
//		FileFilter fileFilter = new FileFilter() {
//			public boolean accept(File file) {
//				return file.isDirectory();
//			}
//		};
//		files = dir.listFiles(fileFilter);
//	}

	/**
	 * @param dir
	 * Process all files and directories under dir
	 */
	public void visitAllDirsAndFiles(File dir) {		
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				visitAllDirsAndFiles(new File(dir, children[i]));
			}
		}else{
			process(dir);			
		}
	}	
	
	/**
	 * @param dir
	 * Process only directories under dir
	 */	
	public void visitAllDirs(File dir) {
		if (dir.isDirectory()) {
			process(dir);
			
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				visitAllDirs(new File(dir, children[i]));
			}
		}
	}
	
	/**
	 * @param dir
	 * Process only files under dir
	 */		
	public void visitAllFiles(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				visitAllFiles(new File(dir, children[i]));
			}
		} else {
			process(dir);
		}
	}
	
	public static void moveAllFiles(String toMove,String dest){
		//File (or directory) to be moved
		File file = new File(toMove);			
		// Destination directory
		//File dir = new File(dest);
		
		System.out.println("file "+file.getName()+" d? "+file.isDirectory());
		if (file.isDirectory()) {
			String[] children = file.list();
			for (int i=0; i<children.length; i++) {			
				moveAllFiles(children[i],dest);
			}
		} else {
			System.out.println("File a mover "+file.getName());
			//boolean success = file.renameTo(new File(dest, file.getName()));
		}		
	}
	
	/**
	 * Moving a File or Directory to Another Directory.
	 * @param filename the filename.
	 * @param dirDest the destination directory.
	 */	
	public static boolean moveFileDirectory(String filename,String dirDest){		
		// File (or directory) to be moved
		File file = new File(filename);
			
		// Destination directory
		File dir = new File(dirDest);
		
System.out.println("orig: "+file.getAbsolutePath()+" dir "+dir.getAbsolutePath());
		
		// Move file to new directory
	boolean success = file.renameTo(new File(dir, file.getName()));
		
		//moveAllFiles(file,dir);

	
		//boolean success=true;
		
		return success;
	}
	
	/**
	 * @param dir
	 * Determining If a File or Directory Exists
	 */	
	public static boolean existFileDirectory(String dir){
		boolean exists = (new File(dir)).exists();
		return exists;
	}
	
	/**
	 * @param dir
	 * Create a directory; all ancestor directories must exist
	 */	
	public static void createDirectory(String dir){
		
		boolean success = (new File(dir)).mkdir();
		if (!success) {
			// Directory creation failed
			System.out.println("Unable to create directory "+dir+"!!!");
		}	
	}
	
	/**
	 * @param dir
	 * Create a directory; all non-existent ancestor directories are
	 * automatically created
	 */	
	public static void createAllDirectories(String dir){
		boolean success = (new File(dir)).mkdirs();
		if (!success) {
			// Directory creation failed
			System.out.println("Unable to create directory "+
					dir+" and the corresponding ancestors!!!");
		}
	}
	
	/**
	 * @param dir
	 * Delete an empty directory
	 * @return Returns true if deletion is successful and false otherwise.
	 */	
	public static boolean deleteEmptyDirectory(File dir){		
		boolean success = dir.delete();
		if (!success) {
			// Deletion failed
			System.out.println("Unable to delete directory "+dir+"!!!");
		}		
		return success;
	}
	
	/**
	 * @param dir
	 * Deletes all files and subdirectories under dir.
	 * @return Returns true if all deletions were successful.
	 * If a deletion fails, the method stops attempting to delete and returns false.
	 */	
	public static boolean deleteDirectory(File dir){
		//If the directory is not empty, it is necessary to first recursively delete all files and subdirectories in the directory. Here is a method that will delete a non-empty directory.
		
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDirectory(new File(dir, children[i]));
				if (!success) {
					// Deletion failed
					System.exit(0);
					return success;					
				}
			}
			return true;
		}
		
		// The directory is now empty so delete it
		return deleteEmptyDirectory(dir);
	}
	
	/**
	 * Deletes the specified file.
	 * If deletion fails, the method stops attempting to delete and returns false.
	 * @param f the filename.
	 * @return Returns true deletion was successful.	 
	 */	
	public static boolean deleteFile(String f){
		boolean success = (new File(f)).delete();
		return success;	
	}
	
	/** 
	 * @return Returns current working directory
	 */
	public static String currentDirectory(){
		String curDir = System.getProperty("user.dir");
		return curDir;
	}

}
