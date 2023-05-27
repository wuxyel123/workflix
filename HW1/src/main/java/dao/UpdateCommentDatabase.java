package dao;

import resource.Comments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for updating a comment in the database
 */
public class UpdateCommentDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.comments SET comment_text=? WHERE comment_id=? AND activity_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The comment to be inserted
     */
    Comments comment;

    /**
     * Initialize the DAO object with a connection to the database and the object to be updated
     *
     * @param con the connection to the database
     * @param c   the comment to be updated
     */
    public UpdateCommentDatabase(final Connection con, final Comments c) {
        this.con = con;
        this.comment = c;
    }

    /**
     * Update the comment in the database
     *
     * @return the updated comment
     * @throws SQLException if an error occurred while trying to update the comment
     */
    public Comments UpdateComment() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created comment
        Comments newComment = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, comment.getCommentText());
            ps.setInt(2,comment.getCommentId());
            ps.setInt(3,comment.getActivityId());

            rs = ps.executeQuery();

            if (rs.next()) {
                newComment = new Comments();
                newComment.setCommentId(rs.getInt(Comments.COMMENT_ID));
                newComment.setActivityId(rs.getInt(Comments.ACTIVITY_ID));
                newComment.setUserId(rs.getInt(Comments.USER_ID));
                newComment.setCommentText(rs.getString(Comments.COMMENT_TEXT));
                newComment.setCreationTime(rs.getDate(Comments.CREATION_TIME));
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

        return newComment;
    }
}
