package Login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;


public class SearchProfessor extends JFrame{
	private Vector<String> idd=new Vector<String>();
	private Map<String,String> mmp=new HashMap<String,String>();
	private JButton jb1;
	private JTextField tf;
	private JPanel con;
	private JScrollPane sp1 ;
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	public SearchProfessor(Socket socket) {
		// TODO Auto-generated constructor stub
		super("教授信息查询");
		this.socket=socket;
		try {
			dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

      
		setmmp();
		con=new JPanel();
		setBounds(150, 150, 1150, 500);//
		con.setLayout(null);
		
		 sp1 = new JScrollPane(con);
		this.add(sp1);
		//con=new JPanel();
		//setLayout(null);
		tf=new JTextField();
		tf.setBounds(470, 40, 120, 25);
		jb1=new JButton("确定");
		jb1.setBounds(600, 40, 60, 25);
		JButton jb2=new JButton("取消");
		jb2.setBounds(670, 40, 60, 25);
		con.add(jb1);
		con.add(jb2);
		con.add(tf);
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				MaintainProfessor mp=new MaintainProfessor(socket);
			}
		});
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		setVisible(true);
	}
	public void searchType(String tp) {
		SearchProfessor fs=this;
		JLabel jl=new JLabel("请输入要查询的"+tp);
		jl.setBounds(300, 40, 180, 30);
		con.add(jl);
		JLabel jl3=new JLabel("序号");
		JLabel jl4=new JLabel("pid");
		JLabel jl5=new JLabel("密码");
		JLabel jl6=new JLabel("姓名");
		JLabel jl7=new JLabel("出生日期");
		JLabel jl8=new JLabel("SSN");
		JLabel jl9=new JLabel("职称");
		JLabel jl10=new JLabel("院系");
	
	
		jl3.setBounds(35, 90, 60, 25);
		jl4.setBounds(135, 90, 60, 25);
		jl5.setBounds(235, 90, 60, 25);
		jl6.setBounds(365, 90, 60, 25);	
		jl7.setBounds(465, 90, 60, 25);
		jl8.setBounds(565, 90, 60, 25);
		jl9.setBounds(715, 90, 60, 25);	
		jl10.setBounds(815, 90, 60, 25);
		con.add(jl3);
		con.add(jl4);
		con.add(jl5);
		con.add(jl6);
		con.add(jl7);
		con.add(jl8);
		con.add(jl9);
		con.add(jl10);
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
					String sql="select * from professor where "+mmp.get(tp)+"='"+tf.getText().trim()+"'";// ('"+pid+"','"+name+"','"+birthday+"','"+ssn+"','"+depart+"','"+password+"','"+status+"')";   //SQL语句
					try {
						dos.writeUTF("340"+"#"+sql);
						dos.flush();
						String data=dis.readUTF();
						String da[];
						da=data.split("#");
						int i=0;//i是当前信息的序号，用来实现删除修改的定位
						int j=0;//j是为了得到传回来的数据，因为是用#分开的数据
						
					
				       while(!da[j].equals(" ")){
				    	    final int k=i;
				           // 通过字段检索
				            String pid  = da[j++];
				       		String password=da[j++];
				            String name = da[j++];
				            String birthday= da[j++];			  				    
							String ssn= da[j++];
							String status=da[j++];
							String depart=da[j++];		
									
									JLabel jl11=new JLabel(i+"");
									JLabel l1=new JLabel(pid);
									JLabel l2=new JLabel(password);
									JLabel l3=new JLabel(name);
									JLabel l4=new JLabel(birthday);
									JLabel l5=new JLabel(ssn);
									JLabel l6=new JLabel(status);
									JLabel l7=new JLabel(depart);
									
									
									con.add(jl11);
									//con.add(jl10);
									con.add(l1);
									con.add(l2);
									con.add(l3);
									con.add(l4);
									con.add(l5);
									con.add(l6);
									con.add(l7);
									jl11.setBounds(35, 120+70*i, 60, 25);//控制位置随信息的条数增加变化
									l1.setBounds(135, 120+70*i, 60, 25);
									l2.setBounds(235, 120+70*i, 60, 25);	
									l3.setBounds(365, 120+70*i, 60, 25);
									l4.setBounds(465, 120+70*i, 60, 25);
									l5.setBounds(565, 120+70*i, 60, 25);
									l6.setBounds(715, 120+70*i, 60, 25);
									l7.setBounds(815, 120+70*i, 60, 25);
									JButton jb1=new JButton("删除");//对此条信息删除
									JButton jb2=new JButton("修改");//对此条信息修改
									con.add(jb1);
									jb1.setBounds(945, 120+70*i, 60, 25);
									con.add(jb2);
									jb2.setBounds(1015, 120+70*i, 60, 25);
									idd.add(pid);
									jb1.addActionListener(new ActionListener() {
										//删除按钮添加事件监听
										@Override
										public void actionPerformed(ActionEvent e) {
											// TODO Auto-generated method stub
											int n = JOptionPane.showConfirmDialog(null, "确定要删除吗?", "消息框", JOptionPane.YES_NO_OPTION);
											if (n == JOptionPane.YES_OPTION) {
												Search_Del rg;
												try {
													rg = new Search_Del(socket);
													rg.del(k,idd);
												} catch (IOException e1) {
													// TODO Auto-generated catch block
												//	e1.printStackTrace();
												}
												//删除函数
												dispose();
					
											}
										}
									});
									jb2.addActionListener(new ActionListener() {
										//修改按钮添加事件监听
										@Override
										public void actionPerformed(ActionEvent e) {
											// TODO Auto-generated method stub
											Search_Del rg;
											try {
												rg = new Search_Del(socket);
												rg.modify(k,idd);
											} catch (IOException e1) {
												// TODO Auto-generated catch block
												//e1.printStackTrace();
											}
											//修改函数
											dispose();
											
										}
									});	
									i++;
						          
						       }
				      con.setPreferredSize(new Dimension(1100,150+70*i));	
					} catch (IOException e2) {
						// TODO Auto-generated catch block
					//	e2.printStackTrace();
					}	
					
								
				}
		});
	}
	public void setmmp()
	{
		//选项是文字，把他抓换位表格的属性，执行sql语句的时候用到
		mmp.put("pid", "pid");
		mmp.put("姓名", "name");
		mmp.put("出生日期", "birthday");
		mmp.put("SSN", "SSN");
		mmp.put("职称", "status");
		mmp.put("院系", "department");
		
	}
	
}
