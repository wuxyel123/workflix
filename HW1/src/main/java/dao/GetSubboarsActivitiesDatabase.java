package dao;

import resource.Activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetSubboarsActivitiesDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.comments WHERE subboard_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     */
    Activities activity;

    public GetSubboarsActivitiesDatabase(final Connection con, final Activities s) {
        this.con = con;
        this.activity = s;
    }

    public Activities insertSubboards() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Activities newActivity = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, activity.getSubboardId());
           
            rs = pstmt.executeQuery();

            if (rs.next()) {
                newActivity = new Activities();
                newActivity.setActivityId(rs.getInt(Activities.ACTIVITY_ID));
                newActivity.setSubboardId(rs.getInt(Activities.SUBBOARD_ID));
                newActivity.setName(rs.getString(Activities.NAME));
                newActivity.setDescription(rs.getString(Activities.DESCRIPTION));
                newActivity.setStartDate(rs.getDate(Activities.START_DATE));
                newActivity.setEndDate(rs.getDate(Activities.END_DATE));
                newActivity.setWorkedTime(rs.getInt(Activities.WORKED_TIME));
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

        return activity;
    }

}
