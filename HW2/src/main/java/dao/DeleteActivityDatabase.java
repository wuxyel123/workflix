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
    Activities activity;

    public DeleteActivityDatabase(final Connection con, final Activities a) {
        this.con = con;
        this.activity = a;
    }

    public Activities DeleteActivity() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the deleted activity
        Activities deletedActivity = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, activity.getActivityId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                deletedActivity = new Activities();
                deletedActivity.setActivityId(rs.getInt(Activities.ACTIVITY_ID));
                deletedActivity.setSubboardId(rs.getInt(Activities.SUBBOARD_ID));
                deletedActivity.setName(rs.getString(Activities.NAME));
                deletedActivity.setDescription(rs.getString(Activities.DESCRIPTION));
                deletedActivity.setStartDate(rs.getDate(Activities.START_DATE));
                deletedActivity.setEndDate(rs.getDate(Activities.END_DATE));
                deletedActivity.setWorkedTime(rs.getInt(Activities.WORKED_TIME));
                deletedActivity.setIndex(rs.getInt(Activities.INDEX));
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

        return deletedActivity;
    }

}