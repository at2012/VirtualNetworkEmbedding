package vnr.rank;

import java.util.List;
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
					distance[i][j]=g.getWeight(i, j);
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
			
			if(path!=null) {
				int m;//m��������·�����м�ֵ��
				m=d;
				path.push(d);
				while(m!=s) {
					m=p[s][m];
					path.push(m);
				}
				System.out.println("floyd����ļ����·������"+distance[s][d]);
				System.out.println("floyd___·��ӳ������");
			}
			
		}catch(Exception e){
			System.out.println("Floyd:---"+e);
		}
		return distance[s][d];	
	}
}
