import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;


class Segment{
    Edge edge;
    EventPoint key;

    Segment(Edge a, EventPoint k){
        edge =a;
        key = k;
    }
}
class EventPoint{


    double x;
    double y;
    boolean isUpper=false;
    boolean isLower=false;
    boolean isIntersection=false;
    EventPoint(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void setIsUpper(boolean t){
        isUpper =t;
    }
    public void setIsLower(boolean t ){
        isLower = t;
    }

    public void setIntersection(boolean t){
        isIntersection = t;
    }

    public boolean  isLower(){
        return isLower;
    }
    public boolean isUpper(){
        return isUpper;
    }
    public boolean isIntersection(){
        return isIntersection;
    }
    
}


class Event{

    ArrayList<Segment> upperSegments = new ArrayList<Segment>();
    ArrayList<Segment> lowerSegments = new ArrayList<Segment>();
    ArrayList<Segment> containSegments = new ArrayList<Segment>();


    public void addUperSegment(Segment s){
        upperSegments.add(s);
    }

    public void addLowerSegment(Segment s){
        lowerSegments.add(s);
    }

    public void addContainSegment(Segment s){
        containSegments.add(s);
    }
}

public class Teste{
    Memory mem;

    public Teste(Memory m){
        mem = m;

    }

    Comparator<EventPoint> queueComparator = new Comparator<EventPoint>(){
        @Override
        public int compare(EventPoint a, EventPoint b){                
            if(a.y > b.y){
                return -1;
            }
            else if(a.y == b.y){
                if(a.x < b.x){
                    return -1;
                }
                if(a.x == b.x){
                    return 0;
                }
            }
        return 1;
        }
    };

    Comparator<EventPoint> sweepLineComparator = new Comparator<EventPoint>(){
        
        @Override
        public int compare(EventPoint a, EventPoint b){
            if(a.x < b.x){
                return -1;
            }
            if( a.x > b.x){
                return 1;
            }
            if(a.x == b.x){
                if(a.y > b.y){
                    return -1;
                }
                else{
                    return 1;
                }
            }
            return 0;
                
            }
        };

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

   public  double[]  intersectPoint(Point A, Point B,Point C, Point D){

    double a1 = B.y - A.y;
    double b1 = A.x - B.x;
    double c1 = a1*(A.x) + b1*(A.y);
    
    double a2 = D.y - C.y;
    double b2 = C.x - D.x;
    double c2 = a2*(C.x)+ b2*(C.y);
    
    double determinant = a1*b2 - a2*b1;
    
    
    double x = (b2*c1 - b1*c2)/determinant;
    double y = (a1*c2 - a2*c1)/determinant;
        
    double[] res = {x,y};
    return res;
    
    }



   public  double[] doIntersect(Edge a, Edge b){
       boolean[] res =(edgeIntersect(a, b));
       double[] cords = new double[2]; 
       
       Point aStart,aEnd;
       aStart = mem.points[a.origin];
       aEnd = mem.points[a.dest];

       
       
       if(res[0]==true && res[1]==true){
           cords = intersectPoint(mem.points[a.origin], mem.points[a.dest], mem.points[b.origin], mem.points[b.dest]);
           
           
           if(cords[0] >= Math.min(aStart.x, aEnd.x) && cords[0]<=Math.max(aStart.x, aStart.y)
            && cords[1] >=Math.min(aStart.y, aEnd.y) && cords[1]<= Math.max(aStart.y, aEnd.y)){

                return cords;
            }
           
       }

       if(res[0]==true && res[1] == false){
           //Increase conflicts counter;
       }
    
        return null;
       
   }

