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
	
	
	public String close_Registration() {
		ResultSet rss;
		Statement stmt=null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		if (SRSServer.isRegistration!=0) {
			//若当前存在用户正在注册，则退出用例
			return "notEmpty";
		}
		SRSServer.isRegistration_time=0;//关闭注册通道
		try {
			
			String sqlString="select cid from course where pid is null";
			rss = stmt.executeQuery(sqlString);
			while(rss.next()) {
				//删除没有任课教授的课程
				del_course(rss.getString("cid"));
			}
			sqlString="select cid,number from course where number < 3";
			rss = stmt.executeQuery(sqlString);
			while(rss.next()) {
				if(rss.getString("number").contentEquals("0")) continue;
				//删除学生数小于三人的课程
				del_course(rss.getString("cid"));
			}
			
			//找出存在问题的学生课表,并将备选课程加入到主选课程中
			sqlString="select sid from course_selection where lesson1 is null";
			rss = stmt.executeQuery(sqlString);
			while(rss.next()) {
				merge_course(rss.getString("sid"),"lesson1");
			}
			sqlString="select sid from course_selection where lesson2 is null";
			rss = stmt.executeQuery(sqlString);
			while(rss.next()) {
				merge_course(rss.getString("sid"),"lesson2");
			}
			sqlString="select sid from course_selection where lesson3 is null";
			rss = stmt.executeQuery(sqlString);
			while(rss.next()) {
				merge_course(rss.getString("sid"),"lesson3");
			}
			sqlString="select sid from course_selection where lesson4 is null";
			rss = stmt.executeQuery(sqlString);
			while(rss.next()) {
				merge_course(rss.getString("sid"),"lesson4");
			}
			
			//再检查一次是否存在学生人数小于三人的课表
			sqlString="select cid,number from course where number < 3";
			rss = stmt.executeQuery(sqlString);
			while(rss.next()) {
				if(rss.getString("number").contentEquals("0")) continue;
				//删除学生数小于三人的课程
				del_course(rss.getString("cid"));
			}
			
			//计算学费
			sqlString="select sid from course_selection";
			stmt=conn.createStatement();
			rss = stmt.executeQuery(sqlString);
			while(rss.next()) {
				calculate_fee(rss.getString("sid"));
			}
			
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return "true";
		
	}
	public void del_course(String cid) {
		/*
		 * 针对不满足条件的课程，将该课程从学生课表中删除
		 * para: cid:需要被删除的课程的编号
		 */
		SRSServer.canceled_course.add(cid);
		try {
			String sqlString = "select sid,lesson1,lesson2,lesson3,lesson4 from course_selection";
			String sql2;
			ResultSet rs2 = null;
			pst=conn.prepareStatement(sqlString);
			rs2 = pst.executeQuery();
			while(rs2.next()) {
				if(rs2.getString("lesson1")!=null&&rs2.getString("lesson1").contentEquals(cid)) {
					sql2="update course_selection set lesson1=null where sid=?";
					pst=conn.prepareStatement(sql2);
					pst.setString(1, rs2.getString("sid"));
					pst.executeUpdate();
					dec_num(cid);
				}
				if(rs2.getString("lesson2")!=null&&rs2.getString("lesson2").contentEquals(cid)) {
					sql2="update course_selection set lesson2=null where sid=?";
					pst=conn.prepareStatement(sql2);
					pst.setString(1, rs2.getString("sid"));
					pst.executeUpdate();
					dec_num(cid);
				}
				if(rs2.getString("lesson3")!=null&&rs2.getString("lesson3").contentEquals(cid)) {
					sql2="update course_selection set lesson3=null where sid=?";
					pst=conn.prepareStatement(sql2);
					pst.setString(1, rs2.getString("sid"));
					pst.executeUpdate();
					dec_num(cid);
				}
				if(rs2.getString("lesson4")!=null&&rs2.getString("lesson4").contentEquals(cid)) {
					sql2="update course_selection set lesson4=null where sid=?";
					pst=conn.prepareStatement(sql2);
					pst.setString(1, rs2.getString("sid"));
					pst.executeUpdate();
					dec_num(cid);
				}
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public void inc_num(String cid) {
		/*
		 * 将课程的授课人数+1
		 */
		try {
			String sql="select number from course where cid=?";
			PreparedStatement pst2 = conn.prepareStatement(sql);
			pst2.setString(1, cid);
			ResultSet rscn=pst2.executeQuery();
			if(rscn.next()) {
				int temp = Integer.parseInt(rscn.getString("number"));
				temp++;
				sql = "update course set number=? where cid=?";
				pst2 = conn.prepareStatement(sql);
				pst2.setString(1, Integer.toString(temp));
				pst2.setString(2, cid);
				pst2.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public void dec_num(String cid) {
		/*
		 * 将课程的授课人数减一
		 */
		try {
			String sql="select number from course where cid=?";
			PreparedStatement pst2 = conn.prepareStatement(sql);
			pst2.setString(1, cid);
			ResultSet rscn=pst2.executeQuery();
			if(rscn.next()) {
				int temp = Integer.parseInt(rscn.getString("number"));
				temp--;
				sql = "update course set number=? where cid=?";
				pst2 = conn.prepareStatement(sql);
				pst2.setString(1, Integer.toString(temp));
				pst2.setString(2, cid);
				pst2.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public void merge_course(String sid,String pos) {
		/*
		 * 针对存在问题的学生的课表，将备选课程加入到主课表中
		 * para:  sid:存在问题的课表的学生编号
		 *        pos:指明备选课程需加入到哪个主选课程位置中
		 */
		
		try {
			//检索到两个备选课的编号
			String  sql="select lesson5,lesson6 from course_selection where sid=?";  
			pst=conn.prepareStatement(sql);
			pst.setString(1, sid);
			ResultSet rs = pst.executeQuery();
			String l5=null,l6=null,l5t=null,l6t=null;
			boolean isl5=true,isl6=true;
			if(rs.next()) {
				l5 = rs.getString("lesson5");
				l6 = rs.getString("lesson6");
			}
			//判断是否冲突
			if(l5!=null) {//若第一个备选课不为空
				sql = "select timeslot from course where cid=?";//找到第一个备选课的时间
				pst = conn.prepareStatement(sql);
				pst.setString(1, l5);
				rs = pst.executeQuery();
				if(rs.next()) {
					l5t = rs.getString("timeslot");
				}
				
				sql = "select lesson1,lesson2,lesson3,lesson4 from course_selection where sid=?";//依次检索四个主课的编号
				pst= conn.prepareStatement(sql);
				pst.setString(1, sid);
				ResultSet rs2 =	pst.executeQuery();
				if(rs2.next()) {
					if(rs2.getString("lesson1")!=null) {//如果该位置主课不为空
						String sql2="select timeslot from course where cid=?";//找到该主课的时间
						pst = conn.prepareStatement(sql2);
						pst.setString(1, rs2.getString("lesson1"));
						ResultSet rs3 = pst.executeQuery();
						if(rs3.next()) {
							if(l5t.contentEquals(rs3.getString("timeslot"))) {//比较备选课和当前主选课时间是否冲突
								isl5=false;//若冲突，则当前备选课不能被调入主选课
							}
						}
					}
					if(rs2.getString("lesson2")!=null) {//如果该位置主课不为空
						String sql2="select timeslot from course where cid=?";//找到该主课的时间
						pst = conn.prepareStatement(sql2);
						pst.setString(1, rs2.getString("lesson2"));
						ResultSet rs3 = pst.executeQuery();
						if(rs3.next()) {
							if(l5t.contentEquals(rs3.getString("timeslot"))) {//比较备选课和当前主选课时间是否冲突
								isl5=false;//若冲突，则当前备选课不能被调入主选课
							}
						}
					}
					if(rs2.getString("lesson3")!=null) {//如果该位置主课不为空
						String sql2="select timeslot from course where cid=?";//找到该主课的时间
						pst = conn.prepareStatement(sql2);
						pst.setString(1, rs2.getString("lesson3"));
						ResultSet rs3 = pst.executeQuery();
						if(rs3.next()) {
							if(l5t.contentEquals(rs3.getString("timeslot"))) {//比较备选课和当前主选课时间是否冲突
								isl5=false;//若冲突，则当前备选课不能被调入主选课
							}
						}
					}
					if(rs2.getString("lesson4")!=null) {//如果该位置主课不为空
						String sql2="select timeslot from course where cid=?";//找到该主课的时间
						pst = conn.prepareStatement(sql2);
						pst.setString(1, rs2.getString("lesson4"));
						ResultSet rs3 = pst.executeQuery();
						if(rs3.next()) {
							if(l5t.contentEquals(rs3.getString("timeslot"))) {//比较备选课和当前主选课时间是否冲突
								isl5=false;//若冲突，则当前备选课不能被调入主选课
							}
						}
					}
					
				}
				if(isl5=true) {
					sql= "update course_selection set lesson4=?,lesson5=null where sid=?";
					pst = conn.prepareStatement(sql);
					pst.setString(1, l5);//pos=‘lesson4’
					//pst.setString(2, l5);//l5是原lesson5位置处保存的课程
					pst.setString(2, sid);
					pst.executeUpdate();
					inc_num(l5);
					return;
				}
			}
			
			
			
			//判断是否冲突
			if(l6!=null) {//若第2个备选课不为空
				sql = "select timeslot from course where cid=?";//找到第一个备选课的时间
				pst = conn.prepareStatement(sql);
				pst.setString(1, l6);
				rs = pst.executeQuery();
				if(rs.next()) {
					l6t = rs.getString("timeslot");
				}
				sql = "select lesson1,lesson2,lesson3,lesson4 from course_selection where sid=?";//依次检索四个主课的编号
				pst= conn.prepareStatement(sql);
				pst.setString(1, sid);
				ResultSet rs2 =	pst.executeQuery();
				if(rs2.next()) {
					if(rs2.getString("lesson1")!=null) {//如果该位置主课不为空
						String sql2="select timeslot from course where cid=?";//找到该主课的时间
						pst = conn.prepareStatement(sql2);
						pst.setString(1, rs2.getString("lesson1"));
						ResultSet rs3 = pst.executeQuery();
						if(rs3.next()) {
							if(l6t.contentEquals(rs3.getString("timeslot"))) {//比较备选课和当前主选课时间是否冲突
								isl6=false;//若冲突，则当前备选课不能被调入主选课
							}
						}
					}
					if(rs2.getString("lesson2")!=null) {//如果该位置主课不为空
						String sql2="select timeslot from course where cid=?";//找到该主课的时间
						pst = conn.prepareStatement(sql2);
						pst.setString(1, rs2.getString("lesson2"));
						ResultSet rs3 = pst.executeQuery();
						if(rs3.next()) {
							if(l6t.contentEquals(rs3.getString("timeslot"))) {//比较备选课和当前主选课时间是否冲突
								isl6=false;//若冲突，则当前备选课不能被调入主选课
							}
						}
					}
					if(rs2.getString("lesson3")!=null) {//如果该位置主课不为空
						String sql2="select timeslot from course where cid=?";//找到该主课的时间
						pst = conn.prepareStatement(sql2);
						pst.setString(1, rs2.getString("lesson3"));
						ResultSet rs3 = pst.executeQuery();
						if(rs3.next()) {
							if(l6t.contentEquals(rs3.getString("timeslot"))) {//比较备选课和当前主选课时间是否冲突
								isl6=false;//若冲突，则当前备选课不能被调入主选课
							}
						}
					}
					if(rs2.getString("lesson4")!=null) {//如果该位置主课不为空
						String sql2="select timeslot from course where cid=?";//找到该主课的时间
						pst = conn.prepareStatement(sql2);
						pst.setString(1, rs2.getString("lesson4"));
						ResultSet rs3 = pst.executeQuery();
						if(rs3.next()) {
							if(l6t.contentEquals(rs3.getString("timeslot"))) {//比较备选课和当前主选课时间是否冲突
								isl6=false;//若冲突，则当前备选课不能被调入主选课
							}
						}
					}
					
				}
				if(isl6=true) {
					sql= "update course_selection set lesson4=?,lesson6=null where sid=?";
					pst = conn.prepareStatement(sql);
					pst.setString(1, l6);//pos=‘lesson4’
					//pst.setString(2, l6);//l5是原lesson5位置处保存的课程
					pst.setString(2, sid);
					pst.executeUpdate();
					inc_num(l6);
					return;
				}
			}
			
			
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
	}
	public void calculate_fee(String sid) {
		/*
		 * 计算学生当前课表的学费
		 * para: sid:要计算的学生的编号
		 */
		
		try {
			String course,temp;
			int fee=0;
			String sql="select lesson1,lesson2,lesson3,lesson4 from course_selection where sid="+sid;
			Statement stmt2 = conn.createStatement();
			Statement stmt = conn.createStatement();
			PreparedStatement pst;
			ResultSet rs2 = null;
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				course = rs.getString("lesson1");
				if(course!=null) {
					sql = "select fee from course where cid="+course;
					rs2 = stmt2.executeQuery(sql);
					if(rs2.next()) {
						fee+= rs2.getInt("fee");
					}
				}
				course = rs.getString("lesson2");  //出现BUG
				if(course!=null) {
					sql = "select fee from course where cid="+course;
					rs2 = stmt2.executeQuery(sql);
					if(rs2.next()) {
						fee+= rs2.getInt("fee");
					}
				}
				course = rs.getString("lesson3");
				if(course!=null) {
					sql = "select fee from course where cid="+course;
					rs2 = stmt2.executeQuery(sql);
					if(rs2.next()) {
						fee+= rs2.getInt("fee");
					}
				}
				course = rs.getString("lesson4");
				if(course!=null) {
					sql = "select fee from course where cid="+course;
					rs2 = stmt2.executeQuery(sql);
					if(rs2.next()) {
						fee+= rs2.getInt("fee");
					}
				}
			}
			sql = "update credit_card set fee=? where sid=?";
			pst=conn.prepareStatement(sql);
			pst.setInt(1, fee);
			pst.setString(2, sid);
			pst.executeUpdate();
			
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
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
