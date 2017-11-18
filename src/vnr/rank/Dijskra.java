package vnr.rank;

import vnr.graph.Graph;

public class Dijskra {
	
	
	 
		public static int dijskra(Graph g,int v,int w) throws Exception{//����������һ����������Ҫ�õ����������ֵ��ʱ�򣬿��Բ�д�������ͣ����ǰ�Ҫ����ֵ�ı�����Ϊ�������ݸ�������Ȼ���ڷ����ڶ�����������и�ֵ
			//������ǲ���ֻ���������Ͳ��а����������ֵ���͵Ļ����ڷ����� ��û�Դ������ı������и�ֵ�ģ�������Ҳ�ò��
			
			//�����path��Ϊ�˴洢·����Ϊ�����Ժ��path�����������ʵ��ʱ�����û��̫���ã�
			final int maxWeight=5000;
			int n=g.getNumOfNode();
			int[] distance=new int[g.getNumOfNode()];
			boolean[] s=new boolean[n];
			
			int temp=0;
			//��ʼ������v�ľ��룬ֱ����������ֵ���Լ���0��û�е���weight��ֱ��getweight�ͳɣ�ע������еı��Ϊδ�����ʣ�
			for(int i=0;i<n;i++){
				distance[i]=g.getWeight(v, i);
				s[i]=false;
			}
			s[v]=true;
			


			for(int i=1;i<n;i++){
				int min=maxWeight;//���������min����ֵ��λ�ã���ģ��˷��˺ü���Сʱ��֮ǰҲ��Ϊ����ֵ�ø�ֵλ�ñ�Ӱ������������м��м�
				for(int j=0;j<n;j++){
					if(distance[j]<min && !s[j]){
						temp=j;
						min=distance[j];
						
					}

				}
				
				s[temp]=true;
				
				for(int j=0;j<n;j++){
//					if(!s[j] && distance[j]>distance[temp]+g.getWeight(temp, j) && g.getWeight(temp, j)<maxWeight)//�ڰ�һ���������Ѿ����ʹ�֮�󣬸������еľ���
//						System.out.println("test:"+s[j]);
					if(!s[j] && g.getWeight(temp, j)<maxWeight){
						if(distance[j]>distance[temp]+g.getWeight(temp, j))
							distance[j]=distance[temp]+g.getWeight(temp, j);
					}
				}

				
			}
			
			return distance[w];	
		}


}
