package dao;

import resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for updating an user in the database
 */
public class UpdateUserDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.users SET username=?, email=?, first_name=?, last_name=?, profile_picture=?, description=? WHERE user_id=? AND password=workflix.sha512(?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be updated
     */
    User user;

    /**
     * Initialize the DAO object with a connection to the database and the object to be updated
     *
     * @param con the connection to the database
     * @param u   the user to be updated
     */
    public UpdateUserDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    /**
     * Update the user in the database
     *
     * @return the updated user
     * @throws SQLException if an error occurred while trying to update the user
     */
    public User updateUser() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created user
        User newUser = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getProfilePicture());
            ps.setString(6, user.getDescription());
            ps.setInt(7, user.getUserId());
            ps.setString(8, user.getPassword());
            rs = ps.executeQuery();

            if (rs.next()) {
                newUser = new User();
                newUser.setUserId(rs.getInt(User.USER_ID));
                newUser.setUsername(rs.getString(User.USERNAME));
                newUser.setEmail(rs.getString(User.EMAIL));
                newUser.setFirstName(rs.getString(User.FIRST_NAME));
                newUser.setLastName(rs.getString(User.LAST_NAME));
                newUser.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                newUser.setDescription(rs.getString(User.DESCRIPTION));
                newUser.setCreateDate(rs.getTimestamp(User.CREATE_DATE));
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
