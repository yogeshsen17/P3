package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.CustomerDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.exception.RecordNotFoundException;

public interface CustomerModelInt { 
	
	public long add(CustomerDTO dto) throws ApplicationException, DuplicateRecordException;
	
	public void update(CustomerDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(CustomerDTO dto) throws ApplicationException;

	public CustomerDTO findByPK(long pk) throws ApplicationException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(CustomerDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public List search(CustomerDTO dto) throws ApplicationException;


}
