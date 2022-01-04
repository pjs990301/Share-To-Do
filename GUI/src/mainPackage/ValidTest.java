package mainPackage;

import javax.swing.*;
import java.util.regex.Pattern;

public class ValidTest {
    public static boolean idFormat(String str) {
        boolean result = true;
        //아이디가 5~15자이내로 한정
        if (str.length() < 5 || str.length() > 15) {
            JOptionPane.showMessageDialog(null, "Write ID within 5 to 15 characters.", "Invalid Format", JOptionPane.ERROR_MESSAGE);
            result = false;
        }
        //영어와 숫자 혼용 판단
        int cnt1 = 0;
        int cnt2 = 0;
        //for문을 통해 문자확인
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
                cnt1++;
            else if (ch >= '0' && ch <= '9')
                cnt2++;
        }

        if (cnt1 == 0 || cnt2 == 0) {
            // cnt1=0 혹은 cnt2=0인 경우는 문자나 숫자가 혼용되지 않은 경우
            JOptionPane.showMessageDialog(null, "Mix English and numbers.", "Invalid Format", JOptionPane.ERROR_MESSAGE);
            result = false;
        }

        return result;
    }

    //비밀번호 오류 확인
    public static boolean pwCheck(String pw1, String pw2) {
        int cnt1 = 0;
        int cnt2 = 0;
        boolean result = true;

        //비밀번호 영문자 혼용
        for (int i = 0; i < pw1.length(); i++) {
            char ch = pw1.charAt(i);
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
                cnt1++;
            else if (ch >= '0' && ch <= '9')
                cnt2++;
        }

        if (pw1.length() < 5 || pw1.length() > 15) {
            JOptionPane.showMessageDialog(null, "Write ID within 5 to 15 characters.", "Invalid Format", JOptionPane.ERROR_MESSAGE);
            result = false;
        }
        if (cnt1 == 0 || cnt2 == 0) {
            JOptionPane.showMessageDialog(null, "Mix English and numbers.", "Invalid Format", JOptionPane.ERROR_MESSAGE);
            result = false;
        }
        //비밀번호와 비밀번호 입력이 다른 경우
        if (!pw1.equals(pw2)) {
            JOptionPane.showMessageDialog(null, "The passwords are different.", "Password Error", JOptionPane.ERROR_MESSAGE);
            result = false;
        }

        return result;
    }

    //이름 한글 오류 확인
    public static boolean nameCheck(String name) {
        boolean result = false;
        result = Pattern.matches("^[a-zA-Z]*$", name);
        if(!result)
            JOptionPane.showMessageDialog(null, "Please write name in Korean.", "Invalid Format", JOptionPane.ERROR_MESSAGE);
        return result;
    }

    //생년월일 오류 확인 oooo-oo-oo
    public static boolean birthdayCheck(String birth) {
        boolean result = false;
        result = Pattern.matches("^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$", birth);
        if(!result)
            JOptionPane.showMessageDialog(null, "Please enter it in the form of yyyy-MM-dd or yyyyMMdd.", "Invalid Format", JOptionPane.ERROR_MESSAGE);
        return result;
    }

    //이메일 오류 확인 ooo@0000.0000
    public static boolean emailCheck(String email) {
        boolean result = false;
        result = Pattern.matches("\\w+@\\w+\\.\\w+(\\.\\w+)?", email);
        if(!result)
            JOptionPane.showMessageDialog(null, "Please enter it in the form of ooo@ooo.ooo", "Invalid Format", JOptionPane.ERROR_MESSAGE);
        return result;
    }

    //전화번호 오류 확인 OOO-OOO-OOOO OR OOO-OOOO-OOOO
    public static boolean phoneCheck(String tel) {
        boolean result = false;
        result = Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", tel);
        if(!result)
            JOptionPane.showMessageDialog(null, "Please enter it in the form of 000-0000-0000 or 000-000-0000", "Invalid Format", JOptionPane.ERROR_MESSAGE);
        return result;
    }

    public static boolean roomIDCheck(String id){
        boolean result = true;
        result = Pattern.matches("^[0-9]*$", id);
        if(!result)
            JOptionPane.showMessageDialog(null, "Please contain numbers only.", "Invalid Format", JOptionPane.ERROR_MESSAGE);

        return result;
    }

    public static boolean CheckAllConditions(String id, String pw, String pwCheck, String name, String birth, String email, String tel)
    {
        boolean idResult = idFormat(id);
        boolean pwResult = pwCheck(pw, pwCheck);
        boolean nameResult = nameCheck(name);
        boolean birthResult = birthdayCheck(birth);
        boolean emailResult = emailCheck(email);
        boolean phoneResult = phoneCheck(tel);

        if(idResult && pwResult && nameResult && birthResult && emailResult && phoneResult)
            return true;

        return false;
    }
}
