package dao;

import resource.Subboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for updating a subboard in the database
 */
public class UpdateSubboardDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.subboards SET name=?, index=?, default_completed_activity_subboard=? WHERE subboard_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The subboard to be updated
     */
    Subboard subboards;

    /**
     * Initialize the DAO object with a connection to the database and the object to be updated
     *
     * @param con the connection to the database
     * @param s   the subboard to be updated
     */
    public UpdateSubboardDatabase(final Connection con, final Subboard s) {
        this.con = con;
        this.subboards = s;
    }

    /**
     * Update the subboard in the database
     *
     * @return the updated subboard
     * @throws SQLException if an error occurred while trying to update the subboard
     */
    public Subboard UpdateSubboards() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created device
        Subboard subboard = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setString(1, subboards.getName());
            pstmt.setInt(2, subboards.getIndex());
            pstmt.setBoolean(3, subboards.getDefaultCompletedActivitySubboard());
            pstmt.setInt(4, subboards.getSubboardId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                subboard = new Subboard();
                subboard.setSubboardId(rs.getInt(subboard.SUBBOARD_ID));
                subboard.setBoardId(rs.getInt(subboard.BOARD_ID));
                subboard.setName(rs.getString(subboard.NAME));
                subboard.setIndex(rs.getInt(subboard.INDEX));
                subboard.setDefaultCompletedActivitySubboard(rs.getBoolean(subboard.DEFAULT_COMPLETED_ACTIVITY_SUBBOARD));
                subboard.setCreationTime(rs.getDate(subboard.CREATION_TIME));
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
