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
		//��ʼ������v�ľ��룬ֱ����������ֵ���Լ���0��û�е���weight��ֱ��getweight�ͳɣ�ע������еı��Ϊδ�����ʣ�
		
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
//			System.out.println("stack���ݣ�"+d+"��ջ");
			while(path[s][d]!=s){
				stack.push(path[s][d]);
//				System.out.println("stack���ݣ�"+path[s][d]+"��ջ");
				d=path[s][d];
			}
			stack.push(s);
//			System.out.println("stack���ݣ�"+path[s][d]+"��ջ");
			System.out.println("stack��С��"+stack.size());
			
//			System.out.println(stack.empty());
			

//			for(int i=0;i<stack.size();i++){//�������������ë��������������stack.size()��ֵ�ǻ������ã���������û��������ѭ�������õ���ջ�ĵ�ʱ�򣬣�size��ֵ�ǻ��ã�Ҫ�������sizeֵ�����洢��Ϊѭ���Ľ��ޡ�ͻȻ�뵽��Ϊʲô����while������
////				ֱ��Ϊ�գ��ǵģ�whileѭ���ȽϺã�����
//				
////				System.out.println("ջ��Ԫ��"+stack.peek());
////				System.out.println("ȡ��ջ��"+stack.pop());
//				System.out.println("����ִ�м��鰡������"+stack.size());
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
