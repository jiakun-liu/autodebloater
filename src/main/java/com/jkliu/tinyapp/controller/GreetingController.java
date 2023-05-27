package com.jkliu.tinyapp.controller;

import com.jkliu.tinyapp.core.Config;
import com.jkliu.tinyapp.utils.AaptTool;
import com.jkliu.tinyapp.utils.JavaProcessCall;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Controller
public class GreetingController {
    private static final Logger logger = LogManager.getLogger("GreetingController");


    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) {
        File apkDir = Config.apkDir;
        apkDir.mkdirs();
        String originalFilename = multipartFile.getOriginalFilename();
        File saveToFile = new File(apkDir, originalFilename);
        if (!saveToFile.exists()) {
            try {
                multipartFile.transferTo(saveToFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String appName = AaptTool.getNewName(saveToFile.getAbsolutePath()) + ".apk";
        File newNameFile = new File(apkDir, appName);
        if (!newNameFile.exists()) {
            Path source = Paths.get(saveToFile.getAbsolutePath());
            try {
                Files.move(source, source.resolveSibling(appName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String appStoryDroidResultDir = Config.getAppStoryDroidResultDir(appName).getAbsolutePath();
        File screenShotDir = new File(appStoryDroidResultDir, "screenshots");
        if (!screenShotDir.exists()) {
            String cmd = Config.pythonPath + " " + Config.storyDroid.getAbsolutePath() + "/code-v2.0/gate.py" + " " + Config.storyDroid.getAbsolutePath() + "/main-folder/" + " " + Config.apkDir.getAbsolutePath() + " " + appName;
            JavaProcessCall.execute(cmd);
        }
        try {
//            response.sendRedirect("/");
            response.sendRedirect("/ATG?appName=" + appName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/")
    public String greeting(Model model) {
        File storyDroidOutPuts = new File(Config.storyDroid.getAbsolutePath(), "main-folder/outputs");
        File[] files = storyDroidOutPuts.listFiles();
        ArrayList<String> extractedApplications = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                extractedApplications.add(file.getName());
            }
        }
        model.addAttribute("extractedApplications", extractedApplications);
        return "index";
    }
}
