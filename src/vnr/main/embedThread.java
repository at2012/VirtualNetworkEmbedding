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
	 * @param folderName �洢VNR���ļ���
	 * @param g ��ӳ���������������
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
		int nodeNumVir=-1,edgeNumVir=-1;//�Ľ�����������ò�����������������������������������ͬһ�Ա�����
//		LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//����ӳ��˳��
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
			
			/*����������ر���*/
			Edge[] edgeVir=null;
			Node[] nodeVir=null;
			Graph gVir=null;
			
			/*��������֤��ر���*/
			List<Double> avelength=new LinkedList<>();//����ͳ��ÿ�����������ƽ����·���ȣ��Ա�������ܵ�ƽ������
			double tolRevenue=0;
			
//			//����ӳ����ر�����noderank
//			List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR

//			Logger.getGlobal().info("δӳ�������������ˣ�");
//			for(int x=0;x<gPhy.getNumOfNode();x++) {
//				for(int y=0;y<gPhy.getNumOfNode();y++) {
//					System.out.print(gPhy.getWeight(x, y)+"\t");
//				}
//				System.out.println();
//			}
			
			/*��������topo�����ѱ�ʾ�����������˵��ļ���ΪGraph*/
			File demo = new File(folder);
			File[] vnrs=demo.listFiles();
			
