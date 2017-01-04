package bean;

/**
 * Created by Administrator on 2016/12/29.
 */

public class Student {
    private String name;
    private String sex;
    private String hobby;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public Student(String name,String sex,String hobby){
        this.name = name;
        this.sex = sex;
        this.hobby = hobby;
    }

}
