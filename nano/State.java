import java.util.Arrays;
import java.util.LinkedList;
import java.io.*;

public class State {
	public static void main( String args[ ] ) {
		State state = new State(readTestInput(true));
		Node node = state.nodes[87];		
		Muncher m = state.bestMuncherAtNode(node);
		
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
					u.up = v;					
					v.down = u;			
				} else {
					u.down = v;
					v.up = u;
				}
			}
		}
		nodes = Arrays.copyOf(nodes, size);
		this.blueMunchers = new Muncher[0];
		this.redMunchers = new Node[0];
		this.blueScore = 0;
		this.redScore = 0;
	}
	public State( State o ) {
		this.nodes = new Node[o.nodes.length];
		for( int i = 0; i < this.nodes.length; i++ ) 
			this.nodes[i] = new Node(o.nodes[i]);
		this.blueMunchers = new Muncher[o.blueMunchers.length];
		for( int i = 0; i < this.blueMunchers.length; i++ ) 
			this.blueMunchers[i] = new Muncher(o.blueMunchers[i]);		
		this.redMunchers = new Node[o.redMunchers.length];
		for( int i = 0; i < this.redMunchers.length; i++ ) 
			this.redMunchers[i] = new Node(o.redMunchers[i]);			
		this.blueScore = o.blueScore;
		this.redScore = o.redScore;
		this.playersRemaining = o.playersRemaining;
		this.timeLeft = o.timeLeft;
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
	Muncher bestMuncherAtNode( Node node ) {
		String bestProgram = null;
		int max = 0;
		for( String program : Muncher.allPrograms ) {
			System.out.println(program);
			int score = numMunchableNodes(node, program, 0, new LinkedList<Integer>( ), 0);
			System.out.println(score);			
			if( score > max ) {
				max = score;
				bestProgram = program;
			}	
		}
		return new Muncher( node, bestProgram, 0 );
	}
	static int numMunchableNodes( Node node, String program, int counter, 
						   LinkedList<Integer> visited, int num ) {
		visited.add(node.id);
		for( int i = 0; i < 4; i++ ) {
			char instruction = program.charAt(counter);
			Node n = null;
			switch( instruction ) {
				case 'u': n = node.up; break;
				case 'd': n = node.down; break;				
				case 'l': n = node.left; break;				
				case 'r': n = node.right; break;				
			}
			counter = (counter+1)%4;
			//System.out.println( "here" );
			//if( n != null )
			//	n.view( );
			if( !(n == null || n.munched || visited.contains(n.id)) ) {
				return numMunchableNodes( n, program, counter, 
										  visited, num+1 );
			}
		}
		return num;
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