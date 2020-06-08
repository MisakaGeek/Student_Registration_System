  
/*
 * @author 雷浩洁
 * @version 1.0
 * 学生客户端代码
 */
package Login;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.JOptionPane;

import Student_Registration_GUI.CourseRegistration;

public class Stu_GUI extends JFrame implements ActionListener {
	public int sys_width;
	public int sys_height;
	public int windowsWidth;
	public int windowsHeight;

	public String user_id;// 学生用户名，即学号
	public String password;// 学生密码

	private JButton jb1;// 负责启动学生的两个用例：选课与查看成绩单
	private JButton jb2;
	private JPanel jp;
	private JLabel title;

	public Socket socket;
	public DataInputStream dis;
	public DataOutputStream dos;

	public Stu_GUI(String name, String pw, Socket socket) {
		this.user_id = name;
		this.password = pw;
		this.socket = socket;
		try {
			this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		this.setTitle("学生选课系统");

		// 设置窗口大小
		sys_width = Toolkit.getDefaultToolkit().getScreenSize().width;
		sys_height = Toolkit.getDefaultToolkit().getScreenSize().height;
		windowsWidth = 350;
		windowsHeight = 350;
		this.setSize(windowsWidth, windowsHeight);
		this.setBounds((sys_width - windowsWidth) / 2, (sys_height - windowsHeight) / 2, windowsWidth, windowsHeight);

		// 布局
		this.setLayout(new GridLayout(2, 1));

		// 欢迎语
		title = new JLabel("欢迎使用学生选课系统！", JLabel.CENTER);
		this.add(title);

		// 两个功能
		jb1 = new JButton("选课");
		jb2 = new JButton("查看成绩单");
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jp = new JPanel();
		jp.setLayout(new GridLayout(1, 2));
		jp.add(jb1);
		jp.add(jb2);
		this.add(jp);

		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb1) { // 选课功能
			/*
			 * 1.判断当前服务端是否处于可以选课的状态内 2.若已关闭注册，则弹出对话框提示错误，回到学生GUI的首界面
			 * 3.若可以选课，则关闭当前界面，调用二级界面的GUI，执行选课的功能
			 */
			// 补充：执行选课用例

			try {
				dos.writeUTF("12");
				dos.flush();
				char res = dis.readChar();
				System.out.println(res);
				if(res == '2') {
					setVisible(false);
					new CourseRegistration(this);
				}else if(res == '1') {
					JOptionPane.showMessageDialog(this,"注册关闭", "消息", JOptionPane.PLAIN_MESSAGE);
				}
			}catch(Exception e1) {
				e1.printStackTrace();
			}

		} else if (e.getSource() == jb2) { // 查看成绩单
			// 补充：执行查看成绩单用例
			try {
				ViewReportCardUI ui = new ViewReportCardUI(user_id, password, socket);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dispose();
		}
	}
}
