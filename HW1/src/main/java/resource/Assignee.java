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

public class Assignee {

    public static final String ACTIVITY_ID = "ACTIVITY_ID";
    public static final String USER_ID = "USER_ID";

    private Integer activityId;
    private Integer userId;

    public Integer getActivityId() {
        return activityId;
    }

    public Integer getUserId() {
        return userId;
    }

    // ------------------------------------------------------------------------------------

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // ----------------------------------------------------------------

    /**
     * Get a list of Assignee objects from an InputStream
     *
     * @param inputStream InputStream containing a list of Assignee data
     * @return List of Assignee objects
     * @throws IOException   if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */

    public static List<Assignee> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<Assignee> assignees = new ArrayList<>();
        JSONArray assigneesJSONList = jobj.getJSONArray("assigneesJSONList");

        for (int i = 0; i < assigneesJSONList.length(); i++) {
            assignees.add(fromJSON(assigneesJSONList.getJSONObject(i)));
        }
        return assignees;
    }

    /**
     * Get a assignees object from an InputStream
     *
     * @param inputStream InputStream containing the assignees data
     * @return assignees object
     * @throws IOException   if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */

    public static Assignee fromJSON(InputStream inputStream) throws IOException, JSONException {

        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return fromJSON(new JSONObject(dataString));
    }

    /**
     * Get a Assignee object from a JSONObject
     *
     * @param jObj JSONObject containing the Assignee data
     * @return Assignee object
     * @throws JSONException if the input is not valid JSON
     */

    public static Assignee fromJSON(JSONObject jObj) throws JSONException {

        Integer activityId = jObj.getInt(ACTIVITY_ID);
        Integer userId = jObj.getInt(USER_ID);

        // Create Assignee object, set values and return. Constructor is not used cause
        // it's not clean with so many parameters.
        Assignee assignee = new Assignee();
        assignee.setActivityId(activityId);
        assignee.setUserId(userId);

        return assignee;

    }

    /**
     * Get a JSONObject from a Assignee object
     *
     * @return JSONObject containing the Assignee data
     * @throws JSONException if the input is not valid JSON
     */

    public JSONObject toJSON() throws JSONException {

        JSONObject assigneesJSON = new JSONObject();
        assigneesJSON.put(ACTIVITY_ID, activityId);
        assigneesJSON.put(USER_ID, userId);

        return assigneesJSON;
    }
}