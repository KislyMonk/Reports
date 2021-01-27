/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.stream.StreamSupport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * ColumnDeterminant need to determinant column index.
 * @author Андрей
 */
public class ColumnDeterminant {
    private final String IDname = "ID";
    private final String MRname = "МР";
    private final String CityName = "Город";
    private final String ReasonName = "Процесс";
    private final String DesTimeName = "Решение о переводе на 2ЛТП";
    private final String OpeningDateName = "Дата открытия";
    private final String EndingDateName = "Дата закрытия";
    private final String StatusName = "Статус";
    private final String EmployeeName = "Последний ответственный";
    private final String Start3LTPDateName = "Посл. перевод на 3ЛТП";
    private final String End3LTPDateName = "Посл. перевод с 3ЛТП";
    private final String On2LTPTimeName = "Общ. время ТТ в ЗО 2 ЛТП";
    private final String On3LTPTimeName = "Общ. время ТТ в ЗО 3 ЛТП";
    private final String RepeatedCountName = "Кол-во ТТ тех. кат. по аб. за 30 дн.";
    private final String LastTTFromName = "Пред. обращ-е закрыто на";
    private final String AppealOpeningDateName = "Дата открытия обращения";
    private final String LastTransferOn2LTPDateName = "Посл. перевод на 2ЛТП";
    private final String ClientIdName = "Номер договор";
    private final String Description = "Описание проблемы";
            
    
    
    /**
     * return index of column with name "МР"
     */
    public int MR;

    /**
     * return index of column with name "Город"
     */
    public int CITY;

    /**
     * return index of column with name "Процесс"
     */
    public int REASON;

    /**
     * return index of column with name "Решение о переводе на 2ЛТП"
     */
    public int DESTIME;

    /**
     * return index of column with name "Дата открытия"
     */
    public int OPENING_DATE;

    /**
     * return index of column with name "Дата закрытия"
     */
    public int ENDING_DATE;

    /**
     * return index of column with name "Статус"
     */
    public int STATUS;

    /**
     * return index of column with name " Последний ответственный"
     */
    public int EMPLOYEE;

    /**
     * return index of column with name "Посл. перевод на 3ЛТП"
     */
    public int START_3LTP_DATE;

    /**
     * return index of column with name "Посл. перевод с 3ЛТП"
     */
    public int END_3LTP_DATE;

    /**
     * return index of column with name "Общ. время ТТ в ЗО 2 ЛТП"
     */
    public int ON_2LTP_TIME;

    /**
     * return index of column with name "Общ. время ТТ в ЗО 3 ЛТП"
     */
    public int ON_3LTP_TIME;

    /**
     * return index of column with name "Кол-во ТТ тех. кат. по аб. за 30 дн."
     */
    public int REPEATED_COUNT;

    /**
     * return index of column with name "Пред. обращ-е закрыто на"
     */
    public int LAST_TT_FROM;
    
    /**
     * return index of column with name "Описание проблемы"
     */
    public int DESCRIPTION;
    
    /**
     * 
     * @param row is a object of org.apache.poi.ss.usermodel.Row, that contain titele row
     */
    public ColumnDeterminant(Row row){
        StreamSupport.stream(row.spliterator(), false).forEach(cell -> {
            if(cell.getStringCellValue().equalsIgnoreCase(MRname)){
                MR = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(CityName)){
                CITY = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(ReasonName)){
                REASON = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(DesTimeName)){
                DESTIME = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(OpeningDateName)){
                OPENING_DATE = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(EndingDateName)){
                ENDING_DATE = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(StatusName)){
                STATUS = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(EmployeeName)){
                EMPLOYEE = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(Start3LTPDateName)){
                START_3LTP_DATE = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(End3LTPDateName)){
                END_3LTP_DATE = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(On2LTPTimeName)){
                ON_2LTP_TIME = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(On3LTPTimeName)){
                ON_3LTP_TIME = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(RepeatedCountName)){
                REPEATED_COUNT = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(LastTTFromName)){
                LAST_TT_FROM = cell.getColumnIndex();
            }
            if(cell.getStringCellValue().equalsIgnoreCase(Description)){
                DESCRIPTION = cell.getColumnIndex();
            }
        });
    }
}
