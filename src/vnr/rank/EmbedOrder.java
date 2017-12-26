package vnr.rank;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import vnr.graph.Graph;

public class EmbedOrder {
	
	/**
	 * 作用：得到最终的映射顺序。按照节点重要度值降序的顺序，实时结合当前待映射节点的一跳邻居，对节点key--value进行存储
	 * 参数：节点的key-value条目对应的list，VNR拓扑图（因为需要按照下标访问节点邻居）
	 * 返回类型：LinkedHashMap，这种类型的数据结构允许按照存储顺序访问entry*/
	public static LinkedHashMap<Integer,Double> embOrder(List<Map.Entry<Integer,Double>> rank,Graph g){
//	public static List<Map> embOrder(List<Map.Entry<Integer,Double>> rank,Graph g){
		
//		ArrayList<Map.Entry<Integer,Double>> embOrder=new ArrayList<Map.Entry<Integer,Double>>();
		LinkedHashMap<Integer,Double> embOrderTemp =new LinkedHashMap<Integer,Double>();//待改进，像下面描述的，这种方法感觉很笨，不好，看看有没有别的办法实现。
		//用linkedhashmap存储临时存储节点，因为这种类型的map，在遍历的时候按照value值进行访问。
		//在方法返回结果之前，通过遍历，把结果存储list里面，便于后面根据i进行访问。
		
		List<Map<Integer,Double>> embOrder = new LinkedList<Map<Integer,Double>>();//用于存储最终的返回结果，在最后，他是被按照value递减的顺序加入进list的。
		Map<Integer,Double> map=new HashMap<Integer,Double>();//用于给list添加key-value
//		Map.Entry<Integer, Double> e=new HashMap.Entry<Integer,Double>;
		
		boolean[] f=new boolean[rank.size()];//用于标志节点是否已经加入待映射队列

		try{
			for(int i=0;i<rank.size();i++){
				if(!f[i]){
					embOrderTemp.put(rank.get(i).getKey(),rank.get(i).getValue());
//					System.out.println("put_in_map_self:"+rank.get(i).getKey());
					f[i]=true;
					for(int j=0;j<g.getNumOfNode();j++){
						//下一行有费劲找到的逻辑错误，开始求边权重的时候，用的g.getweight(i,j),但是要注意，ij不是节点的编号哦！！！！！改成下面就对啦
						if(!f[j] && g.getWeight(rank.get(i).getKey(),rank.get(j).getKey())>0 && g.getWeight(rank.get(i).getKey(), rank.get(j).getKey())<5000){//待改进：这里的5000.是图的边的最大权重，要改下，改成用变量表示的。
							embOrderTemp.put(rank.get(j).getKey(), rank.get(j).getValue());//待改进：还有一个问题，如果是根据是不是一跳邻居而加入队列，这些邻居还有个先后得考虑呢，这里可以最后再改进
//							System.out.println("put_in_map_nei:"+rank.get(j).getKey());
							f[j]=true;
						}
					}
				}
				
			}
		}catch (Exception e){
			System.out.println("EmbedOrder__embOrder--"+e);
		}

		
		return embOrderTemp;
		
	}

}
