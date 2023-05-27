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

/** User class, contains all the information about a user
 *
 */
public class User {

    /** Set of constants with the same value as the DB field, useful in DAOs */
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PROFILE_PICTURE = "profile_picture";
    public static final String DESCRIPTION = "description";
    public static final String CREATE_DATE = "create_date";

    /** Set of private fields, each one is a DB field */
    private Integer userId;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String description;
    private Date createDate;

    /** Getters and setters for each private field */
    public Integer getUserId() {
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setUserId(Integer userId) {
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

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /** Get a list of User objects from an InputStream
     *
     * @param inputStream InputStream containing a list of user data
     * @return List of User objects
     * @throws IOException if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */
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

    /** Get a User object from an InputStream
     *
     * @param inputStream InputStream containing the user data
     * @return User object
     * @throws IOException if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */
    public static User fromJSON(InputStream inputStream) throws IOException, JSONException {
        
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return fromJSON(new JSONObject(dataString));
    }

    /** Get a User object from a JSONObject
     *
     * @param jobj JSONObject containing the user data
     * @return User object
     * @throws JSONException if the input is not valid JSON
     */
    public static User fromJSON(JSONObject jobj) throws JSONException {

        // Create User object, set values and return. Constructor is not used cause it's not clean with so many parameters.
        User user = new User();
        user.setUserId(ResourceValueChecker.getValidInteger(jobj.get(USER_ID)));
        user.setUsername(ResourceValueChecker.getValidString(jobj.get(USERNAME)));
        user.setPassword(ResourceValueChecker.getValidString(jobj.get(PASSWORD)));
        user.setEmail(ResourceValueChecker.getValidString(jobj.get(EMAIL)));
        user.setFirstName(ResourceValueChecker.getValidString(jobj.get(FIRST_NAME)));
        user.setLastName(ResourceValueChecker.getValidString(jobj.get(LAST_NAME)));
        user.setProfilePicture(ResourceValueChecker.getValidString(jobj.get(PROFILE_PICTURE)));
        user.setDescription(ResourceValueChecker.getValidString(jobj.get(DESCRIPTION)));
        user.setCreateDate(ResourceValueChecker.getValidDate(jobj.get(CREATE_DATE)));

        return user;

    }
    /** Get a JSONObject from a User object
     *
     * @return JSONObject containing the user data
     * @throws JSONException if the input is not valid JSON
     */
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
        userJSON.put(CREATE_DATE, createDate);

        return userJSON;
    }

    /**
     * Get a JSONArray from a list of user objects
     *
     * @param users List of Users objects
     * @return JSONArray containing the Assignee data
     * @throws JSONException if the input is not valid JSON
     */
    public static JSONArray toJSONList(List<User> users) throws JSONException {

        JSONArray usersJSON = new JSONArray();

        for(User user : users){
            usersJSON.put(user.toJSON());
        }

        return usersJSON;
    }

}