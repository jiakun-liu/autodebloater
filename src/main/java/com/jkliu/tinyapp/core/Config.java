package com.jkliu.tinyapp.core;

import com.jkliu.tinyapp.utils.FileProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Config {
    private static final Logger logger = LogManager.getLogger();
    public static String androidHome;
    public static String baseDir;
    public static File iccBot;
    public static File debloaterJar;
    public static File keystoreFile;
    public static File androidJar;
    public static File apkDir;
    public static File outDir = new File("out/");
    public static File adb;
    public static File emulator;
    public static File zipalign;
    public static File apksigner;
    public static File aapt;
    public static String pythonPath;
    public static File storyDroid;
    private static HashMap<String, String> hash2apk = new HashMap<>();
    private static HashMap<String, String> apk2hash = new HashMap<>();

    public static File getAppResultDir(String appName) {
        return new File(outDir, appName);
    }

    public static File getAppStoryDroidResultDir(String appName) {
        return new File(storyDroid, "main-folder/outputs/" + appName.replace(".apk", ""));
    }

    public static void updateHash(String apkName, String hash) {
        hash2apk.put(hash, apkName);
        apk2hash.put(apkName, hash);
        saveHash();
    }

    public static String getHash(String apkName) {
        return apk2hash.get(apkName);
    }

    public static String getApkName(String hash) {
        return hash2apk.get(hash);
    }

    private static void initHash() {
        ArrayList<String> settings = FileProcessor.readFile("out/processedapps.txt");
        for (String setting : settings) {
            String[] settingPair = setting.split("=");
            hash2apk.put(settingPair[0], settingPair[1]);
            apk2hash.put(settingPair[1], settingPair[0]);
        }
    }

    private static void saveHash() {
        ArrayList<String> settings = new ArrayList<>();
        for (String hash : hash2apk.keySet()) {
            settings.add(hash + "=" + hash2apk.get(hash));
        }
        FileProcessor.write2File("out/processedapps.txt", settings);
    }

    public static void init() {
        ArrayList<String> settings = FileProcessor.readFile("config/system.settings");
        HashMap<String, String> settingMap = new HashMap<>();
        for (String setting : settings) {
            logger.info(setting);
            String[] settingPair = setting.split("=");
            settingMap.put(settingPair[0], settingPair[1]);
        }
        androidHome = settingMap.get("androidHome");
        baseDir = settingMap.get("baseDir");
        androidJar = new File(settingMap.get("androidJar"));
        zipalign = new File(settingMap.get("zipalign"));
        apksigner = new File(settingMap.get("apksigner"));
        aapt = new File(settingMap.get("aapt"));
        pythonPath = settingMap.get("python");
        storyDroid = new File(settingMap.get("storydroid"));

        iccBot = new File(baseDir, "TinyApp/lib/ICCBot-jar-with-dependencies.jar");
        debloaterJar = new File(baseDir, "TinyApp/debloater/target/debloater-0.0.1-SNAPSHOT.jar");
        keystoreFile = new File(baseDir, "TinyApp/Apksigner");
        apkDir = new File(baseDir, "UserStudyAPKs/");
        logger.info("androidHome: " + androidHome);
        adb = new File(androidHome, "Sdk/platform-tools/adb");
        logger.info("adb: " + adb.getAbsolutePath());
        emulator = new File(androidHome, "Sdk/emulator/emulator");
        logger.info("emulator: " + emulator.getAbsolutePath());
        initHash();
    }


}
