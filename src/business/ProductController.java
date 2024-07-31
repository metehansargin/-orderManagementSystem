package business;

import core.Helper;
import core.Item;
import dao.CustomerDao;
import dao.ProductDao;
import entity.Customer;
import entity.Product;

import java.util.ArrayList;

public class ProductController {
    private final ProductDao productDao=new ProductDao();
    public ArrayList<Product> findAll(){
        return this.productDao.findAll();
    }
    public boolean save(Product product){
        return this.productDao.save(product);
    }
    public Product getByID(int id){
        return this.productDao.getById(id);
    }
    public boolean update(Product product){
        if(this.getByID(product.getId())==null){
            Helper.showMsg(product.getId()+"ID kayıtlı ürün bulanamadı");
            return false;
        }
        return this.productDao.update(product);
    }
    public boolean delete(int id){
        if(this.getByID(id)==null){
            Helper.showMsg(id+"Adlı kullanıcı silinmedi");
            return false;
        }
        return this.productDao.delete(id);
    }
    public ArrayList<Product> filter(String name, String code, Item isstock) {
        String query = "SELECT * FROM product";
        ArrayList<String> whereList = new ArrayList<>();

        if ( name.length() > 0) {
            whereList.add("name LIKE '%" + name + "%'");
        }
        if (code.length() > 0) {
            whereList.add("code LIKE '%" + code + "%'");
        }
        if (isstock != null) {
            if (isstock.getKey() == 1) {
                whereList.add("stock > 0");
            } else {
                whereList.add("stock <= 0");
            }
        }

        if (whereList.size() > 0) {
            String query2 = String.join(" AND ", whereList);
            query += " WHERE " + query2;
        }

        return this.productDao.query(query);
    }

}
