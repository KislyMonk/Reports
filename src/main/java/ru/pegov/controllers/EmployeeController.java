package ru.pegov.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.EmptyFileException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.pegov.models.ReportTT_Request;
import ru.pegov.reports.employee.ReportGenerator;
import tools.TT_ReportDownloader;

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
    public ModelAndView submit(@ModelAttribute ReportTT_Request reportTT_request){
        ModelAndView mav = new ModelAndView("Employee");
        
        ReportGenerator rg = new ReportGenerator();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String from = dateFormat.format(reportTT_request.getFrom());
        String to = dateFormat.format(reportTT_request.getTo());

        TT_ReportDownloader TTRD = new TT_ReportDownloader();

        rg.setInputStream(new ByteArrayInputStream(TTRD.getReportUral(from,to)));

        mav.addObject("message", "Ниже смотри отчет или загрузи новый");
        mav.addObject("range", rg.getSamplingRange());
        mav.addObject("modelsList", rg.getModels());
        
        storedMAV = mav;
        
        return mav;
    }
}
