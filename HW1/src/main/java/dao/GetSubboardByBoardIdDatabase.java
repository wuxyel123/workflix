package dao;

import resource.Subboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetSubboardByBoardIdDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.subboards WHERE board_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The subboards to be inserted
     */
    Subboard subboard;

    public GetSubboardByBoardIdDatabase(final Connection con, final Subboard s) {
        this.con = con;
        this.subboard = s;
    }

    public List<Subboard> getSubboardByBoardId() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Subboard> subboards = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, subboard.getBoardId());


            rs = pstmt.executeQuery();

            while (rs.next()) {
            	Subboard subboard = new Subboard();
                subboard.setSubboardId(rs.getInt(Subboard.SUBBOARD_ID));
                subboard.setBoardId(rs.getInt(Subboard.BOARD_ID));
                subboard.setName(rs.getString(Subboard.NAME));
                subboard.setIndex(rs.getInt(Subboard.INDEX));
                subboard.setDefaultCompletedActivitySubboard(rs.getBoolean(Subboard.DEFAULT_COMPLETED_ACTIVITY_SUBBOARD));
                subboard.setCreationTime(rs.getDate(Subboard.CREATION_TIME));
                subboards.add(subboard);
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

        return subboards;
    }

}
