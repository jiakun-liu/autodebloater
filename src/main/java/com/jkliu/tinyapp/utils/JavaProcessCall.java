package com.jkliu.tinyapp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JavaProcessCall {
    private static final Logger logger = LogManager.getLogger();

    public static String execute(String command, String... args) {
        int exitVal = -10086;
        ProcessBuilder processBuilder = new ProcessBuilder();
        ArrayList<String> commandArgs = new ArrayList<>();
        String[] commandSplits = command.split(" ");
        for (String commandSplit : commandSplits) {
            commandArgs.add(commandSplit);
        }
        for (String arg : args) {
            commandArgs.add(arg);
        }
        String argString = String.join(" ", commandArgs);
        logger.info(argString);
        processBuilder.command(commandArgs);
        String result = "";
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
                logger.info(line);
            }
        } catch (Exception e) {
            logger.error("Fail to conduct the command : " + commandArgs);
            logger.error(e.getMessage());
            return result + exitVal;
        }
        logger.info("Succeed to conduct the command : " + commandArgs);
        return result;
    }

    public static void executeNoWait(String command, String... args) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        ArrayList<String> commandArgs = new ArrayList<>();
        String[] commandSplits = command.split(" ");
        for (String commandSplit : commandSplits) {
            commandArgs.add(commandSplit);
        }
        for (String arg : args) {
            commandArgs.add(arg);
        }
        String argString = String.join(" ", commandArgs);
        logger.info(argString);
        processBuilder.command(commandArgs);
        try {
            processBuilder.start();
        } catch (Exception e) {
            logger.error("Fail to conduct the command : " + commandArgs);
            logger.error(e.getMessage());
        }
    }
}
