package com.youkeda.application.ebusiness.service;

import com.youkeda.application.ebusiness.model.Result;
import com.youkeda.application.ebusiness.model.User;
import org.springframework.stereotype.Service;



public interface UserService {

    Result<User>register(User user);

    Result<User>login(String usrName,String pwd);
}
