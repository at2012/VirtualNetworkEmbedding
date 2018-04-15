package vnr.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import vnr.graph.CreateGraph;
import vnr.graph.Edge;
import vnr.graph.Graph;
import vnr.graph.Node;

public class Main {

	
	static volatile Graph gPhy=null;
	
	
	public static void main(String[] args) {
		int nodeNumPhy=-1,edgeNumPhy=-1;
		
		
		try {
			BufferedReader phyReader=new BufferedReader(new FileReader("sub.txt"));
//			Embedding embeder = new Embedding(".\\Result");
			
			String line;
			String regex=" ";
			String[] lineContent;
			List<String> lines=new LinkedList<String>();
			
			/*����������ر���*/
			Edge[] edgePhy=null;//���������ڴ���������Ŀ��У�����������Ŀ���ʹ�õ�ʱ�����δ����ʼ���Ĵ��������������õ�null��
			//��Ȼ֪�������й����п��Ա���ȷ���壬���ǻ��ǵ���----���������洦������ͼ��ʱ���õ�edge�����Ƕ�����try���棬
			//���Բ����ã�����ͬ��һ��������
			Node[] nodePhy=null;
//			Graph gPhy=null;//������֮ǰedge\node\g�Ķ����Լ�����������������if���棬���ڱ����Ķ�����if�����ף�
			
			
			//ʹ����else�����棬����ʹ�õ�ʱ�򱨴�����δ�����塣���԰Ѷ����������������ٷ���if���棬Ӧ��ô�������˰�
			
//			/*����������ر���*/
//			Edge[] edgeVir=null;
//			Node[] nodeVir=null;
//			Graph gVir=null;
			
			//����ӳ����ر�����noderank
			//List<Map.Entry<Integer,Double>> rank=new ArrayList<Map.Entry<Integer,Double>>();//VNR
			
			/*��������topo�����ѱ�ʾ�����������˵��ļ���ΪGraph*/
			while((line=phyReader.readLine())!=null) {
				lines.add(line);
//				System.out.println();
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
			
			Logger.getGlobal().info("��ʼӳ��");
			Thread emThread = new Thread(new embedThread(".\\requests-500-9-10-10", gPhy));
//			emThread.start();
			emThread.run();
			
			
			
//			emThread.sleep(1000);
//			
			
//			System.out.println("���Իָ��̣߳�������");
//			Thread reThread = new Thread(new recoverThread(".\\Result", gPhy));
//			reThread.start();
			

			
		}catch(Exception e) {
			System.out.println("FROME MAIN:"+e);
		}
	}

}
