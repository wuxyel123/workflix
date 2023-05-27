package dao;

import resource.Comments;

import java.sql.*;

/**
 * DAO class to delete a comment from the database
 */
public class DeleteCommentDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM workflix.comments WHERE comment_id=? AND activity_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The comment to be deleted
     */
    Comments comments;

    /**
     * Initialize the DAO object with a connection to the database and the object to be deleted
     *
     * @param con the connection to the database
     * @param c   the comment to be deleted
     */
    public DeleteCommentDatabase(final Connection con, final Comments c) {
        this.con = con;
        this.comments = c;
    }

    /**
     * Deletes the comment from the database
     *
     * @return the deleted comment
     * @throws SQLException if an error occurred during the deletion
     */
    public Comments deleteComments() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the delete comment
        Comments deletedComment = new Comments();

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, comments.getCommentId());
            ps.setInt(2, comments.getActivityId());

            rs = ps.executeQuery();

            if (rs.next()) {
                deletedComment = new Comments();
                deletedComment.setCommentId(rs.getInt(Comments.COMMENT_ID));
                deletedComment.setActivityId(rs.getInt(Comments.ACTIVITY_ID));
                deletedComment.setUserId(rs.getInt(Comments.USER_ID));
                deletedComment.setCommentText(rs.getString(Comments.COMMENT_TEXT));
                deletedComment.setCreationTime(rs.getDate(Comments.CREATION_TIME));

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
        return deletedComment;
    }

}
