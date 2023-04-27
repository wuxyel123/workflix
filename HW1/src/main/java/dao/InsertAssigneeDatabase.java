package dao;

import resource.Assignee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertAssigneeDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO workflix.assignee VALUES(?, ?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    Assignee assignee;

    public InsertAssigneeDatabase(final Connection con, final Assignee assignee) {
        this.con = con;
        this.assignee = assignee;
    }

    public Assignee addAssignee() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Assignee addAssignee = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, assignee.getActivityId());
            ps.setInt(2, assignee.getUserId());

            rs = ps.executeQuery();

            if (rs.next()) {
                addAssignee = new Assignee();
                addAssignee.setActivityId(rs.getInt(Assignee.ACTIVITY_ID));
                addAssignee.setUserId(rs.getInt(Assignee.USER_ID));
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
        return addAssignee;
    }
}