package dao;

import resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for inserting a user to the database
 */
public class InsertUserDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO workflix.users(username, password, email, first_name, last_name, profile_picture, description) VALUES (?, sha512(?), ?, ?, ?, ?, ?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    User user;

    /**
     * Initialize the DAO object with a connection to the database and the object to be inserted
     *
     * @param con the connection to the database
     * @param u   the user to be inserted
     */
    public InsertUserDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    /**
     * Inserts the user to the database
     *
     * @return the created user
     * @throws SQLException if an error occurred during the database operation
     */
    public User insertUser() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created user
        User newUser = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getProfilePicture());
            ps.setString(7, user.getDescription());
            rs = ps.executeQuery();

            if (rs.next()) {
                newUser = new User();
                newUser.setUsername(rs.getString(User.USERNAME));
                newUser.setPassword(rs.getString(User.PASSWORD));
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
