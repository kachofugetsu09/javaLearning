package mybatis;

import jdk.jfr.DataAmount;

@Table(tableName = "user")
public class User {
    private Integer id;
    private String name;
    private Integer age;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }
}
