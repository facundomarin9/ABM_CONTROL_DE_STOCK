package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

public class ProductoDAO {

	private Connection con; // final

	public ProductoDAO(Connection con) {
		this.con = con;

	}

	public int modificar(Producto producto, Integer id) {

		try {

			final PreparedStatement statement = con
					.prepareStatement("UPDATE PRODUCTO SET NOMBRE = ?, DESCRIPCION = ?, CANTIDAD = ?  WHERE id = ?");

			try (statement) {

				statement.setString(1, producto.getNombre());
				statement.setString(2, producto.getDescripcion());
				statement.setInt(3, producto.getCantidad());
				statement.setInt(4, id);

				statement.execute();

				int updateCount = statement.getUpdateCount();

				return updateCount;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public int eliminar(Integer id) {

		try {

			PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");

			try (statement) {

				statement.setInt(1, id);

				statement.execute();

				int updateCount = statement.getUpdateCount();

				return updateCount;

			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public void guardar(Producto producto) {

		try {

			final PreparedStatement statement = con.prepareStatement(
					"INSERT INTO PRODUCTO (NOMBRE, DESCRIPCION, CANTIDAD, CATEGORIA_ID)" + "VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			try (statement) {
				runRecord(producto, statement);
			}
		} catch (SQLException e) {

			throw new RuntimeException(e);
		}

	}

	private void runRecord(Producto producto, PreparedStatement statement) throws SQLException {

		// if(cantidad < 50) {
		// throw new RuntimeException("Ocurrio un error");
		// }
		statement.setString(1, producto.getNombre());
		statement.setString(2, producto.getDescripcion());
		statement.setInt(3, producto.getCantidad());
		statement.setInt(4, producto.getCategoriaId());

		statement.execute();

		final ResultSet resultSet = statement.getGeneratedKeys();

		try (resultSet) {

			while (resultSet.next()) {

				producto.setId(resultSet.getInt(1));
				System.out.println(String.format("Fue insertado el producto de ID %s", producto));

			}
		}
	}

	public List<Producto> listar() {

		List<Producto> resultado = new ArrayList<>();

		try {

			final PreparedStatement statement = con
					.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");

			try (statement) {

				boolean result = statement.execute();

				ResultSet resultSet = statement.getResultSet();

				while (resultSet.next()) {

					Producto fila = new Producto(resultSet.getInt("id"), resultSet.getString("NOMBRE"),
							resultSet.getString("DESCRIPCION"), resultSet.getInt("CANTIDAD"));

					resultado.add(fila);
				}

				return resultado;
			}
		} catch (SQLException e) {

			throw new RuntimeException(e);

		}
	}

	public List<Producto> listar(Integer categoriaId) {

		List<Producto> resultado = new ArrayList<>();

		try {

			var querySelect = "SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO WHERE CATEGORIA_ID = ?";
			System.out.println(querySelect);
			final PreparedStatement statement = con.prepareStatement(querySelect);

			try (statement) {
				statement.setInt(1, categoriaId);
				boolean result = statement.execute();

				ResultSet resultSet = statement.getResultSet();

				while (resultSet.next()) {

					Producto fila = new Producto(resultSet.getInt("id"), resultSet.getString("NOMBRE"),
							resultSet.getString("DESCRIPCION"), resultSet.getInt("CANTIDAD"));

					resultado.add(fila);
				}

				return resultado;
			}
		} catch (SQLException e) {

			throw new RuntimeException(e);

		}

	}

}
