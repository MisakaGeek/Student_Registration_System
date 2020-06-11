package Login;
/*
 * 显示教授已经教授过的课程和有资格教授的课程，并可以选课，返回上一层
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


public class ShowCourseGUI extends JFrame implements ActionListener{//显示有资格教授的课程和已经教授过的课程
	private JButton button2;//选择教授课程，返回上一层
	private JLabel label1,label2,label3;//label3显示当前学期
	private JTable jTable0,jTable1,jTable2;
	private String[] qualifiedtable0;//有资格教授的课程
	private String[] pretaughttable0;//已经教授过的课程
	private String[] selectedtable0;//已经选择的课程，直接查询出来，这里就没有通过服务器了，而是直接和数据库交互
	public String user_id;//教授用户名，即教工号
	public String password;//教授密码
	public Socket socket0;
	private String semester;
	public ShowCourseGUI(String name, String pw,Socket socket,String[] qualifiedtable,String[] pretaughttable,String[] selectedtable) {
		super("教授选课界面");
		this.user_id = name;
		this.password = pw;
		this.socket0=socket;
		this.qualifiedtable0=qualifiedtable;//有资格教授的课程
		this.pretaughttable0=pretaughttable;//已经教授过的课程
		this.selectedtable0=selectedtable;//已经选择过的课程
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;//得到显示屏的宽和高
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		setBounds( (width-900)/2, (height-790)/2 ,900,790);//设置窗体应该出现的位置
		setLayout(null);
	
		
        button2=new JButton("返回");//返回上一层
        button2.setBounds(30,10,80,30);
        this.add(button2);
        
        button2.addActionListener(this);
        try {
			DataInputStream dis = new DataInputStream(
			        new BufferedInputStream(socket.getInputStream()));
			DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(socket.getOutputStream()));
			System.out.println("向服务器发送29代码");
			dos.writeUTF("29");
			dos.flush();
			semester=dis.readUTF();
			System.out.println("gui界面标签判断出学期："+semester);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        label3=new JLabel("当前学期为："+semester);//显示当前学期
        label3.setFont(new Font("楷体", Font.PLAIN, 18));
        label3.setBounds(550,40,300,20);
        this.add(label3);
        
        //已下设置已经教授过的课程显示
        label2=new JLabel("您已经教授过的课程如下：");
		label2.setFont(new Font("楷体", Font.PLAIN, 18));
		label2.setBounds(30,50,300,20);
		this.add(label2);
		
        Vector vData0 = new Vector();//表格数据
		Vector vName0 = new Vector();//表格头
		vName0.add(pretaughttable0[0]);
		vName0.add(pretaughttable0[1]);
		vName0.add(pretaughttable0[2]);
		vName0.add(pretaughttable0[3]);
		
		int k=4;
		//添加表格数据，qualifiedtable0.length/4行，4列
	    for(int i=0;i<pretaughttable0.length/4-1;i++)
	    {
	    	Vector vRow0 = new Vector();
			for(int j=0;j<4;j++)
			{
				vRow0.add(pretaughttable0[k]);
				k++;
			}
			vData0.add(vRow0);
	    }	
	    
		DefaultTableModel model0 = new DefaultTableModel(vData0, vName0);//用于创建表格的内容
		
		jTable0 = new JTable();//创建空表格
		jTable0.setModel(model0);//添加表格内容
		JScrollPane scroll0 = new JScrollPane(jTable0);//添加滚动条
		scroll0.setBounds(30, 80, 800,150);//设置表格和滚动条大小
		this.add(scroll0);
		DefaultTableCellRenderer tcr0 = new DefaultTableCellRenderer();// 设置table内容居中
		tcr0.setHorizontalAlignment(SwingConstants.CENTER);
		jTable0.setDefaultRenderer(Object.class, tcr0);
		//以下设置表格样式
		JTableHeader head0 = jTable0.getTableHeader(); // 创建表格标题对象
        head0.setPreferredSize(new Dimension(head0.getWidth(), 35));// 设置表头大小
        head0.setFont(new Font("楷体", Font.PLAIN, 18));// 设置表格字体
        jTable0.setRowHeight(30);// 设置表格行宽
        jTable0.setFont(new Font("楷体", Font.PLAIN, 18));// 设置表格字体
        jTable0.setSelectionForeground(Color.RED);//选中的行显示红色
        jTable0.setEnabled(false);  //不可编辑
        
        //下面设置有资格教授的课程显示
		label1=new JLabel("您有资格教授的课程如下：");
		label1.setFont(new Font("楷体", Font.PLAIN, 18));
		label1.setBounds(30,250,300,20);
		this.add(label1);
		
		Vector vData = new Vector();//表格数据
		Vector vName = new Vector();//表格头
		vName.add(qualifiedtable0[0]);
		vName.add(qualifiedtable0[1]);
		vName.add(qualifiedtable0[2]);
		vName.add(qualifiedtable0[3]);
		vName.add("执行操作");
		
		k=4;
		//添加表格数据，qualifiedtable0.length/4行，4列
	    for(int i=0;i<qualifiedtable0.length/4-1;i++)
	    {
	    	Vector vRow = new Vector();
			for(int j=0;j<4;j++)
			{
				vRow.add(qualifiedtable0[k]);
				k++;
			}
			vData.add(vRow);
	    }	
		DefaultTableModel model = new DefaultTableModel(vData, vName)//用于创建表格的内容
		{
  			public boolean isCellEditable(int row,int column){  
  			    if(column == 4){  //只允许第四列被编辑
  			       return true;  
  			    }else{  
  			       return false;  
  			    }  
  			}
  		};
		
		jTable1 = new JTable();//创建空表格
		jTable1.setModel(model);//添加表格内容
		JScrollPane scroll = new JScrollPane(jTable1);//添加滚动条
		scroll.setBounds(30, 280, 800,200);//设置表格和滚动条大小
		this.add(scroll);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		jTable1.setDefaultRenderer(Object.class, tcr);
		//以下设置表格样式
		JTableHeader head = jTable1.getTableHeader(); // 创建表格标题对象
        head.setPreferredSize(new Dimension(head.getWidth(), 35));// 设置表头大小
        head.setFont(new Font("楷体", Font.PLAIN, 18));// 设置表格字体
        jTable1.setRowHeight(30);// 设置表格行宽
        jTable1.setFont(new Font("楷体", Font.PLAIN, 18));// 设置表格字体
        jTable1.setSelectionForeground(Color.RED);
        //jTable1.setEnabled(false);
        
      //下面设置已经选择的课程
      		label2=new JLabel("您已经选择的课程如下：");
      		label2.setFont(new Font("楷体", Font.PLAIN, 18));
      		label2.setBounds(30,510,300,20);
      		this.add(label2);
      		
      		Vector vData2 = new Vector();
      		Vector vName2 = new Vector();
      		vName2.add(selectedtable0[0]);
      		vName2.add(selectedtable0[1]);
      		vName2.add(selectedtable0[2]);
      		vName2.add(selectedtable0[3]);
      		vName2.add("执行操作");
      		k=4;
    		//添加表格数据，selectedtable0.length/4行，4列
    	    for(int i=0;i<selectedtable0.length/4-1;i++)
    	    {
    	    	Vector vRow2 = new Vector();
    			for(int j=0;j<4;j++)
    			{
    				vRow2.add(selectedtable0[k]);
    				k++;
    			}
    			vData2.add(vRow2);
    	    }	
      		
      		DefaultTableModel model2 = new DefaultTableModel(vData2, vName2)//用于创建表格的内容
      		{
      			public boolean isCellEditable(int row,int column){  
      			    if(column == 4){  //只允许第四列被编辑
      			       return true;  
      			    }else{  
      			       return false;  
      			    }  
      			}
      		};
      		jTable2 = new JTable();//创建空表格
      		jTable2.setModel(model2);//添加表格内容
      		JScrollPane scroll2 = new JScrollPane(jTable2);//添加滚动条
      		scroll2.setBounds(30, 540, 800,180);//设置表格和滚动条大小
      		this.add(scroll2);
      		DefaultTableCellRenderer tcr2 = new DefaultTableCellRenderer();// 设置table内容居中
      		tcr2.setHorizontalAlignment(SwingConstants.CENTER);
      		jTable2.setDefaultRenderer(Object.class, tcr2);
      		//以下设置表格样式
      		JTableHeader head2 = jTable2.getTableHeader(); // 创建表格标题对象
              head2.setPreferredSize(new Dimension(head2.getWidth(), 35));// 设置表头大小
              head2.setFont(new Font("楷体", Font.PLAIN, 18));// 设置表格字体
              jTable2.setRowHeight(30);// 设置表格行宽
              jTable2.setFont(new Font("楷体", Font.PLAIN, 18));// 设置表格字体
              jTable2.setSelectionForeground(Color.RED);
              
              MyButtonRender mb=new MyButtonRender();
              jTable1.getColumnModel().getColumn(4).setCellRenderer(mb);//表格里加按钮
              jTable1.getColumnModel().getColumn(4).setCellEditor(new MyButtonEditor(jTable0,jTable1,jTable2,socket0,user_id,password,this,mb));//表格里加按钮可以点击触发事件
              //jTable1.setRowSelectionAllowed(false);// 禁止表格的选择功能。不然在点击按钮时表格的整行都会被选中。也可以通过其它方式来实现。  
              MyButtonRender2 mb2=new MyButtonRender2();
              jTable2.getColumnModel().getColumn(4).setCellRenderer(mb2);//表格里加按钮
              jTable2.getColumnModel().getColumn(4).setCellEditor(new MyButtonEditor2(jTable0,jTable1,jTable2,socket0,user_id,password,this,mb2));//表格里加按钮可以点击触发事件  
              
        
        setResizable(false);//禁止缩放
		setVisible(true);//设置窗体可见
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		windowlistener wl=new windowlistener();//退出窗口时也让注册人数减一，顺便回到上一个窗口
	    this.addWindowListener(wl);//委托监听器
			
	}
	class windowlistener implements WindowListener//窗口监听器
	{
		
	 public void windowActivated(WindowEvent e) {}
	 public void windowClosed(WindowEvent e) { 
	 }
	 public void windowClosing(WindowEvent e) //退出窗口时也让注册人数减一，顺便回到上一个窗口
	 {
		 int result=JOptionPane.showConfirmDialog(null, "确定退出？", "退出", JOptionPane.YES_NO_OPTION);
		 if(result==JOptionPane.YES_OPTION){//确定退出
			try {
				DataOutputStream dos = new DataOutputStream(
	                new BufferedOutputStream(socket0.getOutputStream()));
				dos.writeUTF("24");//发送24代码，让服务器将人数减一
				dos.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dispose();
			Prof_GUI p=new Prof_GUI(user_id,password,socket0);
		 }
	 }
	 public void windowDeactivated(WindowEvent e) {
	 }
	 public void windowDeiconified(WindowEvent e) {}
	 public void windowIconified(WindowEvent e) {}
	 public void windowOpened(WindowEvent e) {}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//点击返回按钮，其他按钮在另一个文件中实现，取消时需要将服务器中的人数减一，给服务器发消息24
		try {
			DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(socket0.getOutputStream()));
			dos.writeUTF("24");//发送24代码，让服务器将人数减一
			dos.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		dispose();
		Prof_GUI p=new Prof_GUI(user_id,password,socket0);
		
	}
	
	
} 