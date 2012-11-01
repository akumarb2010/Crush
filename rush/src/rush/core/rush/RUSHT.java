/*
 * @author :  AnilKumar
 */

package rush.core.rush;

import rush.resources.DiskServer;
import rush.resources.SubCluster;

public class RUSHT {
	int index = 0;
	Node root;
	static int clusterIndex = 0;
	static int indexBalance = 0;

	public static void main(String[] args) {
		RUSHT rushT1 = new RUSHT();
		rushT1.createRUSHTree();
		System.out.println(" Number of Nodes in a tree = "
				+ rushT1.getNumberOfNodes());
		/*
		 * System.out.println("******************************************");
		 * System.out.println(" Indices of tree "); rushT1.printTree();
		 * System.out.println("******************************************");
		 * System.out.println(" Weights of tree "); rushT1.printWeightTree();
		 * System.out.println("******************************************");
		 */
		// testPartiallyCorrectness();
		long[] result;
		for (int i = 1; i <= 10; i++) {
			System.out
					.println("******************************************************************************");
			System.out.println("Object i = " + i);
			result = rushT1.rushT(i, 2);
			System.out.println("The cluster is = " + result[0]);
			System.out.println("The server id is  = " + result[1]);
			System.out
					.println("******************************************************************************");
		}
	}

	private void printWeightTree() {
		printWeightTree(this.root);
	}

	private static void printWeightTree(Node root) {
		if (root.left != null) {
			printWeightTree(root.left);
		}
		System.out.println(root.weight);
		if (root.right != null) {
			printWeightTree(root.right);
		}
	}

	/*
	 * To test the completeness of tree.
	 */
	private static void testPartiallyCorrectness() {
		RUSHT rush_i;
		int h;
		boolean result = true;
		System.out.println("Testing for " + (Math.pow(2, 15)) + "clusters");
		for (int i = 0; i <= 15; i++) {
			rush_i = new RUSHT();
			for (int j = 1; j <= Math.pow(2, i); ++j) {
				if (rush_i.root == null) {
					Node newNode = rush_i.getNewNode();
					rush_i.root = newNode;
				} else {
					Node newNode = rush_i.getNewNode();
					rush_i.addClusterNode(newNode);
				}
			}
			h = getHeightOfTree(rush_i.root);
			System.out.println("Height = " + h);
			if (rush_i.getNumberOfNodes() != (Math.pow(2, h + 1) - 1)) {
				result = false;
				System.out.println("Number of nodes = "
						+ rush_i.getNumberOfNodes());
				System.out.println("Mathematically = "
						+ (Math.pow(2, h + 1) - 1));
				break;
			}
		}
		if (result) {
			System.out.println("  Success  ");
		} else {
			System.out.println("  Fail ");
		}
	}

	private void printTree() {
		printTree(this.root);
	}

	private int getNumberOfNodes() {
		return getNumberOfNodes(this.root);
	}

	/*
	 * To construct the RUSH tree, this tree will maintain all the subclusters
	 * of the system. Each sub cluster will be leaf node of the  Rush tree 
	 */
	private void createRUSHTree() {
		for (int i = 1; i <= 10; ++i) {
			if (this.root == null) {
				Node newNode = getNewNode();
				this.root = newNode;
			} else {
				Node newNode = getNewNode();
				this.addClusterNode(newNode);
			}
		}
		System.out.println("height = " + getHeightOfTree(this.root));
	}

	/*
	 * This method will add the Subcluster as a leaf node of tree. As per the
	 * paper description, this tree will ensure that nodes in the tree always
	 * have the same unique identifier.
	 */
	private void addClusterNode(Node newNode) {
		boolean isAdded = false;
		int height = getHeightOfTree(this.root);
		if (height == 0) {
			this.root = createNewRoot(this.root);
			isAdded = this.insertNode(this.root, 1, newNode);
			this.reOrganizeWeights();
			indexBalance = 0;
			reOrganizeIndices(this.root);
			return;
		}
		if (height == 1) {
			isAdded = this.insertNode(root, height, newNode);
		} else {
			if (root.right == null) {
				root.right = getNewDummyNode();
			}
			isAdded = this.insertNode(root.right, --height, newNode);
		}
		if (false == isAdded) {
			this.root = createNewRoot(this.root);
			root.right = getNewDummyNode();
			height = getHeightOfTree(this.root);
			this.insertNode(root.right, height - 1, newNode);
		}
		indexBalance = 0;
		reOrganizeIndices(this.root);
		this.reOrganizeWeights();
	}

	private void reOrganizeWeights() {
		reOrganizeWeights(this.root);
	}

	/*
	 * To print the Tree in inorder.
	 */
	private static void printTree(Node root) {
		if (root.left != null) {
			printTree(root.left);
		}
		System.out.println(root.index);
		if (root.right != null) {
			printTree(root.right);
		}
	}

