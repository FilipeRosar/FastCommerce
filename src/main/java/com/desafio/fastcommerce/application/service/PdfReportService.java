package com.desafio.fastcommerce.application.service;


import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrdersByStatusDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrdersDashboardResponseDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.TopSellingProductDTO;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PdfReportService {
    private final ReportService reportService;

    public byte[] generateDashboardPdf(LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        OrdersDashboardResponseDTO dashboard = reportService.getDashboard(startDate, endDate);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);

        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf);

        document.add(
          new Paragraph("FASTCOMMERCE DASHBOARD REPORT").setUnderline().simulateBold().setFontSize(10)
        );
        document.add(
                new Paragraph("Periodo: " + startDate +" até " + endDate )
        );
        document.add(
                new Paragraph("Receita Total: R$ " + dashboard.totalRevenue())
        );
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Pedidos por Status: ").setUnderline().simulateBold());
        Table statusTable = new Table(2);

        statusTable.addHeaderCell("Status");
        statusTable.addHeaderCell("Quantidade");

        for (OrdersByStatusDTO item : dashboard.ordersByStatus()){
            statusTable.addCell(item.status().name());
            statusTable.addCell(item.total().toString());
        }
        document.add(statusTable);
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Top 10 Produtos Vendidos").simulateBold());

        Table productsTable = new Table(2);

        productsTable.addHeaderCell("Produto");
        productsTable.addHeaderCell("Quantidade vendida");

        for(TopSellingProductDTO product : dashboard.topProducts()){
            productsTable.addCell(product.productName());
            productsTable.addCell(String.valueOf(product.totalSold()));
        }
        document.add(productsTable);
        document.close();
        return outputStream.toByteArray();

    }
}
