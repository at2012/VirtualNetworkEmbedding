package vnr.graph;

public class Edge {

	private int x,y;
	private double weight;
	//��Ա����������
	public int getX(){ 
		return x;
	}
	public int getY(){
		return y;
	}
	public double getWeight(){
		return weight;
	}
	
	
	public Edge(int x,int y,double weight){
		this.x=x;
		this.y=y;
		this.weight=weight;
	}

	
	
}
