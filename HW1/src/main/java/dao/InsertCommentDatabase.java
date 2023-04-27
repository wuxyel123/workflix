package dao;

import resource.Comments;

import java.sql.*;

public class InsertCommentDatabase {

    /**
     * The SQL statement to be executed
     *
     * activity_id INT NOT NULL,
     *                           user_id INT NOT NULL,
     *                           comment_text TEXT NOT NULL,
     *                           creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     */
    private static final String STATEMENT = "INSERT INTO workflix.comments VALUES(?,?,?,?,?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    Comments comments;

    public InsertCommentDatabase(final Connection con, final Comments comments) {
        this.con = con;
        this.comments = comments;
    }

    public Comments addComments() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Comments addComments = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setNull(1, Types.NULL);
            ps.setInt(2, comments.getActivityId());
            ps.setInt(3, comments.getUserId());
            ps.setString(4, comments.getCommentText());
            ps.setDate(5, new Date(System.currentTimeMillis()));

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