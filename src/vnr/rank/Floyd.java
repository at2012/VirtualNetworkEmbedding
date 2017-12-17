package vnr.rank;

import java.util.List;
import java.util.Stack;

import vnr.graph.Graph;

public class Floyd {
	/**
	 * 作用：计算两个节点之间的最短路径<p>
	 * 参数：起始、终止节点下标,节点所在图，<p>
	 * 返回类型：路径list*/
//	public static List<Integer> floyd(Graph g,int s,int d,int[][] distance){
	/**
	 * @author Jing
	 * @param g 待求最短路径的图
	 * @param s 路径的起始节点
	 * @param d 路径的目的节点
	 * @param path 起始终止节点之间的路径（用于将结果带出方法）
	 * @return 路径长度*/
//	public static int floyd(Graph g,int s,int d,List<Integer> path){
	public static int floyd(Graph g,int s,int d,Stack<Integer> path) {
		
//		for(int i)
//		List<Integer> p=new LinkedList<Integer>();
		
		
		
		int[][] distance=new int[g.getNumOfNode()][g.getNumOfNode()];//用于存储整个图中每对节点之间的最短路径距离，初始时候二位数据的值是物理拓扑的边信息。
//		Stack<Integer> stack=new Stack<Integer>	();//用于将路径倒序后加入path 的list
		int[][] p=new int[g.getNumOfNode()][g.getNumOfNode()];//floyd算法中为了记录路径
		
		int n=g.getNumOfNode();
//		int[][] distance=new int[g.getNumOfNode()][g.getNumOfNode()];

//		int temp=0;
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
			
//			System.out.println("dis复制之后");
//			for(int i=0;i<n;i++) {
//				for(int j=0;j<n;j++) {
//					System.out.print(distance[i][j]+"\t");
//				}
//				System.out.println();
//			}
			
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
//			System.out.println("path初始化之后");
//			for(int i=0;i<n;i++) {
//				for(int j=0;j<n;j++) {
//					System.out.print(p[i][j]+"\t");
//				}
//				System.out.println();
//			}
			
			
			for(int k=0;k<n;k++){
				for(int i=0;i<n;i++){
					for(int j=0;j<n;j++){
//						if(!(distance[i][k]==maxWeight || distance[k][j]==maxWeight) && distance[i][j]>distance[i][k]+distance[k][j] && i!=j){
						if(distance[i][j]>distance[i][k]+distance[k][j]) {
							distance[i][j]=distance[i][k]+distance[k][j];
							p[i][j]=p[k][j];
						}
					}
				}
			}
			
			
			int m;//m用作查找路径的中间值，
			m=d;
			path.push(d);
			while(m!=s) {
				m=p[s][m];
				path.push(m);
			}
//			System.out.println("stack内容："+d+"入栈");
//			while(p[s][d]!=s){
//				stack.push(p[s][d]);
////				System.out.println("stack内容："+path[s][d]+"入栈");
//				d=p[s][d];
//			}
//			stack.push(s);
//			System.out.println("stack内容："+path[s][d]+"入栈");
			System.out.println("stack大小："+path.size());
			System.out.println("floyd里面的计算的路径长度"+distance[s][d]);
			
//			for(int i=0;i<stack.size();i++){//启发，又是这个毛病！！！！！！stack.size()的值是会变会变会变得！！！！！没错！！！在循环里面用到出栈的的时候，，size的值是会变得，要把最初的size值单独存储作为循环的界限。突然想到，为什么不用while？？？
////				直到为空，是的，while循环比较好！！！
//				
////				System.out.println("栈顶元素"+stack.peek());
////				System.out.println("取得栈顶"+stack.pop());
//				System.out.println("到底执行几遍啊啊啊啊"+stack.size());
//				p.add(stack.pop());
//			}
			
			/*把路径存入list，*/
//			while(!stack.empty()){//待改进，这种先存站在出战到list得行为不好，而且意义不明确，而且很可能根本没意义，所以最好是弄懂程序以后改下。
//				path.add(stack.pop());
//			}
			System.out.println("floyd___路径映射结果：");
			while(!path.isEmpty()) {
				System.out.print(path.pop()+"->");
			}
//			for(int i=0;i<path.size();i++) {
//				System.out.print(path.get(i)+"\t");
//			}
			System.out.println();
	
			
		}catch(Exception e){
			System.out.println("Floyd:---"+e);
		}
		return distance[s][d];	
	}
	


}
