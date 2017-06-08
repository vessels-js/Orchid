package com.subgraph.orchid.http.ssl;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.util.*;

public class EnforceSslCertificates {
    private static final KeyStore KEY_STORE;
    private static final List<ExportedCertificate> EXPORTED_CERTIFICATES;
    
    static {
        EXPORTED_CERTIFICATES = new ArrayList();
        EXPORTED_CERTIFICATES.add(ExportedCertificate.getInstance("/com/torhttpclient/util/http/post/ssl/", "DSTRootCAX3", "der"));
        EXPORTED_CERTIFICATES.add(ExportedCertificate.getInstance("/com/torhttpclient/util/http/post/ssl/", "StartComCertificationAuthority", "der"));
    }
    
    static {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            Path ksPath = Paths.get(System.getProperty("java.home"),"lib", "security", "cacerts");
//            keyStore.load(new FileInputStream(ksPath.toFile()),"changeit".toCharArray());
//
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            for(ExportedCertificate exportedCertificate : EXPORTED_CERTIFICATES){
//                InputStream caInput = null;
//                try{
//                    caInput = new BufferedInputStream(EnforceSslCertificates.class.getResourceAsStream(exportedCertificate.path+exportedCertificate.name+"."+exportedCertificate.extension));
//                    Certificate crt = cf.generateCertificate(caInput);
//                    keyStore.setCertificateEntry(exportedCertificate.name, crt);
//                } finally{
//                    if(caInput!=null){
//                        caInput.close();
//                    }
//                }
//            }
        } catch (Exception e) {
            //Unable to add key stores.
            //throw new RuntimeException(e);
        } finally{
            KEY_STORE = keyStore;
        }
    }
    
    public static SSLContext getSSLContext(){
        try{
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(KEY_STORE);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            //SSLContext.setDefault(sslContext);
            return sslContext;
        } catch(Exception e){
            return null;
        }
    }
    
    private static class ExportedCertificate{
        private final String extension;
        private final String name;
        private final String path;
        
        private ExportedCertificate(String path, String name, String extension){
            this.extension = extension;
            this.name = name;
            this.path = path;
        }
        
        public static ExportedCertificate getInstance(String path, String name, String extension){
            return new ExportedCertificate(path, name, extension);
        }
    }
}