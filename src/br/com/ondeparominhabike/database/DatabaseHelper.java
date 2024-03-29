package br.com.ondeparominhabike.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.ondeparominhabike.R;
import br.com.ondeparominhabike.json.Lugar;
import br.com.ondeparominhabike.json.SincronizacaoResposta;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "helloAndroid.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 4;

	// the DAO object we use to access the SimpleData table
	private Dao<SincronizacaoResposta, Integer> sincronizacaoRespostaDao = null;
	private Dao<Lugar, Integer> lugarDao = null;
	private RuntimeExceptionDao<Lugar, Integer> lugarRuntimeDao = null;
	private RuntimeExceptionDao<SincronizacaoResposta, Integer> sincronizacaoRespostaRuntimeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Lugar.class);
			TableUtils.createTable(connectionSource, SincronizacaoResposta.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}

		// here we try inserting data in the on-create as a test
		RuntimeExceptionDao<Lugar, Integer> dao = getLugarDao();
		RuntimeExceptionDao<SincronizacaoResposta, Integer> daoSinc = getSincronizacaoRespostaDao();
		long millis = System.currentTimeMillis();
		Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate: " + millis);
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Lugar.class, true);
			TableUtils.dropTable(connectionSource, SincronizacaoResposta.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	public Dao<Lugar, Integer> getDao() throws SQLException {
		if (lugarDao == null) {
			lugarDao = getDao(Lugar.class);
			sincronizacaoRespostaDao = getDao(SincronizacaoResposta.class);
		}
		return lugarDao;
	}

	/**
	 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
	 * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
	 */
	public RuntimeExceptionDao<Lugar, Integer> getLugarDao() {
		if (lugarRuntimeDao == null) {
			lugarRuntimeDao = getRuntimeExceptionDao(Lugar.class);
		}
		return lugarRuntimeDao;
	}
	
	/**
	 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
	 * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
	 */
	public RuntimeExceptionDao<SincronizacaoResposta, Integer> getSincronizacaoRespostaDao() {
		if (sincronizacaoRespostaRuntimeDao == null) {
			sincronizacaoRespostaRuntimeDao = getRuntimeExceptionDao(SincronizacaoResposta.class);
		}
		return sincronizacaoRespostaRuntimeDao;
	}
	
	

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		lugarRuntimeDao = null;
	}
}
