package vnr.bandit;
import vnr.graph.Node;

public class Similarity {
	/**
	 * 作用：计算两个节点之间的相似程度，从cpu和degree的角度评估
	 * 参数：待比较相似度的两个节点*/
	public static double cos(Node m,Node n){
		double mc,nc;
		int md,nd;
		
		mc=m.getCpu();nc=n.getCpu();
		md=m.getDegree();nd=n.getDegree();
		return (mc*nc+md*nd)/(Math.sqrt(mc*mc+md*md)*Math.sqrt(nc*nc+nd*nd));
	}

}
