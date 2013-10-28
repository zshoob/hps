
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
  static String character = "P"; // H for hunter P for prey

  // public static String readSocket(BufferedReader in) throws IOException{
  //   StringBuilder data = new StringBuilder();
  //   char[] cbuf = new char[2048];
  //   while((in.read(cbuf, 0, 2048))!= -1){
  //     data.append(cbuf);
  //     System.out.println("Temp data: " + data.toString() );
  //     if(data.toString().contains("\n")){
  //       System.out.println("break");
  //       break;
  //     }
  //   }
  //   return data.toString().replaceAll(endofmsg, "");
  // }

  public static String readSocket(BufferedReader in) throws IOException{
    StringBuilder sb = new StringBuilder();
    String data = null;
    int count = 0;
    int total = -1;
    while((data = in.readLine()) != null){
      if(count == 1){
        total = Integer.parseInt(data) + 6;
      }
      System.out.println(data);
      sb.append(data);
      sb.append("\n");
      if(count == total){
        break;
      }
      count++;
    }

    return sb.toString();
  }

  public static void sendSocket(PrintWriter out, String text){
    System.out.println("Sending : " + text);
    out.println(text);
  }

  public static void main(String[] args){
    if(args.length >=1){
      character = args[0].trim();
    }

    if(args.length >=2){
      port = Integer.parseInt(args[1].trim());
    }

    System.out.printf("%d %s\n",port,teamName);

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
      System.out.printf("M:%d N%d %n", m, n);

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