    public  TreeMap<EventPoint,Event> prepareEvents(Solution s){
        TreeMap<EventPoint,Event> events= new TreeMap<EventPoint,Event>(queueComparator);

        for(int i=0; i<s.edgeMaxSize; i++){
            Edge a = s.edges[i];
            
            double upperX=Double.NEGATIVE_INFINITY, upperY=Double.NEGATIVE_INFINITY;
            double lowerX=Double.NEGATIVE_INFINITY, lowerY=Double.NEGATIVE_INFINITY;

            
            if(mem.points[a.origin].y > mem.points[a.dest].y){
                upperX = mem.points[a.origin].x;
                upperY = mem.points[a.origin].y;

                lowerX = mem.points[a.dest].x;
                lowerY = mem.points[a.dest].y;
            }

            else if (mem.points[a.origin].y == mem.points[a.dest].y){
                if(mem.points[a.origin].x < mem.points[a.dest].x){
                    upperX = mem.points[a.origin].x;
                    upperY = mem.points[a.origin].y;

                    lowerX = mem.points[a.dest].x;
                    lowerY = mem.points[a.dest].y;
                    
                }
            }
            else{
                upperX = mem.points[a.dest].x;
                upperY = mem.points[a.dest].y;

                lowerX = mem.points[a.origin].x;
                lowerY = mem.points[a.origin].y;
            }

            if(upperX != Double.NEGATIVE_INFINITY && upperY != Double.NEGATIVE_INFINITY){
                EventPoint upper= new EventPoint(upperX, upperY);

                Segment seg = new Segment(a, new EventPoint(upperX, upperY));
             
                if(events.containsKey(upper)){
                    upper.setIsUpper(true);
                     Event p = events.get(upper);
                     p.addUperSegment(seg);
                }
                else{
                    Event p = new Event();
                    p.addUperSegment(seg);
                    upper.setIsUpper(true);
                    events.put(upper, p);
                }
            }
            if(lowerX != Double.NEGATIVE_INFINITY && lowerY != Double.NEGATIVE_INFINITY){
                EventPoint lower = new EventPoint(lowerX,lowerY);

                Segment seg = new Segment(a,new EventPoint(lowerX,lowerY));

                if(events.containsKey(lower)){
                    lower.setIsLower(true);
                    Event p = events.get(lower);
                    p.addLowerSegment(seg);
                }
                else{
                    Event p = new Event();
                    p.addLowerSegment(seg);
                    lower.setIsLower(true);
                    events.put(lower, p);
                }
            }
        }

        return events;
    }


    public  EventPoint[] findNeighbours(TreeMap<EventPoint,Event> sweepLine, EventPoint p){
        EventPoint[]  n= {null,null};
        
        EventPoint a =  sweepLine.lowerKey(p);
        EventPoint b = sweepLine.higherKey(p);
        if(a !=null){
            n[0] = a;
        }
        if(b !=null){
            n[1] =b;
        }
        return n;
        
    }
    
