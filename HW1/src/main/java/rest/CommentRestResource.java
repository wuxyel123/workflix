package rest;

import dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import resource.Board;
import utils.ErrorCode;

import resource.Comments;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class represents the REST resource "/activity
 */
public class CommentRestResource extends RestResource {

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
    public CommentRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }

    /**
     * Get a board
     * @throws IOException Error in IO operations
     */
    public void GetComments() throws IOException {
        try {
            Comments comments = new Comments();
            comments.setActivityId(Integer.parseInt(tokens[6]));
            if (new GetCommentDatabase(con, comments).getComments() == null) {
                initError(ErrorCode.COMMENT_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }
    }

    /**
     * Add a board
     * @throws IOException Error in IO operations
     */
    public void AddComments() throws IOException {
        try {
            Comments comments = Comments.fromJSON(req.getInputStream());
            Comments newComments = new InsertCommentDatabase(con, comments).addComments();
            if (newComments == null) {
                initError(ErrorCode.USER_ALREADY_EXISTS);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newComments.toJSON().toString();
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }
    }
    /**
     * update the comment
     * @throws IOException Error in IO operations
     */
    public void UpdateComment() throws IOException {
        try {
            Comments comments = Comments.fromJSON((req.getInputStream()));
            comments.setActivityId(Integer.parseInt(tokens[6]));
            Comments newcomments = new UpdateCommentDatabase(con,comments).UpdateComment();

            if (newcomments == null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
                response = newcomments.toJSON().toString();

            }
        } catch (SQLException e) {
            ec = ErrorCode.OK;
            res.setContentType("application/json");
        } finally {
            respond();
        }

    }

    /**
     * Delete a comment
     * @throws IOException Error in IO operations
     */
    public void DeleteComment() throws IOException {
        try {
            Comments comments = new Comments();
            comments.setActivityId(Integer.parseInt(tokens[6]));
            if (new DeleteCommentDatabase(con, comments).deleteComments() == null) {
                initError(ErrorCode.COMMENT_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }

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
    private void initError(ErrorCode ec) {
        this.ec = ec;
        response = ec.toJSON().toString();
    }
}