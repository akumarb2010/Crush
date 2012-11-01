/*
 * @author : AnilKumar
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

public class RUSHP {

	private static Random r = new Random();

	/*
	 * Rush P is main functionality includes, when an object's id is given, it retrieves the servers from the cluster
	 * in which that particular object to be stored. If the object already exists in the cluster, it retries all the servers in which that 
	 * particular object stored.
	 * @param objectID object ID to be stored or retrieved
	 * @param replicationID replication of the object 
	 * @returns List of the servers where the input object stored or to be stored.
	 */
	public List<Server> rushP(int objectID, int replicationID) {
		int clusterSize = Cluster.size();
		int clusterID = clusterSize;
		List<Server> servers = new ArrayList<Server>(clusterSize);
		while (clusterID > 0) {
			Server server = null;
			while (clusterID > 0 && server == null) {
				int curClusterWeight = Cluster.getSubCluster(clusterID)
						.weight();
				int totalclusterWeight = Cluster.weight(clusterID);

				long curSubClusterSize = Cluster.getSubCluster(clusterID)
						.size();
				long z = generateUniformLong(curClusterWeight,
						totalclusterWeight);
				long p = generatePrime(z);
				long v = objectID + z + replicationID * p;
				long z_1 = RandomUniformHash.mod((z + replicationID * p),
						curClusterWeight + totalclusterWeight);

				if ((curSubClusterSize >= maximumReplication)
						&& (z_1 < curClusterWeight * curSubClusterSize)) {

					int allServersAvailable = Cluster
							.getAllServersAvailable(clusterID);
					long a = allServersAvailable
							+ RandomUniformHash.mod(v, curSubClusterSize);
					server = Cluster.getSubCluster(clusterID)
							.getServer(
									(int) RandomUniformHash.mod(a,
											allServersAvailable));
					servers.add(server);
				} else if ((curSubClusterSize < maximumReplication)
						&& (z_1 < maximumReplication * curClusterWeight)
						&& (RandomUniformHash.mod(v, (long) maximumReplication) < curSubClusterSize)) {

					int allServersAvailable = Cluster
							.getAllServersAvailable(clusterID);
					long a = allServersAvailable
							+ RandomUniformHash.mod(v, maximumReplication);
					server = Cluster.getSubCluster(clusterID).getServer(
							(int) a % allServersAvailable);
					servers.add(server);
				} else {
					clusterID--;
				}
			}
			clusterID--;
		}
		return servers;
	}

	static long generateUniformLong(double curClusterWeight,
			double totalClusterWeight) {
		double d = r.nextDouble();
		double range = totalClusterWeight - curClusterWeight;
		return (long) (curClusterWeight + d * range);
	}

	public static long generatePrime(long z) {
		long prime;
		boolean bool = true;
		long rand = new Random(z * z << 3).nextInt();
		long initVal = z + 1 * rand;
		initVal = initVal < 0 ? -initVal : initVal;
		if (initVal % 2 == 0) {
			initVal = initVal + 1;
		}
		for (long i = initVal;; i = i + 2) {
			for (int j = 3; j < Math.sqrt(i); j++) {
				if (i % j == 0) {
					bool = false;
					break;
				}
			}
			if (bool == false) {
				bool = true;
			} else {
				prime = i;
				break;
			}
		}
		return prime;
	}

	public static void main(String[] args) {
		Server s1 = new DiskServer(1,1);
		Server s2 = new DiskServer(2,1);
		Server s3 = new DiskServer(3,1);
		Server s4 = new DiskServer(4,1);
		Server s5 = new DiskServer(5,1);
		List<Server> servers1 = new ArrayList<Server>();
		servers1.add(s1);
		servers1.add(s2);
		servers1.add(s3);
		servers1.add(s4);
		servers1.add(s5);
		SubCluster sc1 = new SubCluster(5, 1);
		sc1.addServers(servers1);
		Cluster.addSubCluster(sc1);

		Server s6 = new DiskServer(6,2);
		Server s7 = new DiskServer(7,2);
		Server s8 = new DiskServer(8,2);
		Server s9 = new DiskServer(9,2);
		Server s0 = new DiskServer(10,2);
		List<Server> servers2 = new ArrayList<Server>();
		servers2.add(s6);
		servers2.add(s7);
		servers2.add(s8);
		servers2.add(s9);
		servers2.add(s0);
		SubCluster sc2 = new SubCluster(3, 2);
		sc2.addServers(servers2);
		Cluster.addSubCluster(sc2);

		RUSHP r = new RUSHP();
		List<Server> servers = r.rushP(2, 2);
		for (Server s : servers) {
			System.out.println(s);
		}

	}
}
