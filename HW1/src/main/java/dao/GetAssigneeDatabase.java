package dao;

import resource.Assignee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetAssigneeDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.assignee WHERE activity_id=? AND user_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    Assignee assignee;

    public GetAssigneeDatabase(final Connection con, final Assignee a) {
        this.con = con;
        this.assignee = a;
    }

    public Assignee getAssignee() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Assignee assignee = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, assignee.getActivityId());
            ps.setInt(2, assignee.getUserId());

            rs = ps.executeQuery();

            if (rs.next()) {
                assignee = new Assignee();
                assignee.setActivityId(rs.getInt(Assignee.ACTIVITY_ID));
                assignee.setUserId(rs.getInt(Assignee.USER_ID));
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            con.close();
        }
        return assignee;
    }
}