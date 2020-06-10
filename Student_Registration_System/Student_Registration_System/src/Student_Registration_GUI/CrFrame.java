package Student_Registration_GUI;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;


class CrFrame extends JFrame{
	private CourseRegistration cr;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem item1, item2, item3,item4;
	private JPanel panel1, panel2, panel3; //构造CrFrame时设为null
	private JButton but1, but2, but3, but4, but5, but6, butDeleteOffering, butSelectOfferingAsMain, butSelectOfferingAsAlt, butSave, butSubmit;
	private JLabel label1, label2, label3, label4, label5, label6; //标志各个课表项(but1,but2等)的状态(无,selected, erolled_in)
	private JComboBox<String> comboBox;
	
	private String selectedButTitle;
	
	private class createScheduleListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			cr.createSchedule();
		}
	}
	private class updateScheduleListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			cr.updateSchedule();
		}
	}
	private class deleteScheduleListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			cr.deleteSchedule();
		}
	}
	
	
	/*
	 * 创建窗体，带有一个菜单，这个菜单下面有两个菜单项（创建课表，更新课表），并未每个菜单项添加监听器
	 * 窗体的大小和位置
	 * 窗体的名字
	 */
	private void initFrame() {
		setTitle(cr.getStudentId());
		setBounds(600, 150, 500, 600);
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menu = new JMenu("课表");
        menuBar.add(menu);
        item1 = new JMenuItem("创造课表");
        item1.addActionListener(new createScheduleListener());
        item2 = new JMenuItem("更新课表");
        item2.addActionListener(new updateScheduleListener());
        item3 = new JMenuItem("删除课表");
        item3.addActionListener(new deleteScheduleListener());
        item4 = new JMenuItem("返回");
        item4.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		cr.retToUpLevel();
        	}
        });
        menu.add(item1);
        menu.add(item2);
        menu.add(item3);
        menu.add(item4);
	}
	
	void showMessageDialog(String text) {
		JOptionPane.showMessageDialog(this,text, "消息", JOptionPane.PLAIN_MESSAGE);
	}
	
	boolean showConfirmDialog(String text, String title) {
		int res = JOptionPane.showConfirmDialog(this, text, title, JOptionPane.YES_NO_OPTION);
		if(res == JOptionPane.YES_OPTION) {
			return true;
		}else {
			return false;
		}
	}
	
	/*
	 * 在GUI上设置课程的增加和删除，同时设置课程的状态：已保存，已提交
	 */
	void setCourseButText(int index, String text) {
		if(index == 1) {
			but1.setText(text);
		}else if(index == 2) {
			but2.setText(text);
		}else if(index == 3) {
			but3.setText(text);
		}else if(index == 4) {
			but4.setText(text);
		}else if(index == 5) {
			but5.setText(text);
		}else if(index == 6) {
			but6.setText(text);
		}
		setVisible(true);
	}
	void setCourseButState(int index, String state) {
		if(index == 1) {
			label1.setText(state);
		}else if(index == 2) {
			label2.setText(state);
		}else if(index == 3) {
			label3.setText(state);
		}else if(index == 4) {
			label4.setText(state);
		}else if(index == 5) {
			label5.setText(state);
		}else if(index == 6) {
			label6.setText(state);
		}
		setVisible(true);
	}
	void setAllSelectedCourseState(String state) {
		if(!but1.getText().equals("")) {
			label1.setText(state);
		}
		if(!but2.getText().equals("")) {
			label2.setText(state);
		}
		if(!but3.getText().equals("")) {
			label3.setText(state);
		}
		if(!but4.getText().equals("")) {
			label4.setText(state);
		}
		if(!but5.getText().equals("")) {
			label5.setText(state);
		}
		if(!but6.getText().equals("")) {
			label6.setText(state);
		}
		setVisible(true);
	}
	
	/*
	 * 当点击“创建课表”菜单项时执行这个布局。
	 */
	void createScheduleLayout(ArrayList<String> cofs) {
		if(panel1 != null) {
			remove(panel1);
			remove(panel2);
			remove(panel3);
		}
		setLayout(new GridLayout(3,1));
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(9,2));
		panel1.add(new JLabel("主课程"));
		panel1.add(new JLabel());
		panel1.add(but1 = new JButton());
		but1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButTitle = but1.getText();
			}
		});
		label1 = new JLabel();
		panel1.add(label1);
		panel1.add(but2 = new JButton());
		but2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButTitle = but2.getText();
			}
		});
		label2 = new JLabel();
		panel1.add(label2);
		panel1.add(but3 = new JButton());
		but3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButTitle = but3.getText();
			}
		});
		label3 = new JLabel();
		panel1.add(label3);
		panel1.add(but4 = new JButton());
		but4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButTitle = but4.getText();
			}
		});
		label4 = new JLabel();
		panel1.add(label4);
		panel1.add(new JLabel("备选课程"));
		panel1.add(new JLabel());
		panel1.add(but5 = new JButton());
		but5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButTitle = but5.getText();
			}
		});
		label5 = new JLabel();
		panel1.add(label5);
		panel1.add(but6 = new JButton());
		but6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButTitle = but6.getText();
			}
		});
		label6 = new JLabel();
		panel1.add(label6);
		panel1.add(butDeleteOffering = new JButton());
		butDeleteOffering.setText("删除课程");
		butDeleteOffering.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedButTitle == "") {
					showMessageDialog("未选择任何课程,不能删除");
				}else {
					if(selectedButTitle == but1.getText()) {
						but1.setText("");
						label1.setText("");
						cr.setStudentMainLesson(0, "");
					}else if(selectedButTitle == but2.getText()) {
						but2.setText("");
						label2.setText("");
						cr.setStudentMainLesson(1, "");
					}else if(selectedButTitle == but3.getText()) {
						but3.setText("");
						label3.setText("");
						cr.setStudentMainLesson(2, "");
					}else if(selectedButTitle == but4.getText()) {
						but4.setText("");
						label4.setText("");
						cr.setStudentMainLesson(3, "");
					}else if(selectedButTitle == but5.getText()) {
						but5.setText("");
						label5.setText("");
						cr.setStudentAltLesson(0, "");
					}else if(selectedButTitle == but6.getText()) {
						but6.setText("");
						label6.setText("");
						cr.setStudentAltLesson(1, "");
					}
					setVisible(true);
					showMessageDialog("删除成功");
				}
			}
		});
		
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(3,1));
		panel2.add(comboBox = new JComboBox<String>());
		for(int i=0;i<cofs.size();i++) {
			comboBox.addItem(cofs.get(i));
		}
		comboBox.setBorder(BorderFactory.createTitledBorder("请选择您的课程"));
		comboBox.setSize(300,50);
		panel2.add(butSelectOfferingAsMain = new JButton("选择作为主课程"));
		butSelectOfferingAsMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = (String) comboBox.getSelectedItem();
				String courseName = "";
				courseName = item.split(" ")[0];//从item中提取出课程名字
				cr.selectCourseOffering(courseName, false);
			}
		});
		panel2.add(butSelectOfferingAsAlt = new JButton("选择作为备选课程"));
		butSelectOfferingAsAlt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = (String) comboBox.getSelectedItem();
				String courseName = "";
				courseName = item.split(" ")[0];//从item中提取出课程名字
				cr.selectCourseOffering(courseName, true);
			}
		});
		
		
		panel3 = new JPanel();
		panel3.setLayout(new GridLayout(1,2));
		panel3.add(butSave = new JButton());
		butSave.setText("保存课表");
		butSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cr.saveSchedule();
			}
		});
		panel3.add(butSubmit = new JButton());
		butSubmit.setText("提交课表");
		butSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cr.submitSchedule()) {
					butSave.setEnabled(false);
				}
			}
		});
		
		add(panel1);
		add(panel2);
		add(panel3);
		
		
		setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/*
	 * 当点击“更新课表”菜单项时执行这个布局。
	 * 未完成
	 */
	void updateScheduleLayout(ArrayList<String> main_lesson, ArrayList<String> alt_lesson, ArrayList<String> cofs, String state) {
		if(panel1 != null) {
			remove(panel1);
			remove(panel2);
			remove(panel3);
		}
		setLayout(new GridLayout(3,1));
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(9,2));
		panel1.add(new JLabel("主课程"));
		panel1.add(new JLabel());
		panel1.add(but1 = new JButton());
		but1.setText(main_lesson.get(0).split(" ")[0]);
		but1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButTitle = but1.getText();
			}
		});
		label1 = new JLabel();
		if(!but1.getText().equals("")) label1.setText(state);
		panel1.add(label1);
		panel1.add(but2 = new JButton());
		but2.setText(main_lesson.get(1).split(" ")[0]);
		but2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButTitle = but2.getText();
			}
		});
		label2 = new JLabel();
		if(!but2.getText().equals("")) label2.setText(state);
		panel1.add(label2);
		panel1.add(but3 = new JButton());
		but3.setText(main_lesson.get(2).split(" ")[0]);
		but3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButTitle = but3.getText();
			}
		});
		label3 = new JLabel();
		if(!but3.getText().equals("")) label3.setText(state);
		panel1.add(label3);
		panel1.add(but4 = new JButton());
		but4.setText(main_lesson.get(3).split(" ")[0]);
		but4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButTitle = but4.getText();
			}
		});
		label4 = new JLabel();
		if(!but4.getText().equals("")) label4.setText(state);
		panel1.add(label4);
		panel1.add(new JLabel("备选课程"));
		panel1.add(new JLabel());
		panel1.add(but5 = new JButton());
		but5.setText(alt_lesson.get(0).split(" ")[0]);
		but5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButTitle = but5.getText();
			}
		});
		label5 = new JLabel();
		if(!but5.getText().equals("")) label5.setText(state);
		panel1.add(label5);
		panel1.add(but6 = new JButton());
		but6.setText(alt_lesson.get(1).split(" ")[0]);
		but6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButTitle = but6.getText();
			}
		});
		label6 = new JLabel();
		if(!but6.getText().equals("")) label6.setText(state);
		panel1.add(label6);
		panel1.add(butDeleteOffering = new JButton());
		butDeleteOffering.setText("删除课程");
		butDeleteOffering.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedButTitle == "") {
					showMessageDialog("未选择任何课程,不能删除");
				}else {
					if(selectedButTitle == but1.getText()) {
						but1.setText("");
						label1.setText("");
						cr.setStudentMainLesson(0, "");
					}else if(selectedButTitle == but2.getText()) {
						but2.setText("");
						label2.setText("");
						cr.setStudentMainLesson(1, "");
					}else if(selectedButTitle == but3.getText()) {
						but3.setText("");
						label3.setText("");
						cr.setStudentMainLesson(2, "");
					}else if(selectedButTitle == but4.getText()) {
						but4.setText("");
						label4.setText("");
						cr.setStudentMainLesson(3, "");
					}else if(selectedButTitle == but5.getText()) {
						but5.setText("");
						label5.setText("");
						cr.setStudentAltLesson(0, "");
					}else if(selectedButTitle == but6.getText()) {
						but6.setText("");
						label6.setText("");
						cr.setStudentAltLesson(1, "");
					}
					setVisible(true);
					showMessageDialog("删除课程成功");
				}
				selectedButTitle = "";
			}
		});
		
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(3,1));
		panel2.add(comboBox = new JComboBox<String>());
		for(int i=0;i<cofs.size();i++) {
			comboBox.addItem(cofs.get(i));
		}
		comboBox.setBorder(BorderFactory.createTitledBorder("请选择您的课程"));
		comboBox.setSize(300,50);
		panel2.add(butSelectOfferingAsMain = new JButton("选择作为主课程"));
		butSelectOfferingAsMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = (String) comboBox.getSelectedItem();
				String courseName = "";
				courseName = item.split(" ")[0];//从item中提取出课程名字
				cr.selectCourseOffering(courseName, false);
			}
		});
		panel2.add(butSelectOfferingAsAlt = new JButton("选择作为备选课程"));
		butSelectOfferingAsAlt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = (String) comboBox.getSelectedItem();
				String courseName = "";
				courseName = item.split(" ")[0];//从item中提取出课程名字
				cr.selectCourseOffering(courseName, true);
			}
		});
		
		
		panel3 = new JPanel();
		panel3.setLayout(new GridLayout(1,2));
		if(!state.equals("enrolled_in")) { //如果课表是已提交状态，则不能保存，只能提交
			panel3.add(butSave = new JButton());
			butSave.setText("保存课表");
			butSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cr.saveSchedule();
				}
			});
		}
		panel3.add(butSubmit = new JButton());
		butSubmit.setText("提交课表");
		butSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cr.submitSchedule();
			}
		});
		
		add(panel1);
		add(panel2);
		add(panel3);
		
		
		setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/*
	 * 当点击“删除课表”菜单项时执行这个布局
	 */
	void deleteScheduleLayout(ArrayList<String> main_lesson, ArrayList<String> alt_lesson) {
		if(panel1 != null) {
			remove(panel1);
			remove(panel2);
			remove(panel3);
		}
		
		setLayout(new GridLayout(3,1));
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(5,1));
		panel1.add(new JLabel("主课程"));
		panel1.add(new JLabel(main_lesson.get(0)));
		panel1.add(new JLabel(main_lesson.get(1)));
		panel1.add(new JLabel(main_lesson.get(2)));
		panel1.add(new JLabel(main_lesson.get(3)));
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(3,1));
		panel2.add(new JLabel("备选课程"));
		panel2.add(new JLabel(alt_lesson.get(0)));
		panel2.add(new JLabel(alt_lesson.get(1)));
		panel3 = new JPanel();
		panel3.setLayout(new FlowLayout());
		JButton butDelSchedule = new JButton();
		panel3.add(butDelSchedule);
		butDelSchedule.setText("删除课表");
		butDelSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				cr.submitDeleteSchedule();
				butDelSchedule.setEnabled(false);
			}
		});
		
		add(panel1);
		add(panel2);
		add(panel3);
		
		setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	CrFrame(CourseRegistration cr){
		this.cr = cr;
		panel1 = panel2 = panel3 = null;
		initFrame();
		selectedButTitle = "";
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}