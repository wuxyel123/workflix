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

/**
 * Class representing a Permission object
 */
public class Permission {

    /**
     * Set of constants with the same value as the DB field, useful in DAOs
     */
    static final String PERMISSION_ID = "permission_id";
    static final String PERMISSION_NAME="permission_name;";
    static final String DESCRIPTION="description";

    /**
     * Set of private fields, each one is a DB field
     */
    private Integer PermissionId;
    private String PermissionName;
    private String Description;

    /**
     * Getters and setters for each private field
     */
    public Integer getPermissionId() {
        return PermissionId;
    }
    public String getPermissionName(){
        return PermissionName;
    }
    public String getDescription(){
        return Description;
    }

    /**
     * Setters for each private field
     */
    public void setPermissionId(Integer PermissionId) {
        this.PermissionId = PermissionId;
    }
    public void setPermissionName(String PermissionName) {
        this.PermissionName = PermissionName;
    }
    public void setDescription(String Description) {
        this.Description = Description;
    }


    /**
     * Method to convert a Permission object to a JSON object
     *
     * @param inputStream the input stream containing the JSON object
     * @return the JSON object representing the Permission object
     * @throws JSONException if an error occurs during the JSON creation
     * @throws IOException if the input stream cannot be read
     */
    public static List<Permission> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<Permission> permissions = new ArrayList<>();
        JSONArray permissionsJSONList = jobj.getJSONArray("permissions-list");

        for(int i=0; i<permissionsJSONList.length(); i++){
            permissions.add(fromJSON(permissionsJSONList.getJSONObject(i)));
        }

        return permissions;
    }

    /**
     * Method to convert a Permission object to a JSON object
     *
     * @param inputStream the input stream containing the JSON object
     * @return the JSON object representing the Permission object
     * @throws JSONException if an error occurs during the JSON creation
     * @throws IOException if the input stream cannot be read
     */
    public static Permission fromJSON(InputStream inputStream) throws IOException, JSONException {

        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return fromJSON(new JSONObject(dataString));
    }

    /**
     * Method to convert a Permission object to a JSON object
     *
     * @param jobj the JSON object representing the Permission object
     * @return the JSON object representing the Permission object
     * @throws JSONException if an error occurs during the JSON creation
     */
    public static Permission fromJSON(JSONObject jobj) throws JSONException {
        Integer permissionId = jobj.getInt(PERMISSION_ID);
        String permissionName = jobj.getString(PERMISSION_NAME);
        String description = jobj.getString(DESCRIPTION);
        Permission permission=new Permission();
        permission.setPermissionId(permissionId);
        permission.setPermissionName(permissionName);
        permission.setDescription(description);
        return permission;
    }

    /**
     * Method to convert a Permission object to a JSON object
     *
     * @return the JSON object representing the Permission object
     * @throws JSONException if an error occurs during the JSON creation
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject permissionJSON = new JSONObject();

        permissionJSON.put(PERMISSION_ID, PermissionId);
        permissionJSON.put(PERMISSION_NAME, PermissionName);
        permissionJSON.put(DESCRIPTION, Description);
        return permissionJSON;
    }




}
