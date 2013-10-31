import java.util.Arrays;
import java.io.*;

public class State {
	public static void main( String args[ ] ) {
		State state = new State( readTestInput(true) );
		state.update( readTestInput(false) );
	}
	public State( String input ) {
		nodes = new Node[200]; // max size
		String lines[ ] = input.split("\n");
		int size = 0;
		for( String line : lines ) {
			if( line.length( ) == 0 )
				break;
			String tokens[ ] = line.split(",");
			if( tokens.length == 3 ) { // node spec
				int id = Integer.parseInt(tokens[0]);
				int xloc = Integer.parseInt(tokens[1]);
				int yloc = Integer.parseInt(tokens[2]);
				nodes[id] = new Node(id, xloc, yloc);
				size++;
			} else { // edge spec
				Node u = nodes[Integer.parseInt(tokens[0])];
				Node v = nodes[Integer.parseInt(tokens[1])];
				if( u.xloc < v.xloc ) {
					u.right = v;
					v.left = u;
				} else if( u.xloc > v.xloc ) {
					u.left = v;
					v.right = u;
				} else if( u.yloc < v.yloc ) {
					u.down = v;
					v.up = u;				
				} else {
					u.up = v;
					v.down = u;
				}
			}
		}
		nodes = Arrays.copyOf(nodes, size);
		this.blueMunchers = new Muncher[0];
		this.redMunchers = new Node[0];
		this.blueScore = 0;
		this.redScore = 0;
	}
	public void update( String input ) {
		String lines[ ] = input.split("\n");
		String munched[ ] = lines[0].substring(2).split(",");
		for( String nodestr : munched )
			this.nodes[Integer.parseInt(nodestr)].munched = true;
		String blue[ ] = lines[1].substring(2).split(",");
		this.blueMunchers = new Muncher[blue.length];
		int idx = 0;
		for( String m : blue ) {
			String muncher[ ] = m.split("/");
			Node loc = nodes[Integer.parseInt(muncher[0])];
			String program = muncher[1];
			int counter = Integer.parseInt(muncher[2]);
			this.blueMunchers[idx++] = new Muncher(loc, program, counter);
		}
		String red[ ] = lines[2].substring(2).split(",");		
		this.redMunchers = new Node[red.length];
		for( idx = 0; idx < red.length; idx++ )
			this.redMunchers[idx] = this.nodes[Integer.parseInt(red[idx])];
		String scores[ ] = lines[3].split(",");
		this.blueScore = Integer.parseInt(scores[0]);
		this.redScore = Integer.parseInt(scores[1]);		
		String etc[ ] = lines[4].split(",");		
		this.playersRemaining = Integer.parseInt(etc[0]);
		this.timeLeft = Integer.parseInt(etc[1]);
	}
	static String readTestInput( boolean init ) {
		String out = "";
		try {
			String path;
			if( init )
				path = "/users/zachschubert/Documents/hps/nano/testgraph.txt";
			else 
				path = "/users/zachschubert/Documents/hps/nano/testupdate.txt";
			BufferedReader bf = new BufferedReader(new FileReader(path));		
			String line = null;
			while((line = bf.readLine()) != null) { 
				out += line + "\n";
			}
		} catch( IOException e ) {
			e.printStackTrace( );
		}
		return out;
	}
	Node nodes[ ];
	Muncher blueMunchers[ ];	
	Node redMunchers[ ];
	int blueScore;
	int redScore;
	int playersRemaining;
	int timeLeft;
}