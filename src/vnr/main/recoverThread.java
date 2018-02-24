package vnr.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import vnr.graph.Graph;

public class recoverThread implements Runnable {
	String folder;
	Graph gPhy;
	/**
	 * @param resultFolder �洢ӳ�������ļ���
	 * @param g ������������*/
	public recoverThread(String resultFolder,Graph g) {
		// TODO Auto-generated constructor stub
		folder=resultFolder;
		gPhy=g;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated 0method stub

		/*��Դ�ָ�*/
		File resFolder = new File(folder);
		File[] res=resFolder.listFiles();
		
		
		String line;
		String regex=" ";
		String[] lineContent;
		List<String> lines=new LinkedList<String>();
		
		try {
			for(int i=0;i<res.length;i++) {
				BufferedReader resReader = new BufferedReader(new InputStreamReader(new FileInputStream(res[i])));
				int nn=-1;
				int ln=-1;//nn��ln�ֱ����ڱ�ʾ����ڵ���������·����,�Ľ�,ln��һ��û�б��õ��ı���
				
//				BufferedWriter test = new BufferedWriter(new FileWriter("test"+i+".txt"));
				
				lines.clear();
				while((line=resReader.readLine())!=null) {
					lines.add(line);
				}

				for(int j=0;j<lines.size();j++) {
					lineContent=lines.get(j).split(regex);
					
					if(j==0) {
						//��¼�ڵ������ͱ�����
						nn=Integer.parseInt(lineContent[0]);
						ln=Integer.parseInt(lineContent[1]);
					}else if(j>0 && j<=nn) {
						//�ָ��ڵ���Դ
						int id=Integer.parseInt(lineContent[1]);//����ڵ�id
						double c=Double.parseDouble(lineContent[2]);//�ȴ��ָ�����Դֵ
						gPhy.setCpu(id,gPhy.getCpu(id)+c);
					}else {
						//�ָ���·��Դ
						double w;//������Դ
						int l,n;//�ֱ��ʾһ����·����ʼ����ֹ
						w=Double.parseDouble(lineContent[2]);
					
						for(int x=3;x<lineContent.length-1;x++) {
							l=Integer.parseInt(lineContent[x]);
							n=Integer.parseInt(lineContent[x+1]);
							gPhy.setWeight(l, n, gPhy.getWeight(l, n)+w);
						}
					}
				}
				
//				/*test*/
//				System.out.println("�ָ��������������ˣ�");
//				for(int x=0;x<gPhy.getNumOfNode();x++) {
//					for(int y=0;y<gPhy.getNumOfNode();y++) {
//						System.out.print(gPhy.getWeight(x, y)+"\t");
//					}
//					System.out.println();
//				}
//				
//				for(int x=0;x<gPhy.getNumOfNode();x++) {
//					System.out.println(gPhy.getCpu(x));
//				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("FROM recoverThread-"+e);
		}
	}

}
