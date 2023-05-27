package com.jkliu.tinyapp.core;

import com.jkliu.tinyapp.utils.FileProcessor;
import com.jkliu.tinyapp.utils.JavaProcessCall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

public class CTGProcessor {
    private static final Logger logger = LogManager.getLogger();
    String appName;
    private File appResultDir;

    public ArrayList<String> getATG() {
        File atgFile = new File(appResultDir, appName.replace(".apk", "") + "_atgs.txt");
        if (atgFile.exists()) {
            ArrayList<String> strings = FileProcessor.readFile(atgFile.getAbsolutePath());
            if (strings.size() > 0) {
                return strings;
            }
        }
        String cmd = Config.pythonPath + " " + Config.storyDroid.getAbsolutePath() + "/code-v2.0/main_tinyapp.py '" + Config.storyDroid.getAbsolutePath() + "/main-folder/' " + Config.apkDir.getAbsolutePath() + " '" + Config.storyDroid.getAbsolutePath() + "/main-folder/log.csv' " + appName;
        JavaProcessCall.execute(cmd);
        if (atgFile.exists()) {
            ArrayList<String> strings = FileProcessor.readFile(atgFile.getAbsolutePath());
            if (strings.size() > 0) {
                return strings;
            }
        }
        return null;
    }

    public CTGProcessor(String appName) {
        // This appName is used across the project, rather than the packageName
        this.appName = appName;
        this.appResultDir = Config.getAppStoryDroidResultDir(appName);
    }

    public static void main(String[] args) {
        String appName = "de.blinkt.openvpn.apk";
        CTGProcessor ctgGenerator = new CTGProcessor(appName);
        ArrayList<String> atgs = ctgGenerator.getATG();
        System.out.println(atgs);
    }

}
