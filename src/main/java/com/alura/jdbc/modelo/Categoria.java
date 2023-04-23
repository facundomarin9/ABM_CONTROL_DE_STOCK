package com.alura.jdbc.modelo;

import java.util.ArrayList;
import java.util.List;

public class Categoria {

	private Integer id;
	private String nombre;
	private List<Producto> productos;

	public Categoria(Integer id, String nombre) {

		this.id = id;
		this.nombre = nombre;

	}

	public Integer getId() {
		return this.id;
	}

	public List<Producto> getProductos() {

		return this.productos;

	}

	public void agregar(Producto producto) {

		if (this.productos == null) {
			this.productos = new ArrayList<>();
		}

		this.productos.add(producto);

	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.nombre;
	}

}
