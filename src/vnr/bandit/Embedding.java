package vnr.bandit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import vnr.graph.Graph;

public class Embedding {
	/**
	 * ���ã��÷��� ���� ������õ�VNR�������������˽���<p>
	 * ������embOrder���ź���ȴ�ӳ��Ľڵ��ֵ�Դ��ݵ�����������൱��VNR;<p>
	 * vn�������������������ͼ���ݸ�����<p>
	 * pn��������������ͼ���ݸ�����<p>*/
	public static LinkedHashMap<Integer,Integer> embedding(LinkedHashMap<Integer,Double> embOrder,Graph vn,Graph pn){//n
		LinkedHashMap<Integer,Integer> emb=new LinkedHashMap<Integer,Integer>();//���ڴ洢���յ�ӳ����
//		double[] sim=new double[pn.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��
		boolean[] f=new boolean[pn.getNumOfNode()];//���ڱ�־����ڵ���û�б���ǰ��������ӳ���
		int s;//���ڴ洢��ѡ�еĽڵ���±꣬��Ϊ����������ģ����Զ�ε��÷���
				
		try{
			for(Map.Entry<Integer, Double> entry:embOrder.entrySet()){//���ڴ�ӳ������е�ÿһ��key-value������ӳ��
				System.out.println("Embedding__����ӳ��ڵ�"+entry.getKey());
				double[] sim=new double[pn.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��
				
				for(int i=0;i<pn.getNumOfNode();i++){
					if(!f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//��������ڵ�cpu����VNR�ڵ�cpu
						System.out.println("embedding__��ӳ���������ڵ�������Ҫ��Ľڵ�"+i+"�����ƶ�:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
						sim[i]=Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i));
	//					g.getCpu(i)>g.getCpu(entry.getKey()))
					}
				}
				s=ProbabilitySelected.proSelected(sim);//a��ѽѽѽ��ɵ��ɵ��ɵ�ӣ���Ϊ�����ѡ������������÷������ѡ����һ�����������ֵ��÷����������������������ֵ���п��ܲ�һ���İ�����������������������Ľ��һ��Ҫ�ǵ��ñ����洢�¡�
//				System.out.println("embedding_����ѡ�нڵ㣺"+ProbabilitySelected.proSelected(sim));
//				System.out.println("embedding_����ѡ�нڵ㣺"+s);
				emb.put(entry.getKey(), s);
				System.out.println("embedding_����ѡ�нڵ㣺"+entry.getKey()+"--"+s);
				f[s]=true;
			}
			
		}catch(Exception e){
			System.out.println("Embedding__embedding():"+e);
		}
				
//		emb.put(key, value)
		
		return emb;
	}

}
