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
	
	public void swapIndex(int[] sol, int i, int j, Solution s) {
		
		//edge cases
		if(i == s.solSize-1)	i = 0;
		else					i++;
		
		sol[i] = s.sol[j];
		sol[j] = s.sol[i];
	}
	
	public int[] twoExchange(Solution s, Conflict c) {
		/**
		 * Aplica a heurística two exchange, trocando de posição os dois índices que estavam no
		 * array solução do candidato.
		 */
		int first = s.getEdge(c.a);
		int second = s.getEdge(c.b);
		
		int[] sol = s.copySol();
		swapIndex(sol, first, second, s);
		
		return sol;
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
			neighbours.get(neighbourCount).sol = twoExchange(s, edgeConflicts.remove());
			neighbours.get(neighbourCount).solSize = s.solSize;
			neighbours.get(neighbourCount).edgeGenerate(mem);
			neighbourCount++;
		}
		
		//printNeighbours(neighbours);
		
		return neighbours;
	}	
	
	
	public int firstImprovement(ArrayList<Solution> neighbours){		
		int best = 0;
		int perimeter = neighbours.get(best).getPerimiter();
		
		for(int i=1; i<neighbours.size(); i++) {
			int tmpPerimeter = neighbours.get(i).getPerimiter();
			if(tmpPerimeter < perimeter) {
				best = i;
				break;
			}
		}

		return best;
	}
	
	
	public int bestImprovementFirst(ArrayList<Solution> neighbours){	
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

		return best;
	}
	
	public int lesserConflicts(ArrayList<Solution> neighbours) {
		/**
		 * Retorna o vizinho com menos conflitos e retorna o seu índice
		 */
		int best = 0;
		int conflicts = conflicts(neighbours.get(best), false);
		
		for(int i=1; i<neighbours.size(); i++) {
			int tmpConflicts = conflicts(neighbours.get(i), false);
			if(tmpConflicts < conflicts) {
				best = i;
				conflicts = tmpConflicts;
			}
		}
		return best;
	}
	
	public int getNewRandom(int size) {
		Random rand = new Random();
		return rand.nextInt(size);		
	}
	
	public int chooseFunction(ArrayList<Solution> n, int option) {
		switch(option) {
		case 1: return bestImprovementFirst(n);
		case 2: return firstImprovement(n);
		case 3: return lesserConflicts(n);
		case 4: return getNewRandom(n.size());
		}
		return -1; //error
	}
	
	public void hillClimbing(Solution s, int option) {
		int nConflicts = conflicts(s, true);
		int perimiter = s.getPerimiter();
		ArrayList<Solution> neighbours = new ArrayList<>();
		
		while(nConflicts > 0 && (!neighbours.isEmpty() || !edgeConflicts.isEmpty())) {
			if(!edgeConflicts.isEmpty()) { //se estiver vazio então manteve-se a solução da iteração anterior
				neighbours = resolveConflicts(s);
			}
			
			int best = chooseFunction(neighbours, option);
			if(conflicts(neighbours.get(best), false) == 0) break;
			int curPerimiter = neighbours.get(best).getPerimiter();
			
			if(curPerimiter < perimiter) {
				s.sol = neighbours.get(best).sol;
				s.edges = neighbours.get(best).edges;
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