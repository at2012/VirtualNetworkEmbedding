package vnr.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	
	static final int maxWeight=5000;
	//成员变量
	private List<Node> nodeList;//启发，注意这里一定要写好数组中元素的类型，好调用该类型提供的方法
	private double[][] edges;
	private int numOfEdge;
	
	private double tolSource;//结果分析用，统计当前网络的所有资源总和，cpu和+带宽和
	
	public int survivalTime;
/**
 * 构造函数开辟了存储n个节点的空间，
 * 开辟了存储边的矩阵空间，并且初步初始化这个矩阵，
 * 初始化边的数量为0*/
	public Graph(int n){
		nodeList=new ArrayList<Node>(n);
		edges=new double[n][n];
		tolSource=0;
		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(i==j){
					edges[i][j]=0;
				}
				else{
					edges[i][j]=maxWeight;
				}
			}
		}
		numOfEdge=0;
	}
	
	/**
	 * 用于构建虚拟网络
	 * @param n 节点数
	 * @param sTime 网络生存时间*/
	public Graph(int n,int sTime){
		nodeList=new ArrayList<Node>(n);
		edges=new double[n][n];
		survivalTime=sTime;
		tolSource=0;
		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(i==j){
					edges[i][j]=0;
				}
				else{
					edges[i][j]=maxWeight;
				}
			}
		}
		numOfEdge=0;
	}
	
	
	
	//访问器
	public int getNumOfEdge(){
		return numOfEdge;
	}
	//这似乎是一个没有被使用的方法
	public Object getValue(int n)throws Exception{
		return nodeList.get(n);
	}
	public int getMax() {
		return maxWeight;
	}
	/**获取节点的cpu值
	 * @param n 节点编号
	 * */
	public double getCpu(int n){
		return nodeList.get(n).getCpu();
	}
	public int getDegree(int n){//得到网络中指定下标的节点的节点度，疑惑：不知道为啥，总感觉没有特别大的必要去添加这么 一个方法。
		return nodeList.get(n).getDegree();
	}
	public double getBandwidth(int n){//得到网络中指定下标的节点的节点度，疑惑：不知道为啥，总感觉没有特别大的必要去添加这么 一个方法。
		return nodeList.get(n).getBandwidth();
	}
//	public double get
	/**获取图中节点mn之间边的权重*/
	public double getWeight(int m,int n){
//		if(m<0||m>nodeList.size()||n<0||n>nodeList.size())
//			throw new Exception("参数错误");
		return edges[m][n];
	}
	/**获取图中节点数量*/
	public int getNumOfNode(){
		return nodeList.size();
	}
	public Node getNode(int n){
		return nodeList.get(n);
	}
	public double[][] getEdges(){
		return edges;
	}
	
	public double getTolSource() {
//		for(int i=0;i<nodeList.size();i++){
//			tolSource+=this.getCpu(i);
//		}
		for(int i=0;i<nodeList.size();i++) {
			for(int j=i+1;j<nodeList.size();j++) {
				if(getWeight(i, j)!=this.getMax()) {
					tolSource+=this.getWeight(i, j);
				}
			}
		}
		return tolSource;
	}
	
	/**更新节点cpu值
	 * @param n 图的节点列表中的编号
	 * @param x 节点CPU的新值*/
	public void setCpu(int n,double x) {
		nodeList.get(n).setCpu(x);;
	}
	/**更新链路带宽
	 * @param*/
	public void setWeight(int i,int j,double weight) {
		edges[i][j]=weight;
		edges[j][i]=weight;
	}
	
	//成员方法
	public void insertNode(Node node){//启发：在说明了list元素的类型后，上面的方法就报错了，思考下为什么
		nodeList.add(nodeList.size(),node);
	}
	
	public void insertEdge(int x,int y,double weight){//注意这里要有异常，检查xy是否越界，
		edges[x][y]=weight;
		edges[y][x]=weight;
		numOfEdge++;
		//注意要增加一个边的数量，启发：一个方法在设计的时候要注意更新所有可能被影响的成员变量
	}
	public void delEdge(int x,int y){//注意这里要有异常，检查xy是否越界，要删除的边是否存在
		edges[x][y]=maxWeight;
		numOfEdge--;
	}
	/**这里好像是用于搜索涉及到的三个方法，所以。。。先注释掉看看*/
//	/**
//	 * 深度优先搜索*/
//	public void depthFirstSearch(int v,boolean[] isVisited,Visit vs)throws Exception{//启发，这个递归里，前面递归得到的值是后面递归里的方法参数，所以不需要有返回值，这样想好像不太对
//		
//		vs.print(getValue(v));
//		isVisited[v]=true;
//		
//		int w=getFirstNeighbor(v);
////		int w2
//		while(w!=-1){
//			if(!isVisited[w])//启发，这里不要写isVisited[w]==false,对于这种boolean型的变量的判断，是可以简单点儿的
//				depthFirstSearch(w,isVisited,vs);
//			else
//				w=getNextNeighbor(v,w);
//		}		
//		
//		
//	}
//	
//	
//	//取节点的邻接节点,若存在，返回节点下标，否则返回-1
//	
//	//这个在后面的遍历里会用到，启发：如果你在某个循环或者什么里面，在找到一个以后就不想再找后面的了，而是接着去处理其他动作，那么如果只是在一个函数里面写的话，那么可以把这个过程写成一个函数，让这个函数在找到你要找的以后就返回
//	/**
//	 * */
//	public int getFirstNeighbor(int v){
//
//		for(int i=0;i<nodeList.size();i++){
//			if(edges[v][i]>0 && edges[v][i]<maxWeight)
//				return edges[v][i];
//		}
//		return -1;
//	//启发：for循环中，一旦遇到合适的边，方法就返回了，不会继续访问后面，如果在if里没有返回，那么在最后返回-1
//	}
//	/**
//	 * */
//	public int getNextNeighbor(int v1,int v2){//找到关v1节点的邻接节点v2之后的其他邻接，感觉这个设计很好啊，因为是从v2+1开始搜索，所以无论搜索到第几个都可以
//		for(int i=v2+1;i<nodeList.size();i++){
//			if(edges[v1][i]>0 && edges[v1][i]<maxWeight)
//				return edges[v1][i];
//		}
//		return -1;
//	}
}
