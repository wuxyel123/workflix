package dao;

import resource.Park;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateSubboardsDatabase {

    /
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.subboard SET SUBBOARDS_ID=? BOARD_IDD=?, NAME=?,DEFAULT_COMPLETED_ACTIVITY_SUBBOARD WHERE CREATION_TIME=? RETURNING *;";
    /
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    Subboards subboards;

    public UpdateSubboardsDatabase(final Connection con, final Subboards s) {
        this.con = con;
        this.subboards = s;
    }

    public Subboard updateSubboards() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created user
        Subboard updatesubboard = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.Int(1, subboards.getSubboardId());
            pstmt.setString(2, subboards.getBoardID());
            pstmt.setString(3, subboards.getName());
            pstmt.setString(3, subboards.getIndex());
            pstmt.setBool(3, subboards.setGetDefaultCompletedActivitySubboard());
            pstmt.setLocalDateTime(4, subboards.getCreationTime());



            rs = pstmt.executeQuery();

            if (rs.next()) {
                updateSubboards = new Subboards();
                updateSubboards.setSubboardId(rs.getString(Subboards.SUBBOARDS_ID));
                updateSubboards.setBoardId(rs.getString(Subboards.BOARD_ID));
                updateSubboards.setName(rs.getString(Subboards.NAME));
                updateSubboards.setIndex(rs.getString(Subboards.INDEX));
                updateSubboards.setDefaultCompletedActivitySubboard(rs.getString(Subboards.DEFAULT_COMPLETED_ACTIVITY_SUBBOARD));
                updateSubboards.setCreationTime(rs.getString(Subboards.CREATION_TIME));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

            con.close();
        }

        return updatesubboard;
    }

}