package mybatis;

public interface  UserMapper {
    User selectById(@Param("id") int id);
}
