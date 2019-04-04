package com.tujia.os.lilyth.dao;


import com.tujia.os.lilyth.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author lk
 * @date 2019/4/4
 */
public interface ContractDao extends JpaRepository<Contract,Integer> {
}
