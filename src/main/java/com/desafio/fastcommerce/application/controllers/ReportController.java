package com.desafio.fastcommerce.application.controllers;


import com.desafio.fastcommerce.application.service.ReportService;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrdersDashboardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/admin/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/orders-dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrdersDashboardResponseDTO> dashboard(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
                                                                @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23,59,59);
        return ResponseEntity.ok(reportService.getDashboard(startDateTime, endDateTime));
    }
}
