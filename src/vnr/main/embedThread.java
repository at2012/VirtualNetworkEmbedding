package vnr.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

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

	volatile int sucCount=0;
	volatile int failCount=0;
	volatile List<Double> avelength=new LinkedList<>();
	
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
//		LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//最终映射顺序
//		LinkedHashMap<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		
//		int sucCount=0;
//		int failCount=0;
		
		try {
//			BufferedReader phyReader=new BufferedReader(new FileReader("sub.txt"));
			Embedding embeder = new Embedding(".\\Result");
			
			String line;
			String regex=" ";
			String[] lineContent;
			List<String> lines=new ArrayList<String>();
			
			/*虚拟网络相关变量*/
			Edge[] edgeVir=null;
			Node[] nodeVir=null;
			Graph gVir=null;
			
			/*仿真结果验证相关变量*/
			List<Double> avelength=new LinkedList<>();//用于统计每个虚拟网络的平均链路长度，以便最后求总的平均长度
			double tolRevenue=0;
			
//			//网络映射相关变量：noderank
//			List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR

//			Logger.getGlobal().info("未映射物理网络拓扑：");
//			for(int x=0;x<gPhy.getNumOfNode();x++) {
//				for(int y=0;y<gPhy.getNumOfNode();y++) {
//					System.out.print(gPhy.getWeight(x, y)+"\t");
//				}
//				System.out.println();
//			}
			
			/*虚拟网络topo处理，把表示虚拟网络拓扑的文件存为Graph*/
			File demo = new File(folder);
			File[] vnrs=demo.listFiles();
			
//			BufferedReader virReader = null;
			

			for(int j=0;j<vnrs.length;j++) {

				BufferedReader virReader = new BufferedReader(new InputStreamReader(new FileInputStream(vnrs[j])));
				
				//网络映射相关变量：noderank
				List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR
				//这个语句本来也在上面，受到下面的启发，一起挪过来了
				
				int linkNumCount;//用于统计虚拟网络在物理网络上的平均路径长度
				
				double aveLength;//虚拟网路映射到物理网络后的平均链路长度
				
				LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//最终映射顺序
				LinkedHashMap<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
				//启发：本来这两个变量的定义是在上面，但是运行的时候出现数组越界异常，debug发现result的似乎不太对，所以怀疑是前面对变量的赋值影响了后面
				//试图调整变量定义的位置，使得每个虚拟网络请求都有自己的排序变量，所以放到for里面创建，果然不再出现异常
				
				
				lines.clear();
				while((line=virReader.readLine())!=null) {
					lines.add(line);
				}
				
				for(int i=0;i<lines.size();i++) {
					lineContent=lines.get(i).split(regex);
					if(lineContent.length==4) {
						nodeNumVir=Integer.parseInt(lineContent[0]);
						edgeNumVir=Integer.parseInt(lineContent[1]);
						
						edgeVir=new Edge[edgeNumVir];
						nodeVir=new Node[nodeNumVir];
						gVir=new Graph(nodeNumVir,Integer.parseInt(lineContent[3]));
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
//				for(int u=0;u<rank.size();u++) {
//					System.out.println(rank.get(u).getKey()+"-"+rank.get(u).getValue());
//				}
				embOrder=EmbedOrder.embOrder(rank,gVir);
				/*节点映射+链路映射*/
				switch (embeder.embedNode(embOrder, result, gVir, gPhy,j)) {
				case 1://节点映射成功则进行链路映射
//					switch (embeder.embLink(gVir, gPhy, result,j)) {
//					case 1://链路映射成功
//						sucCount++;
//						break;
//					case -1://等待补充:映射失败后除了统计失败数量,还需要把被这个失败映射占用的资源恢复回来
//						failCount++;
//						break;
//					default:
//						break;
//					}
					linkNumCount=embeder.embLink(gVir, gPhy, result,j);
					if (linkNumCount>0) {//如果虚拟网网络映射成功，统计该虚拟网络链路平均长度，同时统计映射成功数目,
						//还要打开相应文件的计时器,准备恢复该文件对应的资源
						avelength.add((double)linkNumCount/gVir.getNumOfEdge());//统计该网络链路平均映射长度
						/*统计映射收益*/
						System.out.println("映射成功"+j+"收益:"+gVir.getTolSource());
						System.out.println("映射成功"+j+"消耗：");
//						gVir.getTolSource();
//						aveLength=linkNumCount/gVir.getNumOfEdge();
//						System.out.println("平均长度"+aveLength);
						sucCount++;
						
						Timer timer=new Timer();
						timer.schedule(new recoverSourceTimerTask(gPhy, j), gVir.survivalTime);
						
						break;
					} else {
						//如果等于-1，链路映射失败。等待补充:映射失败后除了统计失败数量,还需要把被这个失败映射占用的资源恢复回来
						embeder.recoverSource(gPhy, j);
						failCount++;
					}
					break;
				case -1://等待补充:节点映射失败后除了统计失败数量,还需要把被这个失败映射占用的资源恢复回来
					
					
					
					failCount++;
				default:
					break;
				}
				System.out.println("成功映射数目："+sucCount);
				System.out.println("失败映射数目："+failCount);
				
				Thread.sleep(500);
				
			}
			
			
			double tolLength=0;
			for(int i=0;i<avelength.size();i++) {
				tolLength+=avelength.get(i);
			}
			System.out.println("映射平均长度："+tolLength/sucCount);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("FROM embedThread.java-"+e);
		}
	}
	
	/**用于在计时器中恢复到期虚拟网络占用的资源*/
	class recoverSourceTimerTask extends TimerTask{

		int n;
		Graph g;
		public recoverSourceTimerTask(Graph gphy,int fileNum) {
			n=fileNum;
			g=gphy;
		}
		
		
		@Override
		public void run() {
			File recoverFile = new File(".\\Result\\res"+n+".txt");
			BufferedReader failEmbedReader=null;
			try {
				failEmbedReader=new BufferedReader(new FileReader(recoverFile));
				String line;
				String regex=" ";
				String[] lineContent;
				List<String> lines=new LinkedList<String>();
				
				int nodeNum;
				int linkNum;
				
				while((line=failEmbedReader.readLine())!=null) {
					lines.add(line);
				}
				lineContent=lines.get(0).split(" ");
				nodeNum=Integer.parseInt(lineContent[0]);
				linkNum=Integer.parseInt(lineContent[1]);//改进：链路数量可嫩没用
				for(int i=1;i<lines.size();i++) {//第一行是节点数目、链路数，不需要处理
					lineContent=lines.get(i).split(regex);
//					if(lineContent.length==2) {
					if (i<=nodeNum) {//如果是节点信息，则把节点资源恢复给物理网络
						gPhy.setCpu(Integer.parseInt(lineContent[1])
								,gPhy.getCpu(Integer.parseInt(lineContent[1]))+Double.parseDouble(lineContent[2]));
					}else {
						int flag=3;//用于表示待恢复的物理链路，flag和flag+1表示一条链路
						double w=Double.parseDouble(lineContent[2]);
						while(flag<lineContent.length-1) {
							int s,d;
							s=Integer.parseInt(lineContent[flag]);
							d=Integer.parseInt(lineContent[flag+1]);
							gPhy.setWeight(s, d, gPhy.getWeight(s, d)+w);
							flag++;
						}
					}	
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e+"--embedding.java/recoversource");
			}finally {
				if(failEmbedReader != null) {
					try {
						failEmbedReader.close();
					} catch (IOException e) {
						System.out.println(e);
					}
					Logger.getGlobal().info("这里是资源恢复计时器--删除已经恢复文件"+n);
					
//					recoverFile.delete();
				}
			}
		}
		
	}

	/**用于在计时器中进行资源映射*/
	class embedTimerTask extends TimerTask{

		File[] vnrs;
		Embedding embeder;
		int i;
		/**
		 * 构造映射计时器任务
		 * @param v 把文件夹中文件数组传入
		 * @param e 把用于映射的变量传入
		 * @param j 表示当前处理的第j个网络请求*/
		public embedTimerTask(File[] v,Embedding e,int j) {
			vnrs=v;
			embeder=e;
			i=j;
		}
		
		@Override
		public void run() {
			String line;
			String regex=" ";
			String[] lineContent;
			List<String> lines=new ArrayList<String>();
			
			/*虚拟网络相关变量*/
			Edge[] edgeVir=null;
			Node[] nodeVir=null;
			Graph gVir=null;
			
			int nodeNumVir=-1,edgeNumVir=-1;
			
			try {
//				for(int j=0;j<vnrs.length;j++) {
					BufferedReader virReader = new BufferedReader(new InputStreamReader(new FileInputStream(vnrs[i])));
					
					//网络映射相关变量：noderank
					List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR
					//这个语句本来也在上面，受到下面的启发，一起挪过来了
					
					int linkNumCount;//用于统计虚拟网络在物理网络上的平均路径长度
					
					double aveLength;//虚拟网路映射到物理网络后的平均链路长度
					
					LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//最终映射顺序
					LinkedHashMap<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
					//启发：本来这两个变量的定义是在上面，但是运行的时候出现数组越界异常，debug发现result的似乎不太对，所以怀疑是前面对变量的赋值影响了后面
					//试图调整变量定义的位置，使得每个虚拟网络请求都有自己的排序变量，所以放到for里面创建，果然不再出现异常
					
					
					lines.clear();
					while((line=virReader.readLine())!=null) {
						lines.add(line);
					}
					
					for(int i=0;i<lines.size();i++) {
						lineContent=lines.get(i).split(regex);
						if(lineContent.length==4) {
							nodeNumVir=Integer.parseInt(lineContent[0]);
							edgeNumVir=Integer.parseInt(lineContent[1]);
							
							edgeVir=new Edge[edgeNumVir];
							nodeVir=new Node[nodeNumVir];
							gVir=new Graph(nodeNumVir,Integer.parseInt(lineContent[3]));
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
//					for(int u=0;u<rank.size();u++) {
//						System.out.println(rank.get(u).getKey()+"-"+rank.get(u).getValue());
//					}
					embOrder=EmbedOrder.embOrder(rank,gVir);
					/*节点映射+链路映射*/
					switch (embeder.embedNode(embOrder, result, gVir, gPhy,i)) {
					case 1://节点映射成功则进行链路映射
//						switch (embeder.embLink(gVir, gPhy, result,j)) {
//						case 1://链路映射成功
//							sucCount++;
//							break;
//						case -1://等待补充:映射失败后除了统计失败数量,还需要把被这个失败映射占用的资源恢复回来
//							failCount++;
//							break;
//						default:
//							break;
//						}
						linkNumCount=embeder.embLink(gVir, gPhy, result,i);
						if (linkNumCount>0) {//如果虚拟网网络映射成功，统计该虚拟网络链路平均长度，同时统计映射成功数目,
							//还要打开相应文件的计时器,准备恢复该文件对应的资源
							avelength.add((double)linkNumCount/gVir.getNumOfEdge());//统计该网络链路平均映射长度
							/*统计映射收益*/
							System.out.println("映射成功"+i+"收益:"+gVir.getTolSource());
//							gVir.getTolSource();
//							aveLength=linkNumCount/gVir.getNumOfEdge();
//							System.out.println("平均长度"+aveLength);
							sucCount++;
							
							Timer timer=new Timer();
							timer.schedule(new recoverSourceTimerTask(gPhy, i), gVir.survivalTime);
							
							break;
						} else {
							//如果等于-1，链路映射失败。等待补充:映射失败后除了统计失败数量,还需要把被这个失败映射占用的资源恢复回来

							embeder.recoverSource(gPhy, i);
							
							failCount++;
						}
						
						
						
						break;
					case -1://等待补充:节点映射失败后除了统计失败数量,还需要把被这个失败映射占用的资源恢复回来
						
						
						
						failCount++;
					default:
						break;
					}
					

					System.out.println("成功映射数目："+sucCount);
					System.out.println("失败映射数目："+failCount);
//				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
		
	}
	
}
