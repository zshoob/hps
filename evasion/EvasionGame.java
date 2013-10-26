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
	public static String winner;
	public static int winnerScore, winnerTries;
	public static BoardStates board;
	public static Hunter hunter;
	public static Prey prey;
	public static double startTime, duration;
	public static boolean caught;
	public static final int radius = 5;
	public static int score;
	public static String p1Name, p2Name;
	public static boolean turn;
	public static int p1Score, p2Score, p1Rounds, p2Rounds, p1Ave, p2Ave;
	public static boolean started, choosed;
	public static boolean startTimeNotUpdated;

	public void updateStartTime() {
		startTime = System.currentTimeMillis();
	}

	public void restart() {
		choosed = false;
		startTimeNotUpdated = true;
		startTime = 0;
		duration = 0;
		turn = true;
		caught = false;
		started = false;
		board = new BoardStates();
		hunter = new Hunter(50, 50, board);
		prey = new Prey(320, 200, board);
	}

	public EvasionGame() {
		choosed = false;
		p1Ave = 0;
		p2Ave = 0;
		winnerTries = 0;
		winnerScore = 0;
		p1Name = "Player1";
		p2Name = "Player2";
		winner = "not set";
		p1Rounds = 0;
		p2Rounds = 0;
		startTimeNotUpdated = true;
		startTime = 0;
		duration = 0;
		turn = true;
		p1Score = 0;
		p2Score = 0;
		caught = false;
		started = false;
		board = new BoardStates();
		hunter = new Hunter(50, 50, board);
		prey = new Prey(320, 200, board);

	}

	public boolean started() {
		if (started) {
			return true;
		}
		return false;
	}

	public boolean caught() {
		if (Math.abs(hunter.x - prey.x) <= radius
				&& Math.abs(hunter.y - prey.y) <= radius) {
			if (!caught) {

				caught = true;
				if (turn) {
					p1Rounds += 1;
					p1Ave = (p1Ave * (p1Rounds - 1) + score) / p1Rounds;
					if (score > p1Score) {
						p1Score = score;
					}
				} else {
					p2Rounds += 1;
					p2Ave = (p2Ave * (p2Rounds - 1) + score) / p2Rounds;
					if (score > p2Score) {
						p2Score = score;
					}
				}

				if (p1Score > p2Score) {
					winner = p1Name;
					winnerScore = p1Score;
					winnerTries = p1Rounds;
				} else if (p1Score < p2Score) {
					winner = p2Name;
					winnerScore = p2Score;
					winnerTries = p2Rounds;
				} else {
					winner = "not set";
					winnerScore = 0;
					winnerTries = 0;
				}
			}

			return true;
		} else {
			if (startTimeNotUpdated) {
				startTime = System.currentTimeMillis();
				startTimeNotUpdated = false;
			}
			duration = (System.currentTimeMillis() - startTime) / 1000;
			double tmpScore = 0;
			if (duration < 20.0) {
				tmpScore = 1024 * 20 / duration;
			} else if (duration < 30.0) {
				tmpScore = 512 * 30 / duration;
			} else if (duration < 40.0) {
				tmpScore = 256 * 40 / duration;
			} else {
				tmpScore = 128 * 6 / (Math.log(duration) / Math.log(2));

			}
			score = (int) tmpScore;
		}
		return false;
	}

	Font mySmallFont = new Font("Arial", Font.BOLD, 12);
	Font mySmallFont1 = new Font("Arial", Font.PLAIN, 11);

	public void draw(Graphics g, int w, int h) {
		String text;

		// clear screen

		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);

		g.setColor(Color.black);
		g.drawRect(0, 0, 500, 500);

		g.setColor(Color.red);
		int size = 8;
		g.fillRect(hunter.x - size / 2, hunter.y - size / 2, size, size);

		g.setColor(Color.blue);
		size = 4;
		g.fillRect(prey.x - size / 2, prey.y - size / 2, size, size);

		g.setFont(mySmallFont);
		g.setColor(Color.red);

		text = "BIG RED->Hunter";
		g.drawString(text, 10, 520);

		g.setColor(Color.blue);
		text = "little blue->Prey";
		g.drawString(text, 10, 540);

		g.setColor(Color.black);
		text = "Seconds used: " + duration;
		g.drawString(text, 330, 520);

		text = "Hunter's score: " + score;
		g.drawString(text, 330, 540);

		text = "-------------------------";
		g.drawString(text, 330, 560);

		text = "current winner: ";
		g.drawString(text, 330, 580);

		text = winner;
		g.drawString(text, 330, 600);

		text = "current winner's score: " + winnerScore;
		g.drawString(text, 330, 620);

		g.setFont(mySmallFont1);
		String instruction = "";

		g.setColor(Color.blue);

		instruction = "Hunter:\n";
		g.drawString(instruction, 10, 560);

		g.setColor(Color.black);

		instruction = "Build a vertical wall: v\n";
		g.drawString(instruction, 10, 575);

		instruction = "Build a horizontal wall: h\n";
		g.drawString(instruction, 10, 590);

		instruction = "Remove Wall: Number Key\n";
		g.drawString(instruction, 10, 605);
		g.setColor(Color.red);

		instruction = "Prey:\n";
		g.drawString(instruction, 10, 620);
		g.setColor(Color.black);

		instruction = "Use the arrow keys to move, survive for as long as you can.\n";
		g.drawString(instruction, 10, 635);

		g.setFont(mySmallFont);
		text = "Walls not used:";
		g.drawString(text, 120, 520);

	}
}
