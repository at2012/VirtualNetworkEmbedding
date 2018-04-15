package vnr.bandit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import vnr.graph.Graph;
import vnr.rank.Floyd;

public class Embedding {
	/**
	 * ���ã��÷��� ���� ������õ�VNR�������������˽���<p>
	 *  ������embOrder���ź���ȴ�ӳ��Ľڵ��ֵ�Դ��ݵ�����������൱��VNR;<p>
	 * vn�������������������ͼ���ݸ�����<p>
	 * pn��������������ͼ���ݸ�����<p>
	 * linkedhashmap���ڴ洢��·ӳ����<p>*/
	
	String folder;
	public Embedding(String f) {
		folder=f;
	}
	/**
	 * ����ڵ�ӳ��
	 * @param emborder ��˳���źõĽڵ�
	 * @param nodeEmbedResult �洢�ڵ�ӳ��Ľ��,���ں�������·ӳ��
	 * @param vn �����������������ͼ
	 * @param pn ������������ͼ
	 * @param n ��ǰ��ȡ���ǵڼ������������ļ�
	 * @return ���ؽڵ�ӳ����,�ɹ�Ϊ1,ʧ��Ϊ-1
	 * */
	public  int embedNode(LinkedHashMap<Integer,Double> embOrder,LinkedHashMap<Integer,Integer> nodeEmbedResult,Graph vn,Graph pn,int n){//n
//		LinkedHashMap<Integer,Integer> nodeEmbedResult=new LinkedHashMap<Integer,Integer>();//���ڴ洢���յ�ӳ����,���ڷ��ؽ��
		boolean[] f=new boolean[pn.getNumOfNode()];//���ڱ�־����ڵ���û�б���ǰ��������ӳ���
		int s;//���ڴ洢��ѡ�еĽڵ���±꣬��Ϊ����������ģ����Զ�ε��÷���
		BufferedWriter resOut =null;
		
		try{
			resOut = new BufferedWriter(new FileWriter(folder+"\\res"+n+".txt"));
			
			/*ӳ���һ���͵ڶ����ڵ㣬���ѡ�����κ�ѡ����õ�*/
			System.out.println("Embedding__��ʼӳ��ڵ�");
			int lastSel=pn.getNumOfNode();//���ڴ洢��һ��VNR�ڵ��Ӧ������ڵ�,���������ﶨ���ǲ����ʵģ���Ϊ���ֵ�ڱ�������������Ҫ��������һ�εġ����ÿ�α�����һ���µ�ֵ���ᱻ��ʼ���Ļ��ʹ�����ȥ��
			
			int t=0;//������Ϊ�˲��ϵ�ǰ����map�����ķ������棬û�����ɻ�����һ���Ŀӡ���t������������ǵ�һ���ڵ㣬��Ҫ���⴦��
			Map.Entry<Integer, Double> entryFirst=null;//�洢
			//���ں����if��������δ��ֵ����map.entry�ǽӿڣ���û����ʼ������������null��
			//��������֪���᲻�����г��������������ҵĳ����entryfirstһ���ǿ��Ա�������ֵ�ģ���Ը��Ҫ����ͻȻ��ͨ�˿�ָ���쳣����ô���ֵģ����������ͣ����ܿ�ʼû�и���ʼֵ�������õ�ʱ�򱨴����Ծ��ܻ�������null��ֵ�������������˿�ָ����쳣��
			//��ȷ���ǲ������������Ǿ����п���
			Map.Entry<Integer, Double> entry;
			for(Map.Entry<Integer, Double>e:embOrder.entrySet()) {//�����漰����map�����б�İ취�ġ�
				int firstSel=pn.getNumOfNode();//���ڴ洢��һ��VNR�ڵ��Ӧ������ڵ�,��������ʼֵ�����Լ����ǲ���û����ȷ��ֵ
				int pathLength=pn.getMax();//���ڴ洢��Ӧ������·���ĳ���
				
				int firsTemp;//������ʱ�洢��һ������ڵ�ĳ�α�ѡ�е�����ڵ�
				int lasTemp;//������ʱ�洢ĳ������ڵ㱻��ʱѡ�е�����ڵ�
				//��������������temp�����Ǵ�����if����Ū�����ģ�Ҳ��֪���ĸ��ã������Ǹ�pathTemp�Ҳ���Ū��������ǰ���ֵ��Ӱ�����
								
				if(t==0) {//���ڵ�һ����ӳ��ڵ�
					entryFirst=e;
				}else if(t==1) {//���ڵڶ�����ӳ��ڵ㣬����һ�ԣ���ǰ�����ڵ㣩����Ҫ���⴦��ġ�
					entry=e;
					Stack<Integer> pathSel=new Stack<Integer>();//ĳ�Ա�ѡ�еĽڵ����ձ�ѡ�е�·��
					
					for(int j=0;j<3;j++){//���������˻��˶��ٴ�
//						List<Integer> pathTemp=new LinkedList<Integer>();//���ڴ洢����ʱѡ�е�·����
						Stack<Integer> pathTemp=new Stack<Integer>();//���ڴ洢����ʱѡ�е�·����
						boolean[] fTemp=new boolean[pn.getNumOfNode()];//���ڱ�־����ڵ��ڻ��˹����У���ô�б�ӳ���,����������Ǵ�����������ģ���Ϊÿ�λ��˹��̶�Ҫ��֤���ǿյģ�û�б���ֵ���ġ�
						{						
							Map<Integer,Double> sim=new HashMap<Integer,Double>();
							for(int i=0;i<pn.getNumOfNode();i++){
								if(!f[i] && pn.getCpu(i)>=vn.getCpu(entryFirst.getKey())){//��������ڵ�cpu����VNR�ڵ�cpu
									sim.put(i, Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i)));
								}
							}
							//���sim���ǿյ�,˵������������ڵ����������ڵ�.��֮,���ڽڵ�ӳ�䲿�־�ʧ����
							if(!sim.isEmpty()) {
								firsTemp=ProbabilitySelected.proSelected(sim);
								fTemp[firsTemp]=true;
								System.out.println("embedding___�˴�ѡ�нڵ�"+firsTemp);
							}else {
								resOut.close();
								return -1;
							}
							
						}
						/*����������������������飬��Ϊһ����ӳ��ڵ��Ӧһ��sim���飬��������ڴ洢��ʱ�������ýڵ��±���Ϊ�����±�ģ�Ϊ�˲�����һ�α�����ļ����simֵӰ����һ���ڵ��ֵ��
						 * ����sim������飬����ÿһ����ӳ��ڵ� ����Ҫ�ӿյ����鿪ʼ������ÿ����֮ǰ���¸�ֵΪ0��������ѡ����ʹ��������飬��ÿ�����ﶨ�����飬�������£�*/
						
						/*
						 * ����ģ�����ȼ�����������Ҫ��Ľڵ�����ƶȣ�Ȼ��ͨ������ѡ��ѡ��ڵ㡣
						 */
						{
//							ArrayList<Double> sim=new ArrayList<Double>();//���Ľ������ﲻ�á��������϶��ò�����ô��++���ǣ�ÿ��i���п��ܱ���¼����������ô��������ǿ��Եġ�
							Map<Integer,Double> sim=new HashMap<Integer,Double>();
							for(int i=0;i<pn.getNumOfNode();i++){
								if(!fTemp[i] && !f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//��������ڵ�cpu����VNR�ڵ�cpu
									sim.put(i, Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
								}
							}
							if(!sim.isEmpty()) {
								lasTemp=ProbabilitySelected.proSelected(sim);
								System.out.println("embedding__�˴�ѡ�нڵ㣺"+lasTemp);
							}else {
								resOut.close();
								return -1;
							}							
						}
						/*��·��ӳ�����ҵ��õ�ӳ���*/
						int pathLengthTemp=Floyd.floyd(pn, firsTemp, lasTemp, pathTemp);
						
						if(pathLength>pathLengthTemp) {//����µõ���·������С����ô�ʹ洢����µ�·����Ӧ����ʼ��ֹ�ڵ㣬�Լ�·��
//							pathLength=Floyd.floyd(pn, firsTemp, lasTemp, pathTemp);//�������������ڵ��������ε�floyd������pathTemp�Ǳ���ֵ���ε�
							pathLength=pathLengthTemp;
							firstSel=firsTemp;
							lastSel=lasTemp;
							pathSel=pathTemp;
						}
					}
					
					/*������Ϳ��԰�ӳ�������뵽map������*/
					nodeEmbedResult.put(entryFirst.getKey(), firstSel);
					nodeEmbedResult.put(entry.getKey(), lastSel);
					/*ӳ��ڵ�*/
					
					resOut.write(vn.getNumOfNode()+" "+vn.getNumOfEdge()+"\r\n");//��һ��ӳ��д����������ڵ������ͱ�����
					
					resOut.write(entryFirst.getKey()+" "+firstSel+" "+vn.getCpu(entryFirst.getKey())+"\r\n"
									+entry.getKey()+" "+lastSel+" "+vn.getCpu(entry.getKey())+"\r\n");//��ӳ����д���ı������� ������������
					pn.setCpu(firstSel, pn.getCpu(firstSel)-vn.getCpu(entryFirst.getKey()));
					pn.setCpu(lastSel, pn.getCpu(lastSel)-vn.getCpu(entry.getKey()));
					
					System.out.println(pn.getCpu(firstSel));
					System.out.println(pn.getCpu(lastSel));
					
					/*ӳ����·*/
//					while(!pathSel.empty()) {
//						resOut.write(pathSel.pop()+" ");
//					}
//					resOut.write("\r\n");
//					resOut.write(entry.getKey()+" "+lastSel+" ");

					f[firstSel]=true;//����ڵ㱻����ѡ���󣬼ǵñ���£����治��ѡ��
					f[lastSel]=true;
					
					System.out.println("embedding___����ӳ����__ǰ�����ڵ�"+firstSel+"--"+lastSel);
				}else {//���ڳ���ǰ�����ڵ�����������ڵ��ӳ��
					Stack<Integer> pathSel=new Stack<Integer>();//ĳ�Ա�ѡ�еĽڵ����ձ�ѡ�е�·��
					int lastSelTemp=lastSel;
					entry=e;
					
					//�ŵ����ﲻ�У���Ϊentry�Ѿ��ı�
					for(int j=0;j<3;j++) {
						Stack<Integer> pathTemp=new Stack<Integer>();//���ڴ洢����ʱѡ�е�·����
						Map<Integer,Double> sim=new HashMap<Integer,Double>();
						for(int i=0;i<pn.getNumOfNode();i++){
							if(!f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//��������ڵ�cpu����VNR�ڵ�cpu
								sim.put(i, Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i)));
							}
						}
						lasTemp=ProbabilitySelected.proSelected(sim);
						System.out.println("embedding____�˴�ѡ�нڵ㣺___else:"+lasTemp);
						/*��·��ӳ�����ҵ��õ�ӳ���*/
						int pathLengthTemp=Floyd.floyd(pn, lastSelTemp, lasTemp, pathTemp);
						//����������������Ͱ�lastSel���ģ���Ϊ�������ػ��ݵ�ʱ������Ҫ�õ��ģ��������Դ�����ĳ�������

						if(pathLength>pathLengthTemp) {//����µõ���·������С����ô�ʹ洢����µ�·����Ӧ����ʼ��ֹ�ڵ㣬�Լ�·��
							pathLength=pathLengthTemp;
							lastSel=lasTemp;
							pathSel=pathTemp;
						}
					}
					
					/*������Ϳ��԰�ӳ�������뵽map������*/
					nodeEmbedResult.put(entry.getKey(), lastSel);
					/*ӳ��ڵ�*/
					resOut.write(entry.getKey()+" "+lastSel+" "+vn.getCpu(entry.getKey())+"\r\n");
					pn.setCpu(lastSel, pn.getCpu(lastSel)-vn.getCpu(entry.getKey()));
					System.out.println(pn.getCpu(lastSel));
					/*ӳ����·*/
//					while(!pathSel.empty()) {
//						resOut.write(pathSel.pop()+" ");
//					}
//					resOut.write("\r\n");
//					resOut.write(entry.getKey()+" "+lastSel+" ");//��ʼ���ڷ�����һ�нڵ����·�ļ�¼���������˽��д�뷽ʽ
					f[lastSel]=true;
				}
				t++;		
			}
			resOut.close();
		}catch(Exception e){
			System.out.println("Embedding__embedding():"+e);
		}finally {
			
			if(resOut != null) {
				try {
					resOut.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			
		}
//		return emb;
		return 1;
	}

	/**
	 * ������·ӳ��
	 * @param pn �������磬����Ѱ·
	 * @param vn �������磬���ڻ�ȡ��Ҫӳ��ı�ֵ��Ϣ
	 * @param embNode ǰ��ڵ�ӳ��Ľ��
	 * @param n ��ʾ��ǰӳ������ļ����еڼ��������ļ�*/
	public int embLink(Graph vn,Graph pn,LinkedHashMap<Integer,Integer> nodeEmbedResult,int n){
		int countLinkNum=0;	
		BufferedWriter resOut = null;
		
			try {
				resOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder+"\\res"+n+".txt",true)));
				
				double virWeight;
				int a,b;//���������������ʱ����ab��ʾ������·�����������ڵ㡣
				boolean[][] flag=new boolean[vn.getNumOfNode()][vn.getNumOfNode()];//�����·�Ƿ��Ѿ���ӳ�������ʼ��Ĭ��Ϊfalse
				
