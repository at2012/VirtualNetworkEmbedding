package vnr.bandit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import vnr.graph.Graph;

public class Embedding {
	/**
	 * ���ã��÷��� ���� ������õ�VNR�������������˽���
	 * ������embOrder���ź���ȴ�ӳ��Ľڵ��ֵ�Դ��ݵ�����������൱��VNR
	 * 		g���������紫�ݸ�����*/
	public static Map<Integer,Integer> embedding(LinkedHashMap<Integer,Double> embOrder,Graph g){//
		Map<Integer,Integer> emb=new HashMap<Integer,Integer>();
		double[] sim=new double[g.getNumOfNode()];//���Ľ������ﲻ�á��������϶��ò�����ô��
		
		try{
			for(Map.Entry<Integer, Double> entry:embOrder.entrySet()){//���ڴ�ӳ������е�ÿһ��key-value������ӳ��
				System.out.println("Embedding__����ӳ��ڵ�"+entry.getKey());
	//			entry.getValue()
				for(int i=0;i<g.getNumOfNode();i++){
					if(g.getCpu(i)>=g.getCpu(entry.getKey())){
						Similarity.cos(g.getNode(entry.getKey()),g.getNode(i));
						System.out.println("embedding"+Similarity.cos(g.getNode(entry.getKey()),g.getNode(i)));
						sim[i]=Similarity.cos(g.getNode(entry.getKey()),g.getNode(i));
						
	//					g.getCpu(i)>g.getCpu(entry.getKey()))
					}
				}
			}
			
		}catch(Exception e){
			System.out.println("Embedding__embedding():"+e);
		}
		
//		emb.put(key, value)
		
		
		
		return emb;
	}

}
