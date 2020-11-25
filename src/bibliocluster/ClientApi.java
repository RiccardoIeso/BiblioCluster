//Classe che si occupa di interagire con le API che si hanno a disposzione

package bibliocluster;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

public class ClientApi implements ClientApiInterface {
//	Attributi della classe
	private final String baseUrl;
	private final String apiKey;
	private JSONObject response;

//	Costruttore
	public ClientApi(String baseUrl, String apiKey) {
		this.response = new JSONObject();
		this.baseUrl=baseUrl;
		this.apiKey=apiKey;
	}
	
	
//	Metodo per l'esecuzione della richiesta specificata attraverso i due parametri
	@Override
	public JSONObject execRequest(String query, String field ){
//		Genero l'url al quale effettuare la richiesta
		String urltoReq=baseUrl+"?apiKey="+apiKey+"&"+query+"&field="+field;
		
        URI uri;
		try{
//			Effettuo la richiesta
			uri = new URI(urltoReq);
			
	        JSONTokener tokener;
			try {
//				Parsing del JSON ricevuto
				tokener = new JSONTokener(uri.toURL().openStream());

		        this.response= new JSONObject(tokener);
//		        Return della risosta ricevuta
		        return this.response;
			} catch (JSONException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;
	}

}
