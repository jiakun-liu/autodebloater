package org.jkliu.debloater.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jkliu.debloater.utils.FileProcessor;

import java.io.File;
import java.util.ArrayList;

public class CTGProcessor {
    private static final Logger logger = LogManager.getLogger("CTGProcessor");
    String appName;
    private File appResultDir;

    public CTGProcessor(String appName) {
        // This appName is used across the project, rather than the packageName
        this.appName = appName;
        this.appResultDir = Config.getAppStoryDroidResultDir(appName);
    }

    public String getPackageName() {
        File defined_pkg_nameFile = new File(appResultDir, "used_pkg_name.txt");
        ArrayList<String> lines = FileProcessor.readFile(defined_pkg_nameFile.getAbsolutePath());
        return lines.get(0);
    }


}
