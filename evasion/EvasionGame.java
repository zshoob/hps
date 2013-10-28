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

  public EvasionGame(BoardStates board, Hunter hunter, Prey prey, int n, int m, int stepsToBuildWalls){
    this.board = board;
    this.hunter = hunter;
    this.prey = prey;
    turn = true;
    caught = false;
    availableArea = new int[2][2];
    this.m = m;
    this.n = n;
    this.stepsToBuildWalls = stepsToBuildWalls;
  }

  public static EvasionGame constructGame(String message, int n, int m){
    String[] messArr = message.split("\n");
    assert messArr[0].equals("Walls");
    int wallMount = Integer.parseInt(messArr[1]);
    BoardStates bs = new BoardStates();
    for(int i = 0; i < wallMount; i++){
      bs.walls[i] = parseWall(messArr[2 + i]);
    }
    // System.out.println(messArr[2 + wallMount]);

    int stepsToBuildWalls = Integer.parseInt(messArr[3 + wallMount]);

    Hunter hunter = parseHunter(messArr[4 + wallMount], bs);
    Prey prey = parsePrey(messArr[5 + wallMount], bs);

    return new EvasionGame(bs, hunter, prey, n, m, stepsToBuildWalls);
  }

  public static Wall parseWall(String str){
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
    // System.out.println("Hunter:" + str);
    int indexOfFirstWhiteSpace = str.indexOf(" ");

    int indexOfSecondWhiteSpace = str.indexOf(" ", indexOfFirstWhiteSpace + 1);
    // System.out.println("Index: " + indexOfFirstWhiteSpace + indexOfSecondWhiteSpace);
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


  public String getMoveOfPrey(){
    // get hunter info
    int hunterX = hunter.x;
    int hunterY = hunter.y;
    MoveType direction = hunter.type;

    //get prey info
    int preyX = prey.x;
    int preyY = prey.y;

    //get available area for prey to move
    int minX = Integer.MIN_VALUE; // smallest x prey can move to
    int minY = Integer.MIN_VALUE; // smallest y prey can move to
    int maxX = Integer.MAX_VALUE;
    int maxY = Integer.MAX_VALUE;
    for(Wall wall:board.walls){
      if(wall.position >= 0){
        // horizontal
        if(wall.direction == 0){
          if(wall.position > preyX && wall.position < maxX){
            maxX = wall.position;
          }
          if(wall.position < preyX && wall.position > minX){
            minX = wall.position;
          }
        }else{
          if(wall.position > preyY && wall.position < maxY){
            maxY = wall.position;
          }
          if(wall.position < preyY && wall.position > minY){
            minY = wall.position;
          }
        }
      }
    }

    System.out.println("PREY POSITION->" + preyX + ", " + preyY);
    System.out.println("HUNTER POSITION->" + hunterX + ", " + hunterY);
    System.out.println("PREY AREA->" + minX + "-" + maxX + "," + minY + "-" + maxY);

    // check whether prey and hunter same side
    boolean isSameSide = true;
    if(hunterX < minX || hunterX > maxX || hunterY < minY || hunterY > maxY){
      isSameSide = false;
    }

    // If not, try to move towards to current area's center
    if(!isSameSide){
      System.out.println("HUNTER AND PREY NOT SAME SIDE");
      int centerX = (minX + maxX) / 2;
      int centerY = (minY + maxY) / 2;
      return moveTowards(centerX, centerY, preyX, preyY);
    }

    // check whether the hunter will hit the wall before next build
    int stepsleft = stepsToBuildWalls;
    int hunterImageX = hunterX;
    int hunterImageY = hunterY;
    boolean willHunterTouchWall = false;
    switch(direction){
      case NE:
        hunterImageX = hunterX + stepsleft;
        hunterImageY = hunterY - stepsleft;
        break;
      case NW:
        hunterImageX = hunterX - stepsleft;
        hunterImageY = hunterY - stepsleft;
        break;
      case SE:
        hunterImageX = hunterX + stepsleft;
        hunterImageY = hunterY + stepsleft;
        break;
      case SW:
        hunterImageX = hunterX - stepsleft;
        hunterImageY = hunterY + stepsleft;
        break;
      default:
        break;
    }

    if(hunterImageX < minX || hunterImageX > maxX || hunterImageY < minY || hunterImageY > maxY){
      willHunterTouchWall = true;
    }

    // check whether hunter move towards prey
    boolean isHunterTowardsPrey = true;
    int hunterNextX = hunterX;
    int hunterNextY = hunterY;
    switch(direction){
      case NE:
        hunterNextX = hunterX + 2;
        hunterNextY = hunterY - 2;
        break;
      case NW:
        hunterNextX = hunterX - 2;
        hunterNextY = hunterY - 2;
        break;
      case SE:
        hunterNextX = hunterX + 2;
        hunterNextY = hunterY + 2;
        break;
      case SW:
        hunterNextX = hunterX - 2;
        hunterNextY = hunterY + 2;
        break;
      default:
        break;
    }

    double distanceNow = getDistance(hunterX, hunterY, preyX, preyY);
    double distanceOneStepAfter = getDistance(hunterNextX, hunterNextY, preyX, preyY);
    if(distanceNow < distanceOneStepAfter){
      isHunterTowardsPrey = false;
    }

    // check whether hunter will catch prey before building walls
    boolean willHunterCatchPrey = false;
    double nearestDistance = Double.MAX_VALUE;
    int stepsToCatch = 0;
    Hunter clone = new Hunter(hunterX, hunterY, board, direction);
    for(int i = 0; i < stepsleft / 2; i++){
      clone.bounceMove();
      double dist = getDistance(preyX, preyY, clone.x, clone.y);
      if(dist < nearestDistance){
        nearestDistance = dist;
      }
      if(dist <= RADIUS){
        willHunterCatchPrey = true;
        stepsToCatch = i + 1;
        break;
      }
    }

    // If hunter will catch preyer
    if(willHunterCatchPrey){
      if(isHunterTowardsPrey){
        System.out.println("Towards Prey and Will Catch");
        double angle = 0.0;
        angle = Math.abs(((double)(preyY - hunterY))/(preyX - hunterX));
        boolean isAngleLessThan45 = true;
        if(angle > 1.0){
          isAngleLessThan45 = false;
        }
        switch(direction){
          case NE:
            if(isAngleLessThan45){
              return "SE";
            }else{
              return "NW";
            }
          case NW:
            if(isAngleLessThan45){
              return "SW";
            }else{
              return "NE";
            }
          case SE:
            if(isAngleLessThan45){
              return "NE";
            }else{
              return "SW";
            }
          case SW:
            if(isAngleLessThan45){
              return "NW";
            }else{
              return "SE";
            }
          default:
            return "ZZ";
        }
      }else{
        System.out.println("Very rare case happens");
        // Will think how to move here
      }
    }

    //general conditions
    if(isHunterTowardsPrey){
      // If hunter towards prey, prey move towards hunter to get better chance to live
      // cause if prey move towards hunter, it will get bigger available area when hunter put wall
      switch(direction){
        case NE:
          return "SW";
        case NW:
          return "SE";
        case SE:
          return "NW";
        case SW:
          return "NE";
        default:
          return "ZZ";
      }
    }else{
      return direction.name();
    }

    // return "ZZ";
  }

  // best way to move towards specific point
  private String moveTowards(int centerX, int centerY, int preyX, int preyY){
    int diffX = preyX - centerX;
    int diffY = preyY - centerY;
    if(diffX == 0){
      if(diffY == 0){
        return "ZZ";
      }else if(diffY > 0){
        return "NN";
      }else{
        return "SS";
      }
    }
    if(diffY == 0){
      if(diffX > 0){
        return "WW";
      }else{
        return "EE";
      }
    }

    double angle = Math.abs(((double) diffX) / diffY);
    if(angle > 2.0 || angle < 0.5){
      if(diffX > 0 && diffY > 0){
        return "NW";
      }else if(diffX > 0 && diffY < 0){
        return "SW";
      }else if(diffX < 0 && diffY > 0){
        return "NE";
      }else{
        return "SE";
      }
    }else{
      if(Math.abs(diffX) > Math.abs(diffY)){
        if(diffX > 0){
          return "WW";
        }else{
          return "EE";
        }
      }else{
        if(diffY > 0){
          return "NN";
        }else{
          return "SS";
        }
      }
    }
    
  }

  public boolean caught() {
    if (getDistance(prey.x, prey.y, hunter.x, hunter.y) <= RADIUS) {
      return true;
    }else{
      return false;
    }
  }

  private double getDistance(int x1, int y1, int x2, int y2){
    int disX = x1 - x2;
    int disY = y1 - y2;
    return Math.sqrt(disX * disX + disY * disY);
  }

  public String getMoveOfHunter(){
    return "";
  }

}
