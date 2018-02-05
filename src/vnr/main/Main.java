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
		int nodeNumVir=-1,edgeNumVir=-1;//�Ľ�����������ò�����������������������������������ͬһ�Ա�����
		LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//����ӳ��˳��
		LinkedHashMap<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		
		
		
		try {
			BufferedReader phyReader=new BufferedReader(new FileReader("sub_case.txt"));
			Embedding embeder = new Embedding(".\\Result");
			
			String line;
			String regex=" ";
			String[] lineContent;
			List<String> lines=new LinkedList<String>();
			
			/*����������ر���*/
			Edge[] edgePhy=null;//���������ڴ���������Ŀ��У�����������Ŀ���ʹ�õ�ʱ�����δ����ʼ���Ĵ��������������õ�null��
			//��Ȼ֪�������й����п��Ա���ȷ���壬���ǻ��ǵ���----���������洦������ͼ��ʱ���õ�edge�����Ƕ�����try���棬
			//���Բ����ã�����ͬ��һ��������
			Node[] nodePhy=null;
			Graph gPhy=null;//������֮ǰedge\node\g�Ķ����Լ�����������������if���棬���ڱ����Ķ�����if�����ף�
			//ʹ����else�����棬����ʹ�õ�ʱ�򱨴�����δ�����塣���԰Ѷ����������������ٷ���if���棬Ӧ��ô�������˰�
			
			/*����������ر���*/
			Edge[] edgeVir=null;
			Node[] nodeVir=null;
			Graph gVir=null;
			
			//����ӳ����ر�����noderank
			List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR
			
			/*��������topo�����ѱ�ʾ�����������˵��ļ���ΪGraph*/
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
			

			
			System.out.println("�����������ˣ�");
			for(int x=0;x<nodeNumPhy;x++) {
				for(int y=0;y<nodeNumPhy;y++) {
					System.out.print(gPhy.getWeight(x, y)+"\t");
				}
				System.out.println();
			}
			

			
			/*��������topo�����ѱ�ʾ�����������˵��ļ���ΪGraph*/
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
				
				

				
				
				/*����ڵ������Լ�ӳ��˳��*/
				NodeRank nr=new NodeRank(gVir);
				rank=nr.rank();
				embOrder=EmbedOrder.embOrder(rank,gVir);
				/*�ڵ�ӳ��+��·ӳ��*/
				result=embeder.embedding(embOrder, gVir, gPhy,j);
				embeder.embLink(gVir, gPhy, result,j);
				
				
				System.out.println("�����������ˣ�");
				for(int x=0;x<nodeNumPhy;x++) {
					for(int y=0;y<nodeNumPhy;y++) {
						System.out.print(gPhy.getWeight(x, y)+"\t");
					}
					System.out.println();
				}
				
			}
			
			//==================================================================================================================
			/*��Դ�ָ�*/
			File resFolder = new File(".\\Result");
			File[] res=resFolder.listFiles();
						
			for(int i=0;i<res.length;i++) {
				BufferedReader resReader = new BufferedReader(new InputStreamReader(new FileInputStream(res[i])));
				int nn=-1;
				int ln=-1;//nn��ln�ֱ����ڱ�ʾ����ڵ���������·����,�Ľ�,ln��һ��û�б��õ��ı���
				
				
//				BufferedWriter test = new BufferedWriter(new FileWriter("test"+i+".txt"));
				
				lines.clear();
				while((line=resReader.readLine())!=null) {
					lines.add(line);
				}

				for(int j=0;j<lines.size();j++) {
					lineContent=lines.get(j).split(regex);
					
					if(j==0) {
						//��¼�ڵ������ͱ�����
						nn=Integer.parseInt(lineContent[0]);
						ln=Integer.parseInt(lineContent[1]);
					}else if(j>0 && j<=nn) {
						//�ָ��ڵ���Դ
						int id=Integer.parseInt(lineContent[1]);//����ڵ�id
						double c=Double.parseDouble(lineContent[2]);//�ȴ��ָ�����Դֵ
						gPhy.setCpu(id,gPhy.getCpu(id)+c);
					}else {
						//�ָ���·��Դ
						double w;//������Դ
						int l,n;//�ֱ��ʾһ����·����ʼ����ֹ
						w=Double.parseDouble(lineContent[2]);
					
						for(int x=3;x<lineContent.length-1;x++) {
							l=Integer.parseInt(lineContent[x]);
							n=Integer.parseInt(lineContent[x+1]);
							gPhy.setWeight(l, n, gPhy.getWeight(l, n)+w);
						}
					}
				}
				
				/*test*/
				System.out.println("�����������ˣ�");
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
				
				
//				System.out.println("�����������ˣ�");
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
