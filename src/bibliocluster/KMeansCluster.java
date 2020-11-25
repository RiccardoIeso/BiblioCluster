//Classe la quale si occupa di efettuare tutte le operazioni per effettuare il KMEANS

package bibliocluster;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;

import java.util.ArrayList;
import java.util.List;

public class KMeansCluster implements KMeansClusterInterface{
//	Attributi
	private final Array2DRowRealMatrix adjencyMatrix;	
	private Array2DRowRealMatrix degreeMatrix;
	private Array2DRowRealMatrix laplacianMatrix;
	private Array2DRowRealMatrix matrixToCompute;		//Matrice dello spazio vettoriale
	private double[] eigenValuesArray;					//array degli autovalori
	EigenDecomposition e;							
	Author[] authors;
//	Costruttore 
	public KMeansCluster(Array2DRowRealMatrix adjencyMatrix) {
		this.adjencyMatrix=adjencyMatrix;
	}
	
	public KMeansCluster(Array2DRowRealMatrix adjencyMatrix, Author[] authors) {
		this.adjencyMatrix=adjencyMatrix;
		this.authors=authors;
	}
//	Metodo per ricavare la matrice diagonale dalla matrice di adiacenza
	private void DegreeMatrixFromAdjency() {
//		Recupero dimensione matrice
		int n=adjencyMatrix.getRowDimension();
//		Creo array bidimensionale di tipo double
		double[][] matrixApp= new double[n][n];
//		Assegno alla cella sulla diagonale la somma della riga della matrice di adiacenza
		for(int i=0; i<n; i++) {
			matrixApp[i][i]=getRowSum(adjencyMatrix,i);
		}
//		Inizializzo oggetto contenente la matrice di adiacenza
		degreeMatrix=new Array2DRowRealMatrix(matrixApp);
//		Richiamo il metodo per azzerare tutto ciò che non è sulla diagonale
		allZeroOutsideDiagonal(degreeMatrix);
	}
	
//	Metodo per ricavare la somma dei valori sulla riga di una matrice
	private double getRowSum(Array2DRowRealMatrix m, int r) {
		double rowSum=0;
//		Sommo i valori sulla riga
		for(int i=0; i<m.getColumnDimension(); i++) {
			if(r!=i)
				rowSum=rowSum+m.getEntry(r,i);
		}	
		
		return rowSum;
	}
	
//	Metodo per azzerare tutto ciò che non è sulla diagonale
	private void allZeroOutsideDiagonal(Array2DRowRealMatrix m) {
		
		for(int i=0; i<m.getRowDimension(); i++) {
			
			for(int j=0; j<m.getColumnDimension(); j++) {
				
				if(i!=j)
					m.setEntry(i, j, 0);
			}
		}
	}

//	Metodo per creare la matrice laplaciana
	private void LaplacianMatrix() {
//		Recuper il numero di righe della colonna
		int n= adjencyMatrix.getRowDimension();
//		Creo oggetto della matrice laplaciana
		laplacianMatrix = new Array2DRowRealMatrix(n,n);
//		L=D-A 
//		Setto la matrice laplaciana come definito dalla formula
		for(int i=0; i<n; i++) {
			
			for (int k=0; k<n; k++) {
				
				double value=degreeMatrix.getEntry(i, k) - adjencyMatrix.getEntry(i,k);
				laplacianMatrix.setEntry(i, k, value);
				
			}
		}
	}

//	Metodo per la scomposizione 
	private void eigen() {
		e = new EigenDecomposition(laplacianMatrix);
		
		eigenValuesArray=e.getRealEigenvalues(); //Recupero gli autovalori generati
	}
	
	private void printEigenvalues() {
		for(int i=0; i<adjencyMatrix.getRowDimension(); i++) {
			System.out.println("Eigenvalue "+i+": "+eigenValuesArray[adjencyMatrix.getRowDimension()-1-i]);
		}
	}
	
	
//	Metodo per ricavare il numero di cluster 
//	Cerco il gap maggiore tra gli autovalori
	private int kNumber() {
		int dim=adjencyMatrix.getRowDimension();
		double maxGap=0;
		int index=0;
		for(int i=0; i<dim-2; i++) {
			double gap=eigenValuesArray[i]-eigenValuesArray[i+1];
//			System.out.println("GAP"+gap);
			if(gap>maxGap) {
//				System.out.println(gap);
				maxGap=gap;
				//index=dim-i-2;
				index=i+2;
			}
		}
		return index;
	}
//	NB:La matrice parte dal basso
//	Metodo per la creazione della matrice contenent i primi k autovettori
	private void MatrixToCompute(int col){
		int dim=adjencyMatrix.getRowDimension();
		matrixToCompute = new Array2DRowRealMatrix(dim, col);
		for(int i=0; i<col; i++) {
			double[] arr= new double[dim];
			arr=e.getEigenvector(dim-1-i).toArray();
			matrixToCompute.setColumn(i,arr);
		}
	}

	
//	Metodo per ricavare la matrice dei punti
	@Override
	public Array2DRowRealMatrix getPointMatrix() {
		System.out.println();
		System.out.println("Effettuando il clustering...");
		System.out.println();
		DegreeMatrixFromAdjency();
		LaplacianMatrix();
		eigen();
//		printEigenvalues();
		int k=kNumber();
		MatrixToCompute(k+1);
		return matrixToCompute;
	}
//	Metodo per l'esecuzione del KMEAN sulla lista di input 
	@Override
	public List<CentroidCluster<Author>> execKmeans(List<Author> clusterInput){
		
		KMeansPlusPlusClusterer<Author> clusterer = new KMeansPlusPlusClusterer<Author>(matrixToCompute.getColumnDimension(), 10000);
		//MultiKMeansPlusPlusClusterer<Author> multiClusterer = new MultiKMeansPlusPlusClusterer<Author>(clusterer, 100);
		//List<CentroidCluster<Author>> clusterResults = multiClusterer.cluster(clusterInput);
		List<CentroidCluster<Author>> clusterResults = clusterer.cluster(clusterInput);
		return clusterResults;
	}

}
