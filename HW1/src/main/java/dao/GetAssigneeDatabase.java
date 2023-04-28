package dao;

import resource.Assignee;
import resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class GetAssigneeDatabase {

    /**
     * The SQL statement to be executed
     */
    //TODO Put a correct query here
    private static final String STATEMENT = "SELECT u.user_id, u.username, u.profile_picture FROM users u JOIN assignee a ON u.user_id = a.user_id WHERE a.activity_id =? ;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    Assignee assignee;

    public GetAssigneeDatabase(final Connection con, final Assignee assignee) {
        this.con = con;
        this.assignee = assignee;
    }

    public List<User> getAssignee() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, assignee.getActivityId());

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