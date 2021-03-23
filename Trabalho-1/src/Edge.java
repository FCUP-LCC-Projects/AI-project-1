
public class Edge {
	int origin;
	int dest;
	int distance;
	
	Edge(int i, int j, int d){
		origin = i;
		dest = j;
		distance = d; //distância euclideana 
	}
	
	public boolean equals(Edge a) {
		return this.origin == a.origin && this.dest == a.dest? true: false;
	}
	
	@Override
	public String toString() {
		return "["+origin+","+dest+"]";
	}
}
