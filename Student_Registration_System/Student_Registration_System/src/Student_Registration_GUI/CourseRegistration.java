package Student_Registration_GUI;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Login.Stu_GUI;

class Schedule {
	String stu_id;
	ArrayList<String> main_lesson;
	ArrayList<String> alternate_lesson;
}

/*
 * 功能：这是Student用户界面的二级界面，用于显示课表信息，可选课程列表信息，以及一些按钮。
 */
public class CourseRegistration {
	private Stu_GUI student;
	private CrFrame frame;
	private Socket socket;
	private Schedule schedule;
	private static int port = 8888; //服务器端口
	private static String host = "localhost";
	
	String getStudentId() {
		return student.user_id;
	}
	/*
	 * 设置studnet的schedule，临时的
	 */
	void setStudentMainLesson(int loc, String les) {
		schedule.main_lesson.set(loc, les);
	}
	void setStudentAltLesson(int loc, String les) {
		schedule.alternate_lesson.set(loc, les);
	}
	
	void retToUpLevel() {
		try {
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			DataInputStream dis = new DataInputStream(
	                new BufferedInputStream(socket.getInputStream()));
			dos.writeUTF("1a");
			dos.flush();
			dis.readUTF();
		}catch(Exception e) {
			e.printStackTrace();
		}
		frame.dispose();
		student.setVisible(true);
	}
	void saveSchedule() {
		try {
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			DataInputStream dis = new DataInputStream(
	                new BufferedInputStream(socket.getInputStream()));
			//输出数据
			dos.writeUTF("16");
			dos.writeUTF(student.user_id);
			for(int i=0;i<4;i++) {
				dos.writeUTF(schedule.main_lesson.get(i));
			}
			for(int i=0;i<2;i++) {
				dos.writeUTF(schedule.alternate_lesson.get(i));
			}
			dos.flush();
			
			//接收反馈信息
			System.out.println(dis.readUTF());
			//把gui中的课程状态改变一下
			frame.setAllSelectedCourseState("saved");
			
			frame.showMessageDialog("保存课表成功");	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//测试////////////////////////////
		/*frame.setAllSelectedCourseState("saved");
		frame.showMessageDialog("保存课表成功");*/
		///////////////////////////////////
	}
	boolean submitSchedule() {
		boolean success = false;
		try {
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			DataInputStream dis = new DataInputStream(
	                new BufferedInputStream(socket.getInputStream()));
			//输出数据
			dos.writeUTF("17");
			dos.writeUTF(student.user_id);
			for(int i=0;i<4;i++) {
				dos.writeUTF(schedule.main_lesson.get(i));
			}
			for(int i=0;i<2;i++) {
				dos.writeUTF(schedule.alternate_lesson.get(i));
			}
			dos.flush();
			
			//接收反馈信息,根据反馈信息，调用frame的提示框
			char res = dis.readChar();
			if(res == '1') {
				//把gui中的课程状态改变一下
				frame.setAllSelectedCourseState("enrolled_in");
				success = true;
				frame.showMessageDialog("提交课表成功");
			}else if(res == '2') {
				String course, pre;
				course = dis.readUTF();
				pre = dis.readUTF();
				frame.showMessageDialog(course+"的前导课程"+pre+"没有学");
			}else if(res == '3') {
				String course;
				course = dis.readUTF();
				frame.showMessageDialog(course+"人数满了");
			}else {
				String c1, c2;
				c1 = dis.readUTF();
				c2 = dis.readUTF();
				frame.showMessageDialog(c1+"和"+c2+"时间冲突");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return success;
		
		//测试//////////////////////////////////////////
		/*char res = '1';
		if(res == '1') {
			//把gui中的课程状态改变一下
			frame.setAllSelectedCourseState("enrolled_in");
			frame.showMessageDialog("提交课表成功");
		}else if(res == '2') {
			String course, pre;
			course = "高数";
			pre = "线代";
			frame.showMessageDialog(course+"的前导课程"+pre+"没有学");
		}else if(res == '3') {
			String course;
			course = "离散";
			frame.showMessageDialog(course+"人数满了");
		}else {
			String c1, c2;
			c1 = "英语";
			c2 = "数学";
			frame.showMessageDialog(c1+"和"+c2+"时间冲突");
		}*/
		/////////////////////////////////////////////////////////
	}
	
	/*
	 * alt==true, 则把id添加到alternate_lesson中
	 * alt==false, 则把id添加到main_lesson中
	 */
	void selectCourseOffering(String id, boolean alt) {
		if(id.equals("")) {
			frame.showMessageDialog("未选择任何课程");
			return;
		}
		for(int i=0;i<4;i++) {
			if(schedule.main_lesson.get(i).equals(id)) {
				frame.showMessageDialog("已选过该课程");
				return;
			}
		}
		for(int i=0;i<2;i++) {
			if(schedule.alternate_lesson.get(i).equals(id)) {
				frame.showMessageDialog("已选过该课程");
				return;
			}
		}
		if(alt == true) {
			if(schedule.alternate_lesson.get(0).equals("")) {
				schedule.alternate_lesson.set(0, id);
				frame.setCourseButText(5, id);
				frame.showMessageDialog("添加成功");
			}else if(schedule.alternate_lesson.get(1).equals("")) {
				schedule.alternate_lesson.set(1, id);
				frame.setCourseButText(6, id);
				frame.showMessageDialog("添加成功");
			}else {
				frame.showMessageDialog("备选课程满");
			}
		}else {
			for(int i=0;i<4;i++) {
				if(schedule.main_lesson.get(i).equals("")) {
					schedule.main_lesson.set(i, id);
					frame.setCourseButText(i+1, id);
					frame.showMessageDialog("添加成功");
					//测试用例//////////////////////
					for(int j=0;j<4;j++) {
						System.out.println(schedule.main_lesson.get(j));
					}
					for(int j=0;j<2;j++) {
						System.out.println(schedule.alternate_lesson.get(j));
					}
					///////////////////////////////////
					return;
				}
			}
			frame.showMessageDialog("主课程满");
		}
		
	}
	
	void deleteSchedule() {
		try {
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			DataInputStream dis = new DataInputStream(
	                new BufferedInputStream(socket.getInputStream()));
			//输出数据
			dos.writeUTF("15");
			dos.writeUTF(student.user_id);
			dos.flush();
		
			//接收反馈信息,根据反馈信息，若没有提交的课表，则调用frame的提示框，若有课表，则调用新的界面
			char res = dis.readChar();
			if(res == '1') {
				schedule = new Schedule();
				schedule.main_lesson = new ArrayList<String>();
				schedule.alternate_lesson = new ArrayList<String>();
				for(int i=0;i<4;i++) {
					schedule.main_lesson.add("");
				}
				for(int i=0;i<2;i++) {
					schedule.alternate_lesson.add("");
				}
				for(int i=0;i<4;i++) {
					String str = dis.readUTF();
					schedule.main_lesson.set(i, str);
				}
				for(int i=0;i<2;i++) {
					String str = dis.readUTF();
					schedule.alternate_lesson.set(i, str);
				}
				frame.deleteScheduleLayout(schedule.main_lesson, schedule.alternate_lesson);
			}else {
				frame.showMessageDialog("没有提交的课表，不能删除");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//测试用例///////////////////////////////////////////
		/*char res = '1';
		if(res == '1') {
			student.schedule = new Schedule();
			student.schedule.main_lesson = new ArrayList<String>();
			student.schedule.alternate_lesson = new ArrayList<String>();
			for(int i=0;i<4;i++) {
				student.schedule.main_lesson.add("");
			}
			for(int i=0;i<2;i++) {
				student.schedule.alternate_lesson.add("");
			}
			for(int i=0;i<4;i++) {
				student.schedule.main_lesson.set(i, "高数");
			}
			for(int i=0;i<2;i++) {
				student.schedule.alternate_lesson.set(i, "英语");
			}
			frame.deleteScheduleLayout(student.schedule.main_lesson, student.schedule.alternate_lesson);
		}else {
			frame.showMessageDialog("没有提交的课表，不能删除");
		}*/
		/////////////////////////////////////////
	}
	
	void submitDeleteSchedule() {
		boolean res = frame.showConfirmDialog("删除课表？", "消息");
		if(res) {
			try {
				DataOutputStream dos = new DataOutputStream(
						new BufferedOutputStream(socket.getOutputStream()));
				DataInputStream dis = new DataInputStream(
		                new BufferedInputStream(socket.getInputStream()));
				//输出数据
				dos.writeUTF("19");
				dos.writeUTF(student.user_id);
				dos.flush();
				//接收反馈信息
				dis.readUTF();
				
				frame.showMessageDialog("删除课表成功");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		//测试/////////////////////////
		/*boolean res = frame.showConfirmDialog("删除课表？", "消息");
		if(res) frame.showMessageDialog("删除课表成功");*/
		///////////////////////////
	}
	
	/*
	 * 未定义完成
	 */
	void createSchedule() {
		DataOutputStream dos;
		DataInputStream dis;
		char res; //返回结果的类型
		try {
			dos = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			dis = new DataInputStream(
	                new BufferedInputStream(socket.getInputStream()));
			//输出数据
			dos.writeUTF("13");
			dos.writeUTF(student.user_id);
			dos.flush();
			//接收反馈信息,
			res = dis.readChar();
			if(res == '1') {
				frame.showMessageDialog("已有保存或提交的课表");
			}else if(res == '2') {
				schedule = new Schedule();
				schedule.main_lesson = new ArrayList<String>();
				schedule.alternate_lesson = new ArrayList<String>();
				for(int i=0;i<4;i++) schedule.main_lesson.add("");
				for(int i=0;i<2;i++) schedule.alternate_lesson.add("");
				int num = dis.readInt(); //可选课程的数量
				ArrayList<String> cofs = new ArrayList<String>();
				for(int i=0;i<num;i++) {
					String name = dis.readUTF();
					cofs.add(name);
				}
				frame.createScheduleLayout(cofs);
			}else if(res == '3') {
				frame.showMessageDialog("不能连接到数据库");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void updateSchedule() {
		try {
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			DataInputStream dis = new DataInputStream(
	                new BufferedInputStream(socket.getInputStream()));
			//输出数据
			dos.writeUTF("14");
			dos.writeUTF(student.user_id);
			dos.flush();
		
			//接收反馈信息,根据反馈信息
			char res = dis.readChar();
			if(res == '1') {
				schedule = new Schedule();
				schedule.main_lesson = new ArrayList<String>();
				schedule.alternate_lesson = new ArrayList<String>();
				for(int i=0;i<4;i++) {
					String course = dis.readUTF();
					schedule.main_lesson.add(course);
				}
				for(int i=0;i<2;i++) {
					String course = dis.readUTF();
					schedule.alternate_lesson.add(course);
				}
				String state = dis.readUTF(); //课表的状态
				int num = dis.readInt(); //可选课程的数量
				ArrayList<String> cofs = new ArrayList<String>();
				for(int i=0;i<num;i++) {
					String name = dis.readUTF();
					cofs.add(name);
				}
				frame.updateScheduleLayout(schedule.main_lesson, schedule.alternate_lesson, cofs, state);
			}else if(res == '2'){
				frame.showMessageDialog("不存在保存或提交的课表");
			}else {
				frame.showMessageDialog("课程目录找不到");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//测试用例//////////////////////////
		/*char res = '1';
		if(res == '1') {
			student.schedule = new Schedule();
			student.schedule.main_lesson = new ArrayList<String>();
			student.schedule.alternate_lesson = new ArrayList<String>();
			student.schedule.main_lesson.add("高数");
			student.schedule.main_lesson.add("");
			student.schedule.main_lesson.add("离散");
			student.schedule.main_lesson.add("");
			student.schedule.alternate_lesson.add("英语");
			student.schedule.alternate_lesson.add("");
			String state = "saved"; //课表的状态
			int num = 3; //可选课程的数量
			ArrayList<String> cofs = new ArrayList<String>();
			cofs.add("高数");
			cofs.add("操作系统");
			frame.updateScheduleLayout(student.schedule.main_lesson, student.schedule.alternate_lesson, cofs, state);
		}else if(res == '2'){
			frame.showMessageDialog("不存在保存或提交的课表");
		}else {
			frame.showMessageDialog("课程目录找不到");
		}*/
		///////////////////////////
	}
	/*
	 * 构造函数，显示界面
	 */
	public CourseRegistration(Stu_GUI student) {
		this.student = student;
		this.socket = student.socket;
		frame = new CrFrame(this);
	}
	
	
}
	
	
