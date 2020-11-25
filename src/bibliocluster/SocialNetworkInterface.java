package bibliocluster;

import java.io.IOException;
import java.net.URISyntaxException;


import org.json.JSONException;

public interface SocialNetworkInterface {
	void generateNetwork(Author[] authors) throws JSONException, IOException, URISyntaxException;
	boolean saveGraph(String path);
	void spectralClustering();
	void printSpectralClusteringResults();
	void saveSpectralClusteringResults(String path);
	String info();
}
