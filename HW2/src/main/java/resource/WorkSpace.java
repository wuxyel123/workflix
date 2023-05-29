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

/** WorkSpace class, contains all the information about a workSpace */
public class WorkSpace {

    /** Set of constants with the same value as the DB field, useful in DAOs */
    public static final String WORKSPACE_ID = "workspace_id";
    public static final String WORKSPACE_NAME = "workspace_name";
    public static final String TEMPLATE_ID = "template_id";
    public static final String CREATION_TIME = "creation_time";

    /** Set of private fields, each one is a DB field */
    private Integer workspaceId;
    private String workspaceName;
    private Integer templateId;
    private Date creationTime;

    /** Getters and setters for each private field */
    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public Date getCreationTime() {
        return creationTime;
    }


    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    /** Get a list of workSpace objects from an InputStream
     *
     * @param inputStream InputStream containing a list of workSpace data
     * @return List of workSpace objects
     * @throws IOException if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */
    public static List<WorkSpace> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<WorkSpace> workSpaces = new ArrayList<>();
        JSONArray workSpacesJSONList = jobj.getJSONArray("workSpaces-list");

        for(int i=0; i<workSpacesJSONList.length(); i++){
            workSpaces.add(fromJSON(workSpacesJSONList.getJSONObject(i)));
        }

        return workSpaces;
    }

    /** Get a workSpace object from an InputStream
     *
     * @param inputStream InputStream containing the workSpace data
     * @return workSpace object
     * @throws IOException if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */
    public static WorkSpace fromJSON(InputStream inputStream) throws IOException, JSONException {
        
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return fromJSON(new JSONObject(dataString));
    }

    /** Get a workSpace object from a JSONObject
     *
     * @param jobj JSONObject containing the workSpace data
     * @return workSpace object
     * @throws JSONException if the input is not valid JSON
     */
    public static WorkSpace fromJSON(JSONObject jobj) throws JSONException {

        WorkSpace workSpace = new WorkSpace();
        workSpace.setWorkspaceId(ResourceValueChecker.getValidInteger(jobj.get(WORKSPACE_ID)));
        workSpace.setWorkspaceName(ResourceValueChecker.getValidString(jobj.get(WORKSPACE_NAME)));
        workSpace.setTemplateId(ResourceValueChecker.getValidInteger(jobj.get(TEMPLATE_ID)));
        workSpace.setCreationTime(ResourceValueChecker.getValidDate(jobj.get(CREATION_TIME)));

        return workSpace;

    }
    /** Get a JSONObject from a workSpace object
     *
     * @return JSONObject containing the workSpace data
     * @throws JSONException if the input is not valid JSON
     */
    public JSONObject toJSON() throws JSONException {
        
        JSONObject workSpaceJSON = new JSONObject();
        workSpaceJSON.put(WORKSPACE_ID, workspaceId);
        workSpaceJSON.put(WORKSPACE_NAME, workspaceName);
        workSpaceJSON.put(TEMPLATE_ID, templateId);
        workSpaceJSON.put(CREATION_TIME, creationTime.toString());

        return workSpaceJSON;
    }

    /** Get a json array from a list of workSpace objects
     *
     * @param workSpaces list of workSpace objects
     * @return JSONArray containing the workSpace data
     * @throws JSONException if the input is not valid JSON
     */
    public static JSONArray toJSONList(List<WorkSpace> workSpaces) throws JSONException {

        JSONArray workSpacesJSONList = new JSONArray();
        for(WorkSpace workSpace : workSpaces){
            workSpacesJSONList.put(workSpace.toJSON());
        }

        return workSpacesJSONList;
    }

}