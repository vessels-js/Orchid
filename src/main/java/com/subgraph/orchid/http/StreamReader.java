package com.subgraph.orchid.http;

import java.io.IOException;
import java.io.InputStream;

public class StreamReader {
    public static String read(InputStream stream) {
        StringBuilder value = new StringBuilder();
        int character;
        try{
            while ((character = stream.read())!=-1) {
                value.append((char)character);
            }
        } catch(Exception e){
            System.out.println("ERROR!!! "+e.getLocalizedMessage());
        } finally{
            try{
                stream.close();
            } catch(IOException e){
                //Could not close stream
            }
        }
        return value.toString();
    }    
}