package rest;

import dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ErrorCode;

import resource.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * This class represents the REST resource "/user"
 */
public class UserRestResource extends RestResource{

    // The operation requested by the client
    protected final String op;
    // The error code
    protected ErrorCode ec;
    // The response
    protected String response;
    // The tokens of the request
    protected final String[] tokens;

    /**
     * Constructor
     * @param req The request
     * @param res The response
     * @param con The connection to the database
     */
    public UserRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }

    /**
     * Create a user
     * @throws IOException Error in IO operations
     */
    public void CreateUser() throws IOException {
        try {
            User user = User.fromJSON(req.getInputStream());
            User newUser = new InsertUserDatabase(con, user).insertUser();
            if (newUser == null) {
                initError(ErrorCode.USER_ALREADY_EXISTS);
                logger.warn("User already exists");
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newUser.toJSON().toString();
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Get a user
     * @throws IOException Error in IO operations
     */
    public void GetUserFromId() throws IOException{
        try {
            User user = getUserFromId(Integer.parseInt(tokens[4]));
            if (new GetUserByIdDatabase(con, user).getUserById()==null) {
                initError(ErrorCode.USER_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Get user from mail and password
     * @throws IOException Error in IO operations
     */
    public void GetUserFromMailAndPassword() throws IOException{
        try {
            User user = User.fromJSON(req.getInputStream());
            User newUser = new GetUserByMailPasswordDatabase(con, user).getUserByMailAndPassword();
            if (newUser == null) {
                User checkUser = new GetUserByMailDatabase(con, user).getUserByMail();
                if (checkUser == null) {
                    initError(ErrorCode.USER_NOT_FOUND);
                } else {
                    initError(ErrorCode.USER_NOT_AUTHORIZED);
                }
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newUser.toJSON().toString();
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Update a user
     * @throws IOException Error in IO operations
     */
    public void UpdateUserNoPassword() throws IOException{
        try {
            User user = User.fromJSON(req.getInputStream());
            User newUser = new UpdateUserDatabase(con, user).updateUser();
            if (newUser == null) {
                initError(ErrorCode.USER_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newUser.toJSON().toString();
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Update user password
     * @throws IOException Error in IO operations
     */
    public void UpdateUserPassword() throws IOException{
        try {
            User user = User.fromJSON(req.getInputStream());
            User newUser = new UpdateUserPasswordDatabase(con, user).updateUserPassword();
            if (newUser == null) {
                initError(ErrorCode.USER_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newUser.toJSON().toString();
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Delete a user
     * @throws IOException Error in IO operations
     */
    public void DeleteUser() throws IOException{
        try {
            User user = new User();
            user.setUserId(Integer.parseInt(tokens[5]));
            if (new DeleteUserDatabase(con, user).deleteUser()==null) {
                initError(ErrorCode.USER_NOT_FOUND);
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
        response = "ERROR CODE"+ec.toJSON().toString();
    }

    /**
     * Get user from id
     * @param id The id of the user
     * @return The user
     * @throws SQLException Error in SQL operations
     */
    private User getUserFromId(Integer id) throws SQLException{
        User user = new User();
        user.setUserId(id);
        return new GetUserByIdDatabase(con, user).getUserById();
    }
}
