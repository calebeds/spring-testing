package com.calebe.springcontext.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class CountingService {
    private DataSource dataSource;

    @Autowired
    public CountingService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int getCount() {
        return dataSource.getAll().size();
    }
}
