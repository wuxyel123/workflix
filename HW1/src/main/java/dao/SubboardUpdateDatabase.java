package dao;

import resource.Subboards;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteSubboardsDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM workflix.Subboards WHERE Subboards_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The device to be deleted
     */
    Subboards subboards;

    public DeleteSubboardsDatabase(final Connection con, final Device u) {
        this.con = con;
        this.device = u;
    }

    public Device DeleteSubboards() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created device
        Subboards deletedSubboards = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, subboards.getSubboardsId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                deletedSubboards = new Subboards();
                deletedSubboards.setSubboardsId(rs.getString(Subboards.SUBBOARD_ID));
                deletedSubboards.setBoardId(rs.getString(Subboards.BOARD_ID));
                deletedSubboards.setName(rs.getString(Subboards.NAME));
                deletedSubboards.setIndex(rs.getString(Subboards.BOARD_ID));
                deletedSubboards.setDefaultCompletedActivitySubboard(rs.getString(Subboards.DEFAULT_COMPLETED_ACTIVITY_SUBBOARD));
                deletedSubboards.setCreationTime(rs.getString(Subboards.CREATION_TIME));
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

        return deletedSubboards;
    }

}
