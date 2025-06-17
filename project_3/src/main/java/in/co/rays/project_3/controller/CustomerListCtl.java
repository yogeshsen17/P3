package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.CustomerDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.RoleModelInt;
import in.co.rays.project_3.model.CustomerModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * Customer List functionality controller.to perform Search and List operation.
 * 
 * @author YOGESH SEN
 *
 */
@WebServlet(name = "CustomerListCtl", urlPatterns = { "/ctl/CustomerListCtl" })
public class CustomerListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	 /** The log. */
	private static Logger log = Logger.getLogger(CustomerListCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {
		
		
		CustomerModelInt model = ModelFactory.getInstance().getCustomerModel();

		try {

			List clist = model.list(0, 0);

			request.setAttribute("clientName", clist);

		} catch (ApplicationException e) {
			e.printStackTrace();
		}


		Map<Integer, String> map = new HashMap();

		map.put(1, "High");
		map.put(2, "Medium");
		map.put(3, "Low");

		request.setAttribute("importance", map);

	}
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		
		CustomerDTO dto = new CustomerDTO();

		dto.setClientName(DataUtility.getString(request.getParameter("clientName")));

		dto.setLocation(DataUtility.getString(request.getParameter("location")));
		
		dto.setContactNumber(DataUtility.getLong(request.getParameter("contactNumber")));

		dto.setImportance(DataUtility.getInt(request.getParameter("Importance"))); 
		
		System.out.println(dto);
				
		return dto;
		
	}

	 /**
		 * Contains Display logics.
		 *
		 * @param request the request
		 * @param response the response
		 * @throws ServletException the servlet exception
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CustomerListCtl doGet Start");
		List list;
		List next;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		CustomerDTO dto = (CustomerDTO) populateDTO(request);

		// get the selected checkbox ids array for delete list
		CustomerModelInt model = ModelFactory.getInstance().getCustomerModel();
		try {
			list = model.search(dto, pageNo, pageSize);

			ArrayList<CustomerDTO> a = (ArrayList<CustomerDTO>) list;

			next = model.search(dto, pageNo + 1, pageSize);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);

			} else {
				request.setAttribute("nextListSize", next.size());
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (Exception e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("CustomerListCtl doPOst End");
	}

	  /**
		 * Contains Submit logics.
		 *
		 * @param request the request
		 * @param response the response
		 * @throws ServletException the servlet exception
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CustomerListCtl doPost Start");
		List list = null;
		List next = null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		CustomerDTO dto = (CustomerDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		System.out.println("op---->" + op);

// get the selected checkbox ids array for delete list
		String[] ids = request.getParameterValues("ids");
		CustomerModelInt model = ModelFactory.getInstance().getCustomerModel();
		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.CUSTOMER_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.CUSTOMER_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					CustomerDTO deletedto = new CustomerDTO();
					for (String id : ids) {
						deletedto.setId(DataUtility.getLong(id));
						model.delete(deletedto);
						ServletUtility.setSuccessMessage("Data Deleted Successfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			}
			if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.CUSTOMER_LIST_CTL, request, response);
				return;
			}
			dto = (CustomerDTO) populateDTO(request); 

			list = model.search(dto, pageNo, pageSize);

			ServletUtility.setDto(dto, request);
			next = model.search(dto, pageNo + 1, pageSize);

			ServletUtility.setList(list, request);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				if (!OP_DELETE.equalsIgnoreCase(op)) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
			}
			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);

			} else {
				request.setAttribute("nextListSize", next.size());
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (Exception e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("CustomerListCtl doGet End");
	}

	@Override
	protected String getView() {
		return ORSView.CUSTOMER_LIST_VIEW;
	}

}