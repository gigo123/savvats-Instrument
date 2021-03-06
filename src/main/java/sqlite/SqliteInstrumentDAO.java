package sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.InstrumentDAO;

import models.Instrument;

public class SqliteInstrumentDAO implements InstrumentDAO {

	private final static String SELECT_ID_QUERY = "SELECT * FROM instrument WHERE id = ?";
	private final static String SELECT_NAME_QUERY = "SELECT * FROM instrument WHERE NAME = ?";
	private final static String SELECT_ALL_QUERY = "SELECT * FROM instrument";
	private final static String INSERT_QUERY = "INSERT INTO instrument(name, measure, comment, totalNumber)"
			+ " VALUES(?, ?, ?,0)";
	private final static String DELETE_QUERY = "DELETE FROM instrument WHERE id = ?";
	private SQLConectionHolder conectionHolder;
	private boolean sqlError = false;
	private final static String UPDATE_QUERY = "UPDATE instrument SET name =? , measure =?, comment =? ,totalNumber =?  where id = ?";

	@Override
	public boolean hasError() {
		return sqlError;
	}

	public void setSqlError(boolean sqlError) {
		this.sqlError = sqlError;
	}

	public SQLConectionHolder getConectionHolder() {
		return conectionHolder;
	}

	public void setConectionHolder(SQLConectionHolder conectionHolder) {
		this.conectionHolder = conectionHolder;
	}

	private Object selectQ(Object obj, Object obj2, int type) {
		sqlError = false;

		if (conectionHolder != null && conectionHolder.getConnection() != null) {
			Connection conn = conectionHolder.getConnection();
			ResultSet rs = null;
			PreparedStatement prepSt = null;
			Instrument inst = null;
			List<Instrument> instList = new ArrayList<Instrument>();
			try {
				switch (type) {
				case (1): {
					prepSt = conn.prepareStatement(SELECT_ID_QUERY);
					prepSt.setLong(1, (long) obj);
					rs = prepSt.executeQuery();
					break;
				}
				case 2: {
					prepSt = conn.prepareStatement(SELECT_NAME_QUERY);
					prepSt.setString(1, (String) obj);
					rs = prepSt.executeQuery();
					break;
				}
				case 3: {
					prepSt = conn.prepareStatement(SELECT_NAME_QUERY);
					prepSt.setInt(1, (int) obj);
					rs = prepSt.executeQuery();
					break;
				}

				case 4: {
					prepSt = conn.prepareStatement(SELECT_ALL_QUERY);
					rs = prepSt.executeQuery();
					break;
				}
				default: {
					sqlError = true;
				}
				}
				while (rs.next()) {
					inst = new Instrument();
					inst.setId(rs.getInt("id"));
					inst.setName(rs.getString("name"));
					inst.setComment(rs.getString("comment"));
					inst.setMeasure(rs.getString("measure"));
					inst.setTotalNumber(rs.getFloat("totalNumber"));
					if (type == 1 || type == 2) {
						break;
					} else {
						instList.add(inst);
					}
				}
				// conectionHolder.closeConnection();
				if (type == 1 || type == 2) {
					return inst;
				} else {
					return instList;
				}

			} catch (SQLException e) {
				sqlError = true;
				e.printStackTrace();
			} finally {
				if (prepSt != null) {
					try {
						prepSt.close();
					} catch (SQLException sqlEx) {
					}
					prepSt = null;
				}
			}
		} else {
			sqlError = true;
		}
		return null;

	}

	@Override
	public Instrument getInstrumentByID(long id) {
		return (Instrument) selectQ(id, null, 1);
	}

	@Override
	public Instrument getInstrumentByName(String name) {
		return (Instrument) selectQ(name, null, 2);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Instrument> getInstrumentByNameL(String name) {
		return (List<Instrument>) selectQ(name, null, 3);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Instrument> getAllInstrument() {
		return (List<Instrument>) selectQ(null, null, 4);
	}

	@Override
	public boolean createInstrument(Instrument instrument) {
		sqlError = false;
		if (conectionHolder != null && conectionHolder.getConnection() != null) {
			Connection conn = conectionHolder.getConnection();
			PreparedStatement prepSt = null;
			try {
				prepSt = conn.prepareStatement(INSERT_QUERY);
				prepSt.setString(1, instrument.getName());
				prepSt.setString(2, instrument.getMeasure());
				prepSt.setString(3, instrument.getComment());
				prepSt.execute();
				conectionHolder.closeConnection();
			} catch (SQLException e) {
				sqlError = true;
				e.printStackTrace();
				return false;
			} finally {
				if (prepSt != null) {
					try {
						prepSt.close();
					} catch (SQLException sqlEx) {
					}
					prepSt = null;
				}
			}
			return true;
		} else {
			sqlError = true;
			return false;
		}
	}

	@Override
	public boolean deleteInstrument(long id) {
		sqlError = false;
		PreparedStatement prepSt = null;
		if (conectionHolder != null && !conectionHolder.isError()) {
			Connection conn = conectionHolder.getConnection();
			try {
				prepSt = conn.prepareStatement(DELETE_QUERY);
				prepSt.setLong(1, id);
				prepSt.execute();
				conectionHolder.closeConnection();
			} catch (SQLException e) {
				sqlError = true;
				e.printStackTrace();
			} finally {
				if (prepSt != null) {
					try {
						prepSt.close();
					} catch (SQLException sqlEx) {
					}
					prepSt = null;
				}
			}
		} else {
			sqlError = true;
		}
		return false;
	}

	@Override
	public void closeConection() {
		conectionHolder.closeConnection();

	}

	@Override
	public boolean updateInstrument(Instrument instrument) {
		if (conectionHolder != null && conectionHolder.getConnection() != null) {
			Connection conn = conectionHolder.getConnection();
			PreparedStatement prepSt = null;
			try {
				prepSt = conn.prepareStatement(UPDATE_QUERY);
				prepSt.setString(1, instrument.getName());
				prepSt.setString(2, instrument.getMeasure());
				prepSt.setString(3, instrument.getComment());
				prepSt.setFloat(4, instrument.getTotalNumber());
				prepSt.setLong(5, instrument.getId());
				prepSt.execute();
				conectionHolder.closeConnection();
			} catch (SQLException e) {
				sqlError = true;
				e.printStackTrace();
				return false;
			} finally {
				if (prepSt != null) {
					try {
						prepSt.close();
					} catch (SQLException sqlEx) {
					}
					prepSt = null;
				}
			}
			return true;
		} else {
			sqlError = true;
			return false;
		}
	}

}
