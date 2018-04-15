package vnr.graph;

public class Node {
	
	private double cpu;
	private int degree;
	private double bandwidth;
	
	public Node(double c){//�ڻ�δ���ƽڵ㽨ģ��ʱ�������ô��
		cpu=c;
	}
	
	public Node(double c,int d){//���ƵĽڵ㽨ģ֮�󣬼ǵ��������ʼ���ڵ�,or ���degree�����Ͳ����ڽڵ㽨����ʱ���ʼ���� ����ֻ���ڲ����֮����Զ����ӣ����Խ����ڵ��ʱ���ǲ����ṩegree�� 
		cpu=c;
		degree=d;
	}
	
	public double getCpu(){
		return cpu;
	}
	public int getDegree(){
		return degree;
	}
	public double getBandwidth() {
		return bandwidth;
	}
	
	public double getResourceBlock() {
		return bandwidth*cpu;
	}
	
	public void setCpu(double x) {
		cpu=x;
	}
	
	
	
	public void addDegree(){//�ڲ����֮��ڵ��degree�ͻ�����
		degree++;
	}
	/**
	 * @param bw �¼���ڵ��뵱ǰ�ڵ�֮�����·Ȩ��*/
	public void addBandwidth(double bw) {
		bandwidth+=bw;
	}
	
}
