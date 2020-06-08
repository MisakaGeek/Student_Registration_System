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

public class Student {
	String id;
	String password;
	String name;
	String birthday;
	String SSN;
	String status;
	String graduation_date;
	Schdule schdule;

	Connection conn;
	PreparedStatement pst;
	ResultSet rs;

	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;

	public Student(Socket socket) {
		id = null;
		password = null;
		name = null;
		birthday = null;
		SSN = null;
		status = null;
		graduation_date = null;
		this.socket = socket;
		try {
			this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));// 输出流
			this.conn = Database.getNewConnection();
		} catch (IOException | SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public String login(String id, String pw) throws SQLException {
		/*
		 * 当服务器接收到学生客户端发来的登录请求后，创建学生对象并执行此方法 1.访问数据库，对id与pw进行检索，若检索不到或不匹配，报错，返回0
		 * 2.若匹配，则检索数据库该学生的信息，存放到本对象中，并返回1表示登录成功。
		 */
		// 补充：对数据库进行检索
		this.id = id;
		this.password = pw;
		String sql;
		sql = "select sid,password from student where sid=?";
		pst = conn.prepareStatement(sql);
		pst.setString(1, id);
		rs = pst.executeQuery();
		rs.next();
		String testidString = rs.getString("sid");
		String testpwString = rs.getString("password");
		boolean flag = false;// 测试用，之后删掉
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

	public void Register_for_Courses() {

	}

	public void ViewGrades() throws IOException { // 查看自己的成绩
		String semester = dis.readUTF();
		try {
			// 访问数据库获得学生信息
			Connection conn = Database.getNewConnection();
			String sql;
			PreparedStatement pst;
			if (semester.equals("-----请选择-----")) { // 没有选择具体学期时，默认显示全部学期的课程成绩
				sql = "select semester,cid,cname,credit,grade " + "from grade " + "where sid = ? ";
				pst = conn.prepareStatement(sql);
				pst.setString(1, id);
			} else {// 显示指定学期成绩
				sql = "select semester,cid,cname,credit,grade " + "from grade where semester = ? and " + "sid = ? ";
				pst = conn.prepareStatement(sql);
				pst.setString(1, semester);
				pst.setString(2, id);
			}
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				dos.writeUTF(rs.getString("semester"));
				dos.writeUTF(rs.getString("cid"));
				dos.writeUTF(rs.getString("cname"));
				dos.writeUTF(String.valueOf(rs.getString("credit")));
				dos.writeUTF(String.valueOf(rs.getInt("grade")));
				dos.flush();
			}
			dos.writeUTF("end");
			dos.flush();
			Database.freeDB(null, pst, conn);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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

class Schdule {
	String stu_id;
	ArrayList<String> main_lesson;
	ArrayList<String> alternate_lesson;
	int status;// 当前课表的提交状态，0未提交，1已保存，2已提交

	public Schdule() {
		stu_id = null;
		main_lesson = new ArrayList<String>(4);
		;
		alternate_lesson = new ArrayList<String>(2);
		;
		status = 0;
	}

	public void edit_id(String id) {
		stu_id = id;
	}

	public void edit_status(int s) {
		status = s;
	}

	public void edit_main_lesson(int index, String lesson_id) {
		main_lesson.add(index, lesson_id);
	}

	public void edit_alternate_lesson(int index, String lesson_id) {
		alternate_lesson.add(index, lesson_id);
	}

}
