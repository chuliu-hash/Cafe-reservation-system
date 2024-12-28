import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;

//用户个人信息界面
public class UserManger extends JDialog implements ActionListener {
    private JLabel jl_id = new JLabel("用户编号：");
    private JLabel jl_name = new JLabel("用户名：");
    private JLabel jl_sex = new JLabel("性别：");
    private JLabel jl_phone = new JLabel("联系电话：");
    private JLabel jl_user_image = new JLabel();
    private JLabel jl_sign = new JLabel("个性签名：");

    private JLabel jl_id2 = new JLabel();
    private JTextField jt_name = new JTextField();
    private JTextField jt_phone = new JTextField();
    private JTextField jt_sign = new JTextField();

    private JPanel jp1 = new JPanel();

    private JButton jb_save = new JButton("保存");
    private JButton jb_cancel = new JButton("取消");
    private JButton jb_select = new JButton("上传头像");
    private JButton jb_pw = new JButton("修改密码");

    private JRadioButton jc_male = new JRadioButton("男");
    private JRadioButton jc_female = new JRadioButton("女");
    private ButtonGroup genderButtonGroup = new ButtonGroup();


    private OrderManager order;
    private String filePath;

    public UserManger(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        order = (OrderManager) owner;

        jp1.setLayout(null);

        jl_id.setBounds(5, 5, 100, 20);
        jl_name.setBounds(5, 40, 100, 20);
        jl_sex.setBounds(5, 75, 100, 20);
        jl_phone.setBounds(5, 110, 100, 20);
        jl_sign.setBounds(5,145,100,20);

        jl_id2.setBounds(110, 7, 100, 20);
        jt_name.setBounds(110, 40, 100, 20);
        jt_phone.setBounds(110, 110, 100, 20);
        jt_sign.setBounds(70, 145, 170, 20);
        jt_sign.setFont(new Font("黑体",Font.PLAIN,13));


        genderButtonGroup.add(jc_male);
        genderButtonGroup.add(jc_female);
        jc_male.setBounds(110, 75, 40, 20);
        jc_female.setBounds(150, 75, 40, 20);
        jc_male.setFocusPainted(false);
        jc_female.setFocusPainted(false);

        jl_user_image.setBounds(265, 30, 80, 80);

        jb_select.setBounds(255, 130, 100, 30);
        jb_select.addActionListener(this);
        jb_select.setFocusPainted(false);

        jb_save.setBounds(80, 190, 60, 30);
        jb_save.addActionListener(this);
        jb_save.setFocusPainted(false);

        jb_pw.setBounds(180, 190, 100, 30);
        jb_pw.setFocusPainted(false);
        jb_pw.addActionListener(this);


        ImageIcon user_image = ToolFunction.getUserImage(jl_user_image.getWidth(),jl_user_image.getHeight());
        jl_user_image.setIcon(user_image);

        setMessage();

        jp1.add(jl_id);
        jp1.add(jl_name);
        jp1.add(jl_sex);
        jp1.add(jl_phone);
        jp1.add(jl_id2);
        jp1.add(jt_name);
        jp1.add(jc_male);
        jp1.add(jc_female);
        jp1.add(jt_phone);
        jp1.add(jb_save);
        jp1.add(jb_cancel);
        jp1.add(jl_user_image);
        jp1.add(jb_select);
        jp1.add(jl_sign);
        jp1.add(jb_pw);
        jp1.add(jt_sign);



        this.add(jp1);
        this.setSize(390, 300);

        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {

        //选择一张图片预览
        if (e.getSource() == jb_select) {
            FileDialog fileDialog = new FileDialog(this, "选择图片文件", FileDialog.LOAD);
            fileDialog.setFile("*.jpg;*.jpeg;*.png;*.gif;*.bmp");
            fileDialog.setVisible(true);

            if (fileDialog.getDirectory() != null) {
                String directory = fileDialog.getDirectory();
                String filename = fileDialog.getFile();
                filePath = directory + filename;
                ImageIcon user_image = ToolFunction.getUserImage(jl_user_image.getWidth(),jl_user_image.getHeight(),filePath);
                jl_user_image.setIcon(user_image);
            }
        }
        //保存用户信息和选择的图片
        else if(e.getSource() == jb_save)
        {
            String name = jt_name.getText();
            String phone = jt_phone.getText();
            String sign = jt_sign.getText();
            String sex;
            if (jc_male.isSelected())
                 sex = "M";
            else
                 sex = "F";

            //保存用户信息
            saveMessage(name,sex,phone,sign);
            //更新用户头像
            if (filePath != null) {
                try {
                    //复制文件到工作目录下的resources文件夹
                    Path sourcePath = new File(filePath).toPath();
                    Path destinationPath = new File(String.format("resources/user%s.png",ToolFunction.userid)).toPath();
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    ImageIcon user_image = ToolFunction.getUserImage(order.getJl_user_image().getWidth(),order.getJl_user_image().getHeight());
                    order.getJl_user_image().setIcon(user_image);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            this.dispose();
        }
        else if( e.getSource() == jb_pw)
        {
               new PwChange(this,"修改密码",true);
        }

    }

    //在界面设置用户信息
    public void setMessage()
    {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String url = ToolFunction.url;
        String user = ToolFunction.user;
        String passwd = ToolFunction.passwd;

        jl_id2.setText(String.valueOf(ToolFunction.userid));
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, passwd);
            String sql = "SELECT PHONE,NICKNAME,SEX,SIGN FROM USERS WHERE ID = "+ ToolFunction.userid;
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                jt_phone.setText(resultSet.getString("PHONE"));
                jt_name.setText(resultSet.getString("NICKNAME"));
                jt_sign.setText(resultSet.getString("SIGN"));
                if (resultSet.getString("SEX") != null)
                {
                    String sex = resultSet.getString("SEX");
                    if (sex.equals("M"))
                        jc_male.setSelected(true);
                    else if (sex.equals("F"))
                        jc_female.setSelected(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
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


    //保存用户信息到数据库
    private void saveMessage(String name, String sex, String phone, String sign) {
        Connection connection = null;
        PreparedStatement statement = null;
        String url = ToolFunction.url;
        String user = ToolFunction.user;
        String passwd = ToolFunction.passwd;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, passwd);
            String sql = "UPDATE users SET nickname=?, sex=?, phone=?, sign=? WHERE id=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, sex);
            statement.setString(3, phone);
            statement.setString(4, sign);
            statement.setInt(5, ToolFunction.userid);
            statement.executeUpdate();
        }catch(Exception e)
        {e.printStackTrace();}
        finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
            } catch (Exception e)
            {e.printStackTrace();}
        }
    }






}




//修改密码窗口
class PwChange extends JDialog implements ActionListener {
    private  JLabel jl_pw1 = new JLabel("原密码：");
    private  JLabel jl_pw2 = new JLabel("新密码：");
    private  JLabel jl_pw3 = new JLabel("确定新密码：");

