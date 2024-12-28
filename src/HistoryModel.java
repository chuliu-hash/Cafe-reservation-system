import javax.swing.table.AbstractTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class HistoryModel extends AbstractTableModel {
    //存放表中所有行
    Vector rowData = new Vector();
    //存放表中列名
    Vector columnNames= new Vector();

    PreparedStatement statement = null;
    Connection connection = null;
    ResultSet resultSet = null;



    public HistoryModel() {

        String sql = "select * from admin_order WHERE USERID = ?";
        columnNames.add("订单编号");
        columnNames.add("提交时间");
        columnNames.add("总花费/¥");
        columnNames.add("状态");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = ToolFunction.url;
            String user = ToolFunction.user;
            String passwd = ToolFunction.passwd;

            connection = DriverManager.getConnection(url, user, passwd);
            statement = connection.prepareStatement(sql);
            statement.setInt(1,ToolFunction.userid);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                //存放表中单行数据
                Vector hang = new Vector();
                hang.add(resultSet.getInt("orderid"));
                hang.add(resultSet.getString("time"));
                hang.add(resultSet.getInt("consume"));
                resultSet.getInt("state");
                if (resultSet.getInt("state") == 1)
                    hang.add("进行中");
                else
                    hang.add("已完成");
                //加入到rowData中
                rowData.add(hang);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet !=null)
                    resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //得到共有多少行
    public int getRowCount() {
        return this.rowData.size();
    }

    //得到共有多少列
    public int getColumnCount() {
        return this.columnNames.size();
    }

    //得到某行某列的数据
    public Object getValueAt(int row, int column) {

        return ((Vector) (this.rowData.get(row))).get(column);
    }
    //得到列名
    public String getColumnName(int column) {

        return (String)this.columnNames.get(column);
    }
}
