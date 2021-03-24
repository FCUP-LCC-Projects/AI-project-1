import java.util.ArrayList;
import java.util.Random;
import java.util.LinkedList;

public class HillClimbing {
	/** 
	 * Classe para resolução do Hill Climbing e com todas as funções auxiliares
	 * 
	 * mem - Conjunto de pontos gerados originalmente
	 * edgeConflicts - subconjunto referido globalmente de cruzamentos de arestas para qualquer candidato
	 * 					que está a ser analisado.
	 */
	Memory mem;
	LinkedList<Conflict> edgeConflicts; 
	
	HillClimbing(Memory m){
		mem = m;
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
        if(o2==0  && onSegment(start1, end2, end1)) return true;
        if(o3==0  && onSegment(start2, start1, end2)) return true;
        if(o4==0  && onSegment(start2, end1, end2)) return true;

        return false;

	}
	
	class Conflict{
		/**
		 * Classe que contém duas arestas que estejam intersetadas
		 */
		Edge a;
		Edge b;
		
		Conflict(Edge first, Edge second){
			a = first;
			b = second;
		}
		
		public boolean equals(Edge fst, Edge snd) {
			return a.equals(fst) ? b.equals(snd) : false;
		}
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
				if(!b.equals(a) && a.dest!=b.origin && a.origin!=b.dest && edgeIntersect(a,b) && !conflictAlreadyFound(a,b, edgeConflicts)) {
					if(keepEdges) edgeConflicts.add(new Conflict(a,b));
					conflicts++;
				}
				
			}
		}
		
		return conflicts;
	}
	
	public Edge[] sortPath(Edge[] edges, int s){
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

	public Edge[] twoExchange(Solution s, Conflict c){
		Edge fst, scnd;
		
		fst = new Edge(c.a.origin, c.b.origin, s.euclidean(mem, c.a.origin, c.b.origin));
		scnd =new  Edge(c.a.dest, c.b.dest, s.euclidean(mem, c.a.dest, c.b.dest));
		
		for(int i=0; i<s.edges.length; i++){
			if(s.edges[i] == fst || s.edges[i] == scnd){
				Edge[] empty = new Edge[0];
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
			//TwoExchange function returns a empty array of Edges when can't perform the twoExchange action
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
		
		//printNeighbours(neighbours);
		
		return neighbours;
	}	
	
	
	public Solution firstImprovement(ArrayList<Solution> neighbours){		
		int best = 0;
		int perimeter = neighbours.get(best).getPerimiter();
		
		for(int i=1; i<neighbours.size(); i++) {
			int tmpPerimeter = neighbours.get(i).getPerimiter();
			if(tmpPerimeter < perimeter) {
				best = i;
				break;
			}
		}		
		return neighbours.get(best);
	}
	
	
	public Solution bestImprovementFirst(ArrayList<Solution> neighbours){	
		/**
		 * Calcula o vizinho com melhor perímetro e retorna o seu índice
		 */
		int best = 0;
		int perimeter = neighbours.get(best).getPerimiter();
		
		for(int i=1; i<neighbours.size(); i++) {
			int tmpPerimeter = neighbours.get(i).getPerimiter();
			if(tmpPerimeter < perimeter) {
				best = i;
				perimeter = tmpPerimeter;
			}
		}
		return neighbours.get(best);
	}
	
	public Solution lesserConflicts(ArrayList<Solution> neighbours) {
		/**
		 * Retorna o vizinho com menos conflitos e retorna o seu índice
		 */
		int best = 0;
		int conflicts = conflicts(neighbours.get(best),false);
		
		for(int i=0; i<neighbours.size(); i++) {
			int tmpConflicts = conflicts(neighbours.get(i), false);
			if(tmpConflicts < conflicts) {
				best = i;
				conflicts = tmpConflicts;
			}
		}
		return neighbours.get(best);
	}
	
	public Solution getNewRandom(int size, ArrayList<Solution> n) {
		Random rand = new Random();
		return n.get(rand.nextInt(size));		
	}
	
	public Solution chooseFunction(Solution s, ArrayList<Solution> n, int option) {
		switch(option) {
		case 1: return bestImprovementFirst(n);
		case 2: return firstImprovement(n);
		case 3: return lesserConflicts(n);
		case 4: return getNewRandom(n.size(), n);
		}
		return s;
	}
	
	public void hillClimbing(Solution s, int option) {
		int nConflicts = conflicts(s, true);
		int perimiter = s.getPerimiter();
		ArrayList<Solution> neighbours = new ArrayList<>();
		
		while(nConflicts > 0 && (!neighbours.isEmpty() || !edgeConflicts.isEmpty())) {
			if(!edgeConflicts.isEmpty()) { //se estiver vazio então manteve-se a solução da iteração anterior
				neighbours = resolveConflicts(s);
			}
			
			Solution best = chooseFunction(s, neighbours, option);
			int curPerimiter = best.getPerimiter();
			
			if(curPerimiter < perimiter) {
				s.sol = best.sol;
				s.edges = best.edges;
				nConflicts = conflicts(s, true);
				perimiter = curPerimiter;
			}
			else if(option == 4) neighbours.remove(best);
			else break; //se o melhor candidato procurado na vizinhança não melhora a solução atual então a solução
						// atual é um máximo
		}
		System.out.println("conflicts: "+nConflicts);
		s.printSolution(mem);
		
	}
	
	
	public void printNeighbours(ArrayList<Solution> n) {
		for(Solution s : n) {
			s.printEdges(mem);
			System.out.println();
		}
	}
	
	
	public void printConflicts(){
		System.out.print("empty? :" + edgeConflicts.isEmpty());
		for(Conflict c : edgeConflicts)
			System.out.println(c.a+" "+c.b);
	}
}
