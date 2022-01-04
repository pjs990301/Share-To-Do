package Server.Member;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MemberError {

    //ID형식 오류 확인
    public int idFormat(String id) {
        int result = 0;
        //아이디가 5~15자이내로 한정
        if (id.length() < 5 || id.length() > 15) {
            result = 1;
        }
        //영어와 숫자 혼용 판단
        int cnt1 = 0;
        int cnt2 = 0;
        //for문을 통해 문자확인
        for (int i = 0; i < id.length(); i++) {
            char ch = id.charAt(i);
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
                cnt1++;
            else if (ch >= '0' && ch <= '9')
                cnt2++;
        }

        if (cnt1 == 0 || cnt2 == 0)
            // cnt1=0 혹은 cnt2=0인 경우는 문자나 숫자가 혼용되지 않은 경우
            result = 2;
        return result;
    }

    //비밀번호 오류 확인
    public int pwCheck(String pw) {
        int result = 0;
        if (pw.length() < 5 || pw.length() > 15) {
            result = 1;
        }
        int cnt1 = 0;
        int cnt2 = 0;

        //비밀번호 영문자 혼용
        for (int i = 0; i < pw.length(); i++) {
            char ch = pw.charAt(i);
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
                cnt1++;
            else if (ch >= '0' && ch <= '9')
                cnt2++;
        }
        if (cnt1 == 0 || cnt2 == 0)
            result = 2;

        return result;
    }

    //이름 영어 오류 확인
    public boolean nameCheck(String name) {
        boolean result = false;
        result = Pattern.matches("^[a-zA-Z]*$", name);
        return result;
    }

    //생년월일 오류 확인 oooo-oo-oo
    public boolean birthdayCheck(String birth) {
        boolean result = false;
        result = Pattern.matches("^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$", birth);
        return result;
    }

    //이메일 오류 확인 ooo@0000.0000
    public boolean emailCheck(String email) {
        boolean result = false;
        result = Pattern.matches("\\w+@\\w+\\.\\w+(\\.\\w+)?", email);
        return result;
    }

    //전화번호 오류 확인 OOO-OOO-OOOO OR OOO-OOOO-OOOO
    public boolean phoneCheck(String tel) {
        boolean result = false;
        result = Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", tel);
        return result;
    }

    //login 확인 ID, PW의 존재 여부 확인
    public boolean login(MemberDTO dto) throws SQLException{
        boolean result = false;
        MemberDAO dao = new MemberDAO();
        List<MemberDTO> lists = dao.getList();

        for(int i = 0; i<lists.size();i++){
            if(lists.get(i).getId().equals(dto.getId()) && lists.get(i).getPw().equals(dto.getPw())){
                result = true;
            }
        }
        return result;
    }
}
