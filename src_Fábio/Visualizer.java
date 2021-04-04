import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javax.swing.*;

public class Visualizer extends JPanel {

    private static final long serialVersionUID = -1L;
    double spacing = 20;
	double range;
	ArrayList<Point> points;

	public Visualizer(){
		setBackground(Color.WHITE);

	}
	public Visualizer(ArrayList<Point> points, int range) {
		this.points = points;
		this.range = (double)range;
		setBackground(Color.WHITE);
	}

	private void lines(Graphics2D g2, double x1, double y1, double x2, double y2) {

		g2.draw(new Line2D.Double(x1, y1, x2, y2));
	}

	@Override
	public void paint(Graphics g) {

		super.paint(g);


		final double width = getWidth();
		final double height = getHeight();

		final double use = Math.min(width,height);

		while(spacing * range > use/2){
			spacing *=0.95;
		}

		
		final double x2 = width;
		final double y2 = height;
		final double xaxis = width / 2.0;
		final double yaxis = height / 2.0;
		final double x1 = 0;
		final double y1 = 0;
		

		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.GRAY);
		g2.setStroke(new BasicStroke(1));

		for (double x = spacing; x < width; x += spacing) {

			lines(g2, xaxis + x, y1, xaxis + x, y2);
			lines(g2, xaxis - x, y1, xaxis - x, y2);
		}

		for (double y = spacing; y < height; y += spacing) {

			lines(g2, x1, yaxis + y, x2, yaxis + y);
			lines(g2, x1, yaxis - y, x2, yaxis - y);
		}

		g.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2));

		g2.draw(new Line2D.Double(x1, yaxis, x2, yaxis));
		g2.draw(new Line2D.Double(xaxis, y1, xaxis, y2));

		g.setColor(Color.GREEN);

		Point a = points.get(0);
		Point b = points.get(1);

		for(int i=1; i<points.size(); i++){
			b = points.get(i);
			
			double[] aCords = cords(a, xaxis, yaxis);
			double[] bCords = cords(b, xaxis, yaxis);

			lines(g2, aCords[0], aCords[1], bCords[0], bCords[1]);
			 a =b;

		}
		double[] bCords = cords(b, xaxis, yaxis);
		double[] aCords = cords(points.get(0), xaxis, yaxis);
		lines(g2, bCords[0],bCords[1], aCords[0],aCords[1]);

		g.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(4));

		for(int i=0; i<points.size();i++){
			Point p = points.get(i);
			double[] cords = cords(p, xaxis, yaxis);
			lines(g2,cords[0],cords[1], cords[0],cords[1]);
		}

	}

	private double[] cords(Point a, double xaxis, double yaxis){
		double x,y;
		if(a.x >=0 && a.y>=0){
			x = xaxis + (a.x * spacing);
			y = yaxis - (a.y * spacing);
		}
		else if(a.x >=0 && a.y<=0){
			x = xaxis + (a.x * spacing);
			y = yaxis - (a.y * spacing);
		}
		else if(a.x <=0 && a.y <=0){
			x = xaxis + (a.x * spacing);
			y = yaxis - (a.y * spacing);
		}
		else{
			x = xaxis + (a.x * spacing);
			y = yaxis - (a.y * spacing);

		}
		double[] tmp = {x,y};
		return tmp;
		
	}

	public static void initVisualizer(ArrayList<Point> points, int range) {

		JFrame window = new JFrame("Cartesian Plane");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width =(int) screenSize.getWidth();
		int height =(int)screenSize.getHeight();
		
		window.add(new Visualizer(points,range));
		
		window.setSize(width, height);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}