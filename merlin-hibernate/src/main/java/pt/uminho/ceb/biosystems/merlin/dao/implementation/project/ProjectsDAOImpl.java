package pt.uminho.ceb.biosystems.merlin.dao.implementation.project;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.project.IProjectsDAO;
import pt.uminho.ceb.biosystems.merlin.entities.project.Projects;


public class ProjectsDAOImpl extends GenericDaoImpl<Projects> implements IProjectsDAO{

	public ProjectsDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, Projects.class);
		
	}

	public void addProject(Projects Project) {
		super.save(Project);
		
	}

	public void addProjects(List<Projects> Projects) {
		for (Projects Project: Projects) {
			this.addProject(Project);
		}
		
	}

	public List<Projects> getAllProjects() {
		return super.findAll();
	}

	public Projects getProject(Integer id) {
		return super.findById(id);
	}

	public void removeProject(Projects Project) {
		super.delete(Project);
		
	}

	public void removeProjects(List<Projects> Projects) {
		for (Projects Project: Projects) {
			this.removeProject(Project);
		}
		
	}

	public void updateProjects(List<Projects> Projects) {
		for (Projects Project: Projects) {
			this.update(Project);
		}
		
	}

	public void updateProject(Projects Project) {
		super.update(Project);
		
	}


}