//			BufferedReader virReader = null;
			

			for(int j=0;j<vnrs.length;j++) {

				BufferedReader virReader = new BufferedReader(new InputStreamReader(new FileInputStream(vnrs[j])));
				
				//����ӳ����ر�����noderank
				List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR
				//�����䱾��Ҳ�����棬�ܵ������������һ��Ų������
				
				int linkNumCount;//����ͳ���������������������ϵ�ƽ��·������
				
				double aveLength;//������·ӳ�䵽����������ƽ����·����
				
				LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//����ӳ��˳��
				LinkedHashMap<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
				//���������������������Ķ����������棬�������е�ʱ���������Խ���쳣��debug����result���ƺ���̫�ԣ����Ի�����ǰ��Ա����ĸ�ֵӰ���˺���
				//��ͼ�������������λ�ã�ʹ��ÿ�����������������Լ���������������Էŵ�for���洴������Ȼ���ٳ����쳣
				
				
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
				
				/*����ڵ������Լ�ӳ��˳��*/
				NodeRank nr=new NodeRank(gVir);
				rank=nr.rank();  
//				for(int u=0;u<rank.size();u++) {
//					System.out.println(rank.get(u).getKey()+"-"+rank.get(u).getValue());
//				}
				embOrder=EmbedOrder.embOrder(rank,gVir);
				/*�ڵ�ӳ��+��·ӳ��*/
				switch (embeder.embedNode(embOrder, result, gVir, gPhy,j)) {
				case 1://�ڵ�ӳ��ɹ��������·ӳ��
//					switch (embeder.embLink(gVir, gPhy, result,j)) {
//					case 1://��·ӳ��ɹ�
//						sucCount++;
//						break;
//					case -1://�ȴ�����:ӳ��ʧ�ܺ����ͳ��ʧ������,����Ҫ�ѱ����ʧ��ӳ��ռ�õ���Դ�ָ�����
//						failCount++;
//						break;
//					default:
//						break;
//					}
					linkNumCount=embeder.embLink(gVir, gPhy, result,j);
					if (linkNumCount>0) {//�������������ӳ��ɹ���ͳ�Ƹ�����������·ƽ�����ȣ�ͬʱͳ��ӳ��ɹ���Ŀ,
						//��Ҫ����Ӧ�ļ��ļ�ʱ��,׼���ָ����ļ���Ӧ����Դ
						avelength.add((double)linkNumCount/gVir.getNumOfEdge());//ͳ�Ƹ�������·ƽ��ӳ�䳤��
						/*ͳ��ӳ������*/
						System.out.println("ӳ��ɹ�"+j+"����:"+gVir.getTolSource());
						System.out.println("ӳ��ɹ�"+j+"���ģ�");
//						gVir.getTolSource();
//						aveLength=linkNumCount/gVir.getNumOfEdge();
//						System.out.println("ƽ������"+aveLength);
						sucCount++;
						
						Timer timer=new Timer();
						timer.schedule(new recoverSourceTimerTask(gPhy, j), gVir.survivalTime);
						
						break;
					} else {
						//�������-1����·ӳ��ʧ�ܡ��ȴ�����:ӳ��ʧ�ܺ����ͳ��ʧ������,����Ҫ�ѱ����ʧ��ӳ��ռ�õ���Դ�ָ�����
						embeder.recoverSource(gPhy, j);
						failCount++;
					}
					break;
				case -1://�ȴ�����:�ڵ�ӳ��ʧ�ܺ����ͳ��ʧ������,����Ҫ�ѱ����ʧ��ӳ��ռ�õ���Դ�ָ�����
					
					
					
					failCount++;
				default:
					break;
				}
				System.out.println("�ɹ�ӳ����Ŀ��"+sucCount);
				System.out.println("ʧ��ӳ����Ŀ��"+failCount);
				
				Thread.sleep(500);
				
			}
			
			
			double tolLength=0;
			for(int i=0;i<avelength.size();i++) {
				tolLength+=avelength.get(i);
			}
			System.out.println("ӳ��ƽ�����ȣ�"+tolLength/sucCount);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("FROM embedThread.java-"+e);
		}
	}
	
	/**�����ڼ�ʱ���лָ�������������ռ�õ���Դ*/
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
				linkNum=Integer.parseInt(lineContent[1]);//�Ľ�����·��������û��
				for(int i=1;i<lines.size();i++) {//��һ���ǽڵ���Ŀ����·��������Ҫ����
					lineContent=lines.get(i).split(regex);
//					if(lineContent.length==2) {
					if (i<=nodeNum) {//����ǽڵ���Ϣ����ѽڵ���Դ�ָ�����������
						gPhy.setCpu(Integer.parseInt(lineContent[1])
								,gPhy.getCpu(Integer.parseInt(lineContent[1]))+Double.parseDouble(lineContent[2]));
					}else {
						int flag=3;//���ڱ�ʾ���ָ���������·��flag��flag+1��ʾһ����·
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
					Logger.getGlobal().info("��������Դ�ָ���ʱ��--ɾ���Ѿ��ָ��ļ�"+n);
					
//					recoverFile.delete();
				}
			}
		}
		
	}

	/**�����ڼ�ʱ���н�����Դӳ��*/
	class embedTimerTask extends TimerTask{

		File[] vnrs;
		Embedding embeder;
		int i;
		/**
		 * ����ӳ���ʱ������
		 * @param v ���ļ������ļ����鴫��
		 * @param e ������ӳ��ı�������
		 * @param j ��ʾ��ǰ����ĵ�j����������*/
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
			
			/*����������ر���*/
			Edge[] edgeVir=null;
			Node[] nodeVir=null;
			Graph gVir=null;
			
			int nodeNumVir=-1,edgeNumVir=-1;
			
			try {
//				for(int j=0;j<vnrs.length;j++) {
					BufferedReader virReader = new BufferedReader(new InputStreamReader(new FileInputStream(vnrs[i])));
					
					//����ӳ����ر�����noderank
					List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR
					//�����䱾��Ҳ�����棬�ܵ������������һ��Ų������
					
					int linkNumCount;//����ͳ���������������������ϵ�ƽ��·������
					
					double aveLength;//������·ӳ�䵽����������ƽ����·����
					
					LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//����ӳ��˳��
					LinkedHashMap<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
					//���������������������Ķ����������棬�������е�ʱ���������Խ���쳣��debug����result���ƺ���̫�ԣ����Ի�����ǰ��Ա����ĸ�ֵӰ���˺���
					//��ͼ�������������λ�ã�ʹ��ÿ�����������������Լ���������������Էŵ�for���洴������Ȼ���ٳ����쳣
					
					
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
					
					/*����ڵ������Լ�ӳ��˳��*/
					NodeRank nr=new NodeRank(gVir);
					rank=nr.rank();  
//					for(int u=0;u<rank.size();u++) {
//						System.out.println(rank.get(u).getKey()+"-"+rank.get(u).getValue());
//					}
					embOrder=EmbedOrder.embOrder(rank,gVir);
					/*�ڵ�ӳ��+��·ӳ��*/
					switch (embeder.embedNode(embOrder, result, gVir, gPhy,i)) {
					case 1://�ڵ�ӳ��ɹ��������·ӳ��
//						switch (embeder.embLink(gVir, gPhy, result,j)) {
//						case 1://��·ӳ��ɹ�
//							sucCount++;
//							break;
//						case -1://�ȴ�����:ӳ��ʧ�ܺ����ͳ��ʧ������,����Ҫ�ѱ����ʧ��ӳ��ռ�õ���Դ�ָ�����
//							failCount++;
//							break;
//						default:
//							break;
//						}
						linkNumCount=embeder.embLink(gVir, gPhy, result,i);
						if (linkNumCount>0) {//�������������ӳ��ɹ���ͳ�Ƹ�����������·ƽ�����ȣ�ͬʱͳ��ӳ��ɹ���Ŀ,
							//��Ҫ����Ӧ�ļ��ļ�ʱ��,׼���ָ����ļ���Ӧ����Դ
							avelength.add((double)linkNumCount/gVir.getNumOfEdge());//ͳ�Ƹ�������·ƽ��ӳ�䳤��
							/*ͳ��ӳ������*/
							System.out.println("ӳ��ɹ�"+i+"����:"+gVir.getTolSource());
//							gVir.getTolSource();
//							aveLength=linkNumCount/gVir.getNumOfEdge();
//							System.out.println("ƽ������"+aveLength);
							sucCount++;
							
							Timer timer=new Timer();
							timer.schedule(new recoverSourceTimerTask(gPhy, i), gVir.survivalTime);
							
							break;
						} else {
							//�������-1����·ӳ��ʧ�ܡ��ȴ�����:ӳ��ʧ�ܺ����ͳ��ʧ������,����Ҫ�ѱ����ʧ��ӳ��ռ�õ���Դ�ָ�����

							embeder.recoverSource(gPhy, i);
							
							failCount++;
						}
						
						
						
						break;
					case -1://�ȴ�����:�ڵ�ӳ��ʧ�ܺ����ͳ��ʧ������,����Ҫ�ѱ����ʧ��ӳ��ռ�õ���Դ�ָ�����
						
						
						
						failCount++;
					default:
						break;
					}
					

					System.out.println("�ɹ�ӳ����Ŀ��"+sucCount);
					System.out.println("ʧ��ӳ����Ŀ��"+failCount);
//				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
		
	}
	
}
