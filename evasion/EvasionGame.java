// authors: { Do Kook Choe Nathaniel Weinman Tengchao Zhou }, the undergrads, the CWZ, the DNT
// javac *.java -classpath /System/Library/Frameworks/JavaVM.framework/Resources/Deploy.bundle/Contents/Resources/Java/plugin.jar

import java.awt.*;

class Wall {
	public int position;
	// 0 is horizontal 1 is vertical
	public int direction;
	public int status;
	public int x1;
	public int x2;
	public int y1;
	public int y2;


	public Wall(int position, int direction, int x1, int x2, int y1, int y2) {
		this.position = position;
		this.direction = direction;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.status = 0;
	}

	public void reset() {
		position = -100;
		direction = -100;
		x1 = 0;
		x2 = 0;
		y1 = 0;
		y2 = 499;
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
	
	public int[ ] enclosingQuadrangle( ) {
		int top = 0;
		int left = 0;
		int right = 499;
		int bottom = 499;
		for( Wall wall : b.walls ) {
			if( wall.status == 0 )
				continue;
			if( wall.direction == 0 ) { // horizontal
				if( wall.position < this.y && wall.position > top )
					if( wall.x1 < this.x && wall.x2 > this.x )
								top = wall.position;
				if( wall.position > this.y && wall.position < bottom )
					if( wall.x1 < this.x && wall.x2 > this.x )			
						bottom = wall.position;
			} else { // vertical
				if( wall.position < this.x && wall.position > left )
					if( wall.y1 < this.y && wall.y2 > this.y )				
						left = wall.position;
				if( wall.position > this.x && wall.position < right )
					if( wall.y1 < this.y && wall.y2 > this.y )		
						right = wall.position;			
			}
		}
		return new int[]{top,bottom,left,right};
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
			if(b.walls[i].position >= 0){
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
		}
		x += delta_x;
		y += delta_y;
	}
	
	String getMove(int stepsToBuildWalls, Prey prey) {
		return "";
	}	
}

class BoardStates {

	Wall[] walls;

	public BoardStates() {
		walls = new Wall[24];

		for (int i = 0; i < 20; i++) {
			walls[i] = new Wall(-100, 0, 0, 0, 0, 0);
		}

		walls[20] = new Wall(0, 0, 0, 0, 0, 499);
		walls[21] = new Wall(500, 1, 0, 499, 0, 0);
		walls[22] = new Wall(500, 0, 0, 499, 499, 499);
		walls[23] = new Wall(0, 1, 499, 499, 0, 499);
	}

  public BoardStates(Wall[] walls){
    this.walls = walls;
  }
}

public class EvasionGame {
	public  BoardStates board;
	public  ZachHunter hunter;
	public  Prey prey;
	public  boolean caught;
	public static final int RADIUS = 4;
	public  boolean turn;
	public  int[][] availableArea;
  public int stepsToBuildWalls;
  public int m;
  public int n;


	public EvasionGame(){
		turn = true;
		caught = false;
		board = new BoardStates();
		hunter = new ZachHunter(50, 50, board, MoveType.SE, n, m);
		prey = new Prey(320, 200, board);
		availableArea = new int[2][2];
	}

  public EvasionGame(BoardStates board, ZachHunter hunter, Prey prey, int n, int m, int steps){
    this.board = board;
    this.hunter = hunter;
    this.prey = prey;
    turn = true;
    caught = false;
    availableArea = new int[2][2];
    this.m = m;
    this.n = n;
    this.stepsToBuildWalls = steps;
  }

  public static EvasionGame constructGame(String message, int n, int m){
    String[] messArr = message.split("\n");
    assert messArr[0].equals("Walls");
    int wallMount = Integer.parseInt(messArr[1]);
    BoardStates bs = new BoardStates();
    for(int i = 0; i < wallMount; i++){
      bs.walls[i] = parseWall(messArr[2 + i]);
      bs.walls[i].status = 1;
    }
    // System.out.println(messArr[2 + wallMount]);

    int stepsToBuildWalls = Integer.parseInt(messArr[3 + wallMount]);
	
    ZachHunter hunter = parseZachHunter(messArr[4 + wallMount], bs, n, m);
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
      return new Wall(x1, 1, x1, x2, y1, y2);
    }else{
      return new Wall(y1, 0, x1, x2, y1, y2);
    }

  }

  public static ZachHunter parseZachHunter(String str, BoardStates bs, int n, int m){
    // System.out.println("ZachHunter:" + str);
    int indexOfFirstWhiteSpace = str.indexOf(" ");

    int indexOfSecondWhiteSpace = str.indexOf(" ", indexOfFirstWhiteSpace + 1);
    // System.out.println("Index: " + indexOfFirstWhiteSpace + indexOfSecondWhiteSpace);
    MoveType type = MoveType.valueOf(str.substring(indexOfFirstWhiteSpace + 1, indexOfSecondWhiteSpace));
    int indexOfLeftBrac = str.indexOf("(");
    int indexOfComm = str.indexOf(",");
    int indexOfRightBrac = str.indexOf(")");

    int x = Integer.parseInt(str.substring(indexOfLeftBrac + 1, indexOfComm));
    int y = Integer.parseInt(str.substring(indexOfComm + 1, indexOfRightBrac));
    return new ZachHunter(x, y, bs, type, n, m);
  }

