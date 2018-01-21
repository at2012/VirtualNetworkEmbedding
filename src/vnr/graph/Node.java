package vnr.graph;

public class Node {
	
	private int cpu;
	private int degree;
	
	public int getCpu(){
		return cpu;
	}
	public int getDegree(){
		return degree;
	}
	
	public void addDegree(){//在插入边之后节点的degree就会增加
		degree++;
	}
	
	public Node(int c){//在还未完善节点建模的时候可以这么用
		cpu=c;
	}
	
	public Node(int c,int d){//完善的节点建模之后，记得用这个初始化节点,or 这个degree根本就不是在节点建立的时候初始化的 ，就只能在插入边之后才自动增加，所以建立节点的时候是不会提供egree的 
		cpu=c;
		degree=d;
	}
	



}
