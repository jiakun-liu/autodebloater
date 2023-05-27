package org.jkliu.debloater.core;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jkliu.debloater.instrument.Instrumentor;
import org.jkliu.debloater.instrument.MethodTransformer;
import org.jkliu.debloater.utils.FileProcessor;
import soot.BodyTransformer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    private static final Logger logger = LogManager.getLogger("Main");

    public static void main(String[] args) {
        logger.debug("Begin");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine parseCMD = parser.parse(getOptions(), args, false);
            analyzeArgs(parseCMD);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        debloating(Config.appName, Config.toDebloatPath);
    }


    private static Options getOptions() {
        Options options = new Options();
        options.addOption("h", false, "-h: Show the help information.");

        options.addOption("name", true, "-name: Set the name of the apk under analysis.");
        options.addOption("apkDir", true, "-apkDir: Set the path to the apk under analysis.");
        options.addOption("toDebloatPath", true, "-toDebloatPath: Set the path the file that contains to removed activities.");
        options.addOption("androidJar", true, "-androidJar: Set the path of android.jar.");
        options.addOption("outDir", true, "-outDir: Set the output folder of the apk.");
        options.addOption("storyDroid", true, "-outDir: Set the folder to the storydroid result.");
        options.addOption("cg", true, "-cg: Set the file that save the cg. If it is set to an empty file, debloating will be performed without slicing.");
        return options;
    }

    private static void analyzeArgs(CommandLine mCmd) {
        if (null == mCmd)
            System.exit(-1);

        if (mCmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.setWidth(120);
            formatter.printHelp("java -jar [jarFile] [options] [-path] [-name] [-outDir] [-toDebloatPath]\n" +
                    "E.g., -path apk\\ -name test.apk -outputDir result -client MainClient", getOptions());
            System.exit(0);
        }

        Config myConfig = Config.getInstance();
        myConfig.androidJar = new File(mCmd.getOptionValue("androidJar", ""));
        myConfig.apkDir = new File(mCmd.getOptionValue("apkDir", ""));
        myConfig.outDir = new File(mCmd.getOptionValue("outDir", ""));
        myConfig.appName = mCmd.getOptionValue("name", "");
        myConfig.toDebloatPath = mCmd.getOptionValue("toDebloatPath", "");
        myConfig.storyDroid = new File(mCmd.getOptionValue("storyDroid", ""));
        myConfig.CGFile = new File(mCmd.getOptionValue("cg", ""));
    }

    private static void debloating(String appName, String toDebloatedClassPath) {
        String packageName = new CTGProcessor(appName).getPackageName();
        HashSet<String> toDebloatClassStrings = new HashSet<>(FileProcessor.readFile(toDebloatedClassPath));
        MethodFinder methodFinder = new MethodFinder(appName, toDebloatClassStrings);
        String apkPath = Config.apkDir.getAbsolutePath() + "/" + appName;
        String debloatedDirPath = new File(Config.toDebloatPath).getParentFile().getAbsolutePath();
        new File(debloatedDirPath).mkdirs();
        long startTime = System.currentTimeMillis();
        System.out.println("APK path: " + apkPath);
        Instrumentor.initializeSoot(debloatedDirPath, apkPath);
        List<BodyTransformer> transformers = new ArrayList<>();
        MethodTransformer e = new MethodTransformer(methodFinder, packageName);
        transformers.add(e);
        Instrumentor.instrument(transformers, apkPath);
        System.out.println(debloatedDirPath + appName);
        Set<String> removedCounter = e.getRemovedCounter();
        Set<String> totalCounter = e.getTotalCounter();
        System.out.println("Removed " + removedCounter + " elements.");
        logger.info("Debloating finished.");
        long endTime = System.currentTimeMillis();
        System.out.println("Debloating time: " + (endTime - startTime) + "ms");
        FileProcessor.write2File(toDebloatedClassPath.replace("config.csv", "removed_methods.csv"),removedCounter);
        FileProcessor.write2File(toDebloatedClassPath.replace("config.csv", "total_methods.csv"),totalCounter);
        FileProcessor.write2File(toDebloatedClassPath.replace("config.csv", "time.txt"),(endTime - startTime));

    }
}

//-apkDir /home/jiakun/Projects/TinyAppAll/UserStudyAPKs -name de.schildbach.wallet_100300.apk -androidJar /home/jiakun/Projects/TinyAppAll/Related/android-platforms -outDir /home/jiakun/Projects/TinyAppAll/TinyApp/out/debloated/de.schildbach.wallet_100300_2023_05_26_23_59_11_938 -toDebloatPath /home/jiakun/Projects/TinyAppAll/TinyApp/out/debloated/de.schildbach.wallet_100300_2023_05_26_23_59_11_938/config.csv -storyDroid /home/jiakun/Projects/TinyAppAll/Related/StoryDroid -cg ""
//-apkDir /home/jiakun/Projects/TinyAppAll/UserStudyAPKs -name de.blinkt.openvpn_159.apk -androidJar /home/jiakun/Projects/TinyAppAll/Related/android-platforms -outDir /home/jiakun/Projects/TinyAppAll/TinyApp/out/debloated/de.blinkt.openvpn_159_2023_05_27_00_48_58_576 -toDebloatPath /home/jiakun/Projects/TinyAppAll/TinyApp/out/debloated/de.blinkt.openvpn_159_2023_05_27_00_48_58_576/config.csv -storyDroid /home/jiakun/Projects/TinyAppAll/Related/StoryDroid -cg /home/jiakun/Projects/TinyAppAll/Related/StoryDroid/main-folder/outputs/de.blinkt.openvpn_159/de.blinkt.openvpn_159_cgs.txt