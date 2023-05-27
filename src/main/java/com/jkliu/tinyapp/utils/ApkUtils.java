package com.jkliu.tinyapp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jkliu.tinyapp.core.Config;

import java.io.File;

public class ApkUtils {
    private static final Logger logger = LogManager.getLogger("ApkUtils");
    public static void sign(String inputApkFileRelativePath) {
        File inputAPKFile = new File(inputApkFileRelativePath);
        String inputApkFileAbsPath = inputAPKFile.getAbsolutePath();
        String alignedAPkFile = inputApkFileAbsPath.replace(".apk", "_aligned_tmp.apk");
        String outputAPKFile = inputApkFileAbsPath.replace(".apk", "_signed.apk");
        String tempFile = inputApkFileAbsPath.replace(".apk", "_signed.apk.idsig");

        JavaProcessCall.execute(Config.zipalign.getAbsolutePath(), "-f", "4", inputApkFileAbsPath, alignedAPkFile);
        JavaProcessCall.execute(Config.apksigner.getAbsolutePath(), "sign", "--ks", Config.keystoreFile.getAbsolutePath(), "--ks-pass", "pass:60948491",
                "--out", outputAPKFile, alignedAPkFile);
        FileProcessor.removeFile(alignedAPkFile);
        FileProcessor.removeFile(tempFile);
        FileProcessor.renameFile(outputAPKFile, inputApkFileAbsPath);
        logger.info("Succeed to sign " + inputApkFileAbsPath);
    }
}
