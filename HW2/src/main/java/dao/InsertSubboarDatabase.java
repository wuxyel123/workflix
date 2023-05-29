package dao;

import resource.Subboard;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for inserting a subboard to the database
 */
public class InsertSubboarDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO workflix.subboards (board_id, name, index, default_completed_activity_subboard) VALUES(?,?,?,?) RETURNING *;";
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
     * @param s   the subboards to be inserted
     */
    public InsertSubboarDatabase(final Connection con, final Subboard s) {
        this.con = con;
        this.subboard = s;
    }

    /**
     * Inserts the subboard to the database
     *
     * @return the created subboards
     * @throws SQLException if an error occurred during the database operation
     */
    public Subboard insertSubboards() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created subboards
        Subboard newSubboard = null;
        
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, subboard.getBoardId());
            pstmt.setString(2, subboard.getName());
            pstmt.setInt(3, subboard.getIndex());
            pstmt.setBoolean(4, subboard.getDefaultCompletedActivitySubboard());


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
