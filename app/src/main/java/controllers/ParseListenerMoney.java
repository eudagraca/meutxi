package controllers;

import com.parse.ParseException;

import java.util.List;

public interface ParseListenerMoney {

    void moneyOnWallet(List<Double> mCash);

    void onDataRetrieveFailed(ParseException e);
}