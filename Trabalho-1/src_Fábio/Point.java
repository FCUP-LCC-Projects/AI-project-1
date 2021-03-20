public class Point {
	int x;
	int y;
	
	
	Point(int x, int y){
		this.x = x;
		this.y = y;
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
