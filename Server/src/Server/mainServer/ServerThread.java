package Server.mainServer;

import Server.Member.MemberMethod;
import Server.mainToDoList.MainListMethod;
import Server.room.RoomMethod;
import Server.subTodoList.SubListMethod;

import java.io.*;
import java.lang.Thread;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerThread extends Thread {
    private Socket socket = null;
    private BufferedReader in = null;
    private BufferedWriter out = null;
    private static String FilePath = "src/Server.log";

    MemberMethod Member = new MemberMethod();
    RoomMethod Room = new RoomMethod();
    MainListMethod mainList = new MainListMethod();
    SubListMethod subList = new SubListMethod();
    SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
    Date time = new Date();
    String time1 = format1.format(time);
    String inputMessage = "", res = "";
    BufferedWriter bw = new BufferedWriter(new FileWriter(FilePath,true));

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "EUC_KR"));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "EUC_KR"));
            bw.append(time1 + "\n");
            bw.append(socket.getRemoteSocketAddress() +" Connection complete\n");
            bw.flush();
            System.out.println(time1);
            System.out.println(socket.getRemoteSocketAddress() + " Connection complete");

            while (true) {
                inputMessage = in.readLine();
                //ex)task:login/id:asd/pw:1q2w3e4r!/"
                bw.append(time1 + "\n");
                bw.append(socket.getRemoteSocketAddress()+"\n");
                bw.append(inputMessage+ "\n");
                bw.flush();

                System.out.println(inputMessage);
                String[] info = inputMessage.split("/");
                //info[0]="task:login info[1]="id:asd" info[2]="pw:1q2w3e4r!"

                //task의 전달 내용에 따라서 작업을 수행
                switch (info[0]) {
                    case "task:login": //task:login/id/pw/
                        res = Member.login(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:join_in"://task:signup/id/pw1/pw2/name/birthday/email/tel
                        res = Member.make_user(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:room_login"://task:loginRoom/roomid/password
                        res = Room.login_room(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:room_establish"://task:makeRoom/roomid/password/admin
                        res = Room.make_room(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:get_todo_list"://task:get_todo_list/roomid/userid
                        res = mainList.list_topic(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:get_sub_todo_list":
                        res = subList.list_sub(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:add_main_topic"://task:makeMainToDoList/roomid/mainnum/name/position/manager/maindo/deadline
                        res = mainList.make_topic(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:add_sub_topic":
                        res = subList.make_sub(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:set_main_complete" :
                        res = mainList.complete_topic(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:set_sub_complete" :
                        res = subList.complete_sub(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:set_deadline":
                        res = mainList.deadline_topic(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:set_manager":
                        res = mainList.manager_topic(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:set_topic_name":
                        res = mainList.name_topic(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:set_sub_name":
                        res = subList.name_sub(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:leave_room":
                        res = Room.leave_room(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:get_room_info":
                        res = Room.getInfo_room(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:set_room_info":
                        res = Room.update_room(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:delete_room":
                        res = Room.delete_room(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:get_member_list":
                        res = Room.getMemberList_room(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:member_out":
                        res = Room.memberOut_room(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:set_user_info":
                        res = Member.update_user(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    case "task:get_user_info":
                        res = Member.getInfo_user(info);
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                    default:
                        res = "Invalid command.";
                        System.out.println(res);
                        out.write(res + "\n");
                        out.flush();
                        bw.append(res + "\n");
                        bw.flush();
                        break;
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            res = "task:get_todo_list/response:false/message:room can't found 404./";
            System.out.println(res);
            try {
                out.write(res + "\n");
                out.flush();
                bw.append(res + "\n");
                bw.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
}
