/**
 * 
 */
package com.zh.bean.flight;

import java.io.Serializable;

/**
 * @author licm
 *
 */
public class FlightIn  implements Serializable{
    private static final long serialVersionUID = 1L;

    private String timestamp = null;
    private String opration = null;

	/** 航班唯一??? */
    private String flgt_ffid = null;
    /** 所属机?代? */
    private String flgt_airport_code = null;
    /** 航班号 */
    private String flgt_flno = null;
    /** 航班日期 */
    private String flgt_flop = null;
    /** ?机?型 */
    private String flgt_acname = null;
    /** ?机号? */
    private String flgt_regn = null;
    /** 机位 */
    private String flgt_placecode = null;
    /** 航空公司二字? */
    private String flgt_al2c = null;
    /** ??到??? */
    private String flgt_a_stot = null;
    /** ??到??? */
    private String flgt_a_etot = null;
    /** ??到??? */
    private String flgt_a_atot = null;
    /** ??起??? */
    private String flgt_d_stot = null;
    /** ??起??? */
    private String flgt_d_etot = null;
    /** ??起??? */
    private String flgt_d_atot = null;
    /** 出?地机?三字? */
    private String flgt_org3c = null;
    /** 目的地机?三字? */
    private String flgt_des3c = null;
    /** ??港 */
    private String flgt_adid = null;
    /** 航班任?属性 */
    private String flgt_mission_prop = null;
    /** 航班性? */
    private String flgt_nature = null;
    /** 航班性??分 */
    private String flgt_sub_nature = null;
    /** 航班国内/国? */
    private String flgt_flti = null;
    /** 航班状? */
    private String flgt_ftyp = null;
    /** 延?原因 */
    private String flgt_delaycode = null;
    /** 航空服?代理 */
    private String flgt_proxy = null;
    /** ?接航班号 */
    private String flgt_link_flno = null;
    /** ?港?道 */
    private String flgt_dep_runway = null;
    /** 到港?道 */
    private String flgt_arr_runway = null;
    /** 登机? */
    private String flgt_gate = null;
    /** 上?档?? */
    private String flgt_chocks_in = null;
    /** 撤???? */
    private String flgt_chocks_out = null;
    /** 第一件行李?? */
    private String flgt_first_lugg = null;
    /** 最后一件行李?? */
    private String flgt_last_lugg = null;
    /**
     * 航线
     */
    private String flgt_vialc = null;
    /**
     * 经停机场三字码1
     */
    private String flgtTrs3c1 = null;

    /**
     * 经停机场1
     */
    private String flgtTrsnm1 = null;
    /**
     * 经停机场三字码2
     */
    private String flgtTrs3c2 = null;
    
    /**
     * 经停机场2
     */
    private String flgtTrsnm2 = null;
    /**
     * 经停机场三字码3
     */
    private String flgtTrs3c3 = null;
    
    /**
     * 经停机场3
     */
    private String flgtTrsnm3 = null;
    /**
     * 经停机场三字码4
     */
    private String flgtTrs3c4 = null;
    
    /**
     * 经停机场4
     */
    private String flgtTrsnm4 = null;
    /**
     * 经停机场三字码5
     */
    private String flgtTrs3c5 = null;
    
    /**
     * 经停机场5
     */
    private String flgtTrsnm5 = null;
    /**
     * 经停机场三字码6
     */
    private String flgtTrs3c6 = null;
    
    /**
     * 经停机场6
     */
    private String flgtTrsnm6 = null;
    
    /**
     * 主航班号
     */
    private String mFlightNo = null;
    /**
     * 连接航班日期
     */
    private String flgt_link_flop= null;
    
    /**
     * 连接航班连接次数
     */
    private String flgt_link_repeat= null;
    
    /**
     * 航班连接次数
     */
    private String flgt_repeat= null;
    
    /**
     * 航班航段
     */
    private String flgt_otc= null;
    
    /**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the opration
	 */
	public String getOpration() {
		return opration;
	}

	/**
	 * @param opration the opration to set
	 */
	public void setOpration(String opration) {
		this.opration = opration;
	}

    /**
	 * @return the mFlightNo
	 */
	public String getmFlightNo() {
		return mFlightNo;
	}

	/**
	 * @param mFlightNo the mFlightNo to set
	 */
	public void setmFlightNo(String mFlightNo) {
		this.mFlightNo = mFlightNo;
	}

