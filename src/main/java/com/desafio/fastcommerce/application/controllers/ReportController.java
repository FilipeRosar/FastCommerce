package com.desafio.fastcommerce.application.controllers;


import com.desafio.fastcommerce.application.service.PdfReportService;
import com.desafio.fastcommerce.application.service.ReportService;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrdersDashboardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/admin/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final PdfReportService pdfReportService;

    @GetMapping("/orders-dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrdersDashboardResponseDTO> dashboard(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
                                                                @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23,59,59);
        return ResponseEntity.ok(reportService.getDashboard(startDateTime, endDateTime));
    }
    @GetMapping("/export/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportPdf(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
                                            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) throws IOException {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23,59,59);

        byte[] pdf = pdfReportService.generateDashboardPdf(startDateTime,endDateTime);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fastcommerce-report.pdf")
                .contentType(MediaType.APPLICATION_PDF).body(pdf);
    }
}
