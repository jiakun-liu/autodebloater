package com.jkliu.tinyapp.core;

import com.jkliu.tinyapp.utils.FileProcessor;
import com.jkliu.tinyapp.utils.JavaProcessCall;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DotFileGenerator {

    public static void main(String[] args) {
        String appName = "de.blinkt.openvpn.apk";
        generateATG(appName);
    }

    public static List<String> generateATG(String appName) {
        CTGProcessor ctgProcessor = new CTGProcessor(appName);
        ArrayList<String> atg = ctgProcessor.getATG();
        String appStoryDroidResultDir = Config.getAppStoryDroidResultDir(appName).getAbsolutePath();
        File screenShotDir = new File(appStoryDroidResultDir, "screenshots");
        if (!screenShotDir.exists()) {
            String cmd = Config.pythonPath + " " + Config.storyDroid.getAbsolutePath() + "/code-v2.0/gate.py" + " " + Config.storyDroid.getAbsolutePath() + "/main-folder/" + " " + Config.apkDir.getAbsolutePath() + " " + appName;
            JavaProcessCall.execute(cmd);
        }
        List<String> recordedActivities = Arrays.asList(screenShotDir.list());
        recordedActivities.replaceAll(s -> s.substring(0, s.lastIndexOf(".")));
        File dotFile = new File(Config.getAppResultDir(appName), "TinyAppResult/ATG.dot");
        if (dotFile.exists()) {
            return recordedActivities;
        }
        ArrayList<String> writeToBuffer = new ArrayList<>();
        writeToBuffer.add("digraph G {\n" +
                "\tcompound=true;\n" +
                "\trankdir=LR;\n" +
                "\tlabel=\"App Name: " + appName + "\";\n" +
                "\tlabelloc=t;\n" +
                "\tlabeljust=l;\n" +
                "\tfontsize=100;\n" +
                "\tfontname=\"Times-Roman\";\n" +
                "\tfontcolor=black;\n" +
                "\tbgcolor=white;\n" +
//                "\tnode [shape=box,style=filled,fillcolor=white,fontname=\"Times-Roman\",fontsize=20];\n" +
                "\tedge [fontname=\"Times-Roman\",fontsize=20];");
        for (String activity : recordedActivities) {
            writeToBuffer.add("\t" + activity.substring(activity.lastIndexOf(".") + 1) + " [image=\"" + appStoryDroidResultDir + "/screenshots/" + activity + ".png\",labelloc=c,fontsize=70,fontname=\"Times-Roman\",fontcolor=black,shape=box,style=filled,fillcolor=white,imagepos=tc];");
        }
        for (String line : atg) {
            String[] split = line.split("-->");
            String from = split[0];
            if (!recordedActivities.contains(from)) {
                continue;
            }
            String to = split[1];
            if (!recordedActivities.contains(to)) {
                continue;
            }
            writeToBuffer.add("\t" + from.substring(from.lastIndexOf(".") + 1) + " -> " + to.substring(to.lastIndexOf(".") + 1) + " [penwidth=10];");
        }
        writeToBuffer.add("}");
        FileProcessor.write2File(dotFile.getAbsolutePath(), writeToBuffer);
        JavaProcessCall.execute("dot -Tpng " + dotFile.getAbsolutePath() + " -o " + Config.getAppResultDir(appName) + "/TinyAppResult/ATG.png");
        return recordedActivities;
    }


}
