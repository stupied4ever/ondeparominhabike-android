package br.com.ondeparominhabike.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;
import br.com.ondeparominhabike.database.DatabaseHelper;
import br.com.ondeparominhabike.json.Lugar;
import br.com.ondeparominhabike.json.SincronizacaoResposta;

import com.google.gson.Gson;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;


public abstract class Lugares {
	public static List<Lugar> todos(Context context){
		// get our dao
		DatabaseHelper dh = new DatabaseHelper(context);
		RuntimeExceptionDao<Lugar, Integer> lugarDao = dh.getLugarDao();

		return lugarDao.queryForAll();
	}

	public static void sincronizaLugares(Context context){
		// get our dao
		DatabaseHelper dh = new DatabaseHelper(context);
		RuntimeExceptionDao<Lugar, Integer> simpleDao = dh.getLugarDao();

		// query for all of the data objects in the database
//		List<Lugar> list = simpleDao.queryForAll();
//		try {
//			simpleDao.delete(simpleDao.deleteBuilder().prepare());
//		} catch (SQLException e) {
//			Log.d("db", "nao pode apagar os lugares");
//			e.printStackTrace();
//			return;
//		}

		Log.d("db", String.valueOf(simpleDao.queryForAll().size()) + " lugares nno banco" );

		List<Lugar> lugares = parseJson(context);
		Log.d("db", String.valueOf(lugares.size()) + " lugares no array banco" );
		for(Lugar l: lugares){
			simpleDao.createOrUpdate(l);
		}

		Log.d("db", String.valueOf(simpleDao.queryForAll().size()) + " lugares nno banco" );
	}

	private static List<Lugar> parseJson(Context context){
		Gson gson = new Gson();
		DatabaseHelper dh = new DatabaseHelper(context);
		RuntimeExceptionDao<SincronizacaoResposta, Integer> simpleDao = dh.getSincronizacaoRespostaDao();
		
		//    	String url = "http://192.168.0.11:3000/lugares.json?since=2012-02-18 00:50:44";

		
		String url;
		try {
			
			QueryBuilder<SincronizacaoResposta, Integer> qb = simpleDao.queryBuilder();
			
			List<SincronizacaoResposta> sincronizacoes =  qb.orderBy("ultimaAtualizacao", false).limit(1).query();
			
			if(sincronizacoes.size() > 0){
				url = "http://ondeparominhabike-web.heroku.com/lugares.json?desde=" + URLEncoder.encode(sincronizacoes.get(0).ultimaAtualizacao, "utf-8");
			}else{
				url = "http://ondeparominhabike-web.heroku.com/lugares.json";
			}
			
			Log.d("sincronizacao", url);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// TODO Auto-generated catch block
			Log.d("sincronizacao", "nao conseguiu encodar a url");
			return new ArrayList<Lugar>();
		} catch (SQLException e) {
			Log.d("sincronizacao", "executar select para pegar ultima atualizacao");
			return new ArrayList<Lugar>();
		}

		InputStream source = retrieveStream(url);

		Reader reader = new InputStreamReader(source);
		

		
		
		SincronizacaoResposta resposta = gson.fromJson(reader, SincronizacaoResposta.class);
		
		
		
		simpleDao.create(resposta);
		

		return resposta.lugares;

	}

	private static InputStream retrieveStream(String url) {
		DefaultHttpClient client = new DefaultHttpClient(); 

		HttpGet getRequest = new HttpGet(url);

		try {

			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) { 
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();

		} 
		catch (IOException e) {
			getRequest.abort();
		}
		return null;   
	}
}
