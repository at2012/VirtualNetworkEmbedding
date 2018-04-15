package vnr.rank;

import java.util.Stack;

import vnr.graph.Graph;

public class Floyd {
	/**
	 * ���ã����������ڵ�֮������·��<p>
	 * ��������ʼ����ֹ�ڵ��±�,�ڵ�����ͼ��<p>
	 * �������ͣ�·��list*/

	/**
	 * @author Jing
	 * @param g �������·����ͼ
	 * @param s ·������ʼ�ڵ�
	 * @param d ·����Ŀ�Ľڵ�
	 * @param path ��ʼ��ֹ�ڵ�֮���·�������ڽ��������������
	 * @return ·������*/
	public static int floyd(Graph g,int s,int d,Stack<Integer> path) {
		int[][] distance=new int[g.getNumOfNode()][g.getNumOfNode()];//���ڴ洢����ͼ��ÿ�Խڵ�֮������·�����룬��ʼʱ���λ���ݵ�ֵ���������˵ı���Ϣ��
		int[][] p=new int[g.getNumOfNode()][g.getNumOfNode()];//floyd�㷨��Ϊ�˼�¼·��
		
		int n=g.getNumOfNode();
		
		//��ʼ������v�ľ��룬ֱ����������ֵ���Լ���0��û�е���weight��ֱ��getweight�ͳɣ�ע������еı��Ϊδ�����ʣ�
		try{
			/*
			 * ��ʼ��distance
			 */
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++) {
//					distance[i][j]=g.getWeight(i, j);
					if(i!=j) {
						if(g.getWeight(i, j)!=g.getMax()) {
							distance[i][j]=1;
						}else {
							distance[i][j]=g.getMax();
						}
						
					}else {
						distance[i][j]=0;					
					}
					
				}
			}
			
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
			
			if(path!=null) {//�ж������Ϊ�˰�floyd�õ�����Ҫ�洢·����ֻ��Ҫ·�����ȵĵط�
				int m;//m��������·�����м�ֵ��
				m=d;
				path.push(d);
				while(m!=s) {
					m=p[s][m];
					path.push(m);
				}
				System.out.println("floyd--·������"+distance[s][d]);
				System.out.println("floyd___·��ӳ������");
			}
			
		}catch(Exception e){
			System.out.println("Floyd:---"+e);
		}
		return distance[s][d];	
	}
	
	
	
	/**
	 * @author Jing
	 * @param g �������·����ͼ
	 * @param s ·������ʼ�ڵ�
	 * @param d ·����Ŀ�Ľڵ�
	 * @return ·������*/
	public static int floyd(Graph g,int s,int d) {
		int[][] distance=new int[g.getNumOfNode()][g.getNumOfNode()];//���ڴ洢����ͼ��ÿ�Խڵ�֮������·�����룬��ʼʱ���λ���ݵ�ֵ���������˵ı���Ϣ��
//		int[][] p=new int[g.getNumOfNode()][g.getNumOfNode()];//floyd�㷨��Ϊ�˼�¼·��
		
		int n=g.getNumOfNode();
		
		//��ʼ������v�ľ��룬ֱ����������ֵ���Լ���0��û�е���weight��ֱ��getweight�ͳɣ�ע������еı��Ϊδ�����ʣ�
		try{
			/*
			 * ��ʼ��distance
			 */
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++) {
//					distance[i][j]=g.getWeight(i, j);
					if(i!=j) {
						if(g.getWeight(i, j)!=g.getMax()) {
							distance[i][j]=1;
						}else {
							distance[i][j]=g.getMax();
						}
						
					}else {
						distance[i][j]=0;					
					}
					
				}
			}
			
			
			for(int k=0;k<n;k++){
				for(int i=0;i<n;i++){
					for(int j=0;j<n;j++){
						if(distance[i][j]>distance[i][k]+distance[k][j]) {
							distance[i][j]=distance[i][k]+distance[k][j];
						}
					}
				}
			}
			
			
		}catch(Exception e){
			System.out.println("Floyd:---"+e);
		}
		return distance[s][d];	
	}
	
	/**
	 * �д������Ƶ�floyd�㷨������·��ӳ����̣�������ص�·������=ͼ�ı�ֵ�����ֵ����Ѱ·ʧ��
	 * @version 2018.2.5
	 * @param g ��Ѱ�����·����ͼ
	 * @param s ��ʼ�ڵ�
	 * @param d Ŀ�Ľڵ�
	 * @param path ���ڴ洢·�����
	 * @param w ��������ֵ
	 * @return sd֮�����·����·����
	 *  */
	public static int floyd(Graph g,int s,int d,Stack<Integer> path,double w) {
		
		
		int[][] distance=new int[g.getNumOfNode()][g.getNumOfNode()];//���ڴ洢����ͼ��ÿ�Խڵ�֮������·�����룬��ʼʱ���λ���ݵ�ֵ���������˵ı���Ϣ��
		int[][] p=new int[g.getNumOfNode()][g.getNumOfNode()];//floyd�㷨��Ϊ�˼�¼·��
		
		int n=g.getNumOfNode();
		
		//��ʼ������v�ľ��룬ֱ����������ֵ���Լ���0��û�е���weight��ֱ��getweight�ͳɣ�ע������еı��Ϊδ�����ʣ�
		try{
			/*
			 * ��ʼ��distance
			 */
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++) {
//					distance[i][j]=g.getWeight(i, j);
					if(i!=j) {
						if(g.getWeight(i, j)!=g.getMax() && g.getWeight(i, j)>=w) {//���������&&֮������ƣ����Ա�֤����鵽���������Ҫ��Ľڵ�
							distance[i][j]=1;
						}else {
							distance[i][j]=g.getMax();
						}
						
					}else {
						distance[i][j]=0;					
					}
					
				}
			}
			
			/*
			 * ��ʼ��path			
			 */
			for(int i=0;i<n;i++){
				for(int j=0;j<n;j++){
					if(g.getWeight(i, j)==g.getMax() || g.getWeight(i, j)<w){
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
			
			//if(path!=null) {//�ж������Ϊ�˰�floyd�õ�����Ҫ�洢·����ֻ��Ҫ·�����ȵĵط�
			int m;//m��������·�����м�ֵ��
			m=d;
			path.push(d);
			while(m!=s && m!=-1) {
				m=p[s][m];
				path.push(m);
			}
//				System.out.println("floyd--·������"+distance[s][d]);
			//}
			
		}catch(Exception e){
			System.out.println("Floyd:---"+e);
		}
		return distance[s][d];	
	}
}
