//Rizwan Kassamali

//importing packages
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A panel that can draw Koch Curves, Sierpinski Triangles and Sierpinski
 * Carpets, with a set of controls where the user can select the type of fractal
 * to be drawn and the level of recursion. There is also a set of checkboxes
 * that determine which of the 9 possible sections are included in a Sierpinski
 * Carpet.
 */
public class Fractals extends JPanel {
	public static void main(String[] args) {
		JFrame window = new JFrame("Fractal Fun 1");
		window.setContentPane(new Fractals());
		window.pack();
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation((screensize.width - window.getWidth()) / 2, (screensize.height - window.getHeight()) / 2);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	// A class representing the display area where the fractals are drawn.
	private class Display extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			int level = recursionLevel.getSelectedIndex() + 1;

			int w = getWidth();

			int h = getHeight();

			if (selectKoch.isSelected()) {
				g.setColor(Color.RED);
				drawKochCurve(g, level, 10, h / 2, w - 10, h / 2);
			} else if (selectTriangle.isSelected()) {
				g.setColor(Color.BLUE);
				drawSierpinskiTriangle(g, level, w / 2, 10, 20, h - 50, w - 20, h - 50);
			} else {
				g.setColor(new Color(0, 180, 0));
				boolean[][] showSection = new boolean[3][3];
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						showSection[i][j] = carpetDesignBox[i][j].isSelected();
					}
				}
				drawSierpinskiCarpet(g, level, showSection, 0, 0, w, h);
			}
		}
	}

	private JRadioButton selectKoch; // When selected, a Koch Curve is drawn.
	private JRadioButton selectTriangle; // When selected, a Sierpinski Triangle is drawn.
	private JRadioButton selectCarpet; // When selected, a Sierpinski Carpet is drawn.

	private JCheckBox[][] carpetDesignBox; // carpetDesignBox[i][j] tells whether to include
	// the section in row i, column j in the carpet,
	// for i and j from 0 to 3. These checkboxes are
	// enabled only if Sierpinski Carpets are selected.

	private Display display; // The display area.
	private JComboBox recursionLevel; // For selecting the level of recursion.

	/**
	 * Set up the user interface, and install a listener to repaint the display
	 * whenever one of the settings is changed. Also, the recursion level is set
	 * back to 1 when a new type of fractal is selected.
	 */
	public Fractals() {
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				boolean carpet = selectCarpet.isSelected();
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++)
						carpetDesignBox[i][j].setEnabled(carpet);
				}
				if (evt.getSource() instanceof JRadioButton)
					recursionLevel.setSelectedIndex(0);
				display.repaint();
			}
		};

		display = new Display();
		display.setBackground(Color.WHITE);
		display.setPreferredSize(new Dimension(600, 600));

		JPanel carpetDesign = new JPanel();
		carpetDesign.setBackground(Color.WHITE);
		carpetDesign.setLayout(new GridLayout(3, 3, 5, 5));
		carpetDesign.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		carpetDesignBox = new JCheckBox[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				carpetDesignBox[i][j] = new JCheckBox();
				carpetDesignBox[i][j].addActionListener(listener);
				carpetDesignBox[i][j].setEnabled(false);
				carpetDesignBox[i][j].setSelected(true);
				carpetDesign.add(carpetDesignBox[i][j]);
			}
		}
		carpetDesignBox[1][1].setSelected(false);

		JPanel fractalSelect = new JPanel();
		fractalSelect.setBackground(Color.WHITE);
		fractalSelect.setLayout(new GridLayout(3, 1, 5, 5));
		fractalSelect.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		ButtonGroup group = new ButtonGroup();
		selectKoch = new JRadioButton("Koch Curve");
		selectKoch.addActionListener(listener);
		group.add(selectKoch);
		fractalSelect.add(selectKoch);
		selectTriangle = new JRadioButton("Sierpinski Triangle");
		selectTriangle.addActionListener(listener);
		group.add(selectTriangle);
		fractalSelect.add(selectTriangle);
		selectCarpet = new JRadioButton("Sierpinski Carpet");
		selectCarpet.addActionListener(listener);
		group.add(selectCarpet);
		fractalSelect.add(selectCarpet);
		selectKoch.setSelected(true);

		recursionLevel = new JComboBox();
		recursionLevel.addItem("1 level of recursion");
		for (int i = 2; i <= 8; i++) {
			recursionLevel.addItem(i + " levels of recursion");
		}
		recursionLevel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		recursionLevel.addActionListener(listener);

		Box left = Box.createVerticalBox();
		left.setBackground(Color.WHITE);
		left.setOpaque(true);
		left.add(recursionLevel);
		left.add(Box.createVerticalStrut(15));
		left.add(fractalSelect);
		left.add(Box.createVerticalStrut(15));
		left.add(carpetDesign);
		left.add(Box.createVerticalGlue());

		setLayout(new BorderLayout(3, 3));
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
		add(display, BorderLayout.CENTER);
		add(left, BorderLayout.WEST);

	}

	void drawKochCurve(Graphics g, int level, double ax, double ay, double bx, double by) {
		if (level <= 1) {
			g.drawLine((int) ax, (int) ay, (int) bx, (int) by);
		} else {
			double sqrt3 = Math.sqrt(3);

			double cx = (2 * ax + bx) / 3; // C is 1/3 of the way from A to B
			double cy = (2 * ay + by) / 3;

			double ex = (ax + 2 * bx) / 3; // E is 1/3 of the way from B to A
			double ey = (ay + 2 * by) / 3;

			double hx = (ax + bx) / 2; // Half-way point from A to B (not used in drawing)
			double hy = (ay + by) / 2;

			double dx = hx + sqrt3 * (ey - hy); // D is the 3rd vertex of an equilateral triangle with C and E
			double dy = hy - sqrt3 * (ex - hx);

			drawKochCurve(g, level - 1, ax, ay, cx, cy); // Draw the four Koch curves
			drawKochCurve(g, level - 1, cx, cy, dx, dy);
			drawKochCurve(g, level - 1, dx, dy, ex, ey);
			drawKochCurve(g, level - 1, ex, ey, bx, by);
		}
	}

	public void drawSierpinskiTriangle(Graphics g, double level, double ax, double ay, double bx, double by, double cx,
			double cy) {
		int[] x = { (int) (ax), (int) (bx), (int) (cx) };
		int[] y = { (int) (ay), (int) (by), (int) (cy) };

		if (level == 1) {
			g.drawPolygon(x, y, 3);
		} else {
			double dx = (ax + bx) / 2;
			double ex = (bx + cx) / 2;
			double fx = (ax + cx) / 2;

			double dy = (ay + by) / 2;
			double ey = (by + cy) / 2;
			double fy = (ay + cy) / 2;

			drawSierpinskiTriangle(g, level - 1, ax, ay, dx, dy, fx, fy);
			drawSierpinskiTriangle(g, level - 1, bx, by, dx, dy, ex, ey);
			drawSierpinskiTriangle(g, level - 1, cx, cy, ex, ey, fx, fy);
		}
	}

	public void drawSierpinskiCarpet(Graphics g, double level, boolean[][] showSection, double x1, double y1, double x2,
			double y2) {
		// varibale for x and y shift
		double xS = ((x2 - x1) / 3);
		double yS = ((y2 - y1) / 3);

		// base case: if the level of recursion is one, then only one rectangle needs to
		// be drawn
		if (level == 1) {
			// draws the rectangle
			g.fillRect((int) (xS + x1), (int) (yS + y1), (int) (xS), (int) (yS));
		} else {
			// draws the rectangle
			g.fillRect((int) (xS + x1), (int) (yS + y1), (int) (xS), (int) (yS));

			// top left
			if (showSection[0][0] == true)
				drawSierpinskiCarpet(g, level - 1, showSection, x1, y1, x1 + xS, y1 + yS);

			// top center 
			if (showSection[0][1] == true)
				drawSierpinskiCarpet(g, level - 1, showSection, x1 + xS, y1, x1 + (2 * xS), y1 + yS);

			// top right
			if (showSection[0][2] == true)
				drawSierpinskiCarpet(g, level - 1, showSection, x1 + (2 * xS), y1, x1 + (3 * xS), y1 + yS);

			// mid left
			if (showSection[1][0] == true)
				drawSierpinskiCarpet(g, level - 1, showSection, x1, y1 + yS, x1 + xS, y1 + (2 * yS));

			// mid right
			if (showSection[1][2] == true)
				drawSierpinskiCarpet(g, level - 1, showSection, x1 + (2 * xS), y1 + yS, x1 + (3 * xS), y1 + (2 * yS));

			// bot center
			if (showSection[2][0] == true)
				drawSierpinskiCarpet(g, level - 1, showSection, x1, y1 + (2 * yS), x1 + xS, y1 + (3 * yS));

			// bot center
			if (showSection[2][1] == true)
				drawSierpinskiCarpet(g, level - 1, showSection, x1 + xS, y1 + (2 * yS), x1 + (2 * xS), y1 + (3 * yS));

			// bot right
			if (showSection[2][2] == true)
				drawSierpinskiCarpet(g, level - 1, showSection, x1 + (2 * xS), y1 + (2 * yS), x1 + (3 * xS),
						y1 + (3 * yS));
		}
	}
}
