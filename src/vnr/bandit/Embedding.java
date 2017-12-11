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
				int lastSel=pn.getNumOfNode();//用于存储上一个VNR节点对应的物理节点,启发：这里定义是不合适的，因为这个值在遍历过程中是需要被带到下一次的。如果每次遍历到一个新的值都会被初始化的话就带不过去啦
			
			int t=0;
			Map.Entry<Integer, Double> entryFirst=null;//由于后面的if语句出现了未赋值错误，map.entry是接口，又没法初始化，所以用了null，
			//启发：不知道会不会运行出错，不过啊，在我的程序里，entryfirst一定是可以被正经赋值的，但愿不要出错。突然想通了空指针异常是怎么出现的，对于引用型，可能开始没有赋初始值，后面用的时候报错，所以就跑回来，用null赋值，后来及出现了空指针的异常，
			//不确定是不是这样，但是觉得有可能
			Map.Entry<Integer, Double> entry;
			for(Map.Entry<Integer, Double>e:embOrder.entrySet()) {
				int firstSel=pn.getNumOfNode();//用于存储第一个VNR节点对应的物理节点,这样赋初始值都可以检验是不是没有正确赋值
//				List<Integer> pathSel;//某对被选中的节点最终被选中的路径,这个变量要放到if里面去定义，因为每次都有新值
				int pathLength=5000;//用于存储对应的物理路径的长度,待改进，这里的；路径长度选的是长度上线，直接用5000不好
				
				boolean[] fTemp=new boolean[pn.getNumOfNode()];//用于标志这个节点在回退过程中，有么有被映射过
				int firsTemp;//用于临时存储第一个虚拟节点某次被选中的物理节点
				int lasTemp;//用于临时存储某个虚拟节点被临时选中的物理节点
				//启发：上面两个temp变量是从下面if里面弄上来的，也不知道哪个好，但是那个pathTemp我不敢弄上来，怕前面的值会影响后面
				
				
				
				
				if(t==0) {
					entryFirst=e;
				}else if(t==1) {
					entry=e;
//					int firsTemp;//用于临时存储第一个虚拟节点某次被选中的物理节点
//					int lasTemp;//用于临时存储某个虚拟节点被临时选中的物理节点
					
					List<Integer> pathSel;//某对被选中的节点最终被选中的路径
					
					for(int j=0;j<3;j++){
//						List<Integer> pathTemp=new LinkedList<Integer>();//用于存储被临时选中的路径；
						Stack<Integer> pathTemp=new Stack<Integer>();//用于存储被临时选中的路径；
						
						{						
							double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，++但是，每个i都有可能被记录，所以用这么大的数组是可以的。
							for(int i=0;i<pn.getNumOfNode();i++){
								if(!f[i] && pn.getCpu(i)>=vn.getCpu(entryFirst.getKey())){//物理网络节点cpu大于VNR节点cpu
//									System.out.println("embedding__new__待映射点1与物理节点中满足要求的节点"+i+"的相似度:"+Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i)));
									sim[i]=Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i));
									//					g.getCpu(i)>g.getCpu(entryFirst.getKey()))
								}
							}
							firsTemp=ProbabilitySelected.proSelected(sim);
							fTemp[firsTemp]=true;
							System.out.println("embedding___此次选中节点"+firsTemp);
						}
						/*启发：这里用了两个程序块，因为一个待映射节点对应一个sim数组，这个数组在存储的时候我是用节点下标作为数组下标的，为了不让上一次被允许的几点的sim值影响下一个节点的值，
						 * 所以sim这个数组，对于每一个待映射节点 都需要从空的数组开始，可以每次用之前更新赋值为0，但是我选择了使用俩程序块，在每个块里定义数组，这样更新，*/
						{
							double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，++但是，每个i都有可能被记录，所以用这么大的数组是可以的。
							for(int i=0;i<pn.getNumOfNode();i++){
								if(!fTemp[i] && !f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//物理网络节点cpu大于VNR节点cpu
//									System.out.println("embedding__new__待映射点2与物理节点中满足要求的节点"+i+"的相似度:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
									sim[i]=Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i));
									//					g.getCpu(i)>g.getCpu(entry.getKey()))
								}
							}
							lasTemp=ProbabilitySelected.proSelected(sim);
							System.out.println("embedding__此次选中节点："+lasTemp);
						}
						/*在路径映射中找到好的映射点*/
						int pathLengthTemp=Floyd.floyd(pn, firsTemp, lasTemp, pathTemp);
						if(pathLength>pathLengthTemp) {//如果新得到的路径长度小，那么就存储这个新的路径对应的起始终止节点，以及路径
//							pathLength=Floyd.floyd(pn, firsTemp, lasTemp, pathTemp);//启发：这里由于调用了两次的floyd，所以pathTemp是被赋值两次的
							pathLength=pathLengthTemp;
							firstSel=firsTemp;
							lastSel=lasTemp;
							pathSel=pathTemp;
						}
						
