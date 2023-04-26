package dao;

import resource.WorkSpace;

import java.sql.*;

public class workspaceDeleteByIdDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM workflix.workwpace WHERE workspace_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be deleted
     */
    WorkSpace workspace;

    public workspaceDeleteByIdDatabase(final Connection con, final WorkSpace workspace) {
        this.con = con;
        this.workspace = workspace;
    }

    public WorkSpace workspaceDelete() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created user
        WorkSpace deleteWorkspace = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            
            ps.setInt(1, workspace.getWorkspaceId());

            rs = ps.executeQuery();

            if (rs.next()) {
                deleteWorkspace = new WorkSpace();
                deleteWorkspace.setWorkspaceId(rs.getInt(WorkSpace.WORKSPACE_ID));
                deleteWorkspace.setWorkspaceName(rs.getString(WorkSpace.WORKSPACE_NAME));
                deleteWorkspace.setTemplateId(rs.getInt(WorkSpace.TEMPLATE_ID));
                deleteWorkspace.setCreationTime(rs.getDate(WorkSpace.CREATION_TIME));
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
        return deleteWorkspace;
    }

}
