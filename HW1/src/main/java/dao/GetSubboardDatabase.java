package dao;

import resource.Subboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public GetSubboardDatabase(final Connection con, final Subboard s) {
        this.con = con;
        this.subboard = s;
    }

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
