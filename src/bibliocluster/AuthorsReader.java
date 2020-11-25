//Classe la quale si occupa di recuperare la lista degli autori dal file JSON
package bibliocluster;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


public class AuthorsReader implements AuthorsReaderInterface {
//	Attributi
	private String path;
	
//	Costruttore
	public AuthorsReader(String path) {
		this.path=path;
	}
	
//	Metodo per il recupero della lista da un fil JSON
	@Override
	public Author[] fromJson() {
//		Creo array di autori
		Author[] authors;
//      Apro il file
        try (FileReader reader = new FileReader(path))
        {
//          Parser del JSON
        	JSONTokener jsonTokener = new JSONTokener(reader);
//        	Recupero il file JSON come oggetto
        	JSONObject jsonFile = new JSONObject(jsonTokener);
// 			Recupero all'interno del file gli autori contenuti nell'array
            JSONArray authorsList = jsonFile.getJSONArray("authors");
            authors = new Author[authorsList.length()];
//          Per ogni autore presente genero l'oggetto e lo salvo nell'array
            for(int i=0; i<authorsList.length(); i++) {
            	JSONObject a = authorsList.getJSONObject(i);
            	authors[i]=new Author(a.getString("name"),
            							a.getString("lastname"),
            							a.getString("ORCID"),
            							a.getString("scopus-id"),
            							i);
            }
//          Return dell'array
            return authors;
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
	}
}
