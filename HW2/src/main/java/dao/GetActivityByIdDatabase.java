package dao;

import resource.Activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for getting an activity from the database
 */
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

    /**
     * Initialize the DAO object with a connection to the database and the object to be searched
     *
     * @param con the connection to the database
     * @param a   the activity to be searched
     */
    public GetActivityByIdDatabase(final Connection con, final Activities a) {
        this.con = con;
        this.activity = a;
    }

    /**
     * Get the activity from the database
     *
     * @return the activity
     * @throws SQLException if an error occurred while trying to get the activity
     */
    public Activities getActivityById() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Activities res =null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, activity.getActivityId());

            rs = ps.executeQuery();

            if (rs.next()) {
                res = new Activities();
                res.setActivityId(rs.getInt(Activities.ACTIVITY_ID));
                res.setSubboardId(rs.getInt(Activities.SUBBOARD_ID));
                res.setName(rs.getString(Activities.NAME));
                res.setDescription(rs.getString(Activities.DESCRIPTION));
                res.setStartDate(rs.getDate(Activities.START_DATE));
                res.setEndDate(rs.getDate(Activities.END_DATE));
                res.setWorkedTime(rs.getInt(Activities.WORKED_TIME));
                res.setIndex(rs.getInt(Activities.INDEX));

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
        return res;
    }

}
