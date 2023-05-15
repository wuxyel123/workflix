package dao;

import resource.WorkSpace;

import java.sql.*;

/**
 * DAO class responsible for deleting a user from the database
 */
public class DeleteWorkspaceByIdDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM workflix.workspace WHERE workspace_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The workspace to be deleted
     */
    WorkSpace workspace;

    /**
     * Initialize the DAO object with a connection to the database and the object to be deleted
     *
     * @param con the connection to the database
     * @param workspace   the workspace to be deleted
     */
    public DeleteWorkspaceByIdDatabase(final Connection con, final WorkSpace workspace) {
        this.con = con;
        this.workspace = workspace;
    }

    /**
     * Delete the workspace from the database
     *
     * @return the deleted workspace
     * @throws SQLException if an error occurred while trying to delete the workspace
     */
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
