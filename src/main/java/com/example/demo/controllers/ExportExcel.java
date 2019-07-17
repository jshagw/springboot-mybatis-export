package com.example.demo.controllers;

import com.example.demo.mybatis.mapper.TestUserMapper;
import com.example.demo.mybatis.model.TestUser;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Controller
@RequestMapping(value = "/Export/excel")
public class ExportExcel {
    @Autowired
    private TestUserMapper testUserMapper;

    @GetMapping(value = "/{name}")
    public void exportExcel(@PathVariable("name") String name, HttpServletResponse response) {
        TestUser user = testUserMapper.selectByPrimaryKey(1);
        if (null == user) {
            response.setStatus(404);
            return;
        }

        // 创建excel
        HSSFWorkbook wk = new HSSFWorkbook();

        // 样式
        HSSFCellStyle style = wk.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 创建sheet
        HSSFSheet sheet = wk.createSheet();
        sheet.setHorizontallyCenter(true);
        sheet.setVerticallyCenter(true);

        // 创建标题行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("L1");
        cell.setCellStyle(style);

        // 插入数据
        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(user.getId());
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(user.getL1());
        cell.setCellStyle(style);

        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + URLEncoder.encode(name, "UTF-8")); // 变成附件下载
            response.flushBuffer();
            wk.write(response.getOutputStream());
            wk.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
