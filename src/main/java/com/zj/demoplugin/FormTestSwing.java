package com.zj.demoplugin;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author arthur_zhou
 */
public class FormTestSwing {

    private final JPanel north = new JPanel();

    private final JPanel center = new JPanel();

    private final JPanel south = new JPanel();

    /**
     * 为了让位于底部的按钮可以拿到组件内容，这里把表单组件做成类属性
     */
    private final JLabel r1 = new JLabel("输出：");
    private final JLabel r2 = new JLabel("NULL");

    private final JLabel name = new JLabel("姓名：");
    private final JTextField nameContent = new JTextField();

    private final JLabel age = new JLabel("年龄：");
    private final JTextField ageContent = new JTextField();

    public JPanel initNorth() {

        // 定义表单的标题部分，放置到IDEA会话框的顶部位置

        JLabel title = new JLabel("表单标题");
        // 字体样式
        title.setFont(new Font("微软雅黑", Font.PLAIN, 26));
        // 水平居中
        title.setHorizontalAlignment(SwingConstants.CENTER);
        // 垂直居中
        title.setVerticalAlignment(SwingConstants.CENTER);
        north.add(title);

        return north;
    }

    public JPanel initCenter() {

        // 定义表单的主体部分，放置到IDEA会话框的中央位置

        // 一个简单的3行2列的表格布局
        center.setLayout(new GridLayout(3, 2));

        // row1：按钮事件触发后将结果打印在这里
        // 设置字体颜色
        r1.setForeground(new Color(255, 47, 93));
        center.add(r1);
        // 设置字体颜色
        r2.setForeground(new Color(139, 181, 20));
        center.add(r2);

        // row2：姓名+文本框
        center.add(name);
        center.add(nameContent);

        // row3：年龄+文本框
        center.add(age);
        center.add(ageContent);

        return center;
    }

    public JPanel initSouth() {
        //定义表单的提交按钮，放置到IDEA会话框的底部位置
        JButton submit = new JButton("提交");
        // 水平居中
        submit.setHorizontalAlignment(SwingConstants.CENTER);
        // 垂直居中
        submit.setVerticalAlignment(SwingConstants.CENTER);
        south.add(submit);

        //按钮事件绑定
        submit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //获取到name和age
                String name = nameContent.getText();
                String age = ageContent.getText();
                //刷新r2标签里的内容，替换为name和age
                r2.setText(String.format("name:%s, age:%s", name, age));
            }
        });
        return south;
    }
}