//				int countLinkNum=0;
				
				
				for(Map.Entry<Integer, Integer>e:nodeEmbedResult.entrySet()) {
					int i=e.getKey();
					for(int j=0;j<vn.getNumOfNode();j++) {
						virWeight=vn.getWeight(i, j);
						Stack<Integer> pathSel=new Stack<Integer>();//ĳ�Ա�ѡ�еĽڵ����ձ�ѡ�е�·��
						if(vn.getWeight(i, j)!=vn.getMax() && i!=j && !flag[i][j]) {
	//						Floyd.floyd(pn, i, j);*
							resOut.write(i+" "+j+" "+virWeight+" ");
							/*ӳ����·*/
							if(Floyd.floyd(pn, nodeEmbedResult.get(i), nodeEmbedResult.get(j), pathSel,virWeight) < pn.getMax()) {
								//�����������ʵ����Դ����
	//						Floyd.floyd(pn, embNode.get(i), embNode.get(j), pathSel,virWeight);
	//					resOut.write(i+" "+j+"\r\n");
								countLinkNum+=pathSel.size();
								while(!pathSel.empty()) {
									a=pathSel.pop();
									if(pathSel.size()>=1) {
										b=pathSel.peek();
										pn.setWeight(a, b, pn.getWeight(a, b)-virWeight);
									}
									resOut.write(a+" ");
								}
								flag[i][j]=true;
								flag[j][i]=true;
								resOut.write("\r\n");
							}else {//��������������
								System.out.println("δ���������������·ӳ��ʧ��");
//								resOut.close();
								return -1;
							}
						}
						
					}
					
				}
