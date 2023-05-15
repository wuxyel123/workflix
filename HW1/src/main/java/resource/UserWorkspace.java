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

/** UserWorkspace class, contains all the information about a userWorkspace */
public class UserWorkspace {

    public static final String USER_ID = "user_id";
    public static final String WORKSPACE_ID = "workspace_id";
    public static final String PERMISSION_ID = "permission_id";

    /** Set of private fields, each one is a DB field */
    private Integer userId;
    private Integer workspaceId;
    private Integer permissionId;

    /** Getters and setters for each private field */
    public Integer getUserId() {
        return userId;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    /** Static method to create a UserWorkspace from a JSON
     *
     * @param inputStream InputStream from which the JSON is read
     * @return UserWorkspace object created from the JSON
     * @throws IOException if the inputStream throws it
     * @throws JSONException if the JSON is not well formed
     *
     * */
    public static List<UserWorkspace> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<UserWorkspace> userWorkspace = new ArrayList<>();
        JSONArray userWorkspaceJSONList = jobj.getJSONArray("wserWorkspace-list");

        for(int i=0; i<userWorkspaceJSONList.length(); i++){
            userWorkspace.add(fromJSON(userWorkspaceJSONList.getJSONObject(i)));
        }

        return userWorkspace;
    }

    /** Static method to create a UserWorkspace from a JSON
     *
     * @param inputStream InputStream from which the JSON is read
     * @return UserWorkspace object created from the JSON
     * @throws IOException if the inputStream throws it
     * @throws JSONException if the JSON is not well formed
     *
     * */
    public static UserWorkspace fromJSON(InputStream inputStream) throws IOException, JSONException {
        
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return fromJSON(new JSONObject(dataString));
    }

    /** Static method to create a UserWorkspace from a JSON
     *
     * @param jobj JSONObject from which the UserWorkspace is created
     * @return UserWorkspace object created from the JSON
     * @throws JSONException if the JSON is not well formed
     *
     * */
    public static UserWorkspace fromJSON(JSONObject jobj) throws JSONException {
        
        Integer userId = jobj.getInt(USER_ID);
        Integer workspaceId = jobj.getInt(WORKSPACE_ID);
        Integer permissionId = jobj.getInt(PERMISSION_ID);

        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUserId(userId);
        userWorkspace.setWorkspaceId(workspaceId);
        userWorkspace.setPermissionId(permissionId);

        return userWorkspace;

    }

    /** Method to convert a UserWorkspace to a JSON
     *
     * @return JSONObject created from the UserWorkspace
     * @throws JSONException if the JSON is not well formed
     *
     * */
    public JSONObject toJSON() throws JSONException {
        
        JSONObject userWorkspace = new JSONObject();
        userWorkspace.put(USER_ID, userId);
        userWorkspace.put(WORKSPACE_ID, workspaceId);
        userWorkspace.put(PERMISSION_ID, permissionId);

        return userWorkspace;
    }



}