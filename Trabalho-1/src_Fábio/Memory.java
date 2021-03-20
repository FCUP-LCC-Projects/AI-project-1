public class Memory {
	/**
	 * Guarda o conjunto de pontos gerados, não sendo nunca alterado,
	 * e o número de pontos
	 */
	int memSize, nPoints;
	Point[] points;
	
	Memory(int m){
		memSize = m;
		nPoints = 0;
		points = new Point[m] ;
	}
	public void add(Point a){
		points[nPoints++] = a;
	}

	public void add(int x, int y) {
		points[nPoints++] = new Point(x, y);
	}
	
	public boolean contains(Point p) {
		for(Point cur : points) {
			if(cur.equals(p)) return true;
		}
		return false;
	}
}
