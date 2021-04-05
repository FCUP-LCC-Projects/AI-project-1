import java.util.ArrayList;
import java.util.Random;

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
	int totalConflicts;
	int validConflicts;
	
	ArrayList<Edge> conflicts = new ArrayList<Edge>();
	ArrayList<Solution> neighbours = new ArrayList<Solution>();
	Memory mem;
	
	Solution(int n, int[] sols, Edge[] edges,Memory m){
		solMaxSize = n;
		edgeMaxSize = solMaxSize;
		solSize = 0;
		edgeSize = 0;
		sol = new int[solMaxSize];
		for(int i=0; i<n;i++){
			sol[i] = sols[i];
		}
		this.edges = new Edge[solMaxSize];

		for(int i=0; i<edges.length; i++){
			this.edges[i] = edges[i];
		}
		 mem =m;
	}
	//Cópia profunda
	Solution (Solution s){
		this.solMaxSize = s.solMaxSize;
		this.edgeMaxSize = s.edgeMaxSize;
		this.sol = new int[this.solMaxSize];
		this.totalConflicts = s.totalConflicts;
		this.validConflicts = s.validConflicts;
		
		for(int i=0; i<s.solMaxSize;i++){
			this.sol[i] = s.sol[i];
		}
		this.edges = new Edge[this.solMaxSize];

		for(int i=0; i<s.edges.length; i++){
			int origin, des, dist;
			origin = s.edges[i].origin;
			des = s.edges[i].dest;
			dist = s.edges[i].distance;
			this.edges[i] = new Edge(origin,des,dist);
		}
		this.mem = s.mem;

	}
	
	Solution(int n,Memory m){
		solMaxSize = n;
		edgeMaxSize = solMaxSize;
		solSize = 0;
		edgeSize = 0;
		sol = new int[solMaxSize];
		edges = new Edge[edgeMaxSize]; 
		mem = m;
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
		for(int i=0; i<solSize; i++) {
			if(sol[i] == index) return true;
		}
		return false;
	}
	
	public int[] copySol(int limit) {
		int[] s = new int[solMaxSize];
		for(int i = 0; i<limit; i++)
			s[i] = sol[i];
		return s;
	}
	
	public Edge[] copyEdge() {
		Edge[] e = new Edge[edgeMaxSize];
		for(int i=0; i<edgeMaxSize; i++)
			e[i] = edges[i];
		return e;
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
	
	public long getPerimiter(){
		/**
		 * Calcula o perímetro do polígono
		 */
		long size =0; 
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

		conflicts();
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

	public void changeSolFromEdges(){
		Edge a = edges[0];
		sol[0] = a.origin;
		
		for(int i=1; i<edges.length; i++){
			 a = edges[i];
			 sol[i] = a.origin;


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
	
	public void solGenerate(Memory mem) {
		for(int i=0; i<edgeMaxSize; i++) {
			sol[i] = edges[i].origin;
		}
	}
	
	public void permutation(Memory mem) {
		/**
		 * Calcula uma permutação e adiciona os ramos correspondentes.
		 */
		for(int i=0; i<solMaxSize; i++)
			solAdd(i);
		shuffle();
		
		edgeGenerate(mem);
		
		conflicts();

	}
	public ArrayList<Point> returnPoints(){
		ArrayList<Point> points = new ArrayList<Point>();
		for(int i: sol){
			points.add(mem.points[i]);
		}
		return points;
	}

	
	public void printSolution() {
		for(int i : sol) {
			System.out.print(mem.points[i]+" ");
		}
		System.out.println(mem.points[0]);
		System.out.println();
	}
	
	public void printEdges() {
		for(Edge e : edges) {
			System.out.println("["+mem.points[e.origin]+","+mem.points[e.dest]+"]");
		}
	}
	public  boolean inBox(Point p1, Point p2, Point p3){
        return(p3.x >= Math.min(p1.x,p2.x) && p3.x <=Math.max(p1.x,p2.x) && p3.y >= Math.min(p1.y, p2.y) 
                    && p3.y <=Math.max(p1.y,p2.y));
    }

	public  boolean[] edgeIntersect(Edge a, Edge b){
		Point a1 = mem.points[a.origin];
		Point a2 = mem.points[a.dest];

		Point b1 = mem.points[b.origin];
		Point b2 = mem.points[b.dest];
	   
	   boolean valid = true;

	   double d1 = (b1.x - a1.x)*(a2.y-a1.y) -(a2.x -a1.x)*(b1.y-a1.y);

	   double d2 = (b2.x -a1.x)*(a2.y-a1.y) - (a2.x -a1.x)*(b2.y-a1.y);

	   double d3 = (a1.x-b1.x)*(b2.y-b1.y) -(b2.x-b1.x)*(a1.y-b1.y);

	   double d4 = (a2.x-b2.x)*(b2.y-b1.y) -(b2.x-b1.x)*(a2.y-b2.y);

	   boolean [] res = new boolean[2];

	   int val = (a2.x-a1.x)*(b2.y-b1.y) - (a2.y-a1.y)*(b2.x-b1.x);
	   
	   if( val==0){
		   int scalar = (a2.x-a1.x)*(b2.x-b1.x) + (a2.y-a1.y)*(b2.y-b1.y);
		   if(scalar < 0){
			   valid = false;
		   }

	   }
	   res[1] = valid;

	   if(d1*d2<0 && d3*d4<0){
		   res[0] = true;
		   return res;
	   }

	   else if(d1==0 && inBox(a1, a2, b1)){
		   res[0] = true;
		   return res;
	   }

	   else if(d2==0 && inBox(a1,a2,b2)){
		   res[0] = true;
		   return res;
	   }

	   else if(d3==0 && inBox(b1,b2,a1)){
		   res[0] = true;
		   return res;        }

	   else if(d4==0 && inBox(b1,b2,a2)){
		   res[0] = true;
		   return res;        
	   }
	   res[0] = false;
	   return res;
   }
	private static void sortPath(Edge[] edges){
		int size = edges.length;
        
        for(int i=0; i<size-1; i++){
			Edge a,b;
			a = edges[i];
			b = edges[i+1];
		
			if(a.dest != b.origin){
				for(int j=0; j<size; j++){
					Edge c = edges[j];
					if(c.origin == a.dest){
						edges[i+1] = c;
						edges[j] = b;
					}
				}
			}
		}
	}
	public Solution twoExchange(Edge a,Edge b, Solution next){
		
        Edge start = new Edge(a.origin, b.origin,euclidean(next.mem, a.origin, b.origin));
        Edge end = new Edge(a.dest,b.dest,euclidean(next.mem,a.dest,b.dest));
        int i=0;
        
		for(i=0; i<next.edgeMaxSize; i++){
            if(next.edges[i].equals(a)){
                next.edges[i] = start;
                break;
            }
        }
        i++;
        if(i >= next.edgeMaxSize) i =0;

        // Mudar orientação do ciclo entre a aresta a e a aresta b

        while(!next.edges[i].equals(b)){
            int x,y;
            x = next.edges[i].origin;
            y = next.edges[i].dest;

            next.edges[i].origin = y;
            next.edges[i].dest = x;
            i++;
            if(i >= next.edgeMaxSize) i=0;

        }
        next.edges[i] =end;

        sortPath(next.edges);
		changeSolFromEdges();
		return next;
    }
	public static boolean conflictAlreadyFound(Edge a, Edge b,ArrayList<Edge> conflicts){
        
        if(conflicts.size() >0){
            if(conflicts.get(0).equals(a) && conflicts.get(1).equals(b)){
                return true;
            }
            if(conflicts.get(1).equals(a) && conflicts.get(0).equals(b)){
                return true;
            }
        }

        for(int i=1; i<conflicts.size()-1; i++){
            if(conflicts.get(i).equals(a) && conflicts.get(i+1).equals(b)){
                return true;
            }
            if(conflicts.get(i).equals(a) && conflicts.get(i-1).equals(b)){
                return true;
            }

        }
        return false;

    }

	public void conflicts(){
		ArrayList<Edge> conflicts = new ArrayList<Edge>();
		int totalConflicts=0;
		int validConflicts=0;
	
		for(int i=0; i<edgeMaxSize; i++){
			Edge a = edges[i];
			for(int j=0; j<edgeMaxSize; j++){
				Edge b = edges[j];
				if(!a.equals(b) && a.dest != b.origin && a.origin != b.dest){
					boolean[] res = edgeIntersect(a, b);
					boolean intersect=res[0];
					boolean valid=res[1];
					if(intersect && !conflictAlreadyFound(a,b,conflicts)){
						if(!valid){
							totalConflicts++;
						}
						else{
						conflicts.add(a);
						conflicts.add(b);
						totalConflicts++;
						validConflicts++;
						}
					}
				}
			}
		}
		this.totalConflicts = totalConflicts;
		this.validConflicts = validConflicts;
		this.conflicts = conflicts;
	}

	public void resolveConflicts() {
		/**
		 * Esgota todos os cruzamentos do subconjunto formando a vizinhança do candidato atual
		 * Retorna o número de vizinhos que compõem o espaço de vizinhança
		 */
		ArrayList<Solution> neighbours = new ArrayList<>();

		

		for(int i=0; i<conflicts.size(); i+=2){
			Edge a = conflicts.get(i);
			Edge b = conflicts.get(i+1);
			Solution next = new Solution(this);
			twoExchange(a, b,next);
			neighbours.add(next);
		}
		this.neighbours = neighbours;
	}
	
	public void printNeighbours(ArrayList<Solution> n) {
		for(Solution s : n) {
			s.printEdges();
			System.out.println();
		}
	}

	
	
	// public void printConflicts(Memory mem){
	// 	System.out.println("empty? :" + edgeConflicts.isEmpty());
	// 	for(Conflict c : edgeConflicts)
	// 		System.out.println("(" + mem.points[c.a.origin] +" " + mem.points[c.a.dest] +")" +" "+"(" +mem.points[c.b.origin] + " " + mem.points[c.b.dest] + ")");
	// }
}
