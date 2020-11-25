//Classe grafo la quale si occupa di instaziare ed interagire con un grafo
package bibliocluster;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.T;

import org.apache.tinkerpop.gremlin.structure.io.IoCore;


public class Graph implements GraphInterface{
	
	private TinkerGraph graph;					//Grafo
    private GraphTraversalSource g;				//Graph traversal source necessario per effettuare le query
    
    //Costruttore
	public Graph() {
		//Inizializzo le due variabili
		this.graph = TinkerGraph.open();
		this.g=graph.traversal();
	}
	
	//Metodo per l'inserimento di un autore all'interno del grafo
	@Override
	public boolean insertAuthor(Author a) {
		
		try {
			//Eseguo la query per l'aggiunta
			graph.addVertex(T.label,"author", "name", a.getName(), "lastname", a.getLastname(), "scopus-id", a.getScopusID(), "orcid", a.getORCID());
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	//Metodo per l'inserimento di una venue nel grafo
	@Override
	public boolean insertVenue(Venue v) {
		try {
			//Eseguo la query per l'aggiunta
			graph.addVertex(T.label,"venue", "name", v.getName());
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	
	//Metodo per l'inserimento di una pubblicazione all'interno del grafo
	@Override
	public boolean insertPublication(Publication p) {
		String authorScopusId=p.getFromAuthor().getScopusID(); //Recupero scopus-id autore pubblicazione
		String venueName=p.getToVenue().getName();	//Recupero nome venue della pubblicazione

		try {
			//Recupero vertice autore
	        GraphTraversal<Vertex, Vertex> traversalAuth = this.g.V().hasLabel("author").has("scopus-id",authorScopusId);
	        Vertex fromAuth = traversalAuth.next();
	        //Recupero vertice venue
	        GraphTraversal<Vertex, Vertex> traversalVenue = this.g.V().hasLabel("venue").has("name",venueName);
	        Vertex toVenue = traversalVenue.next();
	        //Aggiungo l'arco
			fromAuth.addEdge("hasPublishedOn",toVenue,"anno",p.getAnno(),"id", p.getID());

		}
		catch(Exception e) {
			return false;
		}
		return false;
	}
	
	//Metodo per ottenere informazioni riguardanti il grafo
	@Override
	public String getInfo() {
		return graph.toString();
	}
	
	//Metodo per il salvataggio del grafo
	@Override
	public void saveGraph(String path) {
		
		try {
			graph.io(IoCore.graphml()).writeGraph(path);
		}
		catch (Exception e){
			System.out.println("Failed to save graph");
		}
		
	}

}
