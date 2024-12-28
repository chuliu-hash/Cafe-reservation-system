import javax.swing.*;
import java.awt.*;

public class MaterialManager extends JDialog {
    private JPanel jp = new JPanel();
    private JTable jt;
    private JScrollPane jsp;
    public MaterialManager(Frame owner, String title, boolean modal) {
        super(owner,title,modal);
        jt = new JTable(new MaterialModel());
        //设置表格列不能手动排序
        jt.getTableHeader().setReorderingAllowed(false);
        //只能选中一行
        jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //设置表格行高
        jt.setRowHeight(40);
        //设置表格第4列（下标为3）的宽度
        jt.getColumnModel().getColumn(3).setPreferredWidth(600);
        jsp = new JScrollPane(jt);
        jsp.setBounds(0,0,990,600);
        jp.setLayout(null);

        jp.add(jsp);
        this.add(jp);

        this.setTitle("咖啡厅订餐系统");
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

}
