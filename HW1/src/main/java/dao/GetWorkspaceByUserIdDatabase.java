package dao;

import resource.User;
import resource.WorkSpace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetWorkspaceByUserIdDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.workspace w JOIN workflix.user_workspace uw ON w.workspace_id = uw.workspace_id WHERE uw.user_id=?;";
    //TODO fix query here
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    User user;

    public GetWorkspaceByUserIdDatabase(final Connection con, final User user) {
        this.con = con;
        this.user = user;
    }

    public List<WorkSpace> getWorkspaceByUserId() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<WorkSpace> workSpaces= new ArrayList<>();

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, user.getUserId());

            rs = ps.executeQuery();

            while (rs.next()) {
                WorkSpace workspace=new WorkSpace();
                workspace.setWorkspaceId(rs.getInt(WorkSpace.WORKSPACE_ID));
                workspace.setWorkspaceName(rs.getString(WorkSpace.WORKSPACE_NAME));
                workspace.setTemplateId(rs.getInt(WorkSpace.TEMPLATE_ID));
                workspace.setCreationTime(rs.getDate(WorkSpace.CREATION_TIME));
                workSpaces.add(workspace);
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
        return workSpaces;
    }

}
