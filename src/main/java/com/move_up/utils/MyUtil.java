package com.move_up.utils;

import com.move_up.dto.UserDTO;
import com.move_up.global.Global;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MyUtil {
    public static void setUserAttribute(String username, String key, String value) {
        String attributes = Global.userAttributes.get(username);
        String attribute = key + ":" + value + ";";

        if (attributes == null) {
            Global.userAttributes.put(username, attribute);
            return;
        }

        if (attributes.contains(key)) {
            // get old attribute
            String oldAttribute = "";
            for (int i = attributes.indexOf(key); i < attributes.length(); i++) {
                oldAttribute += attributes.charAt(i);
                if (attributes.charAt(i) == ';')
                    break;
            }

            //set new attribute
            Global.userAttributes.put(username, attributes.replace(oldAttribute, attribute));
        } else {
            Global.userAttributes.put(username, attributes + attribute);
        }
    }

    public static String getUserAttribute(String username, String key) {
        String attributes = Global.userAttributes.get(username);
        if (attributes == null)
            return null;

        if (!attributes.contains(key))
            return null;

        // get attribute
        String attribute = "";
        Boolean startGet = false;
        for (int i = attributes.indexOf(key); i < attributes.length(); i++) {
            if (attributes.charAt(i) == ';')
                break;
            if (startGet)
                attribute += attributes.charAt(i);
            if (attributes.charAt(i) == ':')
                startGet = true;

        }

        return attribute;
    }

    public static String getDocumentFromUrl(String urlStr) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(urlStr);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            in.close();

            return content.toString();
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }

        return null;
    }
}
