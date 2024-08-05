package view;

import business.BasketController;
import business.CustomerController;
import business.ProductController;
import core.Helper;
import core.Item;
import entity.Basket;
import entity.Customer;
import entity.Product;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DashboardUI extends JFrame {

    private JPanel container;
    private JPanel pnl_top;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JTabbedPane tab_menu;
    private JPanel pnl_customer;
    private JScrollPane scrl_customer;
    private JTable tbl_customer;
    private JPanel pnl_customer_filter;
    private JTextField fld_f_customer_name;
    private JComboBox<Customer.TYPE> cmb_f_customer_type;
    private JButton btn_customer_filter;
    private JButton btn_customer_filter_reset;
    private JButton btn_customer_new;
    private JLabel lbl_f_customer_name;
    private JLabel lbl_f_customer_type;
    private JPanel pnl_product;
    private JScrollPane scrl_product;
    private JPanel pnl_filter_product;
    private JTextField fld_f_product_name;
    private JTextField fld_f_product_code;
    private JComboBox<Item> cmb_product_stock;
    private JButton btn_product_filter;
    private JButton btn_product_reset;
    private JButton btn_product_new;
    private JLabel lbl_f_product_name;
    private JLabel lbl_f_product_code;
    private JLabel lbl_f_product_stock;
    private JTable tbl_product;
    private JPanel pnl_basket;
    private JPanel pnl_basket_top;
    private JScrollPane scrl_basket;
    private JComboBox<Item> cmb_basket_customer;
    private JButton btn_basket_reset;
    private JButton btn_basket_new;
    private JLabel lbl_basket_price;
    private JLabel lbl_basket_count;
    private JTable tbl_basket;
    private User user;
    private CustomerController customerController;
    private ProductController productController;
    private BasketController basketController;
    private DefaultTableModel tmdl_customer=new DefaultTableModel();
    private DefaultTableModel tmdl_product=new DefaultTableModel();
    private DefaultTableModel tmdl_basket=new DefaultTableModel();
    private JPopupMenu popup_customer=new JPopupMenu();
    private JPopupMenu popup_product=new JPopupMenu();

    public DashboardUI(User user) {
        this.user = user;
        this.customerController = new CustomerController();
        this.productController = new ProductController();
        this.basketController = new BasketController();
        if(user == null) {
            Helper.showMsg("error");
            dispose();
        }

        this.add(container);
        this.setTitle("Musteri Yonetim Sistemi");
        this.setSize(1000,500);
        int x=(Toolkit.getDefaultToolkit().getScreenSize().width-this.getWidth())/2;
        int y=(Toolkit.getDefaultToolkit().getScreenSize().height-this.getHeight())/2;
        this.setLocation(x,y);
        this.setVisible(true);
        this.lbl_welcome.setText("Hosgeldiniz Sayın "+this.user.getName());
        this.btn_logout.addActionListener(e->{
                dispose();
                LoginUI loginUI=new LoginUI();
        });
        //CUSTOMER TAB
        loadCustomerTable(null);
        loadCustomerPopupMenu();
        loadCustomerButtonEvent();
        this.cmb_f_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));
        this.cmb_f_customer_type.setSelectedItem(null);

        //PRODUCT TAB
        loadProductTable(null);
        loadProductPopupMenu();
        loadProductButtonEvent();
        this.cmb_product_stock.addItem(new Item(1,"Stokta Var"));
        this.cmb_product_stock.addItem(new Item(2,"Stokta Yok"));
        this.cmb_product_stock.setSelectedItem(null);

        //BASKET TAB
        loadBasketTable();
        loadBasketButtonEvent();
        loadCustomerBasketCombo();
        this.btn_basket_new.addActionListener(e->{
            Item selectedCustomer = (Item) cmb_basket_customer.getSelectedItem();
            if(selectedCustomer == null) {
                Helper.showMsg("Lütfen bir müsteri seçiniz");
            }
            else{
                Customer customer = this.customerController.findById(selectedCustomer.getKey());
                ArrayList<Basket> baskets=this.basketController.findAll();
                if(customer.getId() == 0) {
                    Helper.showMsg("Böyle bir müşteri bulunamadı");
                }
                else if (baskets.size()==0){
                    Helper.showMsg("Lütfen sepete ürün ekleyiniz");
                }
                else {
                    CartUI cartUI=new CartUI(customer);

                }
            }
        });

    }
    private void loadCustomerBasketCombo(){
        ArrayList<Customer>customers=this.customerController.findAll();
        this.cmb_basket_customer.removeAllItems();
        for(Customer customer : customers){
            int comboKey=customer.getId();
            String comboValue=customer.getName();
            this.cmb_basket_customer.addItem(new Item(comboKey,comboValue));
        }
        this.cmb_basket_customer.setSelectedItem(null);
    }
    private void loadBasketButtonEvent(){
        this.btn_basket_reset.addActionListener(e->{
            if(this.basketController.clear()){
                Helper.showMsg("done");
                loadBasketTable();
            }
            else{
                Helper.showMsg("error");
            }
        });
    }
    private void loadBasketTable() {
        Object[]colomonBasket={"ID","Ürün Adı","Ürün kodu","Fiyat","Stok"};
        ArrayList<Basket> baskets=this.basketController.findAll();

        //Tablo sıfırlama
        DefaultTableModel clearmodel=(DefaultTableModel)this.tbl_basket.getModel();
        clearmodel.setRowCount(0);
        this.tmdl_basket.setColumnIdentifiers(colomonBasket);
        int totalPrice=0;

        for(Basket basket1 : baskets) {
            Object[]rowObject= {
                    basket1.getId(),
                    basket1.getProduct().getName(),
                    basket1.getProduct().getCode(),
                    basket1.getProduct().getPrice(),
                    basket1.getProduct().getStock()
            };
            this.tmdl_basket.addRow(rowObject);
            totalPrice+=basket1.getProduct().getPrice();
        }
        this.lbl_basket_price.setText(totalPrice+" TL ");
        this.lbl_basket_count.setText(baskets.size()+" Adet ");

        this.tbl_basket.setModel(this.tmdl_basket);
        this.tbl_basket.getTableHeader().setReorderingAllowed(false);
        this.tbl_basket.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_basket.setEnabled(false);//düzeltilemez db deki bilgiler

    }

    private void loadProductButtonEvent(){
        this.btn_product_new.addActionListener(e->{
            ProductUI productUI=new ProductUI(new Product());
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                }
            });
        });
        this.btn_product_reset.addActionListener(e -> {
            loadProductTable(null);
            this.fld_f_product_name.setText(null);
            this.fld_f_product_code.setText(null);
            this.cmb_product_stock.setSelectedItem(null);

        });
        this.btn_product_filter.addActionListener(e -> {
            ArrayList<Product> filteredProducts=this.productController.filter(
                    this.fld_f_product_name.getText(),
                    this.fld_f_product_code.getText(),
                    (Item)this.cmb_product_stock.getSelectedItem()
            );
            loadProductTable(filteredProducts);

        });

    }
    private void loadProductTable(ArrayList<Product> products) {
        Object[]colomnProduct={"ID","Ürün Adı","Ürün kodu","Fiyat","Stok"};

        if(products == null) {
            products=this.productController.findAll();
        }
        //Tablo sıfırlama
        DefaultTableModel clearmodel=(DefaultTableModel)this.tbl_product.getModel();
        clearmodel.setRowCount(0);
        this.tmdl_product.setColumnIdentifiers(colomnProduct);
        for(Product product : products) {
            Object[]rowObject= {
                    product.getId(),
                    product.getName(),
                    product.getCode(),
                    product.getPrice(),
                    product.getStock(),
            };
            this.tmdl_product.addRow(rowObject);
        }
        this.tbl_product.setModel(this.tmdl_product);
        this.tbl_product.getTableHeader().setReorderingAllowed(false);
        this.tbl_product.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_product.setEnabled(false);//düzeltilemez db deki bilgiler

    }
    private void loadProductPopupMenu() {
        this.tbl_product.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_product.rowAtPoint(e.getPoint());
                tbl_product.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });


        this.popup_product.add("Güncelle").addActionListener(e->{
            int selectId= Integer.parseInt(this.tbl_product.getValueAt(this.tbl_product.getSelectedRow(), 0).toString());
            ProductUI productUI=new ProductUI(this.productController.getByID(selectId));
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                    loadBasketTable();
                }
            });


        });
        this.popup_product.add("Sepete Ekle").addActionListener(e->{
            int selectId= Integer.parseInt(this.tbl_product.getValueAt(this.tbl_product.getSelectedRow(), 0).toString());
            Product basketProduct=this.productController.getByID(selectId);
            if(basketProduct.getStock() <=0) {
                Helper.showMsg("Bu Ürün Stokta Yoktur");
            }
            else{
                Basket basket=new Basket(basketProduct.getId());
               if(this.basketController.save(basket)){
                   Helper.showMsg("done");
                   loadBasketTable();
               }
               else{
                   Helper.showMsg("error");
               }
            }
        });
        this.popup_product.add("Sil").addActionListener(e->{
            int selectId= Integer.parseInt(this.tbl_product.getValueAt(this.tbl_product.getSelectedRow(), 0).toString());
            if(Helper.confirm("sure")){
                if(this.productController.delete(selectId)) {
                    Helper.showMsg("done");
                    loadProductTable(null);
                    loadBasketTable();
                }
                else{
                    Helper.showMsg("error");
                }
            }

        });

        this.tbl_product.setComponentPopupMenu(this.popup_product);
    }
    private void loadCustomerButtonEvent() {
        this.btn_customer_new.addActionListener(e->{
        CustomerUI customerUI=new CustomerUI(new Customer());
        customerUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {//burda base e yeni karakter eklediğimizde anında gözükmesi için
                                                    //güncelleme yapıyoruz ve database e geliyor yeni girdiğimiz data
                loadCustomerTable(null);
                loadCustomerBasketCombo();

            }
        });

        });
        this.btn_customer_filter.addActionListener(e -> {
            ArrayList<Customer> filteredCustomers=this.customerController.filter(
                    this.fld_f_customer_name.getText(),
                    (Customer.TYPE) this.cmb_f_customer_type.getSelectedItem()
            );
            loadCustomerTable(filteredCustomers);
        });
        this.btn_customer_filter_reset.addActionListener(e -> {
                loadCustomerTable(null);
                this.fld_f_customer_name.setText(null);
                this.cmb_f_customer_type.setSelectedItem(null);
        });

    }
    private void loadCustomerPopupMenu() {
        this.tbl_customer.addMouseListener(new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_customer.rowAtPoint(e.getPoint());
                tbl_customer.setRowSelectionInterval(selectedRow, selectedRow);
          }
        });
       this.popup_customer.add("Güncelle").addActionListener(e -> {//tablodaki kayıtların id lerini döndürür
           int selectId= Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0).toString());
           CustomerUI customerUI=new CustomerUI(this.customerController.findById(selectId));
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadCustomerBasketCombo();
                }
            });

       });

        this.popup_customer.add("Sil").addActionListener(e -> {
            int selectId= Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0).toString());
            if(Helper.confirm("sure")){
                if(this.customerController.delete(selectId)) {
                    Helper.showMsg("done");
                    loadCustomerTable(null);
                    loadCustomerBasketCombo();

                }
                else{
                    Helper.showMsg("error");
                }
            }
        });

        this.tbl_customer.setComponentPopupMenu(this.popup_customer);
    }
    private void loadCustomerTable(ArrayList<Customer> customers) {
        Object[]colomnCustomer={"ID","Müşteri adi","Tipi","Telefon","E-posta","Adres"};

        if(customers == null) {
            customers=this.customerController.findAll();
        }
        //Tablo sıfırlama
        DefaultTableModel clearmodel=(DefaultTableModel)this.tbl_customer.getModel();
        clearmodel.setRowCount(0);
        this.tmdl_customer.setColumnIdentifiers(colomnCustomer);
        for(Customer customer : customers) {
            Object[]rowObject={
                    customer.getId(),
                    customer.getName(),
                    customer.getType(),
                    customer.getPhone(),
                    customer.getMail(),
                    customer.getAddress()};
            this.tmdl_customer.addRow(rowObject);
        }
        this.tbl_customer.setModel(this.tmdl_customer);
        this.tbl_customer.getTableHeader().setReorderingAllowed(false);
        this.tbl_customer.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_customer.setEnabled(false);//düzeltilemez db deki bilgiler

    }

}
