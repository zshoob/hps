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

class Hunter extends Player {
	int delta_x, delta_y;

	public Hunter(int x, int y, BoardStates board) {
		super(x, y, board);
		delta_x = 2;
		delta_y = 2;
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
}

public class EvasionGame {
	public static BoardStates board;
	public static Hunter hunter;
	public static Prey prey;
	public static boolean caught;
	public static final int RADIUS = 4;
	public static boolean turn;
	public static int[][] availableArea;
  private int stepsToBuildWalls;


	public EvasionGame() {
		turn = true;
		caught = false;
		board = new BoardStates();
		hunter = new Hunter(50, 50, board);
		prey = new Prey(320, 200, board);
		availableArea = new int[2][2];
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

  private getAvailableArea(){
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
    
  }

  public String getMoveOfHunter(){

  }

}
