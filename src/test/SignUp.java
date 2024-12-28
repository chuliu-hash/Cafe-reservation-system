import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Objects;

public class SignUp extends JDialog implements ActionListener{
    private JLabel unLabel = new JLabel("账号：");
    private JTextField unField = new JTextField();
    private JLabel pwLabel = new JLabel("密码：");
    private JPasswordField pwField = new JPasswordField();
    private JLabel pw2Label = new JLabel("确认密码：");
    private JPasswordField pw2Field = new JPasswordField();
    private JButton d2 = new JButton("注册");
    JPanel contentPanel = new JPanel();
    public SignUp(Frame owner, String title, boolean modal)
    {
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

        d2.setBounds(160, 200, 100, 30);
        d2.setFont(new Font("楷体", Font.BOLD, 15));
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
                String url = DatabaseUtils.url;
                String user = DatabaseUtils.user;
                String passwd = DatabaseUtils.passwd;
                if (DatabaseUtils.checkDatabaseConnection()) {
                    try {
                        String username = unField.getText();
                        String password = pwField.getText();
                        String password2 = pw2Field.getText();
                        if (username.isEmpty()|| password.isEmpty() || password2.isEmpty()) {
                            JOptionPane.showMessageDialog(null,"账号或密码为空","注册情况",JOptionPane.PLAIN_MESSAGE);
                            return;
                        }
                        else if (!Objects.equals(password, password2)) {
                            JOptionPane.showMessageDialog(null,"两次输入的密码不一样","注册情况",JOptionPane.PLAIN_MESSAGE);
                            pw2Field.setText("");
                            return;
                        }
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection(url, user, passwd);
                        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, username);
                        statement.setString(2, password);
                        statement.executeUpdate();
                        JOptionPane.showMessageDialog(null,"注册成功","注册情况",JOptionPane.PLAIN_MESSAGE);
                        this.dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,"该账号已经存在","注册情况",JOptionPane.ERROR_MESSAGE);
                    }finally {
                        try {
                            if (connection != null)
                                connection.close();
                            if (statement != null)
                                statement.close();
                        }catch(Exception ef)
                        {
                            ef.printStackTrace();
                        }
                    }
                }
            }
}
