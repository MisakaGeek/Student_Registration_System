/*
 * 学生查看成绩界面
 * 使用socket对象和服务器上的学生类函数通信
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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ViewReportCard {
	public static void main(String[] args) throws UnknownHostException, IOException {
		// ViewReportCardUI ui = new ViewReportCardUI();
	}
}

class ViewReportCardUI extends JFrame implements ActionListener {
	int width = 900;
	int height = 700;
	private JLabel label1, label2, label3;
	private JComboBox cmb1;
	private JScrollPane sp1;
	private JTable table1;
	private JButton button1, button2;
	private String semester = "-----请选择-----"; // 学生选中查询的学期
	private Vector<Vector> rowData;
	Vector columnNames;
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	String user_id;
	String password;

	public ViewReportCardUI(String id, String pw, Socket socket) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub
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
		label1 = new JLabel("成绩查询");
		label2 = new JLabel("学期：");
		label3 = new JLabel("学期成绩");
		label1.setBounds(30, -30, 200, 150);
		label2.setBounds(60, 80, 80, 50);
		label3.setBounds(width / 2 - 75, 140, 160, 50);
		Font font1 = new Font("黑体", Font.PLAIN, 30);
		Font font2 = new Font("黑体", Font.PLAIN, 20);
		Font font3 = new Font("微软雅黑", Font.PLAIN, 16);
		Font font4 = new Font("黑体", Font.PLAIN, 23);
		label1.setFont(font1);
		label2.setFont(font2);
		label3.setFont(font4);
		button1 = new JButton("返回");
		button1.setBounds(width - 150, 40, 100, 40);
		button1.setFont(font2);
		button1.setForeground(Color.white);
		button1.setBackground(new Color(74, 112, 139));
		button2 = new JButton("查询");
		button2.setBounds(330, 85, 80, 40);
		button2.setFont(font3);
		cmb1 = new JComboBox();
		cmb1.addItem("-----请选择-----");
		cmb1.setBounds(130, 85, 170, 40);
		cmb1.setFont(font3);

		Vector<String> semester = new Vector<String>();
		semester.add("2020年第二学期");
		semester.add("2020年第一学期");
		semester.add("2019年第二学期");
		semester.add("2019年第一学期");
		for (int i = 0; i < semester.size(); i++)
			cmb1.addItem(semester.get(i));

		rowData = new Vector<Vector>();
		columnNames = new Vector();
		columnNames.add("学期");
		columnNames.add("选课课号");
		columnNames.add("课程名称");
		columnNames.add("学分");
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
		this.add(label1);
		this.add(label2);
		this.add(label3);
		this.add(button1);
		this.add(button2);
		this.add(cmb1);
		this.add(sp1, BorderLayout.CENTER);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button1) {
			// 先跳转上一级界面！！
			Stu_GUI ui = new Stu_GUI(user_id, password, socket);
			dispose();
		} else if (e.getSource() == button2) {
			semester = cmb1.getSelectedItem().toString();
			rowData.clear();
			try {
				dos.writeUTF("18");// 对应服务器学生类ViewGrades()
				dos.writeUTF(semester);
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
			DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
			table1.setModel(model);

		}
	}
}
