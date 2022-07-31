package com.hnj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnj.domain.Employee;
import com.hnj.service.EmployeeService;
import com.hnj.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




