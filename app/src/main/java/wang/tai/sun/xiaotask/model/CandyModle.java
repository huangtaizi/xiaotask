package wang.tai.sun.xiaotask.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CandyModle implements Parcelable {
    @SerializedName("a")
    public int a;

    @SerializedName("b")
    public int b;

    @SerializedName("add")
    public int add;

    @SerializedName("reduce")
    public int reduce;

    @SerializedName("sizeA")
    public int sizeA;

    @SerializedName("sizeB")
    public int sizeB;

    @SerializedName("sizeAdd")
    public int sizeAdd;

    @SerializedName("sizeReduce")
    public int sizeReduce;

    @SerializedName("result")
    public int result;

    protected CandyModle(Parcel in) {
        a = in.readInt();
        b = in.readInt();
        add = in.readInt();
        reduce = in.readInt();
        sizeA = in.readInt();
        sizeB = in.readInt();
        sizeAdd = in.readInt();
        sizeReduce = in.readInt();
        result = in.readInt();
    }

    public static final Creator<CandyModle> CREATOR = new Creator<CandyModle>() {
        @Override
        public CandyModle createFromParcel(Parcel in) {
            return new CandyModle(in);
        }

        @Override
        public CandyModle[] newArray(int size) {
            return new CandyModle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(a);
        dest.writeInt(b);
        dest.writeInt(add);
        dest.writeInt(reduce);
        dest.writeInt(sizeA);
        dest.writeInt(sizeB);
        dest.writeInt(sizeAdd);
        dest.writeInt(sizeReduce);
        dest.writeInt(result);
    }

    @Override
    public String toString() {
        return "a=" + a + ";b=" + b + ";add=" + add + ";reduce=" + reduce
                + ";sizeA=" + sizeA + ";sizeB=" + sizeB + ";sizeAdd=" + sizeAdd
                + ";sizeReduce=" + sizeReduce + ";result=" + result;
    }
}
