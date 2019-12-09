package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;

public class SaveToTxt {

	 
    /**
     * @param filename
     * @param matrix
     * @param comment
     * @throws Exception
     */
    public static void save_matrix (String filename, String header, String[][] matrix, String comment) throws Exception {
    	
        FileWriter fw = new FileWriter (filename);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(comment);
        bw.newLine();
        bw.write(header);
        bw.newLine();
        bw.write("");
        bw.newLine();
        write_matrix(bw,matrix);
        bw.close();
    }
 
 
    /**
     * @param bw
     * @param matrix
     * @throws Exception
     */
    public static void write_matrix (BufferedWriter bw,String[][] matrix) throws Exception {
    	
        for(int i=0; i < matrix.length; i++) {
        	
            for (int j=0; j < matrix[0].length; j++) {
            	
                bw.write(matrix[i][j]+"\t");
                if (j < matrix[0].length-1) bw.write(" ");
            }
            bw.newLine();
        }
        bw.flush();
    }
    
    /**
     * @param qrtable
     * @return
     */
    public static String[][] qrtableToMatrix(WorkspaceDataTable qrtable) {
    	
    	String[][] result;
    	ArrayList<Object[]> array = qrtable.getTable();
    	if(array.size()==0)
    	{
    		return null;
    	}
    	else
    	{
    		 result = new String[array.size()][array.get(0).length];
    	}
    	for(int i=0;i<array.size();i++)
    	{
    		Object[] linhas = array.get(i);
    		for(int j=0;j<linhas.length;j++)
    		{
    			if(linhas[j]==null)
    				result[i][j]="null";
    			else {
    				
    				String aux = String.valueOf(linhas[j]);
    				result[i][j]=aux;
    			}
    		}
    	}
    	return result;
    }
}
