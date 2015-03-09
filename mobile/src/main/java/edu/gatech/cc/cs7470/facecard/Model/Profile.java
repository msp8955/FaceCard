package edu.gatech.cc.cs7470.facecard.Model;

/**
 * Created by miseonpark on 3/3/15.
 */
public class Profile {

    private String username;
    private String bluetoothId;
    private String name;
    private String note;
    private String description;
    private String organization;
    //image

    public Profile(String username, String bluetoothId, String name, String note){
        this.username = username;
        this.bluetoothId = bluetoothId;
        this.name = name;
        this.note = note;
    }

    public Profile(String name, String description, String organization){
        this.name = name;
        this.description = description;
        this.organization = organization;
    }

    public String getUsername(){
        return username;
    }
    public String getBluetoothId(){
        return bluetoothId;
    }
    public String getName(){
        return name;
    }
    public String getNote(){
        return note;
    }
    public void setNote(String note){
        this.note = note;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getOrganization(){
        return organization;
    }
    public void setOrganization(String organization){
        this.organization = organization;
    }

}
