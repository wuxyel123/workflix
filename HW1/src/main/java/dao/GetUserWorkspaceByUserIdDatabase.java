package dao;

import resource.UserWorkspace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserWorkspaceByUserIdDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.user_workspace WHERE user_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The park to be searched
     */
    UserWorkspace userWorkspace;

    public GetUserWorkspaceByUserIdDatabase(final Connection con, final UserWorkspace userWorkspace) {
        this.con = con;
        this.userWorkspace = userWorkspace;
    }

    public UserWorkspace getUserWorkspaceByUserId() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserWorkspace userWorkSpace=null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, userWorkspace.getUserId());

            rs = ps.executeQuery();

            if (rs.next()) {
                userWorkSpace = new UserWorkspace();
                userWorkSpace.setWorkspaceId(rs.getInt(UserWorkspace.WORKSPACE_ID));
                userWorkSpace.setPermissionId(rs.getInt(UserWorkspace.PERMISSION_ID));
                userWorkSpace.setUserId(rs.getInt(UserWorkspace.USER_ID));

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
        return userWorkSpace;
    }

}
