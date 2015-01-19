package obstruction;

/**
 * Filter.java
 * 
 * @author Adam Winick
 * 
 */

public class Filter {

	/**
	 * GaussianBlur applies a Gaussian function to the specified IntegerImage.
	 * @param image Image to be blurred.
	 * @param sigma Standard deviation of the image.
	 * @return
	 */
	public static IntegerImage gaussianBlur(IntegerImage image, double sigma) {
		int width = image.getWidth();
		int height = image.getHeight();

		IntegerImage tempImage = new IntegerImage(width, height);
		IntegerImage filteredImage = new IntegerImage(width, height);

		int n = (int) (6 * sigma + 1);

		double[] window = new double[n];
		double s2 = 2 * sigma * sigma;

		// Create the Gaussian kernel
		window[(n - 1) / 2] = 1;
		for (int i = 0; i < (n - 1) / 2; i++) {
			window[i] = Math.exp((double) (-i * i) / (double) s2);
			window[n - i - 1] = window[i];
		}

		// Apply filter along the x-axis
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				double sum = 0;
				double color = 0;
				for (int k = 0; k < window.length; k++) {
					int l = i + k - (n - 1) / 2;
					if (image.isValidPixel(l, j)) {
						color += image.getPixel(l, j) * window[k];
						sum += window[k];
					}
				}
				tempImage.setPixel(i, j, (int) (color/sum));
			}
		}

		// Apply filter along the y-axis
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				double sum = 0;
				double color = 0;
				for (int k = 0; k < window.length; k++) {
					int l = j + k - (n - 1) / 2;
					if (image.isValidPixel(i, l)) {
						color += tempImage.getPixel(i, l) * window[k];
						sum += window[k];
					}
				}
				if (image.getPixel(i, j) >= 0) {
					filteredImage.setPixel(i, j, (int) (color / sum));
				}
			}
		}

		return filteredImage;
	}
	
	/**
	 * Equalizes the contrast in an IntegerImage via histogram equalization.
	 * @param image
	 * @return
	 */
	public static IntegerImage histogramEqualizer(IntegerImage image) {
		int L = 256;
		int width = image.getWidth();
		int height = image.getHeight();

		double[] frequency = new double[L];
		int total = 0;
		
		IntegerImage output = new IntegerImage(width, height);

		// Create the frequency histogram
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int p = image.getPixel(i, j);

				// Only add valid pixels
				if (p >= 0 && p <= L - 1) {
					frequency[p]++;
					total++;
				}
			}
		}

		// Equalize each pixel
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int p = image.getPixel(i, j);
				if (p >= 0 && p <= L - 1) {
					output.setPixel(i, j, (int) Math.floor((double) (L - 1) * frequency[p] / total));
				}
			}
		}

		return image;
	}
	
}
