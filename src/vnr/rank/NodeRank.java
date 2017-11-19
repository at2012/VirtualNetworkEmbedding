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
	
	public Graph g;//����������һ����̬�������ڴ��������������ķ�����ʱ���ȸ�g��ֵ��Ȼ���ڵ��÷�����������pagerank���������ÿ������������һ��gҪ���ã���ʡ�ռ䡣�����и������ǣ���Ҫ�Զ��ͼ��rank��ʱ����Ҫ����g��ֵ��Ҫ���ű�ġ�
//	public Graph graph;
	//����һ��map
	/**���ص���һ��list<map.entry>�������Ѿ����ս�������*/
	/**���ص���һ��list<map.entry>�������Ѿ����ս�������*/
	/**
	*���ã�����VNR�и����ڵ���Ҫ��ֵ�������ո�ֵ��������
	*����������û�в�����������rank���ж����˾�̬�������ڽ��������VNR���ݵ�������
	*�������ͣ�Ϊ�˱��ڶԸ����ڵ��Ӧ����Ҫ��ֵ�������򣬲����б�洢map��key--valueֵ*/
	
	
	public NodeRank(Graph g){
		this.g=g;
		
	}
	public List<Map.Entry<Integer,Double>> rank()throws Exception{//�õľ�̬�ģ�����ɶʱ����staticɶʱ������ʽɶɶ�ġ������Ǹ�ֻ��Ҫnew������Ҫָ��������õġ�
		int n=g.getNumOfNode();
		double[] init=new double[g.getNumOfNode()];//�����㷨ִ��ǰ�ڵ�ĳ�ʼ��ֵ
		double[] temp=new double[g.getNumOfNode()];//�ڵ��ʼ��ֵ����ʱ�õ����м��������ڼ���洢��ʼ��ֵ�úͣ�
		
		double tolInit=0.00;//���������ķ�ĸ��
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
		
		//����ÿһ���ڵ㣬�����Լ���ȫ���еĳ�ʼ����
		for(int i=0;i<g.getNumOfNode();i++){
//			init[i]=temp[i]/tolInit;
			init[i]=(double)(Math.round(temp[i]/tolInit*10000))/10000;
//			System.out.println("init:"+init[i]);
		}
		
		

		for(int i=0;i<n;i++){
			map.put(i, init[i]);
		}
		
		int z=35;//���������ѭ������
		
		while(z>0){
			
			for(int i=0;i<n;i++){//�޸ģ�����ܶ�ĵط��õ��ڵ����������Կ�������һ������
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
		//y������������˳�ʼ��pr
		
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
			map.put(entries.get(i).getKey(), entries.get(i).getValue());//�����䣬��list��ת������map���ο������汻ע�͵���һ�������������⡣�������������ԣ�����ǲ��еġ�
//			System.out.println("Node"+entries.get(i).getKey()+":"+entries.get(i).getValue());
		}
//		Arrays.sort(rank);//�����rank������sort�����ں����ã�ע�⣺sort֮�󣬽ڵ��±������ֵ֮��û���˶�Ӧ��ϵ�����Դ洢�±��ǲ��еġ�
		return entries;
	}
/**���������ڵ��Ӱ�졣*/
	public double nov(int m,int n)throws Exception{
//		double novM=0;
		int dis=Dijskra.dijskra(g, m, n);
		return (double)g.getCpu(m)*g.getCpu(n)/(double)(dis*dis)*0.01;//����˵�0.01���ڰѴ������ʵ������֮�󣬷��ֽ�������룬�����Ų鷢�֣���һ����ֵ�����������ױ���ر��ر�󣬲������������ǣ���0.01��������������������
	} 
/**ȫ�������ڵ��ĳ���ڵ���ۺ�Ӱ��*/
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
	 *���������￼����Ҫ��Ҫ�ѿ�ʼ��ʱ��ڵ�ʹ洢��map����
	 *��map���н�������*/
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
