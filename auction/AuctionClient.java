
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AuctionClient{
  static Socket sock = null;
  static PrintWriter out = null;
  static BufferedReader in = null;
  static int port = 4567;
  static String host = "127.0.0.1";
  static String teamName = "OffByOne";
  static String eom = "<EOM>";
  static int playerNo = 0;
  static int itemType = 0;
  static Player[] players;
  static Item[] items;
  static int goal;
  static int playerId;

  public static String readSocket(BufferedReader in) throws IOException{
    StringBuilder sb = new StringBuilder();
    char[] cbuf = new char[2048];
    while((in.read(cbuf, 0, 2048))!= -1){
      sb.append(cbuf);
      // System.out.println("Got message" + sb.toString());
      if(sb.toString().contains(eom)){
        System.out.println("break");
        break;
      }
    }
    return sb.toString().replaceAll(eom, "");
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
    System.out.println("Item List " + Arrays.toString(items));
  }

  private static void parseUpdateInfo(String str, int itemId){
    String[] splitedStr = str.split("\\s+");
    int winnerId = Integer.parseInt(splitedStr[0]);
    int winnerBid = Integer.parseInt(splitedStr[1]);
    int budget = Integer.parseInt(splitedStr[2]);
    System.out.println("budget: " + budget);
    players[winnerId].winItem(items[itemId], winnerBid);
    List<Item> lists = players[winnerId].getOwnedItems();
    System.out.print("Player: " + winnerId + " list: ");
    for(Item item:lists){
      System.out.print(item.getType() + " ");
    }
    System.out.println();
  }

  public static void main(String[] args){

    if(args.length >=1){
      port = Integer.parseInt(args[0].trim());
    }

    try{
      sock = new Socket(host,port);
      out = new PrintWriter(sock.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
      
      String handshake = readSocket(in);
      if(handshake.contains("Name")){
        sendSocket(out, teamName);
      }
      String setupInfo = readSocket(in).trim();
      parseInitialInfo(setupInfo);
      
      // send first random bid
      Random random = new Random();
      int ranValue = random.nextInt(10);
      sendSocket(out, String.valueOf(ranValue));

      int itemId = 0;
      while(true){
        String updateInfo = readSocket(in).trim();
        if(updateInfo.equals("")){
          System.out.println("Game ends!");   
          break;
        }
        parseUpdateInfo(updateInfo, itemId);
        // send random bin
        ranValue = random.nextInt(10);
        sendSocket(out, String.valueOf(ranValue));
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
