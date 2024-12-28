import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//用来实现增添学生功能
public class StuAddDiag extends JDialog implements ActionListener {
    //定义我需要的swing组件
    JLabel jl1,jl2,jl3,jl4,jl5,jl6,jl7;
    JTextField jf1,jf2,jf3,jf4,jf5,jf6,jf7;
    JPanel jp1,jp2,jp3;
    JButton jb1,jb2;
    //owner代笔父窗口,title是窗口的名字,modal指定是模式窗口()或者非模式窗口
    public StuAddDiag(Frame owner, String title, boolean modal){
        //调用父类方法
        super(owner,title,modal);

        jl1 = new JLabel("学号");
        jl2 = new JLabel("名字");
        jl3 = new JLabel("性别");
        jl4 = new JLabel("年龄");
        jl5 = new JLabel("绩点");
        jl6 = new JLabel("联系方式");
        jl7 = new JLabel("班级");

        jf1 = new JTextField(30);
        jf2 = new JTextField(30);
        jf3 = new JTextField(30);
        jf4 = new JTextField(30);
        jf5 = new JTextField(30);
        jf6 = new JTextField(30);
        jf7 = new JTextField(30);

        jb1 = new JButton("添加");
        jb1.addActionListener(this);
        jb2 = new JButton("取消");
        jb2.addActionListener(this);

        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();

        //设置布局
        jp1.setLayout(new GridLayout(7,1));
        jp2.setLayout(new GridLayout(7,1));

        jp3.add(jb1);
        jp3.add(jb2);

        jp1.add(jl1);
        jp1.add(jl2);
        jp1.add(jl3);
        jp1.add(jl4);
        jp1.add(jl5);
        jp1.add(jl6);
        jp1.add(jl7);

        jp2.add(jf1);
        jp2.add(jf2);
        jp2.add(jf3);
        jp2.add(jf4);
        jp2.add(jf5);
        jp2.add(jf6);
        jp2.add(jf7);

        this.add(jp1, BorderLayout.WEST);
        this.add(jp2, BorderLayout.CENTER);
        this.add(jp3, BorderLayout.SOUTH);
        this.setLocation(600, 350);
        this.setSize(300,200);
        this.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jb1){
            Connection ct = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            if (jf1.getText().isEmpty() || jf2.getText().isEmpty() || jf3.getText().isEmpty() ||
                    jf4.getText().isEmpty() || jf5.getText().isEmpty() || jf6.getText().isEmpty() ||
                    jf7.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "值不能为空", "添加情况", JOptionPane.ERROR_MESSAGE);
                return;
                }
                try{
                //1.加载驱动
                Class.forName("com.mysql.cj.jdbc.Driver");
                //定义相关常量，连接数据库
                String url = DatabaseUtils.url;
                String user = DatabaseUtils.user;
                String passwd = DatabaseUtils.passwd;
                ct = DriverManager.getConnection(url,user,passwd);

                //与编译语句对象
                String strsql = "insert into stu values(?,?,?,?,?,?,?)";
                pstmt = ct.prepareStatement(strsql);

                //给对象赋值
                    pstmt.setString(1, jf1.getText());
                    pstmt.setString(2, jf2.getText());
                    pstmt.setString(3, jf3.getText());
                    pstmt.setString(4, jf4.getText());
                    pstmt.setString(5, jf5.getText());
                    pstmt.setString(6, jf6.getText());
                    pstmt.setString(7, jf7.getText());
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "添加成功", "添加情况", JOptionPane.PLAIN_MESSAGE);
                this.dispose();//关闭学生对话框

            }catch(Exception arg1){
                    JOptionPane.showMessageDialog(null, "该学号的学生已经存在", "添加情况", JOptionPane.ERROR_MESSAGE);
            }finally{
                try{
                    if(rs!=null){
                        rs.close();
                    }
                    if(pstmt != null){
                        pstmt.close();
                    }
                    if(ct != null){
                        ct.close();
                    }
                }catch(Exception arg2){
                    arg2.printStackTrace();
                }
            }

        }else{
            this.dispose();
        }

    }
}