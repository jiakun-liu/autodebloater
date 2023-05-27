package org.jkliu.debloater.utils;

import soot.FastHierarchy;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

public class LifecycleMethodUtil {
    public static boolean isLifeCycleMethod(SootMethod sm, FastHierarchy fastHierarchy) {

        SootClass activityClass = Scene.v().getSootClass("android.app.Activity");
        SootClass serviceClass = Scene.v().getSootClass("android.app.Service");
        // SootClass contentProviderClass =
        // Scene.v().getSootClass("android.content.ContentProvider");
        // SootClass broadcastClass =
        // Scene.v().getSootClass("android.content.BroadcastReceiver");
        String smSignature = sm.getName();

        SootClass declaringClass = sm.getDeclaringClass();
//		MyLogger.log(smSignature);

        if (fastHierarchy.isSubclass(declaringClass, activityClass)) {
            /**
             * onCreate onStart onResume
             * onRestart onPause onStop
             * onDestroy
             */
            switch (smSignature) {
                case "onCreate":
                case "onStart":
                case "onResume":
                case "onRestart":
                case "onPause":
                case "onStop":
                case "onDestroy": {
                    return true;
                }
                default: {
                    return false;
                }
            }
        } else if (fastHierarchy.isSubclass(declaringClass, serviceClass)) {
            /**
             * onStartCommand, onBind, onCreate, onDestroy
             */
            switch (smSignature) {
                case "onStartCommand":
                case "onBind":
                case "onCreate":
                case "onDestroy": {
                    return true;
                }
                default: {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
