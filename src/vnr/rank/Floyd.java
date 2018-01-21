package vnr.rank;

import java.util.List;
import java.util.Stack;

import vnr.graph.Graph;

public class Floyd {
	/**
	 * 作用：计算两个节点之间的最短路径<p>
	 * 参数：起始、终止节点下标,节点所在图，<p>
	 * 返回类型：路径list*/

	/**
	 * @author Jing
	 * @param g 待求最短路径的图
	 * @param s 路径的起始节点
	 * @param d 路径的目的节点
	 * @param path 起始终止节点之间的路径（用于将结果带出方法）
	 * @return 路径长度*/
	public static int floyd(Graph g,int s,int d,Stack<Integer> path) {
		int[][] distance=new int[g.getNumOfNode()][g.getNumOfNode()];//用于存储整个图中每对节点之间的最短路径距离，初始时候二位数据的值是物理拓扑的边信息。
		int[][] p=new int[g.getNumOfNode()][g.getNumOfNode()];//floyd算法中为了记录路径
		
		int n=g.getNumOfNode();
		
		//初始化，到v的距离，直接相连的是值，自己是0，没有的事weight，直接getweight就成，注意把所有的标记为未被访问，
		try{
			/*
			 * 初始化distance
			 */
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++) {
					distance[i][j]=g.getWeight(i, j);
				}
			}
			
			/*
			 * 初始化path			
			 */
			for(int i=0;i<n;i++){
				for(int j=0;j<n;j++){
					if(g.getWeight(i, j)==g.getMax()){
						p[i][j]=-1;
					}else{
						p[i][j]=i;
					}
				}
			}
			
			
			for(int k=0;k<n;k++){
				for(int i=0;i<n;i++){
					for(int j=0;j<n;j++){
						if(distance[i][j]>distance[i][k]+distance[k][j]) {
							distance[i][j]=distance[i][k]+distance[k][j];
							p[i][j]=p[k][j];
						}
					}
				}
			}
			
			if(path!=null) {
				int m;//m用作查找路径的中间值，
				m=d;
				path.push(d);
				while(m!=s) {
					m=p[s][m];
					path.push(m);
				}
				System.out.println("floyd里面的计算的路径长度"+distance[s][d]);
				System.out.println("floyd___路径映射结果：");
			}
			
		}catch(Exception e){
			System.out.println("Floyd:---"+e);
		}
		return distance[s][d];	
	}
}
