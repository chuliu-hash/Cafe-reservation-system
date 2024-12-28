import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class CheckOrder extends JDialog implements ActionListener {

    private JTable jt;
    private JPanel jp = new JPanel();
    private JButton  jb_num = new JButton("修改数量");
    private JButton jb_del = new JButton("删除餐品");
    private JLabel jl_price = new JLabel("总价:");
    private JLabel  jl_price2 = new JLabel();
    private JScrollPane jsp;

    private OrderManager order;



    public CheckOrder(Frame owner, String title, boolean modal)
    {
        super(owner,title,modal);

        order = (OrderManager) owner;
        jp.setLayout(null);

        jt = new JTable(new OrderModel());
        jt.getTableHeader().setReorderingAllowed(false);
        jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jt.setRowHeight(25);
        jsp = new JScrollPane(jt);

        jl_price.setBounds(10,520,50,20);
        jl_price.setFont(new Font("黑体",Font.BOLD,15));

        jl_price2.setBounds(50,519,80,20);
        jl_price2.setFont(new Font("微软雅黑",Font.BOLD,15));

        jl_price2.setText("¥"+ ToolFunction.count_money()[0]);

        jb_num.setBounds(230,515,100,30);
        jb_del.setBounds(340,515,100,30);
        jb_num.setFocusPainted(false);
        jb_del.setFocusPainted(false);
        jb_num.addActionListener(this);
        jb_del.addActionListener(this);

        jsp.setBounds(0,0,500,500);


        jp.add(jsp);
        jp.add(jl_price);
        jp.add(jl_price2);
        jp.add(jb_num);
        jp.add(jb_del);

        this.add(jp);
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {

        //修改选中餐品的数量
        if (e.getSource() == jb_num)
        {
            int row = jt.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "请选择一种餐品", "订餐情况", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            try {
            new NumUpdate(this,"订餐情况",true,row);
            jt.setModel(new OrderModel());

            }catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        //删除选中的菜品
        else
        {
            int row = jt.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "请选择一种餐品", "订单情况", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            try {
                Connection connection = null;
                PreparedStatement statement = null;
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);
                String sql = "DELETE FROM USER_ORDER WHERE ID = ? AND USERID = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1,String.valueOf(new OrderModel().getValueAt(row,0)));
                statement.setString(2, String.valueOf(ToolFunction.userid));
                statement.executeUpdate();
                jt.setModel(new OrderModel());

                order.setJtForeground();


            }catch(Exception ex)
            {
                ex.printStackTrace();
            }

        }
        jl_price2.setText("¥"+ ToolFunction.count_money()[0]);

    }



}

class NumUpdate extends JDialog implements ActionListener {
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

    private int price;
    private int id;
    private String name;

    public NumUpdate(JDialog owner, String title, boolean modal, int row) {
        super(owner, title, modal);

        OrderModel md = new OrderModel();
        jp1.setLayout(null);

        jl_id.setBounds(80,60,100,20);
        jl_id.setFont(new Font("楷体",Font.BOLD,16));

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
        jt_num.setText(String.valueOf(md.getValueAt(row,3)) );

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

    //保存修改的数量
    public void actionPerformed(ActionEvent e) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);
            String sql = "UPDATE user_order Set NUM = ? WHERE ID = ? AND USERID = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(jt_num.getText()));
            statement.setInt(2, id);
            statement.setInt(3,ToolFunction.userid);
            statement.executeUpdate();

            this.dispose();

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }finally {
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
