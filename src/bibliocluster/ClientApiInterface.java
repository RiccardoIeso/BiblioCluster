package bibliocluster;

import org.json.JSONObject;

public interface ClientApiInterface {
	JSONObject execRequest(String query, String field);
}
