package dao;

import resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for getting a user from the database
 */
public class GetUserByIdDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.users WHERE user_id=?;";
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
    public GetUserByIdDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    /**
     * Get the user from the database
     *
     * @return the user
     * @throws SQLException if an error occurred while trying to get the user
     */
    public User getUserById() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User result = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, user.getUserId());

            rs = ps.executeQuery();

            if (rs.next()) {
                result = new User();
                result.setUserId(rs.getInt(User.USER_ID));
                result.setUsername(rs.getString(User.USERNAME));
                result.setEmail(rs.getString(User.EMAIL));
                result.setFirstName(rs.getString(User.FIRST_NAME));
                result.setLastName(rs.getString(User.LAST_NAME));
                result.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                result.setDescription(rs.getString(User.DESCRIPTION));
                result.setCreateDate(rs.getTimestamp(User.CREATE_DATE));

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
        return result;
    }

}
