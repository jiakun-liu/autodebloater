package org.jkliu.debloater.instrument;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jkliu.debloater.core.Config;
import soot.*;
import soot.options.Options;

import java.util.Collections;
import java.util.List;

public class Instrumentor {
    private static final Logger logger = LogManager.getLogger("Instrumentor");

    private static boolean generateAPK = true;

    private static String androidJAR = Config.androidJar.getAbsolutePath();

    public static void instrument(List<BodyTransformer> bodyTransformers, String apk) {
        bodyTransformers.forEach(bodyTransformer -> PackManager.v().getPack("jtp").add(new Transform("jtp.myAnalysis" + bodyTransformer.hashCode(), bodyTransformer)));
        logger.debug("Begin runPacks");
        PackManager.v().runPacks();
        logger.debug("Begin writeOutput");
        PackManager.v().writeOutput();
        logger.debug(apk + " is instrumented");
    }

    public static void initializeSoot(String output, String apk) {

        G.reset();
        Options.v().debug();
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_whole_program(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_multiple_dex(true);
        Options.v().set_include_all(true);

        if (generateAPK) {
            Options.v().set_output_format(Options.output_format_dex);
        }

        // resolve the PrintStream and System soot-classes
        Scene.v().addBasicClass("java.io.PrintStream", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System", SootClass.SIGNATURES);
        Scene.v().addBasicClass("android.util.Log", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.String", SootClass.SIGNATURES);

        Options.v().force_overwrite();
        //    Options.v().set_validate(true);

        Options.v().set_output_dir(output);
        Options.v().set_process_dir(Collections.singletonList(apk));
        Options.v().set_android_jars(androidJAR);

        Scene.v().loadNecessaryClasses();
    }
}
