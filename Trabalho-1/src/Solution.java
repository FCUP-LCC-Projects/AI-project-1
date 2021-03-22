import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//melhor não meter os algoritmos aqui, embora seja chamado de Solution, que isto já tá bue longo
public class Solution {
	/**
	 * Contém todas as estruturas necessárias para o cálculo do candidato à solução.
	 * 
	 * sol - array com o candidato à solução.
	 * edges - array com os ramos correspondentes ao candidato à solução.
	 * solSize - número atual de pontos da solução.
	 * edgeSize - número atual de ramos da solução.
	 * solMaxSize - número total de pontos da solução.
	 * edgeMaxSize - número total de ramos da solução.
	 */
	int[] sol;
	Edge[] edges;
	Memory mem;
	int solSize, edgeSize, solMaxSize, edgeMaxSize;

	Solution(Memory mem, Edge[] edges){
		solMaxSize = mem.memSize;
		edgeMaxSize = mem.memSize;
		solSize =0;
		edgeSize =0;
		sol = new int[solMaxSize];
		Arrays.fill(sol,-1);
		this.edges = new Edge[solMaxSize];

		for(int i=0; i<edges.length; i++){
			this.edges[i] = edges[i];
		}
		this.mem = mem;	
	}
	
	Solution(int n, Memory mem){
		solMaxSize = n;
		edgeMaxSize = n;
		solSize = 0;
		edgeSize = 0;
		sol = new int[solMaxSize];
		Arrays.fill(sol,-1);
		edges = new Edge[edgeMaxSize]; //n tenho a certeza se é mesmo este o numero maximo de edges dado n pontos
		this.mem = mem;
	}
	
	//estas funções abaixo são só para facilitar o código, para dar para usá-las como se os arrays fossem listas
	
	public void solAdd(int index) {
		sol[solSize++] = index;
	}
	
	public int solGetFirst() {
		return sol[0]; 
	}
	
	public int solGetLast() {
		return sol[solSize-1];
	}
	
	public boolean solContains(int index) {
		for(int cur : sol) {
			if(cur == index) return true;
		}
		return false;
	}
	
	public boolean edgeContains(Edge e) {
		for(Edge cur : edges) {
			if(cur.equals(e)) return true;
		}
		return false;
	}
	
	public void edgeAdd(Edge edge) {
		edges[edgeSize++] = edge;
	}

	public int getPerimiter(){
		int size =0; 
		for(int i=0; i<solMaxSize; i++){
			size += edges[i].distance;
		}
		return size;
	}
	
	public int nearestNeighbour(Point start, Memory mem) {
		/**
		 * Heurística Nearest Neighbour que, dado o quadrado da distância euclideana de dois pontos,
		 * iterando por todos os pontos, retorna aquele que está mais próximo.
		 */
		int nearest = -1, minDistanceSoFar = Integer.MAX_VALUE;
		
		for(Point cur : mem.points)
			if(!solContains(cur.index)) {
				int distance = euclidean(mem, start.index, cur.index);
				if(distance < minDistanceSoFar) {
					nearest = cur.index;
					minDistanceSoFar = distance;
				}
			}
		
		return nearest;
	}

	public int euclidean(Memory mem, int i, int j) {
		/**
		 * Distância euclideana
		 */
		return (mem.points[j].x - mem.points[i].x)*(mem.points[j].x - mem.points[i].x) +
				(mem.points[j].y - mem.points[i].y)*(mem.points[j].y - mem.points[i].y);
	}
	
	public void greedy(Memory mem) {
		/**
		 * Procura greedy, segundo a heurística Nearest Neighbour, de um candidato à solução,
		 * dado um ponto aleatório inicial.
		 */
		Random rand = new Random();
		int start = rand.nextInt(solMaxSize);
				
		solAdd(start);
		
		while(solSize <solMaxSize){
			int next = nearestNeighbour(mem.points[start], mem);
			solAdd(next);
			edgeAdd(new Edge(start, next, euclidean(mem, start, next)));
			start = next;
		}
		
		int lastEdgeDistance = euclidean(mem, solGetLast(), solGetFirst()); //fica a faltar o último ramo, que é entre o último ponto e o primeiro
		edgeAdd(new Edge(solGetLast(), solGetFirst(), lastEdgeDistance));
	}
	
