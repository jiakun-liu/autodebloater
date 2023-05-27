package com.jkliu.tinyapp.utils;

import com.jkliu.tinyapp.core.Config;

public class AaptTool {

    public static String getNewName(String apkPath) {
        String packageName = "";
        String versionCode = "";
        String aapt = Config.aapt.getAbsolutePath();
        String definedPackageCommand = aapt + " dump badging " + apkPath;
        String execute = JavaProcessCall.execute(definedPackageCommand);
        String[] lines = execute.split("\n");
        for (String line : lines) {
            if (line.contains("package: name=") && line.contains("versionCode=")) {
                String[] split = line.split(" ");
                for (String s : split) {
                    if (s.startsWith("name=")) {
                        packageName = s.split("=")[1].replace("\'", "");
                        System.out.println("packageName : " + packageName);
                    }
                    if (s.startsWith("versionCode=")) {
                        versionCode = s.split("=")[1].replace("\'", "");
                        System.out.println("versionCode : " + versionCode);
                    }
                }
            }
            break;
        }
        return packageName + "_" + versionCode;
    }

    public static void main(String[] args) {
        Config.init();
        String apkPath = "/home/jiakun/Projects/TinyAppAll/UserStudyAPKs/com.adam.aslfms_48.apk";
        String newName = getNewName(apkPath);
        System.out.println(newName);
    }
}
