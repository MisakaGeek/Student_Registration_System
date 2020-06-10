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
自定义一个往列里边添加按钮的单元格编辑器。最好继承DefaultCellEditor，不然要实现的方法就太多了。
 */ 
public class MyButtonEditor2 extends DefaultCellEditor 
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
    private ShowCourseGUI showGUI;//保存按动按钮的界面，用于选课成功的时候让这个界面消失
    private MyButtonRender2 mb;//用于让按钮隐藏
    public MyButtonEditor2(JTable table0,JTable table1,JTable table2,Socket socket0,String pid0,String password0,ShowCourseGUI showGUI0,MyButtonRender2 mb0) 
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
            	MyButtonEditor2.this.fireEditingCanceled(); 
            	int row=table2.getSelectedRow();//判断按下的按钮是第几行的
            	DefaultTableModel model=(DefaultTableModel) table2.getModel();//获取defaulttablemodel
				Vector<String> rowData=new <String>Vector();
				for(int i=0;i<table1.getColumnCount();i++)//得到有多少列，将这一行的数据加入rowData中
					rowData.add((String) table2.getValueAt(row, i));
				model.removeRow(row);//删除选中的行，然后加入下一个表格里
	
				DefaultTableModel amodel = new DefaultTableModel(); 
				amodel=(DefaultTableModel) table1.getModel();
				amodel.addRow(rowData);
				try {
					DataInputStream dis = new DataInputStream(
					        new BufferedInputStream(socket.getInputStream()));
					DataOutputStream dos = new DataOutputStream(
	                new BufferedOutputStream(socket.getOutputStream()));
					dos.writeUTF("2c");//给服务器发送2c代码,表示要退课，数据库相应位置置null
					dos.flush();	
					dos.writeUTF(rowData.get(0));//发送要更改的数据的cid
					dos.flush();	
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				JOptionPane.showMessageDialog(null, "退课成功！", "成功",JOptionPane.INFORMATION_MESSAGE);
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
        this.button.setText("退课");
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