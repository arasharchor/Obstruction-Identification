package obstruction;

/**
 * Blob.java
 * 
 * @author Adam Winick
 * 
 */

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

public class Blob {

	private IntegerImage image, temp, output;

	public void setImage(IntegerImage image) {
		this.image = image;
		output = image.clone();
	}

	public IntegerImage getOutput() {
		return output;
	}

	public void filterBlobs(int min) {
		temp = image.clone();

		for (int i = 0; i < temp.getWidth(); i++) {
			for (int j = 0; j < temp.getHeight(); j++) {
				if (temp.getPixel(i, j) == 0 && getSize(i, j) < min) {
					removeBlob(i, j, output);
				}
			}
		}
	}

	public int countBlobs() {
		return countBlobs(-1, -1);
	}

	public int countBlobs(int min, int max) {
		temp = output.clone();
		int c = 0;

		for (int i = 0; i < temp.getWidth(); i++) {
			for (int j = 0; j < temp.getHeight(); j++) {
				if (temp.getPixel(i, j) == 0) {
					int size = getSize(i, j);
					if ((min == -1 || size >= min) && (max == -1 || size <= max)) {
						c++;
					}
					removeBlob(i, j, temp);
				}
			}
		}

		return c;
	}

	private int getSize(int x, int y) {
		Queue<Point> queue = new LinkedList<Point>();
		queue.add(new Point(x, y));
		int c = 0;

		while (!queue.isEmpty()) {
			Point p = queue.remove();
			if (temp.isValidPixel(p.x, p.y) && temp.getPixel(p.x, p.y) == 0) {
				c++;
				temp.setPixel(p.x, p.y, 255);
				queue.add(new Point(p.x + 1, p.y));
				queue.add(new Point(p.x - 1, p.y));
				queue.add(new Point(p.x, p.y + 1));
				queue.add(new Point(p.x, p.y - 1));
			}
		}
		return c;
	}

	private void removeBlob(int x, int y, IntegerImage image) {
		Queue<Point> queue = new LinkedList<Point>();
		queue.add(new Point(x, y));

		while (!queue.isEmpty()) {
			Point p = queue.remove();
			if (image.isValidPixel(p.x, p.y) && image.getPixel(p.x, p.y) == 0) {
				image.setPixel(p.x, p.y, 255);
				queue.add(new Point(p.x + 1, p.y));
				queue.add(new Point(p.x - 1, p.y));
				queue.add(new Point(p.x, p.y + 1));
				queue.add(new Point(p.x, p.y - 1));
			}
		}
	}

}