package resource;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Analytics{
    static final String USER_ID = "user_id";
    static final String USERNAME = "username";
    static final String WORKSPACE_ID = "workspace_id";
    static final String WORKSPACE_NAME = "workspace_name";
    static final String NUM_COMPLETED_ACTIVITIES = "num_completed_activities";
    static final String NUM_TOTAL_ACTIVITIES = "num_total_activities";
    static final String TOTAL_WORKED_TIME = "total_worked_time";
    static final String NUM_COMMENTS = "num_comments";

    private int userId;
    private String username;
    private String workspaceId;
    private String workspaceName;
    private String NumCompletedActivities;
    private String NumTotalActivities;
    private String TotalWorkedTime;
    private String NumComments;

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public String getNumCompletedActivities() {
        return NumCompletedActivities;
    }

    public String getNumTotalActivities() {
        return NumTotalActivities;
    }

    public String getTotalWorkedTime() {
        return TotalWorkedTime;
    }

    public String getNumComments() {
        return NumComments;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public void setNumCompletedActivities(String NumCompletedActivities) {
        this.NumCompletedActivities = NumCompletedActivities;
    }

    public void setNumTotalActivities(String NumTotalActivities) {
        this.NumTotalActivities = NumTotalActivities;
    }

    public void setTotalWorkedTime(String TotalWorkedTime) {
        this.TotalWorkedTime = TotalWorkedTime;
    }

    public void setNumComments(String NumComments) {
        this.NumComments = NumComments;
    }

   /** Get a list of Analytics objects from an InputStream
    *
    * @param inputStream InputStream containing a list of analytics data
    * @return List of Analytics objects
    * @throws IOException if an I/O error occurs
    * @throws JSONException if the input is not valid JSON
    */
    public static List<Analytics> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<Analytics> user_analytics_per_workspace = new ArrayList<>();
        JSONArray analyticsJSONList = jobj.getJSONArray("user_analytics_per_workspace-list");

        for(int i=0; i<analyticsJSONList.length(); i++){
            user_analytics_per_workspace.add(fromJSON(analyticsJSONList.getJSONObject(i)));
        }

        return user_analytics_per_workspace;
    }
    /** Get a Analytics object from an InputStream
    *
    * @param inputStream InputStream containing the analytics data
    * @return Analytics object
    * @throws IOException if an I/O error occurs
    * @throws JSONException if the input is not valid JSON
    */
    public static Analytics fromJSON(InputStream inputStream) throws IOException, JSONException {
        
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return fromJSON(new JSONObject(dataString));
    }
    /** Get a Analytics object from a JSONObject
    *
    * @param jobj JSONObject containing the analytics data
    * @return Analytics object
    * @throws JSONException if the input is not valid JSON
    */

    public static Analytics fromJSON(JSONObject jobj) throws JSONException {
        
        int userId = jobj.getInt(USER_ID);
        String username = jobj.getString(USERNAME);
        String workspaceId = jobj.getString(WORKSPACE_ID);
        String workspaaceName = jobj.getString(WORKSPACE_NAME);
        String NumCompletedActivities = jobj.getString(NUM_COMPLETED_ACTIVITIES);
        String NumTotalActivities = jobj.getString(NUM_TOTAL_ACTIVITIES);
        String TotalWorkedTime = jobj.getString(TOTAL_WORKED_TIME);
        String NumComments = jobj.getString(NUM_COMMENTS);

        // Create Analytics object, set values and return. Constructor is not used cause it's not clean with so many parameters.
        Analytics analytics = new Analytics();
        analytics.setUserId(userId);
        analytics.setUsername(username);
        analytics.setWorkspaceId(workspaceId);
        analytics.setWorkspaceName(workspaaceName);
        analytics.setNumCompletedActivities(NumCompletedActivities);
        analytics.setNumTotalActivities(NumTotalActivities);
        analytics.setTotalWorkedTime(TotalWorkedTime);
        analytics.setNumComments(NumComments);

        return analytics;

    }
    /** Get a JSONObject from a Analytics object
    *
    * @return JSONObject containing the analytics data
    * @throws JSONException if the input is not valid JSON
    */
    public JSONObject toJSON() throws JSONException {
        
        JSONObject analyticsJSON = new JSONObject();
        analyticsJSON.put(USER_ID, userId);
        analyticsJSON.put(USERNAME, username);
        analyticsJSON.put(WORKSPACE_ID, workspaceId);
        analyticsJSON.put(WORKSPACE_NAME, workspaceName);
        analyticsJSON.put(NUM_COMPLETED_ACTIVITIES, NumCompletedActivities);
        analyticsJSON.put(NUM_TOTAL_ACTIVITIES, NumTotalActivities);
        analyticsJSON.put(TOTAL_WORKED_TIME, TotalWorkedTime);
        analyticsJSON.put(NUM_COMMENTS, NumComments);

        return analyticsJSON;
    }
}