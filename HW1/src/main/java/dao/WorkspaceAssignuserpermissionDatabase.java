package dao;

import resource.UserWorkspace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkspaceAssignuserpermissionDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.UserWorkspace SET permission_id=? WHERE WHERE user_id=? workspace_id=? permission_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    UserWorkspace userWorkspace;

    public WorkspaceAssignuserpermissionDatabase(final Connection con, final UserWorkspace userWorkspace) {
        this.con = con;
        this.userWorkspace = userWorkspace;
    }

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
            pstmt.setInt(4, userWorkspace.getPermissionId());

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
