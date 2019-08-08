package controllers;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.junit.Test;

import utils.Utils;

public class MonthControllerTest {

    @Test
    public void getAllDespesas() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Utils.DATAOFMONTH)
                .whereEqualTo("mes", Utils.currentMonth())
                .whereEqualTo("ano", Utils.currentYear()).whereEqualTo("tipo", Utils.DESPESA)
                .whereEqualTo("uid", "uPZMs2nvyR");
        query.orderByDescending("updatedAt").findInBackground((objects, e) -> {
        });
    }
}