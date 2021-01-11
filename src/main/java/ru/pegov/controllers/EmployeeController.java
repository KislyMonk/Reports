package ru.pegov.controllers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.EmptyFileException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.pegov.reports.employee.ReportGenerator;

/**
 *
 * @author Андрей
 */

@Controller
public class EmployeeController {
    private static ModelAndView storedMAV = null;
    
    @GetMapping("/employee")
    public ModelAndView uploadFile(){
        ModelAndView mav = new ModelAndView("Employee");
        
        mav.addObject("message", "Для обновления/добавления данных выбери файл");
        
        if(storedMAV == null){
            return mav;
        }else{
            storedMAV.addObject("message", "Ниже смотри отчет, он мог протухнуть, хочешь новый - загрузи новый");
            return storedMAV;
        } 
    }
    @PostMapping("/employee")
    public ModelAndView submit(@RequestParam("file") MultipartFile file){
        ModelAndView mav = new ModelAndView("Employee");
        
        ReportGenerator rg = new ReportGenerator();
        
        try {
            rg.setInputStream(file.getInputStream());
        } catch (IOException ex) {
            
        } catch (EmptyFileException ex2) {
            if(storedMAV == null){
                mav.addObject("message", "Воу воу воу полегче, ты забыл файлик");
                return mav;
            }else{
                storedMAV.addObject("message", "Воу воу воу полегче, ты забыл файлик, вот тебе предыдущий отчет");
                return storedMAV;
            } 
        }
        
        
        
        mav.addObject("message", "Ниже смотри отчет или загрузи новый");
        mav.addObject("range", rg.getSamplingRange());
        mav.addObject("modelsList", rg.getModels());
        
        storedMAV = mav;
        
        return mav;
    }
}