  public static Prey parsePrey(String str, BoardStates bs){
    int indexOfLeftBrac = str.indexOf("(");
    int indexOfComm = str.indexOf(",");
    int indexOfRightBrac = str.indexOf(")");

    int x = Integer.parseInt(str.substring(indexOfLeftBrac + 1, indexOfComm));
    int y = Integer.parseInt(str.substring(indexOfComm + 1, indexOfRightBrac));
    return new Prey(x, y, bs);
  }


  public String getMoveOfHunter( ){
    return hunter.getMove(stepsToBuildWalls,prey);
    //return hunter.type + "h" + hunter.wallCoords(true) + "\n";
  }

  String dodge( int x, int y ) {
  	ZachHunter clone = new ZachHunter( hunter );
  	int quad[] = prey.enclosingQuadrangle( );
  	int minY = quad[0] + 1;
  	int minX = quad[2] + 1;
  	int avoid[][] = new int[16][2];
  	for( int i = 0; i < 16; i++ ) {
  		avoid[i][0] = clone.x;
  		avoid[i][1] = clone.y;
  		clone.bounceMove( );  		
  	} 
  	int besty = 0;
  	int bestx = 0;
  	for( int row = Math.max(prey.y-4,quad[0]); row < Math.min(prey.y+4,quad[1]); row++ ) {
	  	for( int col = Math.max(prey.x-4,quad[2]); col < Math.min(prey.x+4,quad[3]); col++ ) {  	
	  		int ydist = 100;
	  		int xdist = 100;	  		
	  		for( int a[ ] : avoid ) {
	  			ydist = Math.min(ydist,Math.abs(row-a[1]));
	  			xdist = Math.min(xdist,Math.abs(col-a[0]));	  			
	  		}	  		
	  		if( ydist >= besty && xdist >= bestx ) {
	  			besty = ydist;
	  			bestx = xdist;
	  			minX = col;
	  			minY = row;
	  		}
	  	}
  	}  	
  	return moveTowards(minX, minY, prey.x, prey.y);
  }

  public String getMoveOfPrey(){
  	
    // If hunter nearby
    if( Math.abs(prey.x-hunter.x) <= 16 && Math.abs(prey.y-hunter.y) <= 16)
      return dodge(prey.x, prey.y);
  	
  	
    // get hunter info
    int hunterX = hunter.x;
    int hunterY = hunter.y;
    MoveType direction = hunter.type;

    // used as copy of hunter
    Hunter clone = null;

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
        if(wall.direction == 1){
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


    // check wheter current area narrow enough (<= RADIUS )
    boolean isWidthLessThanRadius = false;
    if(maxX - minX <= 4 || maxY - minY <= 4){
      isWidthLessThanRadius = true;
    }

    // check whether prey and hunter same side
    boolean isSameSide = true;
    if(hunterX < minX || hunterX > maxX || hunterY < minY || hunterY > maxY){
      isSameSide = false;
    }

    if(isWidthLessThanRadius){
      // If same side, run away as far as possible
      if(isSameSide){
        if(maxX - minX <= 4){
          if(preyY - hunterY > 0){
            return "SS";
          }else{
            return "NN";
          }
        }else{
          if(preyX - hunterX > 0){
            return "EE";
          }else{
            return "WW";
          }
        }
      }
    }



    // If not, try to move towards to current area's center
    if(!isSameSide){
      System.out.println("HUNTER AND PREY NOT SAME SIDE");
      int targetX = (minX + maxX) / 2;
      int targetY = (minY + maxY) / 2;
      if(hunterX > maxX && hunterY >= minY && hunterY <= maxY){
        targetX = maxX;
      }else if(hunterX < minX && hunterY >= minY && hunterY <= maxY){
        targetX = minX;
      }else if(hunterX >= minX && hunterX <= maxX && hunterY > maxY){
        targetY = maxY;
      }else if(hunterX >= minX && hunterX <= maxX && hunterY < minY){
        targetY = minY;
      }else if(hunterX >= maxX && hunterY >= maxY){
        targetX = maxX;
        targetY = maxY;
      }else if(hunterX >= maxX && hunterY <= minY){
        targetX = maxX;
        targetY = minY;
      }else if(hunterX <= minX && hunterY >= maxY){
        targetX = minX;
        targetY = maxY;
      }else if(hunterX <= minX && hunterY <= minY){
        targetX = minX;
        targetY = minY;
      }
      
      // can optimize here
      return moveTowards(targetX, targetY, preyX, preyY);
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
    clone = new Hunter(hunterX, hunterY, board, direction);
    for(int i = 0; i < stepsleft / 2; i++){
      // BUG HERE
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
    }

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
    if(angle > 0.3 && angle < 3.0){
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

}