//				resOut.close();
			}catch(Exception e) {
				System.out.println("FROM embedding.java/embLink:"+e);
			}finally {
				if(resOut != null) {
					try {
						resOut.close();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
				
				
			}
			return countLinkNum;
		}

	
	
	/*
	 * ���ڻָ���Դ
	 * @param pn �������磬����Ѱ·
	 * @param vn �������磬���ڻ�ȡ��Ҫӳ��ı�ֵ��Ϣ
	 * @param n ��ʾ��ǰӳ������ļ����еڼ��������ļ������ڶ�λҪ�ָ����ļ�*/
	public void recoverSource(Graph vn,int n) {
//		BufferedWriter resOut = new BufferedWriter(new FileWriter(folder+"\\res"+n+".txt"));
		File recoverFile = new File(folder+"\\res"+n+".txt");
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
//				if(lineContent.length==2) {
				if (i<=nodeNum) {//����ǽڵ���Ϣ����ѽڵ���Դ�ָ�����������
					vn.setCpu(Integer.parseInt(lineContent[1])
							,vn.getCpu(Integer.parseInt(lineContent[1]))+Double.parseDouble(lineContent[2]));
				}else {
					int flag=3;//���ڱ�ʾ���ָ���������·��flag��flag+1��ʾһ����·
					double w=Double.parseDouble(lineContent[2]);
					while(flag<lineContent.length-1) {
						int s,d;
						s=Integer.parseInt(lineContent[flag]);
						d=Integer.parseInt(lineContent[flag+1]);
						vn.setWeight(s, d, vn.getWeight(s, d)+w);
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
				Logger.getGlobal().info("ɾ��ʧ���ļ�");
//				recoverFile.delete();
			}
		}
		
		
		
	}
	
	/**
	 * ������·ӳ��
	 * @param pn �������磬����Ѱ·
	 * @param vn �������磬���ڻ�ȡ��Ҫӳ��ı�ֵ��Ϣ
	 * @param embNode ǰ��ڵ�ӳ��Ľ��
	 * @param n ��ʾ��ǰӳ������ļ����еڼ��������ļ�*/
//	public int embLink(Graph vn,Graph pn,LinkedHashMap<Integer, Integer> embNode,int n){
//		
//		try {
//			BufferedWriter resOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder+"\\res"+n+".txt",true)));
//			
//			double virWeight;
//			int a,b;//���������������ʱ����ab��ʾ������·�����������ڵ㡣
//			
//			for(int i=0;i<vn.getNumOfNode();i++) {
//				for(int j=i+1;j<vn.getNumOfNode();j++) {
//					virWeight=vn.getWeight(i, j);
//					Stack<Integer> pathSel=new Stack<Integer>();//ĳ�Ա�ѡ�еĽڵ����ձ�ѡ�е�·��
//					if(vn.getWeight(i, j)!=vn.getMax()) {
////						Floyd.floyd(pn, i, j);*
//						resOut.write(i+" "+j+" "+virWeight+" ");
//						/*ӳ����·*/
//						if(Floyd.floyd(pn, embNode.get(i), embNode.get(j), pathSel,virWeight)!=pn.getMax()) {
//							//�����������ʵ����Դ����
////						Floyd.floyd(pn, embNode.get(i), embNode.get(j), pathSel,virWeight);
////					resOut.write(i+" "+j+"\r\n");
//							while(!pathSel.empty()) {
//								a=pathSel.pop();
//								if(pathSel.size()>=1) {
//									b=pathSel.peek();
//									pn.setWeight(a, b, pn.getWeight(a, b)-virWeight);
//								}
//								resOut.write(a+" ");
//							}
//							resOut.write("\r\n");
//						}else {//��������������
//							System.out.println("δ���������������·ӳ��ʧ��");
//							return -1;
//						}
//					}
//				}
//			}
//			resOut.close();
//		}catch(Exception e) {
//			System.out.println("FROM embedding/embLink"+e);
//		}
//		return 1;
//		
//	}

	
	
	/**
	 * */
	


}
