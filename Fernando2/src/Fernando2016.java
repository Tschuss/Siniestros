import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Fernando2016 {

	public static void main(String[] args) {
		FileInputStream fis = null;
		XSSFWorkbook book = null;
		FileOutputStream fos = null;
		try {
			File excel = new File ("D:/Downloads/TRABAJO.xlsx");
			int header_col= 3; // la D
			int data_col=6; //G
			int last_row=43;
			
			int bloque=1;
			
			String registros="REGISTROS("+bloque+")";
			String resultado="RESULTADO("+bloque+")";
			
			fis = new FileInputStream(excel); 
			book = new XSSFWorkbook(fis); 
			
			XSSFSheet sheet = book.getSheet(registros); 

			XSSFSheet sheet2 = getOrCreateSheet(book, resultado);
			int row2count=0;
			XSSFRow row2=getOrCreateRow(sheet2,row2count++);

			XSSFCell cell2=null;
			
			//copiamos los conceptos como cabecera
			String[] conceptos=new String[last_row-23];
			String[] conceptos2=new String[last_row-23];
			int j=0;
			int k=0;
			for (int i=2; i<last_row ;i++){
				if (i==16 || i==17 || i==23 ||i==30 || i==33 || i==36 || i==39) continue;
				System.out.println("Siguente concepto:" +i);
				String header=null;
				Double dd = 0.0;
				if (i<16) {
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
	
			
			System.out.println(Arrays.asList(conceptos2));
			
			
			//copiamos los datos de los informes periciales
			k=data_col;
			String[] report_data= new String[14];
			Double[] report_points= new Double[last_row-23];
			
			while (sheet.getRow(2).getCell(k) != null) {
				j=0;
				try {
					for (int i=2; i<16 ;i++){
						//guardarmos los campos a repetir
	
						switch (sheet.getRow(i).getCell(k).getCellType()) { 
						case Cell.CELL_TYPE_STRING: report_data[j++]=sheet.getRow(i).getCell(k).getStringCellValue(); break;
						case Cell.CELL_TYPE_NUMERIC: report_data[j++]=""+Math.round(sheet.getRow(i).getCell(k).getNumericCellValue()); break;
						default: 
						}
					}
					System.out.println(Arrays.asList(report_data));
	
					j=0;
					for (int i=17; i<last_row ;i++){
						//y guardamos las puntuaciones
						if (i==16 || i==17 || i==23 ||i==30 || i==33 || i==36 || i==39) continue;
						report_points[j++]=sheet.getRow(i).getCell(k).getNumericCellValue();
					}
					System.out.println(Arrays.asList(report_points));
					
					//escribimos los campos a repetir, por cada puntuacion obtenida
					for (int a=0;a<report_points.length; a++) {
						row2=getOrCreateRow(sheet2, row2count++);
						double dd=-666.0;
						for (int b=0;b<report_data.length;b++) {
							try {
								dd = Double.parseDouble(report_data[b]);
								getOrCreateCell(row2, b).setCellValue(dd);
							} catch (NumberFormatException e) {
								getOrCreateCell(row2, b).setCellValue(report_data[b]);
							}
						}
						getOrCreateCell(row2, report_data.length).setCellValue(conceptos[a]);
						getOrCreateCell(row2, report_data.length+1).setCellValue(conceptos2[a]);
						getOrCreateCell(row2, report_data.length+2).setCellValue(report_points[a]);
						
					}
				} catch (IllegalStateException e) {
					System.err.println(e);
				}
				k++;
			}			
			
			fos = new FileOutputStream(excel); 
			book.write(fos);
		    System.out.println("Writing on Excel file Finished ...");
		      
			
		} catch (FileNotFoundException fnf) {
			// TODO Auto-generated catch block
			fnf.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
