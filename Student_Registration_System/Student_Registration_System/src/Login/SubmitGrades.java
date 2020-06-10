/*
 * 教授提交成绩界面
 * 使用socket对象和服务器上的Professor类函数通信
 */
package Login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class SubmitGrades {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// SubmitGradesUI ui = new SubmitGradesUI();
	}
}

class SubmitGradesUI extends JFrame implements ActionListener {
	int width = 900;
	int height = 700;
	private JLabel label1, label2, label3, label4;
	private JComboBox cmb1, cmb2;
	private JScrollPane sp1;
	private JTable table1;
	private JButton button1, button2, button3;
	private Vector<Vector> rowData;// 成绩表数据
	private Vector columnNames; // 成绩表头
	private String semester = "-----请选择-----";// 教授选中的学期
	private String course = "-----请选择-----"; // 教授选中的课程
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	String user_id;
	String password;

	public SubmitGradesUI(String id, String pw, Socket socket) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor
		super("Submit Grades");
		/*
		 * int port = 8888; String host = "localhost"; socket = new Socket(host, port);
		 */
		this.user_id = id;
		this.password = pw;
		this.socket = socket;
		dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocation(0, 0);
		this.setLocationRelativeTo(null);
		setLayout(null);
		label1 = new JLabel("成绩提交");
		label2 = new JLabel("学期：");
		label3 = new JLabel("课程：");
		label4 = new JLabel("学生成绩");
		label1.setBounds(30, -30, 200, 150);
		label2.setBounds(50, 80, 80, 50);
		label3.setBounds(300, 80, 80, 50);
		label4.setBounds(width / 2 - 75, 140, 160, 50);
		Font font1 = new Font("黑体", Font.PLAIN, 30);
		Font font2 = new Font("黑体", Font.PLAIN, 20);
		Font font3 = new Font("微软雅黑", Font.PLAIN, 16);
		Font font4 = new Font("黑体", Font.PLAIN, 23);
		label1.setFont(font1);
		label2.setFont(font2);
		label3.setFont(font2);
		label4.setFont(font4);
		button1 = new JButton("提交");
		button1.setBounds(width - 150, 45, 100, 40);
		button1.setFont(font2);
		button1.setForeground(Color.white);
		button1.setBackground(new Color(178, 34, 34));
		button2 = new JButton("返回");
		button2.setBounds(width - 150, 100, 100, 40);
		button2.setFont(font2);
		button2.setForeground(Color.white);
		button2.setBackground(new Color(74, 112, 139));
		button3 = new JButton("查询");
		button3.setBounds(560, 85, 80, 40);
		button3.setFont(font3);
		cmb1 = new JComboBox();
		cmb1.addItem("-----请选择-----");
		cmb1.setBounds(110, 85, 170, 40);
		cmb1.setFont(font3);
		// 获取该教授已授课程学期信息,实际应从数据库访问！！
		Vector<String> semester = new Vector<String>();
		semester.add("2020年第二学期");
		semester.add("2020年第一学期");
		semester.add("2019年第二学期");
		semester.add("2019年第一学期");
		for (int i = 0; i < semester.size(); i++)
			cmb1.addItem(semester.get(i));
		cmb2 = new JComboBox();
		cmb2.addItem("-----请选择-----");
		cmb2.setBounds(360, 85, 170, 40);
		cmb2.setFont(font3);

		rowData = new Vector<Vector>();
		columnNames = new Vector<String>();
		columnNames.add("学号");
		columnNames.add("学生姓名");
		columnNames.add("学期");
		columnNames.add("课程名称");
		columnNames.add("成绩");

		table1 = new JTable(rowData, columnNames); // 创建指定列名和数据的表格
		sp1 = new JScrollPane(table1); // 创建显示表格的滚动面板
		sp1.setBounds(60, 195, width - 140, height - 260);
		JTableHeader tableHeader = table1.getTableHeader();
		tableHeader.setFont(font2);
		table1.setRowHeight(30);
		table1.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
		tcr.setHorizontalAlignment(JLabel.CENTER);
		table1.setDefaultRenderer(Object.class, tcr);

		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		cmb1.addActionListener(this);
		this.add(label1);
		this.add(label2);
		this.add(label3);
		this.add(label4);
		this.add(button1);
		this.add(button2);
		this.add(button3);
		this.add(cmb1);
		this.add(cmb2);
		this.add(sp1, BorderLayout.CENTER);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button1) // 提交数据库
		{
			try {
				dos.writeUTF("27"); // 对应服务类SumbitGrades()
				for (int i = 0; i < rowData.size(); i++) {
					dos.writeUTF((String) rowData.get(i).get(0)); // 学号
					dos.writeUTF((String) rowData.get(i).get(3)); // 课程名称
					String grade = (String) rowData.get(i).get(4);
					for (int j = 0; j < grade.length(); j++)
						if (!Character.isDigit(grade.charAt(j))) {
							JOptionPane.showMessageDialog(null, "成绩输入有误，请重新输入！");
							dos.writeUTF("end");
							dos.flush();
							return;
						}
					dos.writeUTF(grade); // 成绩

					dos.flush();
				}
				dos.writeUTF("end");
				dos.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == button2) {
			// 先返回到上一级界面！！

			Prof_GUI ui = new Prof_GUI(user_id, password, socket);
			dispose();
		} else if (e.getSource() == button3) {
			course = cmb2.getSelectedItem().toString();
			rowData.clear();
			// 访问数据库获得学生信息！！
			try {
				dos.writeUTF("26");// 对应服务器教授类GetGrades()
				dos.writeUTF(semester);
				dos.writeUTF(course);
				dos.flush();
				String str = dis.readUTF();
				while (!str.equals("end")) {
					Vector tmp = new Vector();
					for (int i = 0; i < 5; i++) {
						tmp.add(str);
						str = dis.readUTF();
					}
					rowData.add(tmp);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 动态地向table添加数据
			DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
			table1.setModel(model);
		} else if (e.getSource() == cmb1) {
			// 通过选定的semester从数据获取该教授的课程！！
			semester = cmb1.getSelectedItem().toString();
			Vector<String> course = new Vector<String>();
			course.add("-----请选择-----");
			try {
				dos.writeUTF("25");// 对应服务端教授类GetCourse()
				dos.writeUTF(semester);
				dos.flush();
				String str = dis.readUTF();
				while (!str.equals("end")) {
					course.add(str);
					str = dis.readUTF();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 动态地向ComboBox添加数据
			ComboBoxModel model = new DefaultComboBoxModel(course);
			cmb2.setModel(model);
		}
	}

}
