package vnr.graph;

public class CreateGraph {
	
	public static void create(Graph g,Node[] node,int numOfNode,Edge[] e,int numOfEdge){
		for(int i=0;i<numOfNode;i++)
			g.insertNode(node[i]);//��洢�ռ��в���ڵ�
		for(int i=0;i<numOfEdge;i++){
			g.insertEdge(e[i].getX(),e[i].getY(), e[i].getWeight());
			
			node[e[i].getX()].addDegree();//�����֮�󣬽ڵ��degree����
			node[e[i].getY()].addDegree(); 
//			node[e[i].]
			node[e[i].getX()].addBandwidth(e[i].getWeight());;//�����֮�󣬽ڵ��degree����
			node[e[i].getY()].addBandwidth(e[i].getWeight());; 
		}
				
	}

}
