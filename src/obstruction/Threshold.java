package obstruction;

/**
 * Threshold.java
 * 
 * @author Adam Winick
 * 
 */

public class Threshold {

	/**
	 * Applies the Sauvola threshold algorithm to an integer image
	 * 
	 * @param image Input image
	 * @param w Window size
	 * @param k Parameter in range [0.0001, 0.1]
	 * @return Thresholded image
	 */
	public static IntegerImage SauvolaThreshold(IntegerImage image, int w, double k) {
		int width = image.getWidth();
		int height = image.getHeight();

		IntegerImage result = new IntegerImage(width, height);
		IntegralImage integral = new IntegralImage(image);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixel = image.getPixel(i, j);
				double m = integral.getMean(i, j, w);
				double s = integral.getStandardDeviation(i, j, w);

				result.setPixel(i, j, (pixel < m * (1 + k * (s / 128 - 1)) && pixel >= 0) ? 0 : 255);
			}
		}

		return result;
	}

}
