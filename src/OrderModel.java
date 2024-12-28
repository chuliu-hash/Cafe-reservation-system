import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;

public class OrderModel extends AbstractTableModel {
    //存放表中所有行
    Vector rowData = new Vector();
    //存放表中列名
    Vector columnNames= new Vector();

    PreparedStatement statement = null;
    Connection connection = null;
    ResultSet resultSet = null;



    public OrderModel() {

        String sql = "select * from user_order WHERE USERID = ?";
        columnNames.add("餐品编号");
        columnNames.add("餐品名字");
        columnNames.add("餐品单价/¥");
        columnNames.add("餐品数量");

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
                hang.add(resultSet.getInt("ID"));
                hang.add(resultSet.getString("NAME"));
                hang.add(resultSet.getInt("PRICE"));
                hang.add(resultSet.getInt("NUM"));

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
