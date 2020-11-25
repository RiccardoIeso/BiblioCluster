// Classe per la creazione della rete sociale per poi successivamente analizzarla
package bibliocluster;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.ml.clustering.CentroidCluster;

public class SocialNetwork implements SocialNetworkInterface {
	
//	Attributi
	private final ScopusAnalyzer scopusA = new ScopusAnalyzer();
	private final Graph grafo = new Graph();
	private Author[] authors;
	private List<Venue> venues;
	private List<Publication> publications;
	private Array2DRowRealMatrix adjencyMatrix;
	private KMeansCluster kmeans;
	private List<CentroidCluster<Author>> KMeansClusterResults;
	
//	Metodo per la creazione della rete dato l'array di autori
	@Override
	public void generateNetwork(Author[] authors) throws JSONException, IOException, URISyntaxException {
		this.authors=authors;
		for(Author a : authors) {
			//Per ogni autore se non presente scopus id lo recuper attravero orcid
			if(a.getScopusID()==null) {
				String scopusID=scopusA.getAuthorIDfromORCID(a.getORCID());
				a.setScopusID(scopusID);
			}
		}
//		Recupero venues e pubblicazioni degli autori nella rete
		VenuesAndPublications ris= scopusA.getNewVenuesNewPublications(authors);
//		Salvo le venue e le pubblicazioni ricavate
		this.venues = ris.getVenuesArray();
		this.publications = ris.getPublicationsArray();
//		Inserisco autori, venues e pubblicazioni nel grafo
		insertAuthors();
		insertVenues();
		insertPublications();
	}
	
//	Metodo per il salvataggio del grafo
	@Override
	public boolean saveGraph(String path) {
		try {
			grafo.saveGraph(path);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
//	Metodo per l'esecuzione dello spectral clustering
	@Override
	public void spectralClustering() {
//		Genero matrice di adiacenza tra autori
		AdjencyMatrix();
		kmeans = new KMeansCluster(adjencyMatrix);
		Array2DRowRealMatrix mtc = kmeans.getPointMatrix();	
		//li aggiungo all'autore
		authorsPointsSetter(mtc);
		//passo la lista di autori al clusterator
		List<Author> clusterInput=generateClusterInput();
		KMeansClusterResults=kmeans.execKmeans(clusterInput);
	}
	
//	Metodo per la stampa dei risultati ottenuti
	@Override
	public void printSpectralClusteringResults() {
		for (int i=0; i<KMeansClusterResults.size(); i++) {
		    System.out.println("Cluster " + i);
		    for (Author a : KMeansClusterResults.get(i).getPoints()) {
		    	System.out.println("SCOPUS ID: "+a.getScopusID()+" "+a.getName()+" - "+a.getLastname());
//		        System.out.println(a.getName()+" - "+a.getLastname());
		    }
		    System.out.println();
		}	
	}
	
//	Metodo per il salvataggio dei risultati ottenuti in un file json
	@Override
	public void saveSpectralClusteringResults(String path) {
//		Recupero data ed ora di creazione
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		JSONObject fileJson = new JSONObject();
		JSONObject aut;
		fileJson.put("created-at",df.format(dateobj));
//		Genero array contenente i cluster
		JSONArray results = new JSONArray();
		for (int i=0; i<KMeansClusterResults.size(); i++) {
//			Genero i cluster in formato JSON
			JSONObject cluster = new JSONObject();
			cluster.put("id",i);
			JSONArray authorsArr= new JSONArray();
			int ind=0;
		    for (Author a : KMeansClusterResults.get(i).getPoints()) {
		    	aut = new JSONObject();
		    	aut.put("name", a.getName());
		    	aut.put("lastname", a.getLastname());
		    	aut.put("ORCID", a.getORCID());
		    	aut.put("scopus-id", a.getScopusID());
		        authorsArr.put(ind, aut);
		        ind++;
		    }
		    cluster.putOnce("authors", authorsArr);
		    results.put(i, cluster);
		    
		}
		fileJson.putOpt("clusters", results);
		try (FileWriter file = new FileWriter(path)) {
			file.write(fileJson.toString(2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	Metodo per la generazione della lista di input dove eseguire il K-MEANS
	private List<Author> generateClusterInput(){
		List<Author> clusterInput = new ArrayList<Author>(authors.length);
		for(Author a : authors) {
			clusterInput.add(a);
		}
		return clusterInput;
	}
	
//	Metodo per assegnare ad ogni autore dei punti
	private void authorsPointsSetter(Array2DRowRealMatrix m) {
		for (int i=0; i<authors.length; i++) {
			authors[i].setPoints(m.getRowVector(i).toArray());
		}
	}

	
	private void AdjencyMatrix() {
		//Creo matrice di adiacenza
		int n=authors.length;
		double[][] matrixApp = new double[n][n];
		//adjencyMatrix= new Array2DRowRealMatrix();
		for(Venue v : venues) {
			
			for(Author onRow : authors) {
				int rowPub=publicationOn(onRow,v);
				for(Author onCol : authors) {
					
					if(onRow.getID()!=onCol.getID()) {
						int colPub=publicationOn(onCol,v);
						matrixApp[onRow.getID()][onCol.getID()]=matrixApp[onRow.getID()][onCol.getID()]+Math.min(rowPub, colPub);
						}
					}
				}
		}
		//matrixApp=setDiagonalOfAdjency(matrixApp);
		adjencyMatrix= new Array2DRowRealMatrix(matrixApp);
	}
	
	//g.V().has("lastname","Loreti").outE("hasPublishedOn").inV().groupCount().by("name").unfold()
	private int publicationOn(Author a1, Venue v) {
		int count=0;
		for(Publication p : publications) {
			if(p.getToVenue().getName().equals(v.getName())) {
				if(a1.getScopusID().equals(p.getFromAuthor().getScopusID())) {
					count=count+1;
				}
			}
		}
		return count;
	}
	//Metodo per l'inserimento degli autori nel grafo
	private void insertAuthors() {
		for(Author aut : authors) {
			grafo.insertAuthor(aut);
		}
	}
	//Metodo per l'inserimento delle venue nel grafo
	private void insertVenues() {
		for(int i=0; i<venues.size(); i++) {
			grafo.insertVenue(venues.get(i));
		}
	}
	//Metodo per l'inserimento delle pubblicazioni nel grafo
	private void insertPublications() {
		for(int i=0; i<publications.size(); i++) {
			grafo.insertPublication(publications.get(i));
		}
	}
	
	//Metodo per ricavare le informazioni del grafo
	@Override
	public String info() {
		return grafo.getInfo();
	}
	
}
