import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;

public class AdminManager extends JFrame implements ActionListener {
    private Object[] types = {"-查询全部订单-", "按订单编号查询", "按顾客编号查询", "按订单总价查询(当前价格以上)", "按订单总价查询(当前价格以下)", "本月订单","进行中订单"};
    private JComboBox searchType = new JComboBox(types);
    private JTextField search_field = new JTextField();
    private JPanel jp1 = new JPanel();
    private JPanel jp2 = new JPanel();
    private JPanel jp = new JPanel();
    private JLabel jl_id = new JLabel("管理员编号：");
    private JLabel jl_id2 = new JLabel();
    private JButton jb_search = new JButton("查询");
    private JButton jb_out = new JButton("登出");
    private JButton jb_material = new JButton("查看原料表");
    private JButton jb_check = new JButton("查看本月营业报表");
    private JButton jb_close = new JButton("结束选中订单");


    private JTable jt;
    private JScrollPane jsp;
    private JLabel jl_user_image = new JLabel();
    public AdminManager() {
        jt = new JTable(new AdminModel());
        //设置表格列不能手动排序
        jt.getTableHeader().setReorderingAllowed(false);
        //只能选中一行
        jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //设置表格行高
        jt.setRowHeight(40);
        setRowWidth();
        jsp = new JScrollPane(jt);

        jp.setLayout(null);
        jp1.setLayout(null);
        jp2.setLayout(null);

        jl_user_image.setBounds(10,5,50,50);
        jl_id.setBounds(70,10,80,50);
        jl_id2.setBounds(150,10,10,50);
        jl_id2.setText(String.valueOf(ToolFunction.userid));

        searchType.setBounds(350,20,200,30);
        search_field.setBounds(560,20,130,30);
        jb_search.setBounds(700,20,60,30);
        jb_out.setBounds(1000,20,60,30);
        jb_material.setBounds(360,10,100,30);
        jb_check.setBounds(470,10,150,30);
        jb_close.setBounds(630,10,130,30);

        jp1.setBounds(0,0,1100,60);
        jsp.setBounds(0,60,1087,650);
        jp2.setBounds(0,710,1100,70);


        jb_search.setFocusPainted(false);
        jb_search.addActionListener(this);

        jb_out.setFocusPainted(false);
        jb_out.addActionListener(this);

        jb_material.setFocusPainted(false);
        jb_material.addActionListener(this);

        jb_check.setFocusPainted(false);
        jb_check.addActionListener(this);

        jb_close.setFocusPainted(false);
        jb_close.addActionListener(this);


        ImageIcon user_image = ToolFunction.getUserImage(jl_user_image.getWidth(),jl_user_image.getHeight(), "resources/admin.png");
        jl_user_image.setIcon(user_image);

        jp1.add(jl_user_image);
        jp1.add(jl_id);
        jp1.add(jl_id2);
        jp1.add(searchType);
        jp1.add(search_field);
        jp1.add(jb_search);
        jp1.add(jb_out);


        jp2.add(jb_material);
        jp2.add(jb_check);
        jp2.add(jb_close);

        jp1.setBackground(Color.LIGHT_GRAY);
        jp2.setBackground(Color.LIGHT_GRAY);


        jp.add(jp1);
        jp.add(jsp);
        jp.add(jp2);

        this.add(jp);
        this.setTitle("咖啡厅订餐系统");
        this.setSize(1100, 800);

        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        //返回登录界面
        if (e.getSource() ==jb_out) {
            this.dispose();
            new Login();
        }
        //查找
        else if(e.getSource() == jb_search){
            int index = searchType.getSelectedIndex();
            String sql = "";
            String field =search_field.getText().trim();
            if(index == 0){
                sql = "select * from admin_order ";
            }
            else if(index == 1){
                if (!field.isEmpty())
                    sql = "select * from admin_order where orderid = "+Integer.parseInt(field);
            }
            else if(index == 2){
                sql = "select * from admin_order where userid = "+Integer.parseInt(field);
            }
            else if(index == 3){
                if (!field.isEmpty())
                    sql = "select * from admin_order where consume >= "+Integer.parseInt(field);
            }
            else if(index == 4){
                if (!field.isEmpty())
                    sql = "select * from admin_order where consume <= "+Integer.parseInt(field);
            }
            else if(index ==5) {
                LocalDateTime currentDateTime = LocalDateTime.now();
                int month = currentDateTime.getMonthValue();
                sql = "SELECT * FROM admin_order WHERE EXTRACT(month FROM time) = "+ month;
            }
            else if(index ==6) {
                sql = "select * from admin_order where state = 1";
            }
            //刷新表
            jt.setModel(new AdminModel(sql));
            setRowWidth();
        }
        // 查看原料表
        else if (e.getSource() == jb_material)
        {
            new MaterialManager(null,"原料情况",true);
        }
        //查看本月营业报表
        else if (e.getSource() == jb_check)
        {
            new Business(null,"本月报表",true);
        }
        //结束选中订单
        else if(e.getSource() == jb_close)
        {
            PreparedStatement statement = null;
            Connection connection = null;
            ResultSet resultSet = null;
            int row = jt.getSelectedRow();
            if (row == -1)
            {
                JOptionPane.showMessageDialog(null, "请选中一个订单", "订单情况", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int orderid = (int) new AdminModel().getValueAt(row,0);
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);
                String sql = "UPDATE admin_order SET state = 0 WHERE orderid = "+ orderid;
                statement = connection.prepareStatement(sql);
                statement.executeUpdate(sql);
                jt.setModel(new AdminModel());
                setRowWidth();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }


    }
    //设置表格行宽
    public void setRowWidth()
    {
        jt.getColumnModel().getColumn(0).setPreferredWidth(30);
        jt.getColumnModel().getColumn(1).setPreferredWidth(30);
        jt.getColumnModel().getColumn(2).setPreferredWidth(30);
        jt.getColumnModel().getColumn(3).setPreferredWidth(100);
        jt.getColumnModel().getColumn(4).setPreferredWidth(30);
        jt.getColumnModel().getColumn(5).setPreferredWidth(600);
    }



}
