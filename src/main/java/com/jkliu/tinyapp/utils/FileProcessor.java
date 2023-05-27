package com.jkliu.tinyapp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class FileProcessor {
    private static final Logger logger = LogManager.getLogger();
    public static ArrayList<String> readFile(String fileName) {
        ArrayList<String> content = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.add(line);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return content;
    }

    public static void write2File(String fileName, Object content) {
        try {
            File file = new File(fileName);
            File parentFile = file.getParentFile();
            parentFile.mkdirs();
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(content.toString());
            myWriter.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void append2File(String fileName, Object content) {
        try {
            File file = new File(fileName);
            File parentFile = file.getParentFile();
            parentFile.mkdirs();
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.append(content.toString());
            myWriter.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void write2File(String fileName, Collection collection) {
        try {
            File file = new File(fileName);
            File parentFile = file.getParentFile();
            parentFile.mkdirs();
            FileWriter myWriter = new FileWriter(fileName);
            for (Object o : collection) {
                myWriter.write(o.toString() + '\n');
            }
            myWriter.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void removeFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }

    public static void renameFile(String from, String to) {
        File fromFile = new File(from);
        File toFile = new File(to);
        while (toFile.exists()) {
            toFile.delete();
        }
        fromFile.renameTo(toFile);
    }

    public static void copyFile(String from, String to) {
        File fromFile = new File(from);
        File toFile = new File(to);
        try {
            InputStream in = new FileInputStream(fromFile);
            OutputStream out = new FileOutputStream(toFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
