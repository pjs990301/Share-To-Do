package Server.room;

public class RoomDTO {
    private String roomId;
    private String password;
    private String newpassword;
    private String userId;
    private String room_name;
    private String admin;

    public RoomDTO(String roomId, String room_name, String password, String admin) {
        this.roomId = roomId;
        this.room_name = room_name;
        this.password = password;
        this.admin = admin;
    }

    public RoomDTO() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
