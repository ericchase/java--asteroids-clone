package asteroids;

import java.awt.*;

/**
 * Created by User on 7/6/2016.
 */
public class Player
{
	public Player(int screen_width, int screen_height)
	{
		this.screen_width = screen_width;
		this.screen_height = screen_height;

		this.width = 10;
		this.height = 10;

		this.x = screen_width / 2 - this.width;
		this.y = screen_height / 2 - this.height;
	}

	public double minX()
	{
		return x - width / 2;
	}
	public double maxX()
	{
		return x + width / 2;
	}
	public double minY()
	{
		return y - height / 2;
	}
	public double maxY()
	{
		return y + height / 2;
	}

	public void rotate(int x, int y)
	{
		this.direction = Math.atan2(y - this.y, x - this.x);
	}

	public void logicalUpdate()
	{
		if (propel) {
			double x_vector = speed * Math.cos(direction);
			x += x_vector;
			double y_vector = speed * Math.sin(direction);
			y += y_vector;
		}
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawOval((int) (x - width / 2), (int) (y - height / 2), width, height);
		//g.drawRect((int) minX(), (int) minY(), (int) (maxX() - minX()), (int) (maxY() - minY()));
	}

	int screen_width, screen_height, width, height;
	double x, y, direction, speed = 1;
	boolean propel = false;
}

