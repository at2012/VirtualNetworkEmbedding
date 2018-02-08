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
		
		
		try {
			BufferedReader phyReader=new BufferedReader(new FileReader("sub.txt"));
//			Embedding embeder = new Embedding(".\\Result");
			
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
			
//			/*虚拟网络相关变量*/
//			Edge[] edgeVir=null;
//			Node[] nodeVir=null;
//			Graph gVir=null;
			
			//网络映射相关变量：noderank
			//List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR
			
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
			
			
			System.out.println("测试映射线程！！！！");
			Thread emThread = new Thread(new embedThread(".\\topo-500-5-10-0", gPhy));
			emThread.start();
//			emThread.sleep(1000);
//			
			
//			System.out.println("测试恢复线程！！！！");
//			Thread reThread = new Thread(new recoverThread(".\\Result", gPhy));
//			reThread.start();
			

			
		}catch(Exception e) {
			System.out.println("FROME MAIN:"+e);
		}
	}

}
