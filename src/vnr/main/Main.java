package vnr.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import vnr.bandit.Embedding;
import vnr.bandit.Similarity;
import vnr.graph.CreateGraph;
import vnr.graph.Edge;
import vnr.graph.Graph;
import vnr.graph.Node;
import vnr.rank.EmbedOrder;
import vnr.rank.Floyd;
import vnr.rank.NodeRank;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int nodeNum=-1,edgeNum=-1;
		
		LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//最终映射顺序
		LinkedHashMap<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		
		try {
			
			/*物理网络topo处理*/
			BufferedReader br=new BufferedReader(new FileReader("physical_topo.txt"));
			String line;
			String regex=" ";
			String[] lineContent;
			List<String> lines=new LinkedList<String>();
			
			Edge[] edgePhy=null;//启发：由于创建在另外的块中，所以在另外的块中使用的时候出现未被初始化的错误。所以在这里用的null，虽然知道在运行过程中可以被正确定义，但是还是担心----启发：后面处理拓扑图的时候用到edge，但是定义在try里面，
			//所以不能用，错误同上一个启发。
			Node[] nodePhy=null;
			Graph gPhy=null;//启发：之前edge\node\g的定义以及创建都被定义在了if里面，由于变量的定义在if块厘米，使用在else块里面，所以使用的时候报错：变量未被定义。所以把定义放在了这里，创建再放在if里面，应该么有问题了吧
			
			
			
			//网络映射相关变量：noderank
			List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR
			
			
			while((line=br.readLine())!=null) {
				lines.add(line);
			}
			
			for(int i=0;i<lines.size();i++) {
				lineContent=lines.get(i).split(regex);
				if(lineContent.length==2) {
					nodeNum=Integer.parseInt(lineContent[0]);
					edgeNum=Integer.parseInt(lineContent[1]);
					
					edgePhy=new Edge[edgeNum];
					nodePhy=new Node[nodeNum];
					gPhy=new Graph(nodeNum);
					
//					Graph g=new Graph(nodeNum);
//					Edge[] edge=new Edge[edgeNum];
//					Node[] node=new Node[nodeNum];
				}else if(lineContent.length==3) {
					edgePhy[i-1]=new Edge(Integer.parseInt(lineContent[0]),Integer.parseInt(lineContent[1]),Integer.parseInt(lineContent[2]));
					
				}else {
					nodePhy[i-1-edgeNum]=new Node(Integer.parseInt(lineContent[0]));
				}	
			}
			CreateGraph.create(gPhy, nodePhy, nodeNum, edgePhy, edgeNum);
//			NodeRank nr=new NodeRank(g);
//			rank=nr.rank();
//			for(int i=0;i<rank.size();i++) {
//				System.out.println("key:"+rank.get(i).getKey()+"-value:"+rank.get(i).getValue());
//				
//			}
			/*虚拟网络topo处理*/
			Edge[] edgeVir={
					new Edge(0,1,6),
					new Edge(0,2,3),
					new Edge(1,2,2),
					new Edge(1,3,5),
					new Edge(3,2,3),
					new Edge(3,4,2),
					new Edge(2,4,4),
					new Edge(3,5,3),
					new Edge(4,5,5)};
			Node[] nodeVir={new Node(10),new Node(8),new Node(5),new Node(3),new Node(9),new Node(4)};
			Graph gVir=new Graph(nodeVir.length);
			CreateGraph.create(gVir, nodeVir, nodeVir.length, edgeVir, edgeVir.length);
			
			
			NodeRank nr=new NodeRank(gVir);
			rank=nr.rank();
			for(int i=0;i<rank.size();i++) {
				System.out.println("key:"+rank.get(i).getKey()+"-value:"+rank.get(i).getValue());
				
			}
			
			embOrder=EmbedOrder.embOrder(rank,gVir);
			
			
			for(Map.Entry<Integer, Double> entry:embOrder.entrySet()){
				System.out.println("main_key_emborder"+entry.getKey());
			}
			result=Embedding.embedding(embOrder, gVir, gPhy);
			
				
			for(Map.Entry<Integer, Integer> entry:result.entrySet()){
				System.out.println("最终对于VNR的映射结果："+entry.getKey()+"--"+entry.getValue());
			}
			
		}catch(Exception e) {
			System.out.println("FROME MAIN"+e);
		}
		
		

	}

}
