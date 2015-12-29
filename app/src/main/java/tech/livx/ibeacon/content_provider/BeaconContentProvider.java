package tech.livx.ibeacon.content_provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tjeannin.provigen.ProviGenOpenHelper;
import com.tjeannin.provigen.ProviGenProvider;
import com.tjeannin.provigen.helper.TableBuilder;
import com.tjeannin.provigen.helper.TableUpdater;
import com.tjeannin.provigen.model.Constraint;

import tech.livx.ibeacon.models.BeaconContract;
import tech.livx.ibeacon.models.SpecialContract;

/**
 * Created by AJ van Deventer on 2015/11/25.
 *
 */
public class BeaconContentProvider extends ProviGenProvider {
    public BeaconContentProvider(){}
    private static Class[] contracts = new Class[]{SpecialContract.class};

    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new SQLiteOpenHelper(getContext(), "beaconDb", null, 1) {

            @Override
            public void onCreate(SQLiteDatabase database) {
                // Automatically creates table and needed columns.
                new TableBuilder(SpecialContract.class)
                        .addConstraint(SpecialContract.ID, Constraint.UNIQUE, Constraint.OnConflict.REPLACE)
                        .createTable(database);
            }

            @Override
            public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
                // Automatically adds new columns.
                TableUpdater.addMissingColumns(database, SpecialContract.class);

                // Anything else related to database upgrade should be done here.
            }
        };
    }

    @Override
    public Class[] contractClasses() {
        return contracts;
    }
}
