//Classe per permettere il ritorno di due tipi di dato insieme
package bibliocluster;
import java.util.List;

public class VenuesAndPublications {
	private final List<Venue> venuesArray;
	private final List<Publication> publicationsArray;
	
	public VenuesAndPublications(List<Venue> venues, List<Publication> publications){
		this.venuesArray=venues;
		this.publicationsArray=publications;
	}
	
	public List<Venue> getVenuesArray(){
		return this.venuesArray;
	}
	
	public List<Publication> getPublicationsArray() {
		return this.publicationsArray;
	}
}
