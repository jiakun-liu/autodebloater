package org.jkliu.debloater.core;

import org.jkliu.debloater.utils.FileProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CG {
    String appName;

    public CG(String appName) {
        this.appName = appName;
        File CGFile = Config.CGFile;
        ArrayList<String> cgEdges = FileProcessor.readFile(CGFile.getAbsolutePath());
        for (String cgEdge : cgEdges) {
            String[] split = cgEdge.split("-->");
            String source = "<" + split[0] + ">";
            String destination = "<" + split[1] + ">";
            addCGEdge(source, destination);
        }
    }

    private HashMap<String, HashSet> methodOfClass = new HashMap<>();
    private HashMap<String, HashSet> sourcesOfKey = new HashMap<>();
    private HashMap<String, HashSet> targetsOfKey = new HashMap<>();

    private void addCGEdge(String source, String target) {
        if (!sourcesOfKey.containsKey(target)) {
            sourcesOfKey.put(target, new HashSet());
        }
        if (!targetsOfKey.containsKey(source)) {
            targetsOfKey.put(source, new HashSet());
        }
        sourcesOfKey.get(target).add(source);
        targetsOfKey.get(source).add(target);
        addMethodsToClass(source);
        addMethodsToClass(target);
    }

    private void addMethodsToClass(String methodName) {
        String className = methodName.substring(1, methodName.indexOf(": "));
        if (!methodOfClass.containsKey(className)) {
            methodOfClass.put(className, new HashSet());
        }
        methodOfClass.get(className).add(methodName);
    }

    public HashSet<String> getMethodsOfClass(String className) {
        return methodOfClass.get(className);
    }

    public HashSet getSources(String key) {
        return this.sourcesOfKey.get(key);
    }

    public HashSet getTargets(String key) {
        return this.targetsOfKey.get(key);
    }


}
