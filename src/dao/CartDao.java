package dao;

import core.Database;
import entity.Cart;
import entity.Customer;
import entity.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CartDao {
    private Connection connection;
    private CustomerDao customerDao;
    private ProductDao productDao;

    public CartDao() {
        this.connection= Database.getInstance();
        this.customerDao = new CustomerDao();
        this.productDao = new ProductDao();
    }
    public ArrayList<Cart> findAll(){
        ArrayList<Cart> carts=new ArrayList<>();//listenin içine at ve döndür o listeyi
        try {
            ResultSet rs=this.connection.createStatement().executeQuery("SELECT *from cart");//bütün userları listelemek isitoruz
            while(rs.next()){//dönen elemaının ilk elemanını al
                carts.add(this.match(rs));//ve mactch işlemini gerçekleştir
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carts;
    }
    public boolean save(Cart cart) {
        String query = "INSERT INTO cart (" +
                "customertwo_id, " +
                "product_id, " +
                "price, " +
                "date, " +
                "note" +
                ") VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1, cart.getCustomerId());
            pr.setInt(2, cart.getProductId());
            pr.setInt(3, cart.getPrice());
            pr.setDate(4, Date.valueOf(cart.getDate()));
            pr.setString(5, cart.getNote());
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Cart match(ResultSet rs) throws SQLException {
        Cart cart=new Cart();
        cart.setId(Integer.parseInt(String.valueOf(rs.getInt("id"))));
        cart.setCustomerId(Integer.parseInt(String.valueOf(rs.getInt("customertwo_id"))));
        cart.setProductId(Integer.parseInt(String.valueOf(rs.getInt("product_id"))));
        cart.setPrice(rs.getInt("price"));
        cart.setNote(rs.getString("note"));
        cart.setDate(LocalDate.parse(rs.getString("date")));
        cart.setCustomer(this.customerDao.getById(cart.getCustomerId()));
        cart.setProduct(this.productDao.getById(cart.getProductId()));

        return cart;
    }
}
