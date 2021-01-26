package ru.pegov.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pegov.models.ReportTT_Request;
import ru.pegov.reports.maintSigma.ReportGenerator;
import tools.TT_ReportDownloader;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;

/**
 *
 * @author Андрей
 */

@Controller
public class MaintenanceSigmaADController {
    private static ModelAndView storedMAV = null;
    
    
    @GetMapping("/maintenanceSigmaAD")
    public ModelAndView uploadFile(){
        ModelAndView mav = new ModelAndView("MaintenanceSigmaAD");
        
        mav.addObject("message", "Для обновления данных выбери дату");
        
        if(storedMAV == null){
            return mav;
        }else{
            storedMAV.addObject("message", "Ниже смотри отчет");
            return storedMAV;
        } 
    }
    
    
    @PostMapping("/maintenanceSigmaAD")
    public ModelAndView submit(@ModelAttribute ReportTT_Request reportTT_request){
        ReportGenerator rg = new ReportGenerator();
        ModelAndView mav = new ModelAndView("MaintenanceSigmaAD");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String from = dateFormat.format(reportTT_request.getFrom());
        String to = dateFormat.format(reportTT_request.getTo());

        TT_ReportDownloader TTRD = new TT_ReportDownloader();

        rg.setInputStream(new ByteArrayInputStream(TTRD.getReportUral(from,to)));
        


        mav.addObject("message", "Ниже смотри отчет или загрузи новый");
        mav.addObject("range", rg.getSamplingRange());
        mav.addObject("cityModelsList", rg.getCityModels());
        mav.addObject("repOn3LTP", rg.getRepOn3LTP());
        mav.addObject("repOn3LTPMGN", rg.getRepOn3LTPMGN());
        mav.addObject("timeMoreThen24h", rg.getTimeMoreThen24h());
        mav.addObject("avgTime2LTP", rg.getAvgTime2LTP());
        mav.addObject("avgTime3LTP", rg.getAvgTime3LTP());
        
        return mav;
    }
}
