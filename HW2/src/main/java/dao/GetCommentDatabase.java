package dao;

import resource.Activities;
import resource.Comments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class responsible for getting a comment from the database
 */
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
    Activities activity;

    /**
     * Initialize the DAO object with a connection to the database and the object to be searched
     *
     * @param con the connection to the database
     * @param activity   the comment to be searched
     */
    public GetCommentDatabase(final Connection con, final Activities activity) {
        this.con = con;
        this.activity = activity;
    }

    /**
     * Get the comments from the database
     *
     * @return the user
     * @throws SQLException if an error occurred while trying to get the user
     */
    public List<Comments> getComments() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //List of comments
        List<Comments> commentsList = new ArrayList<>();


        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, activity.getActivityId());

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