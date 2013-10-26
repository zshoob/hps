
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class EvasionClient{
  static Socket sock = null;
  static PrintWriter out = null;
  static BufferedReader in = null;
  static int port = 4567;
  static String host = "127.0.0.1";
  static String endofmsg = "<EOM>";
  static String teamName = "OffByOne";

  static EvasionGame constructGame(String str){

  }

  public static String readSocket(BufferedReader in) throws IOException{
    StringBuilder data = new StringBuilder();
    char[] cbuf = new char[2048];
    while((in.read(cbuf, 0, 2048))!= -1){
      data.append(cbuf);
      System.out.println("Temp data: " + data.toString() );
      System.out.println(data.toString().contains("\n"));
      System.out.println(data.toString().endsWith("\n"));
      if(data.toString().contains("\n")){
        System.out.println("break");
        break;
      }
    }
    return data.toString().replaceAll(endofmsg, "");
  }

  public static void sendSocket(PrintWriter out, String text){
    System.out.println("Sending : " + text);
    out.println(text);
  }

  public static void main(String[] args){
    if(args.length >=1){
      port = Integer.parseInt(args[0].trim());
    }

    if(args.length >=2){
      teamName = args[1].trim();
    }

    System.out.printf("%d %s\n",port,teamName);

    try{
      sock = new Socket(host,port);
      out = new PrintWriter(sock.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

      String handshake = readSocket(in);
      System.out.println("Accept socket message! ");
      assert handshake.trim().equalsIgnoreCase("Team Name?");
      sendSocket(out, teamName);

      String setupInfo = readSocket(in);
      setupInfo = readSocket(in);

    } catch (UnknownHostException e) {
      System.err.printf("Unknown host: %s:%d\n", host, port);
      System.exit(1);
    } catch (IOException e) {
     System.err.printf("Can't get I/O streams for the connection to: %s:%d\n", host, port);
     System.exit(1);
   }finally{
    try{
      in.close();
      out.close();
      sock.close();
    }catch(Exception e){
      System.out.println("Close Stream and socket exception!");
      System.exit(1);
    }
    
   }

    
 }

}
