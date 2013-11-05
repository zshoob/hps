import java.util.Arrays;
import java.util.LinkedList;
import java.util.Collections;
import java.io.*;


public class State {
	public static void main( String args[ ] ) {
		State state = new State(readTestInput(true));		
		state.update(readTestInput(false));		
		State copy = new State(state);
		System.out.println(state.nodes[0] == copy.nodes[0]);
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
				} else if( u.yloc > v.yloc ) {
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
			String red[ ] = lines[2].substring(lines[2].indexOf(":")+1).split(",");		
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
					bestMuncher.run(true);
			}
		}
		out = out.substring(0,out.length( )-1);
		return count + ":" + out;
	}
	/*
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
			Muncher m = blockRedMuncher( this, redMunchers[i] );
			if( m != null ) {
				if( numMunchableNodes(this, m.loc, m.program) <= 2 )
					continue;
				out += m.loc.id + "/" + m.program + ",";
				m.loc.view( );
				count++;
			}
		}
		if( out.length( ) == 0 )
			return "0";
		out = out.substring(0,out.length( )-1);
		return count + ":" + out;
	}
	*/
	public String getTurn( ) {
		if( State.turnZero ) {
			State.turnZero = false;
			return "0";
		}	
		if( numBlocked == numMunchers ) 
			return greedyTurn( );
		int numToBlock = (numMunchers-redRemaining) - numBlocked;
		System.out.println( "red remaining: " + redRemaining + "\tnum to block: " + numToBlock );		
		if( numToBlock <= 0 )
			return "0";	
		LinkedList<Muncher> mList = new LinkedList<Muncher>( );		
		for( Node redNode : redMunchers ) {
			Muncher m = blockRedMuncher( this, redNode );
			if( m == null || m.value <= 2 )
				continue;
			mList.add(m);
		}
		System.out.println( "list size: " + mList.size( ) );		 
		if( mList.size( ) == 0 )
			return "0";
		numBlocked += mList.size( );
		Muncher mArray[ ] = mList.toArray(new Muncher[0]);
		Arrays.sort(mArray, Collections.reverseOrder( ));
		mArray = Arrays.copyOf(mArray,Math.min(mArray.length, numToBlock));
		String out = mArray.length + ":";
		for( Muncher m : mArray ) {
			out += m.loc.id + "/" + m.program + ",";
			System.out.println( "muncher score: " + m.value );
		}
		out = out.substring(0,out.length( )-1);
		return out;		
	}
	static public Muncher blockRedMuncher( State state, Node redNode ) {
		int max = 0;
		Muncher bestMuncher = null;
		for( Node node : redNode.activeSiblings( ) ) {
			Muncher m = bestMuncherAtNode(state, node);
			int score = m.value;
			System.out.println( "score: " + score );
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
		if( bestProgram == null )
			return new Muncher( node, "udlr", 0 );
		Muncher out = new Muncher( node, bestProgram, 0 );
		out.value = max;
		return out;
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
	static Muncher bestMuncherAtNode2( State state, Node node ) {
		LinkedList<Muncher> redList = new LinkedList<Muncher>( );
		for( Node redNode : state.redMunchers ) {
			Muncher r = bestMuncherAtNode( state, redNode );
			if( r != null )
				redList.add(r);
		}
		int max = 0;
		String bestProgram = null;
		for( String program : Muncher.allPrograms ) {
			int score = numMunchableNodes2( state, node, program, redList );
			if( score >= max ) {
				max = score;
				bestProgram = program;
			}
		}
		if( bestProgram == null )
			return null;
		Muncher out = new Muncher( node, bestProgram, 0 );
		out.value = max; 
		return out;
	}
	static int numMunchableNodes2( State state, Node node, 
								   String program, LinkedList<Muncher> mList ) {
		for( Node n : state.nodes )
			n.visitedBy = 0;
		System.out.println(program);
		System.out.print( "start: " );
		node.view( );
		Muncher blue = new Muncher( node, program, 0 );
		mList.add(blue);
		int num = 0;
		LinkedList<Node> visited = new LinkedList<Node>( );
		while( !blue.starved ) {
			num++;
			for( Muncher m : mList ) {
					visited.add(m.loc);
					if( m == blue )
						m.loc.visitedBy = 1;
					else
						m.loc.visitedBy = 2;
				if( !m.starved ) {
					m.run( visited );
				}
			}
			for( Muncher a : mList ) {
				for( Muncher b : mList ) {
					if( a == b )
						continue;
					if( a.loc == b.loc ) {
						if( a.lastMove == 'u' )
							b.starved = true;
						else if( b.lastMove == 'u' )
							a.starved = true;							
						else if( a.lastMove == 'l' )
							b.starved = true;														
						else if( b.lastMove == 'l' )
							a.starved = true;							
						else if( a.lastMove == 'd' )
							b.starved = true;														
						else if( b.lastMove == 'd' )
							a.starved = true;																																									
						else if( a.lastMove == 'r' )
							b.starved = true;														
						else
							a.starved = true;							
					}
				}
			}	
		}
		System.out.print( num + "\n\n" );
		return num;
	}
	/*
	static int numMunchableNodes2( State state, Node node, 
								   String program, LinkedList<Muncher> mList ) {
		System.out.println(program);
		System.out.print( "start: " );
		node.view( );
		State copy = new State(state);
		//int count = 0;
		//for( Node n : copy.nodes ) 
		//	if( n.munched )
		//		count++;
		//System.out.println(count);
		Muncher blue = new Muncher( copy.nodes[node.id], program, 0 );
		for( Muncher m : mList )
			m.loc = copy.nodes[m.loc.id];
		mList.add(blue);
		int num = 0;
		while( !blue.starved ) {
			num++;
			for( Muncher m : mList ) {
					m.loc.munched = true;		
				if( !m.starved ) {
					m.run(false);
				}
			}
			for( Muncher a : mList ) {
				for( Muncher b : mList ) {
					if( a == b )
						continue;
					if( a.loc == b.loc ) {
						if( a.lastMove == 'u' )
							b.starved = true;
						else if( b.lastMove == 'u' )
							a.starved = true;							
						else if( a.lastMove == 'l' )
							b.starved = true;														
						else if( b.lastMove == 'l' )
							a.starved = true;							
						else if( a.lastMove == 'd' )
							b.starved = true;														
						else if( b.lastMove == 'd' )
							a.starved = true;																																									
						else if( a.lastMove == 'r' )
							b.starved = true;														
						else
							a.starved = true;							
					}
				}
			}	
		}
		System.out.print( num + "\n\n" );
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
	static boolean turnZero = true;
	int blueScore;
	int redScore;
	int blueRemaining;
	int redRemaining;
	int numBlocked;
	static int numMunchers;
	int timeLeft;
}
