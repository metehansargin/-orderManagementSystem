import core.Database;
import core.Helper;
import view.LoginUI;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Helper.setTheme();
        LoginUI loginUI=new LoginUI();
    }
}