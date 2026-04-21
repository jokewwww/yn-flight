package com.ncse.fdds.xmlbean;

import com.ncse.fdds.annotation.JSONNeed;
import lombok.ToString;

@ToString
public class KMGData {
    @JSONNeed
    private Header Header;
    @JSONNeed
    private Body Body;

}
