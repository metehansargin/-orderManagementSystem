import business.UserController;
import core.Database;
import core.Helper;
import entity.User;
import view.DashboardUI;
import view.LoginUI;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Helper.setTheme();
       // LoginUI loginUI=new LoginUI();
        UserController userController = new UserController();
        User user=userController.findByLogin("metehan@sargın.com","123123");
        DashboardUI dashboardUI=new DashboardUI(user);


    }
}