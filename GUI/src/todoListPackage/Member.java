package todoListPackage;

public class Member {
    private String memberName;
    private String memberID;

    public Member(String memberName, String memberID)
    {
        this.memberName = memberName;
        this.memberID = memberID;
    }

    public String GetMemberName()
    {
        return memberName;
    }
    public String GetMemberID()
    {
        return memberID;
    }
}