	public String getFlgtTrs3c2() {
		return flgtTrs3c2;
	}

	public void setFlgtTrs3c2(String flgtTrs3c2) {
		this.flgtTrs3c2 = flgtTrs3c2;
	}

	public String getFlgtTrsnm2() {
		return flgtTrsnm2;
	}

	public void setFlgtTrsnm2(String flgtTrsnm2) {
		this.flgtTrsnm2 = flgtTrsnm2;
	}

	public String getFlgtTrs3c3() {
		return flgtTrs3c3;
	}

	public void setFlgtTrs3c3(String flgtTrs3c3) {
		this.flgtTrs3c3 = flgtTrs3c3;
	}

	public String getFlgtTrsnm3() {
		return flgtTrsnm3;
	}

	public void setFlgtTrsnm3(String flgtTrsnm3) {
		this.flgtTrsnm3 = flgtTrsnm3;
	}

	public String getFlgtTrs3c4() {
		return flgtTrs3c4;
	}

	public void setFlgtTrs3c4(String flgtTrs3c4) {
		this.flgtTrs3c4 = flgtTrs3c4;
	}

	public String getFlgtTrsnm4() {
		return flgtTrsnm4;
	}

	public void setFlgtTrsnm4(String flgtTrsnm4) {
		this.flgtTrsnm4 = flgtTrsnm4;
	}

	public String getFlgtTrs3c5() {
		return flgtTrs3c5;
	}

	public void setFlgtTrs3c5(String flgtTrs3c5) {
		this.flgtTrs3c5 = flgtTrs3c5;
	}

	public String getFlgtTrsnm5() {
		return flgtTrsnm5;
	}

	public void setFlgtTrsnm5(String flgtTrsnm5) {
		this.flgtTrsnm5 = flgtTrsnm5;
	}

	public String getFlgtTrs3c6() {
		return flgtTrs3c6;
	}

	public void setFlgtTrs3c6(String flgtTrs3c6) {
		this.flgtTrs3c6 = flgtTrs3c6;
	}

	public String getFlgtTrsnm6() {
		return flgtTrsnm6;
	}

	public void setFlgtTrsnm6(String flgtTrsnm6) {
		this.flgtTrsnm6 = flgtTrsnm6;
	}

	/**
     * 经停机场三字码1
     */
    public String getFlgtTrs3c1() {
		return flgtTrs3c1;
	}
    
    /**
     * 经停机场三字码1
     */
	public void setFlgtTrs3c1(String flgtTrs3c1) {
		this.flgtTrs3c1 = flgtTrs3c1;
	}

	/**
     * 经停机场1
     */
	public String getFlgtTrsnm1() {
		return flgtTrsnm1;
	}

	/**
     * 经停机场1
     */
	public void setFlgtTrsnm1(String flgtTrsnm1) {
		this.flgtTrsnm1 = flgtTrsnm1;
	}

	/**
     * 航班唯一???を取得する.
     * 
     * @return 航班唯一???
     */
    public String getFlgt_ffid() {
        return flgt_ffid;
    }

    /**
	 * @return the flgt_vialc
	 */
	public String getFlgt_vialc() {
		return flgt_vialc;
	}

	/**
	 * @param flgt_vialc the flgt_vialc to set
	 */
	public void setFlgt_vialc(String flgt_vialc) {
		this.flgt_vialc = flgt_vialc;
	}

	/**
     * 所属机?代?を取得する.
     * 
     * @return 所属机?代?
     */
    public String getFlgt_airport_code() {
        return flgt_airport_code;
    }

    /**
     * 航班号を取得する.
     * 
     * @return 航班号
     */
    public String getFlgt_flno() {
        return flgt_flno;
    }

    /**
     * 航班日期を取得する.
     * 
     * @return 航班日期
     */
    public String getFlgt_flop() {
        return flgt_flop;
    }

    /**
     * ?机?型を取得する.
     * 
     * @return ?机?型
     */
    public String getFlgt_acname() {
        return flgt_acname;
    }

    /**
     * ?机号?を取得する.
     * 
     * @return ?机号?
     */
    public String getFlgt_regn() {
        return flgt_regn;
    }

    /**
     * 机位を取得する.
     * 
     * @return 机位
     */
    public String getFlgt_placecode() {
        return flgt_placecode;
    }

