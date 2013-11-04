import java.util.Arrays;
import java.util.LinkedList;
import java.util.Collections;
import java.io.*;


public class State {
	public static void main( String args[ ] ) {
		State state = new State(readTestInput(true));
		state.update(readTestInput(false));		
		System.out.println(state.getTurn( ));
	}
	public State( String input ) {
		nodes = new Node[200]; // max size
		String lines[ ] = input.split("\n");
		int size = 0;
		for( String line : lines ) {
			if( line.length( ) == 0 )
				break;
			if( line.charAt(0) == 'n' )
				continue;
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
		this.numBlocked = 0;
	}
	public State( State o ) {
		this.nodes = new Node[o.nodes.length];
		for( int i = 0; i < this.nodes.length; i++ )
			this.nodes[i] = new Node(o.nodes[i]);
		for( int i = 0; i < o.nodes.length; i++ ) {
			if( o.nodes[i].up != null )
				this.nodes[i].up = this.nodes[o.nodes[i].up.id];
			if( o.nodes[i].down != null )
				this.nodes[i].down = this.nodes[o.nodes[i].down.id];				
			if( o.nodes[i].left != null )
				this.nodes[i].left = this.nodes[o.nodes[i].left.id];				
			if( o.nodes[i].right != null )
				this.nodes[i].right = this.nodes[o.nodes[i].right.id];				
		}
		this.blueMunchers = new Muncher[o.blueMunchers.length];
		for( int i = 0; i < this.blueMunchers.length; i++ ) {
			this.blueMunchers[i] = new Muncher(o.blueMunchers[i]);		
			this.blueMunchers[i].loc = this.nodes[o.blueMunchers[i].loc.id];	
		}
		this.redMunchers = new Node[o.redMunchers.length];
		for( int i = 0; i < this.redMunchers.length; i++ )
			this.redMunchers[i] = this.nodes[o.redMunchers[i].id];
		this.blueScore = o.blueScore;
		this.redScore = o.redScore;
		this.blueRemaining = o.blueRemaining;
		this.redRemaining = o.redRemaining;
		this.numBlocked = o.numBlocked;
		this.timeLeft = o.timeLeft;
	}
	public void update( String input ) {
		String lines[ ] = input.split("\n");
		if( lines.length < 3 )
			return;
		if( lines[0].length( ) > 1 ) {
			String munched[ ] = lines[0].substring(lines[0].indexOf(":")+1).split(",");
			for( String nodestr : munched ) {
				String tokens[ ] = nodestr.split("/");
				for( String t : tokens ) {
					this.spawnloc.add(Integer.parseInt(t));				
					this.nodes[Integer.parseInt(t)].munched = true;									
				}
			}
		}
		if( lines[1].length( ) > 1 ) {
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
		} else {
			this.blueMunchers = new Muncher[0];
		}
		if( lines[2] .length( ) > 1 ) {
			String red[ ] = lines[2].substring(2).split(",");		
			this.redMunchers = new Node[red.length];
			for( int idx = 0; idx < red.length; idx++ ) {
				this.redMunchers[idx] = this.nodes[Integer.parseInt(red[idx])];
			}
				
		} else {
			this.redMunchers = new Node[0];
		}
		String scores[ ] = lines[3].split(",");
		this.blueScore = Integer.parseInt(scores[0]);
		this.redScore = Integer.parseInt(scores[1]);		
		String etc[ ] = lines[4].split(",");		
		this.blueRemaining = Integer.parseInt(etc[0]);
		this.redRemaining = Integer.parseInt(etc[1]);
		if( turnZero )
			State.numMunchers = this.blueRemaining;
		this.timeLeft = Integer.parseInt(etc[2]);
	}	
	public String greedyTurn( ) {
		State copy = new State(this);
		String out = "";
		int count = 0;
		for( int loop = 0; loop < 10; loop++ ) {
			int max = 0;
			Muncher bestMuncher = null;
			for( Node node : copy.nodes ) {
				if( node.munched )
					continue;
				Muncher m = bestMuncherAtNode( copy, node );
				int score = numMunchableNodes( copy, node, m.program );
				if( score > max ) {
					max = score;
					bestMuncher = m;
				}
			}
			if( bestMuncher != null ) {
				count++;
				out += bestMuncher.loc.id + "/" + bestMuncher.program + ",";
				while( !bestMuncher.starved )
					bestMuncher.run( );
			}
		}
		out = out.substring(0,out.length( )-1);
		return count + ":" + out;
	}
	public String getTurn( ) {
		System.out.println( State.numMunchers );
		if( State.turnZero ) {
			State.turnZero = false;
			return "0";
		}	
		if( redRemaining == 0 ) 
			return greedyTurn( );
		String out = "";
		int count = 0;
		for( int i = 0; i < redMunchers.length; i++ ) {
			Muncher m = chokeRedMuncher( this, redMunchers[i] );
			if( m != null ) {
				if( numMunchableNodes(this, m.loc, m.program) <= 2 )
					continue;
				out += m.loc.id + "/" + m.program + ",";
				m.loc.view( );
				count++;
			}
		}
		spawnloc = new LinkedList<Integer>( );
		if( out.length( ) == 0 )
			return "0";
		out = out.substring(0,out.length( )-1);
		return count + ":" + out;
	}
	static public Muncher chokeRedMuncher( State state, Node redNode ) {
		int max = 0;
		Muncher bestMuncher = null;
		for( Node node : redNode.activeSiblings( ) ) {
			Muncher m = bestMuncherAtNode(state, node);
			int score = numMunchableNodes(state, node, m.program);
			if( score > max ) {
				max = score;
				bestMuncher = m;
			}
		}
		return bestMuncher;
	}
	static Muncher bestMuncherAtNode( State state, Node node ) {
		String bestProgram = null;
		int max = -1;
		for( String program : Muncher.allPrograms ) {
			int score = numMunchableNodes(state, node, program);
			if( score > max ) {
				max = score;
				bestProgram = program;
			}	
		}
		//if( bestProgram == null )
		//	return new Muncher( node, "udlr", 0 );
		return new Muncher( node, bestProgram, 0 );
	}
	static int numMunchableNodes( State state, Node node, String program ) {
		LinkedList<Integer> visited = new LinkedList<Integer>( );
		for( Node n : state.redMunchers ) 
			visited.add(n.id);
		return numMunchableNodesInner(node, program, 0, visited, 0);
	}	
	static int numMunchableNodesInner( Node node, String program, int counter, 
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
			if( !(n == null || n.munched || visited.contains(n.id)) ) {
				return numMunchableNodesInner( n, program, counter, visited, num+1 );
			}
		}
		return num+1;
	}
	/*
	static int numMunchableNodes( State state, int loc, String program ) {
		State copy = new State(state);
		Muncher reds[ ] = new Muncher[state.redMunchers.length];
		for( int i = 0; i < reds.length; i++ ) {
			reds[i] = bestMuncherAtNodeSimple(copy.redMunchers[i]);
		}
		Node node = copy.nodes[loc];
		Muncher m = new Muncher(node, program, 0);
		return numMunchableNodes( state, m, reds );
	}
	static int numMunchableNodes( State state, Muncher muncher, Muncher reds[ ] ) {
	
		int num = 0;
		while( !muncher.starved ) {
			for( Muncher m : reds ) 
				m.run( );
			muncher.run( );			
			muncher.loc.view( );
			num++;
		}		
		return num;
	}
	*/
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
	static int numBlocked = 0;
	static boolean turnZero = true;
	int blueScore;
	int redScore;
	int blueRemaining;
	int redRemaining;
	int numBlocked;
	static int numMunchers;
	int timeLeft;
}
