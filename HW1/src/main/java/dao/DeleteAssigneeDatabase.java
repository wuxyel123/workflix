package dao;

import resource.Assignee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteAssigneeDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM workflix.assignee WHERE activity_id=? AND user_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    Assignee assignee;

    public DeleteAssigneeDatabase(final Connection con, final Assignee a) {
        this.con = con;
        this.assignee = a;
    }

    public Assignee deleteAssignee() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Assignee deletedAssignee = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, assignee.getActivityId());
            ps.setInt(2, assignee.getUserId());

            rs = ps.executeQuery();

            if (rs.next()) {
                deletedAssignee = new Assignee();
                deletedAssignee.setActivityId(rs.getString(Assignee.ACTIVITY_ID));
                deletedAssignee.setUserId(rs.getString(Assignee.USER_ID));
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
        return deletedAssignee;
    }
}