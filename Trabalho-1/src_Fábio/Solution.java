import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Solution {
	
	Edge[] edges;
	Point[] points;
	int numPoints;
	int currEdge;
	
	
	Solution(Point[] points){
		numPoints = points.length;
		edges = new Edge[numPoints]; 
		this.points = points;
		currEdge =-1;
	}
	
	//estas funções abaixo são só para facilitar o código, para dar para usá-las como se os arrays fossem listas
	
	 public void AddEdge(Edge edge) {
	 	edges[++currEdge] = edge;
	 }
	 
	 public Edge getLast(){
		 return edges[currEdge];
	 }
	
	public boolean edgeContains(Edge e) {
		for(Edge cur : edges) {
			if(cur.equals(e)) return true;
		}
		return false;
	}
	
	
	public Point nearestNeighbour(Point start,ArrayList<Point> usedPoints) {
		/**
		 * Heurística Nearest Neighbour que, dado o quadrado da distância euclideana de dois pontos,
		 * iterando por todos os pontos, retorna aquele que está mais próximo.
		 */
		Point nearest = start; 
		int minDistanceSoFar = Integer.MAX_VALUE;
		
		for(Point cur : points)
			if(!usedPoints.contains(cur)){
				
				int distance = euclidean(start, cur);
				
				if(distance < minDistanceSoFar) {
					nearest = cur;
					minDistanceSoFar = distance;
				}
			}
		
		return nearest;
	}

	public int euclidean(Point a, Point b) {
		/**
		 * Distância euclideana
		 */
		return (a.x - b.x)*(a.x - b.x) + (a.y - b.y)* (a.y - b.y);
	}
	
	public void greedy() {
		/**
		 * Procura greedy, segundo a heurística Nearest Neighbour, de um candidato à solução,
		 * dado um ponto aleatório inicial.
		 */
		Random rand = new Random();
		ArrayList<Point> usedPoints = new ArrayList<Point>();
		
		int position= rand.nextInt(numPoints);

		Point start = points[position];
		usedPoints.add(start);
		Edge temp;

		for(int i=0; i<numPoints-1; i++){
			Point end;
			end = nearestNeighbour(start,usedPoints);
			temp = new Edge(start,end);
			AddEdge(temp);
			usedPoints.add(end);
			start = end;
		}
		//Connect end to the start
		temp = new Edge(start, edges[0].origin);
		AddEdge(temp);
		
	}
	
	public void random() {
		/**
		 * Cria um candidato a solução aleatório
		 */
		ArrayList<Point> positions = new ArrayList<Point>();
		for(int i=0;i<numPoints; i++){
			positions.add(points[i]);
		}

		Collections.shuffle(positions);
		Edge temp;
		Point start,end;

		for(int i=0; i<numPoints-1;i++){
			
			start = positions.get(i);
			end = positions.get(i+1);
			temp = new Edge(start,end);
			AddEdge(temp);
		}
		//Conect last point with the first
		start = getLast().dest;
		temp = new Edge(start, edges[0].origin);
		AddEdge(temp);
		}
	private int orientation(Edge a, Point p){
		Point start,end;
		start = a.origin;
		end = a.dest;
		int value = (end.y - start.y) * (p.x - end.x) - (end.x - start.x) * (p.y - end.y);
		if(value ==0){
			return 0;
		}
		if(value >0){
			return 1;
		}
		else{
			return 0;
		}
	}
	private boolean onSegment(Point p, Point q, Point r){
        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && 
        
        q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y)){
            return true; 
        } 
  
    return false;
    }

	public boolean doesEdgesIntersect(Edge a, Edge b){
		int o1, o2,o3,o4;
		o1 = orientation(a, b.origin);
		o2 = orientation(a, b.dest);
		o3 = orientation(b, a.origin);
		o4 = orientation(b, a.dest);
		if(o1 != o2 && o3 != o4){
            return true;
        }

        if(o1==0  && onSegment(a.origin, b.origin, a.dest)) return true;
        if(o2==0  && onSegment(a.origin, b.dest, a.dest)) return true;
        if(o3==0  && onSegment(b.origin, a.origin, b.dest)) return true;
        if(o4==0  && onSegment(b.origin, a.dest, b.dest)) return true;

        return false;

	}
	
	 public ArrayList<Edge> getAllIntersections(){
	 	ArrayList<Edge> allIntersections = new ArrayList<Edge>();




		 return allIntersections;

	 }

	//  public void permutation(Memory mem) {
	// 	/**
	// 	 * Calcula uma permutação e adiciona os ramos correspondentes.
	// 	 */
	// 	for(int i=0; i<solMaxSize; i++)
	// 		solAdd(i);
	// 	shuffle();
		
	// 	for(int i=0; i<solMaxSize-1; i++) {
	// 		edgeAdd(new Edge(sol[i], sol[i+1], 
	// 				euclidean(mem, sol[i], sol[i+1])));
	// 	}
	// }



	


}