    /**
     * 航空公司二字?を取得する.
     * 
     * @return 航空公司二字?
     */
    public String getFlgt_al2c() {
        return flgt_al2c;
    }

    /**
     * ??到???を取得する.
     * 
     * @return ??到???
     */
    public String getFlgt_a_stot() {
        return flgt_a_stot;
    }

    /**
     * ??到???を取得する.
     * 
     * @return ??到???
     */
    public String getFlgt_a_etot() {
        return flgt_a_etot;
    }

    /**
     * ??到???を取得する.
     * 
     * @return ??到???
     */
    public String getFlgt_a_atot() {
        return flgt_a_atot;
    }

    /**
     * ??起???を取得する.
     * 
     * @return ??起???
     */
    public String getFlgt_d_stot() {
        return flgt_d_stot;
    }

    /**
     * ??起???を取得する.
     * 
     * @return ??起???
     */
    public String getFlgt_d_etot() {
        return flgt_d_etot;
    }

    /**
     * ??起???を取得する.
     * 
     * @return ??起???
     */
    public String getFlgt_d_atot() {
        return flgt_d_atot;
    }

    /**
     * 出?地机?三字?を取得する.
     * 
     * @return 出?地机?三字?
     */
    public String getFlgt_org3c() {
        return flgt_org3c;
    }

    /**
     * 目的地机?三字?を取得する.
     * 
     * @return 目的地机?三字?
     */
    public String getFlgt_des3c() {
        return flgt_des3c;
    }

    /**
     * ??港を取得する.
     * 
     * @return ??港
     */
    public String getFlgt_adid() {
        return flgt_adid;
    }

    /**
     * 航班任?属性を取得する.
     * 
     * @return 航班任?属性
     */
    public String getFlgt_mission_prop() {
        return flgt_mission_prop;
    }

    /**
     * 航班性?を取得する.
     * 
     * @return 航班性?
     */
    public String getFlgt_nature() {
        return flgt_nature;
    }

    /**
     * 航班性??分を取得する.
     * 
     * @return 航班性??分
     */
    public String getFlgt_sub_nature() {
        return flgt_sub_nature;
    }

    /**
     * 航班国内/国?を取得する.
     * 
     * @return 航班国内/国?
     */
    public String getFlgt_flti() {
        return flgt_flti;
    }

    /**
     * 航班状?を取得する.
     * 
     * @return 航班状?
     */
    public String getFlgt_ftyp() {
        return flgt_ftyp;
    }

    /**
     * 延?原因を取得する.
     * 
     * @return 延?原因
     */
    public String getFlgt_delaycode() {
        return flgt_delaycode;
    }

    /**
     * 航空服?代理を取得する.
     * 
     * @return 航空服?代理
     */
    public String getFlgt_proxy() {
        return flgt_proxy;
    }

    /**
     * ?接航班号を取得する.
     * 
     * @return ?接航班号
     */
    public String getFlgt_link_flno() {
        return flgt_link_flno;
    }

    /**
     * ?港?道を取得する.
     * 
     * @return ?港?道
     */
    public String getFlgt_dep_runway() {
        return flgt_dep_runway;
    }

    /**
     * 到港?道を取得する.
     * 
     * @return 到港?道
     */
    public String getFlgt_arr_runway() {
        return flgt_arr_runway;
    }

    /**
     * 登机?を取得する.
     * 
     * @return 登机?
     */
    public String getFlgt_gate() {
        return flgt_gate;
    }

    /**
     * 上?档??を取得する.
     * 
     * @return 上?档??
     */
    public String getFlgt_chocks_in() {
        return flgt_chocks_in;
    }

    /**
     * 撤????を取得する.
     * 
     * @return 撤????
     */
    public String getFlgt_chocks_out() {
        return flgt_chocks_out;
    }

    /**
     * 第一件行李??を取得する.
     * 
     * @return 第一件行李??
     */
    public String getFlgt_first_lugg() {
        return flgt_first_lugg;
    }

    /**
     * 最后一件行李??を取得する.
     * 
     * @return 最后一件行李??
     */
    public String getFlgt_last_lugg() {
        return flgt_last_lugg;
    }

    /**
     * 航班唯一???を設定する.
     * 
     * @param flgt_ffid 航班唯一???
     */
    public void setFlgt_ffid(String flgt_ffid) {
        this.flgt_ffid = flgt_ffid;
    }

