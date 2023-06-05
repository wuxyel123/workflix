package rest;

import dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import resource.Activities;
import resource.Board;
import utils.ErrorCode;

import resource.Comments;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * This class represents the REST resource "/activity
 */
public class CommentRestResource extends RestResource {

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
    public CommentRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }

    /**
     * Get a comment
     * @throws IOException Error in IO operations
     */
    public void GetComments() throws IOException {
        try {
            Activities activity = new Activities();
            activity.setActivityId(Integer.parseInt(tokens[4]));
            List<Comments> comments = new GetCommentDatabase(con, activity).getComments();
            if (comments == null || comments.isEmpty()) {
                initError(ErrorCode.COMMENT_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = Comments.toJSONList(comments).toString();
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        }
        finally {
            respond();
        }
    }

    /**
     * Add a comment
     * @throws IOException Error in IO operations
     */
    public void AddComments() throws IOException {
        try {
            Comments comments = Comments.fromJSON(req.getInputStream());
            comments.setActivityId(Integer.parseInt(tokens[4]));
            Comments newComments = new InsertCommentDatabase(con, comments).addComments();
            if (newComments == null) {
                initError(ErrorCode.INTERNAL_ERROR);
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
            comments.setActivityId(Integer.parseInt(tokens[4]));
            comments.setCommentId(Integer.parseInt(tokens[7]));
            Comments newcomments = new UpdateCommentDatabase(con,comments).UpdateComment();

            if (newcomments == null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newcomments.toJSON().toString();

            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
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
            Comments comment = Comments.fromJSON(req.getInputStream());
            comment.setActivityId(Integer.parseInt(tokens[4]));
            comment.setCommentId(Integer.parseInt(tokens[7]));
            Comments deletedComment =new DeleteCommentDatabase(con, comment).deleteComments();
            if (deletedComment == null) {
                initError(ErrorCode.COMMENT_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = deletedComment.toJSON().toString();
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