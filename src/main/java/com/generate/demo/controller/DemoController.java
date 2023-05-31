package com.generate.demo.controller;

import com.generate.demo.dto.DemoRequest;
import com.generate.demo.dto.RespondDto;
import net.sf.jasperreports.engine.*;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {

    @GetMapping("/message")
    public String getMessage(){
        return "Hello world";
    }

    @PostMapping("/greetings")
    public ResponseEntity<RespondDto> createMember(@RequestBody DemoRequest demoRequest){
        RespondDto respondDto = new RespondDto();
        Date date = new Date();
        DateFormat desiredFormat = new SimpleDateFormat("dd MMM YYYY");
        String today = desiredFormat.format(date);
        String subject = "Jakarta " + today + ", Selamat " + demoRequest.getLatarWaktu() +" pelanggan!";
        respondDto.setStatus("success");
        respondDto.setText(subject);
        return ResponseEntity.ok(respondDto);
    }

    @PostMapping("/generatePDF")
    public ResponseEntity<RespondDto> generatePdf(@RequestBody DemoRequest demoRequest) throws JRException, IOException {
        RespondDto respondDto = new RespondDto();
        Date date = new Date();
        DateFormat desiredFormat = new SimpleDateFormat("dd MMM YYYY");
        String today = desiredFormat.format(date);

        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:templates/report1.jrxml");
        String filePath = resource.getFile().getAbsolutePath();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("tanggal", today);
        parameters.put("latarWaktu", demoRequest.getLatarWaktu());
        JasperReport report = JasperCompileManager.compileReport(filePath);
        JasperPrint print = JasperFillManager.fillReport(report,parameters,new JREmptyDataSource());
        JasperExportManager.exportReportToPdfFile(print, "C:\\PDFS\\Greeting "+today+".pdf");
        respondDto.setStatus("success");
        respondDto.setText("File generate in folder C:\\PDFS");
        return ResponseEntity.ok(respondDto);
    }
}
