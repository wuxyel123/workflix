package dao;

import resource.WorkSpace;

import java.sql.*;

public class InsertWorkspaceDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO workflix.workspace(workspace_name, template_id) VALUES(?, ?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    WorkSpace workspace;

    public InsertWorkspaceDatabase(final Connection con, final WorkSpace workspace) {
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

            pstmt.setString(1, workspace.getWorkspaceName());
            pstmt.setInt(2, workspace.getTemplateId());


            rs = pstmt.executeQuery();

            if (rs.next()) {
                insertedWorkspace = new WorkSpace();
                insertedWorkspace.setWorkspaceId(rs.getInt(WorkSpace.WORKSPACE_ID));
                insertedWorkspace.setWorkspaceName(rs.getString(WorkSpace.WORKSPACE_NAME));
                insertedWorkspace.setTemplateId(rs.getInt(WorkSpace.TEMPLATE_ID));
                insertedWorkspace.setCreationTime(rs.getDate(WorkSpace.CREATION_TIME));
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
