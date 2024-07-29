package dao;

import core.Database;
import entity.Customer;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDao {
    private Connection connection;

    public CustomerDao() {
        this.connection= Database.getInstance();
    }
    public ArrayList<Customer> findAll(){
        ArrayList<Customer> customers=new ArrayList<>();//listenin içine at ve döndür o listeyi
        try {
            ResultSet rs=this.connection.createStatement().executeQuery("SELECT *from customertwo");//bütün userları listelemek isitoruz
            while(rs.next()){//dönen elemaının ilk elemanını al
                customers.add(this.match(rs));//ve mactch işlemini gerçekleştir
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    public boolean save(Customer customer) {
        String query = "insert into customertwo" +
                "(" +
                "name," +
                "type," +
                "phone," +
                "mail," +
                "address" +
                ") " +
                "values(?,?,?,?,?)";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setString(1, customer.getName());
            pr.setString(2, customer.getType().toString());
            pr.setString(3, customer.getPhone());
            pr.setString(4, customer.getMail());
            pr.setString(5, customer.getAddress());
             return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public Customer getById(int id){
        Customer customer=null;
        String query = "select * from customertwo where id=?";
        try {
            PreparedStatement pr=this.connection.prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs=pr.executeQuery();
            if(rs.next()){
                customer=this.match(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
    public boolean update(Customer customer) {
        String query="update customertwo set" +
                " name=?" +
                ",type=?" +
                ",phone=?" +
                ",mail=?" +
                ",address=?" +
                " where id=?";
        try {
            PreparedStatement pr=this.connection.prepareStatement(query);
            pr.setString(1, customer.getName());
            pr.setString(2, customer.getType().toString());
            pr.setString(3, customer.getPhone());
            pr.setString(4, customer.getMail());
            pr.setString(5, customer.getAddress());
            pr.setInt(6, customer.getId());
            return pr.executeUpdate() != -1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }
    public boolean delete(int id) {
        String query = "delete from customertwo where id=?";
        try {
            PreparedStatement pr=this.connection.prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public ArrayList<Customer> query(String query) {
        ArrayList<Customer> customers=new ArrayList<>();

        try {
            ResultSet rs = this.connection.createStatement().executeQuery(query);
            while (rs.next()){
                customers.add(this.match(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;

    }
    public Customer match(ResultSet rs) throws SQLException {
        Customer customer=new Customer();
        customer.setId(Integer.parseInt(String.valueOf(rs.getInt("id"))));
        customer.setName(rs.getString("name"));
        customer.setMail(rs.getString("mail"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setType(Customer.TYPE.valueOf(rs.getString("type")));


        return customer;
    }
}
