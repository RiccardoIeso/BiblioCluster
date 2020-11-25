//Interfaccia del grafo
package bibliocluster;

public interface GraphInterface {
	String getInfo();
	boolean insertAuthor(Author a);		//Metodo per l'inserimento di un autore
	boolean insertPublication(Publication p);			//Metodo per l'inserimento di un arco
	boolean insertVenue(Venue v); 		//Metodo per l'inserimento di una venue
	void saveGraph(String path);
}
