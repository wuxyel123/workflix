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

public class Subboards {

    static final String SUBBOARD_ID = "subboard_id";
    static final String BOARD_ID = "board_id";
    static final String NAME = "name";
    static final String INDEX = "index";
    static final String DEFAULT_COMPLETED_ACTIVITY_SUBBOARD = "default_completed_activity_subboard";
    static final String CREATION_TIME = "creation_time";


    private Integer subboardId;
    private Integer boardId;
    private String name;
    private Integer index;
    private Boolean defaultCompletedActivitySubboard;
    private LocalDateTime creationTime;


    public int getSubboardId() {
        return subboardId;
    }

    public int getBoardId() {
        return boardId;
    }

    public String getName() {
        return name;
    }
    public Integer getIndex() {
        return index;
    }
    public Boolean getDefaultCompletedActivitySubboard() {
        return defaultCompletedActivitySubboard;
    }
    
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

//------------------------------------------------------------------------------------

    public void setSubboardId(int subboardId) {
        this.subboardId = subboardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public void setGetDefaultCompletedActivitySubboard(Boolean getDefaultCompletedActivitySubboard) {
        this.defaultCompletedActivitySubboard = getDefaultCompletedActivitySubboard;
    }
    

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
    //----------------------------------------------------------------

    /** Get a list of Subboards objects from an InputStream
     *
     * @param inputStream InputStream containing a list of Subbourds data
     * @return List of Subbourds objects
     * @throws IOException if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */
    public static List<Subboards> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<Subboards> subboards = new ArrayList<>();
        JSONArray subboardsJSONList = jobj.getJSONArray("subbaoardsJSONList");

        for(int i=0; i<subbaoardsJSONList.length(); i++){
            subboards.add(fromJSON(subboardsJSONList.getJSONObject(i)));
        }

        return subboards;
    }

    /** Get a subboards object from an InputStream
     *
     * @param inputStream InputStream containing the Subbourds data
     * @return subboards object
     * @throws IOException if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */
    public static Subboards fromJSON(InputStream inputStream) throws IOException, JSONException {
        
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return fromJSON(new JSONObject(dataString));
    }

    /** Get a Subboards object from a JSONObject
     *
     * @param jobj JSONObject containing the Subboards data
     * @return Subboards object
     * @throws JSONException if the input is not valid JSON
     */
    public static Subboards fromJSON(JSONObject jobj) throws JSONException {
        
        Integer subboardId = jobj.getInt(SUBBOARD_ID);
        Integer boardId = jobj.getInt(BOARD_ID);
        String name = jobj.getString(NAME);
        Integer index = jobj.getInt(INDEX);
        Boolean defaultCompletedActivitySubboard = jobj.getBoolean(DEFAULT_COMPLETED_ACTIVITY_SUBBOARD);
        LocalDateTime creationTime = LocalDateTime.parse(jobj.getString(CREATION_TIME));

        // Create Subboards object, set values and return. Constructor is not used cause it's not clean with so many parameters.
        Subboards subboard = new Subboards();
        subboard.setSubboardId(subboardId);
        subboard.setBoardId(boardId);
        subboard.setName(name);
        subboard.setIndex(index);
        subboard.setGetDefaultCompletedActivitySubboard(defaultCompletedActivitySubboard);
        subboard.setCreationTime(creationTime);

        return subboard;

    }
    /** Get a JSONObject from a Subbourds object
     *
     * @return JSONObject containing the Subbourds data
     * @throws JSONException if the input is not valid JSON
     */
    public JSONObject toJSON() throws JSONException {
        
        JSONObject SubboardsJSON = new JSONObject();
        SubboardsJSON.put(SUBBOARD_ID, subboardId);
        SubboardsJSON.put(BOARD_ID, boardId);
        SubboardsJSON.put(NAME , name);
        SubboardsJSON.put(INDEX , index);
        SubboardsJSON.put(DEFAULT_COMPLETED_ACTIVITY_SUBBOARD , defaultCompletedActivitySubboard);
        SubboardsJSON.put(CREATION_TIME, creationTime);

        return SubboardsJSON;
    }



}