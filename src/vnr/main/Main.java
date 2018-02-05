package vnr.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import vnr.bandit.Embedding;
import vnr.graph.CreateGraph;
import vnr.graph.Edge;
import vnr.graph.Graph;
import vnr.graph.Node;
import vnr.rank.EmbedOrder;
import vnr.rank.NodeRank;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int nodeNumPhy=-1,edgeNumPhy=-1;
		int nodeNumVir=-1,edgeNumVir=-1;//改进：这里好像用不到两个变量，物理网络和虚拟网络可以用同一对变量。
		LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//最终映射顺序
		LinkedHashMap<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		
		
		
		try {
			BufferedReader phyReader=new BufferedReader(new FileReader("sub_case.txt"));
			Embedding embeder = new Embedding(".\\Result");
			
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
			
			/*物理网络topo处理，把表示物理网络拓扑的文件存为Graph*/
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
				}else if(lineContent.length==1) {
					nodePhy[i-1]=new Node(Double.parseDouble(lineContent[0]));
				}else {
					edgePhy[i-1-nodeNumPhy]=new Edge(Integer.parseInt(lineContent[0]),Integer.parseInt(lineContent[1]),
							Double.parseDouble(lineContent[2]));
				}	
			}
			CreateGraph.create(gPhy, nodePhy, nodeNumPhy, edgePhy, edgeNumPhy);
			

			
			System.out.println("物理网络拓扑：");
			for(int x=0;x<nodeNumPhy;x++) {
				for(int y=0;y<nodeNumPhy;y++) {
					System.out.print(gPhy.getWeight(x, y)+"\t");
				}
				System.out.println();
			}
			

			
			/*虚拟网络topo处理，把表示虚拟网络拓扑的文件存为Graph*/
			File demo = new File(".\\Demo");
			File[] vnrs=demo.listFiles();
			
			for(int j=0;j<vnrs.length;j++) {
				BufferedReader virReader = new BufferedReader(new InputStreamReader(new FileInputStream(vnrs[j])));
				
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
					}else if(lineContent.length==1) {
						nodeVir[i-1]=new Node(Double.parseDouble(lineContent[0]));
					}else {
						edgeVir[i-1-nodeNumVir]=new Edge(Integer.parseInt(lineContent[0]),Integer.parseInt(lineContent[1]),
								Double.parseDouble(lineContent[2]));
					}	
				}
				CreateGraph.create(gVir, nodeVir, nodeNumVir, edgeVir, edgeNumVir);
				
				

				
				
				/*计算节点排名以及映射顺序*/
				NodeRank nr=new NodeRank(gVir);
				rank=nr.rank();
				embOrder=EmbedOrder.embOrder(rank,gVir);
				/*节点映射+链路映射*/
				result=embeder.embedding(embOrder, gVir, gPhy,j);
				embeder.embLink(gVir, gPhy, result,j);
				
				
				System.out.println("物理网络拓扑：");
				for(int x=0;x<nodeNumPhy;x++) {
					for(int y=0;y<nodeNumPhy;y++) {
						System.out.print(gPhy.getWeight(x, y)+"\t");
					}
					System.out.println();
				}
				
			}
			
			//==================================================================================================================
			/*资源恢复*/
			File resFolder = new File(".\\Result");
			File[] res=resFolder.listFiles();
						
			for(int i=0;i<res.length;i++) {
				BufferedReader resReader = new BufferedReader(new InputStreamReader(new FileInputStream(res[i])));
				int nn=-1;
				int ln=-1;//nn和ln分别用于表示网络节点数量和链路数量,改进,ln是一个没有被用到的变量
				
				
//				BufferedWriter test = new BufferedWriter(new FileWriter("test"+i+".txt"));
				
				lines.clear();
				while((line=resReader.readLine())!=null) {
					lines.add(line);
				}

				for(int j=0;j<lines.size();j++) {
					lineContent=lines.get(j).split(regex);
					
					if(j==0) {
						//记录节点数量和边数量
						nn=Integer.parseInt(lineContent[0]);
						ln=Integer.parseInt(lineContent[1]);
					}else if(j>0 && j<=nn) {
						//恢复节点资源
						int id=Integer.parseInt(lineContent[1]);//物理节点id
						double c=Double.parseDouble(lineContent[2]);//等待恢复的资源值
						gPhy.setCpu(id,gPhy.getCpu(id)+c);
					}else {
						//恢复链路资源
						double w;//带宽资源
						int l,n;//分别表示一段链路的起始和终止
						w=Double.parseDouble(lineContent[2]);
					
						for(int x=3;x<lineContent.length-1;x++) {
							l=Integer.parseInt(lineContent[x]);
							n=Integer.parseInt(lineContent[x+1]);
							gPhy.setWeight(l, n, gPhy.getWeight(l, n)+w);
						}
					}
				}
				
				/*test*/
				System.out.println("物理网络拓扑：");
				for(int x=0;x<nodeNumPhy;x++) {
					for(int y=0;y<nodeNumPhy;y++) {
						System.out.print(gPhy.getWeight(x, y)+"\t");
					}
					System.out.println();
				}
				
				for(int x=0;x<nodeNumPhy;x++) {
					
					System.out.println(gPhy.getCpu(x));
				}
				
				
//				for(int x=0;x<nodeNumPhy;x++) {
//					for(int y=0;y<nodeNumPhy;y++) {
//						test.write(gPhy.getWeight(x, y)+"\t");
//					}
//					test.write("\r\n");
//				}
//				
//				test.close();
				
				
//				System.out.println("物理网络拓扑：");
//				for(int x=0;x<nodeNumPhy;x++) {
//					for(int y=0;y<nodeNumPhy;y++) {
//						System.out.print(gPhy.getWeight(x, y)+"\t");
//					}
//					System.out.println();
//				}
//				
				
				
			}

			
		}catch(Exception e) {
			System.out.println("FROME MAIN:"+e);
		}
	}

}
