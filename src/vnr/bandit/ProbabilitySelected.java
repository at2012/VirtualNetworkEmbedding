package vnr.bandit;

import java.util.HashMap;
import java.util.Map;

public class ProbabilitySelected {
	/**
	 * ���ã������ƶȱ���ΪȨ�أ�����ѡ��ĳ����Ӧ�ڵ�
	 * ��������ӳ��ڵ�������ڵ����ƶ�����*/
	public static int proSelected(Map<Integer,Double> sim) {
	
		double tol=0;
		int re=-1;
		Map<Integer,Double> temp=new HashMap<Integer,Double>();
		/*����sim��value�������
		 */
		for(double v:sim.values()) {
			tol+=v;
		}
		
		/*
		 * ����sim���ֱ����ÿһ����ѡ�еĸ��ʣ�������map
		 */
		for(Map.Entry<Integer, Double> entry:sim.entrySet()) {
			temp.put(entry.getKey(), entry.getValue()/tol);
		}
//		for(Map.Entry<Integer, Double> entry:temp.entrySet()) {
//			System.out.println("�ڵ�"+entry.getKey()+"��ѡ�и���"+entry.getValue());
//		}
		//����ע��
//		
		double ran=Math.random();
//		System.out.println("�����"+ran);
		//����ע��
		

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
