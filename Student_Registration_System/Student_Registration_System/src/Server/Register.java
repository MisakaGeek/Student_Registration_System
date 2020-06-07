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

public class Register {
	private Connection conn;
	private PreparedStatement pst;
	private ResultSet rs;
	
	Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
	
    public Register(Socket socket) {
    	this.socket=socket;
    	try {
			this.dis = new DataInputStream(
			        new BufferedInputStream(socket.getInputStream()));
			this.dos = new DataOutputStream(
	                new BufferedOutputStream(socket.getOutputStream()));//输出流
			this.conn = Database.getNewConnection();
		} catch (IOException | SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
    }
    
	public String login(String id,String pw) {
		try {
			conn = Database.getNewConnection();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return "1";
	}
	public void close_Registration() {
		
	}
	public void Maintain_Student_Information() {
		
	}
	public void Maintain_Teacher_Information() {
		
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
