package obstruction;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * IntegerImage.java
 * 
 * IntegerImage is a class which enables the storing of an image in a convenient
 * and computationally inexpensive data structure. The class is created so that
 * a large number of pixel operations may be performed in as little time as
 * possible.
 * 
 * @author Adam Winick
 * 
 */

public class IntegerImage {

	private int[][] image;
	private int width, height;

	/**
	 * Constructs an empty IntegerImage.
	 * 
	 * @param width Width of the IntegerImage
	 * @param height Height of the IntegerImage
	 */
	public IntegerImage(int width, int height) {
		this.width = width;
		this.height = height;

		image = new int[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				image[i][j] = -1;
			}
		}
	}

	/**
	 * Constructs a new IntegerImage given a BufferedImage input.
	 * 
	 * @param in Input image
	 */
	public IntegerImage(BufferedImage in) {
		width = in.getWidth();
		height = in.getHeight();

		image = new int[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color c = new Color(in.getRGB(x, y), true);

				// If the pixel is transparent store it as -1.
				if (c.getAlpha() == 0) {
					image[x][y] = -1;
					continue;
				}

				// Visible pixels are converted to grayscale.
				image[x][y] = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
			}
		}
	}

	public IntegerImage clone() {
		IntegerImage clone = new IntegerImage(width, height);

		for (int i = 0; i < width; i++) {
			clone.image[i] = image[i].clone();
		}

		return clone;
	}

	/**
	 * Converts the IntegerImage to a BufferedImage.
	 * 
	 * @return BufferedImage
	 */
	public BufferedImage getImage() {
		BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// Only write valid pixels to the output image.
				if (image[x][y] >= 0 && image[x][y] <= 255) {
					output.setRGB(x, y, new Color(image[x][y], image[x][y], image[x][y]).getRGB());
				}
			}
		}

		return output;
	}

	/**
	 * @return A copy of the integer array representing the image.
	 */
	public int[][] getIntArray() {
		int[][] copy = new int[width][];

		for (int i = 0; i < width; i++) {
			copy[i] = image[i].clone();
		}

		return image;
	}

	/**
	 * @return The width of the IntegerImage.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return The height of the IntegerImage.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Determines whether a pixel is within the image raster.
	 * 
	 * @param x X-coordinate of the pixel.
	 * @param y Y-coordinate of the pixel.
	 * @return Does the pixel at (x, y) exist?
	 */
	public boolean isValidPixel(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	/**
	 * Gets a specified pixel from the IntegerImage.
	 * 
	 * @param x X-coordinate of the pixel.
	 * @param y Y-coordinate of the pixel.
	 * @return The pixel at coordinate (x, y).
	 */
	public int getPixel(int x, int y) {
		if (isValidPixel(x, y)) {
			return image[x][y];
		}
		return 0;
	}

	/**
	 * Sets a pixel to a specified value.
	 * 
	 * @param x X-coordinate of the pixel
	 * @param y Y-coordinate of the pixel.
	 * @param c Value to be assigned to the pixel.
	 */
	public void setPixel(int x, int y, int c) {
		if (isValidPixel(x, y)) {
			image[x][y] = c;
		}

	}

}
