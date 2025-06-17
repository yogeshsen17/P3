package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.CartDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.CartModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "CartListCtl", urlPatterns = { "/ctl/CartListCtl" })
public class CartListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	/** The log. */
	private static Logger log = Logger.getLogger(CartListCtl.class);

	protected void preload(HttpServletRequest request) {

		CartModelInt cartmodel = ModelFactory.getInstance().getCartModel();

		try {
//			List list = model.list();
//			List ulist = umodel.list();
//			request.setAttribute("roleList", list);
//			request.setAttribute("uList", ulist);

			HashMap<Integer, String> map = new HashMap();

			map.put(1, "Tablet");
			map.put(2, "Mobile");
			map.put(3, "Laptop");
			map.put(4, "Fridge");
			map.put(5, "Speaker");
			map.put(6, "Ac");

			request.setAttribute("productList", map);
		} catch (Exception e) {
			log.error(e);

		}
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		CartDTO dto = new CartDTO();

		dto.setCustomerName(DataUtility.getString(request.getParameter("customerName")));
		dto.setProductName(DataUtility.getString(request.getParameter("product")));
		dto.setTransactionDate(DataUtility.getDate(request.getParameter("tDate")));
		dto.setProductQuantity(DataUtility.getInt(request.getParameter("productQuantity")));
		populateBean(dto, request);
		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CartListCtl doGet Start");
		List list;
		List next;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		CartDTO dto = (CartDTO) populateDTO(request);

		// get the selected checkbox ids array for delete list
		CartModelInt model = ModelFactory.getInstance().getCartModel();
		try {
			list = model.search(dto, pageNo, pageSize);

			ArrayList<CartDTO> a = (ArrayList<CartDTO>) list;

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
		log.debug("CartListCtl doPOst End");
	}

	/**
	 * Contains Submit logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CartListCtl doPost Start");
		List list = null;
		List next = null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		CartDTO dto = (CartDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		System.out.println("op---->" + op);

// get the selected checkbox ids array for delete list
		String[] ids = request.getParameterValues("ids");
		CartModelInt model = ModelFactory.getInstance().getCartModel();
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
				ServletUtility.redirect(ORSView.CART_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.CART_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					CartDTO deletedto = new CartDTO();
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
				ServletUtility.redirect(ORSView.CART_LIST_CTL, request, response);
				return;
			}
			dto = (CartDTO) populateDTO(request);

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
		log.debug("CartListCtl doGet End");
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.CART_LIST_VIEW;
	}

}