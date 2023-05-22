package resource;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.ResourceValueChecker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class representing a Activity object
 * */
public class Activities {

    /**
     * Set of constants with the same value as the DB field, useful in DAOs
     * */
    public static final String ACTIVITY_ID = "activity_id";
    public static final String SUBBOARD_ID = "subboard_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String WORKED_TIME = "worked_time";
    public static final String INDEX = "index";

    /**
     * Set of private fields, each one is a DB field
     * */
    private Integer activityId;
    private Integer subboardId;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private Integer workedTime;
    private Integer index;

    /**
     * Getters and setters for each private field
     * */
    public Integer getActivityId() {
        return activityId;
    }

    public Integer getSubboardId() {
        return subboardId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getWorkedTime() {
        return workedTime;
    }

    public Integer getIndex() {
        return index;
    }

    // ------------------------------------------------------------------------------------

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public void setSubboardId(Integer subboardId) {
        this.subboardId = subboardId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setWorkedTime(Integer workedTime) {
        this.workedTime = workedTime;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    // ----------------------------------------------------------------


    /**
     * Static method that returns a list of Activities from a JSON list file
     *
     * @param inputStream InputStream of the JSON file
     * @return List<Activities> activities
     * @throws IOException  if the file is not found
     * @throws JSONException if the JSON file is not well formatted
     * */
    public static List<Activities> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<Activities> activities = new ArrayList<>();
        JSONArray activitiesJSONList = jobj.getJSONArray("activitiesJSONList");

        for (int i = 0; i < activitiesJSONList.length(); i++) {
            activities.add(fromJSON(activitiesJSONList.getJSONObject(i)));
        }
        return activities;
    }

    /**
     * Static method that returns a single Activity from a JSON file
     *
     * @param inputStream InputStream of the JSON file
     * @return Activities activities
     * @throws IOException  if the file is not found
     * @throws JSONException if the JSON file is not well formatted
     * */
    public static Activities fromJSON(InputStream inputStream) throws IOException, JSONException {

        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return fromJSON(new JSONObject(dataString));
    }

    /**
     * Static method that returns a single Activities from a JSON object
     *
     * @param jObj JSONObject of the JSON file
     *             (it must be a single object, not a list)
     * @return Activities activities
     * @throws JSONException if the JSON file is not well formatted
     * */
    public static Activities fromJSON(JSONObject jObj) throws JSONException {

        Activities activities = new Activities();
        activities.setActivityId(ResourceValueChecker.getValidInteger(jObj.get(ACTIVITY_ID)));
        activities.setSubboardId(ResourceValueChecker.getValidInteger(jObj.get(SUBBOARD_ID)));
        activities.setName(ResourceValueChecker.getValidString(jObj.get(NAME)));
        activities.setDescription(ResourceValueChecker.getValidString(jObj.get(DESCRIPTION)));
        activities.setStartDate(ResourceValueChecker.getValidDate(jObj.get(START_DATE)));
        activities.setEndDate(ResourceValueChecker.getValidDate(jObj.get(END_DATE)));
        activities.setWorkedTime(ResourceValueChecker.getValidInteger(jObj.get(WORKED_TIME)));
        activities.setIndex(ResourceValueChecker.getValidInteger(jObj.get(INDEX)));

        return activities;

    }


    /**
     * Method that returns a JSON object from a single Activities
     *
     * @return JSONObject activitiesJSON
     * @throws JSONException if the JSON file is not well formatted
     * */
    public JSONObject toJSON() throws JSONException {

        JSONObject activitiesJSON = new JSONObject();
        activitiesJSON.put(ACTIVITY_ID, activityId);
        activitiesJSON.put(SUBBOARD_ID, subboardId);
        activitiesJSON.put(NAME, name);
        activitiesJSON.put(DESCRIPTION, description);
        activitiesJSON.put(START_DATE, startDate);
        activitiesJSON.put(END_DATE, endDate);
        activitiesJSON.put(WORKED_TIME, workedTime);
        activitiesJSON.put(INDEX, index);

        return activitiesJSON;
    }

    /** Get a json array from a list of activities objects
     *
     * @param activities list of workSpace objects
     * @return JSONArray containing the activity data
     * @throws JSONException if the input is not valid JSON
     */
    public static JSONArray toJSONList(List<Activities> activities) throws JSONException {

        JSONArray activitiesJSONList = new JSONArray();
        for(Activities activity : activities){
            activitiesJSONList.put(activity.toJSON());
        }

        return activitiesJSONList;
    }

}