    private JPasswordField jpw_1 = new JPasswordField();
    private JPasswordField jpw_2 = new JPasswordField();
    private JPasswordField jpw_3 = new JPasswordField();

    private JButton jb_save = new JButton("确认");

    private JPanel  jp1 = new JPanel();



    public PwChange(JDialog owner, String title, boolean modal)
    {
        super(owner, title, modal);


        jp1.setLayout(null);

        jl_pw1.setBounds(70,30,60,50);
        jl_pw1.setFont(new Font("黑体",Font.PLAIN, 15));
        jl_pw2.setBounds(70,80,60,50);
        jl_pw2.setFont(new Font("黑体",Font.PLAIN, 15));
        jl_pw3.setBounds(60,130,100,50);
        jl_pw3.setFont(new Font("黑体",Font.PLAIN, 15));


        jpw_1.setBounds(150,45,150,20);
        jpw_2.setBounds(150,95,150,20);
        jpw_3.setBounds(150,145,150,20);

        jb_save.setBounds(150,200,60,30);
        jb_save.setFocusPainted(false);
        jb_save.addActionListener(this);


        jp1.add(jl_pw1);
        jp1.add(jl_pw2);
        jp1.add(jl_pw3);
        jp1.add(jpw_1);
        jp1.add(jpw_2);
        jp1.add(jpw_3);
        jp1.add(jb_save);
        this.add(jp1);
        this.setSize(390, 300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);



    }
    public void actionPerformed(ActionEvent e) {
        String pw1 = String.valueOf(jpw_1.getPassword());
        String pw2 = String.valueOf(jpw_2.getPassword());
        String pw3 = String.valueOf(jpw_3.getPassword());
        String pw_org="";
            if (pw1.isEmpty() || pw2.isEmpty() || pw3.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "密码为空", "密码情况", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (!pw2.equals(pw3))
            {
                JOptionPane.showMessageDialog(null, "两次输入的新密码不一致", "密码情况", JOptionPane.ERROR_MESSAGE);
                jpw_3.setText("");
                return;
            }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(ToolFunction.url,ToolFunction.user,ToolFunction.passwd);
            String sql1 = "SELECT PASSWORD FROM USERS WHERE ID = ? ";
            statement =connection.prepareStatement(sql1);
            statement.setString(1, String.valueOf(ToolFunction.userid));
            resultSet = statement.executeQuery();
            //获取原密码
            if (resultSet.next())
                pw_org = resultSet.getString("password");
            //原密码输入正确
            if (pw_org.equals(pw1))
            {
                //更新密码
                String sql2 = "UPDATE USERS SET PASSWORD = ? WHERE ID = ?";
                statement = connection.prepareStatement(sql2);
                statement.setString(1, pw2);
                statement.setString(2, String.valueOf(ToolFunction.userid));
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "密码修改成功", "密码情况", JOptionPane.PLAIN_MESSAGE);
                this.dispose();
            }
            else
                JOptionPane.showMessageDialog(null, "原密码不正确", "密码情况", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}

