package edu.gatech.cc.cs7470.facecard.Model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by miseonpark on 3/24/15.
 */
public class FaceCard implements Parcelable{

    private String bluetoothId, accountId, name, tag;
    private Bitmap profilePicture;

    public FaceCard(String bluetoothId, String accountId, String name, String tag){
        this.bluetoothId = bluetoothId;
        this.accountId = accountId;
        this.name = name;
        this.tag = tag;
    }

    public FaceCard(String bluetoothId, String accountId, String name, String tag, Bitmap profilePicture){
        this.bluetoothId = bluetoothId;
        this.accountId = accountId;
        this.name = name;
        this.tag = tag;
        this.profilePicture = profilePicture;
    }

    public String getBluetoothId() {
        return bluetoothId;
    }

    public void setBluetoothId(String bluetoothId) {
        this.bluetoothId = bluetoothId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Bitmap getProfilePicture() { return profilePicture; }

    public void setProfilePicture(Bitmap profilePicture) { this.profilePicture = profilePicture; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bluetoothId);
        dest.writeString(accountId);
        dest.writeString(name);
        dest.writeString(tag);
        dest.writeValue(profilePicture);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<FaceCard> CREATOR = new Parcelable.Creator<FaceCard>() {
        public FaceCard createFromParcel(Parcel in) {
            return new FaceCard(in);
        }

        public FaceCard[] newArray(int size) {
            return new FaceCard[size];
        }
    };

    private FaceCard(Parcel in){
        bluetoothId = in.readString();
        accountId = in.readString();
        name = in.readString();
        tag = in.readString();
        profilePicture = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }
}
