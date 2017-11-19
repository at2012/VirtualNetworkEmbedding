package vnr.rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import vnr.graph.Graph;

public class NodeRank {
	
	public Graph g;//启发：定义一个静态变量，在打算用这个类下面的方法的时候，先给g赋值，然后在调用方法。这样比pagerank工程里面的每个方法都传递一遍g要更好，节省空间。但是有个问题是，当要对多个图的rank的时候，需要考虑g的值是要跟着变的。
//	public Graph graph;
	//返回一个map
	/**返回的是一个list<map.entry>，并且已经按照降序排序*/
	/**返回的是一个list<map.entry>，并且已经按照降序排序*/
	/**
	*作用：计算VNR中各个节点重要度值，并按照该值进行排名
	*参数：方法没有参数，而是在rank类中定义了静态变量用于将待计算的VNR传递到方法内
	*返回类型：为了便于对各个节点对应的重要度值进行排序，采用列表存储map的key--value值*/
	
	
	public NodeRank(Graph g){
		this.g=g;
		
	}
	public List<Map.Entry<Integer,Double>> rank()throws Exception{//用的静态的，想想啥时候用static啥时候用隐式啥啥的。就是那个只需要new，不需要指向对象引用的。
		int n=g.getNumOfNode();
		double[] init=new double[g.getNumOfNode()];//排名算法执行前节点的初始化值
		double[] temp=new double[g.getNumOfNode()];//节点初始化值量化时用到的中间量，用于计算存储初始化值得和，
		
		double tolInit=0.00;//用作量化的分母。
		double[] cpuPro=new double[g.getNumOfNode()];
		double tolCpu=0.00;
		
		
		Map<Integer,Double> map=new HashMap<Integer,Double>();
		
		
		for(int i=0;i<n;i++){
			tolCpu+=g.getCpu(i);
		}
		for(int i=0;i<n;i++){
			cpuPro[i]=g.getCpu(i)/tolCpu;
		}
		for(int i=0;i<g.getNumOfNode();i++){
			temp[i]=novM(i);
			tolInit+=temp[i];
		}
		
		//对于每一个节点，计算自己在全局中的初始排名
		for(int i=0;i<g.getNumOfNode();i++){
//			init[i]=temp[i]/tolInit;
			init[i]=(double)(Math.round(temp[i]/tolInit*10000))/10000;
//			System.out.println("init:"+init[i]);
		}
		
		

		for(int i=0;i<n;i++){
			map.put(i, init[i]);
		}
		
		int z=35;//排名计算的循环次数
		
		while(z>0){
			
			for(int i=0;i<n;i++){//修改：这里很多的地方用到节点数量，可以考虑增加一个变量
				map.put(i, 0.6*cpuPro[i]);
//				rank[i]=0.6*cpuPro[i];
				for(int j=0;j<n;j++){
					if(i!=j){
						double t=map.get(i);
						map.put(i, t+0.4*nov(i,j)*map.get(j));
//						rank[i]+=0.4*nov(i,j)*rank[j];
					}
				}
			}
			
			z--;
		}
		//y以上终于完成了初始化pr
		
//		
//		Map<Integer,Double> map=new HashMap<Integer,Double>();
//		for(int i=0;i<g.getNumOfNode();i++){
//			map.put(i, rank[i]);
//		}
//		
		//test
//		for(Double value:map.values()){
//			System.out.println("nodeRank:"+value);
//		}
		ArrayList<Map.Entry<Integer,Double>> entries=sortMap(map);
		//test
//		for(int i=0;i<g.getNumOfNode();i++){
//			System.out.println("Node"+entries.get(i).getKey()+":"+entries.get(i).getValue());
//		}
		for(int i=0;i<g.getNumOfNode();i++){
			map.put(entries.get(i).getKey(), entries.get(i).getValue());//这个语句，把list又转换成了map，参考下下面被注释掉的一个输出，便于理解。。。。经过测试，这个是不行的。
//			System.out.println("Node"+entries.get(i).getKey()+":"+entries.get(i).getValue());
		}
//		Arrays.sort(rank);//这里对rank进行下sort，便于后续用，注意：sort之后，节点下标和排名值之间没有了对应关系，所以存储下标是不行的。
		return entries;
	}
/**计算两个节点间影响。*/
	public double nov(int m,int n)throws Exception{
//		double novM=0;
		int dis=Dijskra.dijskra(g, m, n);
		return (double)g.getCpu(m)*g.getCpu(n)/(double)(dis*dis)*0.01;//这里乘的0.01是在把代码初步实现运行之后，发现结果不理想，挨个排查发现，这一步的值到后来很容易变得特别特别大，不好收敛，于是，用0.01进行修正，让他能收敛
	} 
/**全局其他节点对某个节点的综合影响*/
	public double novM(int m)throws Exception{
		double novM=0;
		
		for(int i=0;i<g.getNumOfNode();i++){
			if(m!=i){
				int dis=Dijskra.dijskra(g, m, i);
				novM=novM+nov(m,i);
			}
		}
		return novM;
	}
	/**
	 *启发，这里考虑下要不要把开始的时候节点就存储在map里面
	 *对map进行降序排序*/
	public static ArrayList<Map.Entry<Integer,Double>> sortMap(Map map){
//		ArrayList re=new ArrayList();
		List<Map.Entry<Integer,Double>> entries =new ArrayList<Map.Entry<Integer,Double>>(map.entrySet());
		
		
		Collections.sort(entries,new Comparator<Map.Entry<Integer,Double>>(){
			public int compare(Map.Entry<Integer, Double> obj1,Map.Entry<Integer, Double> obj2){
//				return (int)(obj2.getValue()-obj1.getValue());
				if(obj1.getValue()>obj2.getValue())
					return -1;
				else if(obj1.getValue()==obj2.getValue())
					return 0;
				else
					return 1;
			}
		});
		return (ArrayList<Entry<Integer,Double>>) entries;
	}

}
