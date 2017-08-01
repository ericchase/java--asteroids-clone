package asteroids;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by User on 7/6/2016.
 */
public class GUI extends JFrame
{
	public GUI()
	{
		showStartScreen();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}

	void switchScreen(Screen new_screen)
	{
		if (active_screen != null) {
			this.remove(active_screen);
		}

		if (new_screen != null) {
			active_screen = new_screen;
			this.add(active_screen);
			active_screen.requestFocus();
		}

		this.pack();
	}

	void showStartScreen()
	{
		Start new_screen = new Start();
		new_screen.Play_SetActionListener(e -> showPlayScreen());
		new_screen.Instructions_SetActionListener(e -> showInstructionsScreen());
		new_screen.HighScores_SetActionListener(e -> showHighScoresScreen());

		switchScreen(new_screen);
	}
	void showPlayScreen()
	{
		Play new_screen = new Play();
		new_screen.setCallback(this::showGameOverScreen);
		switchScreen(new_screen);
	}
	void showInstructionsScreen()
	{
		Instructions new_screen = new Instructions();
		new_screen.SetMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				showStartScreen();
			}
			@Override
			public void mousePressed(MouseEvent e)
			{

			}
			@Override
			public void mouseReleased(MouseEvent e)
			{

			}
			@Override
			public void mouseEntered(MouseEvent e)
			{

			}
			@Override
			public void mouseExited(MouseEvent e)
			{

			}
		});

		switchScreen(new_screen);
	}
	void showHighScoresScreen()
	{
		HighScores new_screen = new HighScores();

		switchScreen(new_screen);
	}
	void showGameOverScreen()
	{
		GameOver new_screen = new GameOver();

		switchScreen(new_screen);
	}

	Screen active_screen = null; // empty JPanel
}

//DONE
abstract class Screen extends JPanel
{
	final static int width = 300;
	final static int height = 250;

	public Screen()
	{
		setPreferredSize(new Dimension(width, height));
	}
}

//DONE
class Start extends Screen
{
	public Start()
	{
		this.setLayout(new GridBagLayout());
		content.setLayout(new GridLayout(3, 1));

		this.add(background);
		background.add(content);
		content.add(play);
		content.add(instructions);
		content.add(high_scores);
	}

	public void Play_SetActionListener(ActionListener new_al)
	{
		// remove all other listeners
		for (ActionListener al : play.getActionListeners()) {
			play.removeActionListener(al);
		}
		// add new action listener
		play.addActionListener(new_al);
	}
	public void Instructions_SetActionListener(ActionListener new_al)
	{
		// remove all other listeners
		for (ActionListener al : instructions.getActionListeners()) {
			instructions.removeActionListener(al);
		}
		// add new action listener
		instructions.addActionListener(new_al);
	}
	public void HighScores_SetActionListener(ActionListener new_al)
	{
		// remove all other listeners
		for (ActionListener al : high_scores.getActionListeners()) {
			high_scores.removeActionListener(al);
		}
		// add new action listener
		high_scores.addActionListener(new_al);
	}

	// panels
	JPanel background = new JPanel();
	JPanel content = new JPanel();

	// buttons
	JButton play = new JButton("New Game");
	JButton instructions = new JButton("Instructions");
	JButton high_scores = new JButton("High Scores");
}

