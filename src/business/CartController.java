package business;

import dao.CartDao;
import dao.CustomerDao;
import entity.Cart;
import entity.Customer;

import java.util.ArrayList;

public class CartController {
    private final CartDao cartDao=new CartDao();
    public ArrayList<Cart> findAll(){
        return this.cartDao.findAll();
    }
    public boolean save(Cart cart){
        return this.cartDao.save(cart);
    }
}
