package mybatis;

public class Main {
    public static void main(String[] args) {
        MySqlSessionFactory mySqlSessionFactory = new MySqlSessionFactory();
        UserMapper mapper = mySqlSessionFactory.getMapper(UserMapper.class);
        User user = mapper.selectById(1);
        System.out.println(user);
    }
}
