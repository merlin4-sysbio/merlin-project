package pt.uminho.ceb.biosystems.merlin.dao.interfaces.project;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.project.Projects;

public interface IProjectsDAO extends IGenericDao<Projects>{
	
	public void addProject(Projects project); //adiciona um projeto
	
	public void addProjects(List<Projects> projects); //adiciona projetos
	
	public List<Projects> getAllProjects(); //retorna os projetos
	
	public Projects getProject(Integer id); //retorna um projeto
	
	public void removeProject(Projects project); //remove um projeto
	
	public void removeProjects(List<Projects> project); //remove projetos
	
	public void updateProjects(List<Projects> projects); //update de um projeto
	
	public void updateProject(Projects project);

}
