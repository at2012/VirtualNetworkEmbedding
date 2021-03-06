package vnr.graph;

public class Node {
	
	private double cpu;
	private int degree;
	private double bandwidth;
	
	public Node(double c){//在还未完善节点建模的时候可以这么用
		cpu=c;
	}
	
	public Node(double c,int d){//完善的节点建模之后，记得用这个初始化节点,or 这个degree根本就不是在节点建立的时候初始化的 ，就只能在插入边之后才自动增加，所以建立节点的时候是不会提供egree的 
		cpu=c;
		degree=d;
	}
	
	public double getCpu(){
		return cpu;
	}
	public int getDegree(){
		return degree;
	}
	public double getBandwidth() {
		return bandwidth;
	}
	
	public double getResourceBlock() {
		return bandwidth*cpu;
	}
	
	public void setCpu(double x) {
		cpu=x;
	}
	
	
	
	public void addDegree(){//在插入边之后节点的degree就会增加
		degree++;
	}
	/**
	 * @param bw 新加入节点与当前节点之间的链路权重*/
	public void addBandwidth(double bw) {
		bandwidth+=bw;
	}
	
}
