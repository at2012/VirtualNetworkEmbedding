package vnr.graph;

public class Node {
	
	private int cpu;
	private int degree;
	
	public int getCpu(){
		return cpu;
	}
	public int getDegree(){
		return degree;
	}
	
	public void addDegree(){//�ڲ����֮��ڵ��degree�ͻ�����
		degree++;
	}
	
	public Node(int c){//�ڻ�δ���ƽڵ㽨ģ��ʱ�������ô��
		cpu=c;
	}
	
	public Node(int c,int d){//���ƵĽڵ㽨ģ֮�󣬼ǵ��������ʼ���ڵ�,or ���degree�����Ͳ����ڽڵ㽨����ʱ���ʼ���� ����ֻ���ڲ����֮����Զ����ӣ����Խ����ڵ��ʱ���ǲ����ṩegree�� 
		cpu=c;
		degree=d;
	}
	



}
