package vnr.bandit;

public class ProbabilitySelected {
	/**
	 * ���ã������ƶȱ���ΪȨ�أ�����ѡ��ĳ����Ӧ�ڵ�
	 * ��������ӳ��ڵ�������ڵ����ƶ�����*/
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
//				if(sim[i]/tol<=ran && ran<=sim[i]+sim[i+1]/tol)//�߼�������
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
