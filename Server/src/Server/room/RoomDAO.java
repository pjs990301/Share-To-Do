package Server.room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    private Connection conn;
    private PreparedStatement pstmt;
    private Statement stmt;
    private ResultSet rs;
    private String users;

    public RoomDAO() {

    }

    //데이터베이스 연결
    public void getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/network";
            String user = "root", passwd = "12345";
            conn = DriverManager.getConnection(url, user, passwd);
            stmt = conn.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //데이터베이스 연결해제
    public void dbClose() throws SQLException {
        if (pstmt != null) pstmt.close();
        if (stmt != null) stmt.close();
        if (rs != null) rs.close();
        if (conn != null) conn.close();
    }

    //Room 생성
    public boolean insert(RoomDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "INSERT INTO room value(?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getRoomId());
            pstmt.setString(2, dto.getRoom_name());
            pstmt.setString(3, dto.getPassword());
            pstmt.setString(4, dto.getAdmin());
            pstmt.setString(5, "");
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

    //Room 삭제
    public boolean delete(String roomId) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "DELETE from room where room_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomId);

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

    public boolean update(RoomDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "UPDATE room set room_name = ?, room_pw = ? where room_id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, dto.getRoom_name());
            pstmt.setString(2, dto.getNewpassword());
            pstmt.setString(3, dto.getRoomId());
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

    public List<RoomDTO> getFullList_room() throws SQLException {

        List<RoomDTO> lists = new ArrayList<>();
        RoomDTO dto = new RoomDTO();
        try {
            getConnection();
            String sql = "SELECT * from room";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                dto.setRoomId(rs.getString("room_id"));
                dto.setRoom_name(rs.getString("room_name"));
                dto.setPassword(rs.getString("room_pw"));
                dto.setAdmin(rs.getString("user_id"));
                lists.add(new RoomDTO(dto.getRoomId(), dto.getRoom_name(), dto.getPassword(), dto.getAdmin()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return lists;
    }

    public String getRoomName_room(RoomDTO dto) throws SQLException {
        String name = "";
        try {
            getConnection();
            String sql = "SELECT room_name from room where room_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getRoomId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                dto.setRoom_name(rs.getString(1));
            }
            name = dto.getRoom_name();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return name;
    }

    public boolean enter_room(RoomDTO dto, String users) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "UPDATE room set user_id = ? where room_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, users + dto.getUserId() + "/");
            pstmt.setString(2, dto.getRoomId());
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

    public boolean outUserUpdate_room(RoomDTO dto, String users) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "UPDATE room set user_id = ? where room_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, users);
            pstmt.setString(2, dto.getRoomId());
            int r = pstmt.executeUpdate();
            if (r > 0) result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return result;
    }

    public String userIdList_room(RoomDTO dto) throws SQLException {
        try {
            getConnection();
            String sql = "SELECT user_id from room where room_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getRoomId());
            rs = pstmt.executeQuery();
            while (rs.next())
                users = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return users;
    }

    public String admin_room(RoomDTO dto) throws SQLException {
        String admin = "";
        try {
            getConnection();
            String sql = "SELECT admin_id from room where room_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getRoomId());
            rs = pstmt.executeQuery();
            while (rs.next())
                dto.setAdmin(rs.getString(1));
            admin = dto.getAdmin();
        } finally {
            dbClose();
        }
        return admin;
    }

    public boolean leaveUpdate_room(RoomDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "update topic set manager = (select admin_id from room where room_id = ?) where room_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getRoomId());
            pstmt.setString(2, dto.getRoomId());
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

}