    /**
     * 所属机?代?を設定する.
     * 
     * @param flgt_airport_code 所属机?代?
     */
    public void setFlgt_airport_code(String flgt_airport_code) {
        this.flgt_airport_code = flgt_airport_code;
    }

    /**
     * 航班号を設定する.
     * 
     * @param flgt_flno 航班号
     */
    public void setFlgt_flno(String flgt_flno) {
        this.flgt_flno = flgt_flno;
    }

    /**
     * 航班日期を設定する.
     * 
     * @param flgt_flop 航班日期
     */
    public void setFlgt_flop(String flgt_flop) {
        this.flgt_flop = flgt_flop;
    }

    /**
     * ?机?型を設定する.
     * 
     * @param flgt_acname ?机?型
     */
    public void setFlgt_acname(String flgt_acname) {
        this.flgt_acname = flgt_acname;
    }

    /**
     * ?机号?を設定する.
     * 
     * @param flgt_regn ?机号?
     */
    public void setFlgt_regn(String flgt_regn) {
        this.flgt_regn = flgt_regn;
    }

    /**
     * 机位を設定する.
     * 
     * @param flgt_placecode 机位
     */
    public void setFlgt_placecode(String flgt_placecode) {
        this.flgt_placecode = flgt_placecode;
    }

    /**
     * 航空公司二字?を設定する.
     * 
     * @param flgt_al2c 航空公司二字?
     */
    public void setFlgt_al2c(String flgt_al2c) {
        this.flgt_al2c = flgt_al2c;
    }

    /**
     * ??到???を設定する.
     * 
     * @param flgt_a_stot ??到???
     */
    public void setFlgt_a_stot(String flgt_a_stot) {
        this.flgt_a_stot = flgt_a_stot;
    }

    /**
     * ??到???を設定する.
     * 
     * @param flgt_a_etot ??到???
     */
    public void setFlgt_a_etot(String flgt_a_etot) {
        this.flgt_a_etot = flgt_a_etot;
    }

    /**
     * ??到???を設定する.
     * 
     * @param flgt_a_atot ??到???
     */
    public void setFlgt_a_atot(String flgt_a_atot) {
        this.flgt_a_atot = flgt_a_atot;
    }

    /**
     * ??起???を設定する.
     * 
     * @param flgt_d_stot ??起???
     */
    public void setFlgt_d_stot(String flgt_d_stot) {
        this.flgt_d_stot = flgt_d_stot;
    }

    /**
     * ??起???を設定する.
     * 
     * @param flgt_d_etot ??起???
     */
    public void setFlgt_d_etot(String flgt_d_etot) {
        this.flgt_d_etot = flgt_d_etot;
    }

    /**
     * ??起???を設定する.
     * 
     * @param flgt_d_atot ??起???
     */
    public void setFlgt_d_atot(String flgt_d_atot) {
        this.flgt_d_atot = flgt_d_atot;
    }

    /**
     * 出?地机?三字?を設定する.
     * 
     * @param flgt_org3c 出?地机?三字?
     */
    public void setFlgt_org3c(String flgt_org3c) {
        this.flgt_org3c = flgt_org3c;
    }

    /**
     * 目的地机?三字?を設定する.
     * 
     * @param flgt_des3c 目的地机?三字?
     */
    public void setFlgt_des3c(String flgt_des3c) {
        this.flgt_des3c = flgt_des3c;
    }

    /**
     * ??港を設定する.
     * 
     * @param flgt_adid ??港
     */
    public void setFlgt_adid(String flgt_adid) {
        this.flgt_adid = flgt_adid;
    }

    /**
     * 航班任?属性を設定する.
     * 
     * @param flgt_mission_prop 航班任?属性
     */
    public void setFlgt_mission_prop(String flgt_mission_prop) {
        this.flgt_mission_prop = flgt_mission_prop;
    }

    /**
     * 航班性?を設定する.
     * 
     * @param flgt_nature 航班性?
     */
    public void setFlgt_nature(String flgt_nature) {
        this.flgt_nature = flgt_nature;
    }

    /**
     * 航班性??分を設定する.
     * 
     * @param flgt_sub_nature 航班性??分
     */
    public void setFlgt_sub_nature(String flgt_sub_nature) {
        this.flgt_sub_nature = flgt_sub_nature;
    }

