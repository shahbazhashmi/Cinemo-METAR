package cinemo.metar.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Shahbaz Hashmi on 2020-01-28.
 */
@Entity(tableName = "station")
public class Station {

    @PrimaryKey
    @ColumnInfo(name = "file_name")
    @NonNull
    private String fileName;

    @ColumnInfo(name = "date_modified")
    private String dateModified;

    @ColumnInfo(name = "size")
    private long size;

    @ColumnInfo(name = "data")
    private String data;

    public Station(String dateModified, long size, String fileName) {
        this.dateModified = dateModified;
        this.size = size;
        this.fileName = fileName;
    }

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
}
