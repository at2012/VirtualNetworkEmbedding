package vnr.bandit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import vnr.graph.Graph;

public class Embedding {
	/**
	 * 作用：该方法 根据 被处理好的VNR和物理网络拓扑进行<p>
	 * 参数：embOrder将排好序等待映射的节点键值对传递到方法，这个相当于VNR;<p>
	 * vn把虚拟网络请求的拓扑图传递给方法<p>
	 * pn把物理网络拓扑图传递给方法<p>*/
	public static LinkedHashMap<Integer,Integer> embedding(LinkedHashMap<Integer,Double> embOrder,Graph vn,Graph pn){//n
		LinkedHashMap<Integer,Integer> emb=new LinkedHashMap<Integer,Integer>();//用于存储最终的映射结果
//		double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，
		boolean[] f=new boolean[pn.getNumOfNode()];//用于标志这个节点有没有被当前虚拟网络映射过
		int s;//用于存储被选中的节点的下标，因为是随机产生的，所以多次调用方法
				
		try{
			for(Map.Entry<Integer, Double> entry:embOrder.entrySet()){//对于待映射队列中的每一个key-value，进行映射
				System.out.println("Embedding__以下映射节点"+entry.getKey());
				double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，
				
				for(int i=0;i<pn.getNumOfNode();i++){
					if(!f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//物理网络节点cpu大于VNR节点cpu
						System.out.println("embedding__待映射点与物理节点中满足要求的节点"+i+"的相似度:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
						sim[i]=Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i));
	//					g.getCpu(i)>g.getCpu(entry.getKey()))
					}
				}
				s=ProbabilitySelected.proSelected(sim);//a啊呀呀呀，傻子傻子傻子，因为是随机选择，你在这里调用方法随机选择了一个，，下面又调用方法，就又随机啦，这两个值很有可能不一样的啊！！！！！这种随机产生的结果一定要记得用变量存储下。
//				System.out.println("embedding_最终选中节点："+ProbabilitySelected.proSelected(sim));
//				System.out.println("embedding_最终选中节点："+s);
				emb.put(entry.getKey(), s);
				System.out.println("embedding_最终选中节点："+entry.getKey()+"--"+s);
				f[s]=true;
			}
			
		}catch(Exception e){
			System.out.println("Embedding__embedding():"+e);
		}
				
//		emb.put(key, value)
		
		return emb;
	}

}
