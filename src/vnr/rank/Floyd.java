package vnr.rank;

import java.util.List;
import java.util.Stack;

import vnr.graph.Graph;

public class Floyd {
	/**
	 * ���ã����������ڵ�֮������·��<p>
	 * ��������ʼ����ֹ�ڵ��±�,�ڵ�����ͼ��<p>
	 * �������ͣ�·��list*/
//	public static List<Integer> floyd(Graph g,int s,int d,int[][] distance){
	/**
	 * @author Jing
	 * @param g �������·����ͼ
	 * @param s ·������ʼ�ڵ�
	 * @param d ·����Ŀ�Ľڵ�
	 * @param path ��ʼ��ֹ�ڵ�֮���·�������ڽ��������������
	 * @return ·������*/
//	public static int floyd(Graph g,int s,int d,List<Integer> path){
	public static int floyd(Graph g,int s,int d,Stack<Integer> path) {
		
//		for(int i)
//		List<Integer> p=new LinkedList<Integer>();
		
		
		
		int[][] distance=new int[g.getNumOfNode()][g.getNumOfNode()];//���ڴ洢����ͼ��ÿ�Խڵ�֮������·�����룬��ʼʱ���λ���ݵ�ֵ���������˵ı���Ϣ��
//		Stack<Integer> stack=new Stack<Integer>	();//���ڽ�·����������path ��list
		int[][] p=new int[g.getNumOfNode()][g.getNumOfNode()];//floyd�㷨��Ϊ�˼�¼·��
		
		int n=g.getNumOfNode();
//		int[][] distance=new int[g.getNumOfNode()][g.getNumOfNode()];

//		int temp=0;
		//��ʼ������v�ľ��룬ֱ����������ֵ���Լ���0��û�е���weight��ֱ��getweight�ͳɣ�ע������еı��Ϊδ�����ʣ�
		
		try{
			/*
			 * ��ʼ��distance
			 */
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++) {
					distance[i][j]=g.getWeight(i, j);
				}
			}
			
//			System.out.println("dis����֮��");
//			for(int i=0;i<n;i++) {
//				for(int j=0;j<n;j++) {
//					System.out.print(distance[i][j]+"\t");
//				}
//				System.out.println();
//			}
			
			/*
			 * ��ʼ��path			
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
//			System.out.println("path��ʼ��֮��");
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
			
			
			int m;//m��������·�����м�ֵ��
			m=d;
			path.push(d);
			while(m!=s) {
				m=p[s][m];
				path.push(m);
			}
//			System.out.println("stack���ݣ�"+d+"��ջ");
//			while(p[s][d]!=s){
//				stack.push(p[s][d]);
////				System.out.println("stack���ݣ�"+path[s][d]+"��ջ");
//				d=p[s][d];
//			}
//			stack.push(s);
//			System.out.println("stack���ݣ�"+path[s][d]+"��ջ");
			System.out.println("stack��С��"+path.size());
			System.out.println("floyd����ļ����·������"+distance[s][d]);
			
//			for(int i=0;i<stack.size();i++){//�������������ë��������������stack.size()��ֵ�ǻ������ã���������û��������ѭ�������õ���ջ�ĵ�ʱ�򣬣�size��ֵ�ǻ��ã�Ҫ�������sizeֵ�����洢��Ϊѭ���Ľ��ޡ�ͻȻ�뵽��Ϊʲô����while������
////				ֱ��Ϊ�գ��ǵģ�whileѭ���ȽϺã�����
//				
////				System.out.println("ջ��Ԫ��"+stack.peek());
////				System.out.println("ȡ��ջ��"+stack.pop());
//				System.out.println("����ִ�м��鰡������"+stack.size());
//				p.add(stack.pop());
//			}
			
			/*��·������list��*/
//			while(!stack.empty()){//���Ľ��������ȴ�վ�ڳ�ս��list����Ϊ���ã��������岻��ȷ�����Һܿ��ܸ���û���壬���������Ū�������Ժ���¡�
//				path.add(stack.pop());
//			}
			System.out.println("floyd___·��ӳ������");
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
