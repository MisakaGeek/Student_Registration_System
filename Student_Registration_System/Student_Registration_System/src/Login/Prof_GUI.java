/*
 * @author 雷浩洁
 * @version 1.0
 * 教授客户端代码
 */
package Login;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

//import Login.ShowCourseeqGUI.windowlistener;

public class Prof_GUI extends JFrame implements ActionListener{
	public int sys_width;
	public int sys_height;
	public int windowsWidth;
	public int windowsHeight;
	
	public String user_id;//教授用户名，即教工号
	public String password;//教授密码
	
	private JButton jb1;//负责启动教授的两个用例：选择执教课程与提交成绩
	private JButton jb2;
	private JButton jb3;//返回上一层
	private JPanel jp;
	private JLabel label1,label2;
	private String pname;
	
	//负责与服务器通信的socket，已在构造函数中初始化
	public Socket socket;
	public DataInputStream dis;
	public DataOutputStream dos;
	public ShowCourseeqGUI showeq;
	public ShowCourseGUI show;
	public NotRegisterTimeShowCourseGUI nrtshow;
	public Prof_GUI(String name, String pw,Socket socket) {
		this.user_id = name;
		this.password = pw;
		this.socket=socket;
		try {
			this.dis = new DataInputStream(
			        new BufferedInputStream(socket.getInputStream()));
			this.dos = new DataOutputStream(
	                new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		this.setTitle("教授选课系统");
		
		 jb3=new JButton("返回");//返回上一层
		 jb3.setBounds(0,0,80,30);
	     this.add(jb3);
		//设置窗口大小
		sys_width = Toolkit.getDefaultToolkit().getScreenSize().width;
	    sys_height = Toolkit.getDefaultToolkit().getScreenSize().height;
	    windowsWidth = 350;
	    windowsHeight = 350;
	    this.setSize(windowsWidth,windowsHeight);
	    this.setBounds((sys_width- windowsWidth) / 2,
                (sys_height - windowsHeight) / 2, windowsWidth, windowsHeight);
		
	    //布局
	    setLayout(null);
	    
	    //欢迎语
	    
	    try {
			DataInputStream dis = new DataInputStream(
			        new BufferedInputStream(socket.getInputStream()));
			DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(socket.getOutputStream()));
			System.out.println("向服务器发送2y代码");
			dos.writeUTF("2y");
			dos.flush();
			dos.writeUTF(user_id);
			dos.flush();
			pname=dis.readUTF();
			System.out.println("profgui界面标签判断出教授名字："+pname);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        label1=new JLabel("您好，"+pname+"老师！");//显示当前学期
        label1.setFont(new Font("楷体", Font.PLAIN, 18));
        label1.setBounds(100,80,300,20);
        this.add(label1);
        
        label2=new JLabel("欢迎使用教授选课系统！");//显示当前学期
        label2.setFont(new Font("楷体", Font.PLAIN, 18));
        label2.setBounds(70,120,300,20);
        this.add(label2);
        
	    //两个功能
	    jb1 = new JButton("选择执教课程");
	    jb1.setBounds(50,200,130,40);
	    this.add(jb1);
	    jb2 = new JButton("提交成绩");
	    jb2.setBounds(200,200,100,40);
	    this.add(jb2);
	    jb1.addActionListener(this);
	    jb2.addActionListener(this);
	    jb3.addActionListener(this);
	    setResizable(false);//禁止缩放
	    this.setVisible(true);
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		windowlistener wl=new windowlistener();//退出窗口时回到上一个窗口
	    this.addWindowListener(wl);//委托监听器
			
	}
	class windowlistener implements WindowListener//窗口监听器
	{
		
	 public void windowActivated(WindowEvent e) {}
	 public void windowClosed(WindowEvent e) { 
	 }
	 public void windowClosing(WindowEvent e) //退出窗口时也让注册人数减一，顺便回到上一个窗口
	 {
		 int result=JOptionPane.showConfirmDialog(null, "确定退出？", "退出", JOptionPane.YES_NO_OPTION);
		 if(result==JOptionPane.YES_OPTION){//确定退出
			 dispose();
			Main_Login_GUI main= new Main_Login_GUI();
		 }
	 }
	 public void windowDeactivated(WindowEvent e) {
	 }
	 public void windowDeiconified(WindowEvent e) {}
	 public void windowIconified(WindowEvent e) {}
	 public void windowOpened(WindowEvent e) {}
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb1) {  //选择执教课程功能
			//补充：执行选择执教课程用例
			try {
				
				DataInputStream dis = new DataInputStream(
	                new BufferedInputStream(socket.getInputStream()));
				
				DataOutputStream dos = new DataOutputStream(
	                new BufferedOutputStream(socket.getOutputStream()));
				System.out.println("向服务器发送21代码");
				dos.writeUTF("21");//向服务器发送21代码，让服务器查询是否正在注册
				dos.flush();//立即写出缓存
				String s=dis.readUTF();//读取服务器发回的消息
				
				//不管是否在注册时间，下面这些都要执行
					System.out.println("向服务器发送22代码");
					dos.writeUTF("22");//向服务器发送22代码，让服务器调用findQualifiedCourses(String user_id)函数查找有资格教授的课程
					dos.flush();
	
					int flag=dis.readInt();//有几行数据传过来，包括属性名
					System.out.println("有资格教授的课程有："+flag+"行");
					
					String qualifiedtable[]=new String[flag*4];//保存传过来的所有行的数据，包括属性名
					//qualifiedtable[0]=dis.readUTF();//属性名
					
					for(int j=0;j<flag*4;j++)//循环结束，qualifiedtable里面就是要返回给教授的有资格教授的全部课程
					{
						qualifiedtable[j]=dis.readUTF();
					}
					for(int j=0;j<flag*4;j++)
						System.out.println(qualifiedtable[j]);
					
					System.out.println("向服务器发送23代码");
					dos.writeUTF("23");//向服务器发送23代码，让服务器调用preTaughtCourses(pid)函数查找以前教授的课程
					dos.flush();
					dos.writeUTF(user_id);//然后向服务器发送教授id，用作上述函数的参数
					dos.flush();
					
					int i=dis.readInt();//有几行数据传过来，包括属性名
					System.out.println("有"+i+"行");
					
					String pretaughttable[]=new String[i*4];//保存传过来的所有行的数据，包括属性名
					
					
					for(int j=0;j<i*4;j++)//循环结束，pretaughttable里面就是要返回给教授的有资格教授的全部课程
					{
						pretaughttable[j]=dis.readUTF();
					}
					for(int j=0;j<i*4;j++)
						System.out.println(pretaughttable[j]);
					
					System.out.println("向服务器发送2b代码");
					dos.writeUTF("2b");//向服务器发送2b代码，让服务器调用professor的selectedCourse（String pid）函数查找已经选择过的课程
					dos.flush();
					dos.writeUTF(user_id);//向服务器发送pid用作上述函数的参数
					dos.flush();
					i=dis.readInt();//有几行数据传过来，包括属性名
					System.out.println("选择过的课程有"+i+"行");
					
					String [] selectedtable=new String[i*4];//保存传过来的所有行的数据，包括属性名
					
					
					for(int j=0;j<i*4;j++)//循环结束，selectedtable里面就是要返回给教授的已经选择的全部课程
					{
						selectedtable[j]=dis.readUTF();
					}
					for(int j=0;j<i*4;j++)
						System.out.println(selectedtable[j]);
					
					if(s.equals(new String("当前不是注册时间！")))
					{
						JOptionPane.showMessageDialog(null,"当前不是注册时间，您仅可以查看已教授的课程和已选择的课程","不在注册时间",JOptionPane.INFORMATION_MESSAGE);
						//socket.close();//服务终止
						nrtshow=new NotRegisterTimeShowCourseGUI(user_id,password,socket,pretaughttable,selectedtable);//创建显示信息窗口，参数为要显示的信息，已经教授过的课程和已经选择的课程
						
					}
					else//是注册时间
					{
						dispose();
						System.out.println("向服务器发送2x代码");
						dos.writeUTF("2x");//向服务器发送2x代码，让服务器将注册人数加一，为了测试此处返回一下注册人数
						dos.flush();
						System.out.println("当前注册人数："+dis.readInt());
						
						if(flag==1)//有资格教授的课程表格只有一行，则不显示有资格教授的课程
						{
							JOptionPane.showMessageDialog(null, "您当前没有有资格教授的课程，仅可以查看选过的课程，若您有已经选择的课程，则可以执行退课操作", "显示信息",JOptionPane.INFORMATION_MESSAGE);
							showeq=new ShowCourseeqGUI(user_id,password,socket,pretaughttable,selectedtable);//创建显示信息窗口，参数为要显示的信息，已经教授过的课程和已经选择的课程
						
						}
						else
							show=new ShowCourseGUI(user_id,password,socket,qualifiedtable,pretaughttable,selectedtable);//创建显示信息窗口，参数为要显示的信息，有资格教授的课程和已经教授过的课程和已经选择的课程
					}
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(e.getSource()==jb2) {  //提交成绩
			//补充：执行提交成绩用例
		}
		else if(e.getSource()==jb3) {  //返回上一层
			dispose();
			Main_Login_GUI main= new Main_Login_GUI();
		}
	}
}

