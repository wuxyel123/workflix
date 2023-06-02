package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** DAO class responsible for getting all templates from the database */
public class GetTemplatesDatabase {
    private static final String STATEMENT= "SELECT * FROM workflix.template;";
    private final Connection connection;

    /**
     * Initialize the DAO object with a connection to the database
     *
     * @param connection the connection to the database
     */
    public GetTemplatesDatabase(final Connection connection) {
        this.connection = connection;
    }

    /**
     * Get all templates from the database
     *
     * @return the templates
     * @throws SQLException if an error occurred while trying to get the templates
     */
    public List<Template> getTemplates ()throws SQLException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<Template> templates=new ArrayList<Template>();

        try{
            ps=connection.prepareStatement(STATEMENT);
            rs=ps.executeQuery();

            while (rs.next()){
                Template template = new Template();
                template.setImageUrl(rs.getString(Template.IMAGE_URL));
                template.setTemplateName(rs.getString(Template.TEMPLATE_NAME));
                templates.add(template);
            }
        }finally {
            if (rs!=null){
                rs.close();
            }
            if (ps!=null){
                ps.close();
            }
            connection.close();
        }
        return templates;
    }
}