//						/*在这里就可以把映射结果加入到map里面啦*///这里还不行哦，还在循环里面，等到循环完了再去put
//						emb.put(entryFirst.getKey(), firstSel);
//						emb.put(entry.getKey(), lastSel);
					}
					
					/*在这里就可以把映射结果加入到map里面啦*/
					emb.put(entryFirst.getKey(), firstSel);
					emb.put(entry.getKey(), lastSel);
					f[firstSel]=true;//物理节点被最终选定后，记得标记下，后面不再选择
					f[lastSel]=true;
					System.out.println("embedding___最终映射结果__前两个节点"+firstSel+"--"+lastSel);
				}else {//对于除了前两个节点以外的其他节点的映射
					
//					List<Integer> pathTemp=new LinkedList<Integer>();//用于存储被临时选中的路径；
					Stack<Integer> pathTemp=new Stack<Integer>();//用于存储被临时选中的路径；
					List<Integer> pathSel;//某对被选中的节点最终被选中的路径
					entry=e;
					
					for(int j=0;j<3;j++) {
						
						double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，++但是，每个i都有可能被记录，所以用这么大的数组是可以的。
						for(int i=0;i<pn.getNumOfNode();i++){
							if(!f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//物理网络节点cpu大于VNR节点cpu
//								System.out.println("embedding__new__待映射点2与物理节点中满足要求的节点"+i+"的相似度:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
								sim[i]=Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i));
								//					g.getCpu(i)>g.getCpu(entry.getKey()))
							}
						}
						lasTemp=ProbabilitySelected.proSelected(sim);
						System.out.println("embedding____此次选中节点：___else:"+lasTemp);
						/*在路径映射中找到好的映射点*/
						int pathLengthTemp=Floyd.floyd(pn, lastSel, lasTemp, pathTemp);
						if(pathLength>pathLengthTemp) {//如果新得到的路径长度小，那么就存储这个新的路径对应的起始终止节点，以及路径
							pathLength=pathLengthTemp;
							lastSel=lasTemp;
							pathSel=pathTemp;
						}
					}
					
					
					/*在这里就可以把映射结果加入到map里面啦*/
