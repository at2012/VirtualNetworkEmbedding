package vnr.graph;

public class Node {
	
	private double cpu;
	private int degree;
	
	public double getCpu(){
		return cpu;
	}
	public int getDegree(){
		return degree;
	}
	
	public void setCpu(double x) {
		cpu=x;
	}
	
	
	public void addDegree(){//�ڲ����֮��ڵ��degree�ͻ�����
		degree++;
	}
	
	public Node(double c){//�ڻ�δ���ƽڵ㽨ģ��ʱ�������ô��
		cpu=c;
	}
	
	public Node(double c,int d){//���ƵĽڵ㽨ģ֮�󣬼ǵ��������ʼ���ڵ�,or ���degree�����Ͳ����ڽڵ㽨����ʱ���ʼ���� ����ֻ���ڲ����֮����Զ����ӣ����Խ����ڵ��ʱ���ǲ����ṩegree�� 
		cpu=c;
		degree=d;
	}
	



}
