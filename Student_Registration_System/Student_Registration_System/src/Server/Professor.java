package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Professor {
	String id;
	String password;
	String name;
	String birthday;
	String SSN;
	String status;
	String department;
	
	private Connection conn;
	private PreparedStatement pst;
	private ResultSet rs;
	
	Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
	public Professor(Socket socket) {
		id = null;
		password=null;
		name=null;
		birthday=null;
		SSN=null;
		status=null;
		department=null;
		try {
			this.dis = new DataInputStream(
			        new BufferedInputStream(socket.getInputStream()));
			this.dos = new DataOutputStream(
	                new BufferedOutputStream(socket.getOutputStream()));//输出流
			conn = Database.getNewConnection();
		} catch (SQLException | IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public String login(String id,String pw) throws SQLException {
		/*
		 * 当服务器接收到教授客户端发来的登录请求后，创建教授对象并执行此方法
		 * 1.访问数据库，对id与pw进行检索，若检索不到或不匹配，报错，返回0
		 * 2.若匹配，则检索数据库该教授的信息，存放到本对象中，并返回1表示登录成功。
		 */
		//补充：对数据库进行检索
		
		String sql;
		sql = "select pid,password from professor where pid=?";
		pst=conn.prepareStatement(sql);
		pst.setString(1, id);
		rs = pst.executeQuery();
		rs.next();
		String testidString=rs.getString("pid");
		String testpwString=rs.getString("password");
		boolean flag=false;
		if(id.equals(testidString)&&pw.equals(testpwString)) {flag=true;}
		else {flag=false;}
		if(flag) {
			return "1";
		}else {
			return "0";
		}
	}
	
	public void close() {
		try {
			rs.close();
			pst.close();
			conn.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
}
