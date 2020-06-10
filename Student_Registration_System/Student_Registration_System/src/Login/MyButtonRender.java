package Login;
/*
 * 在JTable中，单元格的数据默认是Label的效果，也没有事件。

如果要为单元格添加一个按钮显示效果的话，那么就需要使用到一个swing的接口：javax.swing.table.TableCellRenderer，来改变单元桥格的默认默认渲染方法（DefaultTableCellRenderer），

实现的自定义的渲染器如下：

 */
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MyButtonRender implements TableCellRenderer
{
    private JPanel panel;

    private JButton button;

    public MyButtonRender()
    {
        this.initButton();

        this.initPanel();

        // 添加按钮。
        this.panel.add(this.button);
    }

    private void initButton()
    {
        this.button = new JButton();

        // 设置按钮的大小及位置。
        this.button.setBounds(40,5, 60,20);

        // 在渲染器里边添加按钮的事件是不会触发的
        // this.button.addActionListener(new ActionListener()
        // {
        //
        // public void actionPerformed(ActionEvent e)
        // {
        // // TODO Auto-generated method stub
        // }
        // });

    }

    private void initPanel()
    {
        this.panel = new JPanel();

        // panel使用绝对定位，这样button就不会充满整个单元格。
        this.panel.setLayout(null);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
            int column)
    {
        // 只为按钮赋值即可。也可以作其它操作，如绘背景等。
       // this.button.setText(value == null ? "" : String.valueOf(value));
    	this.button.setText("选课");
        return this.panel;
    }
    public void hideButton()//隐藏按钮
    {
    	button.setVisible(false);
    }
}