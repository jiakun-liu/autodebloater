package org.jkliu.debloater.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashSet;

public class MethodFinder {
    private static final Logger logger = LogManager.getLogger("MethodFinder");
    String appName;
    HashSet<String> toDebloatClasses;
    HashSet<String> toDebloatMethods = new HashSet<>();

    public MethodFinder(String appName, HashSet<String> toDebloatedClasses) {
        this.appName = appName;
        this.toDebloatClasses = toDebloatedClasses;
    }

    public HashSet<String> getToDebloatClasses() {
        logger.info("Number of classes to debloat: " + toDebloatClasses.size());
        return toDebloatClasses;
    }

    public HashSet<String> getToDebloatMethods() {
        File cgFile = Config.CGFile;
        if (cgFile.exists() && cgFile.length() > 0) {
            logger.info("CG file exists, loading...");
            CG cg = new CG(appName);
            for (String toDebloatClass : toDebloatClasses) {
                toDebloatMethods.addAll(cg.getMethodsOfClass(toDebloatClass));
            }
            slicing(cg);
        }
        logger.info("Number of methods to debloat: " + toDebloatMethods.size());
        return toDebloatMethods;
    }

    public void slicing (CG cg) {
        HashSet<String> worklist = new HashSet<>(toDebloatMethods);
        while (!worklist.isEmpty()) {
            HashSet<String> newAdded = new HashSet<>();
            for (String toDebloatMethod : worklist) {
                HashSet<String> targets = cg.getTargets(toDebloatMethod);
                if (targets == null) {
                    continue;
                }
                for (String target : targets) {
                    cg.getSources(target).remove(toDebloatMethod);
                    if (cg.getSources(target).isEmpty()) {
                        newAdded.add(target);
                    }
                }
            }
            worklist = newAdded;
            toDebloatMethods.addAll(newAdded);
        }
    }

}