package vnr.rank;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import vnr.graph.Graph;

public class Floyd {
	/**
	 * 作用：计算两个节点之间的最短路径<p>
	 * 参数：起始、终止节点下标,节点所在图，<p>
	 * 返回类型：路径list*/
	public static List<Integer> floyd(Graph g,int s,int d,int[][] distance){
//		for(int i)
		List<Integer> p=new LinkedList<Integer>();
		
		Stack<Integer> stack=new Stack<Integer>	();
		
		final int maxWeight=5000;
		int n=g.getNumOfNode();
//		int[][] distance=new int[g.getNumOfNode()][g.getNumOfNode()];
		int[][] path=new int[g.getNumOfNode()][g.getNumOfNode()];
//		int temp=0;
		//初始化，到v的距离，直接相连的是值，自己是0，没有的事weight，直接getweight就成，注意把所有的标记为未被访问，
		
		try{
			System.arraycopy(g.getEdges(), 0, distance, 0, g.getNumOfNode());
			
//			
//			for(int i=0;i<n;i++){
//				distance[i]=g.getWeight(s, i);
//			}
			
			
			for(int i=0;i<n;i++){
				for(int j=0;j<n;j++){
					if(g.getWeight(i, j)==maxWeight){
						path[i][j]=-1;
					}else{
						path[i][j]=i;
					}
				}
			}
			
			
			for(int k=0;k<n;k++){
				for(int i=0;i<n;i++){
					for(int j=0;j<n;j++){
						if(!(distance[i][k]==maxWeight || distance[k][j]==maxWeight) && distance[i][j]>distance[i][k]+distance[k][j]){
							distance[i][j]=distance[i][k]+distance[k][j];
							path[i][j]=path[k][j];
						}
					}
				}
			}
			
			stack.push(d);
//			System.out.println("stack内容："+d+"入栈");
			while(path[s][d]!=s){
				stack.push(path[s][d]);
//				System.out.println("stack内容："+path[s][d]+"入栈");
				d=path[s][d];
			}
			stack.push(s);
//			System.out.println("stack内容："+path[s][d]+"入栈");
			System.out.println("stack大小："+stack.size());
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
			while(!stack.empty()){//待改进，这种先存站在出战到list得行为不好，而且意义不明确，而且很可能根本没意义，所以最好是弄懂程序以后改下。
				p.add(stack.pop());
			}
			System.out.println("路径映射结果：");
			for(int i=0;i<p.size();i++) {
				System.out.print(p.get(i)+"\t");
			}
			System.out.println();
	
			
		}catch(Exception e){
			System.out.println("Floyd:---"+e);
		}

		return p;	
	}
	


}
