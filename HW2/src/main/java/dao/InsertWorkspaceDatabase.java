package dao;

import resource.WorkSpace;

import java.sql.*;

/**
 * DAO class responsible for inserting a workspace to the database
 */
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

    /**
     * Initialize the DAO object with a connection to the database and the object to be inserted
     *
     * @param con the connection to the database
     * @param workspace   the user to be inserted
     */
    public InsertWorkspaceDatabase(final Connection con, final WorkSpace workspace) {
        this.con = con;
        this.workspace = workspace;
    }

    /**
     * Execute the SQL statement to insert the user to the database
     *
     * @return the created user
     * @throws SQLException if any error occurred when inserting the user to the database
     */
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
