package com.jkliu.tinyapp.core;

import com.jkliu.tinyapp.utils.ApkUtils;
import com.jkliu.tinyapp.utils.JavaProcessCall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Debloater {

    private static final Logger logger = LogManager.getLogger();
    String appName;
    static String debloaterJarAbsolutePath = Config.debloaterJar.getAbsolutePath();
    static String androidJarPath = Config.androidJar.getAbsolutePath();
    static String apkDir = Config.apkDir.getAbsolutePath();
    static String outDir = Config.outDir.getAbsolutePath();
    private File appResultDir;

    public Debloater(String appName) {
        this.appName = appName;
        this.appResultDir = Config.getAppResultDir(appName);
    }

    public void debloating(String timeHash) {
        String debloatedDirPath = Config.outDir.getAbsolutePath() + "/debloated/" + appName.replace(".apk", "") + "_" + timeHash + "/";
        File userSpecificationFile = new File(debloatedDirPath, "config.csv");
        File debloatedAPK = new File(debloatedDirPath, appName);
        if (debloatedAPK.exists()) {
            return;
        }
        String cgPath = Config.getAppStoryDroidResultDir(appName) + "/" + appName.replace(".apk", "") + "_cgs.txt";
        String cmd = "java -jar " + debloaterJarAbsolutePath + " -apkDir " + apkDir + " -name " + appName + " -androidJar " + androidJarPath + " -outDir " + outDir + " -toDebloatPath " + userSpecificationFile.getAbsolutePath() + " -storyDroid " + Config.storyDroid.getAbsolutePath() + " -cg " + cgPath;
        JavaProcessCall.execute(cmd);
        ApkUtils.sign(debloatedAPK.getAbsolutePath());
    }
}

//java -jar /home/jiakun/Projects/TinyAppAll/TinyApp/debloater/target/debloater-0.0.1-SNAPSHOT.jar -apkDir /home/jiakun/Projects/TinyAppAll/UserStudyAPKs -name com.amaze.filemanager_77.apk -androidJar /home/jiakun/Projects/TinyAppAll/Related/android-platforms -outDir /home/jiakun/Projects/TinyAppAll/TinyApp/out -toDebloatPath /home/jiakun/Projects/TinyAppAll/TinyApp/out/debloated/com.amaze.filemanager_77_2023_05_27_13_48_43_455/config.csv -storyDroid /home/jiakun/Projects/TinyAppAll/Related/StoryDroid -cg /home/jiakun/Projects/TinyAppAll/TinyApp/out/com.amaze.filemanager_77.apk/com.amaze.filemanager_77_cgs.txt

