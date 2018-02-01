package vnr.main;

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

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int nodeNumPhy=-1,edgeNumPhy=-1;
		int nodeNumVir=-1,edgeNumVir=-1;//�Ľ�����������ò�����������������������������������ͬһ�Ա�����
		LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();//����ӳ��˳��
		LinkedHashMap<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		
		try {
			
			BufferedReader phyReader=new BufferedReader(new FileReader("case0_sub.txt"));
			

			BufferedReader virReader=new BufferedReader(new FileReader("case0_vir.txt"));
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
					nodePhy[i-1-edgeNumPhy]=new Node(Integer.parseInt(lineContent[0]));
				}else {
					edgePhy[i-1]=new Edge(Integer.parseInt(lineContent[0]),Integer.parseInt(lineContent[1]),
							Integer.parseInt(lineContent[2]));
				}	
			}
			CreateGraph.create(gPhy, nodePhy, nodeNumPhy, edgePhy, edgeNumPhy);
			
//			/*test:*/
//			System.out.println("�����������ˣ�");
//			for(int i=0;i<nodeNumPhy;i++) {
//				for(int j=0;j<nodeNumPhy;j++) {
//					System.out.print(gPhy.getWeight(i, j)+"\t");
//				}
//				System.out.println();
//			}
			
			/*��������topo����*/
			File demo = new File(".\\demo");
			File[] vnrs=demo.listFiles();
			
			for(int j=0;j<vnrs.length;j++) {
				
				BufferedReader vnReader = new BufferedReader(new InputStreamReader(new FileInputStream(vnrs[j])));
//				InputStreamReader vnReader = new InputStreamReader(new FileInputStream(vnrs[i])) ;
//				BufferedReader vnFilesReader = new BufferedReader(vnReader);
				
				lines.clear();
				while((line=vnReader.readLine())!=null) {
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
						nodeVir[i-1-edgeNumVir]=new Node(Integer.parseInt(lineContent[0]));
					}else {
						edgeVir[i-1]=new Edge(Integer.parseInt(lineContent[0]),Integer.parseInt(lineContent[1]),
								Integer.parseInt(lineContent[2]));
						
					}	
				}
				CreateGraph.create(gVir, nodeVir, nodeNumVir, edgeVir, edgeNumVir);
				
				/*test:*/
				System.out.println("�����������ˣ�");
				for(int m=0;m<nodeNumVir;m++) {
					for(int n=0;n<nodeNumVir;n++) {
						System.out.print(gVir.getWeight(m, n)+"\t");
					}
					System.out.println();
				}
				
				System.out.println("��һ������");
			}
			
			NodeRank nr=new NodeRank(gVir);
			rank=nr.rank();
//			for(int i=0;i<rank.size();i++) {
//				System.out.println("from main:"+"������Ϣ��"+"key:"+rank.get(i).getKey()+"-value:"+rank.get(i).getValue());
//			}
			System.out.println("from main--����ӳ��˳��:");
			embOrder=EmbedOrder.embOrder(rank,gVir);
			for(Map.Entry<Integer, Double> entry:embOrder.entrySet()){
				System.out.println(entry.getKey());
			}
			result=Embedding.embedding(embOrder, gVir, gPhy);
			
				
			for(Map.Entry<Integer, Integer> entry:result.entrySet()){
				System.out.println("���ն���VNR��ӳ������"+entry.getKey()+"--"+entry.getValue());
			}
			
		}catch(Exception e) {
			System.out.println("FROME MAIN:"+e);
		}
	}

}
