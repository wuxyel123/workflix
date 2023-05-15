package dao;

import resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for getting a user from the database
 */
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

    /**
     * Initialize the DAO object with a connection to the database and the object to be searched
     *
     * @param con the connection to the database
     * @param u   the user to be searched
     */
    public GetUserByMailDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    /**
     * Get the user from the database
     *
     * @return the user
     * @throws SQLException if an error occurred while trying to get the user
     */
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