//TODO
class Play extends Screen
{
	public Play()
	{
		// create a player
		// create array of asteroids
		for (int n = 0; n < 10; ++n) {
			asteroids.add(new Asteroid(width, height));
		}
		timer.start();

		// set up listeners
		this.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{

			}
			@Override
			public void mousePressed(MouseEvent e)
			{
				fireMissile();
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{

			}
			@Override
			public void mouseEntered(MouseEvent e)
			{

			}
			@Override
			public void mouseExited(MouseEvent e)
			{

			}
		});
		this.addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{

			}
			@Override
			public void mouseMoved(MouseEvent e)
			{
				player.rotate(e.getX(), e.getY());
			}
		});

		this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "space_down");
		this.getInputMap().put(KeyStroke.getKeyStroke("released SPACE"), "space_up");
		this.getActionMap().put("space_down", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				player.propel = true;
			}
		});
		this.getActionMap().put("space_up", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				player.propel = false;
			}
		});
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		// draw player
		player.paintComponent(g);
		// draw asteroids
		for (Asteroid a : asteroids) {
			a.paintComponent(g);
		}
		// draw missiles
		for (Missile m : missiles) {
			m.paintComponent(g);
		}
	}

	void logicalUpdate()
	{
		player.logicalUpdate();

		for (Asteroid a : asteroids) {
			a.logicalUpdate();
		}

		ArrayList<Missile> remove_missiles = new ArrayList<>(10);
		for (Missile m : missiles) {
			m.logicalUpdate();
			if (!m.isValid()) {
				remove_missiles.add(m);
			}
		}
		missiles.removeAll(remove_missiles);

		ArrayList<Asteroid> add_asteroids = new ArrayList<>(10);
		ArrayList<Asteroid> remove_asteroids = new ArrayList<>(40);
		for (Asteroid a : asteroids) {
			// check if player hit asteroid
			if (a.hitTest(player.minX(), player.maxX(), player.minY(), player.maxY())) {
				callback.call();
			}
			// check if missiles hit asteroid
			for (Missile m : missiles) {
				if (a.hitTest(m.minX(), m.maxX(), m.minY(), m.maxY())) {
					add_asteroids.addAll(a.explode());
					remove_asteroids.add(a);
					missiles.remove(m);
					break;
				}
			}
		}
		asteroids.removeAll(remove_asteroids);
		asteroids.addAll(add_asteroids);

		repaint();

		//System.out.println("asteroids: " + asteroids.size() + " missiles: " + missiles.size());
	}

	void fireMissile()
	{
		// create a new missile
		// x and y should place it at the nose of the player
		// direction should be parallel to the nose of the player
		missiles.add(new Missile(width, height, player.x, player.y, player.direction));
	}

	void setCallback(Callback new_callback)
	{
		callback = new_callback;
	}

	Player player = new Player(width, height);
	ArrayList<Missile> missiles = new ArrayList<>(10);
	ArrayList<Asteroid> asteroids = new ArrayList<>(10);
	Timer timer = new Timer(1000 / 60, ae -> logicalUpdate());
	Callback callback = () -> {
	};
}

//DONE
class Instructions extends Screen
{
	public Instructions()
	{
		String help_text = "Instructions:"
				+ "\n" + "Destroy all the asteroids."
				+ "\n" + "Press W key to move towards."
				+ "\n" + "Press left mouse button to fire."
				+ "\n" + "(click screen when done reading)";
		instructions.setText(help_text);
		instructions.setEditable(false);

		this.setLayout(new GridBagLayout());
		this.add(background);
		background.add(content);
		content.add(instructions);
	}

	public void SetMouseListener(MouseListener new_ml)
	{
		// remove all other listeners
		for (MouseListener ml : this.getMouseListeners()) {
			this.removeMouseListener(ml);
		}
		for (MouseListener ml : instructions.getMouseListeners()) {
			instructions.removeMouseListener(ml);
		}
		this.addMouseListener(new_ml);
		instructions.addMouseListener(new_ml);
	}

	// panels
	JPanel background = new JPanel();
	JPanel content = new JPanel();

	// textareas
	JTextArea instructions = new JTextArea();
}

//TODO
class HighScores extends Screen
{
}

//TODO
class GameOver extends Screen
{
	public GameOver()
	{
		String help_text = "Game Over";
		instructions.setText(help_text);
		instructions.setEditable(false);

		this.setLayout(new GridBagLayout());
		this.add(background);
		background.add(content);
		content.add(instructions);
	}

	// panels
	JPanel background = new JPanel();
	JPanel content = new JPanel();

	// textareas
	JTextArea instructions = new JTextArea();
}

interface Callback
{
	void call();
}
