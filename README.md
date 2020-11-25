# BiblioCluster
Bachelor thesis project. 
Scientific communities recognition software based on Scopus APIs.
The main goal of this software is: given a list of authors (researchers) it finds the scientific communities inside the list.
Basically this program interacts with the Elseviers's database using the Scopus APIs to retrieve information about the authors.
Once it has the necessaries information about the researchers, it creates a graph of authors based on their publication and the pubblications venues. Subsequentially, it performs the K-Means algorithm to cluster the authors.
