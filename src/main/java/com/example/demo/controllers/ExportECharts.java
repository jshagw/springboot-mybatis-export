package com.example.demo.controllers;

import com.example.demo.config.EChartsConfig;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.google.gson.Gson;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.ReuseExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.util.resources.cldr.lu.CurrencyNames_lu;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

@Controller
@RequestMapping(value = "/Export/ECharts")
@Slf4j
public class ExportECharts {
    @Autowired
    private EChartsConfig config;

    @GetMapping(value = "/Line/{name}")
    public void exportLine(@PathVariable("name") String name, HttpServletResponse response) {
        String id = UUID.randomUUID().toString();
        String inFile = generateGsonFile(id);
        if ( inFile == "" ) {
            response.setStatus(500);
            return;
        }

        String imgPath = config.getOutputPath() + "/line-" + id + ".png";

        try {
            String cmd = config.getExecPath() + " " + config.getJSPath()
                    + " -infile " + inFile + " -outfile " + imgPath;
            Process process = Runtime.getRuntime().exec(cmd);

            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ( (line = input.readLine()) != null ) {
                log.info(line);
            }

            input.close();

            response.setContentType("image/png");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + URLEncoder.encode(name, "UTF-8")); // 变成附件下载
            response.flushBuffer();

            BufferedInputStream bis = null;
            try {
                File file = new File(imgPath);
                bis = new BufferedInputStream(new FileInputStream(file));
                OutputStream os = response.getOutputStream();
                byte [] buffer = new byte[1024];
                int i = 0;
                while ( (i = bis.read(buffer)) != -1 ) {
                    os.write(buffer, 0, i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if ( null != bis ) {
                    bis.close();
                }
            }

            deleteFile(inFile);
            deleteFile(imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateGsonFile(String id) {
        String [] types = {"邮件营销", "联盟广告", "视频广告"};
        int [][] datas = {
                {120, 130, 124, 135, 100, 150, 140},
                {150, 160, 155, 165, 130, 180, 170},
                {170, 180, 175, 186, 150, 200, 190}
        };
        String title = "电流图";
        String [] valXis = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        GsonOption option = new GsonOption();

        // 大标题、小标题、位置
        option.title().text(title).subtext("电流图").x("left");

        // 提示工具
        option.tooltip().trigger(Trigger.axis); // 在轴上触发提示数据
        // 工具栏
        option.toolbox().show(true).feature(Tool.saveAsImage); // 显示保存为图片

        option.legend(types); // 图例

        CategoryAxis category = new CategoryAxis(); // 轴分类
        category.data(valXis);
        category.boundaryGap(false); // 其实和结束两端空白策略

        // 数据填充
        for ( int i = 0; i < types.length; ++i ) {
            Line line = new Line();
            String type = types[i];
            line.name(type).stack("总量");
            for ( int j = 0; j < datas[i].length; ++j ) {
                line.data(datas[i][j]);
            }
            option.series(line);
        }

        option.xAxis(category); // x轴
        option.yAxis(new ValueAxis()); // y轴

        String jsonPath = config.getOutputPath() + "/" + id + ".json";

        return writeFile(new Gson().toJson(option), jsonPath);
    }

    private static String writeFile(String content, String path) {
        try {
            File file = new File(path); // 相对路径，如果没有则要建立一个新的文件
            if ( !makeSureFileExsit(file) ) {
                return  "";
            }

            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(content); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
        return path;
    }

    private static boolean makeSureFileExsit(File file) {
        try {
            if (!file.exists()) {   //文件不存在则创建文件，先创建目录
                File dir = new File(file.getParent());
                if (!dir.exists() && !dir.mkdirs() || !file.createNewFile()) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    private static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }
}
