package dao;

import resource.WorkSpace;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkspaceUpdateDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.workspace SET name=? template_id=?, creation_time=? WHERE workspace_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    WorkSpace workspace;

    public WorkspaceUpdateDatabase(final Connection con, final WorkSpace workspace) {
        this.con = con;
        this.workspace = workspace;
    }

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
