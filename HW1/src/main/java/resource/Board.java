package resource;
import java.beans.Visibility;
import java.time.LocalDateTime;

import javax.print.DocFlavor.STRING;
import javax.swing.border.Border;

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

public class Board {
    public static final String BOARD_ID = "board_id";
    public static final String WORKSPACE_ID = "workspace_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String VISIBILITY = "visibility";
    public static final String CREATE_TIME = "create_time";

    private Integer boardId;
    private Integer workspaceId;
    private String name;
    private String description;
    private String visibility;
    private LocalDateTime createTime;

    public Integer getBoardId() {
        return boardId;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVisibility() {
        return visibility;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * Get a list of Board objects from an InputStream
     *
     * @param inputStream InputStream containing a list of board data
     * @return List of Board objects
     * @throws IOException   if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */
    public static List<Board> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<Board> boards = new ArrayList<>();
        JSONArray boardsJSONList = jobj.getJSONArray("boards-list");

        for (int i = 0; i < boardsJSONList.length(); i++) {
            boards.add(fromJSON(boardsJSONList.getJSONObject(i)));
        }

        return boards;
    }

    /**
     * Get a Board object from an InputStream
     *
     * @param inputStream InputStream containing the Board data
     * @return Board object
     * @throws IOException   if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */
    public static Board fromJSON(InputStream inputStream) throws IOException, JSONException {

        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return fromJSON(new JSONObject(dataString));
    }

    /**
     * Get a Board object from a JSONObject
     *
     * @param jobj JSONObject containing the Board data
     * @return Board object
     * @throws JSONException if the input is not valid JSON
     */
    public static Board fromJSON(JSONObject jobj) throws JSONException {

        Integer boardId = jobj.getInt(BOARD_ID);
        Integer workspaceId = jobj.getInt(WORKSPACE_ID);
        String name = jobj.getString(NAME);
        String description = jobj.getString(DESCRIPTION);
        String visibility = jobj.getString(VISIBILITY);
        LocalDateTime createTime = LocalDateTime.parse(jobj.getString(CREATE_TIME));

        // Create Board object, set values and return. Constructor is not used cause
        // it's not clean with so many parameters.
        Board board = new Board();
        board.setBoardId(boardId);
        board.setWorkspaceId(workspaceId);
        board.setName(name);
        board.setDescription(description);
        board.setVisibility(visibility);
        board.setCreateTime(createTime);

        return board;

    }

    /**
     * Get a JSONObject from a Board object
     *
     * @return JSONObject containing the Board data
     * @throws JSONException if the input is not valid JSON
     */
    public JSONObject toJSON() throws JSONException {

        JSONObject boardJSON = new JSONObject();
        boardJSON.put(BOARD_ID, boardId);
        boardJSON.put(WORKSPACE_ID, workspaceId);
        boardJSON.put(NAME, name);
        boardJSON.put(DESCRIPTION, description);
        boardJSON.put(VISIBILITY, visibility);
        boardJSON.put(CREATE_TIME, createTime.toString());

        return boardJSON;
    }

}

