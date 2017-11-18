package vnr.bandit;

public class ProbabilitySelected {
	/**
	 * 作用：以相似度比例为权重，概率选择某个对应节点
	 * 参数：待映射节点与各个节点相似度数组*/
	public static int proSelected(double[] sim){
	
		double tol=0;
		int re=sim.length;
		double[] temp=new double[re];
		for(int i=0;i<sim.length;i++){
			tol+=sim[i];
		}
		for(int i=0;i<sim.length;i++){
			temp[i]=sim[i]/tol;
		}
		double ran=Math.random();
		
		double left=0;
		double right=temp[0];
		
		for(int i=0;i<sim.length;i++){
			if(left<=ran && ran<right){
				re=i;
				break;
			}else{
				left=right;
				right+=temp[i+1];
			}
			
		}
				
//		if(0<=ran && ran<sim[0]/tol)
//			re=0;
//		for(int i=1;i<sim.length;i++){
////			if(0<=ran && ran<sim[i])
////				return i;
//			if(i<sim.length-1){
//				if(sim[i]/tol<=ran && ran<=sim[i]+sim[i+1]/tol)//逻辑有问题
//					re=i;
//			}else{
//				re=sim.length-1;
//			}
////			else
////				continue;		
//		}
		
		return re;
	}

}
