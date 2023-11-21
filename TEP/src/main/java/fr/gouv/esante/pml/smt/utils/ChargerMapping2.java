package fr.gouv.esante.pml.smt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ChargerMapping2 {
	
	public static HashMap<String, List<String>> listConceptsTpe = new HashMap<String, List<String>>();
	public static List<String> listeCodes = new ArrayList<String>();

	public static  void  chargeExcelConceptToList(final String xlsFile) throws IOException, ParseException, InvalidFormatException {
		
	//public static  void  main(String []args) throws IOException, ParseException {	
		
		
      // String xlsFile = PropertiesUtil.getProperties("xlsxTepFile");
		
		
		
		getlisteCodes(xlsFile);
		
		FileInputStream file = new FileInputStream(new File(xlsFile));
		
		//XSSFWorkbook workbook = new XSSFWorkbook(file);
		org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(file);
		
		//XSSFSheet sheet = workbook.getSheet("TEP 2022.v0");
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(1);
		
		Iterator<Row> rowIterator = sheet.iterator();
		
		rowIterator.next(); 
		
		
		while (rowIterator.hasNext()) {
			 
			 List<String> properties = new ArrayList<String>(); 
			
			 Row row = rowIterator.next();
			 
	    	 Cell cellCode = row.getCell(9); //get Ref Code
	    	 Cell cellLibelle = row.getCell(21); // get Libelle
	    	 Cell cellDescp = row.getCell(19); // get Description
	    	 Cell cellSynonyme = row.getCell(18); // get synonyme
	    	 Cell cellStatus =  row.getCell(25); // get status
	    	 Cell cellNatureModif = row.getCell(22); // get Libelle
	    	 
	    	 Cell cellClass = row.getCell(1); // get Class
	    	 Cell cellSousClass = row.getCell(2); // get sousClass
	    	 Cell cellNiveau1 = row.getCell(3); // get Niveau1
	    	 Cell cellNiveau2 = row.getCell(4); // get Niveau2
	    	 Cell cellNiveau3 = row.getCell(5); // get Niveau3
	    	 Cell cellNiveau4 = row.getCell(6); // get Niveau4
	    	 Cell cellNiveau5 = row.getCell(7); // get Niveau5
	    	 Cell cellNiveau6 = row.getCell(8); // get Niveau6
	    	 
	     if(cellCode != null && cellCode.getCellType() != Cell.CELL_TYPE_BLANK)	 {
		     String synonyme = "N/A";
		     String description = "N/A";
		     String sousClass = "N/A";
		     String natureModif = "N/A";
		     String niveau1 ="N/A";
		     String niveau2 ="N/A";
		     String niveau3 ="N/A";
		     String niveau4 ="N/A";
		     String niveau5 ="N/A";
		     String niveau6 ="N/A";

		     if(cellSynonyme != null && cellSynonyme.getCellType() != Cell.CELL_TYPE_BLANK)
		    	 synonyme = cellSynonyme.getStringCellValue();
		     
		     if(cellNatureModif != null && cellNatureModif.getCellType() != Cell.CELL_TYPE_BLANK)
		    	 natureModif = cellNatureModif.getStringCellValue();
		     
		     if(cellDescp != null && cellDescp.getCellType() != Cell.CELL_TYPE_BLANK)
		    	 description = cellDescp.getStringCellValue();
		     
		     if(cellSousClass != null && cellSousClass.getCellType() != Cell.CELL_TYPE_BLANK)
		     sousClass = cellSousClass.getStringCellValue();
		     
		     
		     
		     if(cellNiveau1 != null && cellNiveau1.getCellType() != Cell.CELL_TYPE_BLANK) {
		    	try {
		    	       niveau1 = cellNiveau1.getStringCellValue();
		    	}catch (Exception e) {
					if((int)cellNiveau1.getNumericCellValue() < 10)
						
		    		  niveau1 =  "0".concat(String.valueOf( (int)cellNiveau1.getNumericCellValue()));
					else
						niveau1 =  (String.valueOf( (int)cellNiveau1.getNumericCellValue()));
				}   
		     }
		    	
		     if(cellNiveau2 != null && cellNiveau2.getCellType() != Cell.CELL_TYPE_BLANK)
		    	 niveau2 = cellNiveau2.getStringCellValue();
		     
		     if(cellNiveau3 != null && cellNiveau3.getCellType() != Cell.CELL_TYPE_BLANK)
		    	 niveau3 = cellNiveau3.getStringCellValue();
		     
		     if(cellNiveau4 != null && cellNiveau4.getCellType() != Cell.CELL_TYPE_BLANK)
		     {
			    	try {
			    	       niveau4 = cellNiveau4.getStringCellValue();
			    	}catch (Exception e) {
			    		
			    		if((int)cellNiveau4.getNumericCellValue() < 10)
			    		  niveau4 =  "0".concat(String.valueOf( (int)cellNiveau4.getNumericCellValue()));
			    		else
			    			niveau4 =  (String.valueOf( (int)cellNiveau4.getNumericCellValue()));
			    	}     
			  }
		     
		     if(cellNiveau5 != null && cellNiveau5.getCellType() != Cell.CELL_TYPE_BLANK)
		     {
			    	try {
			    	       
			    		niveau5 = cellNiveau5.getStringCellValue();
			    	}catch (Exception e) {
			    		if((int)cellNiveau5.getNumericCellValue() < 10)
			    		  niveau5 =  "0".concat(String.valueOf( (int)cellNiveau5.getNumericCellValue()));
			    		else 
			    		  niveau5 =  (String.valueOf( (int)cellNiveau5.getNumericCellValue()));
					}   
			  }
		     
		     if(cellNiveau6 != null && cellNiveau6.getCellType() != Cell.CELL_TYPE_BLANK)
		    	 niveau6 = cellNiveau6.getStringCellValue();
		     
		     List<String> listprop =
		    		 getCodeParent(cellClass.getStringCellValue(), sousClass, niveau1, niveau2, niveau3, niveau4, niveau5, niveau6);
		     
		     properties.add(cellLibelle.getStringCellValue());//0
		     properties.add(description);//1
		     properties.add(synonyme);//2
		     properties.add(String.valueOf((int)cellStatus.getNumericCellValue()));//3
             properties.add(natureModif);//4
             properties.add(listprop.get(0));//5 codeParent
             properties.add(listprop.get(1));//6 type niveau
            
             
             
             listConceptsTpe.put(cellCode.getStringCellValue(), properties);
		     
		    //System.out.println("*** "+cellCode.getStringCellValue() +" "+cellLibelle.getStringCellValue()+ " " + description+
		    		//" "+synonyme +" "+ (int)cellStatus.getNumericCellValue()+ " "+cellNatureModif.getStringCellValue()+ " "+
		    		//cellClass.getStringCellValue()+ " "+ sousClass + " "+niveau1+ " "+niveau2+ " "+
		    		//niveau3+ " "+niveau4 + " "+niveau5+ " "+niveau6);
		     
		     
		    
	     }
		     
		   
		}
		
		



		 //return listConceptsTpe;
	    

	}

	private static void getlisteCodes(final String xlsFile) throws IOException {
		
        FileInputStream file = new FileInputStream(xlsFile);
		
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		
		XSSFSheet sheet = workbook.getSheet("TEP 2022.v0");
		
		Iterator<Row> rowIterator = sheet.iterator();
		
		rowIterator.next();
		
		while (rowIterator.hasNext()) {
			
             Row row = rowIterator.next();
			 
	    	 Cell cellCode = row.getCell(9); //get Ref Code
	    	 
	    	 if(cellCode != null && cellCode.getCellType() != Cell.CELL_TYPE_BLANK)	 {
	    		 
	    		 listeCodes.add(cellCode.getStringCellValue());
	    	 }
			
			
		}
		
		
	}

	//retourne codeParent, type(niveau)
	private static List<String> getCodeParent(String Class, String sousClass, String niveau1, String niveau2,
			String niveau3, String niveau4, String niveau5, String niveau6) {
		
		String codeParent ="";
		String type ="";
		List<String> parametres = new ArrayList<String>();
		
		if("N/A".equals(sousClass)) {codeParent="TEP"; type="Classe";}
		else if(!"N/A".equals(sousClass) && "N/A".equals(niveau1)){codeParent=Class; type="Sous Classe";}
		else if(!"N/A".equals(niveau1) && "N/A".equals(niveau2)){codeParent=Class.concat(sousClass); type="Niveau 1";}
		else if(!"N/A".equals(niveau2)&& "N/A".equals(niveau3)){codeParent=Class.concat(sousClass).concat(niveau1); type="Niveau 2";}
		else if(!"N/A".equals(niveau3)&& "N/A".equals(niveau4)){codeParent=Class.concat(sousClass).concat(niveau1).concat(niveau2); type="Niveau 3";}
		else if(!"N/A".equals(niveau4)&& "N/A".equals(niveau5)){codeParent=Class.concat(sousClass).concat(niveau1).concat(niveau2).concat(niveau3); type="Niveau 4";}
		else if(!"N/A".equals(niveau5)&& "N/A".equals(niveau6)){codeParent=Class.concat(sousClass).concat(niveau1).concat(niveau2).concat(niveau3).concat(niveau4); type="Niveau 5";}
		else if(!"N/A".equals(niveau5)&& !"N/A".equals(niveau6)){codeParent=Class.concat(sousClass).concat(niveau1).concat(niveau2).concat(niveau3).concat(niveau4).concat(niveau5); type="Niveau 6";}
        
		if(!listeCodes.contains(codeParent)) {
			System.out.println("** "+codeParent);
			if("5".equals(type))
				codeParent=Class.concat(sousClass).concat(niveau1);
			else if("6".equals(type))
				codeParent=Class.concat(sousClass).concat(niveau1).concat(niveau2);
			else if("7".equals(type))
				codeParent=Class.concat(sousClass).concat(niveau1).concat(niveau2).concat(niveau3);
			else if("8".equals(type))
				codeParent=Class.concat(sousClass).concat(niveau1).concat(niveau2).concat(niveau3).concat(niveau4);
			
		}
		
		parametres.add(codeParent);
		parametres.add(type);
		
		return parametres;
				
		
	}

}
