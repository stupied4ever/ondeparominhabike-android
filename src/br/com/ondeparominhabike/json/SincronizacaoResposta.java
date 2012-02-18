package br.com.ondeparominhabike.json;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public class SincronizacaoResposta {
	
	@SerializedName("ultima_atualizacao")
	public String ultimaAtualizacao;
	
	@SerializedName("lugares")
	public List<Lugar> lugares;
}
