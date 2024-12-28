import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//登录界面
public class Login extends JFrame implements ActionListener {
    private JLabel unLabel = new JLabel("账号名：");
    private JTextField unField = new JTextField();
    private JLabel pwLabel = new JLabel("密码：");
    private JPasswordField pwField = new JPasswordField();
    private JButton dl = new JButton("登录");
    private JButton d2 = new JButton("注册");
    private JPanel jp1 = new JPanel();

    private JRadioButton jr_admin = new JRadioButton("管理员账号");


    public Login() {

        URL backURL = Main.class.getResource("/back.png");
        ImageIcon back = new ImageIcon(backURL);
        JLabel backLabel = new JLabel(back);

        backLabel.setBounds(0, 0, 800, 534);
        jp1.setLayout(null);

        unLabel.setBounds(230,175,70,30);
        unLabel.setForeground(Color.WHITE);
        unLabel.setFont(new Font("黑体", Font.BOLD, 15));
        unField.setBounds(300,170,150,35);
        unField.setFont(new Font("黑体", Font.PLAIN, 15));
        pwLabel.setBounds(240,235,50,30);
        pwLabel.setForeground(Color.WHITE);
        pwLabel.setFont(new Font("黑体", Font.BOLD, 15));
        pwField.setBounds(300,230,150,35);
        pwField.setFont(new Font("微软雅黑", Font.PLAIN, 15));

        jr_admin.setBounds(300,280,91,20);
        jr_admin.setForeground(Color.RED);
        jr_admin.setFocusPainted(false);
        jr_admin.setFont(new Font("微软雅黑", Font.BOLD, 13));

        dl.setBounds(230,310,80,35);
        dl.setFont(new Font("楷体", Font.BOLD, 18));
        //设置点击时不绘制焦点边框
        dl.setFocusPainted(false);

        d2.setBounds(390,310,80,35);
        d2.setFont(new Font("楷体", Font.BOLD, 18));
        d2.setFocusPainted(false);

        dl.addActionListener(this);
        d2.addActionListener(this);


        jp1.add(unLabel);
        jp1.add(unField);
        jp1.add(pwLabel);
        jp1.add(pwField);
        jp1.add(dl);
        jp1.add(d2);
        jp1.add(jr_admin);
        jp1.add(backLabel);
        this.add(jp1);
        this.setTitle("咖啡厅订餐系统");
        this.setSize(700, 550);

        this.setLocationRelativeTo(null);

        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

//        unField.setText("admin");
//        pwField.setText(("admin"));

        ToolFunction.checkDatabaseConnection();
    }

    public void actionPerformed(ActionEvent e) {
        //登录
        if (e.getSource() == dl) {
            if (ToolFunction.checkDatabaseConnection()) {
                String username = unField.getText();
                String password = String.valueOf(pwField.getPassword());
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "账号或密码为空", "登录情况", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int type = checkUsers(username, password);
                //进入管理员界面
                if (type == 1)
                {
                    this.dispose();
                    new AdminManager();
                }
                //进入用户界面
                else if(type == 2)
                {
                    this.dispose();
                    new OrderManager();
                }
                else {
                    JOptionPane.showMessageDialog(null, "账号或密码错误！", "登录情况", JOptionPane.ERROR_MESSAGE);
                }
            }
            //注册
        } else {
            if (ToolFunction.checkDatabaseConnection()) {
                new SignUp(this, "注册界面", true);
            }
        }
    }
    //检查用户名和密码
    public  int checkUsers(String username, String password) {
        int type = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql="";

        try {

            connection = DriverManager.getConnection(ToolFunction.url,ToolFunction.user,ToolFunction.passwd);
            if (jr_admin.isSelected()) {
                sql  = "SELECT * FROM admin WHERE username = ? AND password = ?";
                type = 1;
            }
            else{
                sql  = "SELECT * FROM users WHERE username = ? AND password = ?";
                type = 2;
            }

            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            resultSet = statement.executeQuery();
            //查询到了用户
            if (resultSet.next()) {
                ToolFunction.userid = resultSet.getInt("Id");
            }
            else{
                type=-1;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet !=null)
                    resultSet.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return type;
    }
}
