package dao;

import resource.Subboard;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertSubboarDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO amupark.subboards (type, description, rideid, userid, date_performed, planned) VALUES(?::amupark.subboardscategories, ?, ?, ?, TO_DATE(?, 'YYYY/MM/DD'), ?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The subboards to be inserted
     */
    Subboard subboard;

    public InsertSubboarDatabase(final Connection con, final Subboard s) {
        this.con = con;
        this.subboard = s;
    }

    public Subboard insertSubboards() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created subboards
        Subboard newSubboard = null;
        
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, subboard.getSubboardId());
            pstmt.setInt(2, subboard.getBoardId());
            pstmt.setString(3, subboard.getName());
            pstmt.setInt(4, subboard.getIndex());
            pstmt.setBoolean(5, subboard.getDefaultCompletedActivitySubboard());
            pstmt.setDate(6, (Date) subboard.getCreationTime());


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