//					emb.put(entryFirst.getKey(), firstSel);
					emb.put(entry.getKey(), lastSel);
					f[lastSel]=true;
					
				}
				
				t++;
					
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
//			
//			
//			Iterator<Map.Entry<Integer,Double>> it=embOrder.entrySet().iterator();//用于遍历整个带映射网络的节点
//			
////			List<Integer> pathTemp=new LinkedList<Integer>();//启发：这个变量在这里定义不好，因为每一次的循环都需要pathTemp是空的
//			
//			int t=1;//用于在while循环中判断节点是不是前两个节点
//			int sTemp;//由于对于一个节点要映射多次，所以用于临时存储所有可能结果。
//			int sTemp2;//都是因为前两个节点的处理顺序跟其他 节点的处理顺序不太一致，都需要单独存储。
//			/*开始挨个映射节点和链路，只是最初的两个节点的映射要不太一样*/
//			while(it.hasNext()){
////			for(each)
//				
//				System.out.println("这里来了几次啊      ！！！！！");
//				if(t<2){
//					Map.Entry<Integer, Double> entry =it.next();
//					Map.Entry<Integer, Double> entry2=it.next();
//					System.out.println("ititititi"+entry);
//					System.out.println("ititititi22222"+entry2);
//					int lTemp=pn.getNumOfEdge();//用于存储某次节点映射之后路径的
//					int disTemp=5000;//用于存储某条暂时被选中的路径的长度，初始化为最大路径值
//					List<Integer> pathTemp=new LinkedList<Integer>();
////					double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，++但是，每个i都有可能被记录，所以用这么大的数组是可以的。
//					//sim作用：由于需要计算每个符合要求的节点的相似度的所占的比例，作为概率选择的概率，所以需要存储与所有的符合要求的点的相似度，用于求和后再计算比值。启发，想知道有没有别的办法可以直接就计算了比值了，感觉是没有的，嘿嘿
//					//int[][] disTemp = new int[pn.getNumOfNode()][pn.getNumOfNode()];
//					boolean[] fTemp=new boolean[pn.getNumOfNode()];//用于标志这个节点在回退过程中，有么有被映射过
//					{						
//						double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，++但是，每个i都有可能被记录，所以用这么大的数组是可以的。
//						for(int i=0;i<pn.getNumOfNode();i++){
//							if(!f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//物理网络节点cpu大于VNR节点cpu
//								System.out.println("embedding__new__待映射点1与物理节点中满足要求的节点"+i+"的相似度:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
//								sim[i]=Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i));
//								//					g.getCpu(i)>g.getCpu(entry.getKey()))
//							}
//						}
//						sTemp=ProbabilitySelected.proSelected(sim);
//						fTemp[sTemp]=true;
//						System.out.println("此次选中节点"+sTemp);
//					}
//					/*启发：这里用了两个程序块，因为一个待映射节点对应一个sim数组，这个数组在存储的时候我是用节点下标作为数组下标的，为了不让上一次被允许的几点的sim值影响下一个节点的值，
//					 * 所以sim这个数组，对于每一个待映射节点 都需要从空的数组开始，可以每次用之前更新赋值为0，但是我选择了使用俩程序块，在每个块里定义数组，这样更新，*/
//					{
//						double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，++但是，每个i都有可能被记录，所以用这么大的数组是可以的。
//						for(int i=0;i<pn.getNumOfNode();i++){
//							if(!fTemp[i] && !f[i] && pn.getCpu(i)>=vn.getCpu(entry2.getKey())){//物理网络节点cpu大于VNR节点cpu
//								System.out.println("embedding__new__待映射点2与物理节点中满足要求的节点"+i+"的相似度:"+Similarity.cos(vn.getNode(entry2.getKey()),pn.getNode(i)));
//								sim[i]=Similarity.cos(vn.getNode(entry2.getKey()),pn.getNode(i));
//								//					g.getCpu(i)>g.getCpu(entry.getKey()))
//							}
//						}
//						sTemp2=ProbabilitySelected.proSelected(sim);
//						System.out.println("此次选中节点："+sTemp2);
//					}
//					
////					pathTemp=Floyd.floyd(pn, sTemp, sTemp2,disTemp);
////					lTemp=disTemp[sTemp][sTemp2];
//					System.out.println("路径长度"+Floyd.floyd(pn, sTemp, sTemp2, pathTemp));
//										
//					for(int i=0;i<pathTemp.size();i++){
//						System.out.println("临时路径存储测试："+pathTemp.get(i));
//					}
//					
//					
//
//
//					
//					//调用概率选择方法，最终选定映射节点
////					sTemp=ProbabilitySelected.proSelected(sim);//a啊呀呀呀，傻子傻子傻子，因为是随机选择，你在这里调用方法随机选择了一个，，下面又调用方法，就又随机啦，这两个值很有可能不一样的啊！！！！！这种随机产生的结果一定要记得用变量存储下。
//					
////					if(!)
//					
//					System.out.println("测试测试测试哟"+entry.getKey()+":"+entry.getValue());
//					
//				}else{
//					/*当处理完前两个节点之后，开始处理后面的节点处理方式相同*/
//					System.out.println("这里过来过吗？？？");
//					
//				}
//				
//				t++;
//				System.out.println("这里t开始增加"+t);
//				
//			}
			
			
			
//			
//			for(Map.Entry<Integer, Double> entry:embOrder.entrySet()){//对于待映射队列中的每一个key-value，进行映射
//				double[] sim=new double[pn.getNumOfNode()];//待改进：这里不好。这个数组肯定用不到这么大，
//				
//				
//	
//				
//				
//				for(int i=0;i<pn.getNumOfNode();i++){
//					if(!f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//物理网络节点cpu大于VNR节点cpu
//						System.out.println("embedding__待映射点与物理节点中满足要求的节点"+i+"的相似度:"+Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
//						sim[i]=Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i));
//	//					g.getCpu(i)>g.getCpu(entry.getKey()))
//					}
//				}
//				s=ProbabilitySelected.proSelected(sim);//a啊呀呀呀，傻子傻子傻子，因为是随机选择，你在这里调用方法随机选择了一个，，下面又调用方法，就又随机啦，这两个值很有可能不一样的啊！！！！！这种随机产生的结果一定要记得用变量存储下。
////				System.out.println("embedding_最终选中节点："+ProbabilitySelected.proSelected(sim));
////				System.out.println("embedding_最终选中节点："+s);
//				emb.put(entry.getKey(), s);
//				System.out.println("embedding_最终选中节点："+entry.getKey()+"--"+s);
//				f[s]=true;
//			}
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
