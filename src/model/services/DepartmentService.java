package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {

	//vamos criar uma dependeência para conectar com banco
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	//Antes de implementar DB
	//Vamos add uma lista p test
	public List<Department> findAll(){
		
		return dao.findAll();
//		
//		List<Department> list = new ArrayList<>();
//		list.add(new Department(1, "Books"));
//		list.add(new Department(2, "Computers"));
//		list.add(new Department(3, "Electronics"));
//		
//		return list;
	}
}
