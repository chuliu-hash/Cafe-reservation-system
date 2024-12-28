import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;

//数据库菜单表模型
public class MenuModel extends AbstractTableModel {
    //存放表中所有行
    Vector rowData = new Vector();
    //存放表中列名
    Vector columnNames= new Vector();

    PreparedStatement statement = null;
    Connection connection = null;
    ResultSet resultSet = null;

    //初始化
    public void init(String sql) {
        if (sql.equals("")) {
            sql = "select * from meals";
        }

        columnNames.add("编号");
        columnNames.add("名字");
        columnNames.add("单价/¥");
        columnNames.add("介绍");
        columnNames.add("是否售罄");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = ToolFunction.url;
            String user = ToolFunction.user;
            String passwd = ToolFunction.passwd;

            connection = DriverManager.getConnection(url, user, passwd);
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                //存放表中单行数据
                Vector hang = new Vector();
                hang.add(resultSet.getInt("ID"));
                hang.add(resultSet.getString("NAME"));
                hang.add(resultSet.getInt("PRICE"));
                hang.add(resultSet.getString("INTRODUCTION"));
                if(resultSet.getInt("SOLDOUT")==1)
                {
                    hang.add("销售中");
                }
                else
                    hang.add("售罄");


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


    //第二个构造函数，通过传递的sql语句来获得数据模型
    public MenuModel(String sql) {
        this.init(sql);
    }

    //构造函数，用于初始化数据模型（表）
    public MenuModel() {
        this.init("");
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
    //查询指定列中对应指定值的行号
    public  int getRowByColumnValue(int columnIndex, Object value)
    {
        int rowCount = this.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            Object cellValue = this.getValueAt(row, columnIndex);
            if (cellValue != null && cellValue.equals(value)) {
                return row;
            }
        }
        // 如果没有找到匹配的行，返回-1表示未找到
        return -1;
    }

}
