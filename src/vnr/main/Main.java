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
		
		int nodeNumPhy=-1,edgeNumPhy=-1;
		int nodeNumVir=-1,edgeNumVir=-1;//改进：这里好像用不到两个变量，物理网络和虚拟网络可以用同一对变量。
		LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//最终映射顺序
		LinkedHashMap<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		
		try {
			
			/*物理网络topo处理*/
			BufferedReader phyReader=new BufferedReader(new FileReader("case0_sub.txt"));
			BufferedReader virReader=new BufferedReader(new FileReader("case0_vir.txt"));
			String line;
			String regex=" ";
			String[] lineContent;
			List<String> lines=new LinkedList<String>();
			
			/*物理网络相关变量*/
			Edge[] edgePhy=null;//启发：由于创建在另外的块中，所以在另外的块中使用的时候出现未被初始化的错误。所以在这里用的null，
			//虽然知道在运行过程中可以被正确定义，但是还是担心----启发：后面处理拓扑图的时候用到edge，但是定义在try里面，
			//所以不能用，错误同上一个启发。
			Node[] nodePhy=null;
			Graph gPhy=null;//启发：之前edge\node\g的定义以及创建都被定义在了if里面，由于变量的定义在if块厘米，
			//使用在else块里面，所以使用的时候报错：变量未被定义。所以把定义放在了这里，创建再放在if里面，应该么有问题了吧
			
			/*虚拟网络相关变量*/
			Edge[] edgeVir=null;
			Node[] nodeVir=null;
			Graph gVir=null;
			
			//网络映射相关变量：noderank
			List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR
			
			/*物理网络topo处理*/
			while((line=phyReader.readLine())!=null) {
				lines.add(line);
			}
			
			for(int i=0;i<lines.size();i++) {
				lineContent=lines.get(i).split(regex);
				if(lineContent.length==2) {
					nodeNumPhy=Integer.parseInt(lineContent[0]);
					edgeNumPhy=Integer.parseInt(lineContent[1]);
					
					edgePhy=new Edge[edgeNumPhy];
					nodePhy=new Node[nodeNumPhy];
					gPhy=new Graph(nodeNumPhy);
				}else if(lineContent.length==3) {
					edgePhy[i-1]=new Edge(Integer.parseInt(lineContent[0]),Integer.parseInt(lineContent[1]),
							Integer.parseInt(lineContent[2]));
					
				}else {
					nodePhy[i-1-edgeNumPhy]=new Node(Integer.parseInt(lineContent[0]));
				}	
			}
			CreateGraph.create(gPhy, nodePhy, nodeNumPhy, edgePhy, edgeNumPhy);
			
			/*test:*/
			System.out.println("物理网络拓扑：");
			for(int i=0;i<nodeNumPhy;i++) {
				for(int j=0;j<nodeNumPhy;j++) {
					System.out.print(gPhy.getWeight(i, j)+"\t");
				}
				System.out.println();
			}
			
			/*虚拟网络topo处理*/
			lines.clear();
			while((line=virReader.readLine())!=null) {
				lines.add(line);
			}
			
			for(int i=0;i<lines.size();i++) {
				lineContent=lines.get(i).split(regex);
				if(lineContent.length==2) {
					nodeNumVir=Integer.parseInt(lineContent[0]);
					edgeNumVir=Integer.parseInt(lineContent[1]);
					
					edgeVir=new Edge[edgeNumVir];
					nodeVir=new Node[nodeNumVir];
					gVir=new Graph(nodeNumVir);
				}else if(lineContent.length==3) {
					edgeVir[i-1]=new Edge(Integer.parseInt(lineContent[0]),Integer.parseInt(lineContent[1]),
							Integer.parseInt(lineContent[2]));
					
				}else {
					nodeVir[i-1-edgeNumVir]=new Node(Integer.parseInt(lineContent[0]));
				}	
			}
			CreateGraph.create(gVir, nodeVir, nodeNumVir, edgeVir, edgeNumVir);
			
			/*test:*/
			System.out.println("虚拟网络拓扑：");
			for(int i=0;i<nodeNumVir;i++) {
				for(int j=0;j<nodeNumVir;j++) {
					System.out.print(gVir.getWeight(i, j)+"\t");
				}
				System.out.println();
			}
			
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
			System.out.println("FROME MAIN:"+e);
		}
	}

}
