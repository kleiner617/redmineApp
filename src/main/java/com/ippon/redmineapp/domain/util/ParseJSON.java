package com.ippon.redmineapp.domain.util;

import jdk.nashorn.internal.parser.JSONParser;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by owner on 5/29/17.
 */

//TODO: To get just projects:
    //TODO: https://redmine.ippon.fr/projects.json

    //Should probably just get projects first.  Then can get issues for each individ project.

public class ParseJSON {

    private static final String filePath = "/Users/owner/Desktop/laptop.json";

    public static void main(String args[]) {

        String projJSON = "" +

        "{" +
            "\"projects\": [" +
            "{" +
                "\"id\": 2," +
                "\"name\": \"Direction Technique\"," +
                "\"identifier\": \"direction-technique\"," +
                "\"description\": \"\"," +
                "\"status\": 1," +
                "\"created_on\": \"2014-03-26T09:20:29Z\"," +
                "\"updated_on\": \"2014-03-26T09:20:29Z\"" +
            "}," +
            "{" +
                "\"id\": 43," +
                "\"name\": \"FeedBack\"," +
                "\"identifier\": \"feedback\"," +
                "\"description\": \"\"," +
                "\"status\": 1," +
                "\"created_on\": \"2016-02-02T11:22:50Z\"," +
                "\"updated_on\": \"2016-02-02T11:22:50Z\"" +
            "}" +
  "]," +
            "\"total_count\": 10," +
            "\"offset\": 0," +
            "\"limit\": 25" +
        "}";

        parseString(projJSON);


    }

    public static void parseString (String jsonStr){

        JSONArray c = null;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            c = jsonObj.getJSONArray("projects");
            for (int i = 0 ; i < c.length(); i++) {
                JSONObject obj = c.getJSONObject(i);


                System.out.println("BLAH");
                System.out.println(c.getJSONObject(i));

                String A = obj.getString("name");
                String B = obj.getString("status");
                String C = obj.getString("created_on");
                System.out.println(A + " " + B + " " + C);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




}
