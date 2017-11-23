package vnr.bandit;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import vnr.graph.Edge;
import vnr.graph.Graph;
import vnr.rank.Floyd;

public class Embedding {
	/**
	 * 作用：该方法 根据 被处理好的VNR和物理网络拓扑进行<p>
	 *  参数：embOrder将排好序等待映射的节点键值对传递到方法，这个相当于VNR;<p>
	 * vn把虚拟网络请求的拓扑图传递给方法<p>
	 * pn把物理网络拓扑图传递给方法<p>
	 * linkedhashmap用于存储链路映射结果<p>*/
	public static LinkedHashMap<Integer,Integer> embedding(LinkedHashMap<Integer,Double> embOrder,Graph vn,Graph pn){//n
		//,LinkedHashMap<Edge,List<Integer>> linkMap
		LinkedHashMap<Integer,Integer> emb=new LinkedHashMap<Integer,Integer>();//用于存储最终的映射结果
//		double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，
		boolean[] f=new boolean[pn.getNumOfNode()];//用于标志这个节点有没有被当前虚拟网络映射过
		int s;//用于存储被选中的节点的下标，因为是随机产生的，所以多次调用方法
						
		try{
			
			/*映射第一个和第二个节点，随即选择三次后选择最好的*/
			System.out.println("Embedding__开始映射节点");
			Iterator<Map.Entry<Integer,Double>> it=embOrder.entrySet().iterator();//用于遍历整个带映射网络的节点
			
//			List<Integer> pathTemp=new LinkedList<Integer>();//启发：这个变量在这里定义不好，因为每一次的循环都需要pathTemp是空的
			
			int t=0;//用于在while循环中判断节点是不是前两个节点
			int sTemp;//由于对于一个节点要映射多次，所以用于临时存储所有可能结果。
			int sTemp2;//都是因为前两个节点的处理顺序跟其他 节点的处理顺序不太一致，都需要单独存储。
			/*开始挨个映射节点和链路，只是最初的两个节点的映射要不太一样*/
			while(it.hasNext()){
				System.out.println("这里来了几次啊      ！！！！！");
				if(t<2){
					Map.Entry<Integer, Double> entry =it.next();
					Map.Entry<Integer, Double> entry2=it.next();
					System.out.println("ititititi"+entry);
					System.out.println("ititititi22222"+entry2);
					int lTemp=pn.getNumOfEdge();//用于存储某次节点映射之后路径的
					int disTemp=5000;//用于存储某条暂时被选中的路径的长度，初始化为最大路径值
					List<Integer> pathTemp=new LinkedList<Integer>();
//					double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，++但是，每个i都有可能被记录，所以用这么大的数组是可以的。
					//sim作用：由于需要计算每个符合要求的节点的相似度的所占的比例，作为概率选择的概率，所以需要存储与所有的符合要求的点的相似度，用于求和后再计算比值。启发，想知道有没有别的办法可以直接就计算了比值了，感觉是没有的，嘿嘿
					//int[][] disTemp = new int[pn.getNumOfNode()][pn.getNumOfNode()];
					boolean[] fTemp=new boolean[pn.getNumOfNode()];//用于标志这个节点在回退过程中，有么有被映射过
					{						
						double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，++但是，每个i都有可能被记录，所以用这么大的数组是可以的。
						for(int i=0;i<pn.getNumOfNode();i++){
							if(!f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//物理网络节点cpu大于VNR节点cpu
								System.out.println("embedding__new__待映射点1与物理节点中满足要求的节点"+i+"的相似度:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
								sim[i]=Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i));
								//					g.getCpu(i)>g.getCpu(entry.getKey()))
							}
						}
						sTemp=ProbabilitySelected.proSelected(sim);
						fTemp[sTemp]=true;
						System.out.println("此次选中节点"+sTemp);
					}
					/*启发：这里用了两个程序块，因为一个待映射节点对应一个sim数组，这个数组在存储的时候我是用节点下标作为数组下标的，为了不让上一次被允许的几点的sim值影响下一个节点的值，
					 * 所以sim这个数组，对于每一个待映射节点 都需要从空的数组开始，可以每次用之前更新赋值为0，但是我选择了使用俩程序块，在每个块里定义数组，这样更新，*/
					{
						double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，++但是，每个i都有可能被记录，所以用这么大的数组是可以的。
						for(int i=0;i<pn.getNumOfNode();i++){
							if(!fTemp[i] && !f[i] && pn.getCpu(i)>=vn.getCpu(entry2.getKey())){//物理网络节点cpu大于VNR节点cpu
								System.out.println("embedding__new__待映射点2与物理节点中满足要求的节点"+i+"的相似度:"+Similarity.cos(vn.getNode(entry2.getKey()),pn.getNode(i)));
								sim[i]=Similarity.cos(vn.getNode(entry2.getKey()),pn.getNode(i));
								//					g.getCpu(i)>g.getCpu(entry.getKey()))
							}
						}
						sTemp2=ProbabilitySelected.proSelected(sim);
						System.out.println("此次选中节点："+sTemp2);
					}
					
//					pathTemp=Floyd.floyd(pn, sTemp, sTemp2,disTemp);
//					lTemp=disTemp[sTemp][sTemp2];
					System.out.println("路径长度"+Floyd.floyd(pn, sTemp, sTemp2, pathTemp));
										
					for(int i=0;i<pathTemp.size();i++){
						System.out.println("临时路径存储测试："+pathTemp.get(i));
					}
					
					


					
					//调用概率选择方法，最终选定映射节点
//					sTemp=ProbabilitySelected.proSelected(sim);//a啊呀呀呀，傻子傻子傻子，因为是随机选择，你在这里调用方法随机选择了一个，，下面又调用方法，就又随机啦，这两个值很有可能不一样的啊！！！！！这种随机产生的结果一定要记得用变量存储下。
					
//					if(!)
					
					System.out.println("测试测试测试哟"+entry.getKey()+":"+entry.getValue());
					
				}else{
					/*当处理完前两个节点之后，开始处理后面的节点处理方式相同*/
					System.out.println("这里过来过吗？？？");
					
				}
				
				t++;
				System.out.println("这里t开始增加"+t);
				
			}
			
			
			
			
			for(Map.Entry<Integer, Double> entry:embOrder.entrySet()){//对于待映射队列中的每一个key-value，进行映射
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

//	/**
//	 * 作用：
//	 * 参数：物理网络拓扑（用于寻路）。某两个虚拟节点对应的物理节点的下标，///不好不好，这样这个方法的作用跟floyd没啥区别，没必要添加的一个函数。
//	 * 返回类型：*/
//	public static void linkEmbedding(Graph pn,int s,int d){
//		
//	}
}
