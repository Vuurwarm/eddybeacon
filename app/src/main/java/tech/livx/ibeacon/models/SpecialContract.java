package tech.livx.ibeacon.models;

import android.net.Uri;

import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.ContentUri;

/**
 * Created by damionunderworld on 2015/12/09.
 */
public interface SpecialContract extends ProviGenBaseContract {
    @Column(Column.Type.INTEGER)
    public static final String ID = "id";

    @Column(Column.Type.INTEGER)
    public static final String SPECIAL_ID = "special_id";

    @Column(Column.Type.INTEGER)
    public static final String MAJOR = "major";

    @Column(Column.Type.INTEGER)
    public static final String MINOR = "minor";

    @Column(Column.Type.TEXT)
    public static final String IMAGE = "image";

    @Column(Column.Type.TEXT)
    public static final String LINK = "link";

    @Column(Column.Type.TEXT)
    public static final String DESCRIPTION = "description";

    @ContentUri
    public static final Uri CONTENT_URI = Uri.parse("content://tech.livx.ibeacon/SpecialContract");

}
