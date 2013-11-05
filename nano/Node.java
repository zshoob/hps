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
	LinkedList<Node> activeSiblings( ) {
		LinkedList<Node> sibs = new LinkedList<Node>( );
		if( this.up != null && !this.up.munched )
			sibs.add(this.up);
		if( this.down != null && !this.down.munched )
			sibs.add(this.down);
		if( this.left != null && !this.left.munched )
			sibs.add(this.left);
		if( this.right != null && !this.right.munched )
			sibs.add(this.right);			
		return sibs;						
	}
	public void view( ) {
		System.out.println( this.id + ":\t(" + this.xloc + "," + this.yloc + ")\tmunched:" + this.munched );
	}
	int id;
	int xloc;
	int yloc;
	boolean munched;	
	Node up;
	Node down;
	Node left;
	Node right;
	int visitedBy = 0;
}
