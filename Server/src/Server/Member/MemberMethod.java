package Server.Member;

import java.sql.SQLException;
import java.util.List;

public class MemberMethod extends Thread {

    MemberDTO dto = new MemberDTO();
    MemberDAO dao = new MemberDAO();
    MemberError error = new MemberError();
    private String res = "";
    private String content = "";


    public String make_user(String[] info) throws SQLException {
        synchronized (this) {
            int check = 0;
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setId(info[1].substring(idx[1] + 1));
            dto.setPw(info[2].substring(idx[2] + 1));
            dto.setName(info[3].substring(idx[3] + 1));
            dto.setBirth(info[4].substring(idx[4] + 1));
            dto.setEmail(info[5].substring(idx[5] + 1));
            dto.setTel(info[6].substring(idx[6] + 1));

            //ID 유효성 검사
            if (error.idFormat(dto.getId()) == 0)
                check++;
            else if (error.idFormat(dto.getId()) == 1)
                content = "Please set the ID between 5 and 15 letters./";
            else if (error.idFormat(dto.getId()) == 2)
                content = "Please set your ID by mixing English and numbers./";
            else
                content = "There was an error related to the ID./";

            //비밀번호 유효성 검사
            if (error.pwCheck(dto.getPw()) == 0)
                check++;
            else if (error.pwCheck(dto.getPw()) == 1)
                content = "Please set the password between 5 and 15 letters./;";
            else if (error.pwCheck(dto.getPw()) == 2)
                content = "Please set the password using both English and numbers./";
            else
                content = "There was an error related to the password./";

            //이름 유효성 검사
            if (error.nameCheck(dto.getName()))
                check++;
            else
                content = "Please set your name in English./";

            //생년월일 유효성 검사
            if (error.birthdayCheck(dto.getBirth()))
                check++;
            else
                content = "Please enter the date of birth as OOOO-OO-OO./";

            //이메일 오류 확인
            if (error.emailCheck(dto.getEmail()))
                check++;
            else
                content = "Please enter the e-mail as OO@OO.OO./";

            //전화번호 유효성 검사
            if (error.phoneCheck(dto.getTel()))
                check++;
            else
                content = "Please enter your phone number as OOO-OO-OO or OOO-OO-OO-OO./";

            if (check == 6) {
                if (dao.insert(dto))
                    res = "task:join_in/response:true/message:Membership success./";
                else
                    res = "task:join_in/response:false/message:" + content;
            }
            return res;
        }
    }

    public String update_user(String[] info) throws SQLException {
        synchronized (this) {
            int check = 0;
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setId(info[1].substring(idx[1] + 1));
            dto.setPw(info[2].substring(idx[2] + 1));
            dto.setNewpw(info[3].substring(idx[3] + 1));
            dto.setName(info[4].substring(idx[4] + 1));
            dto.setBirth(info[5].substring(idx[5] + 1));
            dto.setEmail(info[6].substring(idx[6] + 1));
            dto.setTel(info[7].substring(idx[7] + 1));

            //비밀번호 유효성 검사
            if (dto.getPw() != dto.getNewpw()) {
                if (error.pwCheck(dto.getNewpw()) == 0)
                    check++;
                else if (error.pwCheck(dto.getPw()) == 1)
                    content = "Please set the password between 5 and 15 letters./";
                else if (error.pwCheck(dto.getNewpw()) == 2)
                    content = "Please set the password by mixing English and numbers./";
                else
                    content = "There was an error related to the password./";
            } else
                content = "The password you want to change is the same as the existing password./";

            //이름 유효성 검사
            if (error.nameCheck(dto.getName()))
                check++;
            else
                content = "Please set your name in English./";

            //생년월일 유효성 검사
            if (error.birthdayCheck(dto.getBirth()))
                check++;
            else
                content = "Please enter the date of birth as OOOO-OO-OO./";

            //이메일 오류 확인
            if (error.emailCheck(dto.getEmail()))
                check++;
            else
                content = "Please enter the e-mail as OO@OO.OO./";

            //전화번호 유효성 검사
            if (error.phoneCheck(dto.getTel()))
                check++;
            else
                content = "Please enter your phone number as OOO-OO-OO or OOO-OO-OO-OO./";

            if (check == 5) {
                if (dao.update(dto))
                    res = "task:set_user_info/response:true/message:Membership information modification is completed./";
                else
                    res = "task:set_user_info/response:false/message:" + content;
            }
            return res;
        }
    }

    public String login(String[] info) throws SQLException {
        synchronized (this) {
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setId(info[1].substring(idx[1] + 1));
            dto.setPw(info[2].substring(idx[2] + 1));

            if (error.login(dto)) {
                res = "task:login/response:true/message:Welcome./";
            } else {
                res = "task:login/response:false/message:Please check the ID and PW./";
            }
            return res;
        }
    }


    public String getInfo_user(String[] info) throws SQLException {
        synchronized (this) {
            int[] idx = new int[info.length];
            for (int i = 1; i < idx.length; i++) {
                idx[i] = info[i].indexOf(":");
            }
            dto.setId(info[1].substring(idx[1] + 1));
            res = "task:get_user_info/response:true/" + dao.getUserInfo_member(dto) + "/message:User information output was successful./";
        }
        return res;
    }
}
