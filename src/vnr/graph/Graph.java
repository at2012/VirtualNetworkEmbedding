package vnr.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	
	static final int maxWeight=5000;
	//��Ա����
	private List<Node> nodeList;//������ע������һ��Ҫд��������Ԫ�ص����ͣ��õ��ø������ṩ�ķ���
	private double[][] edges;
	private int numOfEdge;
	
	private double tolSource;//��������ã�ͳ�Ƶ�ǰ�����������Դ�ܺͣ�cpu��+�����
/**
 * ���캯�������˴洢n���ڵ�Ŀռ䣬
 * �����˴洢�ߵľ���ռ䣬���ҳ�����ʼ���������
 * ��ʼ���ߵ�����Ϊ0*/
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
	
	
	//������
	public int getNumOfEdge(){
		return numOfEdge;
	}
	//���ƺ���һ��û�б�ʹ�õķ���
	public Object getValue(int n)throws Exception{
		return nodeList.get(n);
	}
	public int getMax() {
		return maxWeight;
	}
	/**��ȡ�ڵ��cpuֵ
	 * @param n �ڵ���
	 * */
	public double getCpu(int n){
		return nodeList.get(n).getCpu();
	}
	public int getDegree(int n){//�õ�������ָ���±�Ľڵ�Ľڵ�ȣ��ɻ󣺲�֪��Ϊɶ���ܸо�û���ر��ı�Ҫȥ�����ô һ��������
		return nodeList.get(n).getDegree();
	}
	public double getBandwidth(int n){//�õ�������ָ���±�Ľڵ�Ľڵ�ȣ��ɻ󣺲�֪��Ϊɶ���ܸо�û���ر��ı�Ҫȥ�����ô һ��������
		return nodeList.get(n).getBandwidth();
	}
//	public double get
	/**��ȡͼ�нڵ�mn֮��ߵ�Ȩ��*/
	public double getWeight(int m,int n){
//		if(m<0||m>nodeList.size()||n<0||n>nodeList.size())
//			throw new Exception("��������");
		return edges[m][n];
	}
	/**��ȡͼ�нڵ�����*/
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
	
	/**���½ڵ�cpuֵ
	 * @param n ͼ�Ľڵ��б��еı��
	 * @param x �ڵ�CPU����ֵ*/
	public void setCpu(int n,double x) {
		nodeList.get(n).setCpu(x);;
	}
	/**������·����
	 * @param*/
	public void setWeight(int i,int j,double weight) {
		edges[i][j]=weight;
		edges[j][i]=weight;
	}
	
	//��Ա����
	public void insertNode(Node node){//��������˵����listԪ�ص����ͺ�����ķ����ͱ����ˣ�˼����Ϊʲô
		nodeList.add(nodeList.size(),node);
	}
	
	public void insertEdge(int x,int y,double weight){//ע������Ҫ���쳣�����xy�Ƿ�Խ�磬
		edges[x][y]=weight;
		edges[y][x]=weight;
		numOfEdge++;
		//ע��Ҫ����һ���ߵ�������������һ����������Ƶ�ʱ��Ҫע��������п��ܱ�Ӱ��ĳ�Ա����
	}
	public void delEdge(int x,int y){//ע������Ҫ���쳣�����xy�Ƿ�Խ�磬Ҫɾ���ı��Ƿ����
		edges[x][y]=maxWeight;
		numOfEdge--;
	}
	/**������������������漰�����������������ԡ�������ע�͵�����*/
//	/**
//	 * �����������*/
//	public void depthFirstSearch(int v,boolean[] isVisited,Visit vs)throws Exception{//����������ݹ��ǰ��ݹ�õ���ֵ�Ǻ���ݹ���ķ������������Բ���Ҫ�з���ֵ�����������̫��
//		
//		vs.print(getValue(v));
//		isVisited[v]=true;
//		
//		int w=getFirstNeighbor(v);
////		int w2
//		while(w!=-1){
//			if(!isVisited[w])//���������ﲻҪдisVisited[w]==false,��������boolean�͵ı������жϣ��ǿ��Լ򵥵����
//				depthFirstSearch(w,isVisited,vs);
//			else
//				w=getNextNeighbor(v,w);
//		}		
//		
//		
//	}
//	
//	
//	//ȡ�ڵ���ڽӽڵ�,�����ڣ����ؽڵ��±꣬���򷵻�-1
//	
//	//����ں���ı�������õ����������������ĳ��ѭ������ʲô���棬���ҵ�һ���Ժ�Ͳ������Һ�����ˣ����ǽ���ȥ����������������ô���ֻ����һ����������д�Ļ�����ô���԰��������д��һ��������������������ҵ���Ҫ�ҵ��Ժ�ͷ���
//	/**
//	 * */
//	public int getFirstNeighbor(int v){
//
//		for(int i=0;i<nodeList.size();i++){
//			if(edges[v][i]>0 && edges[v][i]<maxWeight)
//				return edges[v][i];
//		}
//		return -1;
//	//������forѭ���У�һ���������ʵıߣ������ͷ����ˣ�����������ʺ��棬�����if��û�з��أ���ô����󷵻�-1
//	}
//	/**
//	 * */
//	public int getNextNeighbor(int v1,int v2){//�ҵ���v1�ڵ���ڽӽڵ�v2֮��������ڽӣ��о������ƺܺð�����Ϊ�Ǵ�v2+1��ʼ���������������������ڼ���������
//		for(int i=v2+1;i<nodeList.size();i++){
//			if(edges[v1][i]>0 && edges[v1][i]<maxWeight)
//				return edges[v1][i];
//		}
//		return -1;
//	}
}
