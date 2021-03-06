package sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.LocationDAO;
import models.Location;

public class SqliteLocationDAO implements LocationDAO {
	private final static String SELECT_ID_QUERY = "SELECT * FROM location WHERE id = ?";
	private final static String SELECT_NAME_QUERY = "SELECT * FROM location WHERE NAME = ?";
	private final static String INSERT_QUERY = "INSERT INTO location(name, boxes)" + " VALUES(?,?)";
	private final static String DELETE_QUERY = "DELETE FROM location WHERE id = ?";
	private final static String SELECT_ALL = "SELECT * FROM location";
	private final static String SELECT_ALL_WB = "SELECT * FROM location WHERE BOXES = '1'";
	private SQLConectionHolder conectionHolder;
	private boolean sqlError = false;

	public void setSqlError(boolean sqlError) {
		this.sqlError = sqlError;
	}

	public SQLConectionHolder getConectionHolder() {
		return conectionHolder;
	}

	public void setConectionHolder(SQLConectionHolder conectionHolder) {
		this.conectionHolder = conectionHolder;
	}

	@Override
	public boolean createLocation(Location location) {
		sqlError = false;
		if ( conectionHolder!=null&&conectionHolder.getConnection()!=null) {
			Connection conn = conectionHolder.getConnection();
			PreparedStatement prepSt = null;
			try {
				prepSt = conn.prepareStatement(INSERT_QUERY);
				prepSt.setString(1, location.getName());
				prepSt.setBoolean(2, location.isBoxes());
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

	@SuppressWarnings("null")
	@Override
	public Location getLocById(long id) {
		sqlError = false;
		ResultSet rs = null;
		PreparedStatement prepSt = null;
		Location loc = null;
		if ( conectionHolder!=null&&conectionHolder.getConnection()!=null) {
			Connection conn = conectionHolder.getConnection();
			try {
				prepSt = conn.prepareStatement(SELECT_ID_QUERY);
				prepSt.setLong(1, id);
				rs = prepSt.executeQuery();

				while (rs.next()) {
					loc = new Location();
					loc.setId(rs.getInt("id"));
					loc.setName(rs.getString("name"));
					loc.setBoxes(rs.getBoolean("boxes"));
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
		return loc;
	}

	@Override
	public Location getLocByName(String name) {
		sqlError = false;
		ResultSet rs = null;
		PreparedStatement prepSt = null;
		Location loc = null;
		if ( conectionHolder!=null&&conectionHolder.getConnection()!=null) {
			Connection conn = conectionHolder.getConnection();
			try {
				prepSt = conn.prepareStatement(SELECT_NAME_QUERY);
				prepSt.setString(1, name);
				rs = prepSt.executeQuery();

				while (rs.next()) {
					loc = new Location();
					loc.setId(rs.getInt("id"));
					loc.setName(rs.getString("name"));
					loc.setBoxes(rs.getBoolean("boxes"));
					break;
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
		return loc;
	}

	
	@Override
	public boolean deleteLocation(long id) {
		sqlError = false;
		PreparedStatement prepSt = null;
		if ( conectionHolder!=null&&conectionHolder.getConnection()!=null) {
			Connection conn = conectionHolder.getConnection();
			try {
				prepSt = conn.prepareStatement(DELETE_QUERY);
				prepSt.setLong(1, id);
				prepSt.execute();
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
	public boolean hasError() {
		return sqlError;
	}


	@Override
	public List<Location> getAllLocatin() {
		sqlError = false;
		ResultSet rs = null;
		PreparedStatement prepSt = null;
		List<Location> locList = new ArrayList<Location>();
		Location loc = null;
		if ( conectionHolder!=null&&conectionHolder.getConnection()!=null) {
			Connection conn = conectionHolder.getConnection();
			try {
				prepSt = conn.prepareStatement(SELECT_ALL);
				rs = prepSt.executeQuery();

				while (rs.next()) {
					loc = new Location();
					loc.setId(rs.getInt("id"));
					loc.setName(rs.getString("name"));
					loc.setBoxes(rs.getBoolean("boxes"));
					locList.add(loc);
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
		return locList;
	}

	@Override
	public List<Location> getAllLocatinWB() {
		sqlError = false;
		ResultSet rs = null;
		PreparedStatement prepSt = null;
		List<Location> locList = new ArrayList<Location>();
		Location loc = null;
		if ( conectionHolder!=null&&conectionHolder.getConnection()!=null) {
			Connection conn = conectionHolder.getConnection();
			try {
				prepSt = conn.prepareStatement(SELECT_ALL_WB);
				rs = prepSt.executeQuery();

				while (rs.next()) {
					loc = new Location();
					loc.setId(rs.getInt("id"));
					loc.setName(rs.getString("name"));
					loc.setBoxes(rs.getBoolean("boxes"));
					locList.add(loc);
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
		return locList;
	}
	@Override
	public void closeConection() {
		conectionHolder.closeConnection();
		
	}
}
