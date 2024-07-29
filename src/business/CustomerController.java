package business;

import core.Helper;
import dao.CustomerDao;
import entity.Customer;

import java.util.ArrayList;

public class CustomerController {
    private final CustomerDao customerDao=new CustomerDao();
    public ArrayList<Customer> findAll(){
        return this.customerDao.findAll();
    }
    public boolean save(Customer customer){
        return this.customerDao.save(customer);
    }
    public Customer findById(int id){
    return this.customerDao.getById(id);
    }
    public boolean update(Customer customer){
        if(this.findById(customer.getId())==null){
            Helper.showMsg(customer.getId()+"Adlı kullanıcı bulanamadı");
            return false;
        }
        return this.customerDao.update(customer);
    }
    public boolean delete(int id){
        if(this.findById(id)==null){
            Helper.showMsg(id+"Adlı kullanıcı silinmedi");
            return false;
        }
        return this.customerDao.delete(id);
    }
    public ArrayList<Customer> filter(String name,Customer.TYPE type){
        //select * from customertwo where LIKE "%TEST%" and type="person"
        //select * from customertwo where LIKE "%TEST%"
        //select * from customertwo where type="person"
        //select * from customertwo
        String query="SELECT * FROM CUSTOMERTWO";
        ArrayList<String> whereList=new ArrayList<>();
        if(name.length()>0){
            whereList.add("name LIKE '%"+name+"%'");
        }
        if(type!=null){
            whereList.add("type ='"+type+"'");
        }
        if(whereList.size()>0){
            String whereQuery=String.join(" AND ",whereList);
            query+=" WHERE "+whereQuery;
        }
        return this.customerDao.query(query);
    }
}
