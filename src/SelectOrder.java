import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SelectOrder extends JDialog implements ActionListener {
    private JPanel jp1 = new JPanel();
    private JLabel jl_id = new JLabel("餐品编号:");
    private JLabel jl_id2 = new JLabel();
    private JLabel jl_name = new JLabel("餐品名称:");
    private JLabel jl_name2 = new JLabel();
    private JLabel jl_price = new JLabel("餐品单价：");
    private JLabel jl_price2 = new JLabel();
    private JLabel jl_num = new JLabel("订购数量：");
    private JTextField jt_num = new JTextField();
    private JButton jb_ok = new JButton("确定");

    private  OrderManager order;
    private int price;
    private int id;
    private String name;

    public SelectOrder(Frame owner, String title, boolean modal, int row) {
        super(owner, title, modal);
        order = (OrderManager) owner;

        MenuModel md = new MenuModel();

        jp1.setLayout(null);

        jl_id.setBounds(80,60,100,20);
        jl_id.setFont(new Font("宋体",Font.BOLD,15));

        id = (int) md.getValueAt(row,0);
        jl_id2.setText(String.valueOf(id));
        jl_id2.setBounds(160,60,100,20);
        jl_id2.setFont(new Font("宋体",Font.BOLD,15));

        jl_name.setBounds(80,100,100,20);
        jl_name.setFont(new Font("宋体",Font.BOLD,15));

        jl_name2.setBounds(160,100,100,20);
        name = (String) md.getValueAt(row,1);
        jl_name2.setText(name);
        jl_name2.setFont(new Font("宋体",Font.BOLD,15));

        jl_price.setBounds(80,140,100,20);
        jl_price.setFont(new Font("宋体",Font.BOLD,15));
        jl_price2.setBounds(160,140,100,20);
        price = (int) md.getValueAt(row,2);
        jl_price2.setText("¥"+ price );
        jl_price2.setFont(new Font("宋体",Font.BOLD,15));

        jl_num.setBounds(80,180,100,20);
        jl_num.setFont(new Font("宋体",Font.BOLD,15));
        jt_num.setBounds(160,180,50,20);
        jt_num.setFont(new Font("宋体",Font.BOLD,15));
        jt_num.setText("1");

        jb_ok.setBounds(130,230,60,30);
        jb_ok.setFocusPainted(false);
        jb_ok.addActionListener(this);

        jp1.add(jl_id);
        jp1.add(jl_id2);
        jp1.add(jl_name);
        jp1.add(jl_name2);
        jp1.add(jl_price);
        jp1.add(jl_price2);
        jp1.add(jl_num);
        jp1.add(jt_num);
        jp1.add(jb_ok);
        this.add(jp1);
        this.setSize(340, 350);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }



    //添加选中的餐品
    public void actionPerformed(ActionEvent e) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int cost =0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);
            String sql1 = "Select cost from meals  where id =" + id;
            statement = connection.prepareStatement(sql1);
            resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                cost = resultSet.getInt("cost");
            }
            String sql2 = "INSERT INTO user_order (num, id, name, price, userid,cost) VALUES (?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql2);
            statement.setInt(1, Integer.parseInt(jt_num.getText()));
            statement.setInt(2, id);
            statement.setString(3, name);
            statement.setInt(4, price);
            statement.setInt(5, ToolFunction.userid);
            statement.setInt(6, cost);
            statement.executeUpdate();

            order.setJtForeground();
            this.dispose();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
            } catch (Exception ef) {
                ef.printStackTrace();
            }
        }
    }

}


