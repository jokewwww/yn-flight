package com.higer.higerservice.service;

import com.higer.higerservice.entity.oilpro.ASheet;
import com.higer.higerservice.repository.oilpro.ASheetRepository;
import com.higer.higerservice.util.PcResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/2 10:21
 * @Description:
 */
@Service
public class ASheetService {

    @Autowired
    private ASheetRepository aSheetRepository;

    public PcResponseObject<ASheet> findAll(int page) {
        Page<ASheet> all = aSheetRepository.findAll(new PageRequest(page - 1, 10));
        return new PcResponseObject<ASheet>(all);
    }
}
