/**
 * A k-means clustering algorithm implementation.
 * 
 */
import java.util.LinkedList;
import java.util.List;

public class KMeans 
{
	public KMeansResult cluster(double[][] centroids, double[][] instances, double threshold) 
	{
		// instance to be returned 
		KMeansResult result = new KMeansResult();
		
		// an array of the index of the centroid assigned to each row of the instances
		int[] clusterAssignment = new int[instances.length];
		
		// an array to store the distance between each instance and it corresponding
		// centroid
		double[] distInstances = new double[instances.length];
		
		// a list to store the distortion calculated each iteration
		List<Double> distortionList = new LinkedList<Double>();
		
		// this while loop stops when the expression given is smaller than threshold
		while (true)
		{
			// this while loop stops when there is no orphan centroids
			while (true)
			{
				// controller variable to determine the inner while loop continues or not,
				// true means no orphan centroids
				boolean noOrphans = true;
				
				// an array to record each centroid is orphan or not
				boolean[] isOrphans = new boolean[centroids.length];
				for (int k = 0; k < isOrphans.length; k ++)
				{
					isOrphans[k] = true;
				}
				
				// cluster the instances, record the distance between each instance
				// and its corresponding centroid
				for (int n = 0; n < instances.length; n ++)
				{
					// to store the distance between the instance and its closest 
					// centroid
					double smallestDist = 0;
					
					// find the centroid closest to the instance
					for (int k = 0; k < centroids.length; k ++)
					{
						// if the dimension of the instance does not match the 
						// dimension of the centroid, return null
						if (instances[n].length != centroids[k].length)
						{
							return null;
						}
						
						double sum = 0;
						
						for (int d = 0; d < instances[n].length; d ++)
						{
							sum += (instances[n][d] - centroids[k][d]) 
									* (instances[n][d] - centroids[k][d]);
						}
						
						if (k == 0)
						{
							smallestDist = Math.sqrt(sum);
							clusterAssignment[n] = k;
						}
						
						// Assign the instance to a centroid only when their distance
						// is smaller than smallestDist. If they are equal, do not
						// assign again. This way makes sure the instance is assigned
						// the centroid with smallest id when there are several minimum
						// distance
						if (smallestDist > Math.sqrt(sum))
						{
							smallestDist = Math.sqrt(sum);
							clusterAssignment[n] = k;
						}
					}
					
					// set the centroid which obtain an instance be a non-orphan
					isOrphans[clusterAssignment[n]] = false;
					distInstances[n] = smallestDist;
				}
				
				// Check if there is an orphan
				for (int k = 0; k < isOrphans.length && noOrphans == true; k ++)
				{
					if(isOrphans[k] == true)
					{
						// find the farthest instance from its corresponding
						// centroid
						int farthestInstance = 0;
						double farthestDistance = distInstances[0];
						
						for (int n = 0; n < distInstances.length; n ++)
						{
							// Store a new farthest instance only when the distance
							// is larger than the farthestDistance. If equal, do not.
							// Make sure choose the instance with smallest id when
							// there are ties
							if (farthestDistance < distInstances[n])
							{
								farthestInstance = n;
								farthestDistance = distInstances[n];
							}
						}
						
						// choose the farthest instance's position as the position
						// of the orphan centroid
						centroids[k] = instances[farthestInstance];
						noOrphans = false;
					}
				}
				
				// if there are no orphan centroids, jump out of the inner while
				// loop
				if (noOrphans)
				{
					break;
				}
			}
			
			// set new centroids
			for (int k = 0; k < centroids.length; k ++)
			{
				double[] sum = new double[centroids[k].length];
				int numInstances = 0; 
				
				for (int n = 0; n < clusterAssignment.length; n ++)
				{
					if (clusterAssignment[n] == k)
					{
						for (int d = 0; d < instances[n].length; d ++)
						{
							sum[d] += instances[n][d];
						}
						numInstances ++;
					}
				}
				
				for (int d = 0; d < sum.length; d ++)
				{
					centroids[k][d] = sum[d] / numInstances;
				}
			}
			
			// store the distortion in current iteration
			Double currDistortion = (double) 0;
			
			for (int n = 0; n < instances.length; n ++)
			{
				for (int d = 0; d < instances[n].length; d ++)
				{
					currDistortion += (instances[n][d] - centroids[clusterAssignment[n]][d]) 
							* (instances[n][d] - centroids[clusterAssignment[n]][d]);
				}
			}
			
			if (!distortionList.isEmpty())
			{
				// get the distortion in the previous iteration
				Double preDistortion = distortionList.get(distortionList.size() - 1);
				
				// if the expression given is smaller than the threshold, jump out
				// of the outer while loop
				if ( Math.abs((currDistortion - preDistortion) / preDistortion) < threshold )
				{
					distortionList.add(currDistortion);
					break;
				}
			}
			
			distortionList.add(currDistortion);
		}
		
		// an array to store distortion in each iteration
		double[] distortionIterations = new double[distortionList.size()];
		
		for (int i = 0; i < distortionList.size(); i ++)
		{
			distortionIterations[i] = distortionList.get(i);
		}
		
		result.centroids = centroids;
		result.clusterAssignment = clusterAssignment;
		result.distortionIterations = distortionIterations;
		
		return result;
	}
}
