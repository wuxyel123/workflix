package dao;

import resource.WorkSpace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for getting the workspace by id from the database
 */
public class GetWorkspaceByIdDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.workspace WHERE workspace_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    WorkSpace workSpace;

    /**
     * Initialize the DAO object with a connection to the database and the object to be searched
     *
     * @param con the connection to the database
     * @param workSpace   the workSpace to be searched
     */
    public GetWorkspaceByIdDatabase(final Connection con, final WorkSpace workSpace) {
        this.con = con;
        this.workSpace = workSpace;
    }

    public WorkSpace getWorkspaceById() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        WorkSpace getWorkSpace=null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, workSpace.getWorkspaceId());

            rs = ps.executeQuery();

            if (rs.next()) {
                getWorkSpace = new WorkSpace();
                getWorkSpace.setWorkspaceId(rs.getInt(WorkSpace.WORKSPACE_ID));
                getWorkSpace.setWorkspaceName(rs.getString(WorkSpace.WORKSPACE_NAME));
                getWorkSpace.setTemplateId(rs.getInt(WorkSpace.TEMPLATE_ID));
                getWorkSpace.setCreationTime(rs.getDate(WorkSpace.CREATION_TIME));
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
        return getWorkSpace;
    }

}
