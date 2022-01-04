package Server.room;

import Server.Member.MemberDAO;
import Server.Member.MemberDTO;

import java.sql.SQLException;

public class RoomMethod extends Thread {

    RoomDTO dto = new RoomDTO();
    RoomDAO dao = new RoomDAO();
    RoomError error = new RoomError();
    String res = "";

    public String make_room(String[] info) throws SQLException {
        synchronized (this) {
            int[] idx = new int[info.length + 1];
            for (int i = 1; i < idx.length - 1; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setRoom_name(info[1].substring(idx[1] + 1));
            dto.setRoomId(info[2].substring(idx[2] + 1));
            dto.setPassword(info[3].substring(idx[3] + 1));
            dto.setAdmin(info[4].substring(idx[4] + 1));
            if (error.pwCheck1(dto) && error.pwCheck2(dto)) { //room PW format 확인 (한영혼용) & (5글자 ~ 15글자)
                if (dao.insert(dto))//room 추가  //room 추가 성공
                    res = "task:room_establish/response:true/message:Successfully opened a room./";
                else  // room 추가 실패
                    res = "task:room_establish/response:false/message:Failed to open a room./";
            } else //room PW format 유형 불일치
                res = "task:room_establish/response:false/message:the opening of the room, please make the password between English and numerals and between 5 and 15 letters./";
            return res;
        }
    }

    public String delete_room(String[] info) throws SQLException {
        synchronized (this) {
            int[] idx = new int[info.length + 1];
            for (int i = 1; i < idx.length - 1; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setRoomId(info[1].substring(idx[1] + 1));

            if (dao.delete(dto.getRoomId()))
                res = "task:delete_room/response:true/message:Successfully deleted a room./";
            else
                res = "task:delete_room/response:false/message:Failed to delete a room./";

        }
        return res;
    }

    public String update_room(String[] info) throws SQLException {
        synchronized (this) {
            int[] idx = new int[info.length + 1];
            for (int i = 1; i < idx.length - 1; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setPassword(info[2].substring(idx[2] + 1));
            dto.setNewpassword(info[3].substring(idx[3] + 1));
            dto.setRoom_name(info[4].substring(idx[4] + 1));

            if (dto.getPassword() != dto.getNewpassword()) {
                if (dao.update(dto))
                    res = "task:set_room_info/response:true/message:Successfully update a room./";
                else
                    res = "task:set_room_info/response:false/message:Failed to update a room./";
            } else
                res = "task:set_room_info/response:false/message:It's the same as the existing password./";

            return res;
        }
    }

    public String login_room(String[] info) throws SQLException {
        synchronized (this) {
            int[] idx = new int[info.length + 1];
            for (int i = 1; i < idx.length - 1; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setPassword(info[2].substring(idx[2] + 1));
            dto.setUserId(info[3].substring(idx[3] + 1));
            if (error.login_room(dto)) {
                String users = dao.userIdList_room(dto); //현재 해당 Room의 userID 추출
                if (error.userCheck_room(dto) == false) { //과거 Room 로그인 여부 확인
                    if (dao.enter_room(dto, users)) { //DB Room에 userID를 추가
                        res = "task:room_login/response:true/message:Successfully login a room./"; //추가 성공
                    }
                    else
                        res = "task:room_login/response:false/message:Failed to login a room.!/"; //추가 실패
                } else //과거 Room로그인 여부 존재 DB userID 추가 X
                    res = "task:room_login/response:true/message:Successfully login a room./";
            } else //roomID 혹은 roomPW 불일치 로그인 실패
                res = "task:room_login/response:false/message:Failed to login a room./";
            return res;
        }
    }

    public String getInfo_room(String[] info) throws SQLException {
        synchronized (this) {
            int[] idx = new int[info.length + 1];
            for (int i = 1; i < idx.length - 1; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setRoomId(info[1].substring(idx[1] + 1));
            res = "task:get_room_info/response:true/room_name:" + dao.getRoomName_room(dto) + "/message:Successfully Room name check./";
            return res;
        }
    }

    public String getMemberList_room(String[] info) throws SQLException {
        synchronized (this) {
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");
            }
            RoomDTO dto = new RoomDTO();
            MemberDAO Mdao = new MemberDAO();
            MemberDTO Mdto = new MemberDTO();
            dto.setRoomId(info[1].substring(idx[1] + 1));
            String[] usersId = dao.userIdList_room(dto).split("/");
            String[] usersName = new String[usersId.length];

            for (int i = 0; i < usersId.length; i++) {
                Mdto.setId(usersId[i]);
                usersName[i] = Mdao.userNameList_member(Mdto);
            }
            String temp = "";
            for (int i = 0; i < usersId.length; i++) {
               temp = temp +"user_name:"+usersName[i] +"/user_id:"+usersId[i] + "/";
            }
            res = "task:get_member_list/response:true/" + temp + "message:Successfully user information check./";
        }
        return res;
    }

    public String memberOut_room(String[] info) throws SQLException {
        synchronized (this) {
            String temp = "";
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");
            }
            RoomDTO dto = new RoomDTO();
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setUserId(info[2].substring(idx[2] + 1));
            String[] usersId = dao.userIdList_room(dto).split("/");

            for (int i = 0; i < usersId.length; i++) {
                if (usersId[i].equals(dto.getUserId()))
                    continue;
                else
                    temp = temp + usersId[i] + "/";
            }

            if (dao.outUserUpdate_room(dto, temp))
                res = "task:member_out/response:true/message:Successfully user kick out./";
            else
                res = "task:member_out/response:false/message:Failed to kick out user./";

            return res;
        }
    }

    public String leave_room(String[] info) throws SQLException {
        synchronized (this) {
            String temp = "";
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");
            }
            RoomDTO dto = new RoomDTO();
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setUserId(info[2].substring(idx[2] + 1));
            String[] usersId = dao.userIdList_room(dto).split("/");
            for (int i = 0; i < usersId.length; i++) {
                if (usersId[i].equals(dto.getUserId()))
                    continue;
                else
                    temp = temp + usersId[i] + "/";
            }
            if(error.isManager_room(dto.getRoomId(),dto.getUserId()) == false) {
                if (dao.outUserUpdate_room(dto, temp))
                    res = "task:member_out/response:true/message:Successfully user drop out of room./";
                else
                    res = "task:member_out/response:false/message:Failed user drop out of room./";
            }
            else
                if(dao.leaveUpdate_room(dto))
                    res = "task:member_out/response:true/message:Successfully user leave of room./";
                else
                    res = "task:member_out/response:false/message:Failed user leave of room./";

        }
        return res;
    }
}

