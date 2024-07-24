package view;

import core.Helper;
import entity.User;

import javax.swing.*;
import java.awt.*;

public class DashboardUI extends JFrame {

    private JPanel container;
    private JLabel lbl_welcome;
    private User user;
    public DashboardUI(User user) {
        this.user = user;
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

        System.out.println(this.user.toString());

    }
}
