public class Point {
	int x;
	int y;
	int index; //Para algumas operaçõoes, dá jeito o ponto guardar o seu índice
	
	Point(int x, int y, int index){
		this.x = x;
		this.y = y;
		this.index = index;
	}
	
	public boolean equals(Point p) {
		return this.x == p.x && this.y == p.y ? true:false;
	}
	
	public boolean equals(int x, int y) {
		return this.x == x && this.y == y ? true:false;
	}
	
	public int comparePoint(Point p) {
		return this.x == p.x ? this.y - p.y : this.x - p.x;
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}
}
