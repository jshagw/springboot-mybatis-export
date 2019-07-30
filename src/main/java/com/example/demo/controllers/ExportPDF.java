package com.example.demo.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Cleanup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping(value = "/Export/pdf")
public class ExportPDF {
    @GetMapping(value = "/{name}")
    public void exportPDF(@PathVariable("name") String name, HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition",
                "attachment;filename=" + URLEncoder.encode(name, "UTF-8")); // 变成附件下载
        //response.flushBuffer();

        @Cleanup Document document = new Document();

        @Cleanup PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // 中文要采用中文字体，否则不显示
        BaseFont bfCN = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font blueFont = new Font(bfCN);
        blueFont.setColor(BaseColor.BLUE);

        document.add(new Paragraph("Hello World! 你好，世界！", blueFont));

        List list = new List(List.ORDERED);
        list.add("Item one");
        list.add(new ListItem("Item two"));

        document.add(list);
    }
}
