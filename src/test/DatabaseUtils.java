import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DatabaseUtils {
    static String url = "jdbc:mysql://localhost:3306/system";
    static String user = "root";
    static String passwd = "WA2114009";
    static Connection connection = null;
    static PreparedStatement statement = null;
    static ResultSet resultSet = null;
    //检查数据库连接性
    public static boolean checkDatabaseConnection() {
        boolean flag = false;
        try {
            // 尝试连接数据库
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, passwd);
            flag =true;
        } catch (Exception ex) {
            // 连接失败，显示相关数据库信息
            String errorMessage = "数据库连接失败：" + "\nURL=" + url + "\nUser=" + user;
            JOptionPane.showMessageDialog(null, errorMessage, "连接情况", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (connection!=null)
                 connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
    //检查登录用户账号和密码
    public static boolean checkUsers( String userId, String password) {
        boolean flag = false;
        try {
            // 连接数据库
            connection = DriverManager.getConnection(url, user, passwd);
            // 创建 SQL 查询语句
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, password);
            // 执行查询
            resultSet = statement.executeQuery();

            // 判断结果集是否有数据
            if (resultSet.next()) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (resultSet!=null)
                  resultSet.close();
                if (statement!=null)
                  statement.close();
                if (connection!=null)
                  connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return flag;
    }
}