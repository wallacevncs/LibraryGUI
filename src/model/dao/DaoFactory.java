package model.dao;

import db.ConexaoDb;
import model.dao.impl.libraryDaoJDBC;

public class DaoFactory {
	
	public static libraryDao createLibraryDao() {
		return new libraryDaoJDBC(ConexaoDb.getConnection());
	}

}
