
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class AuctionClient{
  static Socket sock = null;
  static PrintWriter out = null;
  static BufferedReader in = null;
  static int port = 4567;
  static String host = "127.0.0.1";
  static String teamName = "OffByOne";
  static int playerNo = 0;
  static Player[] player;
  static Item[] items;
  public static String readSocket(BufferedReader in) throws IOException{
    String data = null;
    if((data = in.readLine()) != null){
      System.out.println("Got data:" + data);
      return data;
    }else{
      return null;
    }
  }

  public static void sendSocket(PrintWriter out, String text){
    //System.out.println("Sending : " + text);
    out.println(text);
  }

  public static void main(String[] args){
    if(args.length >=1){
      character = args[0].trim();
    }

    if(args.length >=2){
      port = Integer.parseInt(args[1].trim());
    }

    try{
      sock = new Socket(host,port);
      out = new PrintWriter(sock.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

      String handshake = in.readLine();
      if(handshake.contains("Team")){
        sendSocket(out, teamName);
      }

      String setupInfo = in.readLine().trim();
      int n = Integer.parseInt(setupInfo.substring(0, setupInfo.indexOf(" ")));
      int m = Integer.parseInt(setupInfo.substring(setupInfo.indexOf(" ") + 1));
      // System.out.printf("M:%d N%d %n", m, n);

      while(true){
        String inputInfo = readSocket(in);
        EvasionGame game = EvasionGame.constructGame(inputInfo, n, m);
        //call the move method. For Zach, you may need to use getMoveOfHunter instead
        String output = null;
        if(character.equals("H")){
          output = game.getMoveOfHunter();
        }else{
          output = game.getMoveOfPrey();
        }
        sendSocket(out, output);
      }
      

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
