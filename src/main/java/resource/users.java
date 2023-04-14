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

public class User {

    static final String USER_ID = "user_id";
    static final String USERNAME = "username";
    static final String PASSWORD = "password";
    static final String EMAIL = "email";
    static final String FIRST_NAME = "first_name";
    static final String LAST_NAME = "last_name";
    static final String PROFILE_PICTURE = "profile_picture";
    static final String DESCRIPTION = "description";
    static final String CREATE_DATE = "create_date";

    private int userId;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String description;
    private LocalDateTime createDate;

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public static List<User> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<User> users = new ArrayList<>();
        JSONArray usersJSONList = jobj.getJSONArray("users-list");

        for(int i=0; i<usersJSONList.length(); i++){
            users.add(fromJSON(usersJSONList.getJSONObject(i)));
        }

        return users;
    }

    public static User fromJSON(InputStream inputStream) throws IOException, JSONException {
        
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);

        int userId = jobj.getInt(USER_ID);
        String username = jobj.getString(USERNAME);
        String password = jobj.getString(PASSWORD);
        String email = jobj.getString(EMAIL);
        String firstName = jobj.getString(FIRST_NAME);
        String lastName = jobj.getString(LAST_NAME);
        String profilePicture = jobj.getString(PROFILE_PICTURE);
        String description = jobj.getString(DESCRIPTION);
        LocalDateTime createDate = LocalDateTime.parse(jobj.getString(CREATE_DATE));

        return new User(userId, username, password, email, firstName, lastName, profilePicture, description, createDate);
    }

    public static User fromJSON(JSONObject jobj) throws JSONException {
        
        int userId = jobj.getInt(USER_ID);
        String username = jobj.getString(USERNAME);
        String password = jobj.getString(PASSWORD);
        String email = jobj.getString(EMAIL);
        String firstName = jobj.getString(FIRST_NAME);
        String lastName = jobj.getString(LAST_NAME);
        String profilePicture = jobj.getString(PROFILE_PICTURE);
        String description = jobj.getString(DESCRIPTION);
        LocalDateTime createDate = LocalDateTime.parse(jobj.getString(CREATE_DATE));

        return new User(userId, username, password, email, firstName, lastName, profilePicture, description, createDate);
    }

    public JSONObject toJSON() throws JSONException {
        
        JSONObject userJSON = new JSONObject();
        userJSON.put(USER_ID, userId);
        userJSON.put(USERNAME, username);
        userJSON.put(PASSWORD, password);
        userJSON.put(EMAIL, email);
        userJSON.put(FIRST_NAME, firstName);
        userJSON.put(LAST_NAME, lastName);
        userJSON.put(PROFILE_PICTURE, profilePicture);
        userJSON.put(DESCRIPTION, description);
        userJSON.put(CREATE_DATE, createDate.toString());

        return userJSON;
    }



}