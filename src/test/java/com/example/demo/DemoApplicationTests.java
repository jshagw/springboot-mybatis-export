package com.example.demo;

import org.jodconverter.DocumentConverter;
import org.jodconverter.office.OfficeException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    private DocumentConverter documentConverter;

//    @Autowired
//    private TestUserMapper testUserMapper;

    @Test
    public void contextLoads() {
//        TestUser user = testUserMapper.selectByPrimaryKey(1);
//        System.out.println("L1= " + user.getL1());
    }

    @Test
    public void testExcel2pdf() throws OfficeException {
        excel2pdf("F:\\temp\\office\\test.xlsx", "F:\\temp\\office\\test.pdf");
    }

    private void excel2pdf(String src, String dst) throws OfficeException {
        documentConverter.convert(new File(src)).to(new File(dst)).execute();
    }
}
