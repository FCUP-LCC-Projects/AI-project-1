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
	int solSize, edgeSize, solMaxSize, edgeMaxSize;
	
	Solution(int n, Edge[] edges){
		solMaxSize = n;
		edgeMaxSize = n;
		solSize = 0;
		edgeSize = 0;
		sol = new int[solMaxSize];
		this.edges = new Edge[solMaxSize];

		for(int i=0; i<edges.length; i++){
			this.edges[i] = edges[i];
		}
	}
	
	Solution(int n){
		solMaxSize = n;
		edgeMaxSize = solMaxSize * solMaxSize;
		solSize = 0;
		edgeSize = 0;
		sol = new int[solMaxSize];
		edges = new Edge[edgeMaxSize]; //n tenho a certeza se é mesmo este o numero maximo de edges dado n pontos
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
	
	public int[] copySol() {
		int[] s = new int[solMaxSize];
		for(int i = 0; i<solSize; i++)
			s[i] = sol[i];
		return s;
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
	
	public int getEdge(Edge edge) {
		for(int i=0; i<edgeSize; i++)
			if(edges[i].equals(edge)) return i;
		return -1; //não existe
	}
	
	public int getPerimiter(){
		/**
		 * Calcula o perímetro do polígono
		 */
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
		
		while(solSize < solMaxSize) {
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
	
	public void edgeGenerate(Memory mem) {
		for(int i=0; i<solMaxSize-1; i++) {
			edgeAdd(new Edge(sol[i], sol[i+1], 
					euclidean(mem, sol[i], sol[i+1])));
		}
		
		int lastEdgeDistance = euclidean(mem, solGetLast(), solGetFirst()); //fica a faltar o último ramo, que é entre o último ponto e o primeiro
		edgeAdd(new Edge(solGetLast(), solGetFirst(), lastEdgeDistance));
	}
	
	public void permutation(Memory mem) {
		/**
		 * Calcula uma permutação e adiciona os ramos correspondentes.
		 */
		for(int i=0; i<solMaxSize; i++)
			solAdd(i);
		shuffle();
		
		edgeGenerate(mem);
	}
	
	public void printEdges(Memory mem) {
		for(Edge e : edges) {
			System.out.println("["+mem.points[e.origin]+","+mem.points[e.dest]+"]");
		}
	}
}
