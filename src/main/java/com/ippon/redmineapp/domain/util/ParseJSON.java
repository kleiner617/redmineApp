package com.ippon.redmineapp.domain.util;

import com.ippon.redmineapp.domain.Issue;
import com.ippon.redmineapp.domain.Project;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by owner on 5/29/17.
 */

//TODO: To get just projects:
    //TODO: https://redmine.ippon.fr/projects.json

    //Should probably just get projects first.  Then can get issues for each individ project.

public class ParseJSON {

    private final Logger log = LoggerFactory.getLogger(ParseJSON.class);

/*    public static void main(String args[]) {

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

    }*/

    public  ArrayList<Project> parseProjects (String jsonStr){

        ArrayList<Project> projList = new ArrayList<>();

        JSONArray c = null;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            c = jsonObj.getJSONArray("projects");
            for (int i = 0 ; i < c.length(); i++) {

                Project project = new Project();
                JSONObject obj = c.getJSONObject(i);

                String StringID = obj.getString("id");
                project.setId(Long.valueOf(StringID));
                project.setName(obj.getString("name"));
                project.setIdentifier(obj.getString("identifier"));
                project.setDescription(obj.getString("description"));
                project.setStatus(obj.getString("status"));

                ZonedDateTime createTime = ZonedDateTime.parse(obj.getString("created_on"));
                project.setCreatedOn(createTime);
                ZonedDateTime updateTime = ZonedDateTime.parse(obj.getString("updated_on"));
                project.setUpdatedOn(updateTime);

                projList.add(project);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return projList;

    }


    public  ArrayList<Issue> parseIssues (String jsonStr){

        ArrayList<Issue> issueList = new ArrayList<>();
        JSONArray c = null;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            c = jsonObj.getJSONArray("issues");
            for (int i = 0 ; i < c.length(); i++) {

                Issue issue = new Issue();
                JSONObject obj = c.getJSONObject(i);

                //TODO: Figure out what ID really is!!!
                String  id = obj.getString("id");
//                long longID = id;
//                issue.setId(longID);


                issue.setSubject(obj.getString("subject"));
                issue.setDescription(obj.getString("description"));
                issue.setDoneRatio(obj.getString("doneRatio"));

                ZonedDateTime startTime = ZonedDateTime.parse(obj.getString("startDate"));
                issue.setCreatedOn(startTime);
                ZonedDateTime createTime = ZonedDateTime.parse(obj.getString("createdOn"));
                issue.setUpdatedOn(createTime);
                ZonedDateTime updateTime = ZonedDateTime.parse(obj.getString("updatedOn"));
                issue.setUpdatedOn(updateTime);

                issueList.add(issue);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return issueList;

    }




}
