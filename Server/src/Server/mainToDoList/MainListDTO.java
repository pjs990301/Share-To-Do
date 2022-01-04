package Server.mainToDoList;

public class MainListDTO {
    private String roomId;
    private String mainNum;
    private String roomMainId;
    private String mainDo;
    private String newMainDo;
    private String manager;
    private String deadline;
    private String completed;

    public String getRoomMainId() {
        return roomMainId;
    }

    public void setRoomMainId(String roomId, String mainNum) {
        this.roomMainId = roomId + "-" + mainNum;
    }

    public String getMainNum() {
        return mainNum;
    }

    public void setMainNum(String mainNum) {
        this.mainNum = mainNum;
    }

    public String getNewMainDo() {
        return newMainDo;
    }

    public void setNewMainDo(String newMainDo) {
        this.newMainDo = newMainDo;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMainDo() {
        return mainDo;
    }

    public void setMainDo(String mainDo) {
        this.mainDo = mainDo;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String admin) {
        this.manager = admin;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }
}
