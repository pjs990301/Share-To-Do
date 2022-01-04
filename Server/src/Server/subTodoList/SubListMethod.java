package Server.subTodoList;

import java.sql.SQLException;

public class SubListMethod {
    SubListDTO dto = new SubListDTO();
    SubListDAO dao = new SubListDAO();
    SubListError error = new SubListError();
    private String res = "";

    public String make_sub(String[] info) throws SQLException {
        synchronized (this) {
            int[] idx = new int[info.length + 1];
            for (int i = 1; i < idx.length - 1; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setMainNum(info[2].substring(idx[2] + 1));
            dto.setRoomMainId(dto.getRoomId(), dto.getMainNum());
            dto.setSubNum(info[3].substring(idx[3] + 1));
            dto.setRoomMainSubId(dto.getRoomMainId(), dto.getSubNum());
            dto.setSubDo(info[4].substring(idx[4] + 1));
            dto.setCompleted("0");

            if (error.authorityCheck(dto.getRoomId(),info[5].substring(idx[5] + 1))) {
                if (dao.insert(dto))
                    res = "task:add_sub_topic/response:true/message:Sub generation has been completed./";
                else
                    res = "task:add_sub_topic/response:false/message:Sub creation failed./";
            }
            else
                res = "task:add_sub_topic/response:false/message:not a room participant./";
            return res;
        }
    }

    public String complete_sub(String[] info) throws SQLException {
        synchronized (this) {
            String res = "";
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setMainNum(info[2].substring(idx[2] + 1));
            dto.setRoomMainId(dto.getRoomId(), dto.getMainNum());
            dto.setSubNum(info[3].substring(idx[3] + 1));
            dto.setRoomMainSubId(dto.getRoomMainId(), dto.getSubNum());
            dto.setCompleted(info[4].substring(idx[4] + 1));

            if (error.authorityCheck(dto.getRoomId(),info[5].substring(idx[5] + 1))) {
                if (dao.completeUpdate_sub(dto))
                    res = "task:set_sub_complete/response:true/message:The sub completion change was successful./";
                else
                    res = "task:set_sub_complete/response:false/message:Sub completion change failed./";
            }
            else
                res = "task:set_sub_complete/response:false/message:not a room participant./";
            return res;
        }
    }

    public String name_sub(String[] info) throws SQLException {
        synchronized (this) {
            String res = "";
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setMainNum(info[2].substring(idx[2] + 1));
            dto.setRoomMainId(dto.getRoomId(), dto.getMainNum());
            dto.setSubNum(info[3].substring(idx[3] + 1));
            dto.setRoomMainSubId(dto.getRoomMainId(), dto.getSubNum());
            dto.setNewsubDo(info[4].substring(idx[4] + 1));
            if (error.authorityCheck(dto.getRoomId(),info[5].substring(idx[5] + 1))) {
                if (dao.nameUpdate_sub(dto))
                    res = "task:set_sub_name/response:true/message:The sub-name change was successful./";
                else
                    res = "task:set_sub_name/response:false/message:Sub-name change failed./";
            } else
                res = "task:set_sub_name/response:false/message:not a room participant./";
            return res;
        }
    }

    public String list_sub(String[] info) throws SQLException {
        synchronized (this){
            String res = "";
            SubListDAO dao = new SubListDAO();
            int[] idx = new int[info.length + 1];
            for (int i = 1; i < idx.length - 1; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setRoomId(info[1].substring(idx[1] + 1));
            dto.setMainNum(info[2].substring(idx[2] + 1));
            dto.setRoomMainId(dto.getRoomId(), dto.getMainNum());
            res = "task:get_sub_todo_list/response:true/" + dao.getSubList_sub(dto) + "message:The sub output was successfully./";
            return res;
        }
    }
}
