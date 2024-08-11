package com.example.ProjectManagementSystem.Controller;

import com.example.ProjectManagementSystem.Service.ProjectReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class ProjectReportController    {

    @Autowired
    private ProjectReportService projectReportService;

    @GetMapping("project/report/{format}")
    public ResponseEntity<byte[]> generateReport(@PathVariable String format) throws IOException, JRException {
        System.out.println("-------Pdf is generated------");
        return projectReportService.exportReport(format);

    }
}
