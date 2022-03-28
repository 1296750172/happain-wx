package cn.happain.wx.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message<T> {
    public Message(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;

    private String message;

    private T object;

    public void success(String message) {
        this.code="200";
        this.message=message;
    }
    public void fail(String message) {
        this.code="500";
        this.message=message;
    }
    public boolean isOk(){
        return this.code.equals("200")? true : false;
    }

}
