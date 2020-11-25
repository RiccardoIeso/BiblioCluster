//Classe menu per facilitare l'interazione con l'utente
package bibliocluster;

import java.util.Scanner;

public class Menu implements MenuInterface{
//	Attributi
	private String inputPath="";
	private String saveGraphPath="";
	private String clusterResultsPath="";
	
//	Costruttore che permette la prima interazione con l'utente
	public Menu() {
		Scanner myObj = new Scanner(System.in);  // Create a Scanner object
		System.out.println("------------------------------BIBLIO-CLUSTER------------------------------");
		System.out.println("Applicativo per il riconoscimento di comunità scientifiche");
		System.out.println("attraverso l'interrograzione di basi di dati bibliografiche.");
		System.out.println("Se non si desidera salvare i risultati ottenuti, lasciare vuoto..");
		System.out.println();
		while(inputPath.isEmpty()) {
			System.out.println("Percorso del file JSON dove recuperare gli autori:");
			inputPath= myObj.nextLine();  // Read user input
			System.out.println();
		}
		System.out.println("Destinazione file graphml dove salvare il grafo ricavato:");
		saveGraphPath=myObj.nextLine();
		System.out.println();
		System.out.println("Destinazione file JSON dove salvare i risultati ottenuti:");
		clusterResultsPath=myObj.nextLine();
		myObj.close();
	}
	
//	getters degli input ottenuti dagli utenti
	@Override
	public String getInputPath() {
		 return inputPath;
	}
	@Override
	public String getSaveGraph() {
		 return saveGraphPath;
	}
	@Override
	public String getPathClusterResult() {
		return clusterResultsPath;
	}
}
