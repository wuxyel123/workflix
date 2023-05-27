package resource;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.ResourceValueChecker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Assignee object
 */
public class Assignee {

    /**
     * Set of constants with the same value as the DB field, useful in DAOs
     */
    public static final String ACTIVITY_ID = "activity_id";
    public static final String USER_ID = "user_id";

    /**
     * Set of private fields, each one is a DB field
     */
    private Integer activityId;
    private Integer userId;

    /**
     * Getters and setters for each private field
     */
    public int getActivityId() {
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

        // Create Assignee object, set values and return. Constructor is not used cause
        // it's not clean with so many parameters.
        Assignee assignee = new Assignee();
        assignee.setActivityId(ResourceValueChecker.getValidInteger(jObj.get(ACTIVITY_ID)));
        assignee.setUserId(ResourceValueChecker.getValidInteger(jObj.get(USER_ID)));

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

    /**
     * Get a JSONArray from a list of Assignee objects
     *
     * @param assignees List of Assignee objects
     * @return JSONArray containing the Assignee data
     * @throws JSONException if the input is not valid JSON
     */
    public static JSONArray toJSONList(List<Assignee> assignees) throws JSONException {

        JSONArray assigneesJSONList = new JSONArray();
        for (Assignee assignee : assignees) {
            assigneesJSONList.put(assignee.toJSON());
        }
        return assigneesJSONList;
    }
}