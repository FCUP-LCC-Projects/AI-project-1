public class Point {
	int x;
	int y;
	int index; //Para algumas operaçõoes, dá jeito o ponto guardar o seu índice
	
	Point(int x, int y, int index){
		this.x = x;
		this.y = y;
		this.index = index;
	}
	
	public int equalPoint(Point p) {
		return this.x == p.x && this.y == p.y ? 1:0;
	}
	
	public int comparePoint(Point p) {
		return this.x == p.x ? this.y - p.y : this.x - p.x;
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}
}
