//
package bibliocluster;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;

public interface ScopusAnalyzerInterface {
	//void analyzerResponse();
	VenuesAndPublications getNewVenuesNewPublications(Author[] authors) throws JSONException, IOException, URISyntaxException;
	String getAuthorIDfromORCID(String ORCID)throws URISyntaxException;
}
