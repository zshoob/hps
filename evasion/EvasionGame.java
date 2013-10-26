// authors: { Do Kook Choe Nathaniel Weinman Tengchao Zhou }, the undergrads, the CWZ, the DNT
// javac *.java -classpath /System/Library/Frameworks/JavaVM.framework/Resources/Deploy.bundle/Contents/Resources/Java/plugin.jar

import java.awt.*;

class Wall {
	public int position;
	// 0 is horizontal 1 is vertical
	public int direction;
	public int status;

	public Wall(int position, int direction) {
		this.position = position;
		this.direction = direction;
		this.status = 0;
	}

	public void reset() {
		position = -100;
		direction = -100;
	}

	public void set(int p, int d) {
		position = p;
		direction = d;
	}
}

class Player {

	public int x, y;
	public BoardStates b;

	public Player(int x, int y, BoardStates board) {
		this.x = x;
		this.y = y;
		this.b = board;
	}
}

class Prey extends Player {
	public Prey(int x, int y, BoardStates board) {
		super(x, y, board);
	}

	public void move(int delta_x, int delta_y) {

		int new_x = x + delta_x;
		int new_y = y + delta_y;

		for (int i = 0; i < b.walls.length; i++) {
			if (b.walls[i].direction == 0) {
				if (y < b.walls[i].position && new_y >= b.walls[i].position) {
					new_y = y;
				}
				if (y > b.walls[i].position && new_y <= b.walls[i].position) {
					new_y = y;
				}
			}
			if (b.walls[i].direction == 1) {
				if (x < b.walls[i].position && new_x >= b.walls[i].position) {
					new_x = x;
				}
				if (x > b.walls[i].position && new_x <= b.walls[i].position) {
					new_x = x;
				}
			}
		}

		this.x = new_x;
		this.y = new_y;

	}
}

enum MoveType{
  NN, WW, EE, SS, NW, NE, SW, SE,;
}

class Hunter extends Player {
	int delta_x, delta_y;
  MoveType type;

	public Hunter(int x, int y, BoardStates board, MoveType type) {
		super(x, y, board);
    this.type = type;
    switch(type){
      case SE:
        delta_x = 2;
        delta_y = 2;
        break;
      case NE:
        delta_x = 2;
        delta_y = -2;
        break;
      case NW:
        delta_x = -2;
        delta_y = -2;
        break;
      case SW:
        delta_x = -2;
        delta_y = 2;
        break;
      default:
        break;
    }
	}

	public void bounceMove() {

		int new_x = x + delta_x;
		int new_y = y + delta_y;
		for (int i = 0; i < b.walls.length; i++) {
			if (b.walls[i].direction == 0) {
				if (y < b.walls[i].position && new_y >= b.walls[i].position) {
					delta_y *= -1;
				}
				if (y > b.walls[i].position && new_y <= b.walls[i].position) {
					delta_y *= -1;
				}
			}
			if (b.walls[i].direction == 1) {
				if (x < b.walls[i].position && new_x >= b.walls[i].position) {
					delta_x *= -1;
				}
				if (x > b.walls[i].position && new_x <= b.walls[i].position) {
					delta_x *= -1;
				}
			}
		}

		x += delta_x;
		y += delta_y;
	}
}

class BoardStates {

	Wall[] walls;

	public BoardStates() {
		walls = new Wall[24];

		for (int i = 0; i < 20; i++) {
			walls[i] = new Wall(-100, 0);
		}

		walls[20] = new Wall(0, 0);
		walls[21] = new Wall(500, 1);
		walls[22] = new Wall(500, 0);
		walls[23] = new Wall(0, 1);
	}

  public BoardStates(Wall[] walls){
    this.walls = walls;
  }
}

public class EvasionGame {
	public  BoardStates board;
	public  Hunter hunter;
	public  Prey prey;
	public  boolean caught;
	public static final int RADIUS = 4;
	public  boolean turn;
	public  int[][] availableArea;
  private int stepsToBuildWalls;
  public int m;
  public int n;


	public EvasionGame(){
		turn = true;
		caught = false;
		board = new BoardStates();
		hunter = new Hunter(50, 50, board, MoveType.SE);
		prey = new Prey(320, 200, board);
		availableArea = new int[2][2];
	}

  public EvasionGame(BoardStates board, Hunter hunter, Prey prey, int n, int m){
    this.board = board;
    this.hunter = hunter;
    this.prey = prey;
    turn = true;
    caught = false;
    availableArea = new int[2][2];
    this.m = m;
    this.n = n;
  }

