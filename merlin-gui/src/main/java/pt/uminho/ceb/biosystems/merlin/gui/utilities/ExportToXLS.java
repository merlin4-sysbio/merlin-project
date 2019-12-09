package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import java.io.FileOutputStream;

import javax.swing.table.TableColumnModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;

public class ExportToXLS {
	
	public static void exportToXLS(String excelFileName, WorkspaceDataTable mainTableData, MyJTable jTable){
		
		try {
			String sheetName = "Sheet1";//name of sheet

			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet(sheetName) ;

			Row row = sheet.createRow(0);
			
			TableColumnModel tc = jTable.getColumnModel();

			int i = 0;
			
			while (i < tc.getColumnCount()) {
				
				row.createCell(i).setCellValue(tc.getColumn(i).getHeaderValue().toString());
				i++;
			}
			
			for (int r=0;r < mainTableData.getRowCount(); r++ )
			{
				row = sheet.createRow(r+2);

				//iterating c number of columns
				for (int c=0;c < mainTableData.getColumnCount(); c++ )
				{
					Cell cell = row.createCell(c);
					
					if(mainTableData.getValueAt(r, c)!=null)
						cell.setCellValue(mainTableData.getValueAt(r, c).toString());
				}
			}
			
			FileOutputStream fileOut = new FileOutputStream(excelFileName);
			
			//write this workbook to an Outputstream.
			wb.write(fileOut);
			fileOut.flush();
			wb.close();
			fileOut.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
