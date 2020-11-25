//Classe per la gestione delle richieste ed il recupero dei dati alle librerie di scopus
package bibliocluster;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

public class ScopusAnalyzer implements ScopusAnalyzerInterface{
	
//	Attributi
	private String apiKey="";
	private JSONObject response;									//Responso ottenuto dal client
	private List<Venue> venuesArray=new ArrayList<Venue>();			//Array dinamico di Venue
	private List<Publication> publicationsArray = new ArrayList<Publication>();		//Array dinamico di Publication
	
	
	//Metodo che controlla se un obj Venue è nell'array delle Venues
	private boolean isInV(Venue v) {
		for(int i=0; i<venuesArray.size(); i++) {
			Venue app = venuesArray.get(i);
			if(v.getName().equals(app.getName()))
					return true;
		}
		return false;
	}
	//Metodo che controlla se un obj PUblication è nell'array publication
	private boolean isInP(Publication p) {
		for(int i=0; i<publicationsArray.size(); i++) {
			Publication app = publicationsArray.get(i);
			if(p.getID()==app.getID())
					return true;
		}
		return false;		
	}
	
	//Metodo che analizza il responso
	private void analyzerResponse(Author aut) {
		//Recupero il primo onj JSON
		JSONObject search_result= response.getJSONObject("search-results"); 
		//Recupero l'array nel json delle pubblicazioni
		JSONArray entry = search_result.getJSONArray("entry");
	
		int ris=entry.length();
		//Recupero le pubblicazioni nell'array
		for(int i=0;i<ris;i++) {
			
			JSONObject l=entry.getJSONObject(i);
			//Recupero ed inserisco Venue
			Venue v= new Venue(l.getString("prism:publicationName"));
			if(!isInV(v)) {
				venuesArray.add(v);
			}
			//Recupero ed inserisco Publication
			String anno=l.getString("prism:coverDate").substring(0,4);
			long id=Long.parseLong(l.getString("dc:identifier").substring(11));
			
			Publication p=new Publication(aut,v,anno,id);
			if(!isInP(p)) {

				publicationsArray.add(p);
			}

		}
	}
	
	//Metodo il quale controlla se vi è un altro link nel response
	private boolean responseHasNext() {
		JSONObject search_result= response.getJSONObject("search-results");
		//Recupero l'array nel json delle pubblicazioni
		JSONArray link = search_result.getJSONArray("link");
		for(int i=0; i<link.length();i++) {
			JSONObject l = link.getJSONObject(i);
			if(l.getString("@ref").equals("next")) {
				return true;
			}
		}
		return false;
	}
	
	//Metodo che recupera il successivo link del response
	private String responseNextUrl() {
		
		JSONObject search_result= response.getJSONObject("search-results");
		//Recupero l'array nel json delle pubblicazioni
		JSONArray link = search_result.getJSONArray("link");
		for(int i=0; i<link.length();i++) {
			JSONObject l = link.getJSONObject(i);
			if(l.getString("@ref").equals("next")) {
				return l.getString("@href");
			}
		}
		return null;
	}
	
	//Metodo che recupera tutte le pubblicazioni e le relative riviste per ogni autore presente
	@Override
	public VenuesAndPublications getNewVenuesNewPublications(Author[] authors) throws JSONException, IOException, URISyntaxException {
		System.out.println("");
		System.out.println("Effetuando le richieste API per il recupero dei dati...");
		for(int a=0; a<authors.length; a++) {
			Author aut=authors[a];
			String baseUrl="https://api.elsevier.com/content/search/scopus";
			//String apiKey="db74ba5799151e0135fd9ceb58e5c4d0";
			boolean hasNext=true;
			ClientApi c=new ClientApi(baseUrl,apiKey);
			String field="prism:publicationName,prism:issn,prism:coverDate,dc:identifier";
			this.response=c.execRequest("query=AU-ID("+aut.getScopusID()+")",field);
			while(hasNext) {
				this.analyzerResponse(aut);
				if(responseHasNext()) {
					String query=responseNextUrl();
					query=query.substring(baseUrl.length()+1, query.length()-field.length()-7);
					this.response=c.execRequest(query,field);
				}
				else {
					hasNext=false;
				}
			}
		}
		return new VenuesAndPublications(this.venuesArray,this.publicationsArray);
	
	}
	
//	Metodo per recuperare l'author id di scopus dall'orcid dell'utente
	@Override
	public String getAuthorIDfromORCID(String ORCID) throws URISyntaxException {
		
		String baseUrl="https://api.elsevier.com/content/search/author";
		ClientApi c = new ClientApi(baseUrl,apiKey);
		
		String field="dc:identifier";
		String query="query=ORCID("+ORCID+")";
		JSONObject res= new JSONObject();
		res=c.execRequest(query, field);
		
		//Recupero il JSON
		JSONObject search_result= res.getJSONObject("search-results"); 
		JSONArray entry = search_result.getJSONArray("entry");
		JSONObject e=entry.getJSONObject(0);
		
		return e.getString(field).substring(9);
	}

}
