package obstruction;

/**
 * IntegralImage.java
 * 
 * @author Adam Winick
 * 
 */

public class IntegralImage {

	private long[][] integral; // Integral image
	private long[][] squaredIntegral; // Squared integral image
	private long[][] sum; // Integral image of non-transparent pixels

	private int width, height;

	/**
	 * Creates an IntegeralImage given an IntegerImage as input
	 * 
	 * @param image Input image
	 */
	public IntegralImage(IntegerImage image) {
		int[][] input = image.getIntArray();

		width = input.length;
		height = input[0].length;

		// Squared input image
		int[][] squaredInput = new int[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				squaredInput[i][j] = input[i][j] * input[i][j];
			}
		}

		// Compute integral image
		integral = new long[width][height];
		squaredIntegral = new long[width][height];
		sum = new long[width][height];

		long c = 0;
		long c2 = 0;
		long c3 = 0;

		for (int i = 0; i < width; i++) {
			if (input[i][0] >= 0) {
				c += input[i][0];
				c2 += squaredInput[i][0];
				c3++;
			}

			integral[i][0] = c;
			squaredIntegral[i][0] = c2;
			sum[i][0] = c3;
		}

		for (int y = 1; y < height; y++) {
			c = 0;
			c2 = 0;
			c3 = 0;

			for (int x = 0; x < width; x++) {
				if (input[x][y] >= 0) {
					c += input[x][y];
					c2 += squaredInput[x][y];
					c3++;
				}

				integral[x][y] = c + integral[x][y - 1];
				squaredIntegral[x][y] = c2 + squaredIntegral[x][y - 1];
				sum[x][y] = c3 + sum[x][y - 1];
			}
		}

	}

	/**
	 * Is the x-coordinate within the array
	 */
	private int validX(int x, int w2) {
		return Math.min(width - w2 - 1, Math.max(w2, x));
	}

	/**
	 * Is the y-coordinate within the array
	 */
	private int validY(int y, int w2) {
		return Math.min(height - w2 - 1, Math.max(w2, y));
	}

	/**
	 * Computes the sum within a window
	 */
	private long getSum(int x, int y, int w2) {
		long total = sum[x + w2][y + w2];
		total += sum[x - w2][y - w2];
		total -= sum[x + w2][y - w2];
		total -= sum[x - w2][y + w2];

		return total;
	}

	/**
	 * Computes the mean within a window
	 */
	public double getMean(int x, int y, int w) {
		int w2 = w / 2;

		// Keep window inside image
		x = validX(x, w2);
		y = validY(y, w2);

		// Compute mean
		double total = integral[x + w2][y + w2];
		total += integral[x - w2][y - w2];
		total -= integral[x + w2][y - w2];
		total -= integral[x - w2][y + w2];

		return total / getSum(x, y, w2);
	}

	/**
	 * Computes the variance within a window
	 */
	public double getVariance(int x, int y, int w) {
		int w2 = w / 2;

		// Keep window inside image
		x = validX(x, w2);
		y = validY(y, w2);

		// Compute variance
		double total = squaredIntegral[x + w2][y + w2];
		total += squaredIntegral[x - w2][y - w2];
		total -= squaredIntegral[x + w2][y - w2];
		total -= squaredIntegral[x - w2][y + w2];

		return total / getSum(x, y, w2) - getMean(x, y, w) * getMean(x, y, w);
	}

	/**
	 * Computes the standard deviation within a window
	 */
	public double getStandardDeviation(int x, int y, int w) {
		return Math.sqrt(getVariance(x, y, w));
	}

}
