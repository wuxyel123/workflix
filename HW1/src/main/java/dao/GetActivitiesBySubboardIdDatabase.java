package dao;

import resource.Activities;
import resource.Subboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO class responsible for getting activities of a subboard from the database
 */
public class GetActivitiesBySubboardIdDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.activities WHERE subboard_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The activities to be searched
     */
    Subboard subboard;

    /**
     * Initialize the DAO object with a connection to the database and the object to be searched
     *
     * @param con the connection to the database
     * @param s   the subboard to be searched
     */
    public GetActivitiesBySubboardIdDatabase(final Connection con, final Subboard s) {
        this.con = con;
        this.subboard = s;
    }

    /**
     * Get the activities from the database
     *
     * @return the activities
     * @throws SQLException if an error occurred while trying to get the activity
     */
    public List<Activities> getActivitiesBySubboardId() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Activities> activities =null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, subboard.getSubboardId());

            rs = ps.executeQuery();

            while (rs.next()) {
                Activities activity = new Activities();
                activity.setActivityId(rs.getInt(Activities.ACTIVITY_ID));
                activity.setSubboardId(rs.getInt(Activities.SUBBOARD_ID));
                activity.setName(rs.getString(Activities.NAME));
                activity.setDescription(rs.getString(Activities.DESCRIPTION));
                activity.setStartDate(rs.getDate(Activities.START_DATE));
                activity.setEndDate(rs.getDate(Activities.END_DATE));
                activity.setWorkedTime(rs.getInt(Activities.WORKED_TIME));
                activity.setIndex(rs.getInt(Activities.INDEX));

                activities.add(activity);

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
        return activities;
    }

}
