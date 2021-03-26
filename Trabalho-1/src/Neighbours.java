import java.util.ArrayList;
import java.util.LinkedList;

public class Neighbours {
	Memory mem;
	LinkedList<Conflict> edgeConflicts; 
		
	Neighbours(Memory m){
		mem = m;
	}
	
	class Conflict{
		/**
		 * Classe que contém duas arestas que estejam intersetadas
		 */
		Edge a;
		Edge b;

		boolean collinear;

		Conflict(Edge first, Edge second, boolean col){
			a = first;
			b = second;
			collinear = col;
		}
		
		public boolean equals(Edge fst, Edge snd) {
			return a.equals(fst) ? b.equals(snd) : false;
		}
	}
	
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
	/*
	Uso um array de booleans porque para elém de querer detetar que se interseta, também quero detetar 
	se são segmentos colineares. o [0] indica se interceta e [1] indica se são colineares
	*/
	private boolean[] edgeIntersect(Edge a, Edge b){
		int o1, o2,o3,o4;
		int count =0;
		Point start1, end1, start2, end2;
		start1 = mem.points[a.origin];
		end1 = mem.points[a.dest];
		start2 = mem.points[b.origin];
		end2 = mem.points[b.dest];
		boolean [] array = new boolean[2];
		boolean collinear = false;
		o1 = orientation(start1, end1, start2);
		o2 = orientation(start1, end1, end2);
		o3 = orientation(start2, end2, start1);
		o4 = orientation(start2, end2, end1);
		
		if(o1==0) count++;
		if(o2==0) count++;
		if(o3==0) count++;
		if(o3==0) count++;

		if(count ==4){
			collinear = true;
		}
		
		array[1] = collinear;
		
		if(end1 != start2 && end2 != start1){
			if(o1 != o2   && o3 != o4 ){
				array[0] = true;
				return array;
			}
		}

		



        if(o1==0  && onSegment(start1, start2, end1)){
			if(start2 != end1 && start2 !=start1){
				array[0] = true;
				return array;
			}
		} 
			
        if(o2==0  && onSegment(start1, end2, end1)){
			if(end2 != start1 && end2!= end1){
				array[0] = true;
				return array;
			}
		} 
		
        if(o3==0  && onSegment(start2, start1, end2)){
			if(start1!= start2 && start1 != end2){
				array[0] = true;
				return array;
			}
		} 
		
        if(o4==0  && onSegment(start2, end1, end2)){
			if(end1!= start2 && end1!=end2){
				array[0] = true;
				return array;
			}
		}
		array[0] = false;
        return array;

	}
	
	public boolean conflictAlreadyFound(Edge a, Edge b, LinkedList<Conflict> conflicts) {
		/**
		 * Retorna se um cruzamento já foi encontrado anteriormente.
		 */
		for(Conflict cur : conflicts) {
			if(cur.equals(a, b)) return true;
			if(cur.equals(b, a)) return true;
		}
		return false;
	}
	
	public int conflicts(Solution s, boolean keepEdges) {
		/**
		 * Calcula o conjunto de todos as arestas que estão intersetadas e formarão futuros candidatos
		 * A flag keepEdges serve para podermos utilizar este método para calcular o número de cruzamentos
		 * de qualquer candidato, sem apagarmos o conjunto de cruzamentos anterior.
		 * 
		 * Retorna o número de cruzamentos encontrados == tamanho do conjunto de cruzamentos.
		 */
		int conflicts = 0;
		edgeConflicts = new LinkedList<>();
		
		for(int i=0; i<s.solMaxSize; i++){
			Edge a = s.edges[i];
			for(int j=0; j<s.solMaxSize; j++){
				Edge b = s.edges[j];
				if(!b.equals(a)){
					boolean[] array =edgeIntersect(a,b);
					boolean isCollinear = array[1]; 
					if(array[0] == true && !conflictAlreadyFound(a,b, edgeConflicts)) {
						if(keepEdges) edgeConflicts.add(new Conflict(a,b,isCollinear));
						conflicts++;
					}
				}
				
			}
		}
		
		return conflicts;
	}

	
	private Edge[] sortPath(Edge[] edges, int s){
		for(int i=0; i<s-1; i++){
			Edge a,b;
			a = edges[i];
			b = edges[i+1];
		
			if(a.dest != b.origin){
				for(int j=0; j<s; j++){
					Edge c = edges[j];
					if(c.origin == a.dest){
						edges[i+1] = c;
						edges[j] = b;
					}
				}
			}
		}
		return edges;
	}
	
	
	private Point  subPoint(Point p1, Point p2){
		int x = p1.x - p2.x;
		int y = p1.y - p2.y;
		return new Point(x,y,0);
	}
	
	
	private boolean checkCandidate(Conflict c){

		Point p1 = mem.points[c.a.origin];
		Point p2 = mem.points[c.a.dest];
		Point p3 = mem.points[c.b.origin];
		Point p4 = mem.points[c.b.dest];

		Point a = subPoint(p2,p1);
		Point b = subPoint(p4,p3);

		if((a.x * b.y + a.y * b.y) >0) return true;

		return false;

	}

