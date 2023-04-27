package dao;

import resource.Comments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GetCommentDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.comments WHERE activity_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    Comments comments;

    public GetCommentDatabase(final Connection con, final Comments comments) {
        this.con = con;
        this.comments = comments;
    }

    public List<Comments> getComments() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //List of comments
        List<Comments> commentsList = null;


        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, comments.getActivityId());

            rs = ps.executeQuery();


            while (rs.next()) {
                Comments comment = new Comments();
                comment.setCommentId(rs.getInt(Comments.COMMENT_ID));
                comment.setActivityId(rs.getInt(Comments.ACTIVITY_ID));
                comment.setUserId(rs.getInt(Comments.USER_ID));
                comment.setCommentText(rs.getString(Comments.COMMENT_TEXT));
                comment.setCreationTime(rs.getDate(Comments.CREATION_TIME));
                commentsList.add(comment);
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
        return commentsList;
    }
}