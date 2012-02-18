package br.com.ondeparominhabike.json;

import com.google.android.maps.GeoPoint;
import com.google.gson.annotations.SerializedName;

public class Lugar {
	
	@SerializedName("id")
	public int id;
	
	@SerializedName("tipo")
	public String tipo;
	
	@SerializedName("nome")
	public String nome;
	
	@SerializedName("endereco_rua")
	public String enderecoRua;
	
	@SerializedName("endereco_numero")
	public String enderecoNumero;
	
	@SerializedName("endereco_bairro")
	public String enderecoBairro;
	
	@SerializedName("endereco_cidade")
	public String enderecoCidade;
	
	@SerializedName("endereco_estado")
	public String enderecoEstado;
	
	@SerializedName("latitude")
	public float latitude;
	
	@SerializedName("longitude")
	public float longitude;
	
	@SerializedName("created_at")
	public String createdAt;
	
	@SerializedName("updated_at")
	public String updatedAt;
	
	public GeoPoint getGeoPoint(){
		if(this.latitude != 0 && this.longitude != 0 ){
			return new GeoPoint((int)(this.latitude* 1E6), (int)(this.longitude* 1E6));	
		}
		return null;
	}
}
