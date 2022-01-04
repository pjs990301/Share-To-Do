package Server.mainToDoList;

import Server.Member.MemberDAO;
import Server.Member.MemberDTO;

import java.nio.charset.StandardCharsets;
import java.sql.*;

public class MainListDAO {

    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;


    public MainListDAO() {

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
        conn = null;
    }

    //메인 todolist 삽입
    public boolean insert(MainListDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "INSERT INTO topic value(?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getRoomId());
            pstmt.setString(2, dto.getMainNum());
            pstmt.setString(3, dto.getRoomMainId());
            pstmt.setString(4, dto.getMainDo());
            pstmt.setString(5, dto.getManager());
            pstmt.setString(6, dto.getDeadline());
            pstmt.setString(7, dto.getCompleted());
            int r = pstmt.executeUpdate();

            if (r > 0) {
                result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return result;
    }

    public boolean completeUpdate_topic(MainListDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "UPDATE topic set completed = ?  where room_main_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getCompleted());
            pstmt.setString(2, dto.getRoomMainId());
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

    public boolean deadlineUpdate_topic(MainListDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "UPDATE topic set deadline = ?  where room_main_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getDeadline());
            pstmt.setString(2, dto.getRoomMainId());
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

    public boolean managerUpdate_topic(MainListDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "UPDATE topic set manager = ? where room_main_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getManager());
            pstmt.setString(2, dto.getRoomMainId());

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

    public boolean nameUpdate_topic(MainListDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "UPDATE topic set topic_name = ?  where room_main_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getNewMainDo());
            pstmt.setString(2, dto.getRoomMainId());
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

    public String getTopicList_topic(MainListDTO dto) throws SQLException {
        String topics ="";
        try {
            getConnection();
            String sql = "SELECT topic_num,topic_name,completed,deadline,manager from topic where room_id =? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getRoomId());
            rs = pstmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                dto.setMainNum(rs.getString(1));
                dto.setMainDo(rs.getString(2));
                dto.setCompleted(rs.getString(3));
                dto.setDeadline(rs.getString(4));
                dto.setManager(rs.getString(5));

                if (i == 0)
                    topics = topics + "/topic_id:"+dto.getMainNum()
                            + "/topic_name:" + dto.getMainDo()
                            + "/topic_completed:" + dto.getCompleted()
                            + "/topic_deadline:" + dto.getDeadline()
                            + "/topic_manager:" + dto.getManager();
                else
                    topics = topics + "/topic_id:"+dto.getMainNum()
                            + "/topic_name:" + dto.getMainDo()
                            + "/topic_completed:" + dto.getCompleted()
                            + "/topic_deadline:" + dto.getDeadline()
                            + "/topic_manager:" + dto.getManager();
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return topics;

    }
    public String getManger_topic(String roomId, String userId) throws SQLException{
        String topic = "";
        MemberDTO mdto = new MemberDTO();
        try{
            getConnection();
            String sql = "SELECT manager from topic where room_id = ? and manager = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomId);
            pstmt.setString(2, userId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                topic = rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return topic;

    }

}

