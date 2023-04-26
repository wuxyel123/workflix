package dao;

import resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserByMailDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.users WHERE email=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    User user;

    public GetUserByMailDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    public User getUserByMail() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user=null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, user.getEmail());

            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString(User.USERNAME));
                user.setPassword(rs.getString(User.PASSWORD));
                user.setEmail(rs.getString(User.EMAIL));
                user.setFirstName(rs.getString(User.FIRST_NAME));
                user.setLastName(rs.getString(User.LAST_NAME));
                user.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                user.setDescription(rs.getString(User.DESCRIPTION));

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

        return user;
    }

}
