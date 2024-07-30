package business;

import core.Helper;
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
}
