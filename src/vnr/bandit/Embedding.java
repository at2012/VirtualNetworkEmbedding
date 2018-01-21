package vnr.bandit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import vnr.graph.Graph;
import vnr.rank.Floyd;

public class Embedding {
	/**
	 * ���ã��÷��� ���� ������õ�VNR�������������˽���<p>
	 *  ������embOrder���ź���ȴ�ӳ��Ľڵ��ֵ�Դ��ݵ�����������൱��VNR;<p>
	 * vn�������������������ͼ���ݸ�����<p>
	 * pn��������������ͼ���ݸ�����<p>
	 * linkedhashmap���ڴ洢��·ӳ����<p>*/
	public static LinkedHashMap<Integer,Integer> embedding(LinkedHashMap<Integer,Double> embOrder,Graph vn,Graph pn){//n
		//,LinkedHashMap<Edge,List<Integer>> linkMap
		LinkedHashMap<Integer,Integer> emb=new LinkedHashMap<Integer,Integer>();//���ڴ洢���յ�ӳ����,���ڷ��ؽ��
//		double[] sim=new double[pn.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��
		boolean[] f=new boolean[pn.getNumOfNode()];//���ڱ�־����ڵ���û�б���ǰ��������ӳ���
		int s;//���ڴ洢��ѡ�еĽڵ���±꣬��Ϊ����������ģ����Զ�ε��÷���
		
		
		File resFile =new File("result.txt");
		try{
			
			BufferedWriter resOut = new BufferedWriter(new FileWriter("embed.txt"));
			
//			PrintWriter resOutput=new PrintWriter(resFile);			
			
			/*ӳ���һ���͵ڶ����ڵ㣬�漴ѡ�����κ�ѡ����õ�*/
			System.out.println("Embedding__��ʼӳ��ڵ�");
			int lastSel=pn.getNumOfNode();//���ڴ洢��һ��VNR�ڵ��Ӧ������ڵ�,���������ﶨ���ǲ����ʵģ���Ϊ���ֵ�ڱ�������������Ҫ��������һ�εġ����ÿ�α�����һ���µ�ֵ���ᱻ��ʼ���Ļ��ʹ�����ȥ��
			
			int t=0;//������Ϊ�˲��ϵ�ǰ����map�����ķ������棬û�����ɻ�����һ���Ŀӡ���t������������ǵ�һ���ڵ㣬��Ҫ���⴦��
			Map.Entry<Integer, Double> entryFirst=null;//���ں����if��������δ��ֵ����map.entry�ǽӿڣ���û����ʼ������������null��
			//��������֪���᲻�����г��������������ҵĳ����entryfirstһ���ǿ��Ա�������ֵ�ģ���Ը��Ҫ����ͻȻ��ͨ�˿�ָ���쳣����ô���ֵģ����������ͣ����ܿ�ʼû�и���ʼֵ�������õ�ʱ�򱨴����Ծ��ܻ�������null��ֵ�������������˿�ָ����쳣��
			//��ȷ���ǲ������������Ǿ����п���
			Map.Entry<Integer, Double> entry;
			for(Map.Entry<Integer, Double>e:embOrder.entrySet()) {//�����漰����map�����б�İ취�ġ�
				int firstSel=pn.getNumOfNode();//���ڴ洢��һ��VNR�ڵ��Ӧ������ڵ�,��������ʼֵ�����Լ����ǲ���û����ȷ��ֵ
//				List<Integer> pathSel;//ĳ�Ա�ѡ�еĽڵ����ձ�ѡ�е�·��,�������Ҫ�ŵ�if����ȥ���壬��Ϊÿ�ζ�����ֵ
				int pathLength=pn.getMax();//���ڴ洢��Ӧ������·���ĳ���
				
//				boolean[] fTemp=new boolean[pn.getNumOfNode()];//���ڱ�־����ڵ��ڻ��˹����У���ô�б�ӳ���
				int firsTemp;//������ʱ�洢��һ������ڵ�ĳ�α�ѡ�е�����ڵ�
				int lasTemp;//������ʱ�洢ĳ������ڵ㱻��ʱѡ�е�����ڵ�
				//��������������temp�����Ǵ�����if����Ū�����ģ�Ҳ��֪���ĸ��ã������Ǹ�pathTemp�Ҳ���Ū��������ǰ���ֵ��Ӱ�����
				
				
				
				
				if(t==0) {//���ڵ�һ����ӳ��ڵ�
					entryFirst=e;
				}else if(t==1) {//���ڵڶ�����ӳ��ڵ㣬����һ�ԣ���ǰ�����ڵ㣩����Ҫ���⴦��ġ�
					entry=e;
//					int firsTemp;//������ʱ�洢��һ������ڵ�ĳ�α�ѡ�е�����ڵ�
//					int lasTemp;//������ʱ�洢ĳ������ڵ㱻��ʱѡ�е�����ڵ�
					
//					Stack<Integer> pathSel=null;//ĳ�Ա�ѡ�еĽڵ����ձ�ѡ�е�·��
					
					Stack<Integer> pathSel=new Stack<Integer>();//ĳ�Ա�ѡ�еĽڵ����ձ�ѡ�е�·��
					
					for(int j=0;j<3;j++){//���������˻��˶��ٴ�
//						List<Integer> pathTemp=new LinkedList<Integer>();//���ڴ洢����ʱѡ�е�·����
						Stack<Integer> pathTemp=new Stack<Integer>();//���ڴ洢����ʱѡ�е�·����
						boolean[] fTemp=new boolean[pn.getNumOfNode()];//���ڱ�־����ڵ��ڻ��˹����У���ô�б�ӳ���,����������Ǵ�����������ģ���Ϊÿ�λ��˹��̶�Ҫ��֤���ǿյģ�û�б���ֵ���ġ�
						{						
//							double[] sim=new double[pn.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��++���ǣ�ÿ��i���п��ܱ���¼����������ô��������ǿ��Եġ�
							//nonono�����������洢������sim����ĳ��Ȳ�����������Ҫ�������ڵ�����ƶ���proselect�����õ����鳤�ȵ�ʱ���ǻ����ġ�
//							ArrayList<Double> sim=new ArrayList<Double>();
							Map<Integer,Double> sim=new HashMap<Integer,Double>();
							for(int i=0;i<pn.getNumOfNode();i++){
								if(!f[i] && pn.getCpu(i)>=vn.getCpu(entryFirst.getKey())){//��������ڵ�cpu����VNR�ڵ�cpu
//									System.out.println("embedding__new__��ӳ���1������ڵ�������Ҫ��Ľڵ�"+i+"�����ƶ�:"+Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i)));
//									sim[i]=Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i));
//									sim.add(Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i)));
									sim.put(i, Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i)));
									//					g.getCpu(i)>g.getCpu(entryFirst.getKey()))
								}
							}
//							sim=simTemp.toArray(new double[simTemp.size()]);
							firsTemp=ProbabilitySelected.proSelected(sim);
							fTemp[firsTemp]=true;
							System.out.println("embedding___�˴�ѡ�нڵ�"+firsTemp);
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
//									System.out.println("embedding__new__��ӳ���2������ڵ�������Ҫ��Ľڵ�"+i+"�����ƶ�:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
//									sim.add(Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i)));
									sim.put(i, Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
									//					g.getCpu(i)>g.getCpu(entry.getKey()))
								}
							}
							lasTemp=ProbabilitySelected.proSelected(sim);
							System.out.println("embedding__�˴�ѡ�нڵ㣺"+lasTemp);
						}
						/*��·��ӳ�����ҵ��õ�ӳ���*/
//						System.out.println("��һ��floyd");
						int pathLengthTemp=Floyd.floyd(pn, firsTemp, lasTemp, pathTemp);
						
						System.out.println("test:"+pathTemp.empty());
						
//						while(pathTemp!=null) {
////							resOutput.print(pathTemp.pop()+"\t");
//							System.out.println("while:"+pathTemp.pop()+"\t");
//						}
						
						if(pathLength>pathLengthTemp) {//����µõ���·������С����ô�ʹ洢����µ�·����Ӧ����ʼ��ֹ�ڵ㣬�Լ�·��
//							pathLength=Floyd.floyd(pn, firsTemp, lasTemp, pathTemp);//�������������ڵ��������ε�floyd������pathTemp�Ǳ���ֵ���ε�
							pathLength=pathLengthTemp;
							firstSel=firsTemp;
							lastSel=lasTemp;
							pathSel=pathTemp;
						}
					}
					
					/*������Ϳ��԰�ӳ�������뵽map������*/
//					System.out.println("test222222:"+pathSel.empty());
					emb.put(entryFirst.getKey(), firstSel);
					emb.put(entry.getKey(), lastSel);
//					resOutput.printf("%d\t%d\t%d\t%d\t",entryFirst.getKey(),firstSel,entry.getKey(),lastSel);//��ӳ����д���ı������� ������������
					resOut.write(entryFirst.getKey()+" "+firstSel+" "+entry.getKey()+" "+lastSel+" ");//��ӳ����д���ı������� ������������
					
					while(!pathSel.empty()) {
//						resOutput.print(pathSel.pop()+"\t");
						resOut.write(pathSel.pop()+" ");;
						
						
					}
					resOut.write("\r\n");
//					resOutput.println();
					f[firstSel]=true;//����ڵ㱻����ѡ���󣬼ǵñ���£����治��ѡ��
					f[lastSel]=true;
					
					resOut.write(entry.getKey()+" "+lastSel+" ");
//					resOutput.printf("%d\t%d\t",entry.getKey(),lastSel);
					
					System.out.println("embedding___����ӳ����__ǰ�����ڵ�"+firstSel+"--"+lastSel);
				}else {//���ڳ���ǰ�����ڵ�����������ڵ��ӳ��
					
//					List<Integer> pathTemp=new LinkedList<Integer>();//���ڴ洢����ʱѡ�е�·����
//					Stack<Integer> pathTemp=new Stack<Integer>();//���ڴ洢����ʱѡ�е�·����
					Stack<Integer> pathSel=new Stack<Integer>();//ĳ�Ա�ѡ�еĽڵ����ձ�ѡ�е�·��
					int lastSelTemp=lastSel;
//					List<Integer> pathSel;//ĳ�Ա�ѡ�еĽڵ����ձ�ѡ�е�·��
					entry=e;
					
//					resOutput.printf("%d\t%d\t",entry.getKey(),lastSel);//��ӳ����д���ı������� ������������
					//�ŵ����ﲻ�У���Ϊentry�Ѿ��ı�
					
					for(int j=0;j<3;j++) {
						Stack<Integer> pathTemp=new Stack<Integer>();//���ڴ洢����ʱѡ�е�·����
//						ArrayList<Double> sim=new ArrayList<Double>();//���Ľ������ﲻ�á��������϶��ò�����ô��++���ǣ�ÿ��i���п��ܱ���¼����������ô��������ǿ��Եġ�
						Map<Integer,Double> sim=new HashMap<Integer,Double>();
						for(int i=0;i<pn.getNumOfNode();i++){
							if(!f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//��������ڵ�cpu����VNR�ڵ�cpu
//								System.out.println("embedding__new__��ӳ���2������ڵ�������Ҫ��Ľڵ�"+i+"�����ƶ�:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
//								sim.add(Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i)));
								sim.put(i, Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i)));
								//					g.getCpu(i)>g.getCpu(entry.getKey()))
							}
						}
						lasTemp=ProbabilitySelected.proSelected(sim);
						System.out.println("embedding____�˴�ѡ�нڵ㣺___else:"+lasTemp);
						/*��·��ӳ�����ҵ��õ�ӳ���*/
						int pathLengthTemp=Floyd.floyd(pn, lastSelTemp, lasTemp, pathTemp);
//						int pathLengthTemp=Floyd.floyd(pn, lastSel, lasTemp, pathTemp);
						//����������������Ͱ�lastSel���ģ���Ϊ�������ػ��ݵ�ʱ������Ҫ�õ��ģ��������Դ�����ĳ�������

						if(pathLength>pathLengthTemp) {//����µõ���·������С����ô�ʹ洢����µ�·����Ӧ����ʼ��ֹ�ڵ㣬�Լ�·��
							pathLength=pathLengthTemp;
							lastSel=lasTemp;
							pathSel=pathTemp;
						}
					}
					
					
					
					/*������Ϳ��԰�ӳ�������뵽map������*/
//					emb.put(entryFirst.getKey(), firstSel);
					
					emb.put(entry.getKey(), lastSel);
					resOut.write(entry.getKey()+" "+lastSel+" ");
//					resOutput.printf("%d\t%d\t",entry.getKey(),lastSel);

					while(!pathSel.empty()) {
						resOut.write(pathSel.pop()+" ");
//						resOutput.print(pathSel.pop()+"\t");
					}
					resOut.write("\r\n");
//					resOutput.println();
//					resOutput.printf("%d\t%d\r\n",entry.getKey(),lastSel);//��ӳ����д���ı������� ������������
					f[lastSel]=true;
					
//					resOutput.printf("%d\t%d\t",entry.getKey(),lastSel);

//					resOutput.printf("%d\t%d\t",entry.getKey(),lastSel);
					resOut.write(entry.getKey()+" "+lastSel+" ");
					
				}
				t++;		
			}
			
			resOut.close();
//			resOutput.close();

		}catch(Exception e){
			System.out.println("Embedding__embedding():"+e);
		}
				
		return emb;
	}


}