	// Durstenfeld shuffle
	public void shuffle() {
		/**
		 * "Baralha" o array para uma permutação aleatória, o candidato à solução
		 */
		for(int i=solMaxSize - 1; i>=0; i--) {
			int j = (int) Math.floor(Math.random()*(i+1));
			int tmp = sol[i];
			sol[i] = sol[j];
			sol[j] = tmp;
		}
	}
	
	public void permutation(Memory mem) {
		/**
		 * Calcula uma permutação e adiciona os ramos correspondentes.
		 */
		for(int i=0; i<solMaxSize; i++)
			solAdd(i);
		shuffle();
		
		for(int i=0; i<solMaxSize-1; i++) {
			edgeAdd(new Edge(sol[i], sol[i+1], 
					euclidean(mem, sol[i], sol[i+1])));
		}

		int lastEdgeDistance = euclidean(mem, solGetLast(), solGetFirst()); //fica a faltar o último ramo, que é entre o último ponto e o primeiro
		edgeAdd(new Edge(solGetLast(), solGetFirst(), lastEdgeDistance));
	}

	// public static Point subPoint(Point a, Point b) {
	// 	/**
	// 	 * Subtrai as coordenadas de dois pontos
	// 	 */
	// 	return new Point(a.x - b.x, a.y - b.y, -1);
	// }
	
	// public static int crossProduct(Point a, Point b) {
	// 	/**
	// 	 * Produto vetorial de dois pontos
	// 	 */
	// 	return (a.x * b.y) - (a.y * b.x); 
	// }
	
	// public static int halfStraddle(Point a, Point b, Point c) {
	// 	/**
	// 	 * Metade da operação de Straddle de dois pontos
	// 	 * (saber se um ponto interseta outro)
	// 	 */
	// 	return crossProduct(subPoint(c,a),subPoint(b,a));
	// }
	
	// public static int straddleTest(Point a, Point b, Point c, Point d) {
	// 	/**
	// 	 * Retorna a posição relativa de um ponto a outro
	// 	 * (saber se um ponto interseta outro)
	// 	 */
	// 	return halfStraddle(c,d,a)*halfStraddle(c,d,b);
	// }
	
	// public static boolean inBox(Point a, Point b, Point c) {
	// 	/**
	// 	 * Retorna se um ponto coincide noutro (?)
	// 	 */
	// 	return Math.min(a.x,b.x) <= c.x && c.x >= Math.max(a.x,b.x)
	// 			&& Math.min(a.y, b.y) <= c.y && c.y >= Math.max(a.y,b.y);
	// }
	
	// public static boolean edgeIntersect(Memory mem, Edge i, Edge j) {
	// 	/**
	// 	 * Retorna se duas arestas se intersetam
	// 	 */
	// 	if(straddleTest(mem.points[i.origin], mem.points[i.dest], mem.points[j.origin], mem.points[j.dest])< 0
	// 			&& straddleTest(mem.points[j.origin], mem.points[j.dest], mem.points[i.origin], mem.points[i.dest])< 0)
	// 		return true;
	// 	else if(halfStraddle(mem.points[i.origin],mem.points[i.dest],mem.points[j.origin]) == 0)
	// 		if(inBox(mem.points[i.origin], mem.points[i.dest], mem.points[j.origin]))
	// 			return true;
	// 	else if(halfStraddle(mem.points[i.origin],mem.points[i.dest],mem.points[j.dest]) == 0)
	// 		if(inBox(mem.points[i.origin], mem.points[i.dest], mem.points[j.dest]))
	// 			return true;
	// 	else if(halfStraddle(mem.points[j.origin],mem.points[j.dest],mem.points[i.origin]) == 0)
	// 		if(inBox(mem.points[j.origin], mem.points[j.dest], mem.points[i.origin]))
	// 			return true;
	// 	else if(halfStraddle(mem.points[j.origin],mem.points[j.dest],mem.points[i.dest]) == 0)
	// 		if(inBox(mem.points[j.origin], mem.points[j.dest], mem.points[i.dest]))
	// 			return true;
	// 	return false;
			
	// }

