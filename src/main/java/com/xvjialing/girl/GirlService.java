package com.xvjialing.girl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GirlService {

    @Autowired
    private GirlRepository girlRepository;

    @Transactional
    public void insertTwo(){
        Girl girlA=new Girl();
        girlA.setAge(11);
        girlA.setName("dwdw");
        girlRepository.save(girlA);

        Girl girlB=new Girl();
        girlB.setAge(11);
        girlB.setName("dwdw");
        girlRepository.save(girlB);
    }
}
