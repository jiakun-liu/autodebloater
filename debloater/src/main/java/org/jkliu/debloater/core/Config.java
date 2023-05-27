package org.jkliu.debloater.core;

import java.io.File;

public class Config {

    public static String appName;
    public static String toDebloatPath;
    public static File androidJar;
    public static File apkDir;
    public static File outDir;
    public static File storyDroid;
    public static File CGFile;

    public static File getAppResultDir(String appName) {
        return new File(outDir, appName.substring(0, appName.length() - 4));
    }

    public static File getAppStoryDroidResultDir(String appName) {
        return new File(storyDroid, "main-folder/outputs/" + appName.replace(".apk", ""));
    }

    private static class SingletonInstance {
        private static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {
        return SingletonInstance.INSTANCE;
    }

}
