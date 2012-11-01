/*
 * @author : Kamesh
 */

package rush.core.rush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rush.resources.Cluster;
import rush.resources.DiskServer;
import rush.resources.Server;
import rush.resources.SubCluster;
import static rush.resources.Cluster.maximumReplication;

public class RUSHR {
	
	int[] a = null;

	public int[] rushR(int objectID, int maximumReplication) {
		
		int numOfSubclusters = (int)Cluster.size();
		int clusterID = numOfSubclusters;
		Server server = null;
		int numOfServersInCluster = calculateNumberOfServersInCluster();
		int numOfServersTill_n_j =  0;
		int numOfServersParsed =0;
		initialize(numOfServersInCluster);
		int [] ChosenList_y = new int[] {};
		double n_j_1 = numberOfServersInClusterWithWeightOfEachSubCluster();
		for(int subCluster_j = numOfSubclusters -1 ; subCluster_j>=0 ; subCluster_j-- ){
		//TODO : seed a random number generator with hash1(x,j);
			numOfServersParsed += Cluster.getSubCluster(subCluster_j).size();
			numOfServersTill_n_j = numOfServersInCluster - numOfServersParsed ;
			int t = max(0, maximumReplication - numOfServersTill_n_j);
			// draw from the weighted hypergeometric distribution
			double m_j_1 = Cluster.getSubCluster(subCluster_j).weight();
			n_j_1 -= m_j_1;
			int numOfServersIn_m_j = (int)Cluster.getSubCluster(subCluster_j).size();
			double w_j = Cluster.getSubCluster(subCluster_j).weight()/numOfServersIn_m_j;
			
			int u = t + draw(maximumReplication - t, n_j_1 - t , m_j_1+n_j_1 -t , w_j);
			
			if(u>0){
				//TODO : seed a random number generator with hash2(x,j);
				int [] tempChosenList_y =  choose(u, numOfServersIn_m_j);
				reset(u, numOfServersIn_m_j);
				//add n_j to each of the elements
				for(int i = 0 ; i< tempChosenList_y.length ; i++){
					tempChosenList_y[i] += numOfServersTill_n_j;
				}
				for(int j=0, i=ChosenList_y.length ; j< tempChosenList_y.length ; j++ , i++){
					ChosenList_y[i] = tempChosenList_y[j];
				}
				maximumReplication = maximumReplication  - u ;
				
			}			
		}
		return ChosenList_y;
		
		
		
	}

	private double numberOfServersInClusterWithWeightOfEachSubCluster() {
		
		int numOfSubClusters = (int)Cluster.size();
		double numberOfServersInClusterWithWeightOfEachSubCluster =0;
		for(int j = numOfSubClusters ; j>0 ; j--){
			numberOfServersInClusterWithWeightOfEachSubCluster += Cluster.getSubCluster(j).weight();
		}
		
		return numberOfServersInClusterWithWeightOfEachSubCluster;
	}

	private void reset(int u , int numOfServersIn_m_j) {
		for(int i =0 ; i<= numOfServersIn_m_j -1  ; i++){
			int c = a[numOfServersIn_m_j - u + i ];
			if(c < (numOfServersIn_m_j - u)){
				a[c] = c ;
			}
			a[numOfServersIn_m_j - u + i ] = numOfServersIn_m_j - u + i;
 		}
	}

	
	private void initialize(int numOfServersInCluster) {
		
		for( int i = 0 ; i < numOfServersInCluster ; i++){
			a[i] = i ;
		}
		
	}

	private int[] choose(int u, int numOfServersIn_m_j) {
		
		if(numOfServersIn_m_j > 0 ) {
			a = new int[numOfServersIn_m_j];
			int[] r = new int[numOfServersIn_m_j];
			long seed = 123456 ;
			for (int i = 0 ; i<= u -1; i++){
				int rand = (int)( (randomNumberGenerator(0, 1, seed)  * (double)(numOfServersIn_m_j - i))); 
				
				r[i] =  a[rand];
				a[rand] = a[numOfServersIn_m_j - i -1];
				a[numOfServersIn_m_j - i -1] = r[i];
			}
			return r;
			
		}
		return null;
	}

	private int calculateNumberOfServersInCluster() {
		int numOfSubClusters = (int)Cluster.size();
		int numOfServers = 0;
		for(int j = numOfSubClusters ; j>0 ; j--){
			numOfServers += Cluster.getSubCluster((int)j).size();			
		}
		return numOfServers;
	}
	private int  max(int i, int j) {
		
		if (i>j){
			return i;
		}
		return j;
		
	}

		
	private int draw(int numOfDraws_r,  double numOfPositivesWithWeight_n, double totalWithWeight_N , double weight_w) {
		
		int c = 0;
		double n_w = numOfPositivesWithWeight_n;
		double total_N = totalWithWeight_N ;
		long seed = 123456 ;
		for(int i =0 ; i < numOfDraws_r-1 ; i++ ){
			double rand_z = randomNumberGenerator(0 , 1, seed );
			
			if(rand_z  < (numOfPositivesWithWeight_n/totalWithWeight_N) ){
				c = c+1 ;
				n_w = n_w - weight_w ;				
			}
			
			total_N = total_N - weight_w ;
			
		}
		return c;
		

	}	
	
	private double randomNumberGenerator(int lowerLimit, int upperLimit, long seed) {
		
		Random random = new Random(seed);
		
		return  random.nextDouble();		
	}
	
	
	public static void main(String[] args) {
		Server s1 = new DiskServer();
		Server s2 = new DiskServer();
		Server s3 = new DiskServer();
		Server s4 = new DiskServer();
		Server s5 = new DiskServer();
		List<Server> servers = new ArrayList<Server>();
		servers.add(s1);
		servers.add(s2);
		servers.add(s3);
		servers.add(s4);
		servers.add(s5);
		SubCluster sc = new SubCluster(5 , 1);
		sc.addServers(servers);
		Cluster.addSubCluster(sc);
		RUSHP r = new RUSHP();
		ArrayList<Server> sers = (ArrayList<Server>) r.rushP(2, 2);
	}

}
