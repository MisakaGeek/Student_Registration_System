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