	private Edge[] twoExchange(Solution s, Conflict c){
		Edge fst, scnd;
		Edge[] empty = new Edge[0];

		
		fst = new Edge(c.a.origin, c.b.origin, s.euclidean(mem, c.a.origin, c.b.origin));
		scnd =new  Edge(c.a.dest, c.b.dest, s.euclidean(mem, c.a.dest, c.b.dest));
		
		for(int i=0; i<s.edges.length; i++){
			if(s.edges[i] == fst || s.edges[i] == scnd){
				return empty;
			}
		}

		if(c.collinear){
			if(!checkCandidate(c)){
				return empty;
			}
		}

		int index =0;
		Edge[] edges = s.copyEdge();
		while(!edges[index].equals(c.a)){
			index++;
		}
		
		edges[index] = new Edge(c.a.origin, c.b.origin, s.euclidean(mem, c.a.origin, c.b.origin));
		
		index++;

		if(index >=s.solMaxSize){
			index=0;
		}

		while(!edges[index].equals(c.b)){
			edges[index] = new Edge(s.edges[index].dest, s.edges[index].origin, s.edges[index].distance);
			index++;
			if(index >= s.solMaxSize){
				index=0;
			}
		}

		edges[index] = new Edge(c.a.dest, c.b.dest, s.euclidean(mem, c.a.dest, c.b.dest));
		

		return sortPath(edges, s.solMaxSize);
	}
	
	public ArrayList<Solution> resolveConflicts(Solution s) {
		/**
		 * Esgota todos os cruzamentos do subconjunto formando a vizinhança do candidato atual
		 * Retorna o número de vizinhos que compõem o espaço de vizinhança
		 */
		int neighbourCount = 0;
		ArrayList<Solution> neighbours = new ArrayList<>();
		while(!edgeConflicts.isEmpty()) {
			neighbours.add(new Solution(s.solMaxSize));
			Edge [] neighbourEdges = twoExchange(s, edgeConflicts.remove());
			//TwoExchange retorna um array vazio quando não pode fazer a troca
			if(neighbourEdges.length ==0 ){
				neighbours.remove(neighbourCount);
			}
			else{
				neighbours.get(neighbourCount).edges = neighbourEdges;
				neighbours.get(neighbourCount).solSize = s.solSize;
				neighbours.get(neighbourCount).edgeSize = s.edgeSize;
				neighbours.get(neighbourCount).solGenerate(mem);
				neighbourCount++;

			}
		}
		
		return neighbours;
	}	
	
	public void printNeighbours(ArrayList<Solution> n) {
		for(Solution s : n) {
			s.printEdges(mem);
			System.out.println();
		}
	}
	
	
	public void printConflicts(Memory mem){
		System.out.println("empty? :" + edgeConflicts.isEmpty());
		for(Conflict c : edgeConflicts)
			System.out.println("(" + mem.points[c.a.origin] +" " + mem.points[c.a.dest] +")" +" "+"(" +mem.points[c.b.origin] + " " + mem.points[c.b.dest] + ")");
	}
}
