package vnr.bandit;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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
		LinkedHashMap<Integer,Integer> emb=new LinkedHashMap<Integer,Integer>();//���ڴ洢���յ�ӳ����
//		double[] sim=new double[pn.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��
		boolean[] f=new boolean[pn.getNumOfNode()];//���ڱ�־����ڵ���û�б���ǰ��������ӳ���
		int s;//���ڴ洢��ѡ�еĽڵ���±꣬��Ϊ����������ģ����Զ�ε��÷���
						
		try{
			
			/*ӳ���һ���͵ڶ����ڵ㣬�漴ѡ�����κ�ѡ����õ�*/
			System.out.println("Embedding__��ʼӳ��ڵ�");
				int lastSel=pn.getNumOfNode();//���ڴ洢��һ��VNR�ڵ��Ӧ������ڵ�,���������ﶨ���ǲ����ʵģ���Ϊ���ֵ�ڱ�������������Ҫ��������һ�εġ����ÿ�α�����һ���µ�ֵ���ᱻ��ʼ���Ļ��ʹ�����ȥ��
			
			int t=0;
			Map.Entry<Integer, Double> entryFirst=null;//���ں����if��������δ��ֵ����map.entry�ǽӿڣ���û����ʼ������������null��
			//��������֪���᲻�����г��������������ҵĳ����entryfirstһ���ǿ��Ա�������ֵ�ģ���Ը��Ҫ����ͻȻ��ͨ�˿�ָ���쳣����ô���ֵģ����������ͣ����ܿ�ʼû�и���ʼֵ�������õ�ʱ�򱨴����Ծ��ܻ�������null��ֵ�������������˿�ָ����쳣��
			//��ȷ���ǲ������������Ǿ����п���
			Map.Entry<Integer, Double> entry;
			for(Map.Entry<Integer, Double>e:embOrder.entrySet()) {
				int firstSel=pn.getNumOfNode();//���ڴ洢��һ��VNR�ڵ��Ӧ������ڵ�,��������ʼֵ�����Լ����ǲ���û����ȷ��ֵ
//				List<Integer> pathSel;//ĳ�Ա�ѡ�еĽڵ����ձ�ѡ�е�·��,�������Ҫ�ŵ�if����ȥ���壬��Ϊÿ�ζ�����ֵ
				int pathLength=5000;//���ڴ洢��Ӧ������·���ĳ���,���Ľ�������ģ�·������ѡ���ǳ������ߣ�ֱ����5000����
				
				boolean[] fTemp=new boolean[pn.getNumOfNode()];//���ڱ�־����ڵ��ڻ��˹����У���ô�б�ӳ���
				int firsTemp;//������ʱ�洢��һ������ڵ�ĳ�α�ѡ�е�����ڵ�
				int lasTemp;//������ʱ�洢ĳ������ڵ㱻��ʱѡ�е�����ڵ�
				//��������������temp�����Ǵ�����if����Ū�����ģ�Ҳ��֪���ĸ��ã������Ǹ�pathTemp�Ҳ���Ū��������ǰ���ֵ��Ӱ�����
				
				
				
				
				if(t==0) {
					entryFirst=e;
				}else if(t==1) {
					entry=e;
//					int firsTemp;//������ʱ�洢��һ������ڵ�ĳ�α�ѡ�е�����ڵ�
//					int lasTemp;//������ʱ�洢ĳ������ڵ㱻��ʱѡ�е�����ڵ�
					
					List<Integer> pathSel;//ĳ�Ա�ѡ�еĽڵ����ձ�ѡ�е�·��
					
					for(int j=0;j<3;j++){
//						List<Integer> pathTemp=new LinkedList<Integer>();//���ڴ洢����ʱѡ�е�·����
						Stack<Integer> pathTemp=new Stack<Integer>();//���ڴ洢����ʱѡ�е�·����
						
						{						
							double[] sim=new double[pn.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��++���ǣ�ÿ��i���п��ܱ���¼����������ô��������ǿ��Եġ�
							for(int i=0;i<pn.getNumOfNode();i++){
								if(!f[i] && pn.getCpu(i)>=vn.getCpu(entryFirst.getKey())){//��������ڵ�cpu����VNR�ڵ�cpu
//									System.out.println("embedding__new__��ӳ���1������ڵ�������Ҫ��Ľڵ�"+i+"�����ƶ�:"+Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i)));
									sim[i]=Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i));
									//					g.getCpu(i)>g.getCpu(entryFirst.getKey()))
								}
							}
							firsTemp=ProbabilitySelected.proSelected(sim);
							fTemp[firsTemp]=true;
							System.out.println("embedding___�˴�ѡ�нڵ�"+firsTemp);
						}
						/*����������������������飬��Ϊһ����ӳ��ڵ��Ӧһ��sim���飬��������ڴ洢��ʱ�������ýڵ��±���Ϊ�����±�ģ�Ϊ�˲�����һ�α�����ļ����simֵӰ����һ���ڵ��ֵ��
						 * ����sim������飬����ÿһ����ӳ��ڵ� ����Ҫ�ӿյ����鿪ʼ������ÿ����֮ǰ���¸�ֵΪ0��������ѡ����ʹ��������飬��ÿ�����ﶨ�����飬�������£�*/
						{
							double[] sim=new double[pn.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��++���ǣ�ÿ��i���п��ܱ���¼����������ô��������ǿ��Եġ�
							for(int i=0;i<pn.getNumOfNode();i++){
								if(!fTemp[i] && !f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//��������ڵ�cpu����VNR�ڵ�cpu
//									System.out.println("embedding__new__��ӳ���2������ڵ�������Ҫ��Ľڵ�"+i+"�����ƶ�:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
									sim[i]=Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i));
									//					g.getCpu(i)>g.getCpu(entry.getKey()))
								}
							}
							lasTemp=ProbabilitySelected.proSelected(sim);
							System.out.println("embedding__�˴�ѡ�нڵ㣺"+lasTemp);
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
						
//						/*������Ϳ��԰�ӳ�������뵽map������*///���ﻹ����Ŷ������ѭ�����棬�ȵ�ѭ��������ȥput
//						emb.put(entryFirst.getKey(), firstSel);
//						emb.put(entry.getKey(), lastSel);
					}
					
					/*������Ϳ��԰�ӳ�������뵽map������*/
					emb.put(entryFirst.getKey(), firstSel);
					emb.put(entry.getKey(), lastSel);
					f[firstSel]=true;//����ڵ㱻����ѡ���󣬼ǵñ���£����治��ѡ��
					f[lastSel]=true;
					System.out.println("embedding___����ӳ����__ǰ�����ڵ�"+firstSel+"--"+lastSel);
				}else {//���ڳ���ǰ�����ڵ�����������ڵ��ӳ��
					
//					List<Integer> pathTemp=new LinkedList<Integer>();//���ڴ洢����ʱѡ�е�·����
					Stack<Integer> pathTemp=new Stack<Integer>();//���ڴ洢����ʱѡ�е�·����
					List<Integer> pathSel;//ĳ�Ա�ѡ�еĽڵ����ձ�ѡ�е�·��
					entry=e;
					
					for(int j=0;j<3;j++) {
						
						double[] sim=new double[pn.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��++���ǣ�ÿ��i���п��ܱ���¼����������ô��������ǿ��Եġ�
						for(int i=0;i<pn.getNumOfNode();i++){
							if(!f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//��������ڵ�cpu����VNR�ڵ�cpu
//								System.out.println("embedding__new__��ӳ���2������ڵ�������Ҫ��Ľڵ�"+i+"�����ƶ�:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
								sim[i]=Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i));
								//					g.getCpu(i)>g.getCpu(entry.getKey()))
							}
						}
						lasTemp=ProbabilitySelected.proSelected(sim);
						System.out.println("embedding____�˴�ѡ�нڵ㣺___else:"+lasTemp);
						/*��·��ӳ�����ҵ��õ�ӳ���*/
						int pathLengthTemp=Floyd.floyd(pn, lastSel, lasTemp, pathTemp);
						if(pathLength>pathLengthTemp) {//����µõ���·������С����ô�ʹ洢����µ�·����Ӧ����ʼ��ֹ�ڵ㣬�Լ�·��
							pathLength=pathLengthTemp;
							lastSel=lasTemp;
							pathSel=pathTemp;
						}
					}
					
					
					/*������Ϳ��԰�ӳ�������뵽map������*/
//					emb.put(entryFirst.getKey(), firstSel);
					emb.put(entry.getKey(), lastSel);
					f[lastSel]=true;
					
				}
				
				t++;
					
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
//			
//			
//			Iterator<Map.Entry<Integer,Double>> it=embOrder.entrySet().iterator();//���ڱ���������ӳ������Ľڵ�
//			
////			List<Integer> pathTemp=new LinkedList<Integer>();//������������������ﶨ�岻�ã���Ϊÿһ�ε�ѭ������ҪpathTemp�ǿյ�
//			
//			int t=1;//������whileѭ�����жϽڵ��ǲ���ǰ�����ڵ�
//			int sTemp;//���ڶ���һ���ڵ�Ҫӳ���Σ�����������ʱ�洢���п��ܽ����
//			int sTemp2;//������Ϊǰ�����ڵ�Ĵ���˳������� �ڵ�Ĵ���˳��̫һ�£�����Ҫ�����洢��
//			/*��ʼ����ӳ��ڵ����·��ֻ������������ڵ��ӳ��Ҫ��̫һ��*/
//			while(it.hasNext()){
////			for(each)
//				
//				System.out.println("�������˼��ΰ�      ����������");
//				if(t<2){
//					Map.Entry<Integer, Double> entry =it.next();
//					Map.Entry<Integer, Double> entry2=it.next();
//					System.out.println("ititititi"+entry);
//					System.out.println("ititititi22222"+entry2);
//					int lTemp=pn.getNumOfEdge();//���ڴ洢ĳ�νڵ�ӳ��֮��·����
//					int disTemp=5000;//���ڴ洢ĳ����ʱ��ѡ�е�·���ĳ��ȣ���ʼ��Ϊ���·��ֵ
//					List<Integer> pathTemp=new LinkedList<Integer>();
////					double[] sim=new double[pn.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��++���ǣ�ÿ��i���п��ܱ���¼����������ô��������ǿ��Եġ�
//					//sim���ã�������Ҫ����ÿ������Ҫ��Ľڵ�����ƶȵ���ռ�ı�������Ϊ����ѡ��ĸ��ʣ�������Ҫ�洢�����еķ���Ҫ��ĵ�����ƶȣ�������ͺ��ټ����ֵ����������֪����û�б�İ취����ֱ�Ӿͼ����˱�ֵ�ˣ��о���û�еģ��ٺ�
//					//int[][] disTemp = new int[pn.getNumOfNode()][pn.getNumOfNode()];
//					boolean[] fTemp=new boolean[pn.getNumOfNode()];//���ڱ�־����ڵ��ڻ��˹����У���ô�б�ӳ���
//					{						
//						double[] sim=new double[pn.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��++���ǣ�ÿ��i���п��ܱ���¼����������ô��������ǿ��Եġ�
//						for(int i=0;i<pn.getNumOfNode();i++){
//							if(!f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//��������ڵ�cpu����VNR�ڵ�cpu
//								System.out.println("embedding__new__��ӳ���1������ڵ�������Ҫ��Ľڵ�"+i+"�����ƶ�:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
//								sim[i]=Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i));
//								//					g.getCpu(i)>g.getCpu(entry.getKey()))
//							}
//						}
//						sTemp=ProbabilitySelected.proSelected(sim);
//						fTemp[sTemp]=true;
//						System.out.println("�˴�ѡ�нڵ�"+sTemp);
//					}
//					/*����������������������飬��Ϊһ����ӳ��ڵ��Ӧһ��sim���飬��������ڴ洢��ʱ�������ýڵ��±���Ϊ�����±�ģ�Ϊ�˲�����һ�α�����ļ����simֵӰ����һ���ڵ��ֵ��
//					 * ����sim������飬����ÿһ����ӳ��ڵ� ����Ҫ�ӿյ����鿪ʼ������ÿ����֮ǰ���¸�ֵΪ0��������ѡ����ʹ��������飬��ÿ�����ﶨ�����飬�������£�*/
//					{
//						double[] sim=new double[pn.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��++���ǣ�ÿ��i���п��ܱ���¼����������ô��������ǿ��Եġ�
//						for(int i=0;i<pn.getNumOfNode();i++){
//							if(!fTemp[i] && !f[i] && pn.getCpu(i)>=vn.getCpu(entry2.getKey())){//��������ڵ�cpu����VNR�ڵ�cpu
//								System.out.println("embedding__new__��ӳ���2������ڵ�������Ҫ��Ľڵ�"+i+"�����ƶ�:"+Similarity.cos(vn.getNode(entry2.getKey()),pn.getNode(i)));
//								sim[i]=Similarity.cos(vn.getNode(entry2.getKey()),pn.getNode(i));
//								//					g.getCpu(i)>g.getCpu(entry.getKey()))
//							}
//						}
//						sTemp2=ProbabilitySelected.proSelected(sim);
//						System.out.println("�˴�ѡ�нڵ㣺"+sTemp2);
//					}
//					
////					pathTemp=Floyd.floyd(pn, sTemp, sTemp2,disTemp);
////					lTemp=disTemp[sTemp][sTemp2];
//					System.out.println("·������"+Floyd.floyd(pn, sTemp, sTemp2, pathTemp));
//										
//					for(int i=0;i<pathTemp.size();i++){
//						System.out.println("��ʱ·���洢���ԣ�"+pathTemp.get(i));
//					}
//					
//					
//
//
//					
//					//���ø���ѡ�񷽷�������ѡ��ӳ��ڵ�
////					sTemp=ProbabilitySelected.proSelected(sim);//a��ѽѽѽ��ɵ��ɵ��ɵ�ӣ���Ϊ�����ѡ������������÷������ѡ����һ�����������ֵ��÷����������������������ֵ���п��ܲ�һ���İ�����������������������Ľ��һ��Ҫ�ǵ��ñ����洢�¡�
//					
////					if(!)
//					
//					System.out.println("���Բ��Բ���Ӵ"+entry.getKey()+":"+entry.getValue());
//					
//				}else{
//					/*��������ǰ�����ڵ�֮�󣬿�ʼ�������Ľڵ㴦��ʽ��ͬ*/
//					System.out.println("����������𣿣���");
//					
//				}
//				
//				t++;
//				System.out.println("����t��ʼ����"+t);
//				
//			}
			
			
			
