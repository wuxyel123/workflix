package dao;

import resource.WorkSpace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkspaceCreate {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO workflix.workspace VALUES(?, ?, ?, ?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    WorkSpace workspace;

    public WorkspaceCreate(final Connection con, final WorkSpace workspace) {
        this.con = con;
        this.workspace = workspace;
    }

    public WorkSpace insertWorkspace() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created user
        WorkSpace insertedWorkspace = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, workspace.getWorkspaceId());
            pstmt.setString(2, workspace.getWorkspaceName());
            pstmt.setInt(3, workspace.getTemplateId());
            pstmt.setLocalDateTime(4, workspace.getCreationTime());


            rs = pstmt.executeQuery();

            if (rs.next()) {
                insertedWorkspace = new WorkSpace();
                insertedWorkspace.setWorkspaceId(rs.getString(WorkSpace.WORKSPACE_ID));
                insertedWorkspace.setWorkspaceName(rs.getString(WorkSpace.WORKSPACE_NAME));
                insertedWorkspace.setTemplateId(rs.getString(WorkSpace.TEMPLATE_ID));
                insertedWorkspace.setCreationTime(rs.getString(WorkSpace.CREATION_TIME));
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

        return insertedWorkspace;
    }

}
