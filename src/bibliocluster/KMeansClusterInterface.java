package bibliocluster;

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.ml.clustering.CentroidCluster;

public interface KMeansClusterInterface {
	Array2DRowRealMatrix getPointMatrix();
	List<CentroidCluster<Author>> execKmeans(List<Author> clusterInput);
}
