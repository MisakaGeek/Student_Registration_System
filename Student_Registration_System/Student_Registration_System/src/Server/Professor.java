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
import java.util.ArrayList;
import java.util.List;

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
		password = null;
		name = null;
		birthday = null;
		SSN = null;
		status = null;
		department = null;
		try {

			this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));// 输出流
			conn = Database.getNewConnection();
			pst = null;
			rs = null;

		} catch (SQLException | IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public String login(String id, String pw) throws SQLException {
		/*
		 * 当服务器接收到教授客户端发来的登录请求后，创建教授对象并执行此方法 1.访问数据库，对id与pw进行检索，若检索不到或不匹配，报错，返回0
		 * 2.若匹配，则检索数据库该教授的信息，存放到本对象中，并返回1表示登录成功。
		 */
		// 补充：对数据库进行检索

		this.id = id;
		this.password = pw;
		String sql;
		sql = "select pid,password from professor where pid=?";
		pst = conn.prepareStatement(sql);
		pst.setString(1, id);
		rs = pst.executeQuery();
		rs.next();
		String testidString = rs.getString("pid");
		String testpwString = rs.getString("password");
		boolean flag = false;
		if (id.equals(testidString) && pw.equals(testpwString)) {
			flag = true;
		} else {
			flag = false;
		}
		if (flag) {
			return "1";
		} else {
			return "0";
		}
	}

	public void GetCourse() throws IOException { // 获取指定学期的课程
		String semester = dis.readUTF();
		try {
			String sql;
			PreparedStatement pst;
			if (semester.equals("-----请选择-----")) { // 没有指定学期时，默认全部学期
				sql = "select distinct cname " + "from grade " + "where pid = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, id);
			} else {
				sql = "select distinct cname " + "from grade " + "where semester= ? " + "and pid = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, semester);
				pst.setString(2, id);
			}
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				dos.writeUTF(rs.getString("cname"));
				// dos.flush();
			}
			dos.writeUTF("end");
			dos.flush();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void GetGrades() throws IOException { // 获取指定学期、指定课程的学生成绩
		String semester = dis.readUTF();
		String course = dis.readUTF();
		try {
			String sql;
			PreparedStatement pst;
			if (course.equals("-----请选择-----")) {
				if (semester.equals("-----请选择-----")) {// 没有指定学期和课程，默认全部
					sql = "select grade.sid,name,semester,cname,grade "
							+ "from grade join student on grade.sid=student.sid " + "where pid = ?";
					pst = conn.prepareStatement(sql);
					pst.setString(1, id);
				} else {// 只指定了学期
					sql = "select grade.sid,name,semester,cname,grade "
							+ "from grade join student on grade.sid=student.sid " + "where semester = ? and pid = ?";
					pst = conn.prepareStatement(sql);
					pst.setString(1, semester);
					pst.setString(2, id);
				}
			} else {// 指定了学期和课程
				sql = "select grade.sid,name,semester,cname,grade "
						+ "from grade join student on grade.sid=student.sid "
						+ "where semester = ? and cname = ? and pid = ? ";
				pst = conn.prepareStatement(sql);
				pst.setString(1, semester);
				pst.setString(2, course);
				pst.setString(3, id);
			}
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				dos.writeUTF(rs.getString("sid"));
				dos.writeUTF(rs.getString("name"));
				dos.writeUTF(rs.getString("semester"));
				dos.writeUTF(rs.getString("cname"));
				dos.writeUTF(String.valueOf(rs.getInt("grade")));
				dos.flush();
			}
			dos.writeUTF("end");
			dos.flush();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void SubmitGrades() throws IOException, SQLException { // 提交学生成绩
		String str = dis.readUTF();
		PreparedStatement pst = null;
		int rs = 0;
		String sql;
		while (!str.equals("end")) {
			String id = str;
			str = dis.readUTF();
			String course = str;
			str = dis.readUTF();
			int grade = 0;
			if (str.equals("end")) // 成绩格式有误，中断传输
				return;
			if (!str.equals(""))
				grade = Integer.valueOf(str);
			str = dis.readUTF();
			sql = "Update grade set grade= ? where sid = ? and cname = ? ";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, grade);
			pst.setString(2, id);
			pst.setString(3, course);
			rs = pst.executeUpdate();
		}
	}
	 public ResultSet findQualifiedCourses() {//检索有资格教授的课程，直接検索course表，里面全是这学期开的课程，即教授有资格教授的课程，但是要除去id是自己的
			// TODO Auto-generated method stub

	    	String sql;
			try {
				stmt = conn.createStatement();
				sql = "select * from course where pid =' ' or pid ='null' or pid ='NULL' or pid is null";//查询有资格教授的课程，就是pid为空的那些，，但是显示原因，只能用空格，pid不是自己的就是别人的
				rs = stmt.executeQuery(sql);//返回查询结果
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();//数据库无法通信的代码稍后再写
			}
			return rs;
		}
	    public ResultSet preTaughtCourses(String pid) {//检索以前教授过的课程，参数为教授的id
	    	String sql;
			try {
				
				stmt = conn.createStatement(); 
				sql = "select cid,cname,credit,semester from grade where pid='"+pid+"' group by pid";//查询语句
				rs = stmt.executeQuery(sql);//返回查询结果
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();//数据库无法通信的代码稍后再写
			}
			return rs;
	    }
	    
	
	public  ResultSet isConflict(String pid,String timeslot) {//判断是否存在冲突并返回冲突课程，参数是教授pid和冲突时间，这里直接用这个类的id是null，不知道为啥
		String sql=null;
		System.out.println("进入判断冲突函数isConflict()");
		try {
			System.out.println("要判断冲突的时间："+timeslot+" 要判断冲突的教授："+pid);
			stmt = conn.createStatement();
			sql = "select cid,name,credit,timeslot from course where pid='"+pid+"' and timeslot='"+timeslot+"' ";//查询冲突
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		return rs;//如果为空说明没冲突，不为空说明有冲突
	}
	public ResultSet selectedCourse(String pid) {//查询已经选择的课程，参数是教授id
		String sql;
		try {
			stmt = conn.createStatement(); 
			System.out.println("查询"+pid+"已经选择的课程");
			sql = "select cid,name,credit,timeslot from course where pid='"+pid+"' ";
			rs = stmt.executeQuery(sql);//返回查询结果
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();//数据库无法通信的代码稍后再写
		}
		return rs;

	}
	public void dropCourse(String cid)//退课，置相应cid数据的pid为null
	{
		String sql;
		try {
			stmt = conn.createStatement(); 
			System.out.println("退cid为："+cid+"的课程");
			sql = "update course set pid = null where cid = '"+cid+"' ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();//数据库无法通信的代码稍后再写
		}
	}
	public void chooseCourse(String cid,String pid)//选课，置所选课程（cid）的教授（pid）为参数pid的值
	{
		String sql;
		try {
			stmt = conn.createStatement(); 
			System.out.println("教授："+pid+"选课程号为："+cid+"的课程");
			sql = "update course set pid = '"+pid+"' where cid = '"+cid+"' ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();//数据库无法通信的代码稍后再写
		}
	}
	
	public String inquireSemester()//查询当前学期
	{
		String sql;
		try {
			System.out.println("进入inquireSemester()函数");
			stmt = conn.createStatement(); 
			sql = "select semester from course";
			rs = stmt.executeQuery(sql);//返回查询结果
			rs.next();
			return rs.getString("semester");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();//数据库无法通信的代码稍后再写
		}
		return null;
	}
	
	public String inquirePname(String pid)//查询当前学期
	{
		String sql;
		try {
			System.out.println("进入inquirePname()函数");
			stmt = conn.createStatement(); 
			sql = "select name from professor where pid='"+pid+"'";
			rs = stmt.executeQuery(sql);//返回查询结果
			rs.next();
			return rs.getString("name");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();//数据库无法通信的代码稍后再写
		}
		return null;
	}
	public String isConceledCourse(List<String> canceled_course,String pid)//检索是否有被取消的课程，置取消的课程pid为空
	{
		String sql,sql1;
		try {
			System.out.println("进入isConceledCourse()函数");
			stmt = conn.createStatement(); 
			sql = "select cid from course where pid='"+pid+"'";//将教授pid选择的课程选出来
			System.out.println(sql);
			rs = stmt.executeQuery(sql);//返回查询结果
			System.out.println("检测错误3");
			List<String> pidcanceledcourse=new ArrayList<String>();//教授pid被取消的课程
			int index=0;//list索引
			System.out.println("检测错误1");
			while(rs.next())
			{
				for(int i=0;i<canceled_course.size();i++)
				if( rs.getString("cid").equals(canceled_course.get(i)) )
				{
					pidcanceledcourse.add(canceled_course.get(i));
				}
			}
			System.out.println("检测错误2");
			System.out.println(pidcanceledcourse.size());
			if(pidcanceledcourse.size()!=0)	{
				for(int k=0;k<pidcanceledcourse.size();k++)//取消的课程pid置为空
				{
					sql1 = "update course set pid = null where cid = '"+pidcanceledcourse.get(k)+"' ";
					stmt.executeUpdate(sql1);
				}
				String infor=new String("您所选择的"+pidcanceledcourse.size()+"门课程：课程号分别为：");
				for(int j=0;j<pidcanceledcourse.size();j++)//infor最后保存pid和它所有取消的课程
				{
					infor=infor+" "+pidcanceledcourse.get(j);
				}
				infor=infor+"的课程由于选课人数不足三人取消授课，请及时前往选择授课界面查看，以免影响您的授课";
				System.out.println("isConceledCourse（）返回信息："+infor);
				return infor;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();//数据库无法通信的代码稍后再写
		}
		String infor=new String("没有取消的课程");
		System.out.println("没有取消的课程");
		return infor;
	}
	public void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pst != null) {
				pst.close();
			}
			conn.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
