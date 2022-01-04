package Server.mainToDoList;

import Server.room.RoomDAO;
import Server.room.RoomDTO;

import java.sql.SQLException;

public class MainListError {

    public boolean authorityCheck(String roomId,String user) throws SQLException {
        boolean inUser = false;

        RoomDTO Rdto = new RoomDTO();
        RoomDAO Rdao = new RoomDAO();
        Rdto.setRoomId(roomId);
        String[] usersId = Rdao.userIdList_room(Rdto).split("/");
        for(int i = 0; i < usersId.length;i++){
            if(usersId[i].equals(user)) {
                inUser = true;
                break;
            }
            else
                inUser = false;
        }
        return inUser;
    }

}
