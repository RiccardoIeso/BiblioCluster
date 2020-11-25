//Interfaccia della classe autore
package bibliocluster;

public interface AuthorInterface {
	void setScopusID(String scopusID);
	int getID();
	String getORCID();
	String getName();
	String getLastname();
	String getScopusID();
	void setName(String name);
	}
