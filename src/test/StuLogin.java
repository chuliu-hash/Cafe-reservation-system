import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.net.URL;

public class StuLogin extends JFrame implements ActionListener {
    private StuLogin self = this;
    private JLabel unLabel = new JLabel("账号：");
    private JTextField unField = new JTextField();
    private JLabel pwLabel = new JLabel("密码：");
    private JPasswordField pwField = new JPasswordField();
    private JButton dl = new JButton("登录");
    private JButton d2 = new JButton("注册");
    private  JPanel jp1 = new JPanel();
    public StuLogin() {

        // 获取背景图片资源路径
        URL backURL = main.class.getResource("/resources/back.jpg");
        URL iconURL = main.class.getResource("/resources/icon.jpg");
       // 创建图标组件
        ImageIcon back = new ImageIcon(backURL);
        ImageIcon icon = new ImageIcon(iconURL);

        Image iconImage = icon.getImage();
        this.setIconImage(iconImage);//设置窗口图像

        JLabel backLabel = new JLabel(back);
        backLabel.setBounds(0, 0, 700, 157); //背景图片在窗口的位置及大小
        jp1.add(backLabel);
        jp1.setLayout(null);

        unLabel.setSize(50, 30);
        unLabel.setLocation(240, 175);
        unLabel.setForeground(Color.red);
        unLabel.setFont(new Font("楷体", Font.BOLD, 15));
        unField.setSize(150, 35);
        unField.setLocation(300, 170);
        pwLabel.setSize(50, 30);
        pwLabel.setLocation(240, 235);
        pwLabel.setForeground(Color.red);
        pwLabel.setFont(new Font("楷体", Font.BOLD, 15));
        pwField.setSize(150, 35);
        pwField.setLocation(300, 230);
        dl.setSize(80, 35);
        dl.setFont(new Font("楷体", Font.BOLD, 15));
        dl.setLocation(230, 300);
        d2.setSize(80, 35);
        d2.setFont(new Font("楷体", Font.BOLD, 15));
        d2.setLocation(390, 300);

        dl.addActionListener(this);
        d2.addActionListener(this);


        jp1.add(unLabel);
        jp1.add(unField);
        jp1.add(pwLabel);
        jp1.add(pwField);
        jp1.add(dl);
        jp1.add(d2);
        this.add(jp1);
        this.setTitle("学生信息管理系统");
        this.setSize(700, 506);// 设置登陆面板大小
        this.setLocation(400, 150); //窗口在显示屏的位置
        this.setVisible(true);  //窗口可见
        this.setResizable(false); //窗口位置不可调节
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        unField.setText("admin");
        pwField.setText(("admin"));

        DatabaseUtils.checkDatabaseConnection();
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==dl) {
            if (DatabaseUtils.checkDatabaseConnection()) {
                String userid = unField.getText();
                String password = pwField.getText();
                if (DatabaseUtils.checkUsers(userid, password)) {
                    self.dispose();
                    new StuManager();
                } else {
                    JOptionPane.showMessageDialog(null, "账号或密码错误！", "登录情况", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
        else{
            new SignUp(self,"注册界面",true);
        }
    }
}
