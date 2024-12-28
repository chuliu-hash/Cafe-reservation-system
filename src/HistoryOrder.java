import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HistoryOrder extends JDialog implements ActionListener {
    private JPanel jp = new JPanel();
    private JScrollPane jsp;
    private JTable jt;
    private JButton jb_check = new JButton("查看订单内容");
    private JButton jb_evaluate = new JButton("评价订单");

    public HistoryOrder(Frame owner, String title, boolean modal)

    {
        super(owner,title,modal);

        jp.setLayout(null);

        jt = new JTable(new HistoryModel());
        jt.getTableHeader().setReorderingAllowed(false);
        jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jt.setRowHeight(25);
        jsp = new JScrollPane(jt);
        jsp.setBounds(0,0,600,400);

        jb_check.setBounds(120,415,130,30);
        jb_check.setFocusPainted(false);
        jb_check.addActionListener(this);

        jb_evaluate.setBounds(330,415,130,30);
        jb_evaluate.setFocusPainted(false);
        jb_evaluate.addActionListener(this);

        jp.add(jsp);
        jp.add(jb_check);
        jp.add(jb_evaluate);
        this.add(jp);
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        //查看订单内容
        if(e.getSource() == jb_check)
        {
            PreparedStatement statement = null;
            Connection connection = null;
            ResultSet resultSet = null;
            int row = jt.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "请选择一个订单", "订单状况", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            HistoryModel md = new HistoryModel();
            int id = (int) md.getValueAt(row,0);
            String menu="";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);
                String sql = "SELECT menu FROM admin_order WHERE orderid = ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, id);
                resultSet = statement.executeQuery();

                if (resultSet.next())
                {
                    menu = resultSet.getString("menu");
                }
                new MenuWindow(menu,this,"订单内容",true);

            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        //提交评价
        else if (e.getSource()==jb_evaluate)
        {
            PreparedStatement statement = null;
            Connection connection = null;
            ResultSet resultSet = null;
            int row = jt.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "请选择一个订单", "订单情况", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            HistoryModel md = new HistoryModel();
            int id = (int) md.getValueAt(row,0);
            String evaluate="";
            //获取已有评价
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);
                String sql1 = "SELECT evaluate FROM admin_order WHERE orderid = ?";
                statement = connection.prepareStatement(sql1);
                statement.setInt(1, id);
                resultSet = statement.executeQuery();

                if (resultSet.next())
                {
                    evaluate = resultSet.getString("evaluate");
                }
               new EvaluateWindow(evaluate,id,this,"订单内容",true);


            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}

class MenuWindow extends JDialog {
    JTextPane jt = new JTextPane();
    JScrollPane jsp = new JScrollPane(jt);

    public MenuWindow(String menu, JDialog owner, String title, boolean modal) {
        super(owner, title, modal);
        this.setLayout(null);

        jsp.setBounds(20, 20, 450, 220);
        jt.setText(menu);
        jt.setEditable(false);
        jt.setBackground(Color.white);
        jt.setFont(new Font("宋体", Font.BOLD, 14));

        // 设置行间距
        StyledDocument doc = jt.getStyledDocument();
        SimpleAttributeSet spacing = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(spacing, 0.5f); // 设置行间距为0.5倍行高
        doc.setParagraphAttributes(0, doc.getLength(), spacing, false);

        this.add(jsp);
        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}

class EvaluateWindow  extends JDialog implements ActionListener{
    private JTextPane jt = new JTextPane();
    private JScrollPane jsp = new JScrollPane(jt);
    private JButton jb = new JButton("提交评价");

    private int id;

    public EvaluateWindow(String evaluate,int id,JDialog owner, String title, boolean modal)
    {
        super(owner, title, modal);

        this.id =id;
        this.setLayout(null);
        jsp.setBounds(20, 20, 450, 180);
        jt.setText(evaluate);
        jt.setBackground(Color.white);
        jt.setFont(new Font("宋体", Font.BOLD, 14));

        jb.setBounds(180,220,100,30);

        // 设置行间距
        StyledDocument doc = jt.getStyledDocument();
        SimpleAttributeSet spacing = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(spacing, 0.5f); // 设置行间距为0.5倍行高
        doc.setParagraphAttributes(0, doc.getLength(), spacing, false);

        jb.setFocusPainted(false);
        jb.addActionListener(this);

        this.add(jsp);
        this.add(jb);
        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        Connection connection = null;
        PreparedStatement statement = null;
        //更新评价
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(ToolFunction.url, ToolFunction.user, ToolFunction.passwd);
            String sql2 = "UPDATE admin_order Set evaluate = ? WHERE orderid = ?";
            statement = connection.prepareStatement(sql2);
            statement.setString(1,jt.getText());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.dispose();
    }
}

