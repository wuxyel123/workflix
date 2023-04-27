package dao;

import resource.Activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetActivityByIdDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.activities WHERE activity_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The activity to be searched
     */
    Activities activity;

    public GetActivityByIdDatabase(final Connection con, final Activities a) {
        this.con = con;
        this.activity = a;
    }

    public Activities getActivityById() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Activities activity =null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, activity.getActivityId());

            rs = ps.executeQuery();

            if (rs.next()) {
                activity = new Activities();
                activity.setActivityId(rs.getInt(Activities.ACTIVITY_ID));
                activity.setSubboardId(rs.getInt(Activities.SUBBOARD_ID));
                activity.setName(rs.getString(Activities.NAME));
                activity.setDescription(rs.getString(Activities.DESCRIPTION));
                activity.setStartDate(rs.getDate(Activities.START_DATE));
                activity.setEndDate(rs.getDate(Activities.END_DATE));
                activity.setWorkedTime(rs.getInt(Activities.WORKED_TIME));
                activity.setIndex(rs.getInt(Activities.INDEX));

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
        return activity;
    }

}
