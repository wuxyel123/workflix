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

public class Permission {
    static final String PERMISSION_ID = "permission_id";
    static final String PERMISSION_NAME="permission_name;";
    static final String DESCRIPTION="description";

    private int PermissionId;
    private String PermissionName;
    private String Description;

    public Integer getPermissionId() {
        return PermissionId;
    }
    public String getPermissionName(){
        return PermissionName;
    }
    public String getDescription(){
        return Description;
    }

    public void setPermissionId(Integer PermissionId) {
        this.PermissionId = PermissionId;
    }
    public void setPermissionName(String PermissionName) {
        this.PermissionName = PermissionName;
    }
    public void setDescription(String Description) {
        this.Description = Description;
    }


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
    public static Permission fromJSON(InputStream inputStream) throws IOException, JSONException {

        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return fromJSON(new JSONObject(dataString));
    }

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
    public JSONObject toJSON() throws JSONException {
        JSONObject permissionJSON = new JSONObject();

        permissionJSON.put(PERMISSION_ID, PermissionId);
        permissionJSON.put(PERMISSION_NAME, PermissionName);
        permissionJSON.put(DESCRIPTION, Description);
        return permissionJSON;
    }




}
