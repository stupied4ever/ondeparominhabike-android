package br.com.ondeparominhabike.model;

import br.com.ondeparominhabike.json.Lugar;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
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

import com.google.gson.Gson;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public abstract class Lugares {
	public static List<Lugar> todos(Context context){
		// get our dao
    	DatabaseHelper dh = new DatabaseHelper(context);
		RuntimeExceptionDao<Lugar, Integer> simpleDao = dh.getLugarDao();
		
		return simpleDao.queryForAll();
	}
	
	
	public static void sincronizaLugares(Context context){
		// get our dao
    	DatabaseHelper dh = new DatabaseHelper(context);
		RuntimeExceptionDao<Lugar, Integer> simpleDao = dh.getLugarDao();
		
		// query for all of the data objects in the database
		List<Lugar> list = simpleDao.queryForAll();
		try {
			simpleDao.delete(simpleDao.deleteBuilder().prepare());
		} catch (SQLException e) {
			Log.d("db", "nao pode apagar os lugares");
			e.printStackTrace();
			return;
		}
		
		Lugar[] lugares = parseJson();
		for(Lugar l: lugares){
			simpleDao.create(l);			
		}

		Log.d("db", String.valueOf(simpleDao.queryForAll().size()) + " lugares nno banco" );
	}
	
	 private static Lugar[] parseJson(){
    	Gson gson = new Gson();
    
//    	String url = "http://10.10.1.100:3000/lugares.json";
    	String url = "http://192.168.0.11:3000/lugares.json";

    	InputStream source = retrieveStream(url);
    	
        Reader reader = new InputStreamReader(source);
        
        Lugar[] lugares = gson.fromJson(reader, Lugar[].class);
        
        return lugares;
        
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
