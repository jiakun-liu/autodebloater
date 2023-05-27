package org.jkliu.debloater.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JavaProcessCall {
    private static final Logger logger = LogManager.getLogger("JavaProcessCall");

    public static int execute(String command, String... args) {
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
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.debug(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Fail to conduct the command : " + commandArgs);
            return exitVal;
        }
        logger.debug("Succeed to conduct the command : " + commandArgs);
        return exitVal;
    }
}
