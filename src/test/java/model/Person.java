package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/15 22:46
 * Email:kangzhihu@163.com
 * Descriptions:
 */
public class Person {
    private String name;
    private String sex;
    private int age;
    public static String SOURCE_OPENAPI_PARTNER_SH = "openapi_partner_sh";
    private boolean old = false;
    List<String> hobbys=new ArrayList<String>(Arrays.asList("唱歌","跳舞","'!@#$%^&*()、\t11",""));
    private Car car;
    public Person(){}

    public boolean isOld() {
        return old;
    }

    public void setOld(boolean old) {
        this.old = old;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Person getDefaultPerson(){
        Person person = new Person();
        person.age = 18;

        person.sex = "woman";
        person.name = "小丽";
        person.car = new Car("宝马");
        return person;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public List<String> getHobbys() {
        return hobbys;
    }

    public void setHobbys(List<String> hobbys) {
        this.hobbys = hobbys;
    }

}
