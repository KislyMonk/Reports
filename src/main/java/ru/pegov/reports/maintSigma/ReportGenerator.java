/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pegov.reports.maintSigma;

import tools.ColumnDeterminant;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
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
    private HashMap<String,CityModel> cityModels = new HashMap<>();
    private double allTimeon2ltp = 0.0;
    private double allTimeOn3LTP = 0;
    private int allTT = 0;
    private int allTTon3LTP = 0;
    
    //uniform format of date
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    
    //date from and to varibles
    private GregorianCalendar min ;
    private GregorianCalendar max ;
    
    private ColumnDeterminant cd = null;
    
    //for remove from result, sime citys must be hidden
    private List<CityModel> toRemove = new ArrayList<>();
    
    //for additional info about TT
    private List<String> repOn3LTPMGN = new ArrayList<String>();
    private List<String> timeMoreThen24h = new ArrayList<String>();
    private List<String> repOn3LTP = new ArrayList<String>();
    
    public ReportGenerator(){
        this.createCityList();
    }
    
    /**
     * Set input stream, that contains input from source report file. Will work only with xslx-file.
     * @param is - InputStream
     */
    public void setInputStream(InputStream is){
        
        int rowCounter = 0;
        
        try {
            XSSFWorkbook sourceBook = new XSSFWorkbook(is);
            XSSFSheet sheet = sourceBook.getSheetAt(0);
            cd = new ColumnDeterminant(sheet.getRow(0));
            sheet.removeRow(sheet.getRow(0)); //delete title row
            
            //prepare to create models
            StreamSupport.stream(sheet.spliterator(), false)
                    .forEach((Row r) ->{
                        try{
                            this.maxMinDate(r.getCell(cd.OPENING_DATE).getDateCellValue().getTime());
                        }catch(IllegalStateException e){
                            this.maxMinDate(this.toLongFromString(r.getCell(cd.OPENING_DATE).getStringCellValue()));
                        }catch (NullPointerException ex){
                            System.out.println("NPE catched, bad row at: " + r.getCell(0));
                        }
                    });
            min.set(GregorianCalendar.HOUR_OF_DAY, 0);
            min.set(GregorianCalendar.MINUTE, 0);
            min.set(GregorianCalendar.SECOND, 0);
            min.set(GregorianCalendar.MILLISECOND, 0);
            max.set(GregorianCalendar.HOUR_OF_DAY, 23);
            max.set(GregorianCalendar.MINUTE, 59);
            max.set(GregorianCalendar.SECOND, 59);
            max.set(GregorianCalendar.MILLISECOND, 0);
            
            //create models
            StreamSupport.stream(sheet.spliterator(), false)
                    .filter(r -> !r.getCell(cd.COMMENT).getStringCellValue().contains("#отключения")) //Колесо попросил отсеить это говно, на самом деле не помогло
                    .forEach(r ->{
                        this.parseRow(r);
                    });
            
        } catch (IOException ex) {
            Logger.getLogger(ReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Get list with statistics models
     * @return list of CityModel objects
     */
    public List<CityModel> getCityModels(){
        List<CityModel> result = new ArrayList<>();
        
        cityModels.values().stream().forEach(cm -> result.add(cm));
        
        for(String c : cityModels.keySet()){
            allTTon3LTP += cityModels.get(c).getCountL3TP();
            allTimeOn3LTP += cityModels.get(c).getTotalTimeL3TP();
        };
        
        //clean result
        for(CityModel city : toRemove){
            result.remove(city);
        }
        
        return result.stream()
                .sorted((CityModel f1, CityModel f2) -> 
                        f1.getName().compareToIgnoreCase(f2.getName()))
                .collect(Collectors.toList());
    }
    
    /**
     * Need for table header. User must know report range
     * @return String like "c 28.01.2018 до 06.02.2018 включительно"
     */
    public String getSamplingRange(){
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
        return " c " + fmt.format(min.getTimeInMillis()) + " до " 
                     + fmt.format(max.getTimeInMillis()) + " включительно";
    }
    
    /**
     * Return list of TT's id, that TT is repeated
     * @return Lis of TT's id, only MGN
     */
    public List<String> getRepOn3LTPMGN() {
        return repOn3LTPMGN;
    }
    
    /**
     * Return list of TT's id, that TT have work time more than 24 hours only MGN
     * @return Lis of TT's id, only MGN
     */
    public List<String> getTimeMoreThen24h() {
        return timeMoreThen24h;
    }
    
    /**
     * Return list of TT's id, that TT is repeated, exclude MGN TT
     * @return Lis of TT's id, exclude MGN TT
     */
    public List<String> getRepOn3LTP() {
        return repOn3LTP;
    }
    
    /**
     * Averege time on 2LTP
     * @return double Averege time on 2LTP
     */
    public double getAvgTime2LTP(){
        return allTimeon2ltp/allTT;
    }
    
    /**
     * Averege time on 3LTP
     * @return double Averege time on 3LTP
     */
    public double getAvgTime3LTP(){
        return allTimeOn3LTP/allTTon3LTP;
    }
    
    //parse a single row
    private void parseRow(Row row){
        String cityName = row.getCell(cd.CITY).getStringCellValue(); //ex: "Магнитогорск (МИ)"
        
        //Белуосова попросила оббъединить Оренбург 21.02.19
        if(cityName.equals("Оренбург")){
            cityName = "Оренбург (Телесот)";
        }
        
        Double timeOnL3TP = getL3TPTime(row); // life time on L3tp
        Double timeOnL2TP = getL2TPTime(row); // life time on L2tp
        String state = row.getCell(cd.STATUS).getStringCellValue(); // name: "Статус"
        
        allTimeon2ltp = allTimeon2ltp + timeOnL2TP;
        allTT++;
        
        long transferTime; //Time of transfer from call-center
        try{
            transferTime = row.getCell(cd.DESTIME).getDateCellValue().getTime();
        }catch(NullPointerException e){
            transferTime = 0;
        }catch(IllegalStateException e){
            transferTime = this.toLongFromString(row.getCell(cd.DESTIME).getStringCellValue());
        }
        
        long openTTTime = 0; //Time of create TT
        try{
            openTTTime = row.getCell(cd.OPENING_DATE).getDateCellValue().getTime();
        }catch(NullPointerException e){
            openTTTime = 0;
        }catch(IllegalStateException e){
            openTTTime = this.toLongFromString(row.getCell(cd.OPENING_DATE).getStringCellValue());
        }
        
        boolean inWorkState = state.equalsIgnoreCase("Новый");
        
        CityModel cm;
        
        if(cityModels.containsKey(cityName)){
            cm = cityModels.get(cityName);
        }else{
            cm = new CityModel(cityName);
            cm.setRegionName(row.getCell(cd.MR).getStringCellValue());
            cityModels.put(cityName, cm);
        }
        
        //all TT are created on L2TP
        if(!inWorkState){
            cm.incCountL2TP();
        }else{
            cm.incAtWorkL2();
        }
        
        if(timeOnL2TP != null){
            cm.incTotalTimeL2TP(timeOnL2TP);
        }
        
        //if TT has any time on L3TP
        try{
            if(timeOnL3TP != null && timeOnL3TP > 0.0001){
                if(!inWorkState){
                    cm.incCountL3TP();
                    cm.incTotalTimeL3TP(timeOnL3TP); //по простбе Белоусовой время считается только тех заявок которые закрыты
                }else{
                    cm.incAtWorkL3();
                }
            }
        }catch(NullPointerException ex){
            //this.printRow(row);
        }
        
        //time division all TTs
        double totalLifeTime;
        if(timeOnL3TP == null){
            if(timeOnL2TP == null){
                totalLifeTime = 0;
            }else{
                totalLifeTime = timeOnL2TP;
            }
        }else{
            try{
                totalLifeTime = timeOnL2TP + timeOnL3TP;
            }catch(NullPointerException e){
                totalLifeTime = 0 + timeOnL3TP;
            }
        }
        
        if(totalLifeTime <= 72.0 && totalLifeTime > 48.0){
            cm.incCompleateAt72h();
        }
        if(totalLifeTime <= 48.0 && totalLifeTime > 36.0){
            cm.incCompleateAt48h();
        }
        if(totalLifeTime <= 36.0){
            cm.incCompleateAt36h();
        }
        if(totalLifeTime > 72.0){
            cm.incCompleateAtMore72h();
        }
        
        //count repeated treatment
        try{
            String pureStr = row.getCell(cd.LAST_TT_FROM).getStringCellValue().replaceAll(" ", ""); //Ибо за каким то перед строкой идут пробелы, блять
            int cutLenght = pureStr.length()/5;
            if(cutLenght != 0){
                String str = pureStr.substring((cutLenght*5), pureStr.length());
                if(str.equalsIgnoreCase("2ЛТП")){
                    cm.incRepeatedTTL2();
                }
                if(str.equalsIgnoreCase("3ЛТП")){
                    if(timeOnL3TP != null && timeOnL3TP > 0.0001){
                        cm.incRepeatedTTL3();
                        if(cityName.equalsIgnoreCase("Магнитогорск (МИ)")){
                           repOn3LTPMGN.add(row.getCell(0).getStringCellValue());
                        }else{
                           repOn3LTP.add(cm.getName() + " : " + cm.getRepeatedTTL3() + ". " + row.getCell(0).getStringCellValue());
                        }
                    }
                }
            }else{
                if(pureStr.equalsIgnoreCase("2ЛТП")){
                    cm.incRepeatedTTL2();
                }
                if(pureStr.equalsIgnoreCase("3ЛТП")){
                    if(timeOnL3TP != null && timeOnL3TP > 0.0001){
                        cm.incRepeatedTTL3();
                        if(cityName.equalsIgnoreCase("Магнитогорск (МИ)")){
                           repOn3LTPMGN.add(row.getCell(0).getStringCellValue());
                        }else{
                            repOn3LTP.add(cm.getName() + " : " + cm.getRepeatedTTL3() + ". " + row.getCell(0).getStringCellValue());
                        }
                    }
                }
            }
        }catch(NullPointerException ex){
            //to do nothing
        }
        
        //time division l2tp's TTs
        try{
            if(timeOnL2TP <= 6.0){
            cm.incCompleateAt6hL2();
            }
            if(timeOnL2TP > 6.0 && timeOnL2TP <= 8.0){
                cm.incCompleateAt8hL2();
            }
            if(timeOnL2TP > 8.0 && timeOnL2TP <= 24.0){
                cm.incCompleateAt24hL2();
            }
            if(timeOnL2TP > 24.0){
                cm.incCompleateAtMore24hL2();
            }
        }catch(NullPointerException e){
            cm.incCompleateAt6hL2();
        }
        
        
        //time division l3tp's TTs
        if(timeOnL3TP != null){
        
            if(timeOnL3TP <= 72.0 && timeOnL3TP > 48.0){
                cm.incCompleateAt72hL3();
            }
            if(timeOnL3TP <= 48.0 && timeOnL3TP > 36.0){
                cm.incCompleateAt48hL3();
            }
            if(timeOnL3TP <= 36.0 && timeOnL3TP > 0.0000000){
                cm.incCompleateAt36hL3();
            }
            if(timeOnL3TP > 72.0){
                cm.incCompleateAtMore72hL3();
            }
            if(timeOnL3TP >= 24.0){
                cm.incCompleateAt24hL3();
                if(cityName.equalsIgnoreCase("Магнитогорск (МИ)")){
                    timeMoreThen24h.add(row.getCell(0).getStringCellValue());
                }
            }
        }
        
        //add decision time
        if(openTTTime != 0 && transferTime != 0){
            if((openTTTime - transferTime) <= 259200000){
                cm.incDecisionToTransfer(openTTTime - transferTime);
            }else{
                cm.incDecisionToTransfer(openTTTime - transferTime);
            }
        }
        
        //System.out.println(row.getCell(0).getStringCellValue() + "\t" + (openTTTime - transferTime)/1000/60);
    }
    
    //return numeric, no metter string or double
    private double fromCellToDouble(Cell cell){
        try{
            return cell.getNumericCellValue();
        }catch(IllegalStateException e){
            return Double.parseDouble(cell.getStringCellValue());
        }
    }
    
    //return life time of TT om L3TP
    private Double getL3TPTime(Row row){
        try{
            return row.getCell(cd.ON_3LTP_TIME).getNumericCellValue();
        }catch(IllegalStateException e){
            try{
                return Double.parseDouble(row.getCell(cd.ON_3LTP_TIME).getStringCellValue());
            }catch(NumberFormatException ex){
                return getTimeFromString(row.getCell(cd.ON_3LTP_TIME).getStringCellValue());
            }
        }
    }
    
    //return life time of TT on L2TP
    private Double getL2TPTime(Row row){
        String rawStr = "";
        try{
            rawStr = row.getCell(cd.ON_2LTP_TIME).getStringCellValue();
        }catch(java.lang.IllegalStateException ex){
            return row.getCell(cd.ON_2LTP_TIME).getNumericCellValue()*24;
        }
        
        return getTimeFromString(rawStr);
    }

    private Double getTimeFromString(String rawStr){
        String hourStr = "00";
        String minuteStr = "00";
        String secondStr = "00";

        try{
            String[] partOfTime = rawStr.split(":");
            hourStr = partOfTime[0];
            minuteStr = partOfTime[1];
            secondStr = partOfTime[2];
        }catch(ArrayIndexOutOfBoundsException e){
            System.err.println(rawStr);
        }


        Double result = 0.0;

        try {
            result = Double.parseDouble(hourStr) +
                    Double.parseDouble(minuteStr)/60 +
                    Double.parseDouble(secondStr)/3600;
        } catch (NumberFormatException ex) {

        }

        return result;
    }
    private Double getL2TPTimeFromDate(Row row){
        Double result = 0d;
        
        
        
        return result;
    }
    
    //create date from string, with uniform format
    private long toLongFromString(String str){        
        try{
            long time = SDF.parse(str).getTime();
            return time;
        }catch(ParseException e){
            return 0;
        }
    }
    
    //sampling range
    private void maxMinDate(long time){
        if(min == null && max == null){
            min = new GregorianCalendar();
            min.setTimeInMillis(time);
            
            max = new GregorianCalendar();
            max.setTimeInMillis(time);
        }else{
            if(min.getTimeInMillis() > time){
                min.setTimeInMillis(time);
            }
            if(max.getTimeInMillis() < time){
                max.setTimeInMillis(time);
            }
        }
    }
    
    //for debug
    private void printRow(Row row){
        System.out.println("\t" + row.getCell(3).getStringCellValue()
            );
    }
    
    private void showAddInfo(){
        System.out.println("Повторные Магнитогорск");
        int i = 0;
        for(String s : repOn3LTPMGN){
            System.out.println(i++ + ". " + s);
        }
        
        System.out.println("\nПовторные Все города");
        i = 0;
        for(String s : repOn3LTP){
            System.out.println(i++ + ". " + s);
        }
        
        System.out.println("\nВремя более 24х часов");
        i = 0;
        for(String s : timeMoreThen24h){
            System.out.println(i++ + ". " + s);
        }
    }
    
    private void createCityList(){
        //full list of URALs Citys
        cityModels.put("Алапаевск", new CityModel("Алапаевск","МР Урал"));
        cityModels.put("Артемовский", new CityModel("Артемовский","МР Урал"));
        cityModels.put("Березники", new CityModel("Березники","МР Урал"));
        cityModels.put("Демьянка", new CityModel("Демьянка","МР Урал"));
        cityModels.put("Дружино", new CityModel("Дружино","МР Урал"));
        cityModels.put("Екатеринбург", new CityModel("Екатеринбург","МР Урал"));
        cityModels.put("Ишим", new CityModel("Ишим","МР Урал"));
        cityModels.put("Каменск-Уральский", new CityModel("Каменск-Уральский","МР Урал"));
        cityModels.put("Камышлов", new CityModel("Камышлов","МР Урал"));
        cityModels.put("Кузино", new CityModel("Кузино","МР Урал"));
        cityModels.put("Куть-Ях", new CityModel("Куть-Ях","МР Урал"));
        cityModels.put("Нижневартовск", new CityModel("Нижневартовск","МР Урал"));
        cityModels.put("Нижний Тагил", new CityModel("Нижний Тагил","МР Урал"));
        cityModels.put("Пермь", new CityModel("Пермь","МР Урал"));
        cityModels.put("Пыть-ях", new CityModel("Пыть-ях","МР Урал"));
        cityModels.put("Реж", new CityModel("Реж","МР Урал"));
        cityModels.put("Салым", new CityModel("Салым","МР Урал"));
        cityModels.put("Серов", new CityModel("Серов","МР Урал"));
        cityModels.put("Соликамск", new CityModel("Соликамск","МР Урал"));
        cityModels.put("Сургут", new CityModel("Сургут","МР Урал"));
        cityModels.put("Тобольск", new CityModel("Тобольск","МР Урал"));
        cityModels.put("Тюмень", new CityModel("Тюмень","МР Урал"));
        cityModels.put("Чусовой", new CityModel("Чусовой","МР Урал"));
        cityModels.put("Югорск", new CityModel("Югорск","МР Урал"));
        cityModels.put("Бакал", new CityModel("Бакал","МР Ю-Урал"));
        cityModels.put("Вязовая", new CityModel("Вязовая","МР Ю-Урал"));
        cityModels.put("Еманжелинск", new CityModel("Еманжелинск","МР Ю-Урал"));
        cityModels.put("Златоуст", new CityModel("Златоуст","МР Ю-Урал"));
        cityModels.put("Карабаш", new CityModel("Карабаш","МР Ю-Урал"));
        cityModels.put("Карталы", new CityModel("Карталы","МР Ю-Урал"));
        cityModels.put("Касли", new CityModel("Касли","МР Ю-Урал"));
        cityModels.put("Копейск", new CityModel("Копейск","МР Ю-Урал"));
        cityModels.put("Коркино", new CityModel("Коркино","МР Ю-Урал"));
        cityModels.put("Кувандык", new CityModel("Кувандык","МР Ю-Урал"));
        cityModels.put("Курган", new CityModel("Курган","МР Ю-Урал"));
        cityModels.put("Кыштым", new CityModel("Кыштым","МР Ю-Урал"));
        cityModels.put("Магнитогорск", new CityModel("Магнитогорск","МР Ю-Урал"));
        cityModels.put("Новогорный", new CityModel("Новогорный","МР Ю-Урал"));
        cityModels.put("Озерск", new CityModel("Озерск","МР Ю-Урал"));
        cityModels.put("Орск", new CityModel("Орск","МР Ю-Урал"));
        cityModels.put("Полетаево", new CityModel("Полетаево","МР Ю-Урал"));
        cityModels.put("Сатка", new CityModel("Сатка","МР Ю-Урал"));
        cityModels.put("Снежинск", new CityModel("Снежинск","МР Ю-Урал"));
        cityModels.put("Троицк", new CityModel("Троицк","МР Ю-Урал"));
        cityModels.put("Чебаркуль", new CityModel("Чебаркуль","МР Ю-Урал"));
        cityModels.put("Челябинск", new CityModel("Челябинск","МР Ю-Урал"));
        cityModels.put("Южноуральск", new CityModel("Южноуральск","МР Ю-Урал"));
        cityModels.put("Оренбург (Телесот)", new CityModel("Оренбург (Телесот)","МР Ю-Урал"));
        cityModels.put("Магнитогорск (МИ)", new CityModel("Магнитогорск (МИ)","ЗАО МАГИНФО"));
        
        //don't show at result
        CityModel putorana = new CityModel("[FVNO] Теплая гора - ООО \"Путарана\")","FVNO");
        CityModel degt = new CityModel("[FVNO] Дегтярск – Интерра Восток");
        CityModel interra = new CityModel("[FVNO] Асбест – Интерра Восток","FVNO");
        CityModel elfimov = new CityModel("[FVNO] Магнитогорск - ИП Елфимов");
        CityModel skynet = new CityModel("[FVNO] Екатеринбург - ООО «СКАЙНЕТ»");
        cityModels.put("[FVNO] Теплая гора - ООО \"Путарана\"", putorana);
        cityModels.put("[FVNO] Асбест – Интерра Восток", interra);
        cityModels.put("[FVNO] Дегтярск – Интерра Восток", degt);
        cityModels.put("[FVNO] Магнитогорск - ИП Елфимов", elfimov);
        cityModels.put("[FVNO] Екатеринбург - ООО «СКАЙНЕТ»", skynet);

        toRemove.add(interra);
        toRemove.add(putorana);
        toRemove.add(degt);
        toRemove.add(elfimov);
        toRemove.add(skynet);
    }
}
