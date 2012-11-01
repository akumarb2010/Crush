/*
 * @author : Kamesh
 */

package rush.resources;

import java.util.ArrayList;
import java.util.List;

public class SubCluster {
	private int weight;
	private int noOfSevers;
	private int id;
	private List<Server> serverList = new ArrayList<Server>();

	public SubCluster(int weight , int id ) {
		this.weight = weight;
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public int weight() {
		return weight;
	}

	public int size() {
		return noOfSevers;
	}

	public Server getServer(int serverID) {
		return serverList.get(serverID);
	}

	public void addServer(Server s) {
		serverList.add(s);
		noOfSevers++;
	}
	
	public void addServers(List<Server> servers) {
		serverList = servers;
		noOfSevers+=serverList.size();
	}
}
