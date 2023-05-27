package com.jkliu.tinyapp.controller;

import com.jkliu.tinyapp.core.Config;
import com.jkliu.tinyapp.core.Debloater;
import com.jkliu.tinyapp.core.DotFileGenerator;
import com.jkliu.tinyapp.utils.FileProcessor;
import com.jkliu.tinyapp.utils.TimeUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Controller
public class ATGController {
    private static final Logger logger = LogManager.getLogger();

    @GetMapping("/ATG")
    public String upload(@RequestParam(value = "appName") String appName, Model model, HttpServletResponse response) {
        Cookie appNameCookie = new Cookie("appName", appName);
        appNameCookie.setPath("/");
        response.addCookie(appNameCookie);
        String appResultDir = Config.getAppStoryDroidResultDir(appName).getAbsolutePath();
        File ATGFile = new File(Config.getAppResultDir(appName), "TinyAppResult/ATG.png");
        List<String> activities = DotFileGenerator.generateATG(appName);
        model.addAttribute("activityNames", activities);
        String ATGPath = ATGFile.getAbsolutePath();
        model.addAttribute("imagePath", ATGPath.substring(ATGPath.lastIndexOf("out") + 4));
        model.addAttribute("appName", appName);
        return "ATG";
    }

    @RequestMapping("/ATG/selectActivity")
    public String selectActivity(@CookieValue(value = "appName") String appName,
            @RequestParam("activity") String[] activity, Model model, HttpServletResponse response) {
        String timeHash = TimeUtil.getCurrentLocalDateTimeStamp();
        String debloatedDirPath = Config.outDir.getAbsolutePath() + "/debloated/"
                + appName.replace(".apk", "") + "_" + timeHash + "/";
        File userSpecificationFile = new File(debloatedDirPath, "config.csv");
        List<String> list = Arrays.asList(activity);
        FileProcessor.write2File(userSpecificationFile.getAbsolutePath(), list);
        new Debloater(appName).debloating(timeHash);
        try {
            response.sendRedirect("/debloated?time=" + timeHash);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/debloated")
    public String download(@CookieValue(value = "appName") String appName, @RequestParam("time") String timeHash,
            Model model) {
        String debloatedDirPath = "out/debloated/" + appName.replace(".apk", "") + "_" + timeHash + "/"
                + appName.replace(".apk", "") + ".apk";
        model.addAttribute("debloatedDirPath", "/download?filename=" + debloatedDirPath);
        logger.info("debloatedDirPath: " + debloatedDirPath);
        return "debloated";
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("filename") String filename) throws IOException {
        String filepath = Config.baseDir + "/TinyApp/" + filename;
        File file = new File(filepath);
        InputStream in = new FileInputStream(file);
        byte[] body = new byte[in.available()];
        in.read(body);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Content-Disposition", "attchement;filename=" + file.getName());
        HttpStatus statusCode = HttpStatus.OK;
        in.close();
        ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
        return entity;
    }

}
