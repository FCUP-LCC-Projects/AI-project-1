import java.util.ArrayList;

public class Neighbours {
	Memory mem;
		
	Neighbours(Memory m){
		mem = m;
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
	public static  Solution twoExchange(Solution s, Edge a,Edge b){
		
		
		Solution next = new Solution(s.edgeMaxSize, s.sol, s.edges,s.mem);

        Edge start = new Edge(a.origin, b.origin,0);
        Edge end = new Edge(a.dest,b.dest,0);
        int i=0;
        
		for(i=0; i<next.edgeMaxSize; i++){
            if(next.edges[i].equals(a)){
                next.edges[i] = start;
                break;
            }
        }
        i++;
        if(i > next.edgeMaxSize) i =0;

        // Mudar orientação do ciclo entre a aresta a e a aresta b

        while(!next.edges[i].equals(b)){
            int x,y;
            x = next.edges[i].origin;
            y = next.edges[i].dest;

            next.edges[i].origin = y;
            next.edges[i].dest = x;
            i++;
            if(i > next.edgeMaxSize) i=0;

        }
        next.edges[i] =end;

        sortPath(next.edges);
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

	public void conflicts(Solution s){
		ArrayList<Edge> conflicts = new ArrayList<Edge>();
		int totalConflicts=0;
		int validConflicts=0;
	
		for(int i=0; i<s.edgeMaxSize; i++){
			Edge a = s.edges[i];
			for(int j=0; j<s.edgeMaxSize; j++){
				Edge b = s.edges[j];
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
		s.totalConflicts = totalConflicts;
		s.validConflicts = validConflicts;
	}

	public ArrayList<Solution> resolveConflicts(Solution s) {
		/**
		 * Esgota todos os cruzamentos do subconjunto formando a vizinhança do candidato atual
		 * Retorna o número de vizinhos que compõem o espaço de vizinhança
		 */
		ArrayList<Solution> neighbours = new ArrayList<>();

		ArrayList<Edge> conflicts = s.conflicts;
		
		Solution n = new Solution(s.solMaxSize,s.mem);

		for(int i=0; i<s.conflicts.size(); i+=2){
			Edge a = conflicts.get(i);
			Edge b = conflicts.get(i+1);
			n = twoExchange(s, a, b);
			neighbours.add(n);
		}

		return neighbours;

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