package com.demo;

import com.subgraph.orchid.logging.SysLog;

public class ApplicationProperties {
    public static boolean useCompressionWhenDownloadingDirectoryData(){
        return true;
    }
    
    public static SysLog loggingThreshold(){
        return SysLog.INFORMATIONAL;
    }
}
