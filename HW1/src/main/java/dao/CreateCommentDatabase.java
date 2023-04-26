package dao;

import resource.Comments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateCommentDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO workflix.comments VALUES(?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    Comments comments;

    public CreateCommentDatabase(final Connection con, final Comments comments) {
        this.con = con;
        this.comments = comments;
    }

    public Comments addComments() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Comments addComments = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, comments.getCommentId());

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