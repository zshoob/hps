public class Node {
	public Node( int id, int xloc, int yloc ) {
		this.id = id;
		this.xloc = xloc;
		this.yloc = yloc;	
		this.munched = false;
	}
	int id;
	int xloc;
	int yloc;
	boolean munched;	
	Node up;
	Node down;
	Node left;
	Node right;
}