package resource;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;

public class Activities {

    static final String ACTIVITY_ID = "ACTIVITY_ID";
    static final String SUBBOARD_ID = "SUBBOARD_ID";
    static final String NAME = "NAME";
    static final String DESCRIPTION = "DESCRIPTION";
    static final String START_DATE = "START_DATE";
    static final String END_DATE = "END_DATE";
    static final String WORKED_TIME = "WORKED_TIME";
    static final String INDEX = "INDEX";

    private int activityId;
    private int subboardId;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int workedTime;
    private int index;

    public int getActivityId() {
        return activityId;
    }

    public int getSubboardId() {
        return subboardId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public int getWorkedTime() {
        return workedTime;
    }

    public int getIndex() {
        return index;
    }

    // ------------------------------------------------------------------------------------

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setSubboardId(int subboardId) {
        this.subboardId = subboardId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setWorkedTime(int workedTime) {
        this.workedTime = workedTime;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    // ----------------------------------------------------------------

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

    public static Activities fromJSON(InputStream inputStream) throws IOException, JSONException {

        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return fromJSON(new JSONObject(dataString));
    }

    public static Activities fromJSON(JSONObject jObj) throws JSONException {

        int activityId = jObj.getInt(ACTIVITY_ID);
        int subboardId = jObj.getInt(SUBBOARD_ID);
        String name = jObj.getString(NAME);
        String description = jObj.getString(DESCRIPTION);
        LocalDateTime startDate = LocalDateTime.parse(jObj.getString(START_DATE));
        LocalDateTime endDate = LocalDateTime.parse(jObj.getString(END_DATE));
        int workedTime = jObj.getInt(WORKED_TIME);
        int index = jObj.getInt(INDEX);

        Activities activities = new Activities();
        activities.setActivityId(activityId);
        activities.setSubboardId(subboardId);
        activities.setName(name);
        activities.setDescription(description);
        activities.setStartDate(startDate);
        activities.setEndDate(endDate);
        activities.setWorkedTime(workedTime);
        activities.setIndex(index);

        return activities;

    }

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
}