import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Fernando2017 {

	public static void main(String[] args) {
		FileInputStream fis = null;
		XSSFWorkbook book = null;
		FileOutputStream fos = null;
		try {
			File excel = new File ("D:\\DOWNLOAD\\AUDITORIA Q1-V1.xlsx");
			int header_col= 3; // la D
			int data_col=6; //G
			int init_row=0; //1
			int last_row=49; //50
			String skeep_rows=",22,23,29,36,39,42,45,"; //empeiza y termina con comas, las filas de excel -1
			int header_rows_num=22; //count
			int first_points_row=24; //25
			int empty_rows_in_pints=5;
			
			int bloque=1;
			 	
			String registros="REGISTROS("+bloque+")"; //nombre de la hoja dentro del excel que tiene los datos
			String resultado="RESULTADO("+bloque+")";// nombre de la hoja donde van los resultados
			
			System.out.println("antes de abrir el excel...");

			fis = new FileInputStream(excel); 
			book = new XSSFWorkbook(fis); 
			
			System.out.println("ya tengo el excel...");
			
			XSSFSheet sheet = book.getSheet(registros); 

			XSSFSheet sheet2 = getOrCreateSheet(book, resultado);
			int row2count=0;
			XSSFRow row2=getOrCreateRow(sheet2,row2count++);

			XSSFCell cell2=null;
			
			//copiamos los conceptos como cabecera
			String[] conceptos=new String[last_row-first_points_row];
			String[] conceptos2=new String[last_row-first_points_row];
			int j=0;
			int k=0;
			for (int i=init_row; i<last_row ;i++){
				if (skeep_rows.indexOf(","+i+",")!=-1) continue; //-1 es que no la encuentra
				System.out.println("Siguente concepto:" +i);
				String header=null;
			//	Double dd = 0.0;
				if (i<first_points_row) {
					header=sheet.getRow(i).getCell(header_col).getStringCellValue();
					cell2=getOrCreateCell(row2, j++);
					cell2.setCellValue(header);
				}
				else {
					conceptos[k]=sheet.getRow(i).getCell(header_col-1).getStringCellValue();
					conceptos2[k++]=sheet.getRow(i).getCell(header_col).getStringCellValue();
				}
			}
			//completamos con dos cabeceras más: concepto y puntos
			cell2=getOrCreateCell(row2, j++);
			cell2.setCellValue("Codigo");

			cell2=getOrCreateCell(row2, j++);
			cell2.setCellValue("Concepto");
			
			cell2=getOrCreateCell(row2, j++);
			cell2.setCellValue("Puntos");
	
			
			System.out.println("conceptos2=>"+Arrays.asList(conceptos2));
			
			
			//copiamos los datos de los informes periciales
			k=data_col;
			String[] report_data= new String[first_points_row-init_row];
			Double[] report_points= new Double[last_row-first_points_row-empty_rows_in_pints];
			
			while (sheet.getRow(1).getCell(k) != null) {
				j=0;
				try {
					for (int i=init_row; i<first_points_row ;i++){
						//guardarmos los campos a repetir
	
						
						if (sheet.getRow(i).getCell(k)!=null) {
							CellType cType=sheet.getRow(i).getCell(k).getCellTypeEnum();
						
							if (cType.equals(CellType.NUMERIC)) {
								report_data[j++]=""+Math.round(sheet.getRow(i).getCell(k).getNumericCellValue());
							} else if (cType.equals(CellType.STRING)) {
								report_data[j++]=sheet.getRow(i).getCell(k).getStringCellValue();
							} else {
								report_data[j++]="";
							}
						} else {
							report_data[j++]="";
						}
					}
					System.out.println("Report Data ("+k+")=> "+Arrays.asList(report_data));
	
					j=0;
					for (int i=first_points_row; i<last_row ;i++){
						//y guardamos las puntuaciones
						if (skeep_rows.indexOf(","+i+",")!=-1) continue;
						if (sheet.getRow(i).getCell(k)!=null) {
							report_points[j++]=sheet.getRow(i).getCell(k).getNumericCellValue();
						} else {
							report_points[j++]=2.0;
						}
					}
					System.out.println("Report Points ("+k+")=> "+Arrays.asList(report_points));
					
					//escribimos los campos a repetir, por cada puntuacion obtenida
					for (int a=0;a<report_points.length; a++) {
						row2=getOrCreateRow(sheet2, row2count++);
						double dd=-666.0;
						for (int b=0;b<report_data.length;b++) {
							try {
								if (report_data[b] != null){
									dd = Double.parseDouble(report_data[b]);
									getOrCreateCell(row2, b).setCellValue(dd);
								}
							} catch (NumberFormatException e) {
								getOrCreateCell(row2, b).setCellValue(report_data[b]);
							}
						}
						getOrCreateCell(row2, report_data.length).setCellValue(conceptos[a]);
						getOrCreateCell(row2, report_data.length+1).setCellValue(conceptos2[a]);
						if (report_points[a] != null){
							getOrCreateCell(row2, report_data.length+2).setCellValue(report_points[a]);
						}
					}
				} catch (IllegalStateException e) {
					System.err.println("Procesing: "+k+","+j);
					e.printStackTrace();
				}
				k++;
			}			
			
			fos = new FileOutputStream(excel); 
			book.write(fos);
		    System.out.println("Writing on Excel file Finished ...");
		      
			
		} catch (Exception fnf) {
			// TODO Auto-generated catch block
			
			fnf.printStackTrace();
		}finally {
			if (fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (book != null){
				try {
					book.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

	static XSSFSheet getOrCreateSheet (XSSFWorkbook book, String name) {
		XSSFSheet sheet = book.getSheet(name);
		if (sheet != null) {
			return sheet;
		} else {
			return book.createSheet(name);
		}
	}

	static XSSFRow getOrCreateRow (XSSFSheet sheet, int rownum) {
		XSSFRow row = sheet.getRow(rownum);
		if (row != null) {
			return row;
		} else {
			return sheet.createRow(rownum);
		}
	}

	static XSSFCell getOrCreateCell (XSSFRow row, int cellnum) {
		XSSFCell cell = row.getCell(cellnum);
		if (cell != null) {
			return cell;
		} else {
			return row.createCell(cellnum);
		}
	}
}
