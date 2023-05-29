package resource;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.ResourceValueChecker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Template {

    /** Set of constants with the same value as the DB field, useful in DAOs */
    public static final String TEMPLATE_ID = "template_id";
    public static final String IMAGE_URL="image_url;";
    public static final String TEMPLATE_NAME="template_name";

    /** Set of private fields, each one is a DB field */
    private Integer TemplateId;
    private String ImageUrl;
    private String TemplateName;

    /** Getters and setters for each private field */
    public Integer getTemplateId() {
        return TemplateId;
    }
    public String getImageUrl(){
        return ImageUrl;
    }
    public String getTemplateName(){
        return TemplateName;
    }

    public void setTemplateId(Integer TemplateId) {
        this.TemplateId = TemplateId;
    }
    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }
    public void setTemplateName(String TemplateName) {
        this.TemplateName = TemplateName;
    }


    /**
     * Method to convert a JSONObject to a Template object
     * @param inputStream the input stream containing data
     * @return the Template object
     * @throws JSONException if there is an error in the conversion
     * @throws IOException if input stream cannot be read
     */
    public static List<Template> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<Template> templates = new ArrayList<>();
        JSONArray templatesJSONList = jobj.getJSONArray("templates-list");

        for(int i=0; i<templatesJSONList.length(); i++){
            templates.add(fromJSON(templatesJSONList.getJSONObject(i)));
        }

        return templates;
    }

    /**
     * Method to convert a JSONObject to a Template object
     * @param inputStream the input stream containing data
     * @return the Template object
     * @throws JSONException if there is an error in the conversion
     * @throws IOException if input stream cannot be read
     */
    public static Template fromJSON(InputStream inputStream) throws IOException, JSONException {

        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return fromJSON(new JSONObject(dataString));
    }

    /**
     * Method to convert a JSONObject to a Template object
     * @param jobj the JSONObject containing data
     * @return the Template object
     * @throws JSONException if there is an error in the conversion
     */
    public static Template fromJSON(JSONObject jobj) throws JSONException {

        // Get each field from the JSON object and create a Template object
        Template template=new Template();
        template.setTemplateId(ResourceValueChecker.getValidInteger(jobj.get(TEMPLATE_ID)));
        template.setImageUrl(ResourceValueChecker.getValidString(jobj.get(IMAGE_URL)));
        template.setTemplateName(ResourceValueChecker.getValidString(jobj.get(TEMPLATE_NAME)));
        return template;
    }

    /**
     * Method to convert a Template object to a JSONObject
     * @return the JSONObject representing the Template object
     * @throws JSONException if an error occurs during the JSON creation
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject templateJSON = new JSONObject();

        templateJSON.put(TEMPLATE_ID, TemplateId);
        templateJSON.put(IMAGE_URL, ImageUrl);
        templateJSON.put(TEMPLATE_NAME, TemplateName);
        return templateJSON;
    }
}
