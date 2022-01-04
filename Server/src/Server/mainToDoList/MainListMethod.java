package Server.mainToDoList;

import Server.room.RoomDAO;
import Server.room.RoomDTO;
import Server.room.RoomError;

import java.sql.SQLException;

public class MainListMethod extends Thread {
    MainListDTO dto = new MainListDTO();
    MainListDAO dao = new MainListDAO();
    MainListError error = new MainListError();
    RoomDTO Rdto = new RoomDTO();

    public String make_topic(String[] info) throws SQLException {
        synchronized (this) {
            String res = "";
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setMainNum(info[2].substring(idx[2] + 1));
            dto.setRoomMainId(dto.getRoomId(), dto.getMainNum());
            dto.setMainDo(info[3].substring(idx[3] + 1));
            dto.setManager(info[4].substring(idx[4] + 1));
            dto.setDeadline(info[5].substring(idx[5] + 1));
            dto.setCompleted("0");
            if (error.authorityCheck(dto.getRoomId(),info[6].substring(idx[6] + 1))) {
                if (dao.insert(dto))  //메인 Topic 추가 //메인 Topic 추가 성공
                    res = "task:add_main_topic/response:true/message:Main Topic generation has been completed./";
                else //메인 Topic 추가 실패
                    res = "task:add_main_topic/response:false/message:Main Topic generation failed./";
            }
            else
                res = "task:add_main_topic/response:false/message:not a room participant./";
            return res;
        }
    }

    public String complete_topic(String[] info) throws SQLException {
        synchronized (this) {
            String res = "";
            boolean result = false;
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");

            }
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setMainNum(info[2].substring(idx[2] + 1));
            dto.setCompleted(info[3].substring(idx[3] + 1));
            dto.setRoomMainId(dto.getRoomId(), dto.getMainNum());

            if (error.authorityCheck(dto.getRoomId(), info[4].substring(idx[4] + 1))) {
                if (dao.completeUpdate_topic(dto))
                    res = "task:set_main_complete/response:true/message:The main topic completion change was successful./";
                else
                    res = "task:set_main_complete/response:false/message:Main Topic completion change failed./";
            }else
                res = "task:set_main_complete/response:false/message:not a room participant./";

            return res;
        }
    }

    public String deadline_topic(String[] info) throws SQLException {
        synchronized (this) {
            String res = "";
            boolean result = false;
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");

            }
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setMainNum(info[2].substring(idx[2] + 1));
            dto.setRoomMainId(dto.getRoomId(), dto.getMainNum());
            dto.setDeadline(info[3].substring(idx[3] + 1));

            if (error.authorityCheck(dto.getRoomId(),info[4].substring(idx[4] + 1))) {
                if (dao.deadlineUpdate_topic(dto))
                    res = "task:set_deadline/response:true/message:The main topic deadline change was successful./";
                else
                    res = "task:set_deadline/response:false/message:The main topic deadline change failed./";
            }
            else
                res = "task:set_deadline/response:false/message:not a room participant./";
            return res;
        }
    }

    public String manager_topic(String[] info) throws SQLException {
        synchronized (this) {
            String res = "";
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");

            }
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setMainNum(info[2].substring(idx[2] + 1));
            dto.setRoomMainId(dto.getRoomId(), dto.getMainNum());
            dto.setManager(info[3].substring(idx[3] + 1));
            if (error.authorityCheck(dto.getRoomId(),info[4].substring(idx[4] + 1))) {
                if (dao.managerUpdate_topic(dto))
                    res = "task:set_manager/response:true/message:The change of the main topic representative was successful./";
                else
                    res = "task:set_manager/response:false/message:Main Topic contact person change failed./";
            }
            else
                res = "task:set_manager/response:false/message:not a room participant./";
            return res;
        }
    }

    public String name_topic(String[] info) throws SQLException {
        synchronized (this) {
            String res = "";
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");

            }
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setMainNum(info[2].substring(idx[2] + 1));
            dto.setRoomMainId(dto.getRoomId(), dto.getMainNum());
            dto.setNewMainDo(info[3].substring(idx[3] + 1));
            if (error.authorityCheck(dto.getRoomId(),info[4].substring(idx[4] + 1))) {
                if (dao.nameUpdate_topic(dto))
                    res = "task:set_topic_name/response:true/message:Main Topic name change successfully./";
                else
                    res = "task:set_topic_name/response:false/message:Main Topic name change failed./";
            }
            else
                res = "task:set_topic_name/response:false/message:not a room participant./";
            return res;
        }
    }

    public String list_topic(String[] info) throws SQLException {
        synchronized (this) {
            String res = "";
            RoomDTO Rdto = new RoomDTO();
            RoomDAO Rdao = new RoomDAO();
            RoomError Rerror = new RoomError();
            int[] idx = new int[info.length + 1];
            for (int i = 1; i < idx.length - 1; i++) {
                idx[i] = info[i].indexOf(":");
            }
            Rdto.setRoomId(info[1].substring(idx[1] + 1));
            Rdto.setUserId(info[2].substring(idx[2] + 1));
            dto.setRoomId(info[1].substring(idx[1] + 1));

            String temp = null;
            String adminResult = Rerror.isAdmin_room(Rdto);
            if(error.authorityCheck(dto.getRoomId(),Rdto.getUserId())) {
                if(adminResult.equals("false")||adminResult.equals("true")){
                    temp = "room_name:" + Rdao.getRoomName_room(Rdto) + "/is_admin:" + Rerror.isAdmin_room(Rdto) + dao.getTopicList_topic(dto);
                    res = "task:get_todo_list/response:true/" + temp + "/message:Main Topic output successfully./";
                }
                else
                    res = adminResult;
            }
            else{
                res = "task:get_todo_list/response:false/message:not a room participant./";
            }
            return res;
        }
    }
}

