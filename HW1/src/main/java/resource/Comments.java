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

public class Comments {
    
    static final String COMMENT_ID = "COMMENT_ID";
    static final String ACTIVITY_ID = "ACTIVITY_ID";
    static final String USER_ID = "USER_ID";
    static final String COMMENT_TEXT = "COMMENT_TEXT";
    static final String CREATION_TIME = "CREATION_TIME";

    private int commentId;
    private int activityId;
    private int userId;
    private String commentText;
    private LocalDateTime creationTime;

    public int getCommentId() {
        return commentId;
    }

    public int getActivityId() {
        return activityId;
    }

    public int getUserId() {
        return userId;
    }

    public String getCommentText() {
        return commentText;
    }
    
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

//------------------------------------------------------------------------------------

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setCreationTime(LocalDateTime creationTime) {
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
        
        int commentId = jObj.getInt(COMMENT_ID);
        int activityId = jObj.getInt(ACTIVITY_ID);
        int userId = jObj.getInt(USER_ID);
        String commentText = jObj.getString(COMMENT_TEXT);
        LocalDateTime creationTime = LocalDateTime.parse(jObj.getString(CREATION_TIME));

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