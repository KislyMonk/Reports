/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pegov.reports.employee;

import tools.ColumnDeterminant;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Андрей
 */
public class ReportGenerator {
    private HashMap<String,EmployeeModel> employeeModels = new HashMap<>();
    private ColumnDeterminant cd = null;
    /**
     * Set input stream, that contains input from source report file. Will work only with xslx-file.
     * @param is - InputStream
     */
    public void setInputStream(InputStream is){
        try {
            XSSFWorkbook sourceBook = new XSSFWorkbook(is);
            XSSFSheet sheet = sourceBook.getSheetAt(0);
            cd = new ColumnDeterminant(sheet.getRow(0));
            sheet.removeRow(sheet.getRow(0)); //delete title row
            
            //create models
            StreamSupport.stream(sheet.spliterator(), false)
                    .forEach(r ->{
                        this.parseRow(r);
                    });
            
        }catch(IOException ex){
            System.err.println("ru.pegov.reports.employee.ReportGenerator.setInputStream() can't load ExcelBook" + ex);;
        }
    }
    
    /**
     * Need for table header. User must know report range
     * @return String like "c 28.01.2018 до 06.02.2018 включительно"
     */
    public String getSamplingRange(){
        return "))";
    }
    
    /**
     * Get list with statistics models
     * @return list of EmployeeModel objects
     */
    public List<EmployeeModel> getModels(){
        List<EmployeeModel> result = new ArrayList<>();
        
        employeeModels.values().stream().forEach(cm -> result.add(cm));
        
        return result.stream()
                .sorted((EmployeeModel f1, EmployeeModel f2) -> 
                        f1.getName().compareToIgnoreCase(f2.getName()))
                .collect(Collectors.toList());
    }
    
    private void parseRow(Row row){
        String name = row.getCell(cd.EMPLOYEE).getStringCellValue();
        String repeatedTT = row.getCell(cd.LAST_TT_FROM).getStringCellValue();
        //String reason = row.getCell(cd.REASON).getStringCellValue();
        Double l3tt = getL3TPTime(row);
        
        EmployeeModel  em;
        
        //some employee din't set name, at TT
        if(name.isEmpty()){name = "John Dow";}
        
        //get or create EmployeeModel object
        if(employeeModels.containsKey(name)){
            em = employeeModels.get(name);
        }else{
            em = new EmployeeModel(name);
            employeeModels.put(name, em);
        }
        
        //increment count of TT
        em.incCountTT();
        
        //increment repeated TT count
        if(repeatedTT.contains("2ЛТП")){
            em.incRepeatedTT();
        }
        
        //increment L3TT count
        if(l3tt != null){
            em.incCountL3TT();
        }
        
       // em.setComplexityString(reason);
    }

    private Double getL3TPTime(Row row){
        try{
            return row.getCell(cd.ON_3LTP_TIME).getNumericCellValue();
        }catch(IllegalStateException e){
            try{
                return Double.parseDouble(row.getCell(cd.ON_3LTP_TIME).getStringCellValue());
            }catch(NumberFormatException ex){
                return null;
            }
        }
    }
}
