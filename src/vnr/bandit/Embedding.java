package vnr.bandit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
		LinkedHashMap<Integer,Integer> emb=new LinkedHashMap<Integer,Integer>();//用于存储最终的映射结果,用于返回结果
		boolean[] f=new boolean[pn.getNumOfNode()];//用于标志这个节点有没有被当前虚拟网络映射过
		int s;//用于存储被选中的节点的下标，因为是随机产生的，所以多次调用方法
		

//		File resFile =new File("result.txt");
		try{
			
			BufferedWriter resOut = new BufferedWriter(new FileWriter("embed.txt"));
			
			/*映射第一个和第二个节点，随即选择三次后选择最好的*/
			System.out.println("Embedding__开始映射节点");
			int lastSel=pn.getNumOfNode();//用于存储上一个VNR节点对应的物理节点,启发：这里定义是不合适的，因为这个值在遍历过程中是需要被带到下一次的。如果每次遍历到一个新的值都会被初始化的话就带不过去啦
			
			int t=0;//这里是为了补上当前这种map遍历的方法里面，没法轻松回退上一个的坑。用t来计数，如果是第一个节点，需要特殊处理。
			Map.Entry<Integer, Double> entryFirst=null;//由于后面的if语句出现了未赋值错误，map.entry是接口，又没法初始化，所以用了null，
			//启发：不知道会不会运行出错，不过啊，在我的程序里，entryfirst一定是可以被正经赋值的，但愿不要出错。突然想通了空指针异常是怎么出现的，对于引用型，可能开始没有赋初始值，后面用的时候报错，所以就跑回来，用null赋值，后来及出现了空指针的异常，
			//不确定是不是这样，但是觉得有可能
			Map.Entry<Integer, Double> entry;
			for(Map.Entry<Integer, Double>e:embOrder.entrySet()) {//这里涉及遍历map，还有别的办法的。
				int firstSel=pn.getNumOfNode();//用于存储第一个VNR节点对应的物理节点,这样赋初始值都可以检验是不是没有正确赋值
				int pathLength=pn.getMax();//用于存储对应的物理路径的长度
				
				int firsTemp;//用于临时存储第一个虚拟节点某次被选中的物理节点
				int lasTemp;//用于临时存储某个虚拟节点被临时选中的物理节点
				//启发：上面两个temp变量是从下面if里面弄上来的，也不知道哪个好，但是那个pathTemp我不敢弄上来，怕前面的值会影响后面
				
								
				if(t==0) {//对于第一个待映射节点
					entryFirst=e;
				}else if(t==1) {//对于第二个待映射节点，，第一对（即前两个节点）是需要特殊处理的。
					entry=e;
					Stack<Integer> pathSel=new Stack<Integer>();//某对被选中的节点最终被选中的路径
					
					for(int j=0;j<3;j++){//这里限制了回退多少次
//						List<Integer> pathTemp=new LinkedList<Integer>();//用于存储被临时选中的路径；
						Stack<Integer> pathTemp=new Stack<Integer>();//用于存储被临时选中的路径；
						boolean[] fTemp=new boolean[pn.getNumOfNode()];//用于标志这个节点在回退过程中，有么有被映射过,启发，这个是从外面调过来的，因为每次回退过程都要保证他是空的，没有被赋值过的。
						{						
							Map<Integer,Double> sim=new HashMap<Integer,Double>();
							for(int i=0;i<pn.getNumOfNode();i++){
								if(!f[i] && pn.getCpu(i)>=vn.getCpu(entryFirst.getKey())){//物理网络节点cpu大于VNR节点cpu
									sim.put(i, Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i)));
								}
							}
							firsTemp=ProbabilitySelected.proSelected(sim);
							fTemp[firsTemp]=true;
							System.out.println("embedding___此次选中节点"+firsTemp);
						}
						/*启发：这里用了两个程序块，因为一个待映射节点对应一个sim数组，这个数组在存储的时候我是用节点下标作为数组下标的，为了不让上一次被允许的几点的sim值影响下一个节点的值，
						 * 所以sim这个数组，对于每一个待映射节点 都需要从空的数组开始，可以每次用之前更新赋值为0，但是我选择了使用俩程序块，在每个块里定义数组，这样更新，*/
						
						/*
						 * 以下模块首先计算所有满足要求的节点的相似度，然后通过概率选中选择节点。
						 */
						{
//							ArrayList<Double> sim=new ArrayList<Double>();//待改进：这里不好。这个数组肯定用不到这么大，++但是，每个i都有可能被记录，所以用这么大的数组是可以的。
							Map<Integer,Double> sim=new HashMap<Integer,Double>();
							for(int i=0;i<pn.getNumOfNode();i++){
								if(!fTemp[i] && !f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//物理网络节点cpu大于VNR节点cpu
									sim.put(i, Similarity.cos(vn.getNode(entry.getKey()),pn.getNode(i)));
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
					}
					
					/*在这里就可以把映射结果加入到map里面啦*/
//					System.out.println("test222222:"+pathSel.empty());
					emb.put(entryFirst.getKey(), firstSel);
					emb.put(entry.getKey(), lastSel);
					/*映射节点*/
					resOut.write(entryFirst.getKey()+" "+firstSel+"\r\n"+entry.getKey()+" "+lastSel+"\r\n");//把映射结果写入文本，作用 类似上面两句
					/*映射链路*/
//					while(!pathSel.empty()) {
//						resOut.write(pathSel.pop()+" ");
//					}
//					resOut.write("\r\n");
//					resOut.write(entry.getKey()+" "+lastSel+" ");

					
					f[firstSel]=true;//物理节点被最终选定后，记得标记下，后面不再选择
					f[lastSel]=true;
					
					System.out.println("embedding___最终映射结果__前两个节点"+firstSel+"--"+lastSel);
				}else {//对于除了前两个节点以外的其他节点的映射
					
					Stack<Integer> pathSel=new Stack<Integer>();//某对被选中的节点最终被选中的路径
					int lastSelTemp=lastSel;
					entry=e;
					
					//放到这里不行，因为entry已经改变
					
					for(int j=0;j<3;j++) {
						Stack<Integer> pathTemp=new Stack<Integer>();//用于存储被临时选中的路径；
						Map<Integer,Double> sim=new HashMap<Integer,Double>();
						for(int i=0;i<pn.getNumOfNode();i++){
							if(!f[i] && pn.getCpu(i)>=vn.getCpu(entry.getKey())){//物理网络节点cpu大于VNR节点cpu
								sim.put(i, Similarity.cos(vn.getNode(entryFirst.getKey()),pn.getNode(i)));
							}
						}
						lasTemp=ProbabilitySelected.proSelected(sim);
						System.out.println("embedding____此次选中节点：___else:"+lasTemp);
						/*在路径映射中找到好的映射点*/
						int pathLengthTemp=Floyd.floyd(pn, lastSelTemp, lasTemp, pathTemp);
						//启发：不能在这里就把lastSel更改，因为后面往回回溯的时候是需要用到的！！！所以从下面改成了上面

						if(pathLength>pathLengthTemp) {//如果新得到的路径长度小，那么就存储这个新的路径对应的起始终止节点，以及路径
							pathLength=pathLengthTemp;
							lastSel=lasTemp;
							pathSel=pathTemp;
						}
					}
					
					
					/*在这里就可以把映射结果加入到map里面啦*/
					emb.put(entry.getKey(), lastSel);
					/*映射节点*/
					resOut.write(entry.getKey()+" "+lastSel+" "+"\r\n");

					/*映射链路*/
//					while(!pathSel.empty()) {
//						resOut.write(pathSel.pop()+" ");
//					}
//					resOut.write("\r\n");
//					resOut.write(entry.getKey()+" "+lastSel+" ");//开始用于方便下一行节点和链路的记录，后来改了结果写入方式
					f[lastSel]=true;
				}
				t++;		
			}
			
			resOut.close();
		}catch(Exception e){
			System.out.println("Embedding__embedding():"+e);
		}
		return emb;
	}


}
