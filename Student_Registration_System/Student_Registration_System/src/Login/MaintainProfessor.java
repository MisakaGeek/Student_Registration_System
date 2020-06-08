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
import java.sql.*;
import javax.swing.*;


import java.util.*;
import javax.swing.*;
public class MaintainProfessor extends JFrame {
	private Vector<String> idd=new Vector<String>();
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	public MaintainProfessor(Socket socket)  {
		super("教授信息维护");
		this.socket=socket;
		try {
			dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

     
		JPanel con=new JPanel();
		setBounds(150, 150, 1150, 500);//
		//setLayout(null);
		con.setLayout(null);
		//con.setPreferredSize(new Dimension(1000,1000));
		JScrollPane sp1 = new JScrollPane(con);
		//sp1.setBounds(400,200,100,100);
		//sp1.setLayout(null);
		//this.getContentPane().add(sp1);
		this.add(sp1);
		
		
		JComboBox jc=new JComboBox();//选择查找标准的下拉框
		jc.setEditable(false);//不可编辑
	    jc.setEnabled(true);
	    String[] arr ={"----查找标准----"};
		ComboBoxModel cbm = new DefaultComboBoxModel(arr);//下拉框的显示不可选
		jc.setModel(cbm);
		jc.addItem("pid");//添加选项
	    jc.addItem("姓名");
	    jc.addItem("出生日期");
	    jc.addItem("SSN");
	    jc.addItem("职称");
	    jc.addItem("院系");
	   
	    con.add(jc);
		jc.setBounds(420, 50, 110, 24);
		JButton jb=new JButton("查找");//查找按钮
		con.add(jb);
		jb.addActionListener(new ActionListener() {
			//给查找按钮添加事件监听
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				if(jc.getSelectedIndex()!=0)
				{
					SearchProfessor sp;
					sp = new SearchProfessor(socket);
					sp.searchType(jc.getSelectedItem().toString());
					
					dispose();
				}
				
				
			}
		});
		jb.setBounds(540, 50, 60, 25);
		JButton b=new JButton("添加");
		con.add(b);
		b.setBounds(610, 50, 60, 25);
		JButton b2=new JButton("返回");
		con.add(b2);
		b2.setBounds(1015, 50, 60, 25);
		JLabel jl1=new JLabel("序号");
		JLabel jl3=new JLabel("pid");
		JLabel jl4=new JLabel("密码");
		JLabel jl5=new JLabel("姓名");
		JLabel jl6=new JLabel("出生日期");
		JLabel jl7=new JLabel("SSN");
		JLabel jl8=new JLabel("职称");
		JLabel jl9=new JLabel("院系");
		jl1.setBounds(35, 90, 60, 25);
		jl3.setBounds(135, 90, 60, 25);
		jl4.setBounds(235, 90, 60, 25);
		jl5.setBounds(365, 90, 60, 25);	
		jl6.setBounds(465, 90, 60, 25);
		jl7.setBounds(565, 90, 60, 25);
		jl8.setBounds(715, 90, 60, 25);	
		jl9.setBounds(815, 90, 60, 25);
		con.add(jl3);
		con.add(jl4);
		con.add(jl5);
		con.add(jl6);
		con.add(jl7);
		con.add(jl8);
		con.add(jl9);
		String sql="select * from professor";
		String data="";
		try {
			dos.writeUTF("340"+"#"+sql);
			dos.flush();
			data=dis.readUTF();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
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
			JLabel jl10=new JLabel(i+"");
			JLabel l1=new JLabel(pid);
			JLabel l2=new JLabel(password);
			JLabel l3=new JLabel(name);
			JLabel l4=new JLabel(birthday);
			JLabel l5=new JLabel(ssn);
			JLabel l6=new JLabel(status);
			JLabel l7=new JLabel(depart);
			con.add(jl1);
			con.add(jl10);
			con.add(l1);
			con.add(l2);
			con.add(l3);
			con.add(l4);
			con.add(l5);
			con.add(l6);
			con.add(l7);
			jl10.setBounds(35, 120+70*i, 60, 25);//控制位置随信息的条数增加变化
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
							rg.del(k,idd);//删除函数
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							//e1.printStackTrace();
						}
						
						dispose();
						//MaintainProfessor a=new MaintainProfessor();
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
					//	e1.printStackTrace();
					}
					//修改函数
					dispose();
					
				}
			});	
			i++;
          
       } 
       con.setPreferredSize(new Dimension(1100,150+70*i));
		   
		
		b.addActionListener(new ActionListener() {	
			//添加按钮添加事件监听
			@Override
			public void actionPerformed(ActionEvent e) {
				AddProfessor add=new AddProfessor(socket);
				dispose();
			}
		});
		b2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				Registrar_GUI rg=new Registrar_GUI(socket);
			}
		});
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}
		
	
}
