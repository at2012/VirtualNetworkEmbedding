package vnr.graph;

public class Edge {

	private int x,y,weight;
	//成员变量访问器
	public int getX(){ 
		return x;
	}
	public int getY(){
		return y;
	}
	public int getWeight(){
		return weight;
	}
	
	
	public Edge(int x,int y,int weight){
		this.x=x;
		this.y=y;
		this.weight=weight;
	}

	
	
}
