// authors: { Do Kook Choe Nathaniel Weinman Tengchao Zhou }, the undergrads, the CWZ, the DNT

import java.awt.*;
import java.awt.event.*;
//import netscape.javascript.JSObject;

/*
<applet name="evasionapplet" CODE="EvasionApplet.class" WIDTH="650" HEIGHT="650" MAYSCRIPT>
		Java Error
	</applet>
*/
public class EvasionApplet extends BufferedApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String info1, info2;

	Font myBigFont1 = new Font("Helvetica", Font.BOLD, 20);
	Font mySmallFont1 = new Font("Helvetica", Font.BOLD, 20);
	Font mySmallFont2 = new Font("Helvetica", Font.BOLD, 10);

	public String getWinner() {
		return EvasionGame.winner;
	}

	public String getWinnerScore() {
		return "" + EvasionGame.winnerScore;
	}

	public String getWinnerTries() {
		return "" + EvasionGame.winnerTries;
	}

	public void render(Graphics g) {
		info1 = "Highest score: " + EvasionGame.p1Score + "\nAverage score: "
				+ EvasionGame.p1Ave + "\nYou played hunter\n for "
				+ EvasionGame.p1Rounds + " round(s).";
		info2 = "Highest score: " + EvasionGame.p2Score + "\nAverage score: "
				+ EvasionGame.p2Ave + "\nYou played hunter\n for "
				+ EvasionGame.p2Rounds + " round(s).";

		if (!eva.started()) {
			if (p1Score.getText() != info1) {
				p1Score.setText(info1);

			}
			if (p2Score.getText() != info2) {
				p2Score.setText(info2);

			}
		} else {

			if (EvasionGame.choosed) {
				if (EvasionGame.turn) {
					info1 = "Hunter\n" + info1;
					info2 = "Prey\n" + info2;
				} else {
					info1 = "Prey\n" + info1;
					info2 = "Hunter\n" + info2;

				}
			}
			if (p1Score.getText() != info1) {
				p1Score.setText(info1);

			}
			if (p2Score.getText() != info2) {
				p2Score.setText(info2);

			}
			int w = getWidth();
			int h = getHeight();
			eva.draw(g, w, h);

			if (eva.caught()) {
				if (!showButton) {
					//JSObject win = JSObject.getWindow(this);
					//win.call("setVisible", null);
					showButton = true;

				}

				g.setFont(myBigFont1);
				String text;
				text = "Game Over!!!";
				g.drawString(text, 10, 100);
				text = "The prey is caught in " + EvasionGame.duration
						+ " seconds.";
				g.drawString(text, 10, 130);
				text = "The hunter got " + EvasionGame.score + " points.";
				g.drawString(text, 10, 160);
				text = "Press the restart button to restart.";
				g.drawString(text, 10, 190);

			} else {

				for (int i = 1; i < EvasionGame.board.walls.length - 4; i++) {
					g.setColor(Color.black);
					if (EvasionGame.board.walls[i].status == 0) {
						g.setFont(mySmallFont1);
						g.drawString("" + i, 100 + 20 * i, 540);
					} else {
						g.setFont(mySmallFont2);
						if (EvasionGame.board.walls[i].direction == 0) {
							g.drawLine(0, EvasionGame.board.walls[i].position,
									500, EvasionGame.board.walls[i].position);
							g.drawString(Integer.toString(i), 250,
									EvasionGame.board.walls[i].position);
						} else {
							g.drawLine(EvasionGame.board.walls[i].position, 0,
									EvasionGame.board.walls[i].position, 500);
							g.drawString(Integer.toString(i),
									EvasionGame.board.walls[i].position, 250);

						}
					}

				}

				if (keyDown[KeyEvent.VK_UP] == true) {
					EvasionGame.prey.move(0, -1);
				}
				if (keyDown[KeyEvent.VK_DOWN] == true) {
					EvasionGame.prey.move(0, 1);

				}
				if (keyDown[KeyEvent.VK_LEFT] == true) {
					EvasionGame.prey.move(-1, 0);
				}
				if (keyDown[KeyEvent.VK_RIGHT] == true) {
					EvasionGame.prey.move(1, 0);
				}
				EvasionGame.hunter.bounceMove();

				if (wasKeyDown['h'] && !keyDown['h']) {
					for (int i = 1; i <= 9; i++) {
						if (EvasionGame.board.walls[i].status == 0) {
							EvasionGame.board.walls[i].set(
									EvasionGame.hunter.y, 0);
							EvasionGame.board.walls[i].status = 1;
							break;
						}
					}

				} else if (wasKeyDown['v'] && !keyDown['v']) {

					for (int i = 1; i <= 9; i++) {
						if (EvasionGame.board.walls[i].status == 0) {
							EvasionGame.board.walls[i].set(
									EvasionGame.hunter.x, 1);
							EvasionGame.board.walls[i].status = 1;
							break;
						}
					}

				}

				for (int i = 1; i <= 9; i++) {
					if (wasKeyDown[48 + i] && !keyDown[48 + i]) {
						EvasionGame.board.walls[i].reset();
						EvasionGame.board.walls[i].status = 0;
					}
				}

			}
			animating = true;
		}
	}
}
