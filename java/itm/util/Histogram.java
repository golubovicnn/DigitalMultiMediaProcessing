package itm.util;

/*******************************************************************************
 This file is part of the ITM course 2017
 (c) University of Vienna 2009-2017
 *******************************************************************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

/**
 * Utility class the implements some basic Histogram operations.
 */
public class Histogram {
	private final static int RED = 0;
	private final static int GREEN = 1;
	private final static int BLUE = 2;

	/**
	 * Histogram array: first value represents the amount of color channels the
	 * second one distinct pixel values
	 */
	private int[][] hist;

	private int colorcomponents;
	private int bins;

	/**
	 * Maximal amount of pixels with the same value. Used for plotting.
	 */
	int max;

	/**
	 * Constructor.
	 * 
	 * @param colorcomponents
	 *            the amount of channels
	 * @param bins
	 *            the distinct values
	 */
	public Histogram(int colorcomponents, int bins) {
		hist = new int[colorcomponents][bins];
		this.colorcomponents = colorcomponents;
		this.bins = bins;
		this.max = 0;
	}

	/**
	 * Must be called in order to fill the histogram array
	 * 
	 * @param input
	 *            a histogram array
	 */
	public void setHistogram(int[][] input) {
		this.hist = input;
		for (int i = 0; i < this.bins; i++) {
			for (int j = 0; j < this.colorcomponents; j++) {
				this.max = (this.max > this.hist[j][i]) ? this.max : this.hist[j][i];
			}
		}
	}

	/**
	 * used to draw the histogram and fill it with colors
	 * 
	 * @param width
	 *            of the histogram
	 * @param height
	 *            of the histogram
	 * @return BufferedImage
	 */
	public BufferedImage plotHistogram(int width, int height) {

		BufferedImage image = null;

		if (this.colorcomponents == 1) {
			/**
			 * grayscale channel
			 */
			image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
			Graphics2D graphics = image.createGraphics();
			Polygon poly = new Polygon();
			graphics.setColor(Color.white);
			graphics.fillRect(0, 0, width, height);

			graphics.setColor(Color.black);
			float xInterval = (float) width / (float) bins;
			float yInterval = (float) height / (float) max;
			poly.addPoint(0, height);

			for (int i = 0; i < bins; i++) {
				int x = (int) ((float) i * xInterval);
				int y = (int) ((float) this.hist[0][i] * yInterval);

				poly.addPoint(x, height - y);
			}

			poly.addPoint(width, height);
			graphics.fill(poly);

		}
		if (this.colorcomponents >= 3) {
			/**
			 * extended RGB algorithm first channel:red second channel: green
			 * third channel: blue fourth channel: the alpha value is being
			 * ignored
			 */
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = image.createGraphics();

			Polygon[] poly = new Polygon[3];

			graphics.setColor(Color.white);
			graphics.fillRect(0, 0, width, height);

			/**
			 * only first three bands are used
			 */
			for (int i = 0; i < 3; i++) {
				poly[i] = new Polygon();
				if (i == RED) {
					graphics.setColor(Color.red);
				}

				else if (i == GREEN) {
					graphics.setColor(Color.green);
				}

				else if (i == BLUE) {
					graphics.setColor(Color.blue);
				}

				float xInterval = (float) width / (float) bins;
				float yInterval = (float) height / (float) max;
				poly[i].addPoint(0, height);

				for (int j = 0; j < bins; j++) {
					int x = (int) ((float) j * xInterval);
					int y = (int) ((float) this.hist[i][j] * yInterval);

					poly[i].addPoint(x, height - y);
				}

				poly[i].addPoint(width, height);
				graphics.fill(poly[i]);
			}

			Area red = new Area(poly[RED]);
			Area green = new Area(poly[GREEN]);
			Area blue = new Area(poly[BLUE]);

			red.intersect(green);
			green.intersect(blue);
			blue.intersect(new Area(poly[0]));

			graphics.setColor(new Color(255, 255, 0));
			graphics.fill(red);

			graphics.setColor(new Color(0, 255, 255));
			graphics.fill(green);

			graphics.setColor(new Color(255, 0, 255));
			graphics.fill(blue);

			graphics.setColor(Color.black);
			blue.intersect(new Area(poly[2]));
			graphics.fill(blue);
		}
		return image;
	}

	/**
	 * debug method
	 */
	private void printSamples() {
		System.out.println("Red Stack:");
		for (int i = 0; i <= 255; i++) {
			System.out.println(i + ": " + this.hist[0][i]);
		}
		System.out.println("Green Stack:");
		for (int i = 0; i <= 255; i++) {
			System.out.println(i + ": " + this.hist[1][i]);
		}
		System.out.println("Blue Stack:");
		for (int i = 0; i <= 255; i++) {
			System.out.println(i + ": " + this.hist[2][i]);
		}
	}
}
