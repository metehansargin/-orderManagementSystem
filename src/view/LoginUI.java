package view;

import business.UserController;
import core.Helper;
import entity.User;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame{
    private JPanel container;
    private JPanel pni_top;
    private JLabel lbl_title;
    private JPanel pnl_button;
    private JTextField fld_mail;
    private JButton btn_login;
    private JLabel lbl_mail;
    private JLabel lbl_password;
    private JPasswordField fld_password;
    private UserController userController;

    public LoginUI(){
        this.userController=new UserController();
        this.add(container);
        this.setTitle("Musteri Yonetim Sistemi");
        this.setSize(400,400);
        int x=(Toolkit.getDefaultToolkit().getScreenSize().width-this.getWidth())/2;
        int y=(Toolkit.getDefaultToolkit().getScreenSize().height-this.getHeight())/2;
        this.setLocation(x,y);
        this.setVisible(true);

        //Giris butonuna basıldığında
        this.btn_login.addActionListener(e -> {
            JTextField[] checkList = new JTextField[]{this.fld_password,this.fld_mail};
            if(!Helper.isEmailValid(this.fld_mail.getText())){
                Helper.showMsg("Gecerli Bir Eposta Giriniz");
            }else if(Helper.isFieldListEmpty(checkList)){
                Helper.showMsg("fill");
            }
            else{
                User user=this.userController.findByLogin(this.fld_mail.getText(),this.fld_password.getText());
                if(user==null){
                    Helper.showMsg("Girdiginiz bilgilere göre kullanici bulunamadi");
                }
                else
                    this.dispose();

                DashboardUI dashboardUI=new DashboardUI(user);

            }

        });
    }
}
