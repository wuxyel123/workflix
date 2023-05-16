package dao;

import resource.Activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteActivityDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT ="DELETE FROM workflix.activities WHERE activity_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    Activities activities;

    public DeleteActivityDatabase(final Connection con, final Activities a) {
        this.con = con;
        this.activities = a;
    }

    public Activities DeleteActivity() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created user
        Activities activities = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, activities.getActivityId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                activities = new Activities();
                activities.setName(rs.getString(Activities.NAME));
                activities.setDescription(rs.getString(Activities.DESCRIPTION));
                activities.setStartDate(rs.getDate(Activities.START_DATE));
                activities.setEndDate(rs.getDate(Activities.END_DATE));
                activities.setWorkedTime(rs.getInt(Activities.WORKED_TIME));
                activities.setIndex(rs.getInt(Activities.INDEX));
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

        return activities;
    }

}