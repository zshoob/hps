public class Muncher {
	public Muncher( Node loc, String program, int counter ) {
		this.loc = loc;
		this.counter = counter;
		this.program = new char[4];
		for( int i = 0; i < 4; i++ )
			this.program[i] = program.charAt(i);
	}
	Node loc;
	int counter;
	char program[ ];
}