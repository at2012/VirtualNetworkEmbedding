package vnr.bandit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import vnr.graph.Graph;

public class Embedding {
	/**
	 * 作用：该方法 根据 被处理好的VNR和物理网络拓扑进行
	 * 参数：embOrder将排好序等待映射的节点键值对传递到方法，这个相当于VNR
	 * 		g把物理网络传递给方法*/
	public static Map<Integer,Integer> embedding(LinkedHashMap<Integer,Double> embOrder,Graph g){//
		Map<Integer,Integer> emb=new HashMap<Integer,Integer>();
		double[] sim=new double[g.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，
		
		try{
			for(Map.Entry<Integer, Double> entry:embOrder.entrySet()){//对于待映射队列中的每一个key-value，进行映射
				System.out.println("Embedding__以下映射节点"+entry.getKey());
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
