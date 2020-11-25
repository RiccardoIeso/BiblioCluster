//Classe per la memorizzazione delle pubblicazioni
package bibliocluster;

public class Publication implements PublicationInterface {
//	Attributi
	private Author fromAuthor; 	//Autore della pubblicazione
	private Venue toVenue;		//Rivista
	private String anno;		//Anno
	private long ID;
	
	//Costruttore
	public Publication(Author fromAuthor, Venue toVenue, String anno, long ID) {
		this.fromAuthor=fromAuthor;
		this.toVenue=toVenue;
		this.anno=anno;
		this.ID=ID;
	}
	
	//Getters dei vari attributi della pubblicazione
	@Override
	public String getAnno() {
		// TODO Auto-generated method stub
		return this.anno;
	}

	@Override
	public Author getFromAuthor() {
		// TODO Auto-generated method stub
		return this.fromAuthor;
	}

	@Override
	public Venue getToVenue() {
		// TODO Auto-generated method stub
		return this.toVenue;
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return this.ID;
	}
	
}
