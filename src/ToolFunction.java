import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

//一些常用的常量和功能
public class ToolFunction {
    static String url = "jdbc:mysql://localhost:3306/coffee_system";
    static String user = "root";
    static String passwd = "123456789";
    static int userid = 1;

    //检查数据库连接性
    public static boolean checkDatabaseConnection() {
        boolean flag = false;
        Connection connection = null;
        try {
            // 尝试连接数据库
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, passwd);
            flag = true;
        } catch (Exception ex) {
            // 连接失败，显示相关数据库信息
            String errorMessage = "数据库连接失败：" + "\nURL=" + url + "\nUser=" + user;
            JOptionPane.showMessageDialog(null, errorMessage, "连接情况", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (!connection.isClosed())
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    //选择用户头像
    public static ImageIcon choseImage(int Width, int Height, String filePath)
    {
        ImageIcon user_image;
        if (filePath.equals(""))
            //从项目目录选择用户头像
            if (new File(String.format("resources/user%s.png",ToolFunction.userid)).exists())
            {
                user_image = new ImageIcon(String.format("resources/user%s.png",ToolFunction.userid));
                Image scaledImage = user_image.getImage().getScaledInstance(Width,Height,Image.SCALE_SMOOTH);
                user_image = new ImageIcon(scaledImage);
            }
            //从类路径的根目录选择(编译输出目录out下的项目目录)
            else
            {
                URL userimage = Main.class.getResource("/resources/user.png");
                user_image = new ImageIcon(userimage);
            }
            //从filePath选择用户头像
        else
        {
            user_image = new ImageIcon(filePath);
            Image scaledImage = user_image.getImage().getScaledInstance(Width,Height,Image.SCALE_SMOOTH);
            user_image = new ImageIcon(scaledImage);
        }
        return user_image;
    }

   //从filePath中获取头像
   public static ImageIcon getUserImage(int Width, int Height, String filePath)
   {
       return  choseImage(Width,Height,filePath);
   }
   //从项目目录中获取头像
    public static ImageIcon getUserImage(int Width, int Height)
    {
        return  choseImage(Width,Height,"");
    }


    //定义jt渲染前景色的方法
    public static void makeFace(JTable table, ArrayList<Integer> list) {
            try {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {

                public Component getTableCellRendererComponent(JTable table,
                                                               Object value, boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    if (list.contains(row)) {
                        setForeground(Color.red); // 设置选中行的前景色为红色
                    }
                    else
                        setForeground(Color.BLACK);
                    return super.getTableCellRendererComponent(table, value,
                            isSelected, hasFocus, row, column);
                }
            };
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    //统计顾客当前订单的成本和单价
    public static int[] count_money()
    {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int[] total = new int[2];
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);
            String sql = "SELECT PRICE,NUM,COST FROM USER_ORDER WHERE USERID = ? ";
            statement = connection.prepareStatement(sql);
            statement.setString(1, String.valueOf(ToolFunction.userid));
            resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                int price = resultSet.getInt("PRICE");
                int cost = resultSet.getInt("cost");
                int num  = resultSet.getInt("NUM");
                total[0] += price*num;
                total[1] += cost*num;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (Exception ef) {
                ef.printStackTrace();
            }
        }
        return total;
    }





}