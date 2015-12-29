package tech.livx.ibeacon.models;

import android.net.Uri;

import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.ContentUri;

/**
 * Created by AJ van Deventer on 2015/11/25.
 *
 */
public interface BeaconContract extends ProviGenBaseContract {
    @Column(Column.Type.INTEGER)
    public static final String ID = "int";

    @Column(Column.Type.INTEGER)
    public static final String MAJOR = "int";

    @Column(Column.Type.INTEGER)
    public static final String MINOR = "int";

    @Column(Column.Type.TEXT)
    public static final String UUID = "string";

    @Column(Column.Type.INTEGER)
    public static final String SPECIAL = "int";

    @Column(Column.Type.INTEGER)
    public static final String Store = "int";

    @ContentUri
    public static final Uri CCONTENT_URI = Uri.parse("content://tech.livx.ibeacon.BeaconContract");

}
