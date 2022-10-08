package yh.recommend.utils;

public class Result {
    //1，成功 2.异常
    private Object data;
    private String message;
    private int code;
    private Object flag;
    private String token;
    private int count;


    public Result(Object data,int count,int code,String message){
        this.data = data;
        this.message = message;
        this.code = code;
        this.count = count;
    }

//    public Result(Result result,String message,int code){
//        this.result = result;
//        this.message = message;
//        this.code = code;
//    }

    public Result(){}
    public Result(String message, int code){
        this.message = message;
        this.code = code;
    }
    public Result(Object data, String message, int code){
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public Result(String message,int code,String token){
        this.code = code;
        this.message = message;
        this.token = token;
    }

    public Result(Object data, String message, int code, String token){
        this.data = data;
        this.message = message;
        this.code = code;
        this.token = token;
    }

    public Result(Object data, String message, int code, Object flag,String token){
        this.data = data;
        this.message = message;
        this.code = code;
        this.flag = flag;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getFlag() {
        return flag;
    }

    public void setFlag(Object flag) {
        this.flag = flag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Result{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", code=" + code +
                ", flag=" + flag +
                ", token='" + token + '\'' +
                ", count=" + count +
                '}';
    }
}
