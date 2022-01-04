package Server.room;

import Server.mainToDoList.MainListDAO;
import Server.mainToDoList.MainListDTO;

import java.sql.SQLException;
import java.util.List;

public class RoomError {

    public boolean pwCheck1(RoomDTO dto) {
        boolean result = false;
        int cnt1 = 0;
        int cnt2 = 0;
        String pw = dto.getPassword();
        for (int i = 0; i < pw.length(); i++) {
            char ch = pw.charAt(i);
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
                cnt1++;
            else if (ch >= '0' && ch <= '9')
                cnt2++;
        }
        if (cnt1 == 0 || cnt2 == 0)
            result = false;
        else
            result = true;

        return result;
    }

    public boolean pwCheck2(RoomDTO dto) {
        boolean result = false;
        String pw = dto.getPassword();
        if (pw.length() < 5 || pw.length() > 15)
            result = false;
        else
            result = true;
        return result;
    }

    public boolean login_room(RoomDTO dto) throws SQLException {
        boolean result = false;
        RoomDAO dao = new RoomDAO();
        List<RoomDTO> lists = dao.getFullList_room();

        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getRoomId().equals(dto.getRoomId()) && lists.get(i).getPassword().equals(dto.getPassword())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean userCheck_room(RoomDTO dto) throws SQLException {
        RoomDAO dao = new RoomDAO();
        boolean result = false;
        String[] users = dao.userIdList_room(dto).split("/");
        //users.lei

        for (int i = 0; i < users.length; i++) {
            if (users[i].equals(dto.getUserId())) {
                result = true;
                break;
            } else
                result = false;
        }
        return result;
    }

    public String isAdmin_room(RoomDTO dto) throws SQLException{
        RoomDAO dao = new RoomDAO();
        String result = "false";
        String admin = null;
        try {
            admin = dao.admin_room(dto);
            if (admin.equals(dto.getUserId())) {
                result = "true";
            }
        } catch (NullPointerException e) {
            result = "task:get_todo_list/response:false/message:room can't found 404./";
        }

        return result;
    }
    public boolean isManager_room(String roomId, String userId) throws SQLException{
        RoomDAO dao = new RoomDAO();
        MainListDAO Mdao = new MainListDAO();
        boolean result = false;
        String manager = Mdao.getManger_topic(roomId, userId);
        if(userId.equals(manager)){
            return true;
        }
        else
            return false;
    }


}
