package com.billlv.codegenerator.common.utils;


import org.apache.velocity.app.VelocityEngine;
import java.util.Properties;

public class VelocityEngineInitializer {

    public static VelocityEngine initVelocityEngine() {
        Properties props = new Properties();
        props.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        props.setProperty("input.encoding", "UTF-8");
        props.setProperty("output.encoding", "UTF-8");

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init(props);
        return velocityEngine;
    }
}
