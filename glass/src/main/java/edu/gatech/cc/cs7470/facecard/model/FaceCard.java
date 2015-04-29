package edu.gatech.cc.cs7470.facecard.Model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by miseonpark on 4/15/15.
 */
public class FaceCard implements Serializable {

    static final long serialVersionUID =-3400790295688980146L;

    private String bluetoothId, accountId, name, tag, imageLink;
//    private Bitmap profilePicture;

    public FaceCard(String bluetoothId, String accountId, String name, String imageLink, String tag){
        this.bluetoothId = bluetoothId;
        this.accountId = accountId;
        this.name = name;
        this.tag = tag;
        this.imageLink = imageLink;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//        this.profilePicture = Bitmap.createBitmap(100, 100, conf);
    }

    public FaceCard(String bluetoothId, String accountId, String name, String imageLink, String tag, Bitmap profilePicture){
        this.bluetoothId = bluetoothId;
        this.accountId = accountId;
        this.name = name;
        this.tag = tag;
        this.imageLink = imageLink;
//        this.profilePicture = profilePicture;
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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Bitmap getProfilePicture() {
//        return profilePicture;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        return Bitmap.createBitmap(100, 100, conf);
    }

    public void setProfilePicture(Bitmap profilePicture) {
//        this.profilePicture = profilePicture;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(bluetoothId);
//        dest.writeString(accountId);
//        dest.writeString(name);
//        dest.writeString(tag);
//        dest.writeValue(profilePicture);
//    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
//    public static final Parcelable.Creator<FaceCard> CREATOR = new Parcelable.Creator<FaceCard>() {
//        public FaceCard createFromParcel(Parcel in) {
//            return new FaceCard(in);
//        }
//
//        public FaceCard[] newArray(int size) {
//            return new FaceCard[size];
//        }
//    };
//
//    private FaceCard(Parcel in){
//        bluetoothId = in.readString();
//        accountId = in.readString();
//        name = in.readString();
//        tag = in.readString();
//        profilePicture = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
//    }

    public byte[] serialize() throws IOException {
        Log.d("TAG", "serialize");
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(this);
        return b.toByteArray();
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of FaceCard or not */
        if (!(o instanceof FaceCard)) {
            return false;
        }

        // Compare account ID
        FaceCard c = (FaceCard) o;
        return this.accountId == c.accountId;
    }
}
