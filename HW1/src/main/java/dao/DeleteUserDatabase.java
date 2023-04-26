package dao;

import resource.User;

import java.sql.*;

public class DeleteUserDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM workflix.users WHERE email=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be deleted
     */
    User user;

    public DeleteUserDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    public User deleteUser() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created user
        User deletedUser = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, user.getEmail());

            rs = ps.executeQuery();

            if (rs.next()) {
                deletedUser = new User();
                deletedUser.setUsername(rs.getString(User.USERNAME));
                deletedUser.setPassword(rs.getString(User.PASSWORD));
                deletedUser.setEmail(rs.getString(User.EMAIL));
                deletedUser.setFirstName(rs.getString(User.FIRST_NAME));
                deletedUser.setLastName(rs.getString(User.LAST_NAME));
                deletedUser.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                deletedUser.setDescription(rs.getString(User.DESCRIPTION));

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
        return deletedUser;
    }

}
