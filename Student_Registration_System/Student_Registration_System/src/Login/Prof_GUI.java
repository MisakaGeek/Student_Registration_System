/*
 * @author 雷浩洁
 * @version 1.0
 * 教授客户端代码
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

public class Prof_GUI extends JFrame implements ActionListener {
	public int sys_width;
	public int sys_height;
	public int windowsWidth;
	public int windowsHeight;

	public String user_id;// 教授用户名，即教工号
	public String password;// 教授密码

	private JButton jb1;// 负责启动教授的两个用例：选择执教课程与提交成绩
	private JButton jb2;
	private JPanel jp;
	private JLabel title;

	// 负责与服务器通信的socket，已在构造函数中初始化
	public Socket socket;
	public DataInputStream dis;
	public DataOutputStream dos;

	public Prof_GUI(String name, String pw, Socket socket) {
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
		this.setTitle("教授选课系统");

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
		title = new JLabel("欢迎使用选课系统！", JLabel.CENTER);
		this.add(title);

		// 两个功能
		jb1 = new JButton("选择执教课程");
		jb2 = new JButton("提交成绩");
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
		if (e.getSource() == jb1) { // 选择执教课程功能
			// 补充：执行选择执教课程用例
		} else if (e.getSource() == jb2) { // 提交成绩
			// 补充：执行提交成绩用例
			try {
				SubmitGradesUI ui = new SubmitGradesUI(user_id, password, socket);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dispose();
		}
	}
}
