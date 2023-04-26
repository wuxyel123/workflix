package resource;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Activities {

    public static final String ACTIVITY_ID = "ACTIVITY_ID";
    public static final String SUBBOARD_ID = "SUBBOARD_ID";
    public static final String NAME = "NAME";

    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";
    public static final String WORKED_TIME = "WORKED_TIME";
    public static final String INDEX = "INDEX";

    private Integer activityId;
    private Integer subboardId;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private Integer workedTime;
    private Integer index;

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

        Integer activityId = jObj.getInt(ACTIVITY_ID);
        Integer subboardId = jObj.getInt(SUBBOARD_ID);
        String name = jObj.getString(NAME);
        String description = jObj.getString(DESCRIPTION);
        Date startDate = java.util.Date
                .from(LocalDateTime.parse(jObj.getString(START_DATE)).atZone(ZoneId.systemDefault())
                        .toInstant());
        Date endDate = java.util.Date
                .from(LocalDateTime.parse(jObj.getString(END_DATE)).atZone(ZoneId.systemDefault())
                        .toInstant());
        Integer workedTime = jObj.getInt(WORKED_TIME);
        Integer index = jObj.getInt(INDEX);

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