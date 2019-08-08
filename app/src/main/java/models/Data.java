package models;

import utils.Utils;

public class Data {

    public String getMes() {
        return Utils.currentMonth();
    }

    public int getAno() {
        return Utils.currentYear();
    }


}
