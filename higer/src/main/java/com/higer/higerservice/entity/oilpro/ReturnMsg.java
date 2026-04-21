package com.higer.higerservice.entity.oilpro;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/2 17:29
 * @Description:
 */
public class ReturnMsg {


    /**
     * code : 0
     * errInfo : null
     * data : {"token":null,"date":"2017-06-19","airport":"大连周水子国际机场","delivered":"厦门航空有限公司","filghtNo":"MF8806","aircraftNo":"B5151","aircraftType":"B738","departure":"DLC 大连","transitStop":"--","destination":"HZH 杭州","testBillNo":"2017-354","descriptionAndGrade":"3号喷气燃油","temperature":22,"actualDensity":0.792,"meterStart":500,"meterFinish":7015,"figures":6515,"figuresWords":null,"quantity":5160,"hydrantPitNo":"79.1","vehicleTypeAndNo":"615","timeStart":"2017-06-19 06:40","timeFinish":"2017-06-19 06:54","signPhoto":null,"signName":"李健","id":"123"}
     */

    private String code;
    private Object errInfo;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(Object errInfo) {
        this.errInfo = errInfo;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token : null
         * date : 2017-06-19
         * airport : 大连周水子国际机场
         * delivered : 厦门航空有限公司
         * filghtNo : MF8806
         * aircraftNo : B5151
         * aircraftType : B738
         * departure : DLC 大连
         * transitStop : --
         * destination : HZH 杭州
         * testBillNo : 2017-354
         * descriptionAndGrade : 3号喷气燃油
         * temperature : 22.0
         * actualDensity : 0.792
         * meterStart : 500
         * meterFinish : 7015
         * figures : 6515
         * figuresWords : null
         * quantity : 5160
         * hydrantPitNo : 79.1
         * vehicleTypeAndNo : 615
         * timeStart : 2017-06-19 06:40
         * timeFinish : 2017-06-19 06:54
         * signPhoto : null
         * signName : 李健
         * id : 123
         */

        private Object token;
        private String date;
        private String airport;
        private String delivered;
        private String filghtNo;
        private String aircraftNo;
        private String aircraftType;
        private String departure;
        private String transitStop;
        private String destination;
        private String testBillNo;
        private String descriptionAndGrade;
        private double temperature;
        private double actualDensity;
        private int meterStart;
        private int meterFinish;
        private int figures;
        private Object figuresWords;
        private int quantity;
        private String hydrantPitNo;
        private String vehicleTypeAndNo;
        private String timeStart;
        private String timeFinish;
        private Object signPhoto;
        private String signName;
        private String id;

        public Object getToken() {
            return token;
        }

        public void setToken(Object token) {
            this.token = token;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAirport() {
            return airport;
        }

        public void setAirport(String airport) {
            this.airport = airport;
        }

        public String getDelivered() {
            return delivered;
        }

        public void setDelivered(String delivered) {
            this.delivered = delivered;
        }

        public String getFilghtNo() {
            return filghtNo;
        }

        public void setFilghtNo(String filghtNo) {
            this.filghtNo = filghtNo;
        }

        public String getAircraftNo() {
            return aircraftNo;
        }

        public void setAircraftNo(String aircraftNo) {
            this.aircraftNo = aircraftNo;
        }

        public String getAircraftType() {
            return aircraftType;
        }

        public void setAircraftType(String aircraftType) {
            this.aircraftType = aircraftType;
        }

        public String getDeparture() {
            return departure;
        }

        public void setDeparture(String departure) {
            this.departure = departure;
        }

        public String getTransitStop() {
            return transitStop;
        }

        public void setTransitStop(String transitStop) {
            this.transitStop = transitStop;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getTestBillNo() {
            return testBillNo;
        }

        public void setTestBillNo(String testBillNo) {
            this.testBillNo = testBillNo;
        }

        public String getDescriptionAndGrade() {
            return descriptionAndGrade;
        }

        public void setDescriptionAndGrade(String descriptionAndGrade) {
            this.descriptionAndGrade = descriptionAndGrade;
        }

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public double getActualDensity() {
            return actualDensity;
        }

        public void setActualDensity(double actualDensity) {
            this.actualDensity = actualDensity;
        }

        public int getMeterStart() {
            return meterStart;
        }

        public void setMeterStart(int meterStart) {
            this.meterStart = meterStart;
        }

        public int getMeterFinish() {
            return meterFinish;
        }

        public void setMeterFinish(int meterFinish) {
            this.meterFinish = meterFinish;
        }

        public int getFigures() {
            return figures;
        }

        public void setFigures(int figures) {
            this.figures = figures;
        }

        public Object getFiguresWords() {
            return figuresWords;
        }

        public void setFiguresWords(Object figuresWords) {
            this.figuresWords = figuresWords;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getHydrantPitNo() {
            return hydrantPitNo;
        }

        public void setHydrantPitNo(String hydrantPitNo) {
            this.hydrantPitNo = hydrantPitNo;
        }

        public String getVehicleTypeAndNo() {
            return vehicleTypeAndNo;
        }

        public void setVehicleTypeAndNo(String vehicleTypeAndNo) {
            this.vehicleTypeAndNo = vehicleTypeAndNo;
        }

        public String getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(String timeStart) {
            this.timeStart = timeStart;
        }

        public String getTimeFinish() {
            return timeFinish;
        }

        public void setTimeFinish(String timeFinish) {
            this.timeFinish = timeFinish;
        }

        public Object getSignPhoto() {
            return signPhoto;
        }

        public void setSignPhoto(Object signPhoto) {
            this.signPhoto = signPhoto;
        }

        public String getSignName() {
            return signName;
        }

        public void setSignName(String signName) {
            this.signName = signName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
