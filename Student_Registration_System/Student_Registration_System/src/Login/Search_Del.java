package Login;
//删除
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;



public class Search_Del {
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	public Search_Del(Socket socket) 
	{
		this.socket=socket;
		try {
			dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

      
		
	}
	public void del(int index, Vector<String> idv) throws IOException
    {
		String sql="delete from professor where pid='"+idv.get(index)+"'";
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
        	 JOptionPane.showMessageDialog(null, "删除成功");		
         }
    	
    	MaintainProfessor a=new MaintainProfessor(socket);
    }
    public void modify(int index,Vector<String> idv) throws IOException
    {
    	
	    String sql="select * from professor where pid='"+idv.get(index)+"'";
		dos.writeUTF("340"+"#"+sql);
		dos.flush();
		String data=dis.readUTF();
		String da[];
		da=data.split("#");
		ModifyProfessor mp=new ModifyProfessor(da,socket);

		
    }
    public void delS(int index, Vector<String> idv) throws IOException
    {
    	String sql="delete from student where sid='"+idv.get(index)+"'";
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
        	JOptionPane.showMessageDialog(null, "删除成功");	
        }
    	
    	MaintainStudent a=new MaintainStudent(socket);
    }
    public void modifyS(int index,Vector<String> idv) throws IOException
    {
    	
	    String sql="select * from student where sid='"+idv.get(index)+"'";
	    dos.writeUTF("341"+"#"+sql);
		dos.flush();
		String data=dis.readUTF();
		String da[];
		da=data.split("#");
		ModifyStudent mp=new ModifyStudent(da,socket);
		
    }
}
