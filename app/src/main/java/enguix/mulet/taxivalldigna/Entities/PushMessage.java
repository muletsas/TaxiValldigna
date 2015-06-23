package enguix.mulet.taxivalldigna.Entities;

import java.io.Serializable;

import enguix.mulet.taxivalldigna.R;

/**
 * Created by root on 10/06/15.
 */
public class PushMessage implements Serializable {

    private String title;
    private String message ;
    private String subtitle;
    private String ticker;
    private String vibrate;
    private String sound;
    private String type;

    public PushMessage() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getVibrate() {
        return vibrate;
    }

    public void setVibrate(String vibrate) {
        this.vibrate = vibrate;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString(){
        String msg="";

        msg+= title+"\n";
        msg+= message;

        return msg;
    }

    public int getSmallIcon(){
        int icon=0;
        switch (type){
            case "info":
                icon = R.drawable.ic_taxi_info;
                break;
            case "taxi":
                icon = R.drawable.ic_taxi_car;
                break;
            default:
                icon = R.mipmap.ic_launcher;
        }


        return icon;
    }

    public int numberNotification(){
        switch (type){
            case "info":
                return  1;

            case "taxi":
                return 2;

            default:
                return 3;
        }
    }

}
