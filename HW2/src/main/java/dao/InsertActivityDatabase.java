package dao;

import resource.Activities;

import java.sql.*;

public class InsertActivityDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO workflix.activities(subboard_id, name, start_date, end_date, worked_time,index, description) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    Activities activities;

    public InsertActivityDatabase(final Connection con, final Activities a) {
        this.con = con;
        this.activities = a;
    }

    public Activities insertActivity() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created user
        Activities newActivities = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, activities.getSubboardId());
            ps.setString(2, activities.getName());
            ps.setDate(3, (Date)activities.getStartDate());
            ps.setDate(4, (Date)activities.getEndDate());
            ps.setInt(5, activities.getWorkedTime());
            ps.setInt(6, activities.getIndex());
            ps.setString(7, activities.getDescription());

            rs = ps.executeQuery();

            if (rs.next()) {
                newActivities = new Activities();
                newActivities.setActivityId(rs.getInt(Activities.ACTIVITY_ID));
                newActivities.setSubboardId(rs.getInt(Activities.SUBBOARD_ID));
                newActivities.setName(rs.getString(Activities.NAME));
                newActivities.setStartDate(rs.getDate(Activities.START_DATE));
                newActivities.setEndDate(rs.getDate(Activities.END_DATE));
                newActivities.setWorkedTime(rs.getInt(Activities.WORKED_TIME));
                newActivities.setIndex(rs.getInt(Activities.INDEX));
                newActivities.setDescription(rs.getString(Activities.DESCRIPTION));

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

        return newActivities;
    }
}