	public long[] rushT(long x, long r) {
		Node node = getSubCluster(this.root, x, r);
		long j = node.index;
		long v = RUSHP.generateUniformLong(x, j) * (node.subCluster.size());
		long z = RUSHP.generateUniformLong(x, j);
		// p shld be >= mj1 based on z
		long p = RandomUniformHash.generatePrime(z);
		long s = RandomUniformHash.mod(
				(x + v + RandomUniformHash.mod(r * p, node.subCluster.size())),
				(node.subCluster.size()));
		return (new long[] { j, s });
	}

	private static Node getSubCluster(Node node, long x, long r) {
		if (node.subCluster instanceof SubCluster) {
			return node;
		}
		if (RUSHP.generateUniformLong(x, node.index) < (node.left.weight)) {
			return getSubCluster(node.left, x, r);
		} else {
			return getSubCluster(node.right, x, r);
		}
	}

	private Node getNewNode() {
		return new Node(++this.index, createNewSubCluster());
	}

	private static SubCluster createNewSubCluster() {
		SubCluster temp = new SubCluster(++clusterIndex, clusterIndex);
		temp.addServer(new DiskServer());
		temp.addServer(new DiskServer());
		temp.addServer(new DiskServer());
		return temp;
	}

	/*
	 * get the Number Of Nodes of Tree.
	 */
	private int getNumberOfNodes(Node root) {
		int c1 = 0, c2 = 0;
		if (root == null) {
			return 0;
		}
		if (root.left != null) {
			c1 = getNumberOfNodes(root.left);
		}
		if (root.right != null) {
			c2 = getNumberOfNodes(root.right);
		}
		return (c1 + c2 + 1);
	}

	private static double reOrganizeWeights(Node root) {
		double leftWeight = 0;
		double rightWeight = 0;
		if (root == null) {
			return 0;
		}
		if (root.left != null)
			leftWeight = reOrganizeWeights(root.left);
		if (root.right != null)
			rightWeight = reOrganizeWeights(root.right);
		root.weight = Math.max(leftWeight + rightWeight, root.weight);
		return root.weight;
	}

	/*
	 * This is to maintain the order of indeces of nodes after re cunstruction
	 * of nodes. This will ensure the unique identity of every subcluster.
	 */
	private static void reOrganizeIndices(Node root) {
		if (root.left != null) {
			reOrganizeIndices(root.left);
		}
		root.index = ++indexBalance;
		if (root.right != null) {
			reOrganizeIndices(root.right);
		}
	}

	/*
	 * This method will insert the new node to tree with "root" as root node,
	 * and this method is recursive.
	 */
	private boolean insertNode(Node root, int height, Node newNode) {
		if (height <= 1) {
			if (root.left == null) {
				root.left = newNode;
				return true;
			} else if (root.right == null) {
				root.right = newNode;
				return true;
			} else {
				return false;
			}
		} else {
			boolean bool = false;
			if (root.left == null) {
				root.left = getNewDummyNode();
			}
			bool = insertNode(root.left, --height, newNode);
			if (false == bool) {
				++height;
				if (root.right == null) {
					root.right = getNewDummyNode();
				}
				bool = insertNode(root.right, --height, newNode);
			}
			return bool;
		}
	}

	private Node createNewRoot(Node root) {
		Node dummyNode = this.getNewDummyNode();
		dummyNode.left = root;
		return dummyNode;
	}

	private Node getNewDummyNode() {
		return new Node(++this.index, null);
	}

	/*
	 * Since it is complete Binary Tree This method will return Height of the
	 * Complete Binary Tree.
	 */
	private static int getHeightOfTree(Node root) {
		int h = 0;
		while (root.left != null) {
			++h;
			root = root.left;
		}
		return h;
	}

	/*
	 * to test construction of binary tree which is not used.
	 */
	private static void testBynaryTreeConstruction(Node root, int value) {
		if (root == null) {
			return;
		} else if (root.weight < value) {
			if (root.right != null) {
				testBynaryTreeConstruction(root.right, value);
			} else {
				root.right = new Node(value);
				return;
			}
		} else {
			if (root.weight > value) {
				if (root.left != null) {
					testBynaryTreeConstruction(root.left, value);
				} else {
					root.left = new Node(value);
					return;
				}
			}
		}
	}
}

class Node {
	int index = 0;
	SubCluster subCluster = null;
	double weight = 0;
	Node left = null;
	Node right = null;

	public Node(int index, SubCluster cluster) {
		this.index = index;
		if (null != cluster) {
			subCluster = cluster;
			weight = subCluster.weight();
		} else {
			weight = 0.0;
		}
	}

	public Node(double weight) {
		this.weight = weight;
	}
}
