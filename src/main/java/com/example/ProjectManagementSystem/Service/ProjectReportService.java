package com.example.ProjectManagementSystem.Service;


import com.example.ProjectManagementSystem.Entity.Project;
import com.example.ProjectManagementSystem.Repository.ProjectRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectReportService {

    @Autowired
    private ProjectRepository projectRepository;

    public ResponseEntity<byte[]> exportReport(String reportFromat) throws IOException, JRException {

        String path = "C:\\Users\\Public\\Documents";
        List<Project> pa = projectRepository.findAllByActiveTrue();


        File file = ResourceUtils.getFile("classpath:pms_report.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(pa);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Createdby", "Bishowjit Saha");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);

        if (reportFromat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\pms_report.html");
        }

        if (reportFromat.equalsIgnoreCase("pdf")) {

            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\pms_report.pdf");

        }
        String reportPath = path + "\\pms_report.pdf";
        byte[] pdfBytes = getFileAsByteArray(reportPath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "report.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    public byte[] getFileAsByteArray(String filePath) throws IOException, IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }


}
