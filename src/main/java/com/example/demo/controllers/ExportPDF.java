package com.example.demo.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Cleanup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/Export/pdf")
public class ExportPDF {
    @GetMapping(value = "/{name}")
    public void exportPDF(@PathVariable("name") String name, HttpServletResponse response) throws IOException, DocumentException {
        @Cleanup Document document = new Document();

        @Cleanup PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
    }
}
