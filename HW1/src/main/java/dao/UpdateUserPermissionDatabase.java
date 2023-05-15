package dao;

import resource.UserWorkspace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for updating a user's permission in the database
 */
public class UpdateUserPermissionDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.user_workspace SET permission_id=? WHERE WHERE user_id=?, workspace_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The userWorkspace to be updated
     */
    UserWorkspace userWorkspace;

    /**
     * Initialize the DAO object with a connection to the database and the object to be updated
     *
     * @param con the connection to the database
     * @param u   the userWorkspace to be updated
     */
    public UpdateUserPermissionDatabase(final Connection con, final UserWorkspace userWorkspace) {
        this.con = con;
        this.userWorkspace = userWorkspace;
    }

    /**
     * Update the userWorkspace in the database
     *
     * @return the updated userWorkspace
     * @throws SQLException if an error occurred while trying to update the userWorkspace
     */
    public UserWorkspace workspaceAssignuserpermission() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created user
        UserWorkspace workspaceAssigneduserpermission = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, userWorkspace.getPermissionId());
            pstmt.setInt(2, userWorkspace.getUserId());
            pstmt.setInt(3, userWorkspace.getWorkspaceId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                workspaceAssigneduserpermission = new UserWorkspace();
                workspaceAssigneduserpermission.setUserId(rs.getInt(UserWorkspace.USER_ID));
                workspaceAssigneduserpermission.setWorkspaceId(rs.getInt(UserWorkspace.WORKSPACE_ID));
                workspaceAssigneduserpermission.setPermissionId(rs.getInt(UserWorkspace.PERMISSION_ID));
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

        return workspaceAssigneduserpermission;
    }

}
