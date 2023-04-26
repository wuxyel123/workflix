//package dao;
//
//import resource.Activities;
//import resource.User;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class UpdateActivityDatabase {
//
//    /**
//     * The SQL statement to be executed
//     */
//    private static final String STATEMENT = "UPDATE workflix.activities SET name=?, user_id=?, description=?, index=? WHERE activiity_id=? RETURNING *;";
//    /**
//     * The connection to the database
//     */
//    private final Connection con;
//
//    /**
//     * The activity to be inserted
//     */
//    Activities activity;
//
//    public UpdateActivityDatabase(final Connection con, final activity a) {
//        this.con = con;
//        this.activity = a;
//    }
//
//    public Activities UpdateActivity() throws SQLException {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        // the created comment
//        Activities newActivity = null;
//
//        try {
//            ps = con.prepareStatement(STATEMENT);
//            ps.setInt(1, activity.getActivityId());
//            ps.setInt(2, activity.getIndex());
////            ps.setString(3, activity.getCommentText());
//            rs = ps.executeQuery();
//
//            if (rs.next()) {
//                newActivity = new Activities();
//                newActivity.setName(rs.getString(Activities.NAME));
//                newActivity.setDescription(rs.getString(Activities.DESCRIPTION));
//                newActivity.setWorkedTime(rs.getInt(Activities.WORKED_TIME));
////                newActivity.setUserId(rs.getInt(Comments.USER_ID));
////                newActivity.setCommentText(rs.getString(Comments.COMMENT_TEXT));
//            }
//        } finally {
//            if (rs != null) {
//                rs.close();
//            }
//
//            if (ps != null) {
//                ps.close();
//            }
//
//            con.close();
//        }
//
//        return newActivity;
//    }
//}
