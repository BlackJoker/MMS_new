package de.team55.mms.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import de.team55.mms.data.Modul;
import de.team55.mms.data.Modulhandbuch;
import de.team55.mms.data.StellvertreterList;
import de.team55.mms.data.Studiengang;
import de.team55.mms.data.User;
import de.team55.mms.data.Zuordnung;
import de.team55.mms.gui.mainscreen;

public class sql {

	String url = "com.mysql.jdbc.Driver";
	ResultSet rs = null;
	private Connection con = null;
	private static boolean connected = false;
	private int FAILED = 0;
	private int SUCCES = 1;

	// Hostname
	private static String dbHost = "db4free.net";

	// Port -- Standard: 3306
	private String dbPort = "3306";

	// Datenbankname
	private String database = "mms4sopra2";

	// Datenbankuser
	private String dbUser = "team5526";

	// Datenbankpasswort
	private String dbPassword = "qwert710";

	public boolean connect() {
		// TODO alle Tabellen am ende sobald da Prog fertig ist anpassen!!!!!
		connected = false;
		try {
			// connect to the server
			Class.forName(url);
			this.con = DriverManager.getConnection("jdbc:mysql://" + dbHost
					+ ":" + dbPort + "/" + database + "?" + "user=" + dbUser
					+ "&" + "password=" + dbPassword);
			this.con.setAutoCommit(false);
			// user table
			Statement stmt = this.con.createStatement();

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `module` ("
					+ "  `name` varchar(255) NOT NULL,"
					+ "  `Modulhandbuchname` varchar(255) NOT NULL,"
					+ "  `Version` int(11) NOT NULL,"
					+ "  `Datum` date NOT NULL,"
					+ "  `akzeptiert` BOOLEAN NOT NULL DEFAULT '0',"
					+ "  `inbearbeitung` BOOLEAN NOT NULL DEFAULT '0',"
					+ "  `sid` int(10) NOT NULL" + ") ");
			this.con.commit();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `modulhandbuch` ("
					+ "  `name` varchar(255) NOT NULL,"
					+ "  `studiengang` varchar(255) DEFAULT NULL,"
					+ "  `jahrgang` varchar(255) NOT NULL,"
					+ "  `akzeptiert` BOOLEAN NOT NULL DEFAULT '0'" + ") ");
			this.con.commit();

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `rights` ("
					+ "  `id` int(11) NOT NULL,"
					+ "  `userchange` BOOLEAN NOT NULL,"
					+ "  `modcreate` BOOLEAN NOT NULL,"
					+ "  `modacc` BOOLEAN NOT NULL,"
					+ "  `modread` BOOLEAN NOT NULL,"
					+ "  `adminwork` BOOLEAN NOT NULL" + ")");
			this.con.commit();

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `studiengang` ("
					+ "  `id` int(10) NOT NULL AUTO_INCREMENT,"
					+ "  `name` varchar(255) NOT NULL,"
					+ "  PRIMARY KEY (`id`),"
					+ "  UNIQUE KEY `Studiengang` (`name`)" + ")");
			this.con.commit();

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `text` ("
					+ "  `name` varchar(255) NOT NULL,"
					+ "  `version` int(11) NOT NULL,"
					+ "  `label` varchar(255) NOT NULL,"
					+ "  `text` varchar(255) NOT NULL,"
					+ "  `dezernat2` BOOLEAN NOT NULL DEFAULT '0'" + ")");
			this.con.commit();

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `user` ("
					+ "  `id` int(11) NOT NULL AUTO_INCREMENT,"
					+ "  `email` varchar(255) NOT NULL,"
					+ "  `titel` varchar(255) DEFAULT NULL,"
					+ "  `vorname` varchar(255) DEFAULT NULL,"
					+ "  `namen` varchar(255) DEFAULT NULL,"
					+ "  `password` varchar(255) NOT NULL,"
					+ "  PRIMARY KEY (`id`),"
					+ "  UNIQUE KEY `email` (`email`)" + ")");
			this.con.commit();

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `user_relation` ("
					+ "  `main_email` varchar(255) NOT NULL,"
					+ "  `sellver_email` varchar(255) NOT NULL" + ")");
			this.con.commit();

			stmt.executeUpdate("INSERT IGNORE INTO `user` (`id`, `email`, `titel`, `vorname`, `namen`, `password`) VALUES"
					+ "	(1, 'admin@mms.de', NULL, 'Admin', 'Admin', 'a384b6463fc216a5f8ecb6670f86456a');");
			this.con.commit();
			stmt.executeUpdate("INSERT IGNORE INTO `rights` (`id`, `userchange`, `modcreate`, `modacc`, `modread`) VALUES"
					+ "	(1, 1, 1, 1, 1);");
			this.con.commit();
			stmt.close();
			connected = true;

		} catch (SQLException e) {
			// TODO fehler fenster aufrufen
			e.printStackTrace();
			mainscreen.noConnection();
			connected = false;

		} catch (ClassNotFoundException e) {
			// TODO fehler fenster aufrufen
			e.printStackTrace();
			connected = false;
		}

		return connected;
	}

