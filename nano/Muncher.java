import java.util.LinkedList;

public class Muncher implements Comparable<Muncher> {
	public Muncher( Node loc, String program, int counter ) {
		this.loc = loc;
		this.counter = counter;
		this.program = program;
		this.starved = false;
		this.value = 0;
	}
	public Muncher( Muncher o ) {
		this.loc = o.loc;
		this.counter = o.counter;
		this.program = o.program;
		this.starved = o.starved;
		this.value = o.value;
	}
	public int compareTo( Muncher o ) {
		if( this.value < o.value )
			return -1;
		else if( this.value == o.value ) 
			return 0;
		else
			return 1;
	}
	public void run( ) {
		this.loc.munched = true;	
		boolean starve = true;
		for( int i = 0; i < 4; i++ ) {
			char instruction = this.program.charAt(this.counter);
			Node node = null;
			switch( instruction ) {
				case 'u': node = this.loc.up; break;
				case 'd': node = this.loc.down; break;				
				case 'l': node = this.loc.left; break;				
				case 'r': node = this.loc.right; break;				
			}
			this.counter = (this.counter + 1) % 4;
			if( !(node == null || node.munched) ) {
				starve = false;
				this.loc = node;
				break;
			}
		}
		if( starve ) {
			this.starved = true;
		}
	}
	static String[ ] allPrograms( ) {
		char dirs[ ] = new char[ ]{'u','d','l','r'};
		LinkedList<String> i = new LinkedList<String>( );
		for( char c : dirs )
			i.add("" + c);
		LinkedList<String> j;
		for( int loop = 0; loop < 3; loop++ ) {
			j = new LinkedList<String>( );
			for( String str : i ) 
				for( char c : dirs )
					if( str.indexOf( c ) < 0 )
						j.add( str + c );
			i = j;
		}
		return i.toArray(new String[0]);
	}	
	Node loc;
	int counter;
	final String program;
	boolean starved;
	static String[ ] allPrograms = allPrograms( );	
	int value;
}
