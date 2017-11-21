package vnr.rank;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import vnr.graph.Graph;

public class Floyd {
	public static List<Integer> floyd(Graph g,int s,int d){
//		for(int i)
		List<Integer> p=new LinkedList<Integer>();
		
		Stack<Integer> stack=new Stack<Integer>	();
		
		final int maxWeight=5000;
		int n=g.getNumOfNode();
		int[][] distance=new int[g.getNumOfNode()][g.getNumOfNode()];
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
			
//			System.out.println(stack.empty());
			

//			for(int i=0;i<stack.size();i++){//启发，又是这个毛病！！！！！！stack.size()的值是会变会变会变得！！！！！没错！！！在循环里面用到出栈的的时候，，size的值是会变得，要把最初的size值单独存储作为循环的界限。突然想到，为什么不用while？？？
////				直到为空，是的，while循环比较好！！！
//				
////				System.out.println("栈顶元素"+stack.peek());
////				System.out.println("取得栈顶"+stack.pop());
//				System.out.println("到底执行几遍啊啊啊啊"+stack.size());
//				p.add(stack.pop());
//			}
			
			
			while(!stack.empty()){
				p.add(stack.pop());
			}
	
			
		}catch(Exception e){
			System.out.println("Floyd:---"+e);
		}

		return p;	
	}
	


}
