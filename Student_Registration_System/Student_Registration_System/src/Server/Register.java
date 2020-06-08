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
import java.sql.Statement;



public class Register {

	private Connection conn=null;
	private PreparedStatement pst=null;
	private ResultSet rs=null;
	private Statement stmt=null;

	
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
			pst=null;
			rs=null;
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return "1";
	}
	public String open_Registration() {
		SRSServer.isRegistration_time=1;
		return "true";
	}
	public void close_Registration() {
		
	}
	public void Maintain_Student_Information() {
		
	}
	public void Maintain_Teacher_Information() {
		
	}
	public void close() {
		try {
  if(rs!=null)
				rs.close();
			if(pst!=null)
				pst.close();
			if(stmt!=null)
				stmt.close();
			if(conn!=null)
				conn.close();

		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
    public void update(String sql) 
    {
    	
    	int line=0;
		
			try {
				stmt=conn.createStatement();
				stmt.executeUpdate(sql);//数据库操作影响的行数，用来判断执行是否成功			    
			    line=1;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}finally
			{
				try {
					dos.writeInt(line);
					dos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
				
    }
    public void searchPro(String sql) 
    {
    	
    	
			//Statement stmt;
			try {
				stmt = conn.createStatement();
				rs=stmt.executeQuery(sql);	    	
		    	String res="";
		    	while(rs.next()){
		            // 通过字段检索
		            String pid  = rs.getString("pid");
		        	String password= rs.getString("password");
		            String name = rs.getString("name");
		            String birthday= rs.getString("birthday");			  				    
		 			String ssn= rs.getString("ssn");
		 			String status= rs.getString("status");
		 			String depart= rs.getString("department");
		 			res=res+pid+"#"+password+"#"+name+"#"+birthday+"#"+ssn+"#"+status+"#"+depart+"#";
		    	}
		    	res=res+" ";
		    	dos.writeUTF(res);
		    	dos.flush();
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
	    	
    }
    public void searchStu(String sql) 
    {
    	
    	try {
			
		stmt=conn.createStatement();
	    	rs=stmt.executeQuery(sql);
	    	String res="";
	    	while(rs.next()){
	            // 通过字段检索	     	
	            String sid  = rs.getString("sid");
	        	String password= rs.getString("password");
	            String name = rs.getString("name");
	            String birthday= rs.getString("birthday");			  				    
	 			String ssn= rs.getString("ssn");
	 			String status= rs.getString("status");
	 			res=res+sid+"#"+password+"#"+name+"#"+birthday+"#"+ssn+"#"+status+"#";
	    	}
	    	res=res+" ";
	    	dos.writeUTF(res);
	    	dos.flush();
		} catch (SQLException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
