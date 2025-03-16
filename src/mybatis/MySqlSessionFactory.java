package mybatis;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MySqlSessionFactory {
    static String jdbcUrl = "jdbc:mysql://localhost:3306/mybatis_db";
    static String username = "root";
    static String password = "12345678";
    public <T> T getMapper(Class<T> mapperClass){
        return (T)Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{mapperClass},new MapperInvocationHandler());
    }

    static class MapperInvocationHandler implements InvocationHandler{

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if(method.getName().startsWith("select")){
                return invokeSelect(proxy,method,args);
            }
            return null;
        }

        private Object invokeSelect(Object proxy, Method method, Object[] args){
                String sql = createSelectSql(method);

//            String sql = "SELECT id,name,age FROM user WHERE id = ?";
            try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                for(int i=0;i<args.length;i++){
                    Object arg = args[i];
                    if(arg instanceof Integer){
                        preparedStatement.setInt(i+1, (int)arg);
                    }else if(arg instanceof String){
                        preparedStatement.setString(i+1, (String)arg);
                    }
                }
                ResultSet rs = preparedStatement.executeQuery(sql);
                if(rs.next()){
                    return parseResult(rs, method.getReturnType());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        private Object parseResult(ResultSet rs, Class<?> returnType) throws Exception {
            Constructor<?> constructor = returnType.getConstructor();
            Object result = constructor.newInstance();
            for(Field field : returnType.getDeclaredFields()){
                Object column =null;
                String name = field.getName();
                if(field.getType()==String.class){
                    column =rs.getString(name);
                }else if(field.getType()==Integer.class){
                    column =rs.getInt(name);
                }
                field.setAccessible(true);
                field.set(result,column);
            }
            return result;
        }

        private String createSelectSql(Method method) {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT ");
            List<String> selectCols = getSelectCols(method.getReturnType());
            sb.append(selectCols);
            sb.append(" FROM ");
            String tableName = getSelectTableName(method.getReturnType());
            sb.append(tableName);
            sb.append(" WHERE ");
            String where = getSelectWhere(method);
            sb.append(where);
            return sb.toString();
        }

        private String getSelectWhere(Method method) {
            return Arrays.stream(method.getParameters()).map(
                    (parameter) -> {
                        Param param = parameter.getAnnotation(Param.class);
                        String value = param.value();
                        String condition = value+" = ?";
                        return condition;
                    }).collect(Collectors.joining(" AND "));
        }

        private String getSelectTableName(Class<?> returnType) {
            Table annotation = returnType.getAnnotation(Table.class);
            if(annotation==null){
                throw new RuntimeException();
            }
            return annotation.tableName();
        }

        private List<String> getSelectCols(Class<?> returnType) {
            Field[] declaredFields = returnType.getDeclaredFields();
            return Arrays.stream(declaredFields).map(field->field.getName()).toList();
        }
    }
    }



