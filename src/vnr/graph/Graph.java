package vnr.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	
	static final int maxWeight=5000;
	//��Ա����
	private List<Node> nodeList;//������ע������һ��Ҫд��������Ԫ�ص����ͣ��õ��ø������ṩ�ķ���
	private int[][] edges;
	private int numOfEdge;
/**
 * ���캯�������˴洢n���ڵ�Ŀռ䣬
 * �����˴洢�ߵľ���ռ䣬���ҳ�����ʼ���������
 * ��ʼ���ߵ�����Ϊ0*/
	public Graph(int n){
		nodeList=new ArrayList<Node>(n);
		edges=new int[n][n];
		
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
	public Object getValue(int n)throws Exception{
//		Node temp=nodeList.get(n);
		return nodeList.get(n);
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ע�������get���������ڲ�֪����ɶ����
		//�������֣���getͼ�У�ĳ���ڵ�� ֵ
	}
	
	
	public int getCpu(int n)throws Exception{
//		Node temp=nodeList.get(n);
		return nodeList.get(n).getCpu();
		//�������֣���getͼ�У�ĳ���ڵ�� ֵ
	}
	public int getDegree(int n){//�õ�������ָ���±�Ľڵ�Ľڵ�ȣ��ɻ󣺲�֪��Ϊɶ���ܸо�û���ر��ı�Ҫȥ�����ô һ��������
		return nodeList.get(n).getDegree();
	}
	/**��ȡͼ�нڵ�mn֮��ߵ�Ȩ��*/
	public int getWeight(int m,int n)throws Exception{
		if(m<0||m>nodeList.size()||n<0||n>nodeList.size())
			throw new Exception("��������");
		return edges[m][n];
	}
	/**��ȡͼ�нڵ�����*/
	public int getNumOfNode(){
		return nodeList.size();
	}
	public Node getNode(int n){
		return nodeList.get(n);
	}
	
	
	
	
	//��Ա����
//	public void insertNode(Object node){
//		nodeList.add(nodeList.size(),node);
//	}
	public void insertNode(Node node){//��������˵����listԪ�ص����ͺ�����ķ����ͱ����ˣ�˼����Ϊʲô
		nodeList.add(nodeList.size(),node);
	}
	
	public void insertEdge(int x,int y,int weight){//ע������Ҫ���쳣�����xy�Ƿ�Խ�磬
		edges[x][y]=weight;
		edges[y][x]=weight;
		numOfEdge++;
		//ע��Ҫ����һ���ߵ�������������һ����������Ƶ�ʱ��Ҫע��������п��ܱ�Ӱ��ĳ�Ա����
	}
	public void delEdge(int x,int y){//ע������Ҫ���쳣�����xy�Ƿ�Խ�磬Ҫɾ���ı��Ƿ����
		edges[x][y]=maxWeight;
		numOfEdge--;
	}
	
	/**
	 * �����������*/
	public void depthFirstSearch(int v,boolean[] isVisited,Visit vs)throws Exception{//����������ݹ��ǰ��ݹ�õ���ֵ�Ǻ���ݹ���ķ������������Բ���Ҫ�з���ֵ�����������̫��
		
		vs.print(getValue(v));
		isVisited[v]=true;
		
		int w=getFirstNeighbor(v);
//		int w2
		while(w!=-1){
			if(!isVisited[w])//���������ﲻҪдisVisited[w]==false,��������boolean�͵ı������жϣ��ǿ��Լ򵥵����
				depthFirstSearch(w,isVisited,vs);
			else
				w=getNextNeighbor(v,w);
		}		
		
		
	}
	
	
	//ȡ�ڵ���ڽӽڵ�,�����ڣ����ؽڵ��±꣬���򷵻�-1
	//����ں���ı�������õ����������������ĳ��ѭ������ʲô���棬���ҵ�һ���Ժ�Ͳ������Һ�����ˣ����ǽ���ȥ����������������ô���ֻ����һ����������д�Ļ�����ô���԰��������д��һ��������������������ҵ���Ҫ�ҵ��Ժ�ͷ���
	public int getFirstNeighbor(int v){

		for(int i=0;i<nodeList.size();i++){
			if(edges[v][i]>0 && edges[v][i]<maxWeight)
				return edges[v][i];
		}
		return -1;
	//������forѭ���У�һ���������ʵıߣ������ͷ����ˣ�����������ʺ��棬�����if��û�з��أ���ô����󷵻�-1
	}
	public int getNextNeighbor(int v1,int v2){//�ҵ���v1�ڵ���ڽӽڵ�v2֮��������ڽӣ��о������ƺܺð�����Ϊ�Ǵ�v2+1��ʼ���������������������ڼ���������
		for(int i=v2+1;i<nodeList.size();i++){
			if(edges[v1][i]>0 && edges[v1][i]<maxWeight)
				return edges[v1][i];
		}
		return -1;
	}
	
	
	
	
	

}
