package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {

	//vamos criar uma dependeência para conectar com banco
	private SellerDao dao = DaoFactory.createSellerDao();
	
	//listar
	public List<Seller> findAll(){
		
		return dao.findAll();
	}
	
	//Save
	public void  saveOrUpdate(Seller obj) {
		//verificar -> se o id é null que dizer não tem esse id inserido
		if(obj.getId() == null) {
			dao.insert(obj); // insiro novo Departamento
		}
		else {
			//caso ja exista
			dao.update(obj); //atualizo 
		}
	}
	//deletar
	public void remove(Seller obj) {
		dao.deleteById(obj.getId());
	}
	
	
//	//Antes de implementar DB
//	//Vamos add uma lista p test
//	public List<Seller> findAll(){
//		
//		
//		List<Seller> list = new ArrayList<>();
//		list.add(new Seller(1, "Books"));
//		list.add(new Seller(2, "Computers"));
//		list.add(new Seller(3, "Electronics"));
//		
//		return list;
//	}
	
	
}
