package vnr.main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import vnr.bandit.Embedding;
import vnr.bandit.ProbabilitySelected;
import vnr.bandit.Similarity;
import vnr.graph.CreateGraph;
import vnr.graph.Edge;
import vnr.graph.Graph;
import vnr.graph.Node;
import vnr.rank.Dijskra;
import vnr.rank.EmbedOrder;
import vnr.rank.Floyd;
import vnr.rank.NodeRank;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int nodeNum=6,edgeNum=9;
		int nodeNum2=3,edgeNum2=3;
		

		
//		double[] rank=new double[nodeNum];
//		Map<Integer,Double> map=null;
		
		List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();
		List<Map.Entry<Integer,Double>> rank2=new ArrayList<Map.Entry<Integer,Double>>();//VNR
		
		LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();
		
		LinkedHashMap<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		
		Node[] node={new Node(10),new Node(8),new Node(5),new Node(3),new Node(9),new Node(4)};
		
		List<Integer> path=new LinkedList<Integer>();
		int dis;
		

//		Edge[] edge={
//				new Edge(0,1,6),
//				new Edge(0,2,3),
//				new Edge(1,2,2),
//				new Edge(1,3,5),
//				new Edge(3,2,3),
//				new Edge(3,4,2),
//				new Edge(2,4,4),
//				new Edge(3,5,3),
//				new Edge(4,5,5)
//};
//		
//		
//		Edge[] edge={
//				new Edge(0,1,1),
//				new Edge(0,2,1),
//				new Edge(1,2,1),
//				new Edge(1,3,1),
//				new Edge(3,2,1),
//				new Edge(3,4,1),
//				new Edge(2,4,1),
//				new Edge(3,5,1),
//				new Edge(4,5,1)};
		
		
		Edge[] edge={
				new Edge(0,1,6),
				new Edge(0,2,3),
				new Edge(1,2,2),
				new Edge(1,3,5),
				new Edge(3,2,3),
				new Edge(3,4,2),
				new Edge(2,4,4),
				new Edge(3,5,3),
				new Edge(4,5,5)};
		Graph g=new Graph(nodeNum);
		
		
		
		Node[] node2={new Node(6),new Node(4),new Node(8)};
		Edge[] edge2={
				new Edge(0,1,1),
				new Edge(0,2,1),
				new Edge(1,2,1)};
		Graph g2=new Graph(nodeNum2);
		
		CreateGraph.create(g, node, nodeNum, edge, edgeNum);
		CreateGraph.create(g2, node2, nodeNum2, edge2, edgeNum2);
		
		NodeRank nr=new NodeRank(g);
		NodeRank nr2=new NodeRank(g2);
		
		int[][] distance = new int[g.getNumOfNode()][g.getNumOfNode()];
		
		

		
		
		try{
			
			
//			for(int i=0;i<g.getNumOfNode();i++){
//				for(int j=i;j<g.getNumOfNode();j++){
//					if(g.getWeight(i, j)>0 && g.getWeight(i, j)<5000)
//					System.out.println(i+"-"+j+":"+g.getWeight(i, j));
//				}
//
//			}
			
//			rank=nr.rank();
			rank2=nr2.rank();
//			for(int i=0;i<rank.size();i++){
//				System.out.println("g2排名结果："+rank2.get(i).getKey()+"--"+rank2.get(i).getValue());
//			}
			
			embOrder=EmbedOrder.embOrder(rank2,g2);
			for(Map.Entry<Integer, Double> entry:embOrder.entrySet()){
				System.out.println("main_key_emborder"+entry.getKey());
			}
			result=Embedding.embedding(embOrder, g2, g);
			System.out.println("还好阿");
			
				
			for(Map.Entry<Integer, Integer> entry:result.entrySet()){
				System.out.println("最终对于VNR的映射结果："+entry.getKey()+"--"+entry.getValue());
			}
			
			
			//link embedding
//			path=Floyd.floyd(g, 1, 0,);
			dis=Floyd.floyd(g, 1, 0, path);
			
			
//			for(int i=0;i<path.size();i++){
//				System.out.print("路径："+path.get(i));
//			}
//			
			
			
//			Arrays.sort(rank);
//			for(int i=0;i<nodeNum;i++){
//				System.out.println(rank[i]);
//			}
//			System.out.println(rank);
//			System.out.println("相似度："+Similarity.cos(g.getNode(0), g.getNode(5)));
//			Similarity.cos(g.getNode(0), g.getNode(3));111
//			System.out.println("节点度："+node[3].getDegree());
		}catch(Exception e){
			System.out.println(e);
		}
		
//		(new FinalRank()).finRank();
		
/**
 * test
 *区间判断
 * 
 */
		/*尴尬，不知道这个是要干嘛的了*/
		double[] test=new double[5];
		for(int i=0;i<5;i++){
			test[i]=Similarity.cos(node[0],node[i]);
		}

//		int m=0;
//		do{
//		System.out.println("第"+m+"次选中节点："+ProbabilitySelected.proSelected(test));
//		m++;
//		}while(m<10);
	}

}
