package resource;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Template {

    public static final String TEMPLATE_ID = "template_id";
    public static final String IMAGE_URL="image_url;";
    public static final String TEMPLATE_NAME="template_name";

    private Integer TemplateId;
    private String ImageUrl;
    private String TemplateName;

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
    public static Template fromJSON(InputStream inputStream) throws IOException, JSONException {

        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return fromJSON(new JSONObject(dataString));
    }

    public static Template fromJSON(JSONObject jobj) throws JSONException {
        Integer templateId = jobj.getInt(TEMPLATE_ID);
        String imageUrl = jobj.getString(IMAGE_URL);
        String templateName = jobj.getString(TEMPLATE_NAME);
        Template template=new Template();
        template.setTemplateId(templateId);
        template.setImageUrl(imageUrl);
        template.setTemplateName(templateName);
        return template;
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject templateJSON = new JSONObject();

        templateJSON.put(TEMPLATE_ID, TemplateId);
        templateJSON.put(IMAGE_URL, ImageUrl);
        templateJSON.put(TEMPLATE_NAME, TemplateName);
        return templateJSON;
    }
}
