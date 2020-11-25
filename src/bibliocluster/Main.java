package bibliocluster;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;


public class Main {
	
	public static void main(String[] args) throws ClientProtocolException, IOException, JSONException, URISyntaxException {
		Author[] authors;
		Menu menu = new Menu();
		String pathOfInput=menu.getInputPath();
		String pathOfSaveGraph=menu.getSaveGraph();
		String pathOfClusterResult=menu.getPathClusterResult();
		
//		C:\\Users\\ricca\\Desktop\\Tesi\\FileTest\\input.json
		AuthorsReader authorsReader = new AuthorsReader(pathOfInput);
		authors=authorsReader.fromJson();
		
		SocialNetwork sn = new SocialNetwork();
		sn.generateNetwork(authors);
//		C:\\Users\\ricca\\Desktop\\Tesi\\FileTest\\graph.graphml
		if(!pathOfSaveGraph.isEmpty()) {
			sn.saveGraph(pathOfSaveGraph);
		}
//		sn.spectralClusteringWithSilhuotte();
		sn.spectralClustering();
		sn.printSpectralClusteringResults();
		if(!pathOfClusterResult.isEmpty()){
			sn.saveSpectralClusteringResults(pathOfClusterResult);
		}
	}
}
