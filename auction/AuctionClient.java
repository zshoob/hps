
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
  static int itemType = 0;
  static Player[] players;
  static Item[] items;
  static int goal;
  static int playerId;

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
    System.out.println("Sending : " + text);
    out.println(text);
  }

  private static void parseInitialInfo(String str){
    String[] splitedStr = str.split("\\s+");
    int itemNo = splitedStr.length - 4;
    playerId = Integer.parseInt(splitedStr[0]);
    playerNo = Integer.parseInt(splitedStr[1]);
    itemType = Integer.parseInt(splitedStr[2]);
    goal = Integer.parseInt(splitedStr[3]);
    players = new Player[playerNo];
    items = new Item[itemNo];
    for(int j = 0; j < playerNo; j++){
      players[j] = new Player(j);
    }
    for(int i = 0; i < itemNo; i++){
      items[i] = new Item(Integer.parseInt(splitedStr[i + 4]));
    }
  }

  private static void parseUpdateInfo(String str, int itemId){
    String[] splitedStr = str.split("\\s+");
    int winnerId = Integer.parseInt(splitedStr[0]);
    int winnerBid = Integer.parseInt(splitedStr[1]);
    int budget = Integer.parseInt(splitedStr[2]);
    System.out.println("budget: " + budget);
    players[winnerId].winItem(items[itemId], winnerBid);
  }

  public static void main(String[] args){

    if(args.length >=1){
      port = Integer.parseInt(args[0].trim());
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
      System.out.println("set up info" + setupInfo);
      parseInitialInfo(setupInfo);
      
      // send first bid
      String output = "0";
      sendSocket(out, output);
      int itemId = 0;
      while(true){
        String updateInfo = in.readLine().trim();
        parseUpdateInfo(updateInfo, itemId);
        sendSocket(out, output);
        itemId++;
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