    /**
     * 航班国内/国?を設定する.
     * 
     * @param flgt_flti 航班国内/国?
     */
    public void setFlgt_flti(String flgt_flti) {
        this.flgt_flti = flgt_flti;
    }

    /**
     * 航班状?を設定する.
     * 
     * @param flgt_ftyp 航班状?
     */
    public void setFlgt_ftyp(String flgt_ftyp) {
        this.flgt_ftyp = flgt_ftyp;
    }

    /**
     * 延?原因を設定する.
     * 
     * @param flgt_delaycode 延?原因
     */
    public void setFlgt_delaycode(String flgt_delaycode) {
        this.flgt_delaycode = flgt_delaycode;
    }

    /**
     * 航空服?代理を設定する.
     * 
     * @param flgt_proxy 航空服?代理
     */
    public void setFlgt_proxy(String flgt_proxy) {
        this.flgt_proxy = flgt_proxy;
    }

    /**
     * ?接航班号を設定する.
     * 
     * @param flgt_link_flno ?接航班号
     */
    public void setFlgt_link_flno(String flgt_link_flno) {
        this.flgt_link_flno = flgt_link_flno;
    }

    /**
     * ?港?道を設定する.
     * 
     * @param flgt_dep_runway ?港?道
     */
    public void setFlgt_dep_runway(String flgt_dep_runway) {
        this.flgt_dep_runway = flgt_dep_runway;
    }

    /**
     * 到港?道を設定する.
     * 
     * @param flgt_arr_runway 到港?道
     */
    public void setFlgt_arr_runway(String flgt_arr_runway) {
        this.flgt_arr_runway = flgt_arr_runway;
    }

    /**
     * 登机?を設定する.
     * 
     * @param flgt_gate 登机?
     */
    public void setFlgt_gate(String flgt_gate) {
        this.flgt_gate = flgt_gate;
    }

    /**
     * 上?档??を設定する.
     * 
     * @param flgt_chocks_in 上?档??
     */
    public void setFlgt_chocks_in(String flgt_chocks_in) {
        this.flgt_chocks_in = flgt_chocks_in;
    }

    /**
     * 撤????を設定する.
     * 
     * @param flgt_chocks_out 撤????
     */
    public void setFlgt_chocks_out(String flgt_chocks_out) {
        this.flgt_chocks_out = flgt_chocks_out;
    }

    /**
     * 第一件行李??を設定する.
     * 
     * @param flgt_first_lugg 第一件行李??
     */
    public void setFlgt_first_lugg(String flgt_first_lugg) {
        this.flgt_first_lugg = flgt_first_lugg;
    }

    /**
     * 最后一件行李??を設定する.
     * 
     * @param flgt_last_lugg 最后一件行李??
     */
    public void setFlgt_last_lugg(String flgt_last_lugg) {
        this.flgt_last_lugg = flgt_last_lugg;
    }

	/**
	 * @return the flgt_link_flop
	 */
	public String getFlgt_link_flop() {
		return flgt_link_flop;
	}

	/**
	 * @param flgt_link_flop the flgt_link_flop to set
	 */
	public void setFlgt_link_flop(String flgt_link_flop) {
		this.flgt_link_flop = flgt_link_flop;
	}

	/**
	 * @return the flgt_link_repeat
	 */
	public String getFlgt_link_repeat() {
		return flgt_link_repeat;
	}

	/**
	 * @param flgt_link_repeat the flgt_link_repeat to set
	 */
	public void setFlgt_link_repeat(String flgt_link_repeat) {
		this.flgt_link_repeat = flgt_link_repeat;
	}

	/**
	 * @return the flgt_repeat
	 */
	public String getFlgt_repeat() {
		return flgt_repeat;
	}

	/**
	 * @param flgt_repeat the flgt_repeat to set
	 */
	public void setFlgt_repeat(String flgt_repeat) {
		this.flgt_repeat = flgt_repeat;
	}

	/**
	 * @return the flgt_otc
	 */
	public String getFlgt_otc() {
		return flgt_otc;
	}

	/**
	 * @param flgt_otc the flgt_otc to set
	 */
	public void setFlgt_otc(String flgt_otc) {
		this.flgt_otc = flgt_otc;
	}
	
}
