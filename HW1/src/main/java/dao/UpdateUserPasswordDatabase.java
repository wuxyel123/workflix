package dao;

import resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for updating a user's password in the database
 */
public class UpdateUserPasswordDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.users SET password=workflix.sha512(?) WHERE user_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    User user;

    /**
     * Initialize the DAO object with a connection to the database and the object to be updated
     *
     * @param con the connection to the database
     * @param u   the user to be updated
     */
    public UpdateUserPasswordDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    /**
     * Update the user in the database
     *
     * @return the updated user
     * @throws SQLException if an error occurred while trying to update the user
     */
    public User updateUserPassword() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created user
        User newUser = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, user.getPassword());
            ps.setInt(2, user.getUserId());
            rs = ps.executeQuery();

            if (rs.next()) {
                newUser = new User();
                newUser.setUsername(rs.getString(User.USERNAME));
                newUser.setEmail(rs.getString(User.EMAIL));
                newUser.setFirstName(rs.getString(User.FIRST_NAME));
                newUser.setLastName(rs.getString(User.LAST_NAME));
                newUser.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                newUser.setDescription(rs.getString(User.DESCRIPTION));
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

        return newUser;
    }
}
