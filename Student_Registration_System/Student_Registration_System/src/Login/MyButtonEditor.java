package Login;
/*通过第一部已经为表格添加了默认的渲染器，但是还无法触发事件，触发事件是没有反应的，因为在点击表格时，
 * 会触发表格的编辑事件，而要想触发渲染的按钮的事件，还需要通过修改表格的默认编辑器来实现：
 */
import java.awt.Component; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import javax.swing.DefaultCellEditor; 
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel; 
import javax.swing.JTable; 
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel; 
 
/*
自定义一个往列里边添加按钮的单元格编辑器。继承DefaultCellEditor
 */ 
public class MyButtonEditor extends DefaultCellEditor 
{ 
 
 
    //private static final long serialVersionUID = -6546334664166791132L; 
 
    private JPanel panel; 
 
    private JButton button; 
    private JTable table0; //第一个表格传进来用于下个界面
    private JTable table1;//第二个表格传进来用于定位按钮
    private JTable table2;//第三个表格传进来用于添加数据
    private Socket socket;
    private String pid;//触发按钮的教师id
    private String password;//触发按钮的教师密码
    private ShowCourseGUI showGUI;//保存按动按钮的界面，用于有冲突的时候仍显示但不可操作
    private MyButtonRender mb;//用于让按钮隐藏
    public MyButtonEditor(JTable table0,JTable table1,JTable table2,Socket socket0,String pid0,String password0,ShowCourseGUI showGUI0,MyButtonRender mb0) 
    { 
    	//DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。  
    	super(new JTextField()); 
        this.table0=table0;
        this.table1=table1;
        this.table2=table2;
        //this.qualifiedtable=qualifiedtable0;
        this.socket=socket0;
        this.pid=pid0;
        this.password=password0;
        this.showGUI=showGUI;
        this.mb=mb0;
        // 设置点击几次激活编辑。  
        this.setClickCountToStart(1); 
 
        this.initButton(); 
 
        this.initPanel(); 
 
        // 添加按钮。  
        this.panel.add(this.button); 
       
    } 
 
    private void initButton() 
    { 
        this.button = new JButton(); 
 
        // 设置按钮的大小及位置。  
        this.button.setBounds(40, 5, 60, 20); 
 
        // 为按钮添加事件。这里只能添加ActionListner事件，Mouse事件无效。  
        this.button.addActionListener(new ActionListener() 
        { 
            public void actionPerformed(ActionEvent e) //表格按钮按下后用于判断冲突，创建选课成功界面和冲突显示
            {
            	   // 触发取消编辑的事件，不会调用tableModel的setValue方法。  
            	MyButtonEditor.this.fireEditingCanceled(); 

            	int row=table1.getSelectedRow();//判断第几行，从0开始
                System.out.println("按下按钮:"+row);
               
                
            	try {
					DataInputStream dis = new DataInputStream(
					        new BufferedInputStream(socket.getInputStream()));
					DataOutputStream dos = new DataOutputStream(
    	                new BufferedOutputStream(socket.getOutputStream()));	
					//JOptionPane.showMessageDialog(null,s,"错误",JOptionPane.ERROR_MESSAGE);
					dos.writeUTF("2a");//给服务器发送2a代码，表示要查询冲突
					dos.flush();
					String timeslot=(String) table1.getValueAt(row, 3);
					dos.writeUTF(timeslot);//给服务器发送要检索冲突的课程时间
					dos.flush();
					
					if(dis.readUTF().equals("有冲突"))//如果有冲突
					{
						int i=dis.readInt();//有几行数据传过来，包括属性名
						System.out.println("MyButtonEditor判断有"+i+"行冲突");
						
						String conflicttable[]=new String[i*4];//保存传过来的所有行的数据，包括属性名
						
						for(int j=0;j<i*4;j++)//循环结束，conflicttable里面就是所有冲突课程
						{
							conflicttable[j]=dis.readUTF();
						}
						for(int j=0;j<i*4;j++)
							{
							    System.out.print(conflicttable[j]+" ");
								if((j+1)%4==0)
										
								System.out.println("\n");
							}
						JOptionPane.showMessageDialog(null, "您当前所选课程和已经选择的课程有冲突，选课失败", "选课失败",JOptionPane.WARNING_MESSAGE);
						
						ConflictCourseGUI ccg=new ConflictCourseGUI(table1,table2,socket,conflicttable,showGUI);
						//showGUI.setFocusable(false);//调用新窗口后旧窗口不允许操作
						//新建一个窗口或者什么
					}
					else//如果没有冲突则选课成功换按钮，有个问题，如果关闭注册的时候发现课程取消了，是否应该通知教授？
					{ 
						System.out.println("没有冲突");
						//mb.hideButton();
						//button.setVisible(false);
						
						DefaultTableModel model=(DefaultTableModel) table1.getModel();//获取defaulttablemodel
						Vector<String> rowData=new <String>Vector();
						for(int i=0;i<table1.getColumnCount();i++)//得到有多少列，将这一行的数据加入rowData中
							rowData.add((String) table1.getValueAt(row, i));
						System.out.println("选课成功，要删除的数据是："+rowData);
						
						dos.writeUTF("28");//给服务器发送28代码,表示要选课，数据库pid位置置为教授id
						dos.flush();	
						dos.writeUTF(rowData.get(0));//发送要更改的数据的cid
						dos.flush();
						dos.writeUTF(pid);//发送要更改的数据的pid
						dos.flush();
						
						model.removeRow(row);//删除选中的行，然后加入下一个表格里
			
						DefaultTableModel amodel = new DefaultTableModel(); 
						amodel=(DefaultTableModel) table2.getModel();
						amodel.addRow(rowData);
						JOptionPane.showMessageDialog(null, "选课成功", "操作成功",JOptionPane.INFORMATION_MESSAGE);
						
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    				
    				
    			
                
            } 
        }); 
        
 
    } 
 
    private void initPanel() 
    { 
        this.panel = new JPanel(); 
 
        // panel使用绝对定位，这样button就不会充满整个单元格。  
        this.panel.setLayout(null); 
    } 
 
 
    /**
     * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格）
     */ 
    @Override 
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
    { 
        this.button.setText("选课");
        return this.panel; 
    } 
 
    /**
     * 重写编辑单元格时获取的值。如果不重写，这里可能会为按钮设置错误的值。
     */  
    @Override 
    public Object getCellEditorValue() 
    { 
        return this.button.getText(); 
    } 
 


/*修改表格的默认编辑器：

this.table.getColumnModel().getColumn(2).setCellEditor(new MyButtonEditor());

 

这样后就能基本达到效果了。但是还要注意，对列2来说，还需要启用可编辑功能，才行，不然仍然触发不了事件的。

*/

public boolean isCellEditable(int row, int column) 
{ 
	// 带有按钮列的功能这里必须要返回true不然按钮点击时不会触发编辑效果，也就不会触发事件。  
	if (column == 4) 
    { 
        return true; 
    } 
    else 
    { 
        return false; 
    } 
} 
}