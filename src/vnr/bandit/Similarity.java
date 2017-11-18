package vnr.bandit;
import vnr.graph.Node;

public class Similarity {
	/**
	 * ���ã����������ڵ�֮������Ƴ̶ȣ���cpu��degree�ĽǶ�����
	 * ���������Ƚ����ƶȵ������ڵ�*/
	public static double cos(Node m,Node n){
		int mc,nc;
		int md,nd;
		
//		(m.getCpu()*n.getCpu()+m.getDegree()*n.getDegree())/(Math.sqrt(m.));
		mc=m.getCpu();nc=n.getCpu();
		md=m.getDegree();nd=n.getDegree();
//		
//		System.out.println("mc*nc+md*nd="+(mc*nc+md*nd));
//		System.out.println("md && nd:"+md+" "+nd);
		return (mc*nc+md*nd)/(Math.sqrt(mc*mc+md*md)*Math.sqrt(nc*nc+nd*nd));
	}

}
