package dao;

import resource.Activities;
import resource.Assignee;
import resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO class responsible for getting the assignee from the database
 */
public class GetAssigneeDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT u.user_id, u.username, u.profile_picture FROM workflix.users u JOIN workflix.assignee a ON u.user_id = a.user_id WHERE a.activity_id =? ;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The Activity to be searched
     */
    Activities activity;

    /**
     * Initialize the DAO object with a connection to the database and the object to be searched
     *
     * @param con the connection to the database
     * @param activity   the assignee to be searched
     */
    public GetAssigneeDatabase(final Connection con, final Activities activity) {
        this.con = con;
        this.activity = activity;
    }

    /**
     * Get the assignee from the database
     *
     * @return the assignee
     * @throws SQLException if an error occurred while trying to get the assignee
     */
    public List<User> getAssignee() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, activity.getActivityId());

            rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt(User.USER_ID));
                user.setUsername(rs.getString(User.USERNAME));
                user.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                users.add(user);
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
        return users;
    }
}