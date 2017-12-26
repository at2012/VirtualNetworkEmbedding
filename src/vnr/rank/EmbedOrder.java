package vnr.rank;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import vnr.graph.Graph;

public class EmbedOrder {
	
	/**
	 * ���ã��õ����յ�ӳ��˳�򡣰��սڵ���Ҫ��ֵ�����˳��ʵʱ��ϵ�ǰ��ӳ��ڵ��һ���ھӣ��Խڵ�key--value���д洢
	 * �������ڵ��key-value��Ŀ��Ӧ��list��VNR����ͼ����Ϊ��Ҫ�����±���ʽڵ��ھӣ�
	 * �������ͣ�LinkedHashMap���������͵����ݽṹ�����մ洢˳�����entry*/
	public static LinkedHashMap<Integer,Double> embOrder(List<Map.Entry<Integer,Double>> rank,Graph g){
//	public static List<Map> embOrder(List<Map.Entry<Integer,Double>> rank,Graph g){
		
//		ArrayList<Map.Entry<Integer,Double>> embOrder=new ArrayList<Map.Entry<Integer,Double>>();
		LinkedHashMap<Integer,Double> embOrderTemp =new LinkedHashMap<Integer,Double>();//���Ľ��������������ģ����ַ����о��ܱ������ã�������û�б�İ취ʵ�֡�
		//��linkedhashmap�洢��ʱ�洢�ڵ㣬��Ϊ�������͵�map���ڱ�����ʱ����valueֵ���з��ʡ�
		//�ڷ������ؽ��֮ǰ��ͨ���������ѽ���洢list���棬���ں������i���з��ʡ�
		
		List<Map<Integer,Double>> embOrder = new LinkedList<Map<Integer,Double>>();//���ڴ洢���յķ��ؽ������������Ǳ�����value�ݼ���˳������list�ġ�
		Map<Integer,Double> map=new HashMap<Integer,Double>();//���ڸ�list���key-value
//		Map.Entry<Integer, Double> e=new HashMap.Entry<Integer,Double>;
		
		boolean[] f=new boolean[rank.size()];//���ڱ�־�ڵ��Ƿ��Ѿ������ӳ�����

		try{
			for(int i=0;i<rank.size();i++){
				if(!f[i]){
					embOrderTemp.put(rank.get(i).getKey(),rank.get(i).getValue());
//					System.out.println("put_in_map_self:"+rank.get(i).getKey());
					f[i]=true;
					for(int j=0;j<g.getNumOfNode();j++){
						//��һ���зѾ��ҵ����߼����󣬿�ʼ���Ȩ�ص�ʱ���õ�g.getweight(i,j),����Ҫע�⣬ij���ǽڵ�ı��Ŷ�����������ĳ�����Ͷ���
						if(!f[j] && g.getWeight(rank.get(i).getKey(),rank.get(j).getKey())>0 && g.getWeight(rank.get(i).getKey(), rank.get(j).getKey())<5000){//���Ľ��������5000.��ͼ�ıߵ����Ȩ�أ�Ҫ���£��ĳ��ñ�����ʾ�ġ�
							embOrderTemp.put(rank.get(j).getKey(), rank.get(j).getValue());//���Ľ�������һ�����⣬����Ǹ����ǲ���һ���ھӶ�������У���Щ�ھӻ��и��Ⱥ�ÿ����أ������������ٸĽ�
//							System.out.println("put_in_map_nei:"+rank.get(j).getKey());
							f[j]=true;
						}
					}
				}
				
			}
		}catch (Exception e){
			System.out.println("EmbedOrder__embOrder--"+e);
		}

		
		return embOrderTemp;
		
	}

}
