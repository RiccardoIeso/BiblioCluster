//Classe per la memorizzazione dei vertici di tipo autore
//Implementa due interfacce:
//							-AuthorInterface 
//							-Clusterable ->interfaccia attraverso la quale possiamo usare l'autore come input per un metodo di clustering
package bibliocluster;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class Author extends Node implements AuthorInterface, Clusterable {
	
//	Attributi dell'autore
	private String lastname;					//Cognome
	private final int id;						//ID
	private String ORCID;						//ORCID per recuperare lo scopus-id nel caso non sia presente
	private String scopusID;					//scopus id -> identificativo per la ricerca dell'autore nelle librerie di scopus
	private double[] points;					//Coordinate dell'autore nello spazio vettoriale dove verrà eseguito l'algoritmo di clustering
	private double totalPub;
//	Costruttore della classe
	public Author(String Name, String Lastname, String ORCID, String ScopusID, int id) {
		super(Name);
		//this.name=Name;
		this.lastname=Lastname;
		this.ORCID=ORCID;
		this.scopusID=ScopusID;
		this.id=id;
		this.totalPub=0;
	}
	
//	Getters e setters dei vari attributi dell'autore
	
	@Override 
	public void setScopusID(String scopusID) {
		this.scopusID=scopusID;
	}
	
	@Override
	public int getID() {
		return this.id;
	}
	
//	@Override
//	public String getName() {
//
//		return this.name;
//	}

	@Override
	public String getLastname() {

		return this.lastname;
	}

	@Override
	public String getScopusID() {

		return this.scopusID;
	}

	@Override
	public String getORCID() {
		return ORCID;
	}

	public void setPoints(double[] points) {
		this.points=points;
	}
	
	@Override
	public double[] getPoint() {
		return points;
	}
//	@Override
//	public void setName(String name) {
//		this.name=name;
//	}
	
	public void AddTotalPub(double n) {
		this.totalPub=this.totalPub+n;
	}
	
	public double getTotalPub() {
		return this.totalPub;
	}

}
