package wang.tai.sun.xiaotask.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfModle implements Parcelable{
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_ABNORMAL = 2;

    @SerializedName("type")
    public int type;

    @SerializedName("thanOne")
    public List<CandyModle> thanOne;

    @SerializedName("thanTwo")
    public List<CandyModle> thanTwo;

    @SerializedName("thanThree")
    public List<CandyModle> thanThree;

    @SerializedName("thanFour")
    public List<CandyModle> thanFour;

    @SerializedName("thanFive")
    public List<CandyModle> thanFive;

    @SerializedName("thanSix")
    public List<CandyModle> thanSix;

    @SerializedName("thanOneAdd")
    public List<CandyModle> thanOneAdd;

    @SerializedName("thanTwoAdd")
    public List<CandyModle> thanTwoAdd;

    @SerializedName("thanThreeAdd")
    public List<CandyModle> thanThreeAdd;

    @SerializedName("thanFourAdd")
    public List<CandyModle> thanFourAdd;

    @SerializedName("thanFiveAdd")
    public List<CandyModle> thanFiveAdd;

    @SerializedName("thanSixAdd")
    public List<CandyModle> thanSixAdd;

    @SerializedName("thanOneReduce")
    public List<CandyModle> thanOneReduce;

    @SerializedName("thanTwoReduce")
    public List<CandyModle> thanTwoReduce;

    @SerializedName("thanThreeReduce")
    public List<CandyModle> thanThreeReduce;

    @SerializedName("thanFourReduce")
    public List<CandyModle> thanFourReduce;

    @SerializedName("thanFiveReduce")
    public List<CandyModle> thanFiveReduce;

    @SerializedName("thanSixReduce")
    public List<CandyModle> thanSixReduce;

    protected ConfModle(Parcel in) {
        type = in.readInt();
        thanOne = in.createTypedArrayList(CandyModle.CREATOR);
        thanTwo = in.createTypedArrayList(CandyModle.CREATOR);
        thanThree = in.createTypedArrayList(CandyModle.CREATOR);
        thanFour = in.createTypedArrayList(CandyModle.CREATOR);
        thanFive = in.createTypedArrayList(CandyModle.CREATOR);
        thanSix = in.createTypedArrayList(CandyModle.CREATOR);
        thanOneAdd = in.createTypedArrayList(CandyModle.CREATOR);
        thanTwoAdd = in.createTypedArrayList(CandyModle.CREATOR);
        thanThreeAdd = in.createTypedArrayList(CandyModle.CREATOR);
        thanFourAdd = in.createTypedArrayList(CandyModle.CREATOR);
        thanFiveAdd = in.createTypedArrayList(CandyModle.CREATOR);
        thanSixAdd = in.createTypedArrayList(CandyModle.CREATOR);
        thanOneReduce = in.createTypedArrayList(CandyModle.CREATOR);
        thanTwoReduce = in.createTypedArrayList(CandyModle.CREATOR);
        thanThreeReduce = in.createTypedArrayList(CandyModle.CREATOR);
        thanFourReduce = in.createTypedArrayList(CandyModle.CREATOR);
        thanFiveReduce = in.createTypedArrayList(CandyModle.CREATOR);
        thanSixReduce = in.createTypedArrayList(CandyModle.CREATOR);
    }

    public static final Creator<ConfModle> CREATOR = new Creator<ConfModle>() {
        @Override
        public ConfModle createFromParcel(Parcel in) {
            return new ConfModle(in);
        }

        @Override
        public ConfModle[] newArray(int size) {
            return new ConfModle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeTypedList(thanOne);
        dest.writeTypedList(thanTwo);
        dest.writeTypedList(thanThree);
        dest.writeTypedList(thanFour);
        dest.writeTypedList(thanFive);
        dest.writeTypedList(thanSix);
        dest.writeTypedList(thanOneAdd);
        dest.writeTypedList(thanTwoAdd);
        dest.writeTypedList(thanThreeAdd);
        dest.writeTypedList(thanFourAdd);
        dest.writeTypedList(thanFiveAdd);
        dest.writeTypedList(thanSixAdd);
        dest.writeTypedList(thanOneReduce);
        dest.writeTypedList(thanTwoReduce);
        dest.writeTypedList(thanThreeReduce);
        dest.writeTypedList(thanFourReduce);
        dest.writeTypedList(thanFiveReduce);
        dest.writeTypedList(thanSixReduce);
    }
}
