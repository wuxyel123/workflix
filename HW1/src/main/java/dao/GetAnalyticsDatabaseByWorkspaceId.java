package dao;

import resource.Analytics;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetAnalyticsDatabaseByWorkspaceId {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.user_analytics_per_workspace WHERE workspace_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    Analytics analytics;

    public GetAnalyticsDatabaseByWorkspaceId(final Connection con, final Analytics comments) {
        this.con = con;
        this.analytics = analytics;
    }

    public Analytics getAnalytics() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Analytics newAnalytics = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, newAnalytics.getWorkspaceId());
            rs = ps.executeQuery();

            if (rs.next()) {
                newAnalytics = new Analytics();
                newAnalytics.setUsername(rs.getString(Analytics.USERNAME));
                newAnalytics.setWorkspaceId(rs.getString(Analytics.WORKSPACE_ID));
                newAnalytics.setWorkspaceName(rs.getString(Analytics.WORKSPACE_NAME));
                newAnalytics.setNumCompletedActivities(rs.getString(Analytics.NUM_COMPLETED_ACTIVITIES));
                newAnalytics.setNumTotalActivities(rs.getString(Analytics.NUM_TOTAL_ACTIVITIES));
                newAnalytics.setTotalWorkedTime(rs.getString(Analytics.TOTAL_WORKED_TIME));
                newAnalytics.setNumComments(rs.getString(Analytics.NUM_COMMENTS));
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
        return newAnalytics;
    }
}
