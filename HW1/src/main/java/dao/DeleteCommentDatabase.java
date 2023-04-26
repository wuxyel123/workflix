package dao;

import resource.Comments;

import java.sql.*;

public class DeleteCommentDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM workflix.comments WHERE comment_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The comment to be deleted
     */
    Comments comments;

    public DeleteCommentDatabase(final Connection con, final Comments c) {
        this.con = con;
        this.comments = c;
    }

    public Comments deleteComments() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the delete comment
        Comments comments = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, comments.getCommentId());

            rs = ps.executeQuery();

            if (rs.next()) {
                comments = new Comments();
                comments.setCommentId(rs.getInt(Comments.COMMENT_ID));
                comments.setActivityId(rs.getInt(Comments.ACTIVITY_ID));
                comments.setUserId(rs.getInt(Comments.USER_ID));
                comments.setCommentText(rs.getString(Comments.COMMENT_TEXT));
                comments.setCreationTime(rs.getDate(Comments.CREATION_TIME));

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
        return comments;
    }

}
