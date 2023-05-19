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

import java.util.Date;
import java.time.ZoneId;

/**
 * Class representing a Comments object
 */
public class Comments {

    /**
     * Set of constants with the same value as the DB field, useful in DAOs
     */
    public static final String COMMENT_ID = "comment_id";
    public static final String ACTIVITY_ID = "activity_id";
    public static final String USER_ID = "user_id";
    public static final String COMMENT_TEXT = "comment_text";
    public static final String CREATION_TIME = "creation_time";

    /**
     * Set of private fields, each one is a DB field
     */
    private Integer commentId;
    private Integer activityId;
    private Integer userId;
    private String commentText;
    private Date creationTime;

    /**
     * Getters and setters for each private field
     */
    public Integer getCommentId() {
        return commentId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getCommentText() {
        return commentText;
    }
    
    public Date getCreationTime() {
        return creationTime;
    }

//------------------------------------------------------------------------------------

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
    //----------------------------------------------------------------

    /** Get a list of Comments objects from an InputStream
     *
     * @param inputStream InputStream containing a list of Comments data
     * @return List of Comments objects
     * @throws IOException if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */

    public static List<Comments> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<Comments> comments = new ArrayList<>();
        JSONArray commentsJSONList = jobj.getJSONArray("commentsJSONList");

        for(int i=0; i<commentsJSONList.length(); i++){
            comments.add(fromJSON(commentsJSONList.getJSONObject(i)));
        }
        return comments;
    }

    /** Get a comments object from an InputStream
     *
     * @param inputStream InputStream containing the Comments data
     * @return comments object
     * @throws IOException if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */

    public static Comments fromJSON(InputStream inputStream) throws IOException, JSONException {
        
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return fromJSON(new JSONObject(dataString));
    }

    /** Get a Comments object from a JSONObject
     *
     * @param jObj JSONObject containing the Comments data
     * @return Comments object
     * @throws JSONException if the input is not valid JSON
     */

    public static Comments fromJSON(JSONObject jObj) throws JSONException {
        
        Integer commentId = jObj.getInt(COMMENT_ID);
        Integer activityId = jObj.getInt(ACTIVITY_ID);
        Integer userId = jObj.getInt(USER_ID);
        String commentText = jObj.getString(COMMENT_TEXT);
        Date creationTime = java.util.Date
                .from(LocalDateTime.parse(jObj.getString(CREATION_TIME)).atZone(ZoneId.systemDefault())
                        .toInstant());

        // Create Comments object, set values and return. Constructor is not used cause it's not clean with so many parameters.
        Comments comment = new Comments();
        comment.setCommentId(commentId);
        comment.setActivityId(activityId);
        comment.setUserId(userId);
        comment.setCommentText(commentText);
        comment.setCreationTime(creationTime);

        return comment;

    }
    /** Get a JSONObject from a Comments object
     *
     * @return JSONObject containing the Comments data
     * @throws JSONException if the input is not valid JSON
     */

    public JSONObject toJSON() throws JSONException {

        JSONObject commentsJSON = new JSONObject();
        commentsJSON.put(COMMENT_ID, commentId);
        commentsJSON.put(ACTIVITY_ID, activityId);
        commentsJSON.put(USER_ID , userId);
        commentsJSON.put(COMMENT_TEXT , commentText);
        commentsJSON.put(CREATION_TIME, creationTime);

        return commentsJSON;
    }
}