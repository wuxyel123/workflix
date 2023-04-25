package dao;

import resource.Subboards;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateSubboardsDatabase {

    /
     * The SQL statement to be executed
     */
    private static final String STATEMENT ="DELETE FROM workflix.subboards WHERE subboard_id=? RETURNING *;";
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

    public Subboards updateSubboards() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created user
        Subboards updateSubboards = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.Int(1, subboards.getSubboardId());

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

        return updateSubboards;
    }

}