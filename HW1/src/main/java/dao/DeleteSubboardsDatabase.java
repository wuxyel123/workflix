package dao;

import resource.Subboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class to delete a subboard from the database
 */
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
     * The Subboard to be deleted
     */
    Subboard subboard;

    /**
     * Initialize the DAO object with a connection to the database and the object to be deleted
     *
     * @param con the connection to the database
     * @param s the object to be inserted
     */
    public DeleteSubboardsDatabase(final Connection con, final Subboard s) {
        this.con = con;
        this.subboard = s;
    }

    /**
     * Delete the subboard from the database
     *
     * @return the created subboard
     * @throws SQLException if the user could not be deleted
     */
    public Subboard DeleteSubboard() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created user
        Subboard subboard = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, subboard.getSubboardId());

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