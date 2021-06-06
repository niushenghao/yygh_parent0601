package com.lening.yygh.hosp.repository;

import com.lening.yygh.model.hosp.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 牛胜浩
 * @date 2021/5/28 21:17
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {

    Hospital findHospitalByHoscode(String hoscode);

    List<Hospital> findHospitalByHosnameLike(String hosname);
}
