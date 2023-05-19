package resource;

import org.json.JSONObject;

/**
 * Class representing a Message object
 */
public class Message {

    /**
     * Set of private fields
     */
    final boolean error;
    final String message;


    /**
     * Constructor for Message
     * @param error boolean
     * @param message String
     */
    public Message(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    /**
     * Getters for each private field
     */
    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return error;
    }

    /**
     * Convert Message object to JSON
     * @return JSONObject
     */
    public JSONObject toJSON(){
        JSONObject result = new JSONObject();
        result.put("error", this.error);
        result.put("message", this.message);
        return result;
    }
}