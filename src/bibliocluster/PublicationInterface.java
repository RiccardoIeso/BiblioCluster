//Interfaccia della pubblicazione
package bibliocluster;

public interface PublicationInterface {
	long getID();
	String getAnno();
	Author getFromAuthor();
	Venue getToVenue();
}
