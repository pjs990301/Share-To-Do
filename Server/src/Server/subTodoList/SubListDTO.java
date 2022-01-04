package Server.subTodoList;

public class SubListDTO {

    private String roomId;
    private String mainNum;
    private String roomMainId;
    private String subNum;
    private String roomMainSubId;
    private String subDo;
    private String newsubDo;
    private String completed;

    public String getNewsubDo() {
        return newsubDo;
    }

    public void setNewsubDo(String newsubDo) {
        this.newsubDo = newsubDo;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMainNum() {
        return mainNum;
    }

    public void setMainNum(String mainNum) {
        this.mainNum = mainNum;
    }

    public String getRoomMainId() {
        return roomMainId;
    }

    public void setRoomMainId(String roomId, String mainNum) {
        this.roomMainId = roomId + "-" + mainNum;
    }

    public String getSubNum() {
        return subNum;
    }

    public void setSubNum(String subNum) {
        this.subNum = subNum;
    }

    public String getRoomMainSubId() {
        return roomMainSubId;
    }

    public void setRoomMainSubId(String roomMainId, String subNum) {
        this.roomMainSubId = roomMainId + "-" + subNum;
    }

    public String getSubDo() {
        return subDo;
    }

    public void setSubDo(String subDo) {
        this.subDo = subDo;
    }

}
