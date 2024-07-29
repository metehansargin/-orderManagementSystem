package view;

import business.CustomerController;
import core.Helper;
import entity.Customer;
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
    private User user;
    private  CustomerController customerController;
    private DefaultTableModel tmdl_customer=new DefaultTableModel();
    private JPopupMenu popup_customer=new JPopupMenu();


    public DashboardUI(User user) {
        this.user = user;
        this.customerController = new CustomerController();
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

    }
    private void loadCustomerButtonEvent() {
        this.btn_customer_new.addActionListener(e->{
        CustomerUI customerUI=new CustomerUI(new Customer());
        customerUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {//burda base e yeni karakter eklediğimizde anında gözükmesi için
                                                    //güncelleme yapıyoruz ve database e geliyor yeni girdiğimiz data
                loadCustomerTable(null);
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
                }
            });

       });

        this.popup_customer.add("Sil").addActionListener(e -> {
            int selectId= Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0).toString());
            if(Helper.confirm("sure")){
                if(this.customerController.delete(selectId)) {
                    Helper.showMsg("done");
                    loadCustomerTable(null);
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
