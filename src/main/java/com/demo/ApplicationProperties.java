package com.demo;

import com.subgraph.orchid.logging.SysLog;

public class ApplicationProperties {
    private static final String NAME = "Orchid";
    private static final String VERSION = "1.0.0";
    private static SysLog LOGGING_THRESHOLD = SysLog.INFORMATIONAL;
    private static boolean USE_COMPRESSION = true;
    
    public static String getName(){
        return NAME;
    }
    
    public static String getVersion(){
        return VERSION;
    }
    
    public static SysLog getLoggingThreshold(){
        return LOGGING_THRESHOLD;
    }
    
    public static void setLoggingThreshold(SysLog loggingThreshold){
        LOGGING_THRESHOLD = loggingThreshold;
    }
    
    public static boolean getUseCompression(){
        return USE_COMPRESSION;
    }
    
    public static void setUseCompression(boolean useCompression){
        USE_COMPRESSION = useCompression;
    }
}
