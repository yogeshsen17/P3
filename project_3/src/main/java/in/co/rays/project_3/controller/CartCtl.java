package in.co.rays.project_3.controller;

import java.io.IOException;
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
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.CartModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "CartCtl", urlPatterns = { "/ctl/CartCtl" })
public class CartCtl extends BaseCtl {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** The log. */
	private static Logger log = Logger.getLogger(CartCtl.class);

	protected void preload(HttpServletRequest request) {

		CartModelInt cartmodel = ModelFactory.getInstance().getCartModel();
		try {

			HashMap<Integer, String> map = new HashMap();

			map.put(1, "Tablet");
			map.put(2, "Mobile");
			map.put(3, "Laptop");
			map.put(4, "Fridge");
			map.put(5, "Speaker");
			map.put(6, "Ac");

			request.setAttribute("productList", map);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		System.out.println("-------------validate started-------------");

		if (DataValidator.isNull(request.getParameter("customerName"))) {
			request.setAttribute("customerName", PropertyReader.getValue("error.require", "Customer Name"));
			System.out.println(pass);
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("customerName"))) {
			request.setAttribute("customerName", "Customer Name must contains alphabets only");
			System.out.println(pass);
			pass = false;

		}
		if (DataValidator.isNull(request.getParameter("product"))) {
			request.setAttribute("product", PropertyReader.getValue("error.require", "Product Name"));
			System.out.println(pass);
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("tDate"))) {
			request.setAttribute("tDate", PropertyReader.getValue("error.require", "Transaction Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("tDate"))) {
			request.setAttribute("tDate", PropertyReader.getValue("error.date", "Transaction Date"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("productQuantity"))) {
			request.setAttribute("productQuantity", PropertyReader.getValue("error.require", "Product Quantity"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("productQuantity"))) {
			request.setAttribute("productQuantity", "Product Quantity must contains Numeric value only");
			pass = false;
		}

		System.out.println(request.getParameter("tDate"));
		System.out.println("validate end ");
		System.out.println(pass);
		return pass;

	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		CartDTO dto = new CartDTO();

		System.out.println(request.getParameter("dob"));

		dto.setId(DataUtility.getLong(request.getParameter("id")));

		dto.setCustomerName(DataUtility.getString(request.getParameter("customerName")));

		dto.setProductName(DataUtility.getString(request.getParameter("product")));
		dto.setTransactionDate(DataUtility.getDate(request.getParameter("tDate")));

		dto.setProductQuantity(DataUtility.getInt(request.getParameter("productQuantity")));

		populateBean(dto, request);

		System.out.println(request.getParameter("tDate") + "....Date..." + dto.getTransactionDate());
		log.debug("UserRegistrationCtl Method populatedto Ended");

		return dto;

	}

	/**
	 * Contain Display Logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("CartCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		CartModelInt model = ModelFactory.getInstance().getCartModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			System.out.println("in id > 0  condition");
			CartDTO dto;
			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Contain Submit Logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String op = DataUtility.getString(request.getParameter("operation"));
		System.out
				.println("-------------------------------------------------------------------------dopost run-------");
		// get model
		CartModelInt model = ModelFactory.getInstance().getCartModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			CartDTO dto = (CartDTO) populateDTO(request);
			System.out.println(" in do post method jkjjkjk++++++++" + dto.getId());
			try {
				if (id > 0) {
					model.update(dto);

					ServletUtility.setSuccessMessage("Data is successfully Update", request);
				} else {

					try {
						model.add(dto);
						ServletUtility.setDto(dto, request);
						ServletUtility.setSuccessMessage("Data is successfully saved", request);
					} catch (ApplicationException e) {
						log.error(e);
						ServletUtility.handleException(e, request, response);
						return;
					} catch (DuplicateRecordException e) {
						ServletUtility.setDto(dto, request);
						ServletUtility.setErrorMessage("Data id already exists", request);
					}

				}
				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Data id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			CartDTO dto = (CartDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.CART_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CART_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CART_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("CartCtl Method doPostEnded");
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.CART_VIEW;
	}

}