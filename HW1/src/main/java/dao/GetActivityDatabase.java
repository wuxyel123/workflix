package dao;

import resource.Activities;
import resource.Subboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class responsible for getting activities of a subboard from the database
 */
public class GetActivityDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.activities;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * Initialize the DAO object with a connection to the database and the object to get
     *
     * @param con the connection to the database
     */
    public GetActivityDatabase(final Connection con) {
        this.con = con;
    }

    /**
     * Get the activities from the database
     *
     * @return the activity
     * @throws SQLException if an error occurred while trying to get the activity
     */
    public List<Activities> getActivity() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        /**
         * The activities to get
         */
        List<Activities> activities = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Activities newActivity = new Activities();

//                newActivity.setActivityId(rs.getInt(Activities.ACTIVITY_ID));
                newActivity.setSubboardId(rs.getInt(Activities.SUBBOARD_ID));
                newActivity.setName(rs.getString(Activities.NAME));
                newActivity.setDescription(rs.getString(Activities.DESCRIPTION));
                newActivity.setStartDate(rs.getDate(Activities.START_DATE));
                newActivity.setEndDate(rs.getDate(Activities.END_DATE));
                newActivity.setWorkedTime(rs.getInt(Activities.WORKED_TIME));
                newActivity.setIndex(rs.getInt(Activities.INDEX));

                activities.add(newActivity);
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
