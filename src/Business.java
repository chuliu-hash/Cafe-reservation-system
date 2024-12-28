import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;

public class Business extends JDialog{
    private JPanel jp = new JPanel();
    private JLabel jl_ordernum = new JLabel("订单总数:");
    private JLabel jl_usernum = new JLabel("顾客总数:");
    private JLabel jl_cost = new JLabel("原料成本:");
    private JLabel jl_income = new JLabel("营业收入:");
    private JLabel jl_profit= new JLabel("营业净利润:");

    private JLabel jl_ordernum2 = new JLabel();
    private JLabel jl_usernum2 = new JLabel();
    private JLabel jl_cost2 = new JLabel();
    private JLabel jl_income2 = new JLabel();
    private JLabel jl_profit2 = new JLabel();

    public Business(Frame owner, String title, boolean modal)
    {
        super(owner,title,modal);
        jp.setLayout(null);
        jl_usernum.setBounds(80,20,100,50);
        jl_usernum.setFont(new Font("宋体", Font.BOLD, 15));

        jl_ordernum.setBounds(80,70,100,50);
        jl_ordernum.setFont(new Font("宋体", Font.BOLD, 15));

        jl_cost.setBounds(80,120,100,50);
        jl_cost.setFont(new Font("宋体", Font.BOLD, 15));

        jl_income.setBounds(80,170,100,50);
        jl_income.setFont(new Font("宋体", Font.BOLD, 15));

        jl_profit.setBounds(80,220,100,50);
        jl_profit.setFont(new Font("宋体", Font.BOLD, 15));

        jl_usernum2.setBounds(200,20,100,50);
        jl_usernum2.setFont(new Font("宋体", Font.BOLD, 15));

        jl_ordernum2.setBounds(200,70,100,50);
        jl_ordernum2.setFont(new Font("宋体", Font.BOLD, 15));

        jl_cost2.setBounds(200,120,100,50);
        jl_cost2.setFont(new Font("宋体", Font.BOLD, 15));

        jl_income2.setBounds(200,170,100,50);
        jl_income2.setFont(new Font("宋体", Font.BOLD, 15));

        jl_profit2.setBounds(200,220,100,50);
        jl_profit2.setFont(new Font("宋体", Font.BOLD, 15));

        statistic();

        jp.add(jl_usernum);
        jp.add(jl_ordernum);
        jp.add(jl_cost);
        jp.add(jl_income);
        jp.add(jl_profit);
        jp.add(jl_usernum2);
        jp.add(jl_ordernum2);
        jp.add(jl_income2);
        jp.add(jl_cost2);
        jp.add(jl_profit2);

        this.add(jp);
        this.setSize(400, 350);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }
    public void statistic()
    {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        int orderCount =0;
        int customerCount = 0;
        LocalDateTime currentDateTime = LocalDateTime.now();
        int month = currentDateTime.getMonthValue();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);
            String sql1 = "SELECT * FROM admin_order WHERE EXTRACT(month FROM time) =" + month;
            statement = connection.prepareStatement(sql1);
            resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                orderCount++;
            }
            jl_ordernum2.setText(String.valueOf(orderCount)+"份");
            String sql2 = "SELECT COUNT(DISTINCT userid) AS customer_count FROM admin_order WHERE EXTRACT(month FROM time) =" + month;
            statement = connection.prepareStatement(sql2);
            resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                 customerCount = resultSet.getInt("customer_count");

            }
            jl_usernum2.setText(String.valueOf(customerCount)+"人");

            String sql = "SELECT CONSUME,COST FROM ADMIN_ORDER  WHERE EXTRACT(month FROM time) =" + month;
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            int total_consume=0;
            int total_cost=0;
            while(resultSet.next())
            {
              total_consume += resultSet.getInt("CONSUME");
              total_cost += resultSet.getInt("cost");
            }
            jl_cost2.setText(total_cost +"/¥");
            jl_income2.setText(total_consume +  "/¥");
            jl_profit2.setText(total_consume - total_cost +"/¥");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
