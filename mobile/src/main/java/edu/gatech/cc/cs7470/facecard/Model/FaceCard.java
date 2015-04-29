package edu.gatech.cc.cs7470.facecard.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by miseonpark on 3/24/15.
 */
public class FaceCard implements Serializable{

    static final long serialVersionUID =-3400790295688980146L;

    private String bluetoothId, accountId, name, tag, imageLink;
//    private Bitmap profilePicture;
//    private byte[] profilePicture;

    public FaceCard(String bluetoothId, String accountId, String name, String imageLink, String tag){
        this.bluetoothId = bluetoothId;
        this.accountId = accountId;
        this.name = name;
        this.tag = tag;
        this.imageLink = imageLink;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(100, 100, conf);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
//        this.profilePicture = stream.toByteArray();
    }

    public FaceCard(String bluetoothId, String accountId, String name, String imageLink, String tag, Bitmap profilePicture){
        this.bluetoothId = bluetoothId;
        this.accountId = accountId;
        this.name = name;
        this.tag = tag;
        this.imageLink = imageLink;
//        this.profilePicture = profilePicture;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        profilePicture.compress(Bitmap.CompressFormat.PNG, 0, stream);
//        this.profilePicture = stream.toByteArray();
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

//    public Bitmap getProfilePicture() {
//        Bitmap bitmap = BitmapFactory.decodeByteArray(profilePicture, 0, profilePicture.length);
//        return bitmap;
//    }
//
//    public void setProfilePicture(Bitmap profilePicture) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        profilePicture.compress(Bitmap.CompressFormat.PNG, 0, stream);
//        this.profilePicture = stream.toByteArray();
//    }

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

//    private FaceCard(Parcel in){
//        bluetoothId = in.readString();
//        accountId = in.readString();
//        name = in.readString();
//        imageLink = in.readString();
//        tag = in.readString();
//        profilePicture = in.readByteArray();
////        profilePicture = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
//    }

    public byte[] serialize() throws IOException {
        Log.d("TAG", "serialize");
//        if(this.profilePicture==null) {
//            Log.d("TAG", this.getName() + " " + this.getBluetoothId() + " null");
//        }else{
//            Log.d("TAG", this.getName() + " " + this.getBluetoothId() + " not null");
//        }
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(this);
        return b.toByteArray();
    }
}
