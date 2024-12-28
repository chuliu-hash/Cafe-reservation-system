import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

//订餐界面
public class OrderManager extends JFrame implements ActionListener {
    private Object[] types = {"-查询全部餐品-", "按编号查询", "按名称查询", "按价格查询(当前价格以上)", "按价格查询(当前价格以下)", "未售罄商品"};
    private JComboBox searchType = new JComboBox(types);
    private JPanel jp1 = new JPanel();
    private JPanel jp2 = new JPanel();
    private JPanel jp = new JPanel();
    private JLabel jl_id = new JLabel("用户编号：");
    private JLabel jl_id2 = new JLabel();
    private JButton jb_user = new JButton("用户信息");
    private JTextField search_field = new JTextField();
    private JButton jb_search = new JButton("查询");
    private JButton jb_out = new JButton("登出");
    private JButton jb_order = new JButton("订餐");
    private JButton jb_check = new JButton("查看订单信息");
    private JButton jb_sumit = new JButton("提交订单");
    private JButton jb_history = new JButton("查看历史订单");

    private JTable jt;
    private JScrollPane jsp;
    private JLabel jl_user_image = new JLabel();

    public OrderManager() {
        jt = new JTable(new MenuModel());
        //设置表格列不能手动排序
        jt.getTableHeader().setReorderingAllowed(false);
        //只能选中一行
        jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //设置表格行高
        jt.setRowHeight(40);
        //设置表格第4列（下标为3）的宽度
        jt.getColumnModel().getColumn(3).setPreferredWidth(600);

        //设置前景色
        setJtForeground();

        jsp = new JScrollPane(jt);

        jp.setLayout(null);
        jp1.setLayout(null);
        jp2.setLayout(null);

        jl_user_image.setBounds(10,5,50,50);
        jl_id.setBounds(70,10,80,50);
        jl_id2.setBounds(140,10,50,50);
        jl_id2.setText(String.valueOf(ToolFunction.userid));
        searchType.setBounds(350,20,200,30);
        search_field.setBounds(560,20,130,30);
        jb_user.setBounds(180,20,100,30);
        jb_search.setBounds(700,20,60,30);
        jb_out.setBounds(1000,20,60,30);
        jb_order.setBounds(400,10,60,30);
        jb_check.setBounds(470,10,120,30);
        jb_sumit.setBounds(600,10,100,30);
        jb_history.setBounds(900,10,130,30);

        jp1.setBounds(0,0,1100,60);
        jsp.setBounds(0,60,1087,650);
        jp2.setBounds(0,710,1100,70);

        jb_user.setFocusPainted(false);
        jb_user.addActionListener(this);

        jb_search.setFocusPainted(false);
        jb_search.addActionListener(this);

        jb_out.setFocusPainted(false);
        jb_out.addActionListener(this);

        jb_order.setFocusPainted(false);
        jb_order.addActionListener(this);

        jb_check.setFocusPainted(false);
        jb_check.addActionListener(this);

        jb_sumit.setFocusPainted(false);
        jb_sumit.addActionListener(this);

        jb_history.setFocusPainted(false);
        jb_history.addActionListener(this);


        ImageIcon user_image = ToolFunction.getUserImage(jl_user_image.getWidth(),jl_user_image.getHeight());
        jl_user_image.setIcon(user_image);

        jp1.add(jl_user_image);
        jp1.add(jl_id);
        jp1.add(jl_id2);
        jp1.add(jb_user);
        jp1.add(searchType);
        jp1.add(search_field);
        jp1.add(jb_search);
        jp1.add(jb_out);


        jp2.add(jb_order);
        jp2.add(jb_check);
        jp2.add(jb_sumit);
        jp2.add(jb_history);

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
        //用户个人信息
        if (e.getSource() == jb_user) {
            new UserManger(this,"用户个人信息",true);
        }
        //查询功能
        else if(e.getSource() == jb_search){
            int index = searchType.getSelectedIndex();
            String sql = "";
            String field =search_field.getText().trim();
            if(index == 0){
                sql = "select * from meals ";
            }
            else if(index == 1){
                if (!field.isEmpty())
                    sql = "select * from meals where ID = "+Integer.parseInt(field);
            }
            else if(index == 2){
                sql = "select * from meals where Name like '%"+field+"%'";
            }
            else if(index == 3){
                if (!field.isEmpty())
                    sql = "select * from meals where Price >= "+Integer.parseInt(field);
            }
            else if(index == 4){
                if (!field.isEmpty())
                    sql = "select * from meals where Price <= "+Integer.parseInt(field);
            }
            else if(index ==5) {
                sql = "select * from meals where Soldout = 1 ";
            }
            //刷新表
            jt.setModel(new MenuModel(sql));
            jt.getColumnModel().getColumn(3).setPreferredWidth(600);
            setJtForeground();
        }
        //返回登录界面
        else if (e.getSource() ==jb_out) {
            this.dispose();
            new Login();
        }
        //订餐
        else if (e.getSource() == jb_order)
        {
            int row = jt.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "请选择一种餐品", "订餐情况", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            try {
                //检查餐品是否售罄
                if ((String.valueOf(new MenuModel().getValueAt(row,4))).equals("售罄"))
                {
                    JOptionPane.showMessageDialog(null, "该餐品已经售罄", "订餐情况", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                //检查餐品在订单内是否已经存在
               if (checkMenu(row)) {
                   new SelectOrder(this, "订餐情况", true, row);
               }
               else
                   JOptionPane.showMessageDialog(null, "该餐品在订单内已存在", "订餐情况", JOptionPane.PLAIN_MESSAGE);

            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        //订单情况
        else if (e.getSource() == jb_check)
        {
            new CheckOrder(this, "订单情况", true);
        }

        //提交订单
        else if (e.getSource() == jb_sumit)
        {

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            String menu ="";
            int id=-1;
            String name ="";
            int num=-1;
            int price =-1;
            LocalDateTime currentDateTime = LocalDateTime.now();
            Timestamp time = java.sql.Timestamp.valueOf(currentDateTime);

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);
                String sql1 = "SELECT * FROM USER_ORDER  WHERE USERID = ?";
                statement = connection.prepareStatement(sql1);
                statement.setInt(1,ToolFunction.userid);
                resultSet = statement.executeQuery();

                //获取订单内容
                while(resultSet.next())
                {
                   id = resultSet.getInt("ID");
                   name = resultSet.getString("NAME");
                   num = resultSet.getInt("NUM");
                   price = resultSet.getInt("Price");
                   menu += String.format("餐品编号:%d,餐品名称:%s,餐品数量:%d,餐品单价:¥%d\n",id,name,num,price);
                }
                //订单为空时不提交
                if (menu.isEmpty())
                {
                    JOptionPane.showMessageDialog(null,"订单为空","提交情况",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String sql2 = "SELECT * from  admin_order";
                statement = connection.prepareStatement(sql2);
                resultSet = statement.executeQuery();
                //获取当前数据库中的订单总数
                int rowCount = 0;
                while (resultSet.next()) {
                    rowCount++;
                }
                //插入订单
               String sql3 = "INSERT INTO admin_order (orderid,userid,menu,consume,time,state,cost) VALUES (?, ?, ?, ?, ?, ?,?)";
               statement = connection.prepareStatement(sql3);

               statement.setInt(1,rowCount+1);
               statement.setInt(2,ToolFunction.userid);
               statement.setString(3,menu);
               statement.setInt(4,ToolFunction.count_money()[0]);
               statement.setTimestamp(5, time);
               statement.setInt(6,1);
                statement.setInt(7,ToolFunction.count_money()[1]);
               statement.executeUpdate();

               //从缓存用户订单中删除当前提交的订单
               String sql4 = "DELETE FROM USER_ORDER WHERE USERID = ?";
               statement = connection.prepareStatement(sql4);
               statement.setString(1, String.valueOf(ToolFunction.userid));
               statement.executeUpdate();

               setJtForeground();
               JOptionPane.showMessageDialog(null,"订单提交成功","提交情况",JOptionPane.PLAIN_MESSAGE);

            }catch (Exception ex)
            {
                ex.printStackTrace();
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
        else if(e.getSource() == jb_history)
        {
            new HistoryOrder(this,"历史订单",true);
        }
    }
    // 获得用户图标组件
    public JLabel getJl_user_image() {
        return jl_user_image;
    }


    //检查餐品是否已经存在
    public Boolean checkMenu(int row)
    {
        boolean flag =false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet =null;
        int id = Integer.parseInt(String.valueOf(new MenuModel().getValueAt(row,0)));
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);
            String sql = "SELECT * FROM USER_ORDER WHERE ID = ? AND USERID = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setInt(2, ToolFunction.userid);
            resultSet = statement.executeQuery();
            if (!resultSet.next())
                flag =true;
        }catch (Exception e)
        {
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
        return flag;
    }

    //设置指定多行的前景色
    public void setJtForeground()
    {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet =null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);
            String sql = "SELECT ID FROM USER_ORDER WHERE USERID = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, ToolFunction.userid);
            resultSet = statement.executeQuery();
            ArrayList<Integer> list = new ArrayList<>();
            //获取所有该用户当前订单的餐品编号
            while (resultSet.next()) {
                list.add(resultSet.getInt("ID"));
            }
            MenuModel md =new MenuModel();
            ArrayList<Integer> list2 = new ArrayList<>();
            //获取每一餐品编号对应餐品所在的行
            for (int id : list)
            {
                list2.add(md.getRowByColumnValue(0, id));
            }
            ToolFunction.makeFace(jt,list2);
            jt.repaint();


        }catch(Exception e)
        {
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
}
