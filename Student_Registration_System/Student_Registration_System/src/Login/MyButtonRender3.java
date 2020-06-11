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

public class MyButtonRender3 implements TableCellRenderer
{
    private JPanel panel;

    private JButton button;

    public MyButtonRender3()
    {
        this.initButton();

        this.initPanel();

        this.panel.add(this.button);
    }

    private void initButton()
    {
        this.button = new JButton();

        this.button.setBounds(40,5, 60,20);

    }

    private void initPanel()
    {
        this.panel = new JPanel();

        this.panel.setLayout(null);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
            int column)
    {
    	this.button.setText("退课");
        return this.panel;
    }
    public void hideButton()//隐藏按钮
    {
    	button.setVisible(false);
    }
}