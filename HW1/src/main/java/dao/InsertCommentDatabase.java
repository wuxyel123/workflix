package dao;

import resource.Comments;

import java.sql.*;

/**
 * DAO class responsible for inserting a comment to the database
 */
public class InsertCommentDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO workflix.comments(activity_id, user_id, comment_text) VALUES(?,?,?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The comment to be inserted
     */
    Comments comments;

    /**
     * Initialize the DAO object with a connection to the database and the object to be inserted
     *
     * @param con the connection to the database
     * @param comments   the assignee to be inserted
     */
    public InsertCommentDatabase(final Connection con, final Comments comments) {
        this.con = con;
        this.comments = comments;
    }

    /**
     * Insert the assignee from the database
     *
     * @return the assignee
     * @throws SQLException if an error occurred while trying to insert the assignee
     */
    public Comments addComments() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Comments addComments = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, comments.getActivityId());
            ps.setInt(2, comments.getUserId());
            ps.setString(3, comments.getCommentText());

            rs = ps.executeQuery();

            if (rs.next()) {
                addComments = new Comments();
                addComments.setCommentId(rs.getInt(Comments.COMMENT_ID));
                addComments.setActivityId(rs.getInt(Comments.ACTIVITY_ID));
                addComments.setUserId(rs.getInt(Comments.USER_ID));
                addComments.setCommentText(rs.getString(Comments.COMMENT_TEXT));
                addComments.setCreationTime(rs.getDate(Comments.CREATION_TIME));
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
        return addComments;
    }
}