    public  void  handleEventPoint(EventPoint p,Event pEvent,TreeMap<EventPoint,Event> sweepLine, ArrayList<Edge> conflicts, TreeMap<EventPoint,Event> eventQueue){
        EventPoint[] n;

        if(p.isUpper()){
            double[] cords;
            
            sweepLine.put(p,pEvent);
            n = findNeighbours(sweepLine,p);
            
            if(n[0]!=null){
                Event left = sweepLine.get(n[0]);
                Edge a,b;
                if(left.upperSegments.size() >1){
                    a = left.upperSegments.get(1).edge;
                }
                a = left.upperSegments.get(0).edge;
                if(pEvent.upperSegments.size()>1){
                    b = pEvent.upperSegments.get(0).edge;
                }
                 b = pEvent.upperSegments.get(0).edge;
                cords = doIntersect(a, b);
                    
                if(cords !=null){
                    EventPoint intersect = new EventPoint(cords[0],cords[1]);
                    intersect.setIntersection(true);
                    Event t = new Event();
                    t.addContainSegment(new Segment(a, n[0]));
                    t.addContainSegment(new Segment(b,p));
                    eventQueue.put(intersect,t);
                }          
            }
            if(n[1] != null){
                Event right = sweepLine.get(n[1]);
                Edge a,b;
                if(right.upperSegments.size() >1){
                    a = right.upperSegments.get(0).edge;
                }
                a = right.upperSegments.get(0).edge;
                
                if(pEvent.upperSegments.size()>1){
                    b = pEvent.upperSegments.get(1).edge;
                }
                 b = pEvent.upperSegments.get(0).edge;
                cords = doIntersect(a, b);
                    if(cords !=null){
                        EventPoint intersect = new EventPoint(cords[0],cords[1]);
                        intersect.setIntersection(true);
                        Event t = new Event();
                        t.addContainSegment(new Segment(a, n[1]));
                        t.addContainSegment(new Segment(b, p));
                        eventQueue.put(intersect,t);

                    }          
            }

        }

        if(p.isLower()){
            n = findNeighbours(sweepLine,p);
            sweepLine.remove(p);

            if(n[0] !=null && n[1] !=null){
                Event left = sweepLine.get(n[0]);
                Event right = sweepLine.get(n[1]);
                Edge a,b;
                a = left.upperSegments.get(0).edge;
                b = right.upperSegments.get(0).edge;

                double[] cords = doIntersect(a, b);

                if(cords !=null){
                    if(cords[1] < p.y){
                        EventPoint intersect = new EventPoint(cords[0],cords[1]);
                        intersect.setIntersection(true);
                        Event t = new Event();
                        t.addContainSegment(new Segment(a, n[0]));
                        t.addContainSegment(new Segment(b,n[1]));
                        eventQueue.put(intersect,t);

                    }
                }
            }
        }
        if(p.isIntersection()){

            EventPoint l = pEvent.containSegments.get(0).key;
            EventPoint r = pEvent.containSegments.get(1).key;
            
            Event left = sweepLine.get(l);
            Event right = sweepLine.get(r);

            sweepLine.remove(l);
            sweepLine.remove(r);
            
            left.containSegments.get(0).key = r;
            right.containSegments.get(0).key = l;

            sweepLine.put(l,right);
            sweepLine.put(r,left);

            n = findNeighbours(sweepLine, r);
            double[] cords;
            if(n[0] !=null){
                Event ln = sweepLine.get(n[0]);
                Edge a,b;
                if(ln.upperSegments.size() >1){
                    a = ln.upperSegments.get(1).edge;
                }
                a = ln.upperSegments.get(0).edge;
                if(pEvent.upperSegments.size()>1){
                    b = pEvent.upperSegments.get(0).edge;
                }
                 b = pEvent.upperSegments.get(0).edge;
                cords = doIntersect(a, b);
                    
                if(cords !=null){
                    EventPoint intersect = new EventPoint(cords[0],cords[1]);
                    intersect.setIntersection(true);
                    Event t = new Event();
                    t.addContainSegment(new Segment(a, n[0]));
                    t.addContainSegment(new Segment(b,p));
                    eventQueue.put(intersect,t);
                } 

            }

            n = findNeighbours(sweepLine, l);
            if(n[1] !=null){
                Event rn = sweepLine.get(n[1]);
                Edge a,b;
                if(rn.upperSegments.size() >1){
                    a = right.upperSegments.get(0).edge;
                }
                a = rn.upperSegments.get(0).edge;
                
                if(pEvent.upperSegments.size()>1){
                    b = pEvent.upperSegments.get(1).edge;
                }
                 b = pEvent.upperSegments.get(0).edge;
                cords = doIntersect(a, b);
                    if(cords !=null){
                        EventPoint intersect = new EventPoint(cords[0],cords[1]);
                        intersect.setIntersection(true);
                        Event t = new Event();
                        t.addContainSegment(new Segment(a, n[1]));
                        t.addContainSegment(new Segment(b, p));
                        eventQueue.put(intersect,t);

                    }          

            }
        }

        conflicts.add(pEvent.containSegments.get(0).edge);
        conflicts.add(pEvent.containSegments.get(1).edge);

        }

    public ArrayList<Edge> findIntersections(Solution s){
        ArrayList<Edge> conflicts = new ArrayList<Edge>();
        
        TreeMap<EventPoint,Event> eventQueue = new TreeMap<EventPoint,Event>();
        eventQueue = prepareEvents(s);
        
        TreeMap<EventPoint,Event> sweepLine = new TreeMap<EventPoint,Event>(sweepLineComparator);

        while(!eventQueue.isEmpty()){
            EventPoint next = eventQueue.firstKey();
            Event event = eventQueue.get(next);
            eventQueue.remove(next);
            handleEventPoint(next,event,sweepLine, conflicts, eventQueue);

        }

        return conflicts;
    }

    public static void main(String[] args){

        
        Scanner in = new Scanner(System.in);
			int n = in.nextInt();
		    Memory mem = new Memory(n);
			for(int i=0; i<n; i++){
				int x,y;
				x = in.nextInt();
				y = in.nextInt();
				mem.add(x,y,i);
			}
            in.close();

        Teste test = new Teste(mem);

        Solution s = new Solution(8,mem);
        s.greedy(mem);

        ArrayList<Edge> conflicts = test.findIntersections(s);

        System.out.println();



       

        

        
    }
}