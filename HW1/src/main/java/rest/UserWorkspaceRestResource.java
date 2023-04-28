package rest;

import dao.InsertUserWorkspaceDatabase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import resource.User;
import utils.ErrorCode;
import dao.DeleteUserWorkspaceDatabase;
import dao.UpdateUserPermissionDatabase;

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

            userWorkSpace.setWorkspaceId(Integer.parseInt(tokens[3]));
            if (new DeleteUserWorkspaceDatabase(con, userWorkSpace).userWorkspaceDelete()==null) {
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

            UserWorkspace userWorkSpace = UserWorkspace.fromJSON(req.getInputStream());

            if (new InsertUserWorkspaceDatabase(con, userWorkSpace).insertUserWorkspace()==null) {
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
            UserWorkspace userWorkSpace = UserWorkspace.fromJSON(req.getInputStream());
            userWorkSpace.setWorkspaceId(Integer.parseInt(tokens[3]));

            if (new UpdateUserPermissionDatabase(con, userWorkSpace).workspaceAssignuserpermission()==null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }

    }

    /**Add user
     * */
    public void AddUser() throws IOException{
        try {
            UserWorkspace userWorkSpace = UserWorkspace.fromJSON(req.getInputStream());
            userWorkSpace.setWorkspaceId(Integer.parseInt(tokens[3]));

            if (new InsertUserWorkspaceDatabase(con, userWorkSpace).insertUserWorkspace()==null) {
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
