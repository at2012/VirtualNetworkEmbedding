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
		LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//����ӳ��˳��
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
			
			/*����������ر���*/
			Edge[] edgeVir=null;
			Node[] nodeVir=null;
			Graph gVir=null;
			
			//����ӳ����ر�����noderank
			List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR
			
//			System.out.println("δӳ�������������ˣ�");
//			for(int x=0;x<gPhy.getNumOfNode();x++) {
//				for(int y=0;y<gPhy.getNumOfNode();y++) {
//					System.out.print(gPhy.getWeight(x, y)+"\t");
//				}
//				System.out.println();
//			}
			
			/*��������topo�����ѱ�ʾ�����������˵��ļ���ΪGraph*/
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
				
				/*����ڵ������Լ�ӳ��˳��*/
				NodeRank nr=new NodeRank(gVir);
				rank=nr.rank();
				embOrder=EmbedOrder.embOrder(rank,gVir);
				/*�ڵ�ӳ��+��·ӳ��*/
				
				switch (embeder.embedding(embOrder, result, gVir, gPhy,j)) {
				case 1://�ڵ�ӳ��ɹ��������·ӳ��
					switch (embeder.embLink(gVir, gPhy, result,j)) {
					case 1://��·ӳ��ɹ�
						sucCount++;
						break;
					case -1://�ȴ�����:ӳ��ʧ�ܺ����ͳ��ʧ������,����Ҫ�ѱ����ʧ��ӳ��ռ�õ���Դ�ָ�����
						failCount++;
						
					default:
						break;
					}
					break;
				case -1://�ȴ�����:ӳ��ʧ�ܺ����ͳ��ʧ������,����Ҫ�ѱ����ʧ��ӳ��ռ�õ���Դ�ָ�����
					failCount++;
				default:
					break;
				}
				
//				System.out.println("ӳ��������������ˣ�");
//				for(int x=0;x<gPhy.getNumOfNode();x++) {
//					for(int y=0;y<gPhy.getNumOfNode();y++) {
//						System.out.print(gPhy.getWeight(x, y)+"\t");
//					}
//					System.out.println();
//				}
				System.out.println("�ɹ�ӳ����Ŀ��"+sucCount);
				System.out.println("ʧ��ӳ����Ŀ��"+failCount);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("FROM embedThread.java-"+e);
		}
	}

}
