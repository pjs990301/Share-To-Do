package Server.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public MemberDAO() {

    }

    //데이터베이스 연결
    public void getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/network";
            String user = "root", passwd = "12345";
            conn = DriverManager.getConnection(url, user, passwd);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //데이터베이스 연결해제
    public void dbClose() throws SQLException {
        if (pstmt != null) pstmt.close();
        if (rs != null) rs.close();
        if (conn != null) conn.close();
    }

    //데이터베이스 회원정보 입력
    public boolean insert(MemberDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "INSERT INTO user value(?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getPw());
            pstmt.setString(3, dto.getName());
            pstmt.setString(4, dto.getBirth());
            pstmt.setString(5, dto.getEmail());
            pstmt.setString(6, dto.getTel());
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

    public boolean update(MemberDTO dto) throws SQLException {
        boolean result = false;
        try {
            getConnection();
            String sql = "UPDATE user set user_pw =?, user_name =?,user_birth =?, user_email =?, user_tel =?  where user_id =?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, dto.getNewpw());
            pstmt.setString(2, dto.getName());
            pstmt.setString(3, dto.getBirth());
            pstmt.setString(4, dto.getEmail());
            pstmt.setString(5, dto.getTel());
            pstmt.setString(6, dto.getId());

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

    public List<MemberDTO> getList( )throws SQLException{

        List<MemberDTO> lists = new ArrayList<>();
        MemberDTO dto = new MemberDTO();
        try {
            getConnection();
            String sql = "SELECT * from user";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery(sql);
            while(rs.next()){
                dto.setId(rs.getString("user_id"));
                dto.setPw(rs.getString("user_pw"));
                dto.setName(rs.getString("user_name"));
                dto.setBirth(rs.getString("user_birth"));
                dto.setEmail(rs.getString("user_email"));
                dto.setTel(rs.getString("user_tel"));
                lists.add(new MemberDTO(dto.getId(),dto.getPw()));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return lists;
    }

    public String userNameList_member(MemberDTO dto) throws SQLException {
        String names = "";
        try {
            getConnection();
            String sql = "SELECT user_name from user where user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getId());
            rs = pstmt.executeQuery();
            while (rs.next())
                names = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return names;
    }

    public String getUserInfo_member(MemberDTO dto) throws SQLException {
        String user_info = "";
        try {
            getConnection();
            String sql = "SELECT user_name,user_birth,user_email,user_tel from user where user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getId());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                dto.setName(rs.getString(1));
                dto.setBirth(rs.getString(2));
                dto.setEmail(rs.getString(3));
                dto.setTel(rs.getString(4));
                user_info = user_info + "user_name:"+dto.getName()
                            + "/user_birth:" + dto.getBirth()
                            + "/user_email:" + dto.getEmail()
                            + "/user_tel:" + dto.getTel();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return user_info;
    }
}



