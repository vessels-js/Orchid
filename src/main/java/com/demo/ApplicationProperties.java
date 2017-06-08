package com.demo;

import com.subgraph.orchid.logging.SysLog;

public class ApplicationProperties {
    public static boolean useCompressionWhenDownloadingDirectoryData(){
        return true;
    }
    
    public static SysLog loggingThreshold(){
        return SysLog.INFORMATIONAL;
    }
    
    public static String getName(){
        return "Orchid";
    }
    
    public static String getVersion(){
        return "1.0.0";
    }
}
