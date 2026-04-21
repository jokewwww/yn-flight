package com.higer.oildataexchange.service;

import com.higer.oildataexchange.common.RedisSendUtils;
import com.higer.oildataexchange.entity.flight.TStaff;
import com.higer.oildataexchange.entity.oil.TFuelRecpt;
import com.higer.oildataexchange.repository.TStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SendRedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TStaffRepository staffRepository;

    public void sendRedisMsg(TFuelRecpt tFuelRecpt) {
        tFuelRecpt.setFlrcSign(null);
        tFuelRecpt.setFlrcSingle(null);
        List<TStaff> staffTypeList = staffRepository.findAll(
                (Specification<TStaff>) (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate staffType = criteriaBuilder.equal(root.<String>get("staffType"), 2);
                    criteriaQuery.where(staffType);
                    return criteriaQuery.getRestriction();
                });
        List<String> collect = staffTypeList.stream().map(TStaff::getStaffId).collect(Collectors.toList());
        collect.add(tFuelRecpt.getFlrcDeliverId());
        String to = String.join(",", collect);
        RedisSendUtils.testDingYue(redisTemplate, to, "", "200", tFuelRecpt);
    }
}
