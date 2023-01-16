package com.berezanskiy.XO.utils;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class Utils {
    public static String getBody(BufferedReader reader) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;



        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            buffer.append(System.lineSeparator());
        }

        return buffer.toString();
    }

    public static LinkedHashMap<String, Object> getData(BufferedReader reader) {
        JSONParser parser = new JSONParser(reader);
        try {
            return parser.object();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
