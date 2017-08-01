package asteroids;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by User on 7/6/2016.
 */
public class Asteroid
{
	public Asteroid(int screen_width, int screen_height)
	{
		this.screen_width = screen_width;
		this.screen_height = screen_height;

		this.size = 3;

		if (random.nextInt() % 2 == 0) {
			this.x = (int) (random.nextDouble() * screen_width);
			if (random.nextInt() % 2 == 0) {
				this.y = 0;
			} else {
				this.y = screen_height;
			}
		} else {
			if (random.nextInt() % 2 == 0) {
				this.x = 0;
			} else {
				this.x = screen_width;
			}
			this.y = (int) (random.nextDouble() * screen_height);
		}
		this.direction = random.nextDouble() * this.max_angle;
		this.speed = random.nextDouble() * this.max_speed + 0.05;
	}

	Asteroid(int screen_width, int screen_height, int size, double x, double y)
	{
		this.screen_width = screen_width;
		this.screen_height = screen_height;

		this.size = size;

		this.x = x;
		this.y = y;
		this.direction = random.nextDouble() * this.max_angle;
		this.speed = random.nextDouble() * this.max_speed + 0.05;
	}

	public boolean hitTest(double minX, double maxX, double minY, double maxY)
	{
		if (this.maxX() < minX || this.minX() > maxX) {
			return false;
		}
		if (this.maxY() < minY || this.minY() > maxY) {
			return false;
		}

		return true;
	}

	public double minX()
	{
		return x - radius();
	}
	public double maxX()
	{
		return x + radius();
	}
	public double minY()
	{
		return y - radius();
	}
	public double maxY()
	{
		return y + radius();
	}

	public ArrayList<Asteroid> explode()
	{
		ArrayList<Asteroid> new_asteroids = new ArrayList<>(3);
		for (int n = 0; n < 3; ++n) {
			if (size > 1) {
				new_asteroids.add(new Asteroid(screen_width, screen_height, size - 1, x, y));
			}
		}
		return new_asteroids;
	}

	public void logicalUpdate()
	{
		double x_vector = speed * Math.cos(direction);
		x += x_vector;
		double y_vector = speed * Math.sin(direction);
		y += y_vector;

		if (maxX() < 0) {
			x += (screen_width + radius() * 2);
		} else if (minX() > screen_width) {
			x -= (screen_width + radius() * 2);
		}

		if (maxY() < 0) {
			y += (screen_height + radius() * 2);
		} else if (minY() > screen_height) {
			y -= (screen_height + radius() * 2);
		}
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawOval((int) (x - radius()), (int) (y - radius()), radius() * 2, radius() * 2);
		//g.drawRect((int) minX(), (int) minY(), (int) (maxX() - minX()), (int) (maxY() - minY()));
	}

	int radius()
	{
		return rfactor * size;
	}

	final int rfactor = 10;
	final double max_angle = 6.3;
	final double max_speed = 0.2;

	// size can range from 3 to 1
	// direction can range from 0 to 6.3
	// speed can range from 1 to 5

	Random random = new Random();
	int screen_width, screen_height, size;
	double x, y, direction, speed;
}
