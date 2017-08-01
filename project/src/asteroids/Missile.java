package asteroids;

import java.awt.*;

/**
 * Created by User on 7/6/2016.
 */
public class Missile
{
	public Missile(int screen_width, int screen_height, double x, double y, double direction)
	{
		this.screen_width = screen_width;
		this.screen_height = screen_height;

		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	public double minX()
	{
		return x - radius;
	}
	public double maxX()
	{
		return x + radius;
	}
	public double minY()
	{
		return y - radius;
	}
	public double maxY()
	{
		return y + radius;
	}

	public void logicalUpdate()
	{
		double x_vector = speed * Math.cos(direction);
		x += x_vector;

		double y_vector = speed * Math.sin(direction);
		y += y_vector;
	}

	public boolean isValid()
	{
		if (maxX() < 0) {
			return false;
		} else if (minX() > screen_width) {
			return false;
		}

		if (maxY() < 0) {
			return false;
		} else if (minY() > screen_height) {
			return false;
		}

		return true;
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawOval((int) (x - radius), (int) (y - radius), (int) (radius * 2), (int) (radius * 2));
	}

	final double radius = 1.5;
	final int speed = 5;

	int screen_width, screen_height;
	double x, y, direction;
}
