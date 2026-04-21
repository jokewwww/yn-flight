package com.higer.higerservice.repository.oilpro;

import com.higer.higerservice.entity.oilpro.AAiImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/5 09:46
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface AAiImgRepository extends JpaRepository<AAiImg, Serializable>, JpaSpecificationExecutor<AAiImg> {


    List<AAiImg> findByLogoCode(String code);
}