	public void deluser(String email) {
		if (connect() == true) {
			Statement state = null;
			ResultSet res = null;
			int id = -1;
			try {
				state = this.con.createStatement();
				res = state.executeQuery("SELECT id FROM user WHERE email='"
						+ email + "';");
				if (res.first())
					id = res.getInt("id");
				res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			if (id != -1) {
				try {
					state = this.con.createStatement();
					state.executeUpdate("DELETE FROM user WHERE id = " + id
							+ ";");
					state.executeUpdate("DELETE FROM rights WHERE id = " + id
							+ ";");
				} catch (SQLException e) {
					// TODO fehler fenster aufrufen
					System.out.print(e.getMessage());
				} finally {
					try {
						this.con.commit();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						state.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				System.out.print("Failed to write rights!");
			}
			disconnect();
		}
	}

	public void disconnect() {
		try {
			this.con.commit();
			this.con.setAutoCommit(true);
			this.con.close();
		} catch (SQLException e) {
			// TODO fehler fenster aufrufen
			System.out.print(e.getMessage());
		}

	}

	public int getAnzahlStudiengaenge() {
		ResultSet res = null;
		Statement state = null;
		int cnt = 0;
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				res = state
						.executeQuery("SELECT COUNT(id) AS cnt FROM studiengang;");
				while (res.next()) {
					cnt = res.getInt("cnt");
				}
				res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return cnt;

	}

	public Modul getModul(String name) {
		ResultSet res = null;
		Statement state = null;
		int version = 0;
		String Modulhandbuch = "";
		ArrayList<Studiengang> Studiengang = new ArrayList<Studiengang>();
		Date datum = new Date();
		boolean akzeptiert = false;
		boolean inbearbeitung = false;
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<Boolean> dezernat = new ArrayList<Boolean>();
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				res = state
						.executeQuery("SELECT IFNULL(MAX(Version),0) as version FROM module WHERE = '"
								+ name + "';");
				if (res.first()) {
					version = res.getInt("version");
				}

				if (version != 0) {
					res = state
							.executeQuery("SELECT *, s.name AS sname FROM module AS m JOIN studiengang as s ON s.id=m.sid WHERE name = '"
									+ name + "'AND version =" + version + ";");
					if (res.first()) {
						Modulhandbuch = res.getString("Modulhandbuchname");
						datum = res.getDate("Datum");
						akzeptiert = res.getBoolean("akzeptiert");
						inbearbeitung = res.getBoolean("inbearbeitung");
						Studiengang.add(new Studiengang(res.getInt("sid"), res
								.getString("sname")));
					}
					while (res.next()) {
						Studiengang.add(new Studiengang(res.getInt("sid"), res
								.getString("sname")));
					}
				}
				res.close();
				state.close();
			} catch (SQLException e) {

			}
			try {
				state = this.con.createStatement();
				res = state
						.executeQuery("SELECT label, text, dezernat FROM text WHERE name = '"
								+ name + "' AND version = " + version + ";");

				while (res.next()) {
					labels.add(res.getString("label"));
					values.add(res.getString("text"));
					dezernat.add(res.getBoolean("dezernat"));
				}

				res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return new Modul(name, Studiengang, Modulhandbuch, version, datum,
				labels, values, dezernat, akzeptiert, inbearbeitung);

	}

	public ArrayList<Modulhandbuch> getModulhandbuch(String studiengang) {
		ResultSet res = null;
		Statement state = null;
		ArrayList<Modulhandbuch> modbuch = new ArrayList<Modulhandbuch>();
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				res = state
						.executeQuery("SELECT m.jahrgang, m.sid FROM modulhandbuch AS m JOIN studiengang AS s on s.id = m.sid where studiengang.name = '"
								+ studiengang + "';");

				while (res.next()) {
					String jg = res.getString("jahrgang");
					int sid = res.getInt("sid");
					boolean ack = res.getBoolean("akzeptiert");
					modbuch.add(new Modulhandbuch(jg, sid));
				}

				res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return modbuch;

	}

	public int getModulVersion(String name) {
		ResultSet res = null;
		Statement state = null;
		int version = 0;
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				String q = "SELECT IFNULL(MAX(Version),0) AS Version FROM module WHERE name = '"
						+ name + "';";
				res = state.executeQuery(q);
				if (res.first()) {
					version = res.getInt("Version");
				}
				res.close();
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disconnect();
		}
		return version;
	}

	public ArrayList<Studiengang> getStudiengaenge() {
		ResultSet res = null;
		Statement state = null;
		ArrayList<Studiengang> sgs = new ArrayList<Studiengang>();
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				res = state.executeQuery("SELECT * FROM studiengang;");
				// verarbeitung der resultset
				while (res.next()) {
					int id = res.getInt("id");
					String name = res.getString("name");
					sgs.add(new Studiengang(id, name));
				}
				res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return sgs;

	}

	public User getUser(String email, String pass) {
		User zws = null;
		ResultSet res = null;
		Statement state = null;
		int id = -1;
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				res = state
						.executeQuery("SELECT u.*,userchange,modcreate,modacc,modread FROM user AS u JOIN rights AS r ON u.id=r.id WHERE email='"
								+ email + "' and password='" + pass + "';");
				if (res.first()) {
					zws = new User(res.getString("vorname"),
							res.getString("namen"), res.getString("titel"),
							res.getString("email"), res.getString("password"),
							res.getBoolean("userchange"),
							res.getBoolean("modcreate"),
							res.getBoolean("modacc"), res.getBoolean("modread"));
				}
				res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return zws;

	}

	public boolean isConnected() {
		return connected;
	}

	public int setModul(Modul neu) {
		int ok = FAILED;
		PreparedStatement state = null;
		if (connect() == true) {
			ArrayList<Zuordnung> typen = neu.getZuordnungen();
			ArrayList<Modulhandbuch> mb = neu.getModulhandbuch();
			String name = neu.getName();
			int version = neu.getVersion();
			ArrayList<String> labels = neu.getLabels();
			ArrayList<String> values = neu.getValues();
			ArrayList<Boolean> dezernat = neu.getDezernat();
			try {
				for (int i = 0; i < typen.size(); i++) {
					state = con
							.prepareStatement("INSERT INTO module (name, jahrgang, version, datum, typid) VALUES(?,?,?,?,?)");
					state.setString(1, name);
					state.setInt(2, neu.getJahrgang());
					state.setInt(3, version);
					state.setDate(4, convertToSQLDate(neu.getDatum()));
					state.setInt(5, typen.get(i).getId());
					state.executeUpdate();
				}
				/*
				 * state = con .prepareStatement(
				 * "INSERT INTO modulhandbuch (name, studiengang, jahrgang) VALUES(?,?,?)"
				 * ); state.setString(1, neu.getModulhandbuch());
				 * state.setString(2, neu.getStudiengang()); state.setString(3,
				 * neu.getJahrgang()); state.executeUpdate();
				 */
				state = con
						.prepareStatement("INSERT INTO text (name,version, label, text, dezernat2) VALUES(?,?,?,?,?)");
				for (int i = 0; i < labels.size(); i++) {
					state.setString(1, name);
					state.setInt(2, version);
					state.setString(3, labels.get(i));
					state.setString(4, values.get(i));
					state.setBoolean(5, dezernat.get(i));
					state.executeUpdate();
				}
				state.close();
				ok = SUCCES;
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return ok;

	}

	/*
	 * Hier muss man leider noch mehr machen, da ich nicht wei� wie du dich
	 * entschieden hast den dynmaischen text zu speichern
	 */
	public void setModul(String name, int version /* LISTE? aka Text zeug */) {
		// ResultSet res = null;
		Statement state = null;
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				state.executeUpdate("");
				// res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			try {
				state = this.con.createStatement();
				state.executeUpdate("");
				// res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}

	}

	// public void setModulhandbuch(Modulhandbuch mh) {
	// String name = mh.getName();
	// String studiengang = mh.getStudiengang();
	// String jahrgang = mh.getJahrgang();
	// Statement state = null;
	// if (connect() == true) {
	// try {
	// state = this.con.createStatement();
	// state.executeUpdate("INSERT INTO modulhandbuch (name, studiengang, jahrgang) VALUES ('"
	// + name
	// + "', '"
	// + studiengang
	// + "', '"
	// + jahrgang
	// + "');");
	//
	// } catch (SQLException e) {
	// // TODO fehler fenster aufrufen
	// e.printStackTrace();
	// }
	// disconnect();
	// }
	//
	// }

	// Neuen Studiengang anlegen
	public int setStudiengang(String name) {
		Statement state = null;
		int status = FAILED;
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				state.executeUpdate("INSERT INTO studiengang (name) VALUES ('"
						+ name + "');");
				status = SUCCES;
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return status;

	}

	public ArrayList<User> userload() {
		User zws = null;
		ResultSet res = null;
		Statement state = null;
		int i = 0;
		int j = 0;
		ArrayList<User> list = new ArrayList<User>();
		if (connect() == true) {
			try {
				state = con.createStatement();
				res = state
						.executeQuery("SELECT u.*,userchange,modcreate,modacc,modread FROM user AS u JOIN rights AS r ON u.id=r.id;");
				while (res.next()) {
					zws = new User(res.getString("vorname"),
							res.getString("namen"), res.getString("titel"),
							res.getString("email"), res.getString("password"),
							res.getBoolean("userchange"),
							res.getBoolean("modcreate"),
							res.getBoolean("modacc"), res.getBoolean("modread"));
					list.add(zws);
				}
				res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return list;

	}

	public int usersave(User user) {
		int ok = FAILED;
		if (connect() == true) {
			PreparedStatement state = null;
			ResultSet res = null;
			int id = -1;
			try {
				state = con
						.prepareStatement("INSERT INTO user (email,vorname,namen,password, titel) VALUES (?,?,?,?,?)");
				state.setString(1, user.geteMail());
				state.setString(2, user.getVorname());
				state.setString(3, user.getNachname());
				state.setString(4, user.getPassword());
				state.setString(5, user.getTitel());
				state.executeUpdate();
				con.commit();
				state.close();

				state = con
						.prepareStatement("SELECT id FROM user WHERE email=?");
				state.setString(1, user.geteMail());
				res = state.executeQuery();
				if (res.first())
					id = res.getInt("id");
				res.close();
				state.close();
			} catch (SQLException e) {
				// e.printStackTrace();
				ok = FAILED;
			}
			if (id != -1) {
				try {
					state = con
							.prepareStatement("INSERT INTO rights (id,userchange,modcreate,modacc,modread) VALUES (?,?,?,?,?)");
					state.setInt(1, id);
					state.setBoolean(2, user.getManageUsers());
					state.setBoolean(3, user.getCreateModule());
					state.setBoolean(4, user.getAcceptModule());
					state.setBoolean(5, user.getReadModule());
					state.executeUpdate();
					con.commit();
					state.close();
					ok = SUCCES;

				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				ok = FAILED;
			}
			disconnect();
		}
		return ok;
	}

	public int userupdate(User user, String email) {
		int ok = FAILED;
		if (connect() == true) {
			PreparedStatement state = null;
			ResultSet res = null;
			int id = -1;
			boolean userexists = false;
			try {
				if (!user.geteMail().equals(email)) {
					state = con
							.prepareStatement("SELECT IFNULL(id,0) AS id FROM user WHERE email=?;");
					state.setString(1, user.geteMail());
					res = state.executeQuery();
					if (res.first()) {
						if (res.getInt("id") != 0) {
							userexists = true;
							ok = FAILED;
						}
					}
					res.close();
					state.close();
				}
				if (!userexists) {
					state = con
							.prepareStatement("SELECT IFNULL(id,0) AS id FROM user WHERE email=?;");
					state.setString(1, email);
					res = state.executeQuery();
					if (res.first()) {
						id = res.getInt("id");
					}
					res.close();
					state.close();
					if (id != 0) {
						if ((user.getPassword() != null)
								&& !user.getPassword().equals("null")) {
							state = con
									.prepareStatement("UPDATE user SET titel = ?, vorname = ?, namen = ?, password = ?, email = ? WHERE id = ? ;");
							state.setString(1, user.getTitel());
							state.setString(2, user.getVorname());
							state.setString(3, user.getNachname());
							state.setString(4, user.getPassword());
							state.setString(5, user.geteMail());
							state.setInt(6, id);
							state.executeUpdate();
							state.close();
						} else {
							state = con
									.prepareStatement("UPDATE user SET titel = ?, vorname = ?, namen = ?, email = ? WHERE id = ? ;");
							state.setString(1, user.getTitel());
							state.setString(2, user.getVorname());
							state.setString(3, user.getNachname());
							state.setString(4, user.geteMail());
							state.setInt(5, id);
							state.executeUpdate();
							state.close();
						}

						state = con
								.prepareStatement("UPDATE rights SET userchange = ?, modcreate =?, modacc =?, modread =? WHERE id = ?;");
						state.setBoolean(1, user.getManageUsers());
						state.setBoolean(2, user.getCreateModule());
						state.setBoolean(3, user.getAcceptModule());
						state.setBoolean(4, user.getReadModule());
						state.setInt(5, id);
						state.executeUpdate();
						con.commit();
						state.close();
						ok = SUCCES;

					} else {
						ok = FAILED;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disconnect();
		}
		return ok;
	}

	private java.sql.Date convertToSQLDate(java.util.Date utilDate) {
		return new java.sql.Date(utilDate.getTime());
	}

	public int getStudiengangID(String name) {
		ResultSet res = null;
		Statement state = null;
		int id = 0;
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				res = state
						.executeQuery("SELECT id FROM studiengang WHERE name ='"
								+ name + "';");
				// verarbeitung der resultset
				if (res.next()) {
					id = res.getInt("id");
				}
				res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return id;
	}

	// public ArrayList<Modulhandbuch> getModulhandbuecher() {
	// ResultSet res = null;
	// Statement state = null;
	// ArrayList<Modulhandbuch> MHs = new ArrayList<Modulhandbuch>();
	// if (connect() == true) {
	// try {
	// state = this.con.createStatement();
	// res = state.executeQuery("SELECT * FROM modulhandbuch;");
	// // verarbeitung der resultset
	// while (res.next()) {
	// String name = res.getString("name");
	// String sg = res.getString("studiengang");
	// String jg = res.getString("jahrgang");
	// boolean ack = res.getBoolean("akzeptiert");
	// MHs.add(new Modulhandbuch(name, sg, jg, ack));
	// }
	//
	// res.close();
	// state.close();
	// } catch (SQLException e) {
	// // TODO fehler fenster aufrufen
	// e.printStackTrace();
	// }
	// disconnect();
	// }
	// return MHs;
	// }

	public ArrayList<Modul> getModule(boolean b) {
		ArrayList<Modul> module = new ArrayList<Modul>();
		ResultSet res = null;
		Statement state = null;
		Statement state2 = null;
		boolean ack;
		if (b == false) {
			ack = true;
		} else {
			ack = false;
		}
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				state2 = this.con.createStatement();
				res = state2
						.executeQuery("SELECT DISTINCT name FROM module ORDER BY name ASC;");

				while (res.next()) {
					String name = res.getString("name");
					String q = "SELECT IFNULL(MAX(Version),0) AS Version FROM module WHERE name = '"
							+ name + "';";
					ResultSet res2 = state.executeQuery(q);
					int version = 0;
					if (res2.first()) {
						version = res2.getInt("Version");
					}

					ArrayList<Studiengang> sgs = new ArrayList<Studiengang>();
					res2 = state
							.executeQuery("SELECT m.*, s.name AS sname FROM module AS m JOIN studiengang AS s ON sid=id WHERE m.name = '"
									+ name + "' AND version=" + version + ";");
					String mh = "";
					Date datum = null;
					boolean inedit = false;
					if (res2.first()) {
						ack = res2.getBoolean("akzeptiert");
						mh = res2.getString("modulhandbuchname");
						datum = res2.getDate("datum");
						inedit = res2.getBoolean("inbearbeitung");
						int sid = res2.getInt("sid");
						String sname = res2.getString("sname");
						sgs.add(new Studiengang(sid, sname));
					}
					if (b == ack) {
						while (res2.next()) {
							int sid = res2.getInt("sid");
							String sname = res2.getString("sname");
							sgs.add(new Studiengang(sid, sname));
						}

						ArrayList<String> labels = new ArrayList<String>();
						ArrayList<String> values = new ArrayList<String>();
						ArrayList<Boolean> dezernat = new ArrayList<Boolean>();

						res2 = state
								.executeQuery("SELECT label, text, dezernat2 FROM text WHERE name = '"
										+ name
										+ "' AND version = "
										+ version
										+ ";");

						while (res2.next()) {
							labels.add(res2.getString("label"));
							values.add(res2.getString("text"));
							dezernat.add(res2.getBoolean("dezernat2"));
						}
						res2.close();

						module.add(new Modul(name, sgs, mh, version, datum,
								labels, values, dezernat, ack, inedit));
					}

				}
				res.close();
				state.close();
				state2.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return module;
	}

	public int getUser(String email) {
		ResultSet res = null;
		Statement state = null;
		int status = FAILED;
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				res = state
						.executeQuery("SELECT IFNULL(id,0) AS id FROM user WHERE email='"
								+ email + "';");
				if (res.first()) {
					if (res.getInt("id") == 0)
						status = SUCCES;
				}
				res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return status;
	}

	// bitte durchschaun ob das wirklich so funktionieren kann xD
	public int setUserRelation(User main, User stellv) {
		Statement state = null;
		int status = FAILED;

		if (connect() == true) {
			try {
				state = this.con.createStatement();
				state.executeUpdate("INSERT INTO user_relation (main_email, stellver_email) VALUES ('"
						+ main.geteMail() + "','" + stellv.geteMail() + "');");
				status = SUCCES;
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return status;

	}

	// �berlegung wie man das einbaut

	// public ArrayList<User> getUserRelation(User main){
	// ResultSet res = null;
	// Statement state = null;
	// ArrayList<User> rellist = new ArrayList<User>();
	//
	// if (connect() == true) {
	// try {
	// state = this.con.createStatement();
	// res = state
	// .executeQuery("SELECT st.email, st.titel, st.vorname, st.namen FROM user as st  id=;");
	// while (res.next()) {
	// if(res.getInt("id")==0);
	//
	// }
	// res.close();
	// state.close();
	// } catch (SQLException e) {
	// // TODO fehler fenster aufrufen
	// e.printStackTrace();
	// }
	// disconnect();
	// }
	// return rellist;
	// }

	public ArrayList<String> getallModultyp() {
		ResultSet res = null;
		Statement state = null;
		ArrayList<String> retyp = new ArrayList<String>();
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				res = state.executeQuery("select name from moduletyp;");

				while (res.next()) {
					String typ = res.getString("name");
					retyp.add(typ);
				}

				res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return retyp;

	}

	public ArrayList<Zuordnung> getZuordnungen() {
		ResultSet res = null;
		Statement state = null;
		ArrayList<Zuordnung> zlist = new ArrayList<Zuordnung>();
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				res = state
						.executeQuery("SELECT t.tid, t.tname, t.abschluss, t.sid, s.name FROM typ AS t JOIN studiengang AS s ON t.sid=s.id ORDER BY t.tName ASC, s.name ASC, t.abschluss ASC;");
				while (res.next()) {
					zlist.add(new Zuordnung(res.getInt("tid"), res
							.getString("tname"), res.getString("name"), res
							.getInt("sid"), res.getString("abschluss")));
				}
				res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return zlist;
	}

	public int setZuordnung(Zuordnung z) {
		Statement state = null;
		int status = FAILED;
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				state.executeUpdate("INSERT INTO typ (tname, sid, abschluss) VALUES ('"
						+ z.getName()
						+ "','"
						+ z.getSid()
						+ "','"
						+ z.getAbschluss() + "');");
				status = SUCCES;
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return status;
	}

	public int setStellvertreter(StellvertreterList sl) {
		PreparedStatement state = null;
		int status = FAILED;
		if (connect() == true) {
			try {
				String eMail = sl.geteMail();
				ArrayList<String> l = sl.getUsr();
				state = con
						.prepareStatement("DELETE FROM user_relation WHERE main_email=?");
				state.setString(1, eMail);
				state.executeUpdate();
				for (int i = 0; i < l.size(); i++) {
					state = con
							.prepareStatement("INSERT INTO user_relation (main_email, stellver_email) VALUES(?,?)");
					state.setString(1, eMail);
					state.setString(2, l.get(i));
					state.executeUpdate();
				}
				status = SUCCES;
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return status;
	}

	public ArrayList<User> getStellv(String mail) {
		ResultSet res = null;
		Statement state = null;
		ArrayList<User> stellv = new ArrayList<User>();
		if (connect() == true) {
			try {
				state = this.con.createStatement();
				res = state
						.executeQuery("SELECT * FROM user_relation as ur JOIN user AS u ON ur.stellver_email=u.email JOIN rights AS r ON u.id=r.id WHERE main_email='"+mail+"';");
				while (res.next()) {
					stellv.add(new User(res.getString("vorname"),
							res.getString("namen"), res.getString("titel"),
							res.getString("email"), res.getString("password"),
							res.getBoolean("userchange"),
							res.getBoolean("modcreate"),
							res.getBoolean("modacc"), res.getBoolean("modread")));
				}
				res.close();
				state.close();
			} catch (SQLException e) {
				// TODO fehler fenster aufrufen
				e.printStackTrace();
			}
			disconnect();
		}
		return stellv;
	}

}
