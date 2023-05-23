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

        // the updated subboard
        Subboard updSubboard = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setString(1, subboards.getName());
            pstmt.setInt(2, subboards.getIndex());
            pstmt.setBoolean(3, subboards.getDefaultCompletedActivitySubboard());
            pstmt.setInt(4, subboards.getSubboardId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                updSubboard = new Subboard();
                updSubboard.setSubboardId(rs.getInt(Subboard.SUBBOARD_ID));
                updSubboard.setBoardId(rs.getInt(Subboard.BOARD_ID));
                updSubboard.setName(rs.getString(Subboard.NAME));
                updSubboard.setIndex(rs.getInt(Subboard.INDEX));
                updSubboard.setDefaultCompletedActivitySubboard(rs.getBoolean(Subboard.DEFAULT_COMPLETED_ACTIVITY_SUBBOARD));
                updSubboard.setCreationTime(rs.getDate(Subboard.CREATION_TIME));
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

        return updSubboard;
    }

}
