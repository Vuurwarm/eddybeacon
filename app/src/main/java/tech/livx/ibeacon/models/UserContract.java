package tech.livx.ibeacon.models;

import android.net.Uri;

import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.ContentUri;

/**
 * Created by damionunderworld on 2015/12/09.
 */
public interface UserContract extends ProviGenBaseContract {
    @Column(Column.Type.INTEGER)
    public static final String ID = "int";

    @Column(Column.Type.TEXT)
    public static final String NAME = "string";

    @Column(Column.Type.TEXT)
    public static final String SURNAME = "string";

    @Column(Column.Type.TEXT)
    public static final String EMAIL = "String";

    @ContentUri
    public static final Uri CCONTENT_URI = Uri.parse("content://tech.livx.ibeacon.UserContract");
}
