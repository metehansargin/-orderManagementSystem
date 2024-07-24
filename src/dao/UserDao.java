package dao;

import core.Database;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao {
    private  Connection connection;

    public UserDao() {
        this.connection= Database.getInstance();
    }
    public User findByLogin(String mail, String password){
        User user=null;

        String query="select * from user where mail=? and password=?";//sorgu olusturduk
        try {
            PreparedStatement pr=this.connection.prepareStatement(query);//sorguyu değistirdik
            pr.setString(1, mail);
            pr.setString(2, password);
            ResultSet rs=pr.executeQuery();//değiştirdiğimiz sorguyu çalıştırdık
            if(rs.next()){//geriye dönen eleman varsa ilk elemanını al
                    user=this.match(rs);//ve match işlemini gerçekleştir
            }
            return user;//geriye o değerimi döndür
        } catch (SQLException e) {
        e.printStackTrace();
        }
        return user;
    }
    public ArrayList<User> findAll(){
        ArrayList<User> users=new ArrayList<>();//listenin içine at ve döndür o listeyi
        try {
            ResultSet rs=this.connection.createStatement().executeQuery("SELECT *from user");//bütün userları listelemek isitoruz
            while(rs.next()){//dönen elemaının ilk elemanını al
                users.add(this.match(rs));//ve mactch işlemini gerçekleştir
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public User match(ResultSet rs) throws SQLException {
        User user=new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("mail"));
        return user;
    }
}
