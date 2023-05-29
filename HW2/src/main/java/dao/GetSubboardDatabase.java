package dao;

import resource.Subboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for getting a subboard from its ID from the database
 */
public class GetSubboardDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.subboards WHERE subboard_Id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The subboards to be inserted
     */
    Subboard subboard;

    /**
     * Initialize the DAO object with a connection to the database and the object to be inserted
     *
     * @param con the connection to the database
     * @param s   the subboard to be inserted
     */
    public GetSubboardDatabase(final Connection con, final Subboard s) {
        this.con = con;
        this.subboard = s;
    }

    /**
     * Get the subboard from the database
     *
     * @return the subboard
     * @throws SQLException if an error occurred while trying to get the subboard
     */
    public Subboard getSubboard() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created subboards
        Subboard newSubboard = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, subboard.getSubboardId());


            rs = pstmt.executeQuery();

            if (rs.next()) {
            	newSubboard = new Subboard();
            	newSubboard.setSubboardId(rs.getInt(Subboard.SUBBOARD_ID));
            	newSubboard.setBoardId(rs.getInt(Subboard.BOARD_ID));
            	newSubboard.setName(rs.getString(Subboard.NAME));
            	newSubboard.setIndex(rs.getInt(Subboard.INDEX));
            	newSubboard.setDefaultCompletedActivitySubboard(rs.getBoolean(Subboard.DEFAULT_COMPLETED_ACTIVITY_SUBBOARD));
            	newSubboard.setCreationTime(rs.getDate(Subboard.CREATION_TIME));
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

        return newSubboard;
    }

}
