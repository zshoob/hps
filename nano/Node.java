import java.util.LinkedList;

public class Node {
	public Node( int id, int xloc, int yloc ) {
		this.id = id;
		this.xloc = xloc;
		this.yloc = yloc;	
		this.munched = false;
	}
	public Node( Node o ) {
		this.id = o.id;
		this.xloc = o.xloc;
		this.yloc = o.yloc;
		this.munched = o.munched;
	}
	public void view( ) {
		System.out.println( this.id + ":\t(" + this.xloc + "," + this.yloc + ")" );
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