//			
//			for(Map.Entry<Integer, Double> entry:embOrder.entrySet()){//���ڴ�ӳ������е�ÿһ��key-value������ӳ��
//				double[] sim=new double[pn.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��
//				
//				
//	
//				
//				
//				for(int i=0;i<pn.getNumOfNode();i++){
//					if(!f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//��������ڵ�cpu����VNR�ڵ�cpu
//						System.out.println("embedding__��ӳ���������ڵ�������Ҫ��Ľڵ�"+i+"�����ƶ�:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
//						sim[i]=Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i));
//	//					g.getCpu(i)>g.getCpu(entry.getKey()))
//					}
//				}
//				s=ProbabilitySelected.proSelected(sim);//a��ѽѽѽ��ɵ��ɵ��ɵ�ӣ���Ϊ�����ѡ������������÷������ѡ����һ�����������ֵ��÷����������������������ֵ���п��ܲ�һ���İ�����������������������Ľ��һ��Ҫ�ǵ��ñ����洢�¡�
////				System.out.println("embedding_����ѡ�нڵ㣺"+ProbabilitySelected.proSelected(sim));
////				System.out.println("embedding_����ѡ�нڵ㣺"+s);
//				emb.put(entry.getKey(), s);
//				System.out.println("embedding_����ѡ�нڵ㣺"+entry.getKey()+"--"+s);
//				f[s]=true;
//			}
		}catch(Exception e){
			System.out.println("Embedding__embedding():"+e);
		}
				
//		emb.put(key, value)
		
		return emb;
	}

//	/**
//	 * ���ã�
//	 * �����������������ˣ�����Ѱ·����ĳ��������ڵ��Ӧ������ڵ���±꣬///���ò��ã�����������������ø�floydûɶ����û��Ҫ��ӵ�һ��������
//	 * �������ͣ�*/
//	public static void linkEmbedding(Graph pn,int s,int d){
//		
//	}
}
