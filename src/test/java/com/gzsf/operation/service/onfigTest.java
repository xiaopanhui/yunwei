package com.gzsf.operation.service;

import com.gzsf.operation.model.ConfigInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Consumer;

@SpringBootTest
@RunWith(SpringRunner.class)
public class onfigTest {
    @Autowired
    private ConfigInfoService configInfoService;
    @Test
    public  void  testGetById(){
        configInfoService.getByConfigInfoId(9L).map(it->{
            System.out.println("查询结果"+it);
            return "";
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
            }
        }).block();
    }
}
