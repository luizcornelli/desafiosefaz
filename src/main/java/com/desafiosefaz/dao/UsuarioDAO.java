package com.desafiosefaz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import com.desafiosefaz.factory.ConnectionFactory;
import com.desafiosefaz.models.Telefone;
import com.desafiosefaz.models.Usuario;

@Stateless
public class UsuarioDAO {

	public List<Usuario> carregarTodosUsuarios() {

		String sql = "SELECT * FROM usuario";

		List<Usuario> usuarios = new ArrayList<Usuario>();

		Connection conn        = null;
		PreparedStatement pstm = null;
		ResultSet rset         = null;

		try {
			
			conn = ConnectionFactory.createConnectionToMySQL();

			pstm = conn.prepareStatement(sql);

			rset = pstm.executeQuery();

			// Enquanto existir dados no banco de dados, faça 
			while (rset.next()) {

				Usuario usuario = new Usuario();

				usuario.setId(rset.getLong("id"));

				usuario.setNome(rset.getString("nome"));

				usuario.setEmail(rset.getString("email"));

				usuario.setSenha(rset.getString("senha"));
				
				usuario.setIsAdmin(rset.getBoolean("is_admin"));
				
				usuarios.add(usuario);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {

				if (rset != null) {

					rset.close();
				}

				if (pstm != null) {

					pstm.close();
				}

				if (conn != null) {
					conn.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return usuarios;
	}

	public void deletar(Usuario usuario) {
		
		deletarTelefones(usuario); 
		
		String sql = "DELETE FROM usuario WHERE id = ?";

		Connection conn        = null;
		PreparedStatement pstm = null;

		try {
			conn = ConnectionFactory.createConnectionToMySQL();

			pstm = conn.prepareStatement(sql);

			pstm.setLong(1, usuario.getId());

			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (pstm != null) {

					pstm.close();
				}

				if (conn != null) {
					conn.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	private void deletarTelefones(Usuario usuario) {
		
		String sql = "DELETE FROM telefone WHERE id_usuario = ?"; 
		
		Connection conn        = null;
		PreparedStatement pstm = null;

		try {
			
			conn = ConnectionFactory.createConnectionToMySQL();

			pstm = conn.prepareStatement(sql);

			pstm.setLong(1, usuario.getId());

			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (pstm != null) {

					pstm.close();
				}

				if (conn != null) {
					conn.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	public void adicionarNovoUsuario(Usuario usuario, List<Telefone> telefones) {
		
		String sql = "INSERT INTO usuario(nome,email,senha,is_admin) VALUES(?,?,?,?)";

		Connection conn = null;
		PreparedStatement pstm = null;

		try {

			conn = ConnectionFactory.createConnectionToMySQL();

			pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstm.setString(1, usuario.getNome());
			
			pstm.setString(2, usuario.getEmail());
			
			pstm.setString(3, usuario.getSenha());
			
			pstm.setBoolean(4, false);

			pstm.execute();
			
			final ResultSet rs = pstm.getGeneratedKeys();
			
			if (rs.next()) {
			    final Long lastId = rs.getLong(1);
			    usuario.setId(lastId);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (pstm != null) {

					pstm.close();
				}

				if (conn != null) {
					conn.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		inserirTelefone(telefones, usuario);
	}

	public void alterar(List<Usuario> usuarios) {
		
		String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE id = ?";
		
		Connection conn        = null;
		PreparedStatement pstm = null;
		
		for (int i = 0; i < usuarios.size(); i++) {
			
			Usuario usuario = (Usuario) usuarios.get(i);

			try {

				conn = ConnectionFactory.createConnectionToMySQL();

				pstm = conn.prepareStatement(sql);

				pstm.setString(1, usuario.getNome());
				
				pstm.setString(2, usuario.getEmail());

				pstm.setString(3, usuario.getSenha());
				
				pstm.setLong(4, usuario.getId());
				
				pstm.execute();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				try {
					if (pstm != null) {

						pstm.close();
					}

					if (conn != null) {
						conn.close();
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
	}

	public Usuario buscarLogin(String email, String senha) {

		String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";

		Connection conn        = null;
		PreparedStatement pstm = null;
		ResultSet rset         = null;
		
		Usuario usuario        = null;
		
		try {

			conn = ConnectionFactory.createConnectionToMySQL();

			pstm = conn.prepareStatement(sql);

			pstm.setString(1, email);
			
			pstm.setString(2, senha);
			
			rset = pstm.executeQuery();
			
			while(rset.next()) {
				
				usuario = new Usuario(); 
				
				usuario.setNome(rset.getString("nome"));
				usuario.setEmail(rset.getString("email"));
				usuario.setSenha(rset.getString("senha"));
				usuario.setIsAdmin(rset.getBoolean("isAdmin"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (pstm != null) {

					pstm.close();
				}

				if (conn != null) {
					conn.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return usuario; 
	}

	public void inserirTelefone(List<Telefone> telefones, Usuario usuario) {

		String sql = "INSERT INTO telefone(ddd,numero,tipo_telefone,id_usuario) VALUES(?,?,?,?)";

		Connection conn        = null;
		PreparedStatement pstm = null;
		
		for (int i = 0; i < telefones.size(); ++i) {

			Telefone telefone = (Telefone) telefones.get(i);
			telefone.setUsuario(usuario);

			try {

				conn = ConnectionFactory.createConnectionToMySQL();

				pstm = conn.prepareStatement(sql);

				pstm.setString(1, telefone.getDdd());
				
				pstm.setString(2, telefone.getNumero());
				
				pstm.setString(3, telefone.getTipoTelefone());
				
				pstm.setLong(4, telefone.getUsuario().getId());
						
				pstm.execute();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				try {
					if (pstm != null) {

						pstm.close();
					}

					if (conn != null) {
						conn.close();
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
	}

	public List<Telefone> buscarTelefonesUsuario(Usuario usuario) {

		String sql = "SELECT * FROM telefone WHERE id_usuario = ?";
		
		List<Telefone> telefones = new ArrayList<Telefone>();
		
		Connection conn        = null;
		PreparedStatement pstm = null;
		ResultSet rset         = null;
		
		try {

			conn = ConnectionFactory.createConnectionToMySQL();

			pstm = conn.prepareStatement(sql);

			pstm.setLong(1, usuario.getId());
			
			rset = pstm.executeQuery();
			
			while(rset.next()) {
				
				Telefone telefone = new Telefone(); 
				
				telefone.setId(rset.getLong("id"));

				telefone.setDdd(rset.getString("ddd"));

				telefone.setNumero(rset.getString("numero"));

				telefone.setTipoTelefone(rset.getString("tipo_telefone"));

				telefones.add(telefone);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (pstm != null) {

					pstm.close();
				}

				if (conn != null) {
					conn.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return telefones; 
	}

	public void deletarTelefone(Telefone telefone, Usuario usuario) {

		String sql = "DELETE FROM telefone WHERE id = ? AND id_usuario = ?";

		Connection conn = null;
		PreparedStatement pstm = null;

		try {
			conn = ConnectionFactory.createConnectionToMySQL();

			pstm = conn.prepareStatement(sql);

			pstm.setLong(1, telefone.getId());
			pstm.setLong(2, usuario.getId());

			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (pstm != null) {

					pstm.close();
				}

				if (conn != null) {
					conn.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	public void alterarTelefone(List<Telefone> telefones) {
		
		String sql = "UPDATE telefone SET ddd = ?, numero = ?, tipo_telefone = ?" + " WHERE id = ?";
		
		Connection conn			= null;
		PreparedStatement pstm 	= null;
		
		for (int i = 0; i < telefones.size(); i++) {
			
			Telefone telefone = (Telefone) telefones.get(i);

			try {

				conn = ConnectionFactory.createConnectionToMySQL();

				pstm = conn.prepareStatement(sql);

				pstm.setString(1, telefone.getDdd());
				
				pstm.setString(2, telefone.getNumero());
				
				pstm.setString(3, telefone.getTipoTelefone());
				
				pstm.setLong(4, telefone.getId());
				
				pstm.execute();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				try {
					if (pstm != null) {

						pstm.close();
					}

					if (conn != null) {
						conn.close();
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
	}

	public void adicionarNovoTelefone(Usuario usuario, Telefone telefone) {
		
		String sql = "INSERT INTO telefone(ddd,numero,tipo_telefone,id_usuario)" + " VALUES(?,?,?,?)";

		Connection conn 		= null;
		PreparedStatement pstm 	= null;

		try {

			conn = ConnectionFactory.createConnectionToMySQL();

			pstm = conn.prepareStatement(sql);

			pstm.setString(1, telefone.getDdd());
			
			pstm.setString(2, telefone.getNumero());
			
			pstm.setString(3, telefone.getTipoTelefone());
			
			pstm.setLong(4, usuario.getId());

			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (pstm != null) {

					pstm.close();
				}

				if (conn != null) {
					conn.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}
}
