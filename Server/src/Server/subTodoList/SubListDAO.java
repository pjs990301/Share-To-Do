package Server.subTodoList;

import java.sql.*;

public class SubListDAO {

    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public SubListDAO() {

    }

    //데이터베이스 연결
    public void getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/network";
            String user = "root", passwd = "12345";
            conn = DriverManager.getConnection(url, user, passwd);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //데이터베이스 연결해제
    public void dbClose() throws SQLException {
        if (pstmt != null) pstmt.close();
        if (rs != null) rs.close();
        if (conn != null) conn.close();
    }

    //서브 todolist 삽입
    public boolean insert(SubListDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "INSERT INTO sub value(?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getRoomMainId());
            pstmt.setString(2, dto.getSubNum());
            pstmt.setString(3, dto.getRoomMainSubId());
            pstmt.setString(4, dto.getSubDo());
            pstmt.setString(5, dto.getCompleted());

            int r = pstmt.executeUpdate();
            if (r > 0)
                result = true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return result;
    }

    public boolean completeUpdate_sub(SubListDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "UPDATE sub set completed = ? where room_main_sub_id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, dto.getCompleted());
            pstmt.setString(2, dto.getRoomMainSubId());
            int r = pstmt.executeUpdate();

            if (r > 0)
                result = true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return result;
    }

    public boolean nameUpdate_sub(SubListDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "UPDATE sub set sub_do = ? where room_main_sub_id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, dto.getNewsubDo());
            pstmt.setString(2, dto.getRoomMainSubId());
            int r = pstmt.executeUpdate();

            if (r > 0)
                result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return result;
    }

    public String getSubList_sub(SubListDTO dto) throws SQLException{
        String subs = "";
        try{
            getConnection();
            String sql = "SELECT sub_do,completed,sub_num from sub where room_main_id = ?";
            pstmt= conn.prepareStatement(sql);
            pstmt.setString(1,dto.getRoomMainId());
            rs = pstmt.executeQuery();
            while(rs.next()){
                dto.setSubDo(rs.getString(1));
                dto.setCompleted(rs.getString(2));
                dto.setSubNum(rs.getString(3));
                subs = subs + "sub_name:"+dto.getSubDo() + "/sub_completed:" + dto.getCompleted() + "/sub_id:" + dto.getSubNum() + "/";;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return subs;
    }
}
