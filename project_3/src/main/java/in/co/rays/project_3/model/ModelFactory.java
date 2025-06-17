package in.co.rays.project_3.model;

import java.util.HashMap;  
import java.util.ResourceBundle;

/**
 * ModelFactory decides which model implementation run
 * 
 * @author YOGESH SEN
 */
//MAKE CLASS FINAL SO THAT CHILD CAN'T BE CREATED
public final class ModelFactory {

	// MAKE DEFAULT CONSTRUCTOR PRIVATE SO THAT NO ONE OTHER CLASS CAN INSTANTIATE
	// THE SINGLETON CLASS
	private ModelFactory() {

	}

	// MAKE PRIVATE STATIC SELF TYPE VARIABLE SO THAT IT WIL HAVE ONLY ONE COPY IN
	// THEIR LIFE TIME
	private static ModelFactory modelFactory = null ;

	// MAKE PUBLIC STATIC SELFTYPE GETINSTANCE METHOD SO THAT IF WILL RETURN THE
	// INSTANCE OF SAME SINGLETON CLASS
	public static ModelFactory getInstance() {
		if (modelFactory == null) {

			modelFactory = new ModelFactory();
		}

		return modelFactory;
	}

	private static ResourceBundle rb = ResourceBundle.getBundle("in/co/rays/project_3/bundle/system");

	private static final String DATABASE = rb.getString("DATABASE");

	private static HashMap modelCache = new HashMap();

	public UserModelInt getUserModel() {

		UserModelInt userModel = (UserModelInt) modelCache.get("userModel");

		if (userModel == null){

			if ("Hibernate".equals(DATABASE)) {
				userModel = new UserModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				userModel = new UserModelJDBCImpl();
			}

		}

		return userModel;
	}
	
	public CartModelInt getCartModel() {

		CartModelInt cartModel = (CartModelInt) modelCache.get("cartModel");
		if (cartModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				cartModel = new CartModelHibImp();
			}
			modelCache.put("cartModel", cartModel);
		}

		return cartModel;
	}

	
	public PurchaseModelInt getPurchaseModel() {

		PurchaseModelInt purchaseModel = (PurchaseModelInt) modelCache.get("purchaseModel");

		if (purchaseModel == null){

			if ("Hibernate".equals(DATABASE)) {
				purchaseModel = new PurchaseModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
				purchaseModel = new PurchaseModelHibImpl();
			}

		}

		return purchaseModel;
	}
	
	public CustomerModelInt getCustomerModel() {

		CustomerModelInt customerModel = (CustomerModelInt) modelCache.get("customerModel");

		if (customerModel == null) {

			if ("Hibernate".equals(DATABASE)) {
				customerModel = new CustomerModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
//				customerModel = new CustomerModelJDBCImpl();
				customerModel = new CustomerModelHibImpl();
			}

		}

		return customerModel;
	}

	public MarksheetModelInt getMarksheetModel() {
		MarksheetModelInt marksheetModel = (MarksheetModelInt) modelCache.get("marksheetModel");
		if (marksheetModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				marksheetModel = new MarksheetModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				marksheetModel = new MarksheetModelJDBCImpl();
			}
			modelCache.put("marksheetModel", marksheetModel);
		}
		return marksheetModel;
	}

	public CollegeModelInt getCollegeModel() {
		CollegeModelInt collegeModel = (CollegeModelInt) modelCache.get("collegeModel");
		if (collegeModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				collegeModel = new CollegeModelHibImp();

			}
			if ("JDBC".equals(DATABASE)) {
				collegeModel = new CollegeModelJDBCImpl();
			}
			modelCache.put("collegeModel", collegeModel);
		}
		return collegeModel;
	}

	public RoleModelInt getRoleModel() {
		RoleModelInt roleModel = (RoleModelInt) modelCache.get("roleModel");
		if (roleModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				roleModel = new RoleModelHibImp();

			}
			if ("JDBC".equals(DATABASE)) {
				roleModel = new RoleModelJDBCImpl();
			}
			modelCache.put("roleModel", roleModel);
		}
		return roleModel;
	}

	public StudentModelInt getStudentModel() {
		StudentModelInt studentModel = (StudentModelInt) modelCache.get("studentModel");
		if (studentModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				studentModel = new StudentModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				studentModel = new StudentModelJDBCImpl();
			}
			modelCache.put("studentModel", studentModel);
		}

		return studentModel;
	}

	public CourseModelInt getCourseModel() {
		CourseModelInt courseModel = (CourseModelInt) modelCache.get("courseModel");
		if (courseModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				courseModel = new CourseModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				courseModel = new CourseModelJDBCImpl();
			}
			modelCache.put("courseModel", courseModel);
		}

		return courseModel;
	}

	public TimetableModelInt getTimetableModel() {

		TimetableModelInt timetableModel = (TimetableModelInt) modelCache.get("timetableModel");

		if (timetableModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				timetableModel = new TimetableModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				timetableModel = new TimetableModelJDBCImpl();
			}
			modelCache.put("timetableModel", timetableModel);
		}

		return timetableModel;
	}

	public SubjectModelInt getSubjectModel() {
		SubjectModelInt subjectModel = (SubjectModelInt) modelCache.get("subjectModel");
		if (subjectModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				subjectModel = new SubjectModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				subjectModel = new SubjectModelJDBCImpl();
			}
			modelCache.put("subjectModel", subjectModel);
		}

		return subjectModel;
	}

	public FacultyModelInt getFacultyModel() {
		FacultyModelInt facultyModel = (FacultyModelInt) modelCache.get("facultyModel");
		if (facultyModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				facultyModel = new FacultyModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				facultyModel = new FacultyModelJDBCImpl();
			}
			modelCache.put("facultyModel", facultyModel);
		}

		return facultyModel;
	}



}
