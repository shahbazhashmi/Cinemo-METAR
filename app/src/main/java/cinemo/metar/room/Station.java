package cinemo.metar.room;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Shahbaz Hashmi on 2020-01-28.
 */
@Entity(tableName = "station")
public class Station implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "file_name")
    @NonNull
    private String fileName;

    @ColumnInfo(name = "date_modified")
    private String dateModified;

    @ColumnInfo(name = "size")
    private long size;

    @ColumnInfo(name = "data")
    private String data = null;

    public Station(@NonNull String fileName, String dateModified, long size) {
        this.fileName = fileName;
        this.dateModified = dateModified;
        this.size = size;
    }

    protected Station(Parcel in) {
        fileName = in.readString();
        dateModified = in.readString();
        size = in.readLong();
        data = in.readString();
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    public void setData(String data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDateModified() {
        return dateModified;
    }

    public long getSize() {
        return size;
    }

    public String getData() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fileName);
        parcel.writeString(dateModified);
        parcel.writeLong(size);
        parcel.writeString(data);
    }
}
