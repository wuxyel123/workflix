package dao;

import resource.Comments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateCommentDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.comments SET activity_id=?, user_id=?, comment_text=? WHERE comment_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The comment to be inserted
     */
    Comments comment;

    public UpdateCommentDatabase(final Connection con, final Comments c) {
        this.con = con;
        this.comment = c;
    }

    public Comments UpdateComment() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created comment
        Comments newComment = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, comment.getActivityId());
            ps.setInt(2, comment.getUserId());
            ps.setString(3, comment.getCommentText());
            rs = ps.executeQuery();

            if (rs.next()) {
                newComment = new Comments();
                newComment.setActivityId(rs.getInt(Comments.ACTIVITY_ID));
                newComment.setUserId(rs.getInt(Comments.USER_ID));
                newComment.setCommentText(rs.getString(Comments.COMMENT_TEXT));
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
