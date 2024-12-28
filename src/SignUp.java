import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

//注册界面
public class SignUp extends JDialog implements ActionListener {
    private JLabel unLabel = new JLabel("账号：");
    private JTextField unField = new JTextField();
    private JLabel pwLabel = new JLabel("密码：");
    private JPasswordField pwField = new JPasswordField();
    private JLabel pw2Label = new JLabel("确认密码：");
    private JPasswordField pw2Field = new JPasswordField();
    private JButton d2 = new JButton("注册");
    private JPanel contentPanel = new JPanel();

    public SignUp(Frame owner, String title, boolean modal) {
        super(owner, title, modal);

        contentPanel.setLayout(null);
        unLabel.setBounds(80, 50, 70, 30);
        unLabel.setFont(new Font("楷体", Font.BOLD, 15));
        unLabel.setForeground(Color.red);

        unField.setBounds(140, 50, 150, 30);

        pwLabel.setBounds(80, 100, 70, 30);
        pwLabel.setFont(new Font("楷体", Font.BOLD, 15));
        pwLabel.setForeground(Color.red);

        pwField.setBounds(140, 100, 150, 30);

        pw2Label.setBounds(60, 150, 120, 30);
        pw2Label.setFont(new Font("楷体", Font.BOLD, 15));
        pw2Label.setForeground(Color.red);

        pw2Field.setBounds(140, 150, 150, 30);

        d2.setFocusPainted(false);
        d2.setBounds(160, 200, 80, 30);
        d2.setFont(new Font("楷体", Font.BOLD, 16));
        d2.addActionListener(this);

        contentPanel.add(unLabel);
        contentPanel.add(unField);
        contentPanel.add(pwLabel);
        contentPanel.add(pwField);
        contentPanel.add(pw2Label);
        contentPanel.add(pw2Field);
        contentPanel.add(d2);
        this.setSize(400, 300);
        this.setLocation(550, 270);
        this.add(contentPanel);
        this.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        if (ToolFunction.checkDatabaseConnection()) {
            try {
                String username = unField.getText();
                String password = String.valueOf(pwField.getPassword());
                String password2 =  String.valueOf(pw2Field.getPassword());
                if (username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "账号或密码为空", "注册情况", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (!password.equals(password2)) {
                    JOptionPane.showMessageDialog(null, "两次输入的密码不一致", "注册情况", JOptionPane.ERROR_MESSAGE);
                    pw2Field.setText("");
                    return;
                }
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);

                String sql1 = "SELECT * from users";
                statement = connection.prepareStatement(sql1);
                resultSet = statement.executeQuery();
                //获取当前数据库中的用户总数
                int rowCount = 0;
                while (resultSet.next()) {
                    rowCount++;
                }

                //账户名是否已经存在
                String sql2 = "SELECT * from users WHERE username = ?";
                statement = connection.prepareStatement(sql2);
                statement.setString(1, username);
                resultSet = statement.executeQuery();
                //该账户名不存在
                if (!resultSet.next())
                {
                    //插入用户数据
                    String sql3 = "INSERT INTO users (username, password,id) VALUES (?, ?, ?)";
                    statement = connection.prepareStatement(sql3);
                    statement.setString(1, username);
                    statement.setString(2, password);
                    statement.setInt(3,rowCount+1);
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "注册成功", "注册情况", JOptionPane.PLAIN_MESSAGE);
                    this.dispose();
                }
                else
                    JOptionPane.showMessageDialog(null, "该账号已经存在", "注册情况", JOptionPane.ERROR_MESSAGE);

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
                } catch (Exception ef) {
                    ef.printStackTrace();
                }
            }
        }
    }
}
