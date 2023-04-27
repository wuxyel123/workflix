package dao;

import resource.UserWorkspace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteUserWorkspaceDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM workflix.user_workspace WHERE user_id=?, workspace_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    UserWorkspace userWorkspace;

    public DeleteUserWorkspaceDatabase(final Connection con, final UserWorkspace userWorkspace) {
        this.con = con;
        this.userWorkspace = userWorkspace;
    }

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
