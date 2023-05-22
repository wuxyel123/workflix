package dao;

import resource.User;

import java.sql.*;

/**
 * DAO class responsible for deleting a user from the database
 */
public class DeleteUserDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM workflix.users WHERE email=? AND user_id=? AND password=workflix.sha512(?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be deleted
     */
    User user;

    /**
     * Initialize the DAO object with a connection to the database and the object to be deleted
     *
     * @param con the connection to the database
     * @param u   the user to be deleted
     */
    public DeleteUserDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    /**
     * Delete the user from the database
     *
     * @return the deleted user
     * @throws SQLException if an error occurred while trying to delete the user
     */
    public User deleteUser() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created user
        User deletedUser = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, user.getEmail());
            ps.setInt(2, user.getUserId());
            ps.setString(3, user.getPassword());

            rs = ps.executeQuery();

            if (rs.next()) {
                deletedUser = new User();
                deletedUser.setUserId(rs.getInt(User.USER_ID));
                deletedUser.setUsername(rs.getString(User.USERNAME));
                deletedUser.setEmail(rs.getString(User.EMAIL));
                deletedUser.setFirstName(rs.getString(User.FIRST_NAME));
                deletedUser.setLastName(rs.getString(User.LAST_NAME));
                deletedUser.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                deletedUser.setDescription(rs.getString(User.DESCRIPTION));
                deletedUser.setCreateDate(rs.getTimestamp(User.CREATE_DATE));

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