  public static EvasionGame constructGame(String message, int n, int m){
    String[] messArr = message.split("\n");
    assert messArr[0].equals("Walls");
    int wallMount = Integer.parseInt(messArr[1]);
    BoardStates bs = new BoardStates();
    for(int i = 0; i < wallMount; i++){
      bs.walls[i] = parseWall(messArr[2 + i]);
    }
    System.out.println(messArr[2 + wallMount]);

    int stepsToBuildWalls = Integer.parseInt(messArr[3 + wallMount]);

    Hunter hunter = parseHunter(messArr[4 + wallMount], bs);
    Prey prey = parsePrey(messArr[5 + wallMount], bs);

    return new EvasionGame(bs, hunter, prey, n, m);
  }

  public static Wall parseWall(String str){
    System.out.println("WALL " + str);
    int indexOfLeftBrac = str.indexOf("(");
    int indexOfFirstComm = str.indexOf(",");
    int indexOfRightBrac = str.indexOf(")");
    int x1 = Integer.parseInt(str.substring(indexOfLeftBrac + 1, indexOfFirstComm));
    int y1 = Integer.parseInt(str.substring(indexOfFirstComm + 1, indexOfRightBrac));

    indexOfLeftBrac = str.indexOf("(", indexOfRightBrac);
    indexOfFirstComm = str.indexOf(",", indexOfLeftBrac);
    indexOfRightBrac = str.indexOf(")", indexOfFirstComm);
    int x2 = Integer.parseInt(str.substring(indexOfLeftBrac + 1, indexOfFirstComm));
    int y2 = Integer.parseInt(str.substring(indexOfFirstComm + 1, indexOfRightBrac));

    if(x1 == x2){
      return new Wall(x1, 1);
    }else{
      return new Wall(y1, 0);
    }

  }

  public static Hunter parseHunter(String str, BoardStates bs){
    System.out.println("Hunter:" + str);
    int indexOfFirstWhiteSpace = str.indexOf(" ");

    int indexOfSecondWhiteSpace = str.indexOf(" ", indexOfFirstWhiteSpace + 1);
    System.out.println("Index: " + indexOfFirstWhiteSpace + indexOfSecondWhiteSpace);
    MoveType type = MoveType.valueOf(str.substring(indexOfFirstWhiteSpace + 1, indexOfSecondWhiteSpace));
    int indexOfLeftBrac = str.indexOf("(");
    int indexOfComm = str.indexOf(",");
    int indexOfRightBrac = str.indexOf(")");

    int x = Integer.parseInt(str.substring(indexOfLeftBrac + 1, indexOfComm));
    int y = Integer.parseInt(str.substring(indexOfComm + 1, indexOfRightBrac));
    return new Hunter(x, y, bs, type);
  }

  public static Prey parsePrey(String str, BoardStates bs){
    int indexOfLeftBrac = str.indexOf("(");
    int indexOfComm = str.indexOf(",");
    int indexOfRightBrac = str.indexOf(")");

    int x = Integer.parseInt(str.substring(indexOfLeftBrac + 1, indexOfComm));
    int y = Integer.parseInt(str.substring(indexOfComm + 1, indexOfRightBrac));
    return new Prey(x, y, bs);
  }

	public boolean caught() {
		if (getDistance() <= RADIUS) {
			return true;
		}else{
			return false;
		}
	}

	private double getDistance(){
    int disX = hunter.x - prey.x;
    int disY = hunter.y - prey.y;
    return Math.sqrt(disX * disX + disY * disY);
  }

  private void getAvailableArea(){
  	int minX = Integer.MAX_VALUE;
  	int minY = Integer.MAX_VALUE;
  	int maxX = Integer.MIN_VALUE;
  	int maxY = Integer.MIN_VALUE;
  	for(Wall wall:board.walls){
  		if(wall.position >= 0){
  			// horizontal
  			if(wall.direction == 0){
  				if(wall.position > maxX){
  					maxX = wall.position;
  				}
  				if(wall.position < minX){
  					minX = wall.position;
  				}
  			}else{
  				if(wall.position > maxY){
  					maxY = wall.position;
  				}
  				if(wall.position < minY){
  					minY = wall.position;
  				}
  			}
  		}
  	}

  	availableArea[0][0] = minX;
  	availableArea[0][1] = maxX;
  	availableArea[1][0] = minY;
  	availableArea[1][1] = maxY;
  }

  public String getMoveOfPrey(){
  	getAvailableArea();
    int n = stepsToBuildWalls;
    while(n >= 0){
      hunter.bounceMove();
      n--;
    }
    return "";
  }

  public String getMoveOfHunter(){
    return "";
  }

}
