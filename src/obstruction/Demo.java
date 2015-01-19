package obstruction;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Demo extends JPanel implements MouseListener {

	private static final long serialVersionUID = 42L;
	
	private BufferedImage input;
	private BufferedImage output;

	private int n;

	public Demo() {
		setPreferredSize(new Dimension(1000, 800));
		addMouseListener(this);

		n = 1;
		
		loadImage();
	}

	public void loadImage() {
		// Cycle the image index
		n++;
		if (n > 8) {
			n = 1;
		}
		
		try {
			input = ImageIO.read(getClass().getResource("/images/test" + n + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		IntegerImage image = new IntegerImage(input);
		
		// Clean up the image
		image = Filter.gaussianBlur(image, 1);
		image = Filter.histogramEqualizer(image);
		
		// Apply the Sauvola threshold
		IntegerImage temp = Threshold.SauvolaThreshold(image, 30, 0.04);

		// Filter out small blobs in the image
		Blob bc = new Blob();
		bc.setImage(temp);	
		bc.filterBlobs(60);
		temp = bc.getOutput();
		
		output = temp.getImage();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(input, 0, 0, null);
		g.drawImage(output, input.getWidth(), 0, null);
	}
	
	public void mouseClicked(MouseEvent e) {
		loadImage();
		repaint();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(new Demo());
		frame.setVisible(true);
		frame.pack();
	}

}
