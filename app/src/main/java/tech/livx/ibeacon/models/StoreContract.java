package tech.livx.ibeacon.models;

import android.net.Uri;

import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.ContentUri;

/**
 * Created by damionunderworld on 2015/12/09.
 */
public interface StoreContract extends ProviGenBaseContract {

    @Column(Column.Type.INTEGER)
    public static final String ID = "int";

    @Column(Column.Type.INTEGER)
    public static final String BEACON_ID = "int";

    @Column(Column.Type.INTEGER)
    public static final String MENU_ID = "int";

    @Column(Column.Type.TEXT)
    public static final String PRIMARY_COLOR = "string";

    @Column(Column.Type.TEXT)
    public static final String NAME = "string";

    @ContentUri
    public static final Uri CCONTENT_URI = Uri.parse("content://tech.livx.ibeacon.MenuItemContract");
}
