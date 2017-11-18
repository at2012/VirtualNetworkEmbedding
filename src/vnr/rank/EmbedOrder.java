package vnr.rank;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import vnr.graph.Graph;

public class EmbedOrder {
	
	/**
	 * ���ã��õ����յ�ӳ��˳�򡣰��սڵ���Ҫ��ֵ�����˳��ʵʱ��ϵ�ǰ��ӳ��ڵ��һ���ھӣ��Խڵ�key--value���д洢
	 * �������ڵ��key-value��Ŀ��Ӧ��list��VNR����ͼ����Ϊ��Ҫ�����±���ʽڵ��ھӣ�
	 * �������ͣ�LinkedHashMap���������͵����ݽṹ�����մ洢˳�����entry*/
	public static LinkedHashMap<Integer,Double> embOrder(List<Map.Entry<Integer,Double>> rank,Graph g){
//		ArrayList<Map.Entry<Integer,Double>> embOrder=new ArrayList<Map.Entry<Integer,Double>>();
		LinkedHashMap<Integer,Double> embOrder =new LinkedHashMap<Integer,Double>();
		boolean[] f=new boolean[rank.size()];//���ڱ�־�ڵ��Ƿ��Ѿ������ӳ�����

		try{
			for(int i=0;i<rank.size();i++){
				if(!f[i]){
					embOrder.put(rank.get(i).getKey(),rank.get(i).getValue());
					System.out.println("put_in_map_self:"+rank.get(i).getKey());
					f[i]=true;
					for(int j=0;j<g.getNumOfNode();j++){
						//��һ���зѾ��ҵ����߼����󣬿�ʼ���Ȩ�ص�ʱ���õ�g.getweight(i,j),����Ҫע�⣬ij���ǽڵ�ı��Ŷ�����������ĳ�����Ͷ���
						if(!f[j] && g.getWeight(rank.get(i).getKey(),rank.get(j).getKey())>0 && g.getWeight(rank.get(i).getKey(), rank.get(j).getKey())<5000){//���Ľ��������5000.��ͼ�ıߵ����Ȩ�أ�Ҫ���£��ĳ��ñ�����ʾ�ġ�
							embOrder.put(rank.get(j).getKey(), rank.get(j).getValue());//���Ľ�������һ�����⣬����Ǹ����ǲ���һ���ھӶ�������У���Щ�ھӻ��и��Ⱥ�ÿ����أ������������ٸĽ�
							System.out.println("put_in_map_nei:"+rank.get(j).getKey());
							f[j]=true;
						}
					}
				}
				
			}
		}catch (Exception e){
			System.out.println("EmbedOrder__embOrder--"+e);
		}

		for(Map.Entry<Integer, Double> entry:embOrder.entrySet()){
			System.out.println("key="+entry.getKey()+",value="+entry.getValue());
		}
		
		return embOrder;
		
	}

}
