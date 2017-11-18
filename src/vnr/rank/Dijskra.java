package vnr.rank;

import vnr.graph.Graph;

public class Dijskra {
	
	
	 
		public static int dijskra(Graph g,int v,int w) throws Exception{//启发：对于一个方法中需要得到多个变量的值得时候，可以不写返回类型，而是把要被赋值的变量作为参数传递给方法，然后在方法内对这个变量进行赋值
			//，这个是不是只有引用类型才行啊，如果是数值类型的话，在方法内 是没对传进来的变量进行赋值的，传引用也得查查
			
			//这里的path是为了存储路径，为的是以后对path进行输出，其实有时候可能没有太大用，
			final int maxWeight=5000;
			int n=g.getNumOfNode();
			int[] distance=new int[g.getNumOfNode()];
			boolean[] s=new boolean[n];
			
			int temp=0;
			//初始化，到v的距离，直接相连的是值，自己是0，没有的事weight，直接getweight就成，注意把所有的标记为未被访问，
			for(int i=0;i<n;i++){
				distance[i]=g.getWeight(v, i);
				s[i]=false;
			}
			s[v]=true;
			


			for(int i=1;i<n;i++){
				int min=maxWeight;//启发：这个min被赋值的位置，妈的，浪费了好几个小时，之前也因为这种值得赋值位置被影响过！！！！切记切记
				for(int j=0;j<n;j++){
					if(distance[j]<min && !s[j]){
						temp=j;
						min=distance[j];
						
					}

				}
				
				s[temp]=true;
				
				for(int j=0;j<n;j++){
//					if(!s[j] && distance[j]>distance[temp]+g.getWeight(temp, j) && g.getWeight(temp, j)<maxWeight)//在把一个点入了已经访问过之后，更新所有的距离
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