	private int orientation(Point edgeStart, Point edgeEnd, Point p){
		
		int value = ((edgeEnd.y - edgeStart.y) * (p.x - edgeEnd.x)) - ((edgeEnd.x- edgeStart.x) * (p.y - edgeEnd.y));
		if(value ==0){
			return 0;
		}
		if(value >0){
			return 1;
		}
		else{
			return 2;
		}
	}
	private boolean onSegment(Point edgeStart, Point edgeEnd, Point p){
        if (edgeEnd.x <= Math.max(edgeStart.x, p.x) && edgeEnd.x >= Math.min(edgeStart.x, p.x) && 
        
        edgeEnd.y <= Math.max(edgeStart.y, p.y) && edgeEnd.y >= Math.min(edgeStart.y, p.y)){
            return true; 
        } 
  
    return false;
    }

	public boolean edgeIntersect(Edge a, Edge b){
		int o1, o2,o3,o4;
		Point start1, end1, start2, end2;
		start1 = mem.points[a.origin];
		end1 = mem.points[a.dest];
		start2 = mem.points[b.origin];
		end2 = mem.points[b.dest];
		
		o1 = orientation(start1, end1, start2);
		o2 = orientation(start1, end1, end2);
		o3 = orientation(start2, end2, start1);
		o4 = orientation(start2, end2, end1);
		if(o1 != o2 && o3 != o4){
            return true;
        }

        if(o1==0  && onSegment(start1, start2, end1)) return true;
        if(o2==0  && onSegment(start1, end2, end2)) return true;
        if(o3==0  && onSegment(start2, start1, end2)) return true;
        if(o4==0  && onSegment(start2, end1, end2)) return true;

        return false;

	}
	
	// public void twoExchange(Edge a, Edge b){
	// 	//int indexA, indexB;
	// 	for(int i=0; i<edgeSize; i++){
	// 		if(edges[i] == a){
	// 			edges[i] = new Edge(a.origin, b.origin, euclidean(mem, a.origin, b.origin));
	// 			//indexA =i;
	// 		}
	// 		if(edges[i] == b){
	// 			edges[i] = new Edge(a.dest, b.dest, euclidean(mem, a.dest, b.dest));
	// 			edges[i-1] = new Edge(edges[i-1].dest, edges[i-1].origin, edges[i-1].distance);
	// 			//indexB = i;
	// 		}
	// 	}
	// }

	public void sortPath(){
		for(int i=0; i<solMaxSize-1; i++){
			Edge a,b;
			a = edges[i];
			b = edges[i+1];
			if(a.dest != b.origin){
				for(int j=0; j<solMaxSize; j++){
					Edge c = edges[j];
					if(c.origin == a.dest){
						edges[i+1] = c;
						edges[j] = b;
					}
				}

			}
		}
	}

	public void twoExchange(Edge a, Edge b){
		int index =0;
		while(edges[index] !=a){
			index++;
		}
		edges[index] = new Edge(a.origin, b.origin, euclidean(mem, a.origin, b.origin));
		index++;

		while(edges[index] != b){
			edges[index] = new Edge(edges[index].dest, edges[index].origin, edges[index].distance);
			index++;
		}

		edges[index] = new Edge(a.dest, b.dest, euclidean(mem, a.dest, b.dest));

		sortPath();

	}

	public boolean conflictAlreadyFound(ArrayList<Edge> conflicts, Edge a, Edge b){

		for(int i=0; i<conflicts.size()-1; i+=2){
			if(conflicts.get(i) == a && conflicts.get(i+1) == b){
				return true;
			}
			if(conflicts.get(i) == b && conflicts.get(i+1) == a){
				return true;
			}
		}
		return false;
	}

	public ArrayList<Edge> getAllConflicts(){
		
		ArrayList<Edge> conflicts = new ArrayList<Edge>();
		for(int i=0; i<solMaxSize; i++){
			Edge a = edges[i];
			for(int j=0; j<solMaxSize; j++){
				Edge b = edges[j];
				if(a != b && a.dest != b.origin && a.origin != b.dest){
					if(edgeIntersect(a, b) && !conflictAlreadyFound(conflicts, a, b)){
						conflicts.add(a);
						conflicts.add(b);
					}
				}
			}
		}
		return conflicts;
	 }
}
