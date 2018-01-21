package vnr.bandit;

import java.util.HashMap;
import java.util.Map;

public class ProbabilitySelected {
	/**
	 * 作用：以相似度比例为权重，概率选择某个对应节点
	 * 参数：待映射节点与各个节点相似度数组*/
	public static int proSelected(Map<Integer,Double> sim) {
	
		double tol=0;
		int re=-1;
		Map<Integer,Double> temp=new HashMap<Integer,Double>();
		/*遍历sim的value，并求和
		 */
		for(double v:sim.values()) {
			tol+=v;
		}
		
		/*
		 * 遍历sim，分别计算每一个被选中的概率，并存入map
		 */
		for(Map.Entry<Integer, Double> entry:sim.entrySet()) {
			temp.put(entry.getKey(), entry.getValue()/tol);
		}
//		for(Map.Entry<Integer, Double> entry:temp.entrySet()) {
//			System.out.println("节点"+entry.getKey()+"的选中概率"+entry.getValue());
//		}
		//有用注释
//		
		double ran=Math.random();
//		System.out.println("随机数"+ran);
		//有用注释
		

		for(Map.Entry<Integer, Double> entry:temp.entrySet()) {
			ran=ran-entry.getValue();
			if(ran<=0) {
				re=entry.getKey();
				break;
			}
			
		}

		return re;
	}

}
