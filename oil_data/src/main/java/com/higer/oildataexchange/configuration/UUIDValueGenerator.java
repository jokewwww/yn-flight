package com.higer.oildataexchange.configuration;

import com.higer.oildataexchange.common.UUIDUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;

public class UUIDValueGenerator extends IdentityGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) {
        return UUIDUtils.getUUID32();
    }
}
