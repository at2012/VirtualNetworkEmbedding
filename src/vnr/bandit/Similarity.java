package vnr.bandit;
import vnr.graph.Node;

public class Similarity {
	/**
	 * ���ã����������ڵ�֮������Ƴ̶ȣ���cpu��degree�ĽǶ�����
	 * ���������Ƚ����ƶȵ������ڵ�*/
	public static double cos(Node m,Node n){
		double mc,nc;
		int md,nd;
		
		mc=m.getCpu();nc=n.getCpu();
		md=m.getDegree();nd=n.getDegree();
		return (mc*nc+md*nd)/(Math.sqrt(mc*mc+md*md)*Math.sqrt(nc*nc+nd*nd));
	}

}
