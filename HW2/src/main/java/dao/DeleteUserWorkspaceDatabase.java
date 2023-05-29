package dao;

import resource.UserWorkspace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for deleting a user from the database
 */
public class DeleteUserWorkspaceDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM workflix.user_workspace WHERE user_id=? AND workspace_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The userWorkspace to be deleted
     */
    UserWorkspace userWorkspace;

    /**
     * Initialize the DAO object with a connection to the database and the object to be deleted
     *
     * @param con the connection to the database
     * @param userWorkspace   the userWorkspace to be deleted
     */
    public DeleteUserWorkspaceDatabase(final Connection con, final UserWorkspace userWorkspace) {
        this.con = con;
        this.userWorkspace = userWorkspace;
    }

    /**
     * Delete the userWorkspace from the database
     *
     * @return the deleted userWorkspace
     * @throws SQLException if an error occurred while trying to delete the userWorkspace
     */
    public UserWorkspace userWorkspaceDelete() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created user
        UserWorkspace delUserWorkspace = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, userWorkspace.getUserId());
            pstmt.setInt(2, userWorkspace.getWorkspaceId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                delUserWorkspace = new UserWorkspace();
                delUserWorkspace.setUserId(rs.getInt(UserWorkspace.USER_ID));
                delUserWorkspace.setWorkspaceId(rs.getInt(UserWorkspace.WORKSPACE_ID));
                delUserWorkspace.setPermissionId(rs.getInt(UserWorkspace.PERMISSION_ID));
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

        return delUserWorkspace;
    }

}
