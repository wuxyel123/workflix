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

/**
 * This class represents the REST resource "/user"
 */
public class UserWorkspaceRestResource extends RestResource{

    // The operation requested by the client
    protected final String op;
    // The error code
    protected ErrorCode ec = null;
    // The response
    protected String response = null;
    // The tokens of the request
    protected final String[] tokens;

    /**
     * Constructor
     * @param req The request
     * @param res The response
     * @param con The connection to the database
     */
    public UserWorkspaceRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }

    /**
     * Create a user
     * @throws IOException Error in IO operations
     */
    public void DeleteUserWorkSpace() throws IOException{
        try {
        	UserWorkspace userWorkSpace = new UserWorkspace();

            userWorkSpace.setWorkspaceId(Integer.parseInt(tokens[3]));
            if (new DeleteUserWorkspaceDatabase(con, userWorkSpace).userWorkspaceDelete()==null) {
                initError(ErrorCode.USER_WORKSPACE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }

    }

    /**
     * Create a user
     * @throws IOException Error in IO operations
     */
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

    /**
     * Create a user
     * @throws IOException Error in IO operations
     */
    public void AssignUserPermission() throws IOException{
        try {
            UserWorkspace userWorkSpace = UserWorkspace.fromJSON(req.getInputStream());
            userWorkSpace.setWorkspaceId(Integer.parseInt(tokens[3]));

            if (new UpdateUserPermissionDatabase(con, userWorkSpace).workspaceAssignuserpermission()==null) {
                initError(ErrorCode.USER_WORKSPACE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }

    }

    /**
     * Add a user to a workspace
     * @throws IOException Error in IO operations
     */
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

    /**
     * Respond to the client
     * @throws IOException Error in IO operations
     */
    private void respond() throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(response);
    }

    /**
     * Initialize the error
     * @param ec The error code
     */
    private void initError(ErrorCode ec){
        this.ec = ec;
        response = ec.toJSON().toString();
    }
}
