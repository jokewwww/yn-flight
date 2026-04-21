package com.zh.bean.auto;

import java.io.Serializable;

public class TWell implements Serializable{
    /**
     * 机位号
     */
    private String wellPlacecode;

    /**
     * 地井编号
     */
    private String wellHydrtPitNo;

    /**
     * T_WELL
     */
    private static final long serialVersionUID = 1L;

    /**
     * 机位号
     * @return well_placecode 机位号
     */
    public String getWellPlacecode() {
        return wellPlacecode;
    }

    /**
     * 机位号
     * @param wellPlacecode 机位号
     */
    public void setWellPlacecode(String wellPlacecode) {
        this.wellPlacecode = wellPlacecode == null ? null : wellPlacecode.trim();
    }

    /**
     * 地井编号
     * @return well_hydrt_pit_no 地井编号
     */
    public String getWellHydrtPitNo() {
        return wellHydrtPitNo;
    }

    /**
     * 地井编号
     * @param wellHydrtPitNo 地井编号
     */
    public void setWellHydrtPitNo(String wellHydrtPitNo) {
        this.wellHydrtPitNo = wellHydrtPitNo == null ? null : wellHydrtPitNo.trim();
    }
}
