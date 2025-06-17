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
import in.co.rays.project_3.dto.PurchaseDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.RoleModelInt;
import in.co.rays.project_3.model.PurchaseModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * Purchase List functionality controller.to perform Search and List operation.
 * 
 * @author YOGESH SEN
 *
 */
@WebServlet(name = "PurchaseListCtl", urlPatterns = { "/ctl/PurchaseListCtl" })
public class PurchaseListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	 /** The log. */
	private static Logger log = Logger.getLogger(PurchaseListCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {
		
		
		PurchaseModelInt model = ModelFactory.getInstance().getPurchaseModel();

		try {

			List clist = model.list(0, 0);

			request.setAttribute("totalQuantity", clist);

		} catch (ApplicationException e) {
			e.printStackTrace();
		}


		Map<Integer, String> map = new HashMap();

		map.put(1, "Mobile");
		map.put(2, "Laptop");
		map.put(3, "Shoes");
		map.put(4, "Perfume");
		map.put(5, "Book");

		request.setAttribute("product", map);

	}
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		
		PurchaseDTO dto = new PurchaseDTO();

		dto.setTotalQuantity(DataUtility.getInt(request.getParameter("totalQuantity")));

		dto.setOrderDate(DataUtility.getDate(request.getParameter("orderDate")));
		
		dto.setTotalCost(DataUtility.getInt(request.getParameter("totalCost")));

		dto.setProduct(DataUtility.getInt(request.getParameter("product")));
		
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
		log.debug("PurchaseListCtl doGet Start");
		List list;
		List next;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		PurchaseDTO dto = (PurchaseDTO) populateDTO(request);

		// get the selected checkbox ids array for delete list
		PurchaseModelInt model = ModelFactory.getInstance().getPurchaseModel();
		try {
			list = model.search(dto, pageNo, pageSize);

			ArrayList<PurchaseDTO> a = (ArrayList<PurchaseDTO>) list;

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
		log.debug("PurchaseListCtl doPOst End");
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
		log.debug("PurchaseListCtl doPost Start");
		List list = null;
		List next = null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		PurchaseDTO dto = (PurchaseDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		System.out.println("op---->" + op);

// get the selected checkbox ids array for delete list
		String[] ids = request.getParameterValues("ids");
		PurchaseModelInt model = ModelFactory.getInstance().getPurchaseModel();
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
				ServletUtility.redirect(ORSView.PURCHASE_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.PURCHASE_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					PurchaseDTO deletedto = new PurchaseDTO();
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
				ServletUtility.redirect(ORSView.PURCHASE_LIST_CTL, request, response);
				return;
			}
			dto = (PurchaseDTO) populateDTO(request); 

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
		log.debug("PurchaseListCtl doGet End");
	}

	@Override
	protected String getView() {
		return ORSView.PURCHASE_LIST_VIEW;
	}

}