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
	 * @param resultFolder 存储映射结果的文件夹
	 * @param g 物理网络拓扑*/
	public recoverThread(String resultFolder,Graph g) {
		// TODO Auto-generated constructor stub
		folder=resultFolder;
		gPhy=g;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated 0method stub

		/*资源恢复*/
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
				int ln=-1;//nn和ln分别用于表示网络节点数量和链路数量,改进,ln是一个没有被用到的变量
				
//				BufferedWriter test = new BufferedWriter(new FileWriter("test"+i+".txt"));
				
				lines.clear();
				while((line=resReader.readLine())!=null) {
					lines.add(line);
				}

				for(int j=0;j<lines.size();j++) {
					lineContent=lines.get(j).split(regex);
					
					if(j==0) {
						//记录节点数量和边数量
						nn=Integer.parseInt(lineContent[0]);
						ln=Integer.parseInt(lineContent[1]);
					}else if(j>0 && j<=nn) {
						//恢复节点资源
						int id=Integer.parseInt(lineContent[1]);//物理节点id
						double c=Double.parseDouble(lineContent[2]);//等待恢复的资源值
						gPhy.setCpu(id,gPhy.getCpu(id)+c);
					}else {
						//恢复链路资源
						double w;//带宽资源
						int l,n;//分别表示一段链路的起始和终止
						w=Double.parseDouble(lineContent[2]);
					
						for(int x=3;x<lineContent.length-1;x++) {
							l=Integer.parseInt(lineContent[x]);
							n=Integer.parseInt(lineContent[x+1]);
							gPhy.setWeight(l, n, gPhy.getWeight(l, n)+w);
						}
					}
				}
				
//				/*test*/
//				System.out.println("恢复后物理网络拓扑：");
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
