package ru.pegov.reports.employee;

import java.util.HashSet;

/**
 * Use it to know how many skills have an employee.
 * @author Pegov Andrew
 */
public class TTComplexity {
    //complexity counts
    private int lowCount = 0;
    private int mediumCount = 0;
    private int hightCount = 0;
    
    //complexity sets
    private HashSet<String> lowSet = new HashSet<String>();
    private HashSet<String> mediumSet = new HashSet<String>();
    private HashSet<String> hightSet = new HashSet<String>();

    public TTComplexity() {
        lowSet.add("Авария/ПР");
        lowSet.add("Жалоба на состояние оборудование (мусор, висят провода в подъезде)");
        lowSet.add("Заявка на ремонт (выездное обслуживание)");
        lowSet.add("Консультация по настройке ТВ (аналог/цифра/IPTV)");
        lowSet.add("ТВ_Не качественное изображение/звук");
        lowSet.add("ТВ_Не показывают каналы (часть/все)");
        lowSet.add("Проблемы с Личным кабинетом");
        lowSet.add("ТВ_Другие технические  ошибки/проблемы");
        lowSet.add("ТВ_Не качественное изображение/звук");
        lowSet.add("ТВ_Не показывают каналы (часть/все)");
        lowSet.add("Повторное обращение по ТТ");
        lowSet.add("Технические проблемы с Телевидением");
        lowSet.add("Др. технические проблемы");
        lowSet.add("Консультация по настройке оборудования");
        
        
        mediumSet.add("Настройка подключения (PPPOE, L2TP, PPTP)");
        mediumSet.add("Консультация по настройке ПО");
        mediumSet.add("Не открываются страницы (подкл. через роутер)");
        mediumSet.add("Не открываются страницы, ошибок нет");
        mediumSet.add("Ошибки 651,638, 678, 769, 815, 814,868, 800");
        mediumSet.add("Ошибки 691, 718, 720, 721, 735, 738, 742, 789, 619, 629");
        mediumSet.add("Телефония_тех. вопросы");
        
        hightSet.add("Низкая скорость");
        hightSet.add("Обрывы связи");
        hightSet.add("Технические проблемы с Интернетом");
        
    }
    
    /**
     * take String with complexity of this TT. Increase one of the three counts
     * @param complexityString 
     */
    public void count(String complexityString){
        if(lowSet.contains(complexityString)){
            lowCount++;
            return ;
        }
        if(mediumSet.contains(complexityString)){
            mediumCount++;
            return;
        }
        if(hightSet.contains(complexityString)){
            hightCount++;
            return;
        }
        System.err.println("TTComplexity unmarked string: " + complexityString);
    }

    /**
     * 
     * @return amount of added low complexity TT
     */
    public int getLowCount() {
        return lowCount;
    }

    /**
     * 
     * @return amount of added medium complexity TT
     */
    public int getMediumCount() {
        return mediumCount;
    }

    /**
     * 
     * @return amount of added hight complexity TT
     */
    public int getHightCount() {
        return hightCount;
    }
    
    
}
