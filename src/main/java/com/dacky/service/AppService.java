package com.dacky.service;


import com.dacky.config.Constants;
import com.dacky.config.DataRepository;
import com.dacky.entity.Product;
import com.dacky.entity.Temp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AppService {
    @Autowired
    private DataRepository dataRepository;

    @Scheduled(fixedRate = 5000)
    public void run() {
        List<Temp> temps = dataRepository.getAllTempData();
        int size = temps.size();
        for (int i = 0; i < size; i++) {
            switch (temps.get(i).getQueryType()) {
                case Constants.QUERY_INSERT:
                    System.out.println("insert *************");
                    if (!dataRepository.insertToDBEnd(temps.get(i))) {
                        temps.remove(i);
                        i--; size--;
                    }
//                    deleteTemp(resultSetTemp, connect);
                    break;
                case Constants.QUERY_UPDATE:
                    System.out.println("update *************");
//                    update(resultSetTemp, connect);
//                    deleteTemp(resultSetTemp, connect);
                    break;
                case Constants.QUERY_DELETE:
                    System.out.println("delete *************");
//                    deleteEnd(resultSetTemp, connect);
//                    deleteTemp(resultSetTemp, connect);
                    break;

                default:
                    System.out.println("invalid *************");
                    break;
            }
            dataRepository.delete(temps);

        }
    }
}


