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

public class WorkSpace {

    static final String WORKSPACE_ID = "workspace_id";
    static final String WORKSPACE_NAME = "workspace_name";
    static final String TEMPLATE_ID = "template_id";
    static final String CREATION_TIME = "creation_time";

    private Integer workspaceId;
    private String workspaceName;
    private Integer templateId;
    private LocalDateTime creationTime;

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public LocalDateTime getCreationTime() {
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

    public void setCreationTime(LocalDateTime creationTime) {
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
        
        Integer workspaceId = jobj.getInt(WORKSPACE_ID);
        String workspaceName = jobj.getString(WORKSPACE_NAME);
        Integer templateId = jobj.getInt(TEMPLATE_ID);
        LocalDateTime creationTime = LocalDateTime.parse(jobj.getString(CREATION_TIME));

        // Create workSpace object, set values and return. Constructor is not used cause it's not clean with so many parameters.
        WorkSpace workSpace = new WorkSpace();
        workSpace.setWorkspaceId(workspaceId);
        workSpace.setWorkspaceName(workspaceName);
        workSpace.setTemplateId(templateId);
        workSpace.setCreationTime(creationTime);

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



}