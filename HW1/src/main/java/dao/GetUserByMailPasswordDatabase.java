package dao;

import resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for getting a user from the database
 */
public class GetUserByMailPasswordDatabase {
    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.users WHERE email=? AND password=workflix.sha512(?);";
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
    public GetUserByMailPasswordDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    /**
     * Get the user from the database
     *
     * @return the user
     * @throws SQLException if an error occurred while trying to get the user
     */
    public User getUserByMailAndPassword() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User response=null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());

            rs = ps.executeQuery();

            if (rs.next()) {
                response = new User();
                response.setUserId(rs.getInt(User.USER_ID));
                response.setUsername(rs.getString(User.USERNAME));
                response.setEmail(rs.getString(User.EMAIL));
                response.setFirstName(rs.getString(User.FIRST_NAME));
                response.setLastName(rs.getString(User.LAST_NAME));
                response.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                response.setDescription(rs.getString(User.DESCRIPTION));
                response.setCreateDate(rs.getDate(User.CREATE_DATE));
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

        return response;
    }
}
