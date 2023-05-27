package dao;

import resource.UserWorkspace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for inserting a user to the database
 */
public class InsertUserWorkspaceDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO workflix.user_workspace VALUES(?, ?, ?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    UserWorkspace userWorkspace;

    /**
     * Initialize the DAO object with a connection to the database and the object to be inserted
     *
     * @param con the connection to the database
     * @param userWorkspace   the user to be inserted
     */
    public InsertUserWorkspaceDatabase(final Connection con, final UserWorkspace userWorkspace) {
        this.con = con;
        this.userWorkspace = userWorkspace;
    }

    /**
     * Inserts the user to the database
     *
     * @return the created user
     * @throws SQLException if an error occurred during the database operation
     */
    public UserWorkspace insertUserWorkspace() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created user
        UserWorkspace insertUserWorkspace = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, userWorkspace.getUserId());
            pstmt.setInt(2, userWorkspace.getWorkspaceId());
            pstmt.setInt(3, userWorkspace.getPermissionId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                insertUserWorkspace = new UserWorkspace();
                insertUserWorkspace.setUserId(rs.getInt(UserWorkspace.USER_ID));
                insertUserWorkspace.setWorkspaceId(rs.getInt(UserWorkspace.WORKSPACE_ID));
                insertUserWorkspace.setPermissionId(rs.getInt(UserWorkspace.PERMISSION_ID));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

            con.close();
        }

        return insertUserWorkspace;
    }

}
