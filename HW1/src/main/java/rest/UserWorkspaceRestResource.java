package rest;

import dao.WorkspaceAddUserDatabase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ErrorCode;
import dao.UserWorkspaceDeleteDatabase;
import dao.WorkspaceAssignuserpermissionDatabase;

import resource.UserWorkspace;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UserWorkspaceRestResource extends RestResource{

    protected final String op;
    protected ErrorCode ec = null;
    protected String response = null;
    protected final String[] tokens;

    public UserWorkspaceRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }


    public void DeleteUserWorkSpace() throws IOException{
        try {
        	UserWorkspace userWorkSpace = new UserWorkspace();

            userWorkSpace.setWorkspaceId(Integer.parseInt(tokens[5]));
            if (new UserWorkspaceDeleteDatabase(con, userWorkSpace).userWorkspaceDelete()==null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }

    }


    public void CreateUserWorkSpace() throws IOException{
        try {
            UserWorkspace userWorkSpace = new UserWorkspace();

            userWorkSpace.setUserId(Integer.parseInt(tokens[5]));
            userWorkSpace.setWorkspaceId(Integer.parseInt(tokens[6]));
            userWorkSpace.setPermissionId(Integer.parseInt(tokens[7]));

            if (new WorkspaceAddUserDatabase(con, userWorkSpace).insertUserWorkspace()==null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }

    }


    public void AssignUserPermission() throws IOException{
        try {
            UserWorkspace userWorkSpace = new UserWorkspace();

            userWorkSpace.setUserId(Integer.parseInt(tokens[5]));
            userWorkSpace.setWorkspaceId(Integer.parseInt(tokens[6]));
            userWorkSpace.setPermissionId(Integer.parseInt(tokens[7]));


            if (new WorkspaceAssignuserpermissionDatabase(con, userWorkSpace).workspaceAssignuserpermission()==null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }

    }



    private void respond() throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(response);
    }
    private void initError(ErrorCode ec){
        this.ec = ec;
        response = ec.toJSON().toString();
    }
}
