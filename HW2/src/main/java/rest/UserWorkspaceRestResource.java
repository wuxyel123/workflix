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
    protected ErrorCode ec = ErrorCode.OK;
    // The response
    protected String response = "";
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
        	UserWorkspace userWorkSpace = UserWorkspace.fromJSON(req.getInputStream());
            userWorkSpace.setWorkspaceId(Integer.parseInt(tokens[4]));
            userWorkSpace = new DeleteUserWorkspaceDatabase(con, userWorkSpace).userWorkspaceDelete();
            if (userWorkSpace==null) {
                initError(ErrorCode.USER_WORKSPACE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = userWorkSpace.toJSON().toString();
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
            userWorkSpace.setWorkspaceId(Integer.parseInt(tokens[4]));

            userWorkSpace = new UpdateUserPermissionDatabase(con, userWorkSpace).workspaceAssignuserpermission();
            if (userWorkSpace==null) {
                initError(ErrorCode.USER_WORKSPACE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = userWorkSpace.toJSON().toString();
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
            userWorkSpace.setWorkspaceId(Integer.parseInt(tokens[4]));

            userWorkSpace = new InsertUserWorkspaceDatabase(con, userWorkSpace).insertUserWorkspace();
            if (userWorkSpace==null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = userWorkSpace.toJSON().toString();
            }
        } catch (SQLException e){
            if (e.getSQLState().equals("23505")) {
                initError(ErrorCode.USER_ALREADY_IN_WORKSPACE);
                logger.warn("User already in workspace");
            }else {
                initError(ErrorCode.INTERNAL_ERROR);
                logger.error("stacktrace:", e);
            }
        }
        catch(Exception e){
            response = e.toString();
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
