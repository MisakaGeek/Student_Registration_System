package Login;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.*;


public class AddProfessor extends JFrame{
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	public AddProfessor( Socket socket)// throws IOException
	{
		
		super("添加教授信息");
		this.socket=socket;
		try {
			dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

     
		this.setBounds(500, 100, 500, 500);
		JButton b1=new JButton("确定");
		JButton b2=new JButton("取消");
		JLabel jl1=new JLabel("pid");
		JLabel jl2=new JLabel("登录密码");
		JLabel jl3=new JLabel("姓名");
		JLabel jl4=new JLabel("出生日期");
		JLabel jl5=new JLabel("SSN");
		JLabel jl6=new JLabel("职称");
		JLabel jl7=new JLabel("院系");
		JTextField tf1=new JTextField();
		JTextField tf2=new JTextField();
		JTextField tf3=new JTextField();
		JTextField tf4=new JTextField();
		JTextField tf5=new JTextField();
		JTextField tf6=new JTextField();
		JTextField tf7=new JTextField();
		this.setLayout(null);
		//this.add(jl1);
		//this.add(jc);
		this.add(jl1);
		this.add(tf1);
		this.add(jl2);
		this.add(tf2);
		this.add(jl3);
		this.add(tf3);
		this.add(jl4);
		this.add(tf4);
		this.add(jl5);
		this.add(tf5);
		this.add(jl6);
		this.add(tf6);
		this.add(jl7);
		this.add(tf7);
		this.add(b1);
		this.add(b2);
		//this.add(jl5);
		jl1.setBounds(150, 50, 60, 30);
		jl2.setBounds(150, 90, 60, 30);
		jl3.setBounds(150, 130, 60, 30);
		jl4.setBounds(150, 170, 60, 30);
		jl5.setBounds(150, 210, 60, 30);
		jl6.setBounds(150, 250, 60, 30);
		jl7.setBounds(150, 290, 60, 30);
		tf1.setBounds(230, 50, 120, 25);
		tf2.setBounds(230, 90, 120, 25);
		tf3.setBounds(230, 130, 120, 25);
		tf4.setBounds(230, 170, 120, 25);
		tf5.setBounds(230, 210, 120, 25);
		tf6.setBounds(230, 250, 120, 25);
		tf7.setBounds(230, 290, 120, 25);
		b1.setBounds(180, 350, 60, 25);
		b2.setBounds(250, 350, 60, 25);
		
	    b1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				int n = JOptionPane.showConfirmDialog(null, "确定要添加吗?", "消息框", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					String pid=tf1.getText().trim();
					String password= tf2.getText().trim();
					String name=tf3.getText().trim();
					String birthday= tf4.getText().trim();				  				    
					String ssn= tf5.getText().trim();
					String status= tf6.getText().trim();
					String depart= tf7.getText().trim();
					
					try {
						String sql="insert into professor values('"+pid+"','"+password+"','"+name+"','"+birthday+"','"+ssn+"','"+status+"','"+depart+"')";
						 dos.writeUTF("33"+"#"+sql);
				         dos.flush();
				         int success=dis.readInt();
				         System.out.println(success);
				         if(success==0)
				         {
				        	 JOptionPane.showMessageDialog(null, "操作失败");
				        	 
				         }
				         else
				         {				        	 
				        	 JOptionPane.showMessageDialog(null, "添加成功");
				        	// 
				         }
				         MaintainProfessor a=new MaintainProfessor(socket);
				         dispose();
				         
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
					}/*finally
					{
						try {
							
							MaintainProfessor a=new MaintainProfessor(socket);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}*/
				   
				 //   RootFrame rf=new RootFrame(shop);
				    
				}
			}
		});
		b2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				MaintainProfessor a=new MaintainProfessor(socket);
			}
		});
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
}
