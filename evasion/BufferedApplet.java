/*
Java class to implement a double buffered applet
You have my permission to use freely, as long
as you keep the attribution. - Ken Perlin
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import javax.swing.border.Border;
import javax.swing.event.*;
//import netscape.javascript.JSObject;

public abstract class BufferedApplet extends java.applet.Applet implements
		Runnable, MouseListener, MouseMotionListener, KeyEventDispatcher {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	class RestartAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			main.restart();

			//JSObject message = JSObject.getWindow(main);
			//message.call("hideMessage", null);
		}
	}

	class RoleAction implements ActionListener {
		int order;

		public RoleAction(int order) {
			this.order = order;
		}

		public void actionPerformed(ActionEvent ae) {
			KeyboardFocusManager.getCurrentKeyboardFocusManager()
					.addKeyEventDispatcher(mainKeyEventDispatcher);

			eva.restart();

			EvasionGame.started = true;
			EvasionGame.choosed = true;

			if (order == 1) {
				EvasionGame.turn = true;

			} else {
				EvasionGame.turn = false;

			}
			cpBig.setVisible(false);
			restart.setVisible(true);

			//JSObject message = JSObject.getWindow(main);
			//message.call("showMessage", null);
		}
	}

	class MyDocumentListener implements DocumentListener {
		int player;

		public MyDocumentListener(int player) {
			this.player = player;
		}

		public void insertUpdate(DocumentEvent e) {
			search();
		}

		public void removeUpdate(DocumentEvent e) {
			search();
		}

		public void changedUpdate(DocumentEvent e) {
			search();
		}

		public void search() {
			if (player == 1) {

				EvasionGame.p1Name = entry1.getText();
				p1H.setText("Start with " + EvasionGame.p1Name + " as Hunter");

			} else if (player == 2) {
				EvasionGame.p2Name = entry2.getText();
				p2H.setText("Start with " + EvasionGame.p2Name + " as Hunter");

			}
			if (EvasionGame.p1Score > EvasionGame.p2Score) {
				EvasionGame.winner = EvasionGame.p1Name;

			} else if (EvasionGame.p1Score < EvasionGame.p2Score) {
				EvasionGame.winner = EvasionGame.p2Name;
			} else {
				EvasionGame.winner = "not set";
			}
		}
	}

	public static BufferedApplet main;
	public static KeyEventDispatcher mainKeyEventDispatcher;
	public static EvasionGame eva;
	public static JTextArea p1Score, p2Score;
	public static JButton restart, changeP1Name, changeP2Name, p1H, p1P, p2H,
			p2P;
	public static Panel cpBig, upperPanel;
	public static JPanel cp;
	public static Border paneEdge;
	public static boolean showButton;

	private static JTextField entry1, entry2;

	public void restart() {
		eva.restart();
		restart.setVisible(false);
		cpBig.setVisible(true);
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.removeKeyEventDispatcher(mainKeyEventDispatcher);
	}

	public void init() {

		showButton = false;
		main = this;
		mainKeyEventDispatcher = this;
		setFocusable(true);

		eva = new EvasionGame();
		EvasionGame.started = true;

		setLayout(new BorderLayout());
		upperPanel = new Panel();

		upperPanel.setBackground(Color.white);
		this.setBackground(Color.white);
		upperPanel.setLayout(new GridLayout(5, 1));

		p1Score = new JTextArea("");
		p2Score = new JTextArea("");

		restart = new JButton("Restart");
		restart.addActionListener(new RestartAction());
		restart.setVisible(false);

		entry1 = new JTextField(11);
		entry1.setText("Player1");
		entry2 = new JTextField(11);
		entry2.setText("Player2");
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				setFocusable(true);
				KeyboardFocusManager.getCurrentKeyboardFocusManager()
						.removeKeyEventDispatcher(mainKeyEventDispatcher);
				KeyboardFocusManager.getCurrentKeyboardFocusManager()
						.addKeyEventDispatcher(mainKeyEventDispatcher);

			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}

			public void mouseEntered(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {

			}

		});
		entry1.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				KeyboardFocusManager.getCurrentKeyboardFocusManager()
						.removeKeyEventDispatcher(mainKeyEventDispatcher);

			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}

			public void mouseEntered(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {

			}

		});

		entry2.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				KeyboardFocusManager.getCurrentKeyboardFocusManager()
						.removeKeyEventDispatcher(mainKeyEventDispatcher);

			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}

			public void mouseEntered(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {

			}

		});

		entry1.getDocument().addDocumentListener(new MyDocumentListener(1));
		entry2.getDocument().addDocumentListener(new MyDocumentListener(2));

		upperPanel.add(entry1);
		upperPanel.add(p1Score);
		upperPanel.add(entry2);

		upperPanel.add(p2Score);

		upperPanel.add(restart);

		cpBig = new Panel();
		cpBig.setBackground(Color.white);
		cpBig.setLayout(new GridLayout(2, 1));

		cp = new JPanel();

		cp.setBackground(Color.white);
		cp.setLayout(new GridLayout(3, 1));
		paneEdge = BorderFactory.createEmptyBorder(5, 15, 5, 15);

		cp.setBorder(paneEdge);

		cp.add(new Label("Choose any of the following modes."));
		Panel leftTwo = new Panel();
		leftTwo.setLayout(new GridLayout(1, 2));

		p1H = new JButton("Start with Player1 as Hunter");
		p1H.addActionListener(new RoleAction(1));

		leftTwo.add(p1H);
		cp.add(p1H);

		Panel rightTwo = new Panel();
		rightTwo.setLayout(new GridLayout(1, 2));

		p2H = new JButton("Start with Player2 as Hunter");
		p2H.addActionListener(new RoleAction(0));

		rightTwo.add(p2H);
		cp.add(p2H);

		cpBig.add(cp);

		JTextArea textArea = new JTextArea(20, 20);
		textArea.setBorder(paneEdge);

		textArea.setText("Instructions:\n"
				+ "The point of this game is for the hunter (red) to catch the prey (blue) as quickly as it can. The hunter is able to move twice as fast as the prey,but it changes direction only by bouncing off a wall. The prey, on the other hand, can move in whichever direction it wants provided it doesn't pass through a wall. The hunter catches the prey by building horizontal and vertical walls at its current position to get close to the prey and trap it.\n\n"
				+ "Controls:\n"
				+ "Hunter:\n"
				+ "Each wall you build is assigned a number. You can remove a wall with the corresponding number key. For example, you can remove wall no.1 by pressing 1 on the keyboard.\n"
				+ "Build a vertical wall: v\n" + "Build a horizontal wall: h\n"
				+ "Remove Wall: Number Key\n\n" + "Prey:\n"
				+ "Use the arrow keys to move; survive for as long as you can.");
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		textArea.setEditable(false);

		cpBig.add(textArea);

		add(cpBig, BorderLayout.CENTER);
		add(upperPanel, BorderLayout.EAST);

	}

	class Event {
	}

	Event event = new Event();

	public boolean keyDown(Event e, int key) {
		return false;
	}

	public boolean keyUp(Event e, int key) {
		return false;
	}

	public boolean mouseDown(Event e, int x, int y) {
		return false;
	}

	public boolean mouseDrag(Event e, int x, int y) {
		return false;
	}

	public boolean mouseUp(Event e, int x, int y) {
		return false;
	}

	public boolean mouseMove(Event e, int x, int y) {
		return false;
	}

	// public Rectangle bounds() { return r; }

	public boolean[] keyDown = new boolean[256], wasKeyDown = new boolean[256];
	public int mouseX, wasMouseX;
	public int mouseY, wasMouseY;
	public boolean mouseDown, wasMouseDown;

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			setKey(e.getKeyCode(), e.getKeyChar(), true);
			keyDown(event, e.getKeyChar());
		} else if (e.getID() == KeyEvent.KEY_RELEASED) {
			setKey(e.getKeyCode(), e.getKeyChar(), false);
			keyDown(event, e.getKeyChar());
		} else if (e.getID() == KeyEvent.KEY_TYPED) {
		}
		return true;
	}

	// public void keyPressed(KeyEvent e) { setKey(e.getKeyCode(),
	// e.getKeyChar(), true); keyDown(event, e.getKeyChar()); }
	// public void keyReleased(KeyEvent e) { setKey(e.getKeyCode(),
	// e.getKeyChar(), false); keyUp(event, e.getKeyChar()); }
	// public void keyTyped(KeyEvent e) { }

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		setMouse(e.getX(), e.getY(), true);
		mouseDown(event, e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e) {
		setMouse(e.getX(), e.getY(), false);
		mouseUp(event, e.getX(), e.getY());
	}

	public void mouseDragged(MouseEvent e) {
		setMouse(e.getX(), e.getY(), true);
		mouseDrag(event, e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		setMouse(e.getX(), e.getY(), false);
		mouseMove(event, e.getX(), e.getY());
	}

	void setKey(int key, int ch, boolean down) {
		if (ch > 255) {
			ch = key;
		}
		keyDown[ch] = down;

		damage = true;
	}

	void setMouse(int x, int y, boolean down) {
		mouseX = x;
		mouseY = y;
		mouseDown = down;

		damage = true;
	}

	public boolean damage = true; // Flag advising app. program to rerender
	public boolean animating = false;

	public abstract void render(Graphics g); // App. defines render method

	Image bufferImage = null; // Image for the double buffer
	private Graphics bufferGraphics = null; // Canvas for double buffer
	private Thread t; // Background thread for rendering
	private Rectangle r = new Rectangle(); // Dimensions of buffer image

	// Extend the start,stop,run methods to implement double buffering.

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

	// public void stop() { if (t != null) { t.stop(); t = null; } }
	public void stop() {
		t = null;
	}

	public void run() {

		// addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		try {
			while (true) {
				repaint();
				Thread.sleep(30);
			} // Repaint each 30 msecs
		} catch (InterruptedException e) {
		}
		; // Catch interruptions of sleep().
	}

	// Update(Graphics) is called by repaint() - the system adds canvas.
	// Extend update method to create a double buffer whenever necessary.

	public void update(Graphics g) {
		if (r.width != getWidth() || r.height != getHeight()) {
			bufferImage = createImage(getWidth(), getHeight());
			bufferGraphics = bufferImage.getGraphics(); // Applet size change.
			r.width = getWidth();
			r.height = getHeight();
			damage = true; // Tell application.
		}
		if (damage) {
			render(bufferGraphics); // Ask application to render to buffer,

			for (int key = 0; key < 256; key++)
				wasKeyDown[key] = keyDown[key];
			wasMouseX = mouseX;
			wasMouseY = mouseY;
			wasMouseDown = mouseDown;
		}
		damage = animating;
		paint(g); // paste buffered image onto the applet.
	}

	// Separate paint method for application to extend if needed.

	public void paint(Graphics g) {
		if (bufferImage != null)
			g.drawImage(bufferImage, 0, 0, this); // Paste result of render().
	}
}
