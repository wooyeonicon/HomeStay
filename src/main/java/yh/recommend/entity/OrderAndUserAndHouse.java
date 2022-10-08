package yh.recommend.entity;

public class OrderAndUserAndHouse {
    private int orderId;
    private String nowtime;//预定时间
    private String checktime;//入住时间
    private String leavetime;//离开时间
    private int state;//状态
    private String oname;//个人姓名
    private int onumber;//房间数（***房间数不能大于未使用的房间数）
    private double priced;//折后价格
    private int ouId;//订单用户外键
    private int ohId;//订单房源外键

    private User user;
    private House house;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getNowtime() {
        return nowtime;
    }

    public void setNowtime(String nowtime) {
        this.nowtime = nowtime;
    }

    public String getChecktime() {
        return checktime;
    }

    public void setChecktime(String checktime) {
        this.checktime = checktime;
    }

    public String getLeavetime() {
        return leavetime;
    }

    public void setLeavetime(String leavetime) {
        this.leavetime = leavetime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public int getOnumber() {
        return onumber;
    }

    public void setOnumber(int onumber) {
        this.onumber = onumber;
    }

    public double getPriced() {
        return priced;
    }

    public void setPriced(double priced) {
        this.priced = priced;
    }

    public int getOuId() {
        return ouId;
    }

    public void setOuId(int ouId) {
        this.ouId = ouId;
    }

    public int getOhId() {
        return ohId;
    }

    public void setOhId(int ohId) {
        this.ohId = ohId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    @Override
    public String toString() {
        return "OrderAndUserAndHouse{" +
                "orderId=" + orderId +
                ", nowtime='" + nowtime + '\'' +
                ", checktime='" + checktime + '\'' +
                ", leavetime='" + leavetime + '\'' +
                ", state=" + state +
                ", oname='" + oname + '\'' +
                ", onumber=" + onumber +
                ", priced=" + priced +
                ", ouId=" + ouId +
                ", ohId=" + ohId +
                ", user=" + user +
                ", house=" + house +
                '}';
    }
}
