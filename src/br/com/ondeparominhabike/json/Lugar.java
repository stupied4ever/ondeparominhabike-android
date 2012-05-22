package br.com.ondeparominhabike.json;

import com.google.android.maps.GeoPoint;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public class Lugar {
	
	@DatabaseField(id = true)
	@SerializedName("id")
	public int id;
	
	@DatabaseField
	@SerializedName("tipo")
	public String tipo;
	
	@DatabaseField
	@SerializedName("nome")
	public String nome;
	
	@DatabaseField
	@SerializedName("endereco_rua")
	public String enderecoRua;
	
	@DatabaseField
	@SerializedName("endereco_numero")
	public String enderecoNumero;
	
	@DatabaseField
	@SerializedName("endereco_bairro")
	public String enderecoBairro;
	
	@DatabaseField
	@SerializedName("endereco_cidade")
	public String enderecoCidade;
	
	@DatabaseField
	@SerializedName("endereco_estado")
	public String enderecoEstado;
	
	@DatabaseField
	@SerializedName("latitude")
	public float latitude;
	
	@DatabaseField
	@SerializedName("longitude")
	public float longitude;
	
	@DatabaseField
	@SerializedName("created_at")
	public String createdAt;
	
	@DatabaseField
	@SerializedName("updated_at")
	public String updatedAt;
	
	
	public GeoPoint getGeoPoint(){
		if(this.latitude != 0 && this.longitude != 0 ){
			return new GeoPoint((int)(this.latitude* 1E6), (int)(this.longitude* 1E6));	
		}
		return null;
	}
}
