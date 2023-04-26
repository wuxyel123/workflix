package dao;

import resource.Subboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteSubboardsDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT ="DELETE FROM workflix.subboards WHERE subboard_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    Subboard subboards;

    public DeleteSubboardsDatabase(final Connection con, final Subboard s) {
        this.con = con;
        this.subboards = s;
    }

    public Subboard DeleteSubboard() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created user
        Subboard subboard = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, subboards.getSubboardId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                subboard = new Subboard();
                subboard.setSubboardId(rs.getInt(Subboard.SUBBOARD_ID));
                subboard.setBoardId(rs.getInt(Subboard.BOARD_ID));
                subboard.setName(rs.getString(Subboard.NAME));
                subboard.setIndex(rs.getInt(Subboard.INDEX));
                subboard.setDefaultCompletedActivitySubboard(rs.getBoolean(Subboard.DEFAULT_COMPLETED_ACTIVITY_SUBBOARD));
                subboard.setCreationTime(rs.getDate(Subboard.CREATION_TIME));
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

        return subboard;
    }

}