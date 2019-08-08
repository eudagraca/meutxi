package controllers;

import java.text.ParseException;
import java.util.List;

import models.Balanco;

public interface ParseBalances {

    void readBalances(List<Balanco> balancos);

    void error(ParseException e);
}
