package ru.pegov.controllers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.pegov.reports.maintSigma.ReportGenerator;
import org.apache.poi.EmptyFileException;

/**
 *
 * @author Андрей
 */

@Controller
public class MaintenanceSigmaController {
    private static ModelAndView storedMAV = null;
    
    
    @GetMapping("/maintenanceSigma")
    public ModelAndView uploadFile(){
        ModelAndView mav = new ModelAndView("MaintenanceSigma");
        
        mav.addObject("message", "Для обновления/добавления данных выбери файл");
        
        if(storedMAV == null){
            return mav;
        }else{
            storedMAV.addObject("message", "Ниже смотри отчет, он мог протухнуть, хочешь новый - загрузи новый");
            return storedMAV;
        } 
    }
    
    
    @PostMapping("/maintenanceSigma")
    public ModelAndView submit(@RequestParam("file") MultipartFile file){
        ModelAndView mav = new ModelAndView("MaintenanceSigma");
        
        ReportGenerator rg = new ReportGenerator();
        
        try {
            rg.setInputStream(file.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(MaintenanceSigmaController.class.getName()).log(Level.SEVERE, null, ex);
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
        mav.addObject("cityModelsList", rg.getCityModels());
        mav.addObject("repOn3LTP", rg.getRepOn3LTP());
        mav.addObject("repOn3LTPMGN", rg.getRepOn3LTPMGN());
        mav.addObject("timeMoreThen24h", rg.getTimeMoreThen24h());
        mav.addObject("avgTime2LTP", rg.getAvgTime2LTP());
        mav.addObject("avgTime3LTP", rg.getAvgTime3LTP());
        
        storedMAV = mav;
        
        return mav;
    }
}
