package dao;

import resource.WorkSpace;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for updating a workspace in the database
 */
public class UpdateWorkspaceDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.workspace SET name=?, template_id=?, creation_time=? WHERE workspace_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The workspace to be updated
     */
    WorkSpace workspace;

    /**
     * Initialize the DAO object with a connection to the database and the object to be updated
     *
     * @param con the connection to the database
     * @param workspace   the workspace to be updated
     */
    public UpdateWorkspaceDatabase(final Connection con, final WorkSpace workspace) {
        this.con = con;
        this.workspace = workspace;
    }

    /**
     * Update the workspace in the database
     *
     * @return the updated workspace
     * @throws SQLException if an error occurred while trying to update the workspace
     */
    public WorkSpace updateWorkspace() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created user
        WorkSpace updatedWorkspace = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setString(1, workspace.getWorkspaceName());
            pstmt.setInt(2, workspace.getTemplateId());
            pstmt.setDate(3, (Date) workspace.getCreationTime());
            pstmt.setInt(4, workspace.getWorkspaceId());




            rs = pstmt.executeQuery();

            if (rs.next()) {
                updatedWorkspace = new WorkSpace();
                updatedWorkspace.setWorkspaceId(rs.getInt(WorkSpace.WORKSPACE_ID));
                updatedWorkspace.setWorkspaceName(rs.getString(WorkSpace.WORKSPACE_NAME));
                updatedWorkspace.setTemplateId(rs.getInt(WorkSpace.TEMPLATE_ID));
                updatedWorkspace.setCreationTime(rs.getDate(WorkSpace.CREATION_TIME));
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

        return updatedWorkspace;
    }

}
