package dao;

import core.Database;
import entity.Basket;
import entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BasketDao {
    private Connection connection;
    private ProductDao productDao;

    public BasketDao() {
        this.connection= Database.getInstance();
        this.productDao = new ProductDao();
    }
    public boolean save(Basket basket) {
        String query = "INSERT INTO basket (product_id) VALUES (?)";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1, basket.getProductId());
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // SQLException durumunda false döndürülmesi daha mantıklı
        }
    }
    public ArrayList<Basket> findAll(){
        ArrayList<Basket> baskets=new ArrayList<>();//listenin içine at ve döndür o listeyi
        try {
            ResultSet rs=this.connection.createStatement().executeQuery("SELECT *from basket");//bütün userları listelemek isitoruz
            while(rs.next()){//dönen elemaının ilk elemanını al
                baskets.add(this.match(rs));//ve mactch işlemini gerçekleştir
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return baskets;
    }
    public boolean clear() {
        String query = "delete from basket ";
        try {
            PreparedStatement pr=this.connection.prepareStatement(query);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Basket match(ResultSet rs) throws SQLException {
        Basket basket=new Basket();
        basket.setId(rs.getInt("id"));
        basket.setProductId(rs.getInt("product_id"));
        basket.setProduct(this.productDao.getById(rs.getInt("product_id")));
        return basket;
    }
}
