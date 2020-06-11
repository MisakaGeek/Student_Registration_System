package Login;
/*
 * 显示冲突课程并可以进行下一步操作
 * */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Login.NotRegisterTimeShowCourseGUI.windowlistener;


public class ConflictCourseGUI extends JDialog implements ActionListener{//显示冲突课程并可进行进一步的操作
	private JButton button1,button2;//删除冲突课程，撤销选课操作
	private JLabel label1,label2;//与您当前所选课程冲突的课程是，您可以进行以下操作
	private JTable jTable,jTable1,jTable2;//冲突课程信息表，有资格教授课程信息表，已经选择的课程信息表
	private String[] conflicttable;//冲突课程
	public Socket socket0;
	private ShowCourseGUI showGUI;//保存按动按钮的界面，用于有冲突的时候仍显示但不可操作
	public ConflictCourseGUI(JTable jtable1,JTable jtable2,Socket socket,String[] conflicttable,ShowCourseGUI showGUI0) {
		//super("所选课程冲突");
		setModal(true);//锁定当前窗口，不能在父窗口执行操作
		this.socket0=socket;
		this.jTable1=jtable1;
		this.jTable2=jtable2;
		this.conflicttable=conflicttable;
		this.showGUI=showGUI0;
		
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;//得到显示屏的宽和高
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		setBounds( (width-900)/2, (height-400)/2 ,900,400);//设置窗体应该出现的位置
		setLayout(null);
	
		
       
        //以下设置冲突课程信息表
        label1=new JLabel("与您所选课程冲突的课程如下：");
		label1.setFont(new Font("楷体", Font.PLAIN, 18));
		label1.setBounds(300,35,300,20);
		this.add(label1);
		
        Vector vData0 = new Vector();//表格数据
		Vector vName0 = new Vector();//表格头
		vName0.add(conflicttable[0]);
		vName0.add(conflicttable[1]);
		vName0.add(conflicttable[2]);
		vName0.add(conflicttable[3]);
		
		int k=4;
		//添加表格数据，conflicttable.length/4行，4列
	    for(int i=0;i<conflicttable.length/4-1;i++)
	    {
	    	Vector vRow0 = new Vector();
			for(int j=0;j<4;j++)
			{
				vRow0.add(conflicttable[k]);
				k++;
			}
			vData0.add(vRow0);
	    }	
	    
		DefaultTableModel model0 = new DefaultTableModel(vData0, vName0);//用于创建表格的内容
		
		jTable = new JTable();//创建空表格
		jTable.setModel(model0);//添加表格内容
		JScrollPane scroll0 = new JScrollPane(jTable);//添加滚动条
		scroll0.setBounds(30, 80, 800,150);//设置表格和滚动条大小
		this.add(scroll0);
		DefaultTableCellRenderer tcr0 = new DefaultTableCellRenderer();// 设置table内容居中
		tcr0.setHorizontalAlignment(SwingConstants.CENTER);
		jTable.setDefaultRenderer(Object.class, tcr0);
		//以下设置表格样式
		JTableHeader head0 = jTable.getTableHeader(); // 创建表格标题对象
        head0.setPreferredSize(new Dimension(head0.getWidth(), 35));// 设置表头大小
        head0.setFont(new Font("楷体", Font.PLAIN, 18));// 设置表格字体
        jTable.setRowHeight(30);// 设置表格行宽
        jTable.setFont(new Font("楷体", Font.PLAIN, 18));// 设置表格字体
        jTable.setSelectionForeground(Color.RED);//选中的行显示红色
        jTable.setEnabled(false);
        label2=new JLabel("您可以进行以下操作：");
		label2.setFont(new Font("楷体", Font.PLAIN, 18));
		label2.setBounds(320,250,300,20);
		this.add(label2);
		
		button1=new JButton("删除以上冲突课程");
        button1.setBounds(180,290,200,30);
        this.add(button1);
        button1.addActionListener(this);
        
		button2=new JButton("撤销当前选课操作");
        button2.setBounds(450,290,200,30);
        this.add(button2);
        button2.addActionListener(this);
        
       // setModal(true);
		setVisible(true);//设置窗体可见
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==button1)//退课
		{
			System.out.println("按下退课按钮");
			//判断要退的课在第三个表格的第几行
			int row;
			System.out.println("已选择课程表格有几行（不算表头）："+jTable2.getRowCount());
			
			for(row=0;row<jTable2.getRowCount();row++)//不算表头，第一行为0行
			{
				
				if(jTable2.getValueAt(row, 0).equals(conflicttable[4]))//如果找到了就退出循环，row中就是要找的行
					break;
			}
			System.out.println("要删除的课在已选课程的第几行："+row);
			
			DefaultTableModel model=(DefaultTableModel) jTable2.getModel();//获取defaulttablemodel
			
			Vector<String> rowData=new <String>Vector();
			
			System.out.println("有几列："+jTable1.getColumnCount());
			
			for(int i=0;i<jTable1.getColumnCount()-1;i++)//得到有多少列，将这一行的数据加入rowData中
				rowData.add((String) jTable2.getValueAt(row, i));
			
			System.out.println("要删除的数据："+rowData);
			model.removeRow(row);//删除选中的行，然后加入下一个表格里

			DefaultTableModel amodel = new DefaultTableModel(); 
			amodel=(DefaultTableModel) jTable1.getModel();
			amodel.addRow(rowData);
			try {
				DataInputStream dis = new DataInputStream(
				        new BufferedInputStream(socket0.getInputStream()));
				DataOutputStream dos = new DataOutputStream(
						new BufferedOutputStream(socket0.getOutputStream()));
				dos.writeUTF("2c");//给服务器发送2c代码,表示要退课，数据库相应位置置null
				dos.flush();	
				dos.writeUTF(rowData.get(0));//发送要更改的数据的cid
				dos.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "退课成功！", "操作成功",JOptionPane.INFORMATION_MESSAGE);
			dispose();
			
		}
		else if(e.getSource()==button2)//撤销操作，显示消息，关闭当前界面
		{
			JOptionPane.showMessageDialog(null, "操作撤销成功！", "操作成功",JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	
		
	}
	
	
} 