import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class OffByOne {
	static Socket client;
    static PrintWriter out;
    static BufferedReader in;
    public static void main(String args[ ]) throws UnknownHostException, IOException {
        if (args.length != 1) {
            System.out.println("java RandomPlayer <port>");
            System.exit(0);
        }
        int port = Integer.parseInt(args[0]);   
       	OffByOne player = new OffByOne(port);
    }
    public OffByOne(int port) throws UnknownHostException, IOException {
        client = new Socket("127.0.0.1", port);
        out = new PrintWriter(client.getOutputStream( ), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream( )));        
        send("OffByOne");
        //send("0");
        State state = new State(receive( ));
        while(true) {
        	String input = receive( );
        	if( input.length( ) == 0 )
        		break;
			state.update(input);        
        	send(state.getTurn( ));
        }    
    }
    public String receive( ) throws IOException {
        StringBuffer sb = new StringBuffer( );
        /*
        String temp;
        while (!(temp = in.readLine( )).equalsIgnoreCase("<EOM>")) {
            sb.append(temp + "\n");
        }
        */
        String temp = in.readLine( );
        while( !(temp == null || temp.equalsIgnoreCase("<EOM>")) ) {
			sb.append(temp + "\n");        	
			temp = in.readLine( );
        }
        if( sb.length( ) > 0 )
	        sb.deleteCharAt(sb.length( ) - 1);
        System.out.println("receive:");
        System.out.println(sb.toString( ));
        return sb.toString( );
    }
    public void send(String str) {
        System.out.println("send:");
        out.println(str);
        System.out.println(str);
        out.println("<EOM>");
        System.out.println("<EOM>");
    }    
}
