package vnr.main;

import java.awt.GraphicsConfigTemplate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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

public class embedThread implements Runnable {

	String folder;
	Graph gPhy;

	/**
	 * @param folderName 存储VNR的文件夹
	 * @param g 待映射的物理网络拓扑
	 * */
	public embedThread(String folderName,Graph g) {
		// TODO Auto-generated constructor stub
		folder=folderName;
		gPhy=g;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int nodeNumPhy=-1,edgeNumPhy=-1;
		int nodeNumVir=-1,edgeNumVir=-1;//改进：这里好像用不到两个变量，物理网络和虚拟网络可以用同一对变量。
		LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//最终映射顺序
		LinkedHashMap<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		
		int sucCount=0;
		int failCount=0;
		
		try {
//			BufferedReader phyReader=new BufferedReader(new FileReader("sub.txt"));
			Embedding embeder = new Embedding(".\\Result");
			
			String line;
			String regex=" ";
			String[] lineContent;
			List<String> lines=new LinkedList<String>();
			
			/*虚拟网络相关变量*/
			Edge[] edgeVir=null;
			Node[] nodeVir=null;
			Graph gVir=null;
			
			//网络映射相关变量：noderank
			List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR
			
//			System.out.println("未映射物理网络拓扑：");
//			for(int x=0;x<gPhy.getNumOfNode();x++) {
//				for(int y=0;y<gPhy.getNumOfNode();y++) {
//					System.out.print(gPhy.getWeight(x, y)+"\t");
//				}
//				System.out.println();
//			}
			
			/*虚拟网络topo处理，把表示虚拟网络拓扑的文件存为Graph*/
			File demo = new File(folder);
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
				
				switch (embeder.embedding(embOrder, result, gVir, gPhy,j)) {
				case 1://节点映射成功则进行链路映射
					switch (embeder.embLink(gVir, gPhy, result,j)) {
					case 1://链路映射成功
						sucCount++;
						break;
					case -1://等待补充:映射失败后除了统计失败数量,还需要把被这个失败映射占用的资源恢复回来
						failCount++;
						
					default:
						break;
					}
					break;
				case -1://等待补充:映射失败后除了统计失败数量,还需要把被这个失败映射占用的资源恢复回来
					failCount++;
				default:
					break;
				}
				
//				System.out.println("映射后物理网络拓扑：");
//				for(int x=0;x<gPhy.getNumOfNode();x++) {
//					for(int y=0;y<gPhy.getNumOfNode();y++) {
//						System.out.print(gPhy.getWeight(x, y)+"\t");
//					}
//					System.out.println();
//				}
				System.out.println("成功映射数目："+sucCount);
				System.out.println("失败映射数目："+failCount);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("FROM embedThread.java-"+e);
		}
	}

}
