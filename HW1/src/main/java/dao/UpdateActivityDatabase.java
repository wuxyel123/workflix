package dao;

import resource.Activities;

import java.sql.*;

public class UpdateActivityDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.activities SET subboard_id=?, name=?, description=?,end_date=?,worked_time=?,index=? WHERE activiity_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The activity to be updated
     */
    Activities activity;

    public UpdateActivityDatabase(final Connection con, final Activities a) {
        this.con = con;
        this.activity = a;
    }

    public Activities UpdateActivity() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created activity
        Activities newActivity = null;

        try {
            ps = con.prepareStatement(STATEMENT);

            ps.setInt(1, activity.getSubboardId());
            ps.setString(2, activity.getName());
            ps.setString(3, activity.getDescription());
            ps.setDate(4, (Date) activity.getEndDate());
            ps.setInt(5, activity.getWorkedTime());
            ps.setInt(6, activity.getIndex());
            ps.setInt(7, activity.getActivityId());

            rs = ps.executeQuery();

            if (rs.next()) {
                newActivity = new Activities();
                newActivity.setSubboardId(rs.getInt(Activities.SUBBOARD_ID));
                newActivity.setName(rs.getString(Activities.NAME));
                newActivity.setDescription(rs.getString(Activities.DESCRIPTION));
                newActivity.setStartDate(rs.getDate(Activities.START_DATE));
                newActivity.setEndDate(rs.getDate(Activities.END_DATE));
                newActivity.setWorkedTime(rs.getInt(Activities.WORKED_TIME));
                newActivity.setIndex(rs.getInt(Activities.INDEX));
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

        return newActivity;
    }